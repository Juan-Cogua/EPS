package model;
public class Trasplante {
    private String organType;
    private String donor;
    private String receiver;
    private String rejectionHistory;
    private String rejectionReason;

    public Trasplante(String organType, String donor, String receiver, String rejectionHistory, String rejectionReason) {
        this.organType = organType;
        this.donor = donor;
        this.receiver = receiver;
        this.rejectionHistory = rejectionHistory;
        this.rejectionReason = rejectionReason;
    }

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
    //validar compatibilidad
    public boolean esCompatible() {
        return donor != null && receiver != null && !donor.equals(receiver);
    }
    //registrar rechazo
    public void registrarRechazo(String motivo) {
        this.rejectionHistory = "Rechazo previo registrado";
        this.rejectionReason = motivo;
    }
    //resumen de trasplante
    public String resumen() {
        return "Trasplante de " + organType + 
               " entre donante: " + donor + " y receptor: " + receiver +
               (rejectionReason != null ? ". Motivo de rechazo: " + rejectionReason : "");
    }


}
