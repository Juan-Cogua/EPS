package excepciones;

/**
 * Excepción lanzada para indicar que un donante no cumple con la restricción mínima de edad
 * para realizar una donación (es menor de 18 años).
 *
 * @author Juan Cogua
 * @version 2.0
 */
public class DonanteMenorEdadException extends Exception {

    /**
     * Construye una nueva excepción DonanteMenorEdadException con un mensaje de detalle null.
     */
    public DonanteMenorEdadException() {
        super();
    }

    /**
     * Construye una nueva excepción DonanteMenorEdadException con el mensaje de detalle especificado.
     *
     * @param message el mensaje de detalle (guardado para su posterior recuperación).
     */
    public DonanteMenorEdadException(String message) {
        super(message);
    }

    /**
     * Construye una nueva excepción DonanteMenorEdadException con el mensaje de detalle y la causa especificados.
     *
     * @param message el mensaje de detalle (guardado para su posterior recuperación).
     * @param cause la causa de la excepción (guardada para su posterior recuperación).
     */
    public DonanteMenorEdadException(String message, Throwable cause) {
        super(message, cause);
    }
}