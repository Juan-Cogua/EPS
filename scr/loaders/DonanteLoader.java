package loaders;

import java.io.*;
import java.util.*;
import model.Donante;

/**
 * Clase encargada de manejar la persistencia de los datos de donantes desde y hacia el archivo Donante.txt.
 * @version 1.1 
 * @author Juan
 * @author Andres
 */
public class DonanteLoader {

    private static final String RUTA = "Donante.txt";

    public static ArrayList<Donante> cargarDonantes() {
        ArrayList<Donante> lista = new ArrayList<>();
        File archivo = new File(RUTA);

        if (!archivo.exists()) {
            System.out.println("⚠ No se encontró el archivo Donante.txt. Se creará al guardar.");
            return lista;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                String[] datos = linea.split(";");
                // Verifica que haya 10 campos en el archivo (asumiendo que la fecha no se guarda)
                if (datos.length >= 10) { 
                    try {
                        Donante d = new Donante(
                            datos[0], // Nombre
                            Byte.parseByte(datos[1]), // Edad
                            datos[2], // ID
                            datos[3], // Tipo de sangre
                            datos[4], // Dirección
                            datos[5], // Teléfono
                            datos[6], // Tipo de donación
                            datos[7], // Estado de salud
                            datos[8].equals("1"), // Elegibilidad
                            datos[9] // Órgano
                        );
                        lista.add(d);
                    } catch (Exception e) {
                        System.err.println("Error al procesar línea de donante: " + linea + ". Error: " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error al cargar donantes: " + e.getMessage());
        }

        return lista;
    }

    public static void guardarDonantes(List<Donante> lista) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(RUTA))) {
            for (Donante d : lista) {
                // Se guardan los 10 campos que se asume maneja el archivo Donante.txt
                pw.println(String.join(";",
                        d.getName(),
                        String.valueOf(d.getAge()),
                        d.getId(),
                        d.getBloodType(),
                        d.getAddress(),
                        d.getPhone(),
                        d.getDonationType(),
                        d.getHealthStatus(),
                        d.isEligibility() ? "1" : "0",
                        d.getOrgano()
                ));
            }
        } catch (IOException e) {
            System.err.println("Error al guardar donantes: " + e.getMessage());
        }
    }

    public static void agregarDonante(Donante nuevo) {
        ArrayList<Donante> lista = cargarDonantes();
        // Se previene duplicidad por ID
        if (lista.stream().noneMatch(d -> d.getId().equalsIgnoreCase(nuevo.getId()))) {
            lista.add(nuevo);
            guardarDonantes(lista);
        } else {
             System.err.println("Error: El ID del donante ya existe.");
        }
    }

    public static boolean eliminarDonante(String id) {
        ArrayList<Donante> lista = cargarDonantes();
        boolean eliminado = lista.removeIf(d -> d.getId().equalsIgnoreCase(id));
        if (eliminado) guardarDonantes(lista);
        return eliminado;
    }
    
    public static Donante buscarDonantePorId(String id) {
        ArrayList<Donante> lista = cargarDonantes();
        for (Donante d : lista) {
            if (d.getId().equalsIgnoreCase(id)) {
                return d;
            }
        }
        return null;
    }
}