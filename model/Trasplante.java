package model;
/**
 * Representa un trasplante en el sistema EPS.
 *
 * @author Andres Rojas
 * @version 1.0
 */
public class Trasplante {
    private String organType;
    private String donor;
    private String receiver;
    private String rejectionHistory;
    private String rejectionReason;

    /**
     * Constructor de Trasplante.
     *
     * @param organType Tipo de órgano.
     * @param donor Donante.
     * @param receiver Receptor.
     * @param rejectionHistory Historial de rechazo.
     * @param rejectionReason Motivo de rechazo.
     */
    public Trasplante(String organType, String donor, String receiver, String rejectionHistory, String rejectionReason) {
        this.organType = organType;
        this.donor = donor;
        this.receiver = receiver;
        this.rejectionHistory = rejectionHistory;
        this.rejectionReason = rejectionReason;
    }
//getters y setters
    public String getOrganType() {
        return organType;
    }

    public void setOrganType(String organType) {
        this.organType = organType;
    }

    public String getDonor() {
        return donor;
    }

    public void setDonor(String donor) {
        this.donor = donor;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getRejectionHistory() {
        return rejectionHistory;
    }

    public void setRejectionHistory(String rejectionHistory) {
        this.rejectionHistory = rejectionHistory;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }
    
    //Metodos Adicionales   
    /**
     * Valida la compatibilidad entre donante y receptor.
     * @return true si son compatibles, false si no.
     */
    public boolean esCompatible() {
        return donor != null && receiver != null && !donor.equals(receiver);
    }

    /**
     * Registra un rechazo en el trasplante.
     * @param motivo Motivo del rechazo.
     */
    public void registrarRechazo(String motivo) {
        this.rejectionHistory = "Rechazo previo registrado";
        this.rejectionReason = motivo;
    }

    /**
     * Devuelve un resumen del trasplante.
     * @return Resumen como String.
     */
    public String resumen() {
        return "Trasplante de " + organType + 
               " entre donante: " + donor + " y receptor: " + receiver +
               (rejectionReason != null ? ". Motivo de rechazo: " + rejectionReason : "");
    }

    /**
     * Convierte el trasplante a una línea de texto para guardar en archivo.
     * @return Cadena con los datos del trasplante separados por punto y coma.
     */
    public String toArchivo() {
        return organType + ";" + donor + ";" + receiver + ";" + rejectionHistory + ";" + rejectionReason;
    }

    /**
     * Crea un trasplante desde una línea de texto.
     * @param linea Línea de texto con los datos del trasplante.
     * @return Instancia de {@link Trasplante} si el formato es válido, null si hay error.
     */
    public static Trasplante fromArchivo(String linea) {
        String[] partes = linea.split(";");
        if (partes.length != 5) {
            return null;
        }
        return new Trasplante(partes[0], partes[1], partes[2], partes[3], partes[4]);
    }

    /**
     * Representa el trasplante como cadena para mostrar en listas.
     * @return Cadena con resumen del trasplante.
     */
    @Override
    public String toString() {
        return resumen();
    }
}
