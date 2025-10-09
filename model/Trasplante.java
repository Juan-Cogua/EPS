package model;

/**
 * Clase que representa un trasplante dentro del sistema.
 * Contiene información sobre el órgano trasplantado, el donante, el receptor
 * y posibles rechazos asociados al procedimiento.
 * 
 * @author Juan Cogua
 * @author Andres Rojas
 * @version 1.0
 */
public class Trasplante {

    /** Tipo de órgano trasplantado. */
    private String organType;

    /** Donante del órgano. */
    private String donor;

    /** Receptor del órgano. */
    private String receiver;

    /** Historial de rechazos del trasplante. */
    private String rejectionHistory;

    /** Motivo del rechazo, si lo hay. */
    private String rejectionReason;

    /**
     * Constructor de la clase Trasplante.
     *
     * @param organType Tipo de órgano trasplantado.
     * @param donor Nombre del donante.
     * @param receiver Nombre del receptor.
     * @param rejectionHistory Historial de rechazos previos.
     * @param rejectionReason Motivo de rechazo.
     */
    public Trasplante(String organType, String donor, String receiver, String rejectionHistory, String rejectionReason) {
        this.organType = organType;
        this.donor = donor;
        this.receiver = receiver;
        this.rejectionHistory = rejectionHistory;
        this.rejectionReason = rejectionReason;
    }

    /**
     * Obtiene el tipo de órgano trasplantado.
     *
     * @return Tipo de órgano.
     */
    public String getOrganType() {
        return organType;
    }

    /**
     * Establece el tipo de órgano trasplantado.
     *
     * @param organType Tipo de órgano.
     */
    public void setOrganType(String organType) {
        this.organType = organType;
    }

    /**
     * Obtiene el nombre del donante.
     *
     * @return Nombre del donante.
     */
    public String getDonor() {
        return donor;
    }

    /**
     * Establece el nombre del donante.
     *
     * @param donor Nombre del donante.
     */
    public void setDonor(String donor) {
        this.donor = donor;
    }

    /**
     * Obtiene el nombre del receptor.
     *
     * @return Nombre del receptor.
     */
    public String getReceiver() {
        return receiver;
    }

    /**
     * Establece el nombre del receptor.
     *
     * @param receiver Nombre del receptor.
     */
    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    /**
     * Obtiene el historial de rechazos del trasplante.
     *
     * @return Historial de rechazos.
     */
    public String getRejectionHistory() {
        return rejectionHistory;
    }

    /**
     * Establece el historial de rechazos del trasplante.
     *
     * @param rejectionHistory Historial de rechazos.
     */
    public void setRejectionHistory(String rejectionHistory) {
        this.rejectionHistory = rejectionHistory;
    }

    /**
     * Obtiene el motivo del rechazo, si existe.
     *
     * @return Motivo del rechazo.
     */
    public String getRejectionReason() {
        return rejectionReason;
    }

    /**
     * Establece el motivo del rechazo.
     *
     * @param rejectionReason Motivo del rechazo.
     */
    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    /**
     * Verifica si el trasplante es compatible.
     *
     * @return true si el donante y receptor son distintos, false en caso contrario.
     */
    public boolean esCompatible() {
        return donor != null && receiver != null && !donor.equals(receiver);
    }

    /**
     * Registra un rechazo del trasplante con su motivo.
     *
     * @param motivo Motivo del rechazo.
     */
    public void registrarRechazo(String motivo) {
        this.rejectionHistory = "Rechazo previo registrado";
        this.rejectionReason = motivo;
    }

    /**
     * Genera un resumen con la información del trasplante.
     *
     * @return Cadena con el resumen del trasplante.
     */
    public String resumen() {
        return "Trasplante de " + organType + 
               " entre donante: " + donor + " y receptor: " + receiver +
               (rejectionReason != null ? ". Motivo de rechazo: " + rejectionReason : "");
    }
}
