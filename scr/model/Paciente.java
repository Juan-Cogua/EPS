package model;

import excepciones.InvalidDataException;
import excepciones.InvariantViolationException;
import java.util.ArrayList;
import java.util.List;
import model.Cita;

/**
 * Clase que representa un paciente, heredando de Persona.
 * Contiene información médica y citas relacionadas.
 * @author Juan
 * @author Andres
 * @version 2.0
 */

public class Paciente extends Persona {
    private double weight;
    private double height;
    private List<String> allergies;
    private List<Cita> citas;

    private static List<Paciente> pacientes = new ArrayList<>();

    public Paciente(String name, byte age, String id, String bloodType, String address, String phone,
                    double weight, double height, List<String> allergies, List<Cita> citas) throws InvalidDataException {
        super(name, age, id, bloodType, address, phone);

        // validaciones: lanzar, no capturar
        if (weight < 0) throw new InvalidDataException("El peso no puede ser negativo.");
        if (height < 0) throw new InvalidDataException("La altura no puede ser negativa.");

        this.weight = weight;
        this.height = height;
        this.allergies = allergies != null ? allergies : new ArrayList<>();
        this.citas = citas != null ? citas : new ArrayList<>();
    }

    /**
     * Verifica las invariantes de Paciente.
     */
    public void checkInvariant() {
        super.checkInvariant();
        if (weight < 0) throw new InvariantViolationException("El peso no puede ser negativo.");
        if (height < 0) throw new InvariantViolationException("La altura no puede ser negativa.");
        if (allergies == null) throw new InvariantViolationException("La lista de alergias no puede ser null.");
        if (citas == null) throw new InvariantViolationException("La lista de citas no puede ser null.");
    }

    // --- Getters y Setters ---
    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }

    public double getHeight() { return height; }
    public void setHeight(double height) { this.height = height; }

    public List<String> getAllergies() { return allergies; }
    public void setAllergies(List<String> allergies) { this.allergies = allergies; }

    public List<Cita> getCitas() { return citas; }
    public void setCitas(List<Cita> citas) { this.citas = citas; }

    // --- Métodos de gestión de lista estática ---
    public static void añadir(Paciente paciente) throws InvalidDataException {
        if (paciente == null) throw new InvalidDataException("No se puede añadir un paciente null.");
        pacientes.add(paciente);
    }

    public static void eliminar(Paciente paciente) throws InvalidDataException {
        if (paciente == null) throw new InvalidDataException("No se puede eliminar un paciente null.");
        pacientes.remove(paciente);
    }

    public static List<Paciente> getPacientes() {
        return pacientes;
    }

    // --- Lógica de negocio ---
    public void agregarCita(Cita cita) throws InvalidDataException {
        if (cita == null) throw new InvalidDataException("No se puede agregar una cita null.");
        citas.add(cita);
    }

    public void cancelarCita(Cita cita) throws InvalidDataException {
        if (cita == null) throw new InvalidDataException("No se puede cancelar una cita null.");
        citas.remove(cita);
    }

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

    // --- Métodos de archivo (solo formato) ---
    public String toArchivo() {
        return getName() + ";" + getAge() + ";" + getId() + ";" + getBloodType() + ";" +
               getAddress() + ";" + getPhone() + ";" + getWeight() + ";" + getHeight() + ";" +
               String.join(",", getAllergies());
    }

    public static Paciente fromArchivo(String linea) {
        if (linea == null || linea.trim().isEmpty()) return null;

        try {
            String[] parts = linea.split(";");
            if (parts.length < 8) return null;

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

            // ahora el constructor puede lanzar InvalidDataException y será capturado abajo
            return new Paciente(name, age, id, bloodType, address, phone, weight, height, allergies, new ArrayList<>());
        } catch (InvalidDataException e) {
            System.err.println("Línea de paciente inválida (se omitirá): " + e.getMessage() + " -> " + linea);
            return null;
        } catch (Exception e) {
            System.err.println("Error al parsear paciente: " + e.getMessage() + " -> " + linea);
            return null;
        }
    }

    /**
     * MODIFICACIÓN: Muestra el nombre, ID y la lista de alergias.
     */
    @Override
    public String toString() {
        String alergiasStr = String.join(", ", getAllergies());
        if (alergiasStr.isEmpty()) {
            alergiasStr = "No registradas"; 
        }
        return getName() + " (ID: " + getId() + ")" + " (alergias: " + alergiasStr + ")";
    }
}