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
 * @version 1.1 (Añadido campo de órgano específico)
 */
public class Donante extends Persona {
    private Date birthDate;
    private String donationType;
    private String healthStatus;
    private boolean eligibility;
    private String organo; // Nuevo campo: tipo de órgano específico (Corazón, Riñón, etc.)

    /**
     * Constructor que inicializa los datos de un donante.
     */
    public Donante(String name, byte age, String id, String bloodType, String address, String phone, 
                   Date birthDate, String donationType, String healthStatus, boolean eligibility, String organo) {
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
        this.organo = organo != null ? organo.trim() : "";
    }

    // Getters y setters
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

    public String getOrgano() {
        return organo;
    }

    public void setOrgano(String organo) {
        this.organo = organo;
    }

    // Lista estática de donantes
    private static List<Donante> donantes = new ArrayList<>();

    public static void añadir(Donante donante) {
        if (donante == null) {
            throw new NullPointerException("No se puede añadir un donante null.");
        }
        donantes.add(donante);
    }

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
        }
    }

    /**
     * Carga los donantes desde un archivo de texto.
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
     */
    public String toArchivo() {
        return getName() + ";" + getAge() + ";" + getId() + ";" + getBloodType() + ";" +
               getAddress() + ";" + getPhone() + ";" + getDonationType() + ";" +
               getHealthStatus() + ";" + (isEligibility() ? "1" : "0") + ";" + organo;
    }

    /**
     * Crea un donante desde una línea de texto.
     */
    public static Donante fromArchivo(String linea) {
        if (linea == null || linea.trim().isEmpty()) {
            System.err.println("La línea está vacía o es null.");
            return null;
        }
        
        try {
            String[] parts = linea.split(";");
            if (parts.length < 10) {
                System.err.println("Formato inválido: se esperaban 10 campos, se encontraron " + parts.length);
                return null;
            }
            
            String name = parts[0];
            byte age = Byte.parseByte(parts[1]);
            String id = parts[2];
            String bloodType = parts[3];
            String address = parts[4];
            String phone = parts[5];
            String donationType = parts[6];
            String healthStatus = parts[7];
            boolean eligibility = parts[8].equals("1");
            String organo = parts[9];
            
            return new Donante(name, age, id, bloodType, address, phone, new Date(), 
                             donationType, healthStatus, eligibility, organo);
        } catch (Exception e) {
            System.err.println("Error al parsear donante: " + e.getMessage());
            return null;
        }
    }

    @Override
    public String toString() {
        String tipoDonacion = (donationType == null || donationType.trim().isEmpty()) ? "Sangre" : donationType.trim();

        if (tipoDonacion.equalsIgnoreCase("Órganos") && organo != null && !organo.trim().isEmpty()) {
            return String.format("%s (ID: %s) | Sangre: %s | Dona: %s",
                    getName(), getId(), getBloodType(), organo);
        } else {
            return String.format("%s (ID: %s) | Sangre: %s | Dona: %s",
                    getName(), getId(), getBloodType(), tipoDonacion);
        }
    }
}
