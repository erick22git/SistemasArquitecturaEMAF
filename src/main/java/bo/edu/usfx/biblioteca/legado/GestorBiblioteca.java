package bo.edu.usfx.biblioteca.legado;

import bo.edu.usfx.biblioteca.infraestructura.NotificadorPrestamos;
import bo.edu.usfx.biblioteca.infraestructura.RepositorioPrestamos;
import bo.edu.usfx.biblioteca.presentacion.ComprobantePrestamo;
import bo.edu.usfx.biblioteca.presentacion.ReportePrestamosCsv;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * =====================================================================
 *  PASO 1 (SRP) APLICADO
 * =====================================================================
 *
 *  GestorBiblioteca ya NO calcula, persiste, notifica y formatea todo
 *  el mismo. Ahora ORQUESTA: decide la politica de negocio (todavia
 *  con el if/else -- eso lo resuelve el Paso 2, OCP) y delega cada
 *  responsabilidad restante a un colaborador que responde ante un
 *  solo actor:
 *
 *    - RepositorioPrestamos   -> Direccion de TI (persistencia)
 *    - NotificadorPrestamos   -> Comunicacion (avisos)
 *    - ComprobantePrestamo    -> Kardex (formato del recibo)
 *    - ReportePrestamosCsv    -> Kardex (formato del reporte)
 *
 *  Nota: todavia construye con "new" su ConexionMySQL y su
 *  ServidorCorreoSMTP. Esa violacion de DIP se corrige en el Paso 5,
 *  no en este paso -- por ahora el objetivo es SOLO separar actores.
 * =====================================================================
 */
public class GestorBiblioteca {

    private final ConexionMySQL conexion =
            new ConexionMySQL("jdbc:mysql://10.0.0.7:3306/biblioteca", "root", "usfx2026");

    private final ServidorCorreoSMTP correo =
            new ServidorCorreoSMTP("smtp.usfx.bo", 587);

    private final RepositorioPrestamos repositorio = new RepositorioPrestamos(conexion);
    private final NotificadorPrestamos notificador = new NotificadorPrestamos(correo);

    private final List<Prestamo> prestamos = new ArrayList<>();

    // -----------------------------------------------------------------
    // 1. REGISTRAR UN PRESTAMO
    // -----------------------------------------------------------------
    public String registrarPrestamo(Usuario usuario, Libro libro, LocalDate hoy) {

        // --- politica de prestamo segun el tipo de usuario ---
        // (el if/else sigue aca a proposito: se resuelve en el Paso 2, OCP)
        int diasPermitidos;
        int maximoLibros;
        if ("ESTUDIANTE".equals(usuario.getTipo())) {
            diasPermitidos = 7;
            maximoLibros = 3;
        } else if ("DOCENTE".equals(usuario.getTipo())) {
            diasPermitidos = 15;
            maximoLibros = 5;
        } else if ("ADMINISTRATIVO".equals(usuario.getTipo())) {
            diasPermitidos = 10;
            maximoLibros = 2;
        } else if ("EXTERNO".equals(usuario.getTipo())) {
            diasPermitidos = 3;
            maximoLibros = 1;
        } else {
            throw new IllegalArgumentException("Tipo de usuario desconocido: " + usuario.getTipo());
        }

        // --- validaciones ---
        if (!libro.isDisponible()) {
            throw new IllegalStateException("El ejemplar " + libro.getSignatura() + " no esta disponible");
        }
        long activos = prestamos.stream()
                .filter(p -> p.getUsuario().getCodigo().equals(usuario.getCodigo()))
                .filter(Prestamo::estaActivo)
                .count();
        if (activos >= maximoLibros) {
            throw new IllegalStateException("El usuario alcanzo su limite de " + maximoLibros + " ejemplares");
        }

        // --- registro en memoria ---
        LocalDate limite = hoy.plusDays(diasPermitidos);
        Prestamo prestamo = new Prestamo(usuario, libro, hoy, limite);
        prestamos.add(prestamo);
        libro.setDisponible(false);

        // --- persistencia: delegada (antes: dos conexion.ejecutar() aca mismo) ---
        repositorio.guardarPrestamo(usuario, libro, hoy, limite);

        // --- notificacion: delegada (antes: correo.enviar() aca mismo) ---
        notificador.notificarPrestamoRegistrado(usuario, limite);

        // --- comprobante: delegado (antes: el String armado aca mismo) ---
        return ComprobantePrestamo.imprimir(usuario, libro, limite);
    }

    // -----------------------------------------------------------------
    // 2. CALCULAR LA MULTA POR RETRASO
    // -----------------------------------------------------------------
    public double calcularMulta(Prestamo prestamo, LocalDate hoy) {

        long diasRetraso = ChronoUnit.DAYS.between(prestamo.getFechaLimite(), hoy);
        if (diasRetraso <= 0) {
            return 0.0;
        }

        String tipo = prestamo.getUsuario().getTipo();
        double multa;
        if ("ESTUDIANTE".equals(tipo)) {
            multa = diasRetraso * 2.0;
        } else if ("DOCENTE".equals(tipo)) {
            multa = diasRetraso * 1.0;
        } else if ("ADMINISTRATIVO".equals(tipo)) {
            multa = diasRetraso * 1.5;
        } else if ("EXTERNO".equals(tipo)) {
            multa = diasRetraso * 5.0;
        } else {
            multa = diasRetraso * 3.0;
        }

        if (multa > 200.0) {
            multa = 200.0;
        }
        return multa;
    }

    // -----------------------------------------------------------------
    // 3. REGISTRAR LA DEVOLUCION
    // -----------------------------------------------------------------
    public String registrarDevolucion(Prestamo prestamo, LocalDate hoy) {
        prestamo.setFechaDevolucion(hoy);
        prestamo.getLibro().setDisponible(true);

        double multa = calcularMulta(prestamo, hoy);

        // --- persistencia: delegada ---
        repositorio.registrarDevolucion(prestamo, hoy, multa);

        // --- notificacion: delegada ---
        if (multa > 0) {
            notificador.notificarMulta(prestamo.getUsuario(), multa);
        }

        return "Devolucion registrada. Multa: Bs " + multa;
    }

    // -----------------------------------------------------------------
    // 4. REPORTE MENSUAL (CSV)
    // -----------------------------------------------------------------
    public String generarReporteMensual(int mes, int anio) {
        repositorio.consultarPrestamosDelMes(mes, anio);

        List<String> filas = new ArrayList<>();
        for (Prestamo p : prestamos) {
            if (p.getFechaPrestamo().getMonthValue() == mes && p.getFechaPrestamo().getYear() == anio) {
                filas.add(p.getUsuario().getCodigo() + ";"
                        + p.getLibro().getTitulo() + ";"
                        + p.getFechaPrestamo() + ";"
                        + p.getFechaLimite() + ";"
                        + calcularMulta(p, LocalDate.now()));
            }
        }
        System.out.println("[FileWriter] C:/reportes/biblioteca_" + anio + "_" + mes + ".csv");

        // --- formato del reporte: delegado ---
        return ReportePrestamosCsv.generar(filas);
    }

    // -----------------------------------------------------------------
    // 5. RECORDATORIOS
    // -----------------------------------------------------------------
    public int enviarRecordatorios(LocalDate hoy) {
        int enviados = 0;
        for (Prestamo p : prestamos) {
            if (p.estaActivo() && p.getFechaLimite().minusDays(1).equals(hoy)) {
                notificador.notificarRecordatorio(p.getUsuario(), p.getLibro());
                enviados++;
            }
        }
        return enviados;
    }

    public List<Prestamo> getPrestamos() { return prestamos; }
    public ConexionMySQL getConexion() { return conexion; }
    public ServidorCorreoSMTP getCorreo() { return correo; }
}
