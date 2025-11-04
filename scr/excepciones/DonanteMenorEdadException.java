package excepciones;

/**
 * Excepción lanzada cuando un donante no cumple con la restricción mínima de edad (menor de 18 años).
 */
public class DonanteMenorEdadException extends InvalidDataException {
    public DonanteMenorEdadException() { super(); }
    public DonanteMenorEdadException(String message) { super(message); }
    public DonanteMenorEdadException(String message, Throwable cause) { super(message, cause); }
}
