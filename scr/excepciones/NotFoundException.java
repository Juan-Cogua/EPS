
package excepciones;

/**
 * Excepción lanzada para indicar que un recurso requerido no ha sido encontrado
 * en el sistema (por ejemplo, Paciente o Donante).
 *
 * @author Juan Cogua
 * @version 2.0
 */
public class NotFoundException extends RuntimeException {

    /**
     * Construye una nueva NotFoundException con un mensaje de detalle null.
     */
    public NotFoundException() {
        super();
    }

    /**
     * Construye una nueva NotFoundException con el mensaje de detalle especificado.
     *
     * @param message el mensaje de detalle que describe qué recurso no se pudo encontrar.
     */
    public NotFoundException(String message) {
        super(message);
    }

    /**
     * Construye una nueva NotFoundException con el mensaje de detalle y la causa especificados.
     *
     * @param message el mensaje de detalle.
     * @param cause la causa de la excepción (guardada para su posterior recuperación).
     */
    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}