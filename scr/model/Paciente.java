package scr.model;

import java.util.List;
import java.util.ArrayList;

/**
 * Representa un paciente en el sistema EPS.
 *
 * @author Juan Cogua
 * @version 1.0
 */
public class Paciente extends Persona {
    private double weight;
    private double height;
    private List<String> allergies;
    private List<Cita> citas;

    /**
     * Constructor de Paciente.
     *
     * @param name Nombre del paciente.
     * @param age Edad del paciente.
     * @param id Identificación del paciente.
     * @param bloodType Tipo de sangre.
     * @param address Dirección.
     * @param phone Teléfono.
     * @param weight Peso en kg.
     * @param height Altura en m.
     * @param allergies Lista de alergias.
     * @param citas Lista de citas.
     */
    public Paciente(String name, byte age, String id, String bloodType, String address, String phone,
                    double weight, double height, List<String> allergies, List<Cita> citas) {
        super(name, age, id, bloodType, address, phone);
        this.weight = weight;
        this.height = height;
        this.allergies = allergies;
        this.citas = citas;
    }
//getters y setters
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

    /**
     * Lista estática para almacenar todos los objetos Paciente en memoria.
     * Sirve como la colección central de pacientes.
     */
    private static List<Paciente> pacientes = new ArrayList<>();

    /**
     * Agrega un nuevo paciente a la lista estática.
     *
     * @param paciente El objeto Paciente a añadir.
     * @return {@code true} si el paciente fue añadido exitosamente.
     */

    public static boolean añadir(Paciente paciente) {
        return pacientes.add(paciente);
    }
    public static void eliminar(Paciente paciente) {
        pacientes.remove(paciente);
    }
    public static List<Paciente> getPacientes() {
        return pacientes;
    }

    /**
     * Agrega una cita al paciente.
     * @param cita Cita a agregar.
     */
    public void agregarCita(Cita cita) {
        citas.add(cita);
    }

    /**
     * Cancela una cita del paciente.
     * @param cita Cita a cancelar.
     */
    public void cancelarCita(Cita cita) {
        citas.remove(cita);
    }

    /**
     * Devuelve el historial de citas del paciente.
     * @return Historial de citas como String.
     */
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

<<<<<<< HEAD:scr/model/Paciente.java
    /**
     * Convierte el paciente a una línea de texto para guardar en archivo.
     * @return Cadena con los datos del paciente separados por punto y coma.
     */
    public String toArchivo() {
        return getName() + ";" + getAge() + ";" + getId() + ";" + getBloodType() + ";" +
               getAddress() + ";" + getPhone() + ";" + getWeight() + ";" + getHeight() + ";" +
               String.join(",", getAllergies());
    }

    /**
     * Crea un paciente desde una línea de texto.
     * @param linea Línea de texto con los datos del paciente.
     * @return Instancia de {@link Paciente} si el formato es válido, null si hay error.
     */
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

    /**
     * Representa el paciente como cadena para mostrar en listas.
     * @return Cadena con nombre e ID del paciente.
     */
    @Override
    public String toString() {
        return getName() + " (ID: " + getId() + ")";
    }

    /**
     * Guarda todos los pacientes en el archivo especificado.
     * @param ruta Ruta del archivo.
     * @throws IOException Si ocurre un error de escritura.
     */
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

    /**
     * Carga los pacientes desde el archivo especificado.
     * @param ruta Ruta del archivo.
     * @throws IOException Si ocurre un error de lectura.
     */
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
=======
>>>>>>> parent of ca5ac4d (Partre final):model/Paciente.java
}
