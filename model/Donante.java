package model;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.io.*;

public class Donante extends Persona {
    private  Date birthDate;
    private String donationType;
    private String healthStatus;
    private boolean eligibility;

    public Donante(String name, byte age, String id, String bloodType, String address, String phone, Date birthDate, String donationType, String healthStatus, boolean eligibility) {
        super(name, age, id, bloodType, address, phone);
        this.birthDate = birthDate;
        this.donationType = donationType;
        this.healthStatus = healthStatus;
        this.eligibility = eligibility;
    }

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

    // Lista estática para almacenar donantes
    private static List<Donante> donantes = new ArrayList<>();

    // Método para añadir un donante
    public static void añadir(Donante donante) {
        donantes.add(donante);
    }

    // Método para eliminar un donante
    public static void eliminar(Donante donante) {
        donantes.remove(donante);
    }

    // Método para obtener la lista de donantes
    public static List<Donante> getDonantes() {
        return donantes;
    }

    // Guarda todos los donantes en el archivo especificado
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

    // Carga los donantes desde el archivo especificado
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

    // Convierte el donante a una línea de texto para guardar
    public String toArchivo() {
        return getName() + ";" + getAge() + ";" + getId() + ";" + getBloodType() + ";" +
               getAddress() + ";" + getPhone() + ";" + getDonationType() + ";" +
               getHealthStatus() + ";" + (isEligibility() ? "1" : "0");
    }

    // Crea un donante desde una línea de texto
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
    //metodo para representar en forma de cadena variables
    @Override
    public String toString() {
        return getName() + " (ID: " + getId() + ")";
    }

}
