package excepciones;

/**
 * Excepción general del dominio que indica que se recibió o se produjo
 * un dato inválido según las reglas de negocio o de validación.
 *
 * <pSe utiliza para señalar errores de validación que deben ser manejados
 * por el código llamante (por ejemplo, en UI o loaders).
 *
 * @author Juan Cogua
 * @version 2.0
 * 
 */
public class InvalidDataException extends Exception {

    /**
     * Construye una nueva InvalidDataException con mensaje nulo.
     */
    public InvalidDataException() {
        super();
    }

    /**
     * Construye una nueva InvalidDataException con el mensaje de detalle especificado.
     *
     * @param message el mensaje de detalle que describe la causa del error
     */
    public InvalidDataException(String message) {
        super(message);
    }

    /**
     * Construye una nueva InvalidDataException con mensaje y causa especificados.
     *
     * @param message el mensaje de detalle
     * @param cause la causa original de la excepción
     */
    public InvalidDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
