package excepciones;

/**
 * Excepción que indica que una **invariante de clase** o una condición interna fundamental
 * del sistema ha sido violada.
 * Esta excepción es típicamente lanzada cuando el estado interno del objeto
 * es inconsistente, lo que sugiere un error de programación.
 *
 * @author Juan Cogua
 * @version 2.0
 */
public class InvariantViolationException extends RuntimeException {

    /**
     * Construye una nueva InvariantViolationException con un mensaje de detalle null.
     */
    public InvariantViolationException() {
        super();
    }

    /**
     * Construye una nueva InvariantViolationException con el mensaje de detalle especificado.
     *
     * @param message el mensaje de detalle que describe qué invariante fue violada.
     */
    public InvariantViolationException(String message) {
        super(message);
    }

    /**
     * Construye una nueva InvariantViolationException con el mensaje de detalle y la causa especificados.
     *
     * @param message el mensaje de detalle.
     * @param cause la causa de la excepción (guardada para su posterior recuperación).
     */
    public InvariantViolationException(String message, Throwable cause) {
        super(message, cause);
    }
}