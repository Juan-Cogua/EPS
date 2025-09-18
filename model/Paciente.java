package model;

import java.io.*;
import java.util.*;

public class Paciente extends Persona {
    private double weight;
    private double height;
    private List<String> allergies;
    private List<Cita> citas;

    public Paciente(String name, byte age, String id, String bloodType, String address, String phone,
                    double weight, double height, List<String> allergies, List<Cita> citas) {
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

    // Métodos Adicionales

    // Lista estática para almacenar pacientes
    private static List<Paciente> pacientes = new ArrayList<>();

    public static boolean añadir(Paciente paciente) {
        return pacientes.add(paciente);
    }
    public static void eliminar(Paciente paciente) {
        pacientes.remove(paciente);
    }
    public static List<Paciente> getPacientes() {
        return pacientes;
    }

    // Manejo de citas
    public void agregarCita(Cita cita) {
        citas.add(cita);
    }
    public void cancelarCita(Cita cita) {
        citas.remove(cita);
    }

    // Historial dinámico de citas
    public String historialCitas() {
        if (citas == null || citas.isEmpty()) {
            return "El paciente no tiene citas registradas.";
        }
        StringBuilder sb = new StringBuilder("Historial de citas:\n");
        for (Cita cita : citas) {
            sb.append(cita.resumen()).append("\n");
        }
        return sb.toString();
    }
    @Override//metodo para representar en forma de cadena variables
    public String toString() {
        return getName() + " (ID: " + getId() + ")";
    }

    // Guarda todos los pacientes en el archivo especificado
    public static void guardarPacientesEnArchivo(String ruta) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ruta))) {
            for (Paciente p : getPacientes()) {
                bw.write(p.toArchivo());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Carga los pacientes desde el archivo especificado
    public static void cargarPacientesDesdeArchivo(String ruta) {
        getPacientes().clear();
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                Paciente p = fromArchivo(linea);
                if (p != null) {
                    añadir(p);
                }
            }
        } catch (IOException e) {
            // Si el archivo no existe, lo ignoramos
        }
    }

    // Convierte el paciente a una línea de texto para guardar
    public String toArchivo() {
        return getName() + ";" + getAge() + ";" + getId() + ";" + getBloodType() + ";" +
               getAddress() + ";" + getPhone() + ";" + getWeight() + ";" + getHeight() + ";" +
               String.join(",", getAllergies());
    }

    // Crea un paciente desde una línea de texto
    public static Paciente fromArchivo(String linea) {
        try {
            String[] parts = linea.split(";");
            String name = parts[0];
            byte age = Byte.parseByte(parts[1]);
            String id = parts[2];
            String bloodType = parts[3];
            String address = parts[4];
            String phone = parts[5];
            double weight = Double.parseDouble(parts[6]);
            double height = Double.parseDouble(parts[7]);
            List<String> allergies = new ArrayList<>();
            if (parts.length > 8 && !parts[8].isEmpty()) {
                for (String a : parts[8].split(",")) allergies.add(a.trim());
            }
            return new Paciente(name, age, id, bloodType, address, phone, weight, height, allergies, new ArrayList<>());
        } catch (Exception e) {
            return null;
        }
    }
}
