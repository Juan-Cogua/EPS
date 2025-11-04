package loaders;

import model.Trasplante;
import java.io.*;
import java.util.*;

/**
 * Clase encargada de la persistencia de trasplantes.
 * @version 1.2 (Modificado: Eliminación por ID, Persistencia ajustada a 7 campos)
 * @author Juan
 * @author Andres
 */

public class TrasplanteLoader {

    private static final String RUTA_ARCHIVO = "Trasplante.txt";
    // El FORMATO_FECHA ya no se necesita aquí si lo manejas en Trasplante.java

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
                try {
                    Trasplante t = Trasplante.fromArchivo(linea); // Llama al fromArchivo que ahora puede lanzar NotFoundException
                    if (t != null) lista.add(t);
                } catch (excepciones.NotFoundException nf) {
                    System.err.println("Advertencia al cargar trasplante: " + nf.getMessage() + " Línea omitida: " + linea);
                    // continuar con siguiente línea
                }
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
                bw.write(t.toArchivo()); // Llama al toArchivo corregido (7 campos)
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al guardar Trasplante.txt: " + e.getMessage());
        }
    }

    /**
     * Elimina un trasplante basado en el ID del donante y receptor.
     *
     * @param idDonante ID del donante.
     * @param idReceptor ID del receptor.
     * @return true si se eliminó, false si no se encontró.
     */
    public static boolean eliminarTrasplante(String idDonante, String idReceptor) {
        List<Trasplante> lista = cargarTrasplantes();
        boolean eliminado = lista.removeIf(t ->
                t.getDonor().getId().equalsIgnoreCase(idDonante) // Usar ID
             && t.getReceiver().getId().equalsIgnoreCase(idReceptor)); //  Usar ID

        if (eliminado) {
            guardarTrasplantes(lista);
        }
        return eliminado;
    }
}