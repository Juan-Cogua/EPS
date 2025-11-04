package excepciones;

/**
 * Excepción lanzada cuando no se encuentra un recurso requerido (por ejemplo, Paciente o Donante).
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException() { super(); }
    public NotFoundException(String message) { super(message); }
    public NotFoundException(String message, Throwable cause) { super(message, cause); }
}
