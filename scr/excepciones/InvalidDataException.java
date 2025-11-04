package excepciones;

/**
 * Excepción lanzada cuando se detecta datos inválidos que violan las invariantes del modelo.
 */
public class InvalidDataException extends RuntimeException {
    public InvalidDataException() { super(); }
    public InvalidDataException(String message) { super(message); }
    public InvalidDataException(String message, Throwable cause) { super(message, cause); }
}
