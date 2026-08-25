package bo.edu.usfx.biblioteca.infraestructura;

import bo.edu.usfx.biblioteca.legado.ConexionMySQL;
import bo.edu.usfx.biblioteca.legado.Libro;
import bo.edu.usfx.biblioteca.legado.Prestamo;
import bo.edu.usfx.biblioteca.legado.Usuario;

import java.time.LocalDate;
import java.util.List;

/**
 * Responsable ante UN SOLO actor: Direccion de TI.
 * Aqui vive el conocimiento de como se guarda un prestamo en la base
 * de datos. Nota: todavia CONSTRUYE su comportamiento a partir de una
 * ConexionMySQL concreta -- esa parte (DIP) se corrige en el Paso 5,
 * no en este paso. Por ahora solo estamos separando el "que dato SQL
 * se arma" de las demas responsabilidades de GestorBiblioteca.
 */
public class RepositorioPrestamos {

    private final ConexionMySQL conexion;

    public RepositorioPrestamos(ConexionMySQL conexion) {
        this.conexion = conexion;
    }

    public void guardarPrestamo(Usuario usuario, Libro libro, LocalDate hoy, LocalDate limite) {
        conexion.ejecutar("INSERT INTO prestamo (codigo_usuario, signatura, fecha, limite) VALUES ('"
                + usuario.getCodigo() + "', '" + libro.getSignatura() + "', '" + hoy + "', '" + limite + "')");
        conexion.ejecutar("UPDATE libro SET disponible = 0 WHERE signatura = '" + libro.getSignatura() + "'");
    }

    public void registrarDevolucion(Prestamo prestamo, LocalDate hoy, double multa) {
        conexion.ejecutar("UPDATE prestamo SET devolucion = '" + hoy + "', multa = " + multa
                + " WHERE signatura = '" + prestamo.getLibro().getSignatura() + "'");
        conexion.ejecutar("UPDATE libro SET disponible = 1 WHERE signatura = '"
                + prestamo.getLibro().getSignatura() + "'");
    }

    public List<String> consultarPrestamosDelMes(int mes, int anio) {
        return conexion.consultar("SELECT * FROM prestamo WHERE MONTH(fecha) = " + mes
                + " AND YEAR(fecha) = " + anio);
    }
}
