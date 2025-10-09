package scr.model;
import java.util.Date;
import java.util.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Clase que representa a un donante en el sistema.
 * Contiene información personal, estado de salud y elegibilidad.
 * 
 * @author Juan Cogua
 * @author Andres Rojas
 * @version 1.0
 */
public class Donante extends Persona {
    /** Fecha de nacimiento del donante. */
    private Date birthDate;
    /** Tipo de donación que realiza el donante. */
    private String donationType;
    /** Estado de salud del donante. */
    private String healthStatus;
    /** Indica si el donante es elegible. */
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
     */
    public Donante(String name, byte age, String id, String bloodType, String address, String phone, Date birthDate, String donationType, String healthStatus, boolean eligibility) {
        super(name, age, id, bloodType, address, phone);
        this.birthDate = birthDate;
        this.donationType = donationType;
        this.healthStatus = healthStatus;
        this.eligibility = eligibility;
    }

    /**
     * Obtiene la fecha de nacimiento del donante.
     *
     * @return Fecha de nacimiento.
     */
    public Date getBirthDate() {
        return birthDate;
    }

    /**
     * Establece la fecha de nacimiento del donante.
     *
     * @param birthDate Fecha de nacimiento.
     */
    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    /**
     * Obtiene el tipo de donación.
     *
     * @return Tipo de donación.
     */
    public String getDonationType() {
        return donationType;
    }

    /**
     * Establece el tipo de donación.
     *
     * @param donationType Tipo de donación.
     */
    public void setDonationType(String donationType) {
        this.donationType = donationType;
    }

    /**
     * Obtiene el estado de salud del donante.
     *
     * @return Estado de salud.
     */
    public String getHealthStatus() {
        return healthStatus;
    }

    /**
     * Establece el estado de salud del donante.
     *
     * @param healthStatus Estado de salud.
     */
    public void setHealthStatus(String healthStatus) {
        this.healthStatus = healthStatus;
    }

    /**
     * Verifica si el donante es elegible.
     *
     * @return true si es elegible, false en caso contrario.
     */
    public boolean isEligibility() {
        return eligibility;
    }

    /**
     * Establece si el donante es elegible.
     *
     * @param eligibility true si es elegible, false en caso contrario.
     */
    public void setEligibility(boolean eligibility) {
        this.eligibility = eligibility;
    }

    /** Lista estática para almacenar donantes. */
    private static List<Donante> donantes = new ArrayList<>();

    /**
     * Añade un donante a la lista.
     *
     * @param donante Donante a añadir.
     */
    public static void añadir(Donante donante) {
        donantes.add(donante);
    }

    /**
     * Elimina un donante de la lista.
     *
     * @param donante Donante a eliminar.
     */
    public static void eliminar(Donante donante) {
        donantes.remove(donante);
    }

    /**
     * Obtiene la lista de donantes.
     *
     * @return Lista de donantes.
     */
    public static List<Donante> getDonantes() {
        return donantes;
    }


    /**
     * Guarda todos los donantes en un archivo de texto.
     *
     * @param ruta Ruta del archivo.
     * @throws IOException Si ocurre un error al escribir el archivo.
     */
    public static void guardarDonantesEnArchivo(String ruta) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ruta))) {
            for (Donante d : getDonantes()) {
                bw.write(d.toArchivo());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Carga los donantes desde un archivo de texto.
     *
     * @param ruta Ruta del archivo.
     */
    public static void cargarDonantesDesdeArchivo(String ruta) {
        getDonantes().clear();
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                Donante d = fromArchivo(linea);
                if (d != null) {
                    añadir(d);
                }
            }
        } catch (IOException e) {
            // Si el archivo no existe, lo ignoramos
        }
    }

    /**
     * Convierte la información del donante en una línea de texto.
     *
     * @return Cadena de texto con los datos del donante.
     * Convierte el donante a una línea de texto para guardar en archivo.
     * @return Cadena con los datos del donante separados por punto y coma.
     */
    public String toArchivo() {
        return getName() + ";" + getAge() + ";" + getId() + ";" + getBloodType() + ";" +
               getAddress() + ";" + getPhone() + ";" + getDonationType() + ";" +
               getHealthStatus() + ";" + (isEligibility() ? "1" : "0");
    }

    /**
     * Crea un objeto Donante a partir de una línea de texto.
     *
     * @param linea Línea con los datos del donante.
     * @return Objeto Donante o null si ocurre un error.
     * @throws Exception Si hay un error al convertir los datos.
     * Crea un donante desde una línea de texto.
     * @param linea Línea de texto con los datos del donante.
     * @return Instancia de {@link Donante} si el formato es válido, null si hay error.
     */
    public static Donante fromArchivo(String linea) {
        try {
            String[] parts = linea.split(";");
            String name = parts[0];
            byte age = Byte.parseByte(parts[1]);
            String id = parts[2];
            String bloodType = parts[3];
            String address = parts[4];
            String phone = parts[5];
            String donationType = parts[6];
            String healthStatus = parts[7];
            boolean eligibility = parts[8].equals("1");
            return new Donante(name, age, id, bloodType, address, phone, new Date(), donationType, healthStatus, eligibility);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Devuelve una representación en cadena del donante.
     *
     * @return Cadena con el nombre e identificación.
     */

     /** 
     * Representa el donante como cadena para mostrar en listas.
     * @return Cadena con nombre e ID del donante.
     */
    //metodo para representar en forma de cadena variables
    @Override
    public String toString() {
        return getName() + " (ID: " + getId() + ")";
    }

}
