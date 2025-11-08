package loaders;

import model.Trasplante;
import model.Donante;
import model.Paciente;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Clase encargada de la persistencia de trasplantes.
 */
public class TrasplanteLoader {

    public static final String RUTA = "Trasplante.txt";
    public static final SimpleDateFormat FORMATO_FECHA = new SimpleDateFormat("dd/MM/yyyy");

    public static ArrayList<Trasplante> cargarTrasplantes() {
        ArrayList<Trasplante> lista = new ArrayList<>();
        File archivo = new File(RUTA);
        if (!archivo.exists()) {
            return lista;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty()) continue;
                Trasplante t = fromArchivo(linea);
                if (t != null) lista.add(t);
            }
        } catch (IOException e) {
            System.err.println("Error leyendo archivo de trasplantes: " + e.getMessage());
        }

        return lista;
    }

    public static void guardarTrasplantes(List<Trasplante> lista) {
        File archivo = new File(RUTA);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
            for (Trasplante t : lista) {
                String linea = toArchivo(t);
                if (linea != null && !linea.isEmpty()) {
                    bw.write(linea);
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Error escribiendo archivo de trasplantes: " + e.getMessage());
        }
    }

    /**
     * Serializa un Trasplante a la línea de archivo.
     * Formato: Órgano: {organType} | Paciente: {NombrePaciente} | Donante: {NombreDonante} | Estado: {Estado} | ID: {ID} | Fecha: {dd/MM/yyyy}
     */
    public static String toArchivo(Trasplante t) {
        if (t == null) return "";
        String organo = t.getOrganType() != null ? t.getOrganType() : "";
        String paciente = t.getReceiver() != null ? t.getReceiver().getName() : "";
        String donante = t.getDonor() != null ? t.getDonor().getName() : "";
        String estado = t.getEstado() != null ? t.getEstado() : "";
        String id = t.getId() != null ? t.getId() : "";
        String fechaStr = t.getFecha() != null ? FORMATO_FECHA.format(t.getFecha()) : "";

        return String.format("Órgano: %s | Paciente: %s | Donante: %s | Estado: %s | ID: %s | Fecha: %s",
                organo, paciente, donante, estado, id, fechaStr);
    }

    /**
     * Crea un objeto Trasplante desde una línea del archivo.
     * Maneja líneas con o sin campo "Órgano:" para compatibilidad.
     * Requiere al menos: Paciente, Donante, ID y Fecha -> devuelve null si faltan.
     */
    public static Trasplante fromArchivo(String linea) {
        if (linea == null || linea.trim().isEmpty()) return null;
        try {
            // separar por '|' y mapear clave:valor normalizando la clave (sin tildes, en minúsculas)
            String[] partes = linea.split("\\|");
            Map<String,String> valores = new HashMap<>();
            for (String p : partes) {
                String[] kv = p.split(":", 2);
                if (kv.length != 2) continue;
                String clave = Normalizer.normalize(kv[0], Normalizer.Form.NFD)
                        .replaceAll("\\p{M}", "")
                        .toLowerCase()
                        .trim();
                String valor = kv[1].trim();
                valores.put(clave, valor);
            }

            // Validar campos mínimos
            if (!valores.containsKey("paciente") || !valores.containsKey("donante")
                    || !valores.containsKey("id") || !valores.containsKey("fecha")) {
                return null;
            }

            String organo = valores.getOrDefault("organo", "");
            String pacientePart = valores.get("paciente");
            String donantePart = valores.get("donante");
            String estado = valores.getOrDefault("estado", "");
            String id = valores.get("id");
            String fechaStr = valores.get("fecha");

            Date fecha = null;
            if (fechaStr != null && !fechaStr.isEmpty()) {
                fecha = FORMATO_FECHA.parse(fechaStr);
            }

            // Normalizador para comparar nombres/ids sin tildes/minúsculas
            java.util.function.Function<String,String> normalize = s -> {
                if (s == null) return "";
                return Normalizer.normalize(s, Normalizer.Form.NFD)
                        .replaceAll("\\p{M}", "")
                        .toLowerCase()
                        .trim();
            };

            Donante donor = null;
            for (Donante d : DonanteLoader.cargarDonantes()) {
                if (d == null) continue;
                String dnId = d.getId() != null ? d.getId().trim() : "";
                String dnName = d.getName() != null ? d.getName() : "";
                if (!dnId.isEmpty() && dnId.equalsIgnoreCase(donantePart)) { donor = d; break; }
                if (normalize.apply(dnName).equals(normalize.apply(donantePart))) { donor = d; break; }
                if (normalize.apply(dnName).contains(normalize.apply(donantePart)) || normalize.apply(donantePart).contains(normalize.apply(dnName))) { donor = d; break; }
            }

            Paciente receiver = null;
            for (Paciente p : PacienteLoader.cargarPacientes()) {
                if (p == null) continue;
                String pnId = p.getId() != null ? p.getId().trim() : "";
                String pnName = p.getName() != null ? p.getName() : "";
                if (!pnId.isEmpty() && pnId.equalsIgnoreCase(pacientePart)) { receiver = p; break; }
                if (normalize.apply(pnName).equals(normalize.apply(pacientePart))) { receiver = p; break; }
                if (normalize.apply(pnName).contains(normalize.apply(pacientePart)) || normalize.apply(pacientePart).contains(normalize.apply(pnName))) { receiver = p; break; }
            }

            // Si no se encontraron donante o receptor, consideramos la línea inválida para las pruebas
            if (donor == null || receiver == null) {
                return null;
            }

            // Si falta organo en la línea, tratar de obtenerlo desde el donante
            if ((organo == null || organo.isBlank()) && donor != null) {
                organo = donor.getOrgano() != null ? donor.getOrgano() : "";
            }

            // Crear trasplante (constructor: id, organType, donor, receiver, estado, historial, rejection, fecha)
            return new Trasplante(id, organo, donor, receiver, estado, "", "", fecha);
        } catch (java.text.ParseException e) {
            System.err.println("Error parseando fecha de trasplante: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("Error al parsear trasplante: " + e.getMessage());
            return null;
        }
    }
}
