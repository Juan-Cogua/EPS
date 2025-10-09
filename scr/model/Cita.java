package scr.model;
import java.util.Date;

/**
 * Clase que representa una cita médica.
 * Contiene información de fecha, hora, lugar, paciente
 * y permite confirmar, cancelar y reprogramar la cita.
 * 
 * @author Juan Cogua
 * @author Andres Rojas
 * @version 1.0
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
     */
    public Cita(Date date, Date time, String location, Paciente paciente) {
        this.date = date;
        this.time = time;
        this.location = location;
        this.paciente = paciente;
    }

    /**
     * Obtiene la fecha de la cita.
     * 
     * @return Fecha de la cita
     */
    public Date getDate() {
        return date;
    }

    /**
     * Establece la fecha de la cita.
     * 
     * @param date Nueva fecha de la cita
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Obtiene la hora de la cita.
     * 
     * @return Hora de la cita
     */
    public Date getTime() {
        return time;
    }

    /**
     * Establece la hora de la cita.
     * 
     * @param time Nueva hora de la cita
     */
    public void setTime(Date time) {
        this.time = time;
    }

    /**
     * Obtiene la ubicación de la cita.
     * 
     * @return Ubicación de la cita
     */
    public String getLocation() {
        return location;
    }

    /**
     * Establece la ubicación de la cita.
     * 
     * @param location Nueva ubicación de la cita
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Obtiene el paciente asociado a la cita.
     * 
     * @return Paciente de la cita
     */
    public Paciente getPaciente() {
        return paciente;
    }

    /**
     * Establece el paciente de la cita.
     * 
     * @param paciente Nuevo paciente de la cita
     */
    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    /**
     * Confirma la cita.
     * Cambia el estado a confirmada y desactiva cancelada.
     * Puede lanzar una excepción si el paciente no está definido.
     * 
     * @throws NullPointerException si el paciente es null
     */
    public void confirmarCita() {
        if (paciente == null) {
            throw new NullPointerException("No se puede confirmar la cita sin paciente.");
        }
        this.confirmada = true;
        this.cancelada = false;
        System.out.println("La cita ha sido confirmada para el paciente " + paciente.getName());
    }

    /**
     * Cancela la cita.
     * Cambia el estado a cancelada y desactiva confirmada.
     * Puede lanzar una excepción si el paciente no está definido.
     * 
     * @throws NullPointerException si el paciente es null
     */
    public void cancelarCita() {
        if (paciente == null) {
            throw new NullPointerException("No se puede cancelar la cita sin paciente.");
        }
        this.cancelada = true;
        this.confirmada = false;
        System.out.println("La cita ha sido cancelada para el paciente " + paciente.getName());
    }

    /**
     * Indica si la cita está confirmada.
     * 
     * @return true si está confirmada, false si no
     */
    public boolean isConfirmada() {
        return confirmada;
    }

    /**
     * Indica si la cita está cancelada.
     * 
     * @return true si está cancelada, false si no
     */
    public boolean isCancelada() {
        return cancelada;
    }

    /**
     * Reprograma la cita con nueva fecha y hora.
     * 
     * @param nuevaFecha Nueva fecha para la cita
     * @param nuevaHora Nueva hora para la cita
     */
    public void reprogramarCita(Date nuevaFecha, Date nuevaHora) {
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
