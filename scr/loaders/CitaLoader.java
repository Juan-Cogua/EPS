package loaders;

import model.Cita;
import model.Paciente;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Clase encargada de la persistencia de citas médicas.
 * Permite cargar, guardar y eliminar citas en el archivo de texto.
 * @version 1.1 
 * @author Juan
 * @author Andres
 */
public class CitaLoader {

    private static final String RUTA_ARCHIVO = "Cita.txt";
    private static final SimpleDateFormat FORMATO_FECHA = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    /**
     * Carga todas las citas desde el archivo.
     * 
     * @return Lista de citas cargadas.
     */
    public static List<Cita> cargarCitas() {
        List<Cita> citas = new ArrayList<>();
        File archivo = new File(RUTA_ARCHIVO);

        if (!archivo.exists()) {
            System.err.println("Archivo de citas no encontrado. Se creará uno nuevo al guardar.");
            return citas;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                Cita cita = fromArchivo(linea);
                if (cita != null) citas.add(cita);
            }
        } catch (IOException e) {
            System.err.println("Error al leer Cita.txt: " + e.getMessage());
        }

        return citas;
    }

    /**
     * Guarda la lista de citas en el archivo de texto.
     * 
     * @param citas Lista de citas a guardar.
     */
    public static void guardarCitas(List<Cita> citas) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA_ARCHIVO))) {
            for (Cita c : citas) {
                bw.write(toArchivo(c));
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al guardar Cita.txt: " + e.getMessage());
        }
    }

    /**
     * Convierte una cita a formato de texto para guardar en archivo.
     */
    private static String toArchivo(Cita cita) {
        return FORMATO_FECHA.format(cita.getDate()) + ";" +
               FORMATO_FECHA.format(cita.getTime()) + ";" +
               cita.getLocation() + ";" +
               cita.getPaciente().getId() + ";" +
               (cita.isConfirmada() ? "1" : "0") + ";" +
               (cita.isCancelada() ? "1" : "0");
    }

    /**
     * Convierte una línea del archivo a una instancia de Cita.
     */
    private static Cita fromArchivo(String linea) {
        try {
            String[] parts = linea.split(";");
            if (parts.length < 6) return null;

            Date fecha = FORMATO_FECHA.parse(parts[0]);
            Date hora = FORMATO_FECHA.parse(parts[1]);
            String lugar = parts[2];
            String idPaciente = parts[3];
            boolean confirmada = parts[4].equals("1");
            boolean cancelada = parts[5].equals("1");

            // Buscar paciente en lista
            Paciente paciente = Paciente.getPacientes().stream()
                    .filter(p -> p.getId().equals(idPaciente))
                    .findFirst()
                    .orElse(null);

            if (paciente == null) return null;

            Cita cita = new Cita(fecha, hora, lugar, paciente);
            if (confirmada) cita.confirmarCita();
            if (cancelada) cita.cancelarCita();
            return cita;
        } catch (ParseException e) {
            System.err.println("Error de formato de fecha: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("Error al procesar cita: " + e.getMessage());
            return null;
        }
    }

    /**
     * Elimina una cita por ID de paciente y fecha.
     */
    public static boolean eliminarCita(String idPaciente, Date fecha) {
        List<Cita> citas = cargarCitas();
        boolean eliminado = citas.removeIf(c ->
            c.getPaciente().getId().equals(idPaciente) && c.getDate().equals(fecha)
        );
        if (eliminado) guardarCitas(citas);
        return eliminado;
    }
}
