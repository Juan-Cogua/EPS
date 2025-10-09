package scr.model;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * Representa un donante en el sistema EPS.
 *
 * @author Juan Cogua
 * @version 1.0
 */
public class Donante extends Persona {
    /** Fecha de nacimiento del donante. */
    private Date birthDate;
    /** Tipo de donación que realiza (ej: sangre, plasma, órganos). */
    private String donationType;
    /** Estado de salud actual del donante. */
    private String healthStatus;
    /** Indica si el donante es elegible para donar. */
    private boolean eligibility;

    /**
     * Constructor de Donante.
     *
     * @param name Nombre del donante.
     * @param age Edad.
     * @param id Identificación.
     * @param bloodType Tipo de sangre.
     * @param address Dirección.
     * @param phone Teléfono.
     * @param birthDate Fecha de nacimiento.
     * @param donationType Tipo de donación.
     * @param healthStatus Estado de salud.
     * @param eligibility Elegibilidad.
     */
    public Donante(String name, byte age, String id, String bloodType, String address, String phone, Date birthDate, String donationType, String healthStatus, boolean eligibility) {
        super(name, age, id, bloodType, address, phone);
        this.birthDate = birthDate;
        this.donationType = donationType;
        this.healthStatus = healthStatus;
        this.eligibility = eligibility;
    }
//getters y setters
    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getDonationType() {
        return donationType;
    }

    public void setDonationType(String donationType) {
        this.donationType = donationType;
    }

    public String getHealthStatus() {
        return healthStatus;
    }

    public void setHealthStatus(String healthStatus) {
        this.healthStatus = healthStatus;
    }

    public boolean isEligibility() {
        return eligibility;
    }

    public void setEligibility(boolean eligibility) {
        this.eligibility = eligibility;
    }
    //Metodos Adicionales

     /**
     * Lista estática para almacenar todos los objetos Donante en memoria.
     */
    private static List<Donante> donantes = new ArrayList<>();

    /**
     * Agrega un nuevo donante a la lista estática.
     * @param donante El objeto Donante a añadir.
     */
    public static void añadir(Donante donante) {
        donantes.add(donante);
    }

    /**
     * Elimina un donante de la lista estática.
     * @param donante El objeto Donante a eliminar.
     * @return {@code true} si se eliminó el donante, {@code false} si no se encontró.
     */
    public static void eliminar(Donante donante) {
        donantes.remove(donante);
    }

    /**
     * Obtiene la lista completa de donantes almacenados.
     * @return La lista de objetos Donante.
     */
    public static List<Donante> getDonantes() {
        return donantes;
    }
<<<<<<< HEAD:scr/model/Donante.java

    /**
     * Guarda todos los donantes en el archivo especificado.
     * @param ruta Ruta del archivo.
     * @throws IOException Si ocurre un error de escritura.
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
     * Carga los donantes desde el archivo especificado.
     * @param ruta Ruta del archivo.
     * @throws IOException Si ocurre un error de lectura.
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
     * Convierte el donante a una línea de texto para guardar en archivo.
     * @return Cadena con los datos del donante separados por punto y coma.
     */
    public String toArchivo() {
        return getName() + ";" + getAge() + ";" + getId() + ";" + getBloodType() + ";" +
               getAddress() + ";" + getPhone() + ";" + getDonationType() + ";" +
               getHealthStatus() + ";" + (isEligibility() ? "1" : "0");
    }

    /**
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
     * Representa el donante como cadena para mostrar en listas.
     * @return Cadena con nombre e ID del donante.
     */
=======
    //metodo para representar en forma de cadena variables
>>>>>>> parent of ca5ac4d (Partre final):model/Donante.java
    @Override
    public String toString() {
        return getName() + " (ID: " + getId() + ")";
    }

}
