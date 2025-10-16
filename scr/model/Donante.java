package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Clase que representa a un donante en el sistema.
 * Contiene información personal, estado de salud y elegibilidad.
 * @author Juan
 * @author Andres
 * @version 1.1 
 */

public class Donante extends Persona {
    private Date birthDate;
    private String donationType;
    private String healthStatus;
    private boolean eligibility;
    private String organo; // Órgano donado

    private static List<Donante> donantes = new ArrayList<>();

    /**
     * Constructor de Donante.
     */
    public Donante(String name, byte age, String id, String bloodType, String address, String phone, 
                   Date birthDate, String donationType, String healthStatus, boolean eligibility, String organo) {
        super(name, age, id, bloodType, address, phone);

        if (birthDate == null)
            throw new NullPointerException("La fecha de nacimiento no puede ser null.");
        if (donationType == null || donationType.trim().isEmpty())
            throw new IllegalArgumentException("El tipo de donación no puede estar vacío.");
        if (healthStatus == null || healthStatus.trim().isEmpty())
            throw new IllegalArgumentException("El estado de salud no puede estar vacío.");

        this.birthDate = birthDate;
        this.donationType = donationType;
        this.healthStatus = healthStatus;
        this.eligibility = eligibility;
        this.organo = organo != null ? organo.trim() : "";
    }

    // --- Getters y setters ---
    public Date getBirthDate() { return birthDate; }
    public void setBirthDate(Date birthDate) { this.birthDate = birthDate; }

    public String getDonationType() { return donationType; }
    public void setDonationType(String donationType) { this.donationType = donationType; }

    public String getHealthStatus() { return healthStatus; }
    public void setHealthStatus(String healthStatus) { this.healthStatus = healthStatus; }

    public boolean isEligibility() { return eligibility; }
    public void setEligibility(boolean eligibility) { this.eligibility = eligibility; }

    public String getOrgano() { return organo; }
    public void setOrgano(String organo) { this.organo = organo; }

    // --- Gestión de lista estática ---
    public static void añadir(Donante donante) {
        if (donante == null)
            throw new NullPointerException("No se puede añadir un donante null.");
        donantes.add(donante);
    }

    public static void eliminar(Donante donante) {
        if (donante == null)
            throw new NullPointerException("No se puede eliminar un donante null.");
        donantes.remove(donante);
    }

    public static List<Donante> getDonantes() {
        return donantes;
    }

    // --- Métodos de archivo (solo formato, sin IO) ---
    public String toArchivo() {
        return getName() + ";" + getAge() + ";" + getId() + ";" + getBloodType() + ";" +
               getAddress() + ";" + getPhone() + ";" + getDonationType() + ";" +
               getHealthStatus() + ";" + (isEligibility() ? "1" : "0") + ";" + organo;
    }

    public static Donante fromArchivo(String linea) {
        if (linea == null || linea.trim().isEmpty())
            return null;

        try {
            String[] parts = linea.split(";");
            if (parts.length < 10)
                return null;

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
        String detalle = tipoDonacion.equalsIgnoreCase("Órganos") && organo != null && !organo.trim().isEmpty()
                ? organo : tipoDonacion;
        return String.format("%s (ID: %s) | Sangre: %s | Dona: %s",
                getName(), getId(), getBloodType(), detalle);
    }
}
