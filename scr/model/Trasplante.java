package model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Clase que representa un trasplante dentro del sistema.
 * Contiene información sobre el órgano trasplantado, el donante, el receptor,
 * fecha del procedimiento y posibles rechazos asociados.
 * 
 * @author Juan
 * @author Andres
 * @version 1.1 (Actualizado con fecha y manejo de archivo completo)
 */
public class Trasplante {

    private String organType;
    private Donante donor;
    private Paciente receiver;
    private String rejectionHistory;
    private String rejectionReason;
    private Date fecha;

    /**
     * Constructor principal de la clase Trasplante.
     *
     * @param organType Tipo de órgano trasplantado.
     * @param donor Donante que participa en el trasplante.
     * @param receiver Paciente receptor.
     * @param rejectionHistory Estado del trasplante ("Aprobado" o "Rechazado").
     * @param rejectionReason Motivo del rechazo (si aplica).
     * @param fecha Fecha del trasplante.
     */
    public Trasplante(String organType, Donante donor, Paciente receiver,
                      String rejectionHistory, String rejectionReason, Date fecha) {

        if (organType == null || organType.trim().isEmpty())
            throw new IllegalArgumentException("El tipo de órgano no puede estar vacío.");
        if (donor == null)
            throw new NullPointerException("El donante no puede ser null.");
        if (receiver == null)
            throw new NullPointerException("El receptor no puede ser null.");
        if (fecha == null)
            fecha = new Date();

        this.organType = organType;
        this.donor = donor;
        this.receiver = receiver;
        this.rejectionHistory = rejectionHistory != null ? rejectionHistory : "";
        this.rejectionReason = rejectionReason != null ? rejectionReason : "";
        this.fecha = fecha;
    }

    // --- Getters y Setters ---
    public String getOrganType() { return organType; }
    public Donante getDonor() { return donor; }
    public Paciente getReceiver() { return receiver; }
    public String getRejectionHistory() { return rejectionHistory; }
    public String getRejectionReason() { return rejectionReason; }
    public Date getFecha() { return fecha; }

    public void setOrganType(String organType) {
        if (organType == null || organType.trim().isEmpty())
            throw new IllegalArgumentException("El tipo de órgano no puede estar vacío.");
        this.organType = organType;
    }

    public void setRejectionHistory(String rejectionHistory) {
        this.rejectionHistory = rejectionHistory != null ? rejectionHistory : "";
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason != null ? rejectionReason : "";
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha != null ? fecha : new Date();
    }

    /**
     * Verifica si el trasplante es compatible en base al tipo de sangre.
     *
     * @return true si los tipos de sangre son compatibles, false en caso contrario.
     */
    public boolean esCompatible() {
        if (donor == null || receiver == null) return false;

        String sangreDonante = donor.getBloodType().toUpperCase();
        String sangreReceptor = receiver.getBloodType().toUpperCase();

        // Compatibilidad básica de sangre
        return sangreDonante.equals(sangreReceptor) ||
               (sangreDonante.equals("O-")) || // universal donante
               (sangreReceptor.equals("AB+"));  // universal receptor
    }

    /**
     * Registra un rechazo del trasplante con su motivo.
     *
     * @param motivo Motivo del rechazo.
     */
    public void registrarRechazo(String motivo) {
        if (motivo == null || motivo.trim().isEmpty())
            throw new IllegalArgumentException("El motivo del rechazo no puede estar vacío.");
        this.rejectionHistory = "Rechazado";
        this.rejectionReason = motivo;
    }

    /**
     * Genera un resumen con la información del trasplante.
     *
     * @return Cadena con el resumen del trasplante.
     */
    public String resumen() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return String.format("Trasplante de %s | Donante: %s | Receptor: %s | Fecha: %s | Estado: %s%s",
                organType,
                donor.getName(),
                receiver.getName(),
                sdf.format(fecha),
                rejectionHistory.isEmpty() ? "Aprobado" : rejectionHistory,
                rejectionReason.isEmpty() ? "" : " (" + rejectionReason + ")");
    }

    // --- Métodos para archivo de texto ---

    /**
     * Convierte el trasplante a una línea de texto para guardar en archivo.
     * @return Cadena con los datos del trasplante separados por punto y coma.
     */
    public String toArchivo() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return organType + ";" +
               donor.getName() + ";" +
               receiver.getName() + ";" +
               rejectionHistory + ";" +
               rejectionReason + ";" +
               sdf.format(fecha);
    }

    /**
     * Crea un trasplante desde una línea de texto.
     * 
     * @param linea Línea de texto con los datos del trasplante.
     * @return Instancia de Trasplante si el formato es válido, null si hay error.
     */
    public static Trasplante fromArchivo(String linea) {
        if (linea == null || linea.trim().isEmpty()) return null;

        try {
            String[] partes = linea.split(";");
            if (partes.length < 6) return null;

            String organ = partes[0];
            String donorName = partes[1];
            String receiverName = partes[2];
            String history = partes[3];
            String reason = partes[4];
            Date fecha;

            try {
                fecha = new SimpleDateFormat("dd/MM/yyyy").parse(partes[5]);
            } catch (ParseException e) {
                fecha = new Date();
            }

            // Buscar los objetos Donante y Paciente por nombre en las listas existentes
            Donante donor = Donante.getDonantes().stream()
                    .filter(d -> d.getName().equalsIgnoreCase(donorName))
                    .findFirst().orElse(null);

            Paciente receiver = Paciente.getPacientes().stream()
                    .filter(p -> p.getName().equalsIgnoreCase(receiverName))
                    .findFirst().orElse(null);

            if (donor == null || receiver == null) return null;

            return new Trasplante(organ, donor, receiver, history, reason, fecha);

        } catch (Exception e) {
            System.err.println("Error al procesar trasplante: " + e.getMessage());
            return null;
        }
    }

    @Override
    public String toString() {
        return resumen();
    }
}
