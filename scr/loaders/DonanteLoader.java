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
                if (datos.length >= 10) {
                    try {
                        Donante d = new Donante(
                                datos[0],                            // nombre
                                Byte.parseByte(datos[1]),            // edad
                                datos[2],                            // id
                                datos[3],                            // tipo sangre
                                datos[4],                            // dirección
                                datos[5],                            // teléfono
                                new Date(),                          // birthDate (placeholder)
                                datos[6],                            // tipo de donación
                                datos[7],                            // estado de salud
                                datos[8].equals("1"),                // elegible
                                datos[9]                             // órgano
                        );
                        lista.add(d);
                    } catch (Exception e) {
                        System.err.println(" Error al leer línea: " + linea);
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
        lista.add(nuevo);
        guardarDonantes(lista);
    }

    public static boolean eliminarDonante(String id) {
        ArrayList<Donante> lista = cargarDonantes();
        boolean eliminado = lista.removeIf(d -> d.getId().equalsIgnoreCase(id));
        if (eliminado) guardarDonantes(lista);
        return eliminado;
    }
}
