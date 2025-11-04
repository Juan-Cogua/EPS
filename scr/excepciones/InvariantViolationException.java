package excepciones;

/**
 * Excepción que indica que una invariante de clase ha sido violada.
 */
public class InvariantViolationException extends RuntimeException {
    public InvariantViolationException() { super(); }
    public InvariantViolationException(String message) { super(message); }
    public InvariantViolationException(String message, Throwable cause) { super(message, cause); }
}
