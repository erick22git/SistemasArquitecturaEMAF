package bo.edu.usfx.biblioteca.infraestructura;

import bo.edu.usfx.biblioteca.legado.Libro;
import bo.edu.usfx.biblioteca.legado.ServidorCorreoSMTP;
import bo.edu.usfx.biblioteca.legado.Usuario;

import java.time.LocalDate;

/**
 * Responsable ante UN SOLO actor: Comunicacion.
 * Si mañana piden cambiar de correo a WhatsApp, se toca esta clase
 * y ninguna otra.
 */
public class NotificadorPrestamos {

    private final ServidorCorreoSMTP correo;

    public NotificadorPrestamos(ServidorCorreoSMTP correo) {
        this.correo = correo;
    }

    public void notificarPrestamoRegistrado(Usuario usuario, LocalDate limite) {
        correo.enviar(usuario.getCorreo(),
                "Prestamo registrado",
                "Estimado/a " + usuario.getNombre() + ", devuelva el ejemplar hasta el " + limite);
    }

    public void notificarMulta(Usuario usuario, double multa) {
        correo.enviar(usuario.getCorreo(),
                "Multa por retraso",
                "Debe cancelar Bs " + multa + " en caja antes de su proximo prestamo.");
    }

    public void notificarRecordatorio(Usuario usuario, Libro libro) {
        correo.enviar(usuario.getCorreo(),
                "Su prestamo vence manana",
                "Recuerde devolver: " + libro.getTitulo());
    }
}
