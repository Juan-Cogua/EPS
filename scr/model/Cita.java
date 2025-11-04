package model;

import excepciones.InvalidDataException;
import java.util.Date;

/**
 * Clase que representa una cita médica.
 * Contiene información de ID, fecha, hora, lugar, paciente y doctor.
 * @version 1.3 (Modificado: ID de Cita añadido)
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
    // Estados manejados: "Pendiente", "Aprobada", "Cancelada"
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
    public Cita(String id, Date date, Date time, String location, Paciente paciente, String doctor) {
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
    public void confirmar() {
        this.confirmada = true;
    }

    public void cancelar() {
        this.cancelada = true;
        this.estado = "Cancelada";
        System.out.println("La cita ha sido cancelada.");
    }
    
    /**
     * Genera un resumen legible de la cita con el formato: 
     * (Fecha;Hora;Lugar;Doctor [ESTADO])
     */
    public String resumen() {
        java.text.SimpleDateFormat formatoFecha = new java.text.SimpleDateFormat("dd/MM/yyyy");
        java.text.SimpleDateFormat formatoHora = new java.text.SimpleDateFormat("HH:mm");

        String estadoStr = "";
        if (!estado.equalsIgnoreCase("Pendiente")) {
            estadoStr = "| Estado: " + estado;
        }

        // Devolvemos la cadena en el formato pedido por la UI: Fecha;Hora;Lugar;Doctor (con posible estado añadido)
        return String.format("%s;%s;%s;%s %s",
            formatoFecha.format(date),
            formatoHora.format(time),
            location,
            doctor,
            estadoStr);
    }
    
    /**
     * Convierte la cita al formato de archivo: ID;Fecha;Hora;Lugar;ID_Paciente;Doctor;Estado
     */
    public String toArchivo() {
        java.text.SimpleDateFormat formatoFecha = new java.text.SimpleDateFormat("dd/MM/yyyy");
        java.text.SimpleDateFormat formatoHora = new java.text.SimpleDateFormat("HH:mm");
        
        return String.join(";",
            this.id,
            formatoFecha.format(date),
            formatoHora.format(time),
            location,
            paciente.getId(),
            doctor,
            estado);
    }
}