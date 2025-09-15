package model;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

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
    //metodo para representar en forma de cadena variables
    @Override
    public String toString() {
        return getName() + " (ID: " + getId() + ")";
    }

}
