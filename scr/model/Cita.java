package model;
import java.util.Date;

/**
 * Clase que representa una cita médica.
 * Contiene información de fecha, hora, lugar, paciente
 * y permite confirmar, cancelar y reprogramar la cita.
 * 
 * @author Juan Cogua
 * @author Andres Rojas
 * @version 1.0 (Mejorado con manejo de excepciones específicas)
 */
public class Cita {
    private Date date;
    private Date time; 
    private String location;
    private Paciente paciente;
    private boolean confirmada = false;
    private boolean cancelada = false;

    /**
     * Constructor para crear una nueva cita.
     * 
     * @param date Fecha de la cita
     * @param time Hora de la cita
     * @param location Ubicación de la cita
     * @param paciente Paciente asociado a la cita
     * @throws NullPointerException si date, time o paciente son null
     * @throws IllegalArgumentException si location está vacía
     */
    public Cita(Date date, Date time, String location, Paciente paciente) {
        if (date == null) {
            throw new NullPointerException("La fecha de la cita no puede ser null.");
        }
        if (time == null) {
            throw new NullPointerException("La hora de la cita no puede ser null.");
        }
        if (location == null || location.trim().isEmpty()) {
            throw new IllegalArgumentException("La ubicación de la cita no puede estar vacía.");
        }
        if (paciente == null) {
            throw new NullPointerException("El paciente de la cita no puede ser null.");
        }
        
        this.date = date;
        this.time = time;
        this.location = location;
        this.paciente = paciente;
    }

    public Date getDate() {
        return date;
    }

    /**
     * Establece la fecha de la cita.
     * 
     * @param date Nueva fecha de la cita
     * @throws NullPointerException si date es null
     */
    public void setDate(Date date) {
        if (date == null) {
            throw new NullPointerException("La fecha de la cita no puede ser null.");
        }
        this.date = date;
    }

    public Date getTime() {
        return time;
    }

    /**
     * Establece la hora de la cita.
     * 
     * @param time Nueva hora de la cita
     * @throws NullPointerException si time es null
     */
    public void setTime(Date time) {
        if (time == null) {
            throw new NullPointerException("La hora de la cita no puede ser null.");
        }
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    /**
     * Establece la ubicación de la cita.
     * 
     * @param location Nueva ubicación de la cita
     * @throws IllegalArgumentException si location está vacía
     */
    public void setLocation(String location) {
        if (location == null || location.trim().isEmpty()) {
            throw new IllegalArgumentException("La ubicación de la cita no puede estar vacía.");
        }
        this.location = location;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    /**
     * Establece el paciente de la cita.
     * 
     * @param paciente Nuevo paciente de la cita
     * @throws NullPointerException si paciente es null
     */
    public void setPaciente(Paciente paciente) {
        if (paciente == null) {
            throw new NullPointerException("El paciente de la cita no puede ser null.");
        }
        this.paciente = paciente;
    }

    /**
     * Confirma la cita.
     * Cambia el estado a confirmada y desactiva cancelada.
     * 
     * @throws IllegalStateException si la cita ya está cancelada
     */
    public void confirmarCita() {
        if (cancelada) {
            throw new IllegalStateException("No se puede confirmar una cita que ya está cancelada.");
        }
        
        this.confirmada = true;
        System.out.println("La cita ha sido confirmada para el paciente " + paciente.getName());
    }

    /**
     * Cancela la cita.
     * Cambia el estado a cancelada y desactiva confirmada.
     * 
     * @throws IllegalStateException si la cita ya está cancelada
     */
    public void cancelarCita() {
        if (cancelada) {
            throw new IllegalStateException("La cita ya está cancelada.");
        }
        
        this.cancelada = true;
        this.confirmada = false;
        System.out.println("La cita ha sido cancelada para el paciente " + paciente.getName());
    }

    public boolean isConfirmada() {
        return confirmada;
    }

    public boolean isCancelada() {
        return cancelada;
    }

    /**
     * Reprograma la cita con nueva fecha y hora.
     * 
     * @param nuevaFecha Nueva fecha para la cita
     * @param nuevaHora Nueva hora para la cita
     * @throws NullPointerException si nuevaFecha o nuevaHora son null
     * @throws IllegalStateException si la cita está cancelada
     */
    public void reprogramarCita(Date nuevaFecha, Date nuevaHora) {
        if (nuevaFecha == null) {
            throw new NullPointerException("La nueva fecha no puede ser null.");
        }
        if (nuevaHora == null) {
            throw new NullPointerException("La nueva hora no puede ser null.");
        }
        if (cancelada) {
            throw new IllegalStateException("No se puede reprogramar una cita cancelada.");
        }
        
        this.date = nuevaFecha;
        this.time = nuevaHora;
        System.out.println("La cita ha sido reprogramada para: " + nuevaFecha + " a las " + nuevaHora);
    }

    /**
     * Genera un resumen de la cita con información básica.
     * 
     * @return Cadena con el resumen de la cita
     */
    public String resumen() {
        return "Cita de " + paciente.getName() + " en " + location + 
               " el " + date + " a las " + time +
               (confirmada ? " [CONFIRMADA]" : "") +
               (cancelada ? " [CANCELADA]" : "");
    }
}
