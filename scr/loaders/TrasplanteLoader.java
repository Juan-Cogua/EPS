package loaders;

import model.Trasplante;
import model.Donante;
import model.Paciente;
import excepciones.NotFoundException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Clase encargada de la persistencia de trasplantes.
 */
public class TrasplanteLoader {

    private static final String RUTA = "Trasplante.txt";
    private static final SimpleDateFormat FORMATO_FECHA = new SimpleDateFormat("dd/MM/yyyy");

    public static ArrayList<Trasplante> cargarTrasplantes() {
        ArrayList<Trasplante> lista = new ArrayList<>();
        File archivo = new File(RUTA);
        if (!archivo.exists()) {
            return lista;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                Trasplante t = fromArchivo(linea);
                if (t != null) lista.add(t);
            }
        } catch (IOException e) {
            System.err.println("Error al cargar trasplantes: " + e.getMessage());
        }

        return lista;
    }

    public static void guardarTrasplantes(List<Trasplante> lista) {
        File archivo = new File(RUTA);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
            for (Trasplante t : lista) {
                // serializamos aquí en el loader (responsabilidad de persistencia)
                bw.write(toArchivo(t));
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al guardar trasplantes: " + e.getMessage());
        }
    }

    /**
     * Serializa un Trasplante a la línea de archivo.
     * Formato: Paciente: {NombrePaciente} | Donante: {NombreDonante} | Estado: {Estado} | ID: {ID} | Fecha: {dd/MM/yyyy}
     */
    public static String toArchivo(Trasplante t) {
        if (t == null) return "";
        String paciente = t.getReceiver() != null ? t.getReceiver().getName() : "";
        String donante = t.getDonor() != null ? t.getDonor().getName() : "";
        String estado = t.getEstado() != null ? t.getEstado() : "";
        String id = t.getId() != null ? t.getId() : "";
        String fechaStr = t.getFecha() != null ? FORMATO_FECHA.format(t.getFecha()) : "";

        return String.format("Paciente: %s | Donante: %s | Estado: %s | ID: %s | Fecha: %s",
                paciente, donante, estado, id, fechaStr);
    }

    /**
     * Crea un objeto Trasplante desde una línea del archivo,
     * buscando Donante/Paciente por nombre (si no se encuentra lanza NotFoundException).
     *
     * Formato esperado (según toArchivo de Trasplante):
     * Paciente: {NombrePaciente} | Donante: {NombreDonante} | Estado: {Estado} | ID: {ID} | Fecha: {dd/MM/yyyy}
     */
    public static Trasplante fromArchivo(String linea) {
        if (linea == null || linea.trim().isEmpty()) return null;
        try {
            String[] partes = linea.split("\\|");
            if (partes.length < 5) return null;

            String pacientePart = partes[0].split(":", 2)[1].trim();
            String donantePart = partes[1].split(":", 2)[1].trim();
            String estado = partes[2].split(":", 2)[1].trim();
            String id = partes[3].split(":", 2)[1].trim();
            String fechaStr = partes[4].split(":", 2)[1].trim();

            Date fecha = FORMATO_FECHA.parse(fechaStr);

            // Buscar por nombre entre los donantes/pacientes cargados
            Donante donor = null;
            for (Donante d : loaders.DonanteLoader.cargarDonantes()) {
                if (d.getName().equalsIgnoreCase(donantePart) || d.getId().equalsIgnoreCase(donantePart)) {
                    donor = d;
                    break;
                }
            }
            if (donor == null) throw new NotFoundException("Donante '" + donantePart + "' no encontrado.");

            Paciente receiver = null;
            for (Paciente p : loaders.PacienteLoader.cargarPacientes()) {
                if (p.getName().equalsIgnoreCase(pacientePart) || p.getId().equalsIgnoreCase(pacientePart)) {
                    receiver = p;
                    break;
                }
            }
            if (receiver == null) throw new NotFoundException("Paciente '" + pacientePart + "' no encontrado.");

            // organType no se serializa en el toArchivo actual; usaremos el órgano declarado en el donante
            String organType = donor.getOrgano();

            return new Trasplante(id, organType, donor, receiver, estado, "", "", fecha);
        } catch (java.text.ParseException e) {
            System.err.println("Error parseando fecha de trasplante: " + e.getMessage());
            return null;
        } catch (NotFoundException e) {
            System.err.println("Registro de trasplante incompleto: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("Error al parsear trasplante: " + e.getMessage());
            return null;
        }
    }
}
