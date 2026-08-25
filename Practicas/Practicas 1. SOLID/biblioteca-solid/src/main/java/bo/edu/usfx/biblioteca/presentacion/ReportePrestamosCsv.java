package bo.edu.usfx.biblioteca.presentacion;

import java.util.List;

/**
 * Responsable ante UN SOLO actor: Kardex.
 * El formato del reporte (cabecera, separador ';') vive solo aqui.
 * Si Kardex pide Excel en vez de CSV, se toca esta clase.
 */
public class ReportePrestamosCsv {

    private static final String CABECERA = "codigo;titulo;fecha;limite;multa\n";

    public static String generar(List<String> filas) {
        StringBuilder csv = new StringBuilder(CABECERA);
        for (String fila : filas) {
            csv.append(fila).append('\n');
        }
        return csv.toString();
    }
}
