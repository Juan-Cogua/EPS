package model;

import java.io.*;
import java.util.*;

/**
 * Clase que representa un paciente, heredando de la clase Persona.
 * Contiene atributos médicos y permite gestionar citas y almacenamiento en archivo.
 * 
 * @author Juan Cogua
 * @author Andres Rojas
 * @version 1.0
 */
public class Paciente extends Persona {
    private double weight;
    private double height;
    private List<String> allergies;
    private List<Cita> citas;

    /**
     * Constructor de la clase Paciente.
     * 
     * @param name Nombre del paciente
     * @param age Edad del paciente
     * @param id Identificación del paciente
     * @param bloodType Tipo de sangre
     * @param address Dirección
     * @param phone Teléfono
     * @param weight Peso del paciente
     * @param height Altura del paciente
     * @param allergies Lista de alergias
     * @param citas Lista de citas
     */
    public Paciente(String name, byte age, String id, String bloodType, String address, String phone,
                    double weight, double height, List<String> allergies, List<Cita> citas) {
        super(name, age, id, bloodType, address, phone);
        this.weight = weight;
        this.height = height;
        this.allergies = allergies;
        this.citas = citas;
    }

    /**
     * Obtiene el peso del paciente.
     * 
     * @return Peso del paciente
     */
    public double getWeight() {
        return weight;
    }

    /**
     * Establece el peso del paciente.
     * 
     * @param weight Nuevo peso
     */
    public void setWeight(double weight) {
        this.weight = weight;
    }

    /**
     * Obtiene la altura del paciente.
     * 
     * @return Altura del paciente
     */
    public double getHeight() {
        return height;
    }

    /**
     * Establece la altura del paciente.
     * 
     * @param height Nueva altura
     */
    public void setHeight(double height) {
        this.height = height;
    }

    /**
     * Obtiene la lista de alergias.
     * 
     * @return Lista de alergias
     */
    public List<String> getAllergies() {
        return allergies;
    }

    /**
     * Establece la lista de alergias.
     * 
     * @param allergies Nueva lista de alergias
     */
    public void setAllergies(List<String> allergies) {
        this.allergies = allergies;
    }

    /**
     * Obtiene la lista de citas.
     * 
     * @return Lista de citas
     */
    public List<Cita> getCitas() {
        return citas;
    }

    /**
     * Establece la lista de citas.
     * 
     * @param citas Nueva lista de citas
     */
    public void setCitas(List<Cita> citas) {
        this.citas = citas;
    }

    // Lista estática para almacenar pacientes
    /**
     * Lista estática para almacenar pacientes registrados.
     */
    private static List<Paciente> pacientes = new ArrayList<>();

    /**
     * Añade un paciente a la lista estática.
     * 
     * @param paciente Paciente a añadir
     * @return true si se añadió correctamente
     */
    public static boolean añadir(Paciente paciente) {
        return pacientes.add(paciente);
    }

    /**
     * Elimina un paciente de la lista estática.
     * 
     * @param paciente Paciente a eliminar
     */
    public static void eliminar(Paciente paciente) {
        pacientes.remove(paciente);
    }

    /**
     * Obtiene la lista de pacientes almacenados.
     * 
     * @return Lista de pacientes
     */
    public static List<Paciente> getPacientes() {
        return pacientes;
    }

    /**
     * Agrega una cita al paciente.
     * 
     * @param cita Cita a agregar
     */
    public void agregarCita(Cita cita) {
        citas.add(cita);
    }

    /**
     * Cancela una cita del paciente.
     * 
     * @param cita Cita a cancelar
     */
    public void cancelarCita(Cita cita) {
        citas.remove(cita);
    }

    /**
     * Devuelve el historial de citas del paciente.
     * 
     * @return Cadena con el historial de citas
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

    /**
     * Representa el paciente como cadena de texto.
     * 
     * @return Nombre e ID del paciente
     */
    @Override
    public String toString() {
        return getName() + " (ID: " + getId() + ")";
    }

    /**
     * Guarda todos los pacientes en un archivo de texto.
     * 
     * @param ruta Ruta del archivo donde se guardarán los pacientes
     * @throws IOException Si ocurre un error de escritura
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
     * Carga los pacientes desde un archivo de texto.
     * 
     * @param ruta Ruta del archivo a leer
     * @throws IOException Si ocurre un error de lectura
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
            // Si el archivo no existe, se ignora
        }
    }

    /**
     * Convierte el paciente a una línea de texto para almacenamiento.
     * 
     * @return Cadena de texto con los datos del paciente
     */
    public String toArchivo() {
        return getName() + ";" + getAge() + ";" + getId() + ";" + getBloodType() + ";" +
               getAddress() + ";" + getPhone() + ";" + getWeight() + ";" + getHeight() + ";" +
               String.join(",", getAllergies());
    }

    /**
     * Crea un paciente a partir de una línea de texto.
     * 
     * @param linea Línea de texto con los datos del paciente
     * @return Objeto Paciente si se parsea correctamente, null si hay error
     * @throws NumberFormatException Si ocurre error al convertir valores numéricos
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
}
