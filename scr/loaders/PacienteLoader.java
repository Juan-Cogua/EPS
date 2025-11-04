package loaders;

import java.io.*;
import java.util.*;
import model.Paciente;


/**
 * Clase encargada de la persistencia de pacientes.
 * @version 1.2 (Modificado: Añadido buscarPacientePorId)
 * @author Juan
 * @author Andres
 */
public class PacienteLoader {

    private static final String RUTA = "Paciente.txt";

    /**
     * Carga los pacientes desde el archivo Paciente.txt
     * @return Lista de pacientes cargada del archivo
     */
    public static ArrayList<Paciente> cargarPacientes() {
        // ... (código existente)
        ArrayList<Paciente> lista = new ArrayList<>();
        File archivo = new File(RUTA);

        if (!archivo.exists()) {
            System.out.println(" No se encontró el archivo Paciente.txt. Se creará al guardar.");
            return lista;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                Paciente p = Paciente.fromArchivo(linea);
                if (p != null) lista.add(p);
            }
        } catch (IOException e) {
            System.err.println(" Error al cargar pacientes: " + e.getMessage());
        }

        return lista;
    }

    /**
     * Busca un paciente por su ID en la lista cargada (solución al bug de CitaLoader).
     * @param id Identificación del paciente
     * @return El paciente encontrado o null si no existe.
     */
    public static Paciente buscarPacientePorId(String id) {
        // Se carga la lista fresca para asegurar que siempre se busque en la versión actualizada
        ArrayList<Paciente> lista = cargarPacientes(); 
        for (Paciente p : lista) {
            if (p.getId().equalsIgnoreCase(id)) {
                return p;
            }
        }
        throw new excepciones.NotFoundException("Paciente con ID '" + id + "' no encontrado.");
    }
    
    /**
     * Guarda la lista de pacientes en el archivo Paciente.txt
     * @param lista lista de pacientes a guardar
     */
    public static void guardarPacientes(List<Paciente> lista) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(RUTA))) {
            for (Paciente p : lista) {
                pw.println(p.toArchivo());
            }
        } catch (IOException e) {
            System.err.println("Error al guardar pacientes: " + e.getMessage());
        }
    }

    /**
     * Agrega un nuevo paciente al archivo.
     * @param nuevo Paciente a agregar
     */
    public static void agregarPaciente(Paciente nuevo) {
        ArrayList<Paciente> lista = cargarPacientes();
        lista.add(nuevo);
        guardarPacientes(lista);
    }

    /**
     * Elimina un paciente por su ID y actualiza el archivo.
     * @param id Identificación del paciente
     * @return true si se eliminó correctamente, false si no se encontró
     */
    public static boolean eliminarPaciente(String id) {
        ArrayList<Paciente> lista = cargarPacientes();
        boolean eliminado = lista.removeIf(p -> p.getId().equalsIgnoreCase(id));
        if (eliminado) guardarPacientes(lista);
        return eliminado;
    }
}
