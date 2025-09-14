package model;

import java.util.List;
import java.util.ArrayList; 

public class Paciente extends Persona {
    private double weight;
    private double height;
    private List<String> allergies;
    private List<Cita> citas;

    public Paciente(String name, byte age, String id, String bloodType, String address, String phone, double weight, double height, List<String> allergies, List<Cita> citas) {
        super(name, age, id, bloodType, address, phone);
        this.weight = weight;
        this.height = height;
        this.allergies = allergies;
        this.citas = citas;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public List<String> getAllergies() {
        return allergies;
    }

    public void setAllergies(List<String> allergies) {
        this.allergies = allergies;
    }

    public List<Cita> getCitas() {
        return citas;
    }

    public void setCitas(List<Cita> citas) {
        this.citas = citas;
    }
    
    //Metodos Adicionales

    // Lista estática para almacenar pacientes
    private static List<Paciente> pacientes = new ArrayList<>();

    // Método para añadir un paciente
    public static void añadir(Paciente paciente) {
        pacientes.add(paciente);
    }

    // Método para eliminar un paciente
    public static void eliminar(Paciente paciente) {
        pacientes.remove(paciente);
    }

    // Método para obtener la lista de pacientes
    public static List<Paciente> getPacientes() {
        return pacientes;
    }

}
