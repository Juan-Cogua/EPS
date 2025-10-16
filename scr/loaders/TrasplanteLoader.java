package loaders;

import model.Trasplante;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Clase encargada de la persistencia de trasplantes.
 * @version 1.1 
 * @author Juan
 * @author Andres
 */

public class TrasplanteLoader {

    private static final String RUTA_ARCHIVO = "Trasplante.txt";
    private static final SimpleDateFormat FORMATO_FECHA = new SimpleDateFormat("dd/MM/yyyy");

    /**
     * Carga todos los trasplantes desde el archivo.
     * 
     * @return Lista de trasplantes cargados.
     */
    public static List<Trasplante> cargarTrasplantes() {
        List<Trasplante> lista = new ArrayList<>();
        File archivo = new File(RUTA_ARCHIVO);

        if (!archivo.exists()) {
            System.err.println("Archivo Trasplante.txt no encontrado. Se creará al guardar.");
            return lista;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                Trasplante t = Trasplante.fromArchivo(linea);
                if (t != null) lista.add(t);
            }
        } catch (IOException e) {
            System.err.println("Error al leer Trasplante.txt: " + e.getMessage());
        }

        return lista;
    }

    /**
     * Guarda todos los trasplantes en el archivo de texto.
     * 
     * @param trasplantes Lista de trasplantes a guardar.
     */
    public static void guardarTrasplantes(List<Trasplante> trasplantes) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA_ARCHIVO))) {
            for (Trasplante t : trasplantes) {
                bw.write(t.toArchivo());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al guardar Trasplante.txt: " + e.getMessage());
        }
    }

    /**
     * Elimina un trasplante basado en el nombre del donante y receptor.
     * 
     * @param nombreDonante Nombre del donante.
     * @param nombreReceptor Nombre del receptor.
     * @return true si se eliminó, false si no se encontró.
     */
    public static boolean eliminarTrasplante(String nombreDonante, String nombreReceptor) {
        List<Trasplante> lista = cargarTrasplantes();
        boolean eliminado = lista.removeIf(t ->
                t.getDonor().getName().equalsIgnoreCase(nombreDonante)
             && t.getReceiver().getName().equalsIgnoreCase(nombreReceptor)
        );
        if (eliminado) guardarTrasplantes(lista);
        return eliminado;
    }
}
