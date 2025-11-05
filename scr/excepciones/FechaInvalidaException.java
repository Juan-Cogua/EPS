package excepciones;

/**
 * Excepción lanzada para indicar que se ha ingresado una **fecha no válida**
 * o que no cumple con el formato o las restricciones lógicas esperadas.
 *
 * @author Juan Cogua
 * @version 2.0
 */
public class FechaInvalidaException extends Exception {
    
    /**
     * Construye una nueva excepción FechaInvalidaException con el mensaje de detalle especificado.
     *
     * @param message El mensaje de detalle que describe la causa de la fecha inválida (guardado para su posterior recuperación).
     */
    public FechaInvalidaException(String message) {
        super(message);
    }
}
