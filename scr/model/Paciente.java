package model;
import java.util.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Clase que representa un paciente, heredando de la clase Persona.
 * Contiene atributos médicos y permite gestionar citas y almacenamiento en archivo.
 * 
 * @author Juan Cogua
 * @author Andres Rojas
 * @version 1.0 (Mejorado con manejo de excepciones específicas)
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
     * @throws IllegalArgumentException si weight o height son valores negativos
     */
    public Paciente(String name, byte age, String id, String bloodType, String address, String phone,
                    double weight, double height, List<String> allergies, List<Cita> citas) {
        super(name, age, id, bloodType, address, phone);
        
        if (weight < 0) {
            throw new IllegalArgumentException("El peso no puede ser negativo.");
        }
        if (height < 0) {
            throw new IllegalArgumentException("La altura no puede ser negativa.");
        }
        
        this.weight = weight;
        this.height = height;
        this.allergies = allergies != null ? allergies : new ArrayList<>();
        this.citas = citas != null ? citas : new ArrayList<>();
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        if (weight < 0) {
            throw new IllegalArgumentException("El peso no puede ser negativo.");
        }
        this.weight = weight;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        if (height < 0) {
            throw new IllegalArgumentException("La altura no puede ser negativa.");
        }
        this.height = height;
    }

    public List<String> getAllergies() {
        return allergies;
    }

    public void setAllergies(List<String> allergies) {
        this.allergies = allergies != null ? allergies : new ArrayList<>();
    }

    public List<Cita> getCitas() {
        return citas;
    }

    public void setCitas(List<Cita> citas) {
        this.citas = citas != null ? citas : new ArrayList<>();
    }

    private static List<Paciente> pacientes = new ArrayList<>();

    /**
     * Añade un paciente a la lista estática.
     * 
     * @param paciente Paciente a añadir
     * @return true si se añadió correctamente
     * @throws NullPointerException si el paciente es null
     */
    public static boolean añadir(Paciente paciente) {
        if (paciente == null) {
            throw new NullPointerException("No se puede añadir un paciente null.");
        }
        return pacientes.add(paciente);
    }

    /**
     * Elimina un paciente de la lista estática.
     * 
     * @param paciente Paciente a eliminar
     * @throws NullPointerException si el paciente es null
     */
    public static void eliminar(Paciente paciente) {
        if (paciente == null) {
            throw new NullPointerException("No se puede eliminar un paciente null.");
        }
        pacientes.remove(paciente);
    }

    public static List<Paciente> getPacientes() {
        return pacientes;
    }

    /**
     * Agrega una cita al paciente.
     * 
     * @param cita Cita a agregar
     * @throws NullPointerException si la cita es null
     */
    public void agregarCita(Cita cita) {
        if (cita == null) {
            throw new NullPointerException("No se puede agregar una cita null.");
        }
        citas.add(cita);
    }

    /**
     * Cancela una cita del paciente.
     * 
     * @param cita Cita a cancelar
     * @throws NullPointerException si la cita es null
     */
    public void cancelarCita(Cita cita) {
        if (cita == null) {
            throw new NullPointerException("No se puede cancelar una cita null.");
        }
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

    @Override
    public String toString() {
        return getName() + " (ID: " + getId() + ")";
    }

    /**
     * Guarda todos los pacientes en un archivo de texto.
     * 
     * @param ruta Ruta del archivo donde se guardarán los pacientes
     */
    public static void guardarPacientesEnArchivo(String ruta) {
        if (ruta == null || ruta.trim().isEmpty()) {
            System.err.println("Error: La ruta del archivo no puede estar vacía.");
            return;
        }
        
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ruta))) {
            for (Paciente p : getPacientes()) {
                bw.write(p.toArchivo());
                bw.newLine();
            }
            System.out.println("Pacientes guardados exitosamente en: " + ruta);
        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error inesperado al guardar pacientes: " + e.getMessage());
        }
    }

    /**
     * Carga los pacientes desde un archivo de texto.
     * 
     * @param ruta Ruta del archivo a leer
     */
    public static void cargarPacientesDesdeArchivo(String ruta) {
        if (ruta == null || ruta.trim().isEmpty()) {
            System.err.println("Error: La ruta del archivo no puede estar vacía.");
            return;
        }
        
        getPacientes().clear();
        
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea;
            int lineaNumero = 0;
            
            while ((linea = br.readLine()) != null) {
                lineaNumero++;
                try {
                    Paciente p = fromArchivo(linea);
                    if (p != null) {
                        añadir(p);
                    } else {
                        System.err.println("Advertencia: No se pudo parsear la línea " + lineaNumero);
                    }
                } catch (Exception e) {
                    System.err.println("Error al procesar línea " + lineaNumero + ": " + e.getMessage());
                }
            }
            System.out.println("Pacientes cargados exitosamente desde: " + ruta);
            
        } catch (FileNotFoundException e) {
            System.err.println("Archivo no encontrado: " + ruta + ". Se creará uno nuevo al guardar.");
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
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
     */
    public static Paciente fromArchivo(String linea) {
        if (linea == null || linea.trim().isEmpty()) {
            System.err.println("La línea está vacía o es null.");
            return null;
        }
        
        try {
            String[] parts = linea.split(";");
            
            // Validar que tenga al menos 8 campos
            if (parts.length < 8) {
                System.err.println("Formato inválido: se esperaban al menos 8 campos, se encontraron " + parts.length);
                return null;
            }
            
            String name = parts[0];
            byte age;
            String id = parts[2];
            String bloodType = parts[3];
            String address = parts[4];
            String phone = parts[5];
            double weight;
            double height;
            
            // Parsear edad
            try {
                age = Byte.parseByte(parts[1]);
            } catch (NumberFormatException e) {
                System.err.println("Error: La edad '" + parts[1] + "' no es un número válido.");
                return null;
            }
            
            // Parsear peso
            try {
                weight = Double.parseDouble(parts[6]);
            } catch (NumberFormatException e) {
                System.err.println("Error: El peso '" + parts[6] + "' no es un número válido.");
                return null;
            }
            
            // Parsear altura
            try {
                height = Double.parseDouble(parts[7]);
            } catch (NumberFormatException e) {
                System.err.println("Error: La altura '" + parts[7] + "' no es un número válido.");
                return null;
            }
            
            // Procesar alergias
            List<String> allergies = new ArrayList<>();
            if (parts.length > 8 && !parts[8].isEmpty()) {
                for (String a : parts[8].split(",")) {
                    allergies.add(a.trim());
                }
            }
            
            return new Paciente(name, age, id, bloodType, address, phone, weight, height, allergies, new ArrayList<>());
            
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Error: Acceso a índice fuera de rango al procesar la línea.");
            return null;
        } catch (IllegalArgumentException e) {
            System.err.println("Error: Argumento inválido - " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("Error inesperado al parsear paciente: " + e.getMessage());
            return null;
        }
    }
}
