package model;

import excepciones.InvalidDataException;
import excepciones.InvariantViolationException;
import java.util.Date;

/**
 * Clase que representa un trasplante dentro del sistema.
 * Contiene información sobre el órgano trasplantado, el donante, el receptor,
 * fecha del procedimiento, estado y posibles rechazos asociados.
 *
 * @author Juan
 * @author Andres
 * @version 1.2 (Añadido campo 'estado' y constructor de 7 argumentos)
 */
public class Trasplante {

    private String id; // Nuevo campo: ID único del trasplante
    private String organType;
    private Donante donor;
    private Paciente receiver;
    private String estado; // Nuevo campo: Aprobado, Pendiente, Rechazado
    private String historialClinico; 
    private String rejectionReason;
    private Date fecha;

    /**
     * Constructor principal de la clase Trasplante (8 argumentos).
     *
     * @param id ID único del trasplante.
     * @param organType Tipo de órgano trasplantado.
     * @param donor Donante que participa en el trasplante.
     * @param receiver Paciente receptor.
     * @param estado Estado del trasplante ("Pendiente", "Aprobado", "Rechazado").
     * @param historialClinico Registro del historial clínico del procedimiento.
     * @param rejectionReason Motivo del rechazo (si aplica).
     * @param fecha Fecha del trasplante.
     */
    public Trasplante(String id, String organType, Donante donor, Paciente receiver,
                        String estado, String historialClinico, String rejectionReason, Date fecha) {

        if (id == null || id.trim().isEmpty())
            throw new InvalidDataException("El ID del trasplante no puede estar vacío.");
        if (organType == null || organType.trim().isEmpty())
            throw new InvalidDataException("El tipo de órgano no puede estar vacío.");
        if (donor == null)
            throw new InvalidDataException("El donante no puede ser null.");
        if (receiver == null)
            throw new InvalidDataException("El receptor no puede ser null.");

        this.id = id;
        this.organType = organType;
        this.donor = donor;
        this.receiver = receiver;
        this.estado = estado;
        this.historialClinico = historialClinico != null ? historialClinico : "";
        this.rejectionReason = rejectionReason != null ? rejectionReason : "";
        this.fecha = fecha;
    }

    // --- Getters y Setters ---
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getOrganType() { return organType; }
    public Donante getDonor() { return donor; }
    public Paciente getReceiver() { return receiver; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getHistorialClinico() { return historialClinico; }
    public String getRejectionReason() { return rejectionReason; }
    public Date getFecha() { return fecha; }


    @Override
    public String toString() {
        return String.format("Trasplante ID: %s | Órgano: %s | Donante: %s (%s) | Receptor: %s (%s) | Estado: %s",
                id, organType, donor.getName(), donor.getId(), receiver.getName(), receiver.getId(), estado);
    }

    /**
     * Genera un resumen legible para el JTextArea.
     */
    // Nota: la presentación (resumen legible) se gestiona en la capa UI (PanelTrasplante).

    /**
     * Verifica invariantes del trasplante.
     */
    public void checkInvariant() {
        if (id == null || id.trim().isEmpty())
            throw new InvariantViolationException("ID del trasplante no puede ser nulo o vacío.");
        if (organType == null || organType.trim().isEmpty())
            throw new InvariantViolationException("Tipo de órgano no puede ser nulo o vacío.");
        if (donor == null) throw new InvariantViolationException("Donante no puede ser null.");
        if (receiver == null) throw new InvariantViolationException("Receptor no puede ser null.");
        if (fecha == null) throw new InvariantViolationException("La fecha del trasplante no puede ser null.");
    }
}