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
 * @version 1.4 (Corregido: EliminarCitaPorId ahora Cancelada la cita en lugar de removerla)
 * @author Juan
 * @author Andres
 */
public class CitaLoader {

    private static final String RUTA_ARCHIVO = "Cita.txt";
    private static final SimpleDateFormat FORMATO_FECHA = new SimpleDateFormat("dd/MM/yyyy");
    private static final SimpleDateFormat FORMATO_HORA = new SimpleDateFormat("HH:mm");

    /**
     * Carga todas las citas desde el archivo y marca las citas pasadas como COMPLETADA.
     * @return Lista de citas cargadas.
     */
    public static List<Cita> cargarCitas() {
        List<Cita> citas = new ArrayList<>();
        File archivo = new File(RUTA_ARCHIVO);
        Date hoy = new Date();

        if (!archivo.exists()) {
            System.err.println("Archivo de citas no encontrado. Se creará uno nuevo al guardar.");
            return citas;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                Cita c = fromArchivo(linea);
                if (c != null) {
                    // Lógica de expiración: si la fecha y hora de la cita ya pasaron y no está cancelada
                    // Combina fecha y hora para una comparación precisa
                    Calendar calCita = Calendar.getInstance();
                    calCita.setTime(c.getDate());
                    Calendar calHora = Calendar.getInstance();
                    calHora.setTime(c.getTime());

                    calCita.set(Calendar.HOUR_OF_DAY, calHora.get(Calendar.HOUR_OF_DAY));
                    calCita.set(Calendar.MINUTE, calHora.get(Calendar.MINUTE));
                    calCita.set(Calendar.SECOND, 0);
                    calCita.set(Calendar.MILLISECOND, 0);
                    
                    Date fechaHoraCita = calCita.getTime();
                    
                    // Si está PENDIENTE y la fecha/hora ya pasó, se marca como COMPLETADA.
                    if (c.getEstado().equals("PENDIENTE") && fechaHoraCita.before(hoy)) {
                        c.setEstado("COMPLETADA");
                    }
                    citas.add(c);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al cargar citas: " + e.getMessage());
        }

        guardarCitas(citas);
        return citas;
    }

    /**
     * Parsea una línea del archivo para crear un objeto Cita.
     * Formato: ID;Fecha;Hora;Lugar;ID_Paciente;Doctor;Estado
     */
    public static Cita fromArchivo(String linea) {
        try {
            String[] parts = linea.split(";");
            if (parts.length < 7) return null;

            String id = parts[0]; 
            Date fecha = FORMATO_FECHA.parse(parts[1]);
            Date hora = FORMATO_HORA.parse(parts[2]);
            String lugar = parts[3];
            String idPaciente = parts[4];
            String doctor = parts[5];
            String estado = parts[6];

            Paciente paciente = PacienteLoader.buscarPacientePorId(idPaciente);

            if (paciente != null) {
                Cita cita = new Cita(id, fecha, hora, lugar, paciente, doctor);
                cita.setEstado(estado);
                return cita;
            } else {
                System.err.println("Paciente con ID " + idPaciente + " no encontrado para la cita: " + id);
                return null;
            }

        } catch (ParseException e) {
            System.err.println("Error de formato de fecha/hora en cita: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("Error al procesar cita: " + e.getMessage());
            return null;
        }
    }

    /**
     * Guarda la lista de citas en el archivo, sobrescribiendo el contenido.
     */
    public static void guardarCitas(List<Cita> citas) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(RUTA_ARCHIVO))) {
            for (Cita c : citas) {
                pw.println(c.toArchivo());
            }
        } catch (IOException e) {
            System.err.println("Error al guardar citas: " + e.getMessage());
        }
    }
    
    /**
     * Cancela (no elimina físicamente) una cita pendiente por su ID.
     */
    public static boolean eliminarCitaPorId(String idCita) {
        List<Cita> citas = cargarCitas();
        boolean encontradaYCancelada = false;
        
        for (Cita c : citas) {
            if (c.getId().equalsIgnoreCase(idCita) && c.getEstado().equals("PENDIENTE")) {
                c.setEstado("CANCELADA"); 
                encontradaYCancelada = true;
                break; 
            }
        }
        
        if (encontradaYCancelada) {
            guardarCitas(citas); 
        }
        return encontradaYCancelada;
    }
}