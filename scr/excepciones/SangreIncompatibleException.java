package excepciones;

/**
 * Excepción lanzada para indicar que se ha intentado realizar una acción (como
 * una transfusión o donación) donde los grupos sanguíneos son **incompatibles**
 * de acuerdo con las reglas médicas o de negocio.
 *
 * @author Juan Cogua
 * @version 2.0
 */
public class SangreIncompatibleException extends Exception {

    /**
     * Construye una nueva SangreIncompatibleException con el mensaje de detalle especificado.
     *
     * @param message el mensaje de detalle que explica la incompatibilidad de la sangre.
     */
    public SangreIncompatibleException(String message) {
        super(message);
    }
}
