package bo.edu.usfx.biblioteca.presentacion;

import bo.edu.usfx.biblioteca.legado.Libro;
import bo.edu.usfx.biblioteca.legado.Usuario;

import java.time.LocalDate;

/**
 * Responsable ante UN SOLO actor: Kardex.
 * El formato exacto del comprobante impreso vive solo aqui. Si Kardex
 * pide cambiar el formato del recibo, se toca esta clase y ninguna otra.
 */
public class ComprobantePrestamo {

    public static String imprimir(Usuario usuario, Libro libro, LocalDate limite) {
        return "=== BIBLIOTECA USFX ===\n"
             + "Usuario : " + usuario.getNombre() + " (" + usuario.getCodigo() + ")\n"
             + "Titulo  : " + libro.getTitulo() + "\n"
             + "Entrega : " + limite + "\n"
             + "=======================";
    }
}
