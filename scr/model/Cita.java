package scr.model;
import java.util.Date;

/**
 * Representa una cita médica en el sistema EPS.
 *
 * @author Juan Cogua
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
     * Constructor de Cita.
     *
     * @param date Fecha de la cita.
     * @param time Hora de la cita.
     * @param location Ubicación.
     * @param paciente Paciente asociado.
     */
    public Cita(Date date, Date time, String location, Paciente paciente) {
        this.date = date;
        this.time = time;
        this.location = location;
        this.paciente = paciente;
    }
//getters y setters
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    /**
     * Confirma la cita.
     */
    public void confirmarCita() {
        this.confirmada = true;
        this.cancelada = false;
        System.out.println("La cita ha sido confirmada para el paciente " + paciente.getName());
    }

    /**
     * Cancela la cita.
     */
    public void cancelarCita() {
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
     * Reprograma la cita.
     * @param nuevaFecha Nueva fecha.
     * @param nuevaHora Nueva hora.
     */
    public void reprogramarCita(Date nuevaFecha, Date nuevaHora) {
        this.date = nuevaFecha;
        this.time = nuevaHora;
        System.out.println("La cita ha sido reprogramada para: " + nuevaFecha + " a las " + nuevaHora);
    }

    /**
     * Devuelve un resumen de la cita.
     * @return Resumen como String.
     */
    public String resumen() {
        return "Cita de " + paciente.getName() + " en " + location + 
               " el " + date + " a las " + time +
               (confirmada ? " [CONFIRMADA]" : "") +
               (cancelada ? " [CANCELADA]" : "");
    }
}
