package model;
import java.util.Date;
import java.util.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Clase que representa a un donante en el sistema.
 * Contiene información personal, estado de salud y elegibilidad.
 * 
 * @author Juan Cogua
 * @author Andres Rojas
 * @version 1.0 (Mejorado con manejo de excepciones específicas)
 */
public class Donante extends Persona {
    private Date birthDate;
    private String donationType;
    private String healthStatus;
    private boolean eligibility;

    /**
     * Constructor que inicializa los datos de un donante.
     *
     * @param name Nombre del donante.
     * @param age Edad del donante.
     * @param id Identificación del donante.
     * @param bloodType Tipo de sangre del donante.
     * @param address Dirección del donante.
     * @param phone Teléfono del donante.
     * @param birthDate Fecha de nacimiento.
     * @param donationType Tipo de donación.
     * @param healthStatus Estado de salud.
     * @param eligibility Elegibilidad para donar.
     * @throws NullPointerException si birthDate es null
     * @throws IllegalArgumentException si donationType o healthStatus están vacíos
     */
    public Donante(String name, byte age, String id, String bloodType, String address, String phone, 
                   Date birthDate, String donationType, String healthStatus, boolean eligibility) {
        super(name, age, id, bloodType, address, phone);
        
        if (birthDate == null) {
            throw new NullPointerException("La fecha de nacimiento no puede ser null.");
        }
        if (donationType == null || donationType.trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo de donación no puede estar vacío.");
        }
        if (healthStatus == null || healthStatus.trim().isEmpty()) {
            throw new IllegalArgumentException("El estado de salud no puede estar vacío.");
        }
        
        this.birthDate = birthDate;
        this.donationType = donationType;
        this.healthStatus = healthStatus;
        this.eligibility = eligibility;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        if (birthDate == null) {
            throw new NullPointerException("La fecha de nacimiento no puede ser null.");
        }
        this.birthDate = birthDate;
    }

    public String getDonationType() {
        return donationType;
    }

    public void setDonationType(String donationType) {
        if (donationType == null || donationType.trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo de donación no puede estar vacío.");
        }
        this.donationType = donationType;
    }

    public String getHealthStatus() {
        return healthStatus;
    }

    public void setHealthStatus(String healthStatus) {
        if (healthStatus == null || healthStatus.trim().isEmpty()) {
            throw new IllegalArgumentException("El estado de salud no puede estar vacío.");
        }
        this.healthStatus = healthStatus;
    }

    public boolean isEligibility() {
        return eligibility;
    }

    public void setEligibility(boolean eligibility) {
        this.eligibility = eligibility;
    }

    private static List<Donante> donantes = new ArrayList<>();

    /**
     * Añade un donante a la lista.
     *
     * @param donante Donante a añadir.
     * @throws NullPointerException si el donante es null
     */
    public static void añadir(Donante donante) {
        if (donante == null) {
            throw new NullPointerException("No se puede añadir un donante null.");
        }
        donantes.add(donante);
    }

    /**
     * Elimina un donante de la lista.
     *
     * @param donante Donante a eliminar.
     * @throws NullPointerException si el donante es null
     */
    public static void eliminar(Donante donante) {
        if (donante == null) {
            throw new NullPointerException("No se puede eliminar un donante null.");
        }
        donantes.remove(donante);
    }

    public static List<Donante> getDonantes() {
        return donantes;
    }

    /**
     * Guarda todos los donantes en un archivo de texto.
     *
     * @param ruta Ruta del archivo.
     */
    public static void guardarDonantesEnArchivo(String ruta) {
        if (ruta == null || ruta.trim().isEmpty()) {
            System.err.println("Error: La ruta del archivo no puede estar vacía.");
            return;
        }
        
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ruta))) {
            for (Donante d : getDonantes()) {
                bw.write(d.toArchivo());
                bw.newLine();
            }
            System.out.println("Donantes guardados exitosamente en: " + ruta);
        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error inesperado al guardar donantes: " + e.getMessage());
        }
    }

    /**
     * Carga los donantes desde un archivo de texto.
     *
     * @param ruta Ruta del archivo.
     */
    public static void cargarDonantesDesdeArchivo(String ruta) {
        if (ruta == null || ruta.trim().isEmpty()) {
            System.err.println("Error: La ruta del archivo no puede estar vacía.");
            return;
        }
        
        getDonantes().clear();
        
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea;
            int lineaNumero = 0;
            
            while ((linea = br.readLine()) != null) {
                lineaNumero++;
                try {
                    Donante d = fromArchivo(linea);
                    if (d != null) {
                        añadir(d);
                    } else {
                        System.err.println("Advertencia: No se pudo parsear la línea " + lineaNumero);
                    }
                } catch (Exception e) {
                    System.err.println("Error al procesar línea " + lineaNumero + ": " + e.getMessage());
                }
            }
            System.out.println("Donantes cargados exitosamente desde: " + ruta);
            
        } catch (FileNotFoundException e) {
            System.err.println("Archivo no encontrado: " + ruta + ". Se creará uno nuevo al guardar.");
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }
    }

    /**
     * Convierte la información del donante en una línea de texto.
     *
     * @return Cadena de texto con los datos del donante.
     */
    public String toArchivo() {
        return getName() + ";" + getAge() + ";" + getId() + ";" + getBloodType() + ";" +
               getAddress() + ";" + getPhone() + ";" + getDonationType() + ";" +
               getHealthStatus() + ";" + (isEligibility() ? "1" : "0");
    }

    /**
     * Crea un donante desde una línea de texto.
     * 
     * @param linea Línea de texto con los datos del donante.
     * @return Instancia de Donante si el formato es válido, null si hay error.
     */
    public static Donante fromArchivo(String linea) {
        if (linea == null || linea.trim().isEmpty()) {
            System.err.println("La línea está vacía o es null.");
            return null;
        }
        
        try {
            String[] parts = linea.split(";");
            
            // Validar que tenga exactamente 9 campos
            if (parts.length < 9) {
                System.err.println("Formato inválido: se esperaban 9 campos, se encontraron " + parts.length);
                return null;
            }
            
            String name = parts[0];
            byte age;
            String id = parts[2];
            String bloodType = parts[3];
            String address = parts[4];
            String phone = parts[5];
            String donationType = parts[6];
            String healthStatus = parts[7];
            boolean eligibility;
            
            // Parsear edad
            try {
                age = Byte.parseByte(parts[1]);
            } catch (NumberFormatException e) {
                System.err.println("Error: La edad '" + parts[1] + "' no es un número válido.");
                return null;
            }
            
            // Parsear elegibilidad
            if (parts[8].equals("1")) {
                eligibility = true;
            } else if (parts[8].equals("0")) {
                eligibility = false;
            } else {
                System.err.println("Error: El valor de elegibilidad debe ser '1' o '0', se encontró: " + parts[8]);
                return null;
            }
            
            return new Donante(name, age, id, bloodType, address, phone, new Date(), 
                             donationType, healthStatus, eligibility);
            
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Error: Acceso a índice fuera de rango al procesar la línea.");
            return null;
        } catch (IllegalArgumentException e) {
            System.err.println("Error: Argumento inválido - " + e.getMessage());
            return null;
        } catch (NullPointerException e) {
            System.err.println("Error: Valor null encontrado - " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("Error inesperado al parsear donante: " + e.getMessage());
            return null;
        }
    }

    @Override
    public String toString() {
        return getName() + " (ID: " + getId() + ")";
    }
}
