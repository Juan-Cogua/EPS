package model;
import java.util.Date;



public class Cita {
    private Date date;
    private Date time; 
    private String location;
    private Paciente paciente;

    public Cita(Date date, Date time, String location, Paciente paciente) {
        this.date = date;
        this.time = time;
        this.location = location;
        this.paciente = paciente;
    }

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
    
//Metodos Adicionales
    //confirmar y cancelar citas
    public void confirmarCita() {
        System.out.println("La cita ha sido confirmada para el paciente " + paciente.getName());
    }

    public void cancelarCita() {
        System.out.println("La cita ha sido cancelada para el paciente " + paciente.getName());
    }
    //reprogramr la cita
    public void reprogramarCita(Date nuevaFecha, Date nuevaHora) {
        this.date = nuevaFecha;
        this.time = nuevaHora;
        System.out.println("La cita ha sido reprogramada para: " + nuevaFecha + " a las " + nuevaHora);
    }
    //resumen de la cita
    public String resumen() {
        return "Cita de " + paciente.getName() + " en " + location + 
               " el " + date + " a las " + time;
    }


}
