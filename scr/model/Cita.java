package model;

import excepciones.InvalidDataException;
import excepciones.InvariantViolationException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.Normalizer;

/**
 * Clase que representa una cita médica.
 * Contiene información de ID, fecha, hora, lugar, paciente y doctor.
 * @version 2.0 (Modificado: ID de Cita añadido)
 * @author Juan Cogua
 * @author Andres Rojas
 */
public class Cita {
    private String id; // NUEVO CAMPO: ID de la Cita (Ej: C001)
    private Date date;
    private Date time; 
    private String location;
    private Paciente paciente;
    private String doctor; 
    private String estado = "Pendiente";
    private boolean confirmada = false;
    private boolean cancelada = false;

    /**
     * Constructor para crear una nueva cita.
     * @param id ID único de la cita
     * @param date Fecha de la cita
     * @param time Hora de la cita
     * @param location Ubicación de la cita
     * @param paciente Paciente asociado a la cita
     * @param doctor Doctor/Especialista de la cita
     */
    public Cita(String id, Date date, Date time, String location, Paciente paciente, String doctor) throws InvalidDataException {
        if (id == null || id.trim().isEmpty()) {
            throw new InvalidDataException("El ID de la cita no puede ser nulo o vacío.");
        }
        if (date == null || time == null) {
            throw new InvalidDataException("La fecha y hora de la cita no pueden ser null.");
        }
        if (location == null || location.trim().isEmpty()) {
            throw new InvalidDataException("La ubicación de la cita no puede estar vacía.");
        }
        if (paciente == null) {
            throw new InvalidDataException("La cita debe estar asociada a un paciente.");
        }
        if (doctor == null || doctor.trim().isEmpty()) {
            throw new InvalidDataException("El nombre del doctor no puede estar vacío.");
        }

        this.id = id;
        this.date = date;
        this.time = time;
        this.location = location;
        this.paciente = paciente;
        this.doctor = doctor;
    }

    // --- Getters ---
    public String getId() { return id; }
    public Date getDate() { return date; }
    public Date getTime() { return time; }
    public String getLocation() { return location; }
    public Paciente getPaciente() { return paciente; }
    public String getDoctor() { return doctor; }
    public String getEstado() { return estado; }
    public boolean isConfirmada() { return confirmada; }
    public boolean isCancelada() { return cancelada; }

    // --- Setters ---
    public void setDate(Date date) { this.date = date; }
    public void setTime(Date time) { this.time = time; }
    public void setLocation(String location) { this.location = location; }
    public void setDoctor(String doctor) { this.doctor = doctor; }
    public void setEstado(String estado) { this.estado = estado; }

    // --- Lógica ---
    public void confirmar() { this.confirmada = true; this.estado = "Aprobada"; }
    public void cancelar() { this.cancelada = true; this.estado = "Cancelada"; }

    // utilidad mínima para normalizar tildes comunes usadas en tests
    private String aplicarTildesComunes(String s) {
        if (s == null) return null;
        // Devolver la cadena tal cual: los tests esperan acentos/puntos exactos.
        return s;
    }

    /**
     * Genera un resumen legible de la cita con el formato: 
     * ID;Fecha;Hora;Lugar;Doctor [ESTADO]
     */
    public String resumen() {
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm");

        String estadoStr = "";
        if (estado != null && !estado.equalsIgnoreCase("Pendiente")) {
            estadoStr = " | Estado: " + estado;
        }

        String loc = aplicarTildesComunes(location);
        String doc = aplicarTildesComunes(doctor);

        return String.format("%s;%s;%s;%s;%s%s",
            id != null ? id : "N/A",
            formatoFecha.format(date),
            formatoHora.format(time),
            loc != null ? loc : "N/A",
            doc != null ? doc : "N/A",
            estadoStr);
    }

    /**
     * Convierte la cita al formato de archivo: ID;Fecha;Hora;Lugar;ID_Paciente;Doctor;Estado
     */
    public String toArchivo() {
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm");

        String loc = aplicarTildesComunes(location);
        String doc = aplicarTildesComunes(doctor);
        String idPaciente = paciente != null ? paciente.getId() : "";

        return String.join(";",
                id != null ? id : "",
                formatoFecha.format(date),
                formatoHora.format(time),
                loc != null ? loc : "",
                idPaciente,
                doc != null ? doc : "",
                estado != null ? estado : "");
    }

    /**
     * Verifica invariantes de la cita.
     */
    public void checkInvariant() {
        if (id == null || id.trim().isEmpty()) throw new InvariantViolationException("ID inválido");
        if (date == null || time == null) throw new InvariantViolationException("Fecha/hora inválida");
        if (location == null || location.trim().isEmpty()) throw new InvariantViolationException("Lugar inválido");
        if (paciente == null) throw new InvariantViolationException("Paciente null");
        if (doctor == null || doctor.trim().isEmpty()) throw new InvariantViolationException("Doctor inválido");
    }
}