package excepciones;

/**
 * Excepción lanzada para indicar que la operación de **trasplante no es válida**
 * debido a condiciones médicas, lógicas, o de incompatibilidad que no se cumplen.
 *
 * @author Juan Cogua
 * @version 2.0
 */
public class TrasplanteInvalidoException extends Exception {

    /**
     * Construye una nueva TrasplanteInvalidoException con el mensaje de detalle especificado.
     *
     * @param message el mensaje de detalle que describe la razón por la que el trasplante es inválido.
     */
    public TrasplanteInvalidoException(String message) {
        super(message);
    }
}
