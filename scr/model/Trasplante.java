package model;
/**
 * Clase que representa un trasplante dentro del sistema.
 * Contiene información sobre el órgano trasplantado, el donante, el receptor
 * y posibles rechazos asociados al procedimiento.
 * 
 * @author Juan Cogua
 * @author Andres Rojas
 * @version 1.0 (Mejorado con manejo de excepciones específicas)
 */
public class Trasplante {

    private String organType;
    private String donor;
    private String receiver;
    private String rejectionHistory;
    private String rejectionReason;

    /**
     * Constructor de la clase Trasplante.
     *
     * @param organType Tipo de órgano trasplantado.
     * @param donor Nombre del donante.
     * @param receiver Nombre del receptor.
     * @param rejectionHistory Historial de rechazos previos.
     * @param rejectionReason Motivo de rechazo.
     * @throws IllegalArgumentException si organType, donor o receiver están vacíos
     */
    public Trasplante(String organType, String donor, String receiver, String rejectionHistory, String rejectionReason) {
        if (organType == null || organType.trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo de órgano no puede estar vacío.");
        }
        if (donor == null || donor.trim().isEmpty()) {
            throw new IllegalArgumentException("El donante no puede estar vacío.");
        }
        if (receiver == null || receiver.trim().isEmpty()) {
            throw new IllegalArgumentException("El receptor no puede estar vacío.");
        }
        
        this.organType = organType;
        this.donor = donor;
        this.receiver = receiver;
        this.rejectionHistory = rejectionHistory != null ? rejectionHistory : "";
        this.rejectionReason = rejectionReason != null ? rejectionReason : "";
    }

    public String getOrganType() {
        return organType;
    }

    public void setOrganType(String organType) {
        if (organType == null || organType.trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo de órgano no puede estar vacío.");
        }
        this.organType = organType;
    }

    public String getDonor() {
        return donor;
    }

    public void setDonor(String donor) {
        if (donor == null || donor.trim().isEmpty()) {
            throw new IllegalArgumentException("El donante no puede estar vacío.");
        }
        this.donor = donor;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        if (receiver == null || receiver.trim().isEmpty()) {
            throw new IllegalArgumentException("El receptor no puede estar vacío.");
        }
        this.receiver = receiver;
    }

    public String getRejectionHistory() {
        return rejectionHistory;
    }

    public void setRejectionHistory(String rejectionHistory) {
        this.rejectionHistory = rejectionHistory != null ? rejectionHistory : "";
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason != null ? rejectionReason : "";
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
     * @throws IllegalArgumentException si el motivo está vacío
     */
    public void registrarRechazo(String motivo) {
        if (motivo == null || motivo.trim().isEmpty()) {
            throw new IllegalArgumentException("El motivo del rechazo no puede estar vacío.");
        }
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
               (rejectionReason != null && !rejectionReason.isEmpty() ? 
                   ". Motivo de rechazo: " + rejectionReason : "");
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
     * 
     * @param linea Línea de texto con los datos del trasplante.
     * @return Instancia de Trasplante si el formato es válido, null si hay error.
     */
    public static Trasplante fromArchivo(String linea) {
        if (linea == null || linea.trim().isEmpty()) {
            System.err.println("La línea está vacía o es null.");
            return null;
        }
        
        try {
            String[] partes = linea.split(";");
            
            // Validar que tenga exactamente 5 campos
            if (partes.length != 5) {
                System.err.println("Formato inválido: se esperaban 5 campos, se encontraron " + partes.length);
                return null;
            }
            
            // Validar que los campos obligatorios no estén vacíos
            if (partes[0].trim().isEmpty()) {
                System.err.println("Error: El tipo de órgano no puede estar vacío.");
                return null;
            }
            if (partes[1].trim().isEmpty()) {
                System.err.println("Error: El donante no puede estar vacío.");
                return null;
            }
            if (partes[2].trim().isEmpty()) {
                System.err.println("Error: El receptor no puede estar vacío.");
                return null;
            }
            
            return new Trasplante(partes[0], partes[1], partes[2], partes[3], partes[4]);
            
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Error: Acceso a índice fuera de rango al procesar la línea.");
            return null;
        } catch (IllegalArgumentException e) {
            System.err.println("Error: Argumento inválido - " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("Error inesperado al parsear trasplante: " + e.getMessage());
            return null;
        }
    }

    @Override
    public String toString() {
        return resumen();
    }
}
