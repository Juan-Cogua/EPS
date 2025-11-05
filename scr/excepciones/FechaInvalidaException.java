package excepciones;

/**
 * Excepción lanzada cuando se ingresa una fecha invalida.
 */
public class FechaInvalidaException extends Exception {
    /**
     * constructor de la clase que crea el mensaje de excepción.
     * @param message
     */
    public FechaInvalidaException(String message) {
        super(message);
    }
}