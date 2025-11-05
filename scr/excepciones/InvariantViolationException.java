package excepciones;

/**
 * Excepción que indica que una invariante de clase ha sido violada.
 */
public class InvariantViolationException extends RuntimeException {
    /**
     * 
     */
    public InvariantViolationException() { super(); }
    /**
     * @param message
     */
    public InvariantViolationException(String message) { super(message); }
    /**
     * @param message
     * @param cause
     */
    public InvariantViolationException(String message, Throwable cause) { super(message, cause); }
}
