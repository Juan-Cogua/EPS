package model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
// Importamos los loaders para la carga estática (usado en fromArchivo)
import loaders.DonanteLoader;
import loaders.PacienteLoader;

/**
 * Clase que representa un trasplante dentro del sistema.
 * Contiene información sobre el órgano trasplantado, el donante, el receptor,
 * fecha del procedimiento, estado y posibles rechazos asociados.
 *
 * @author Juan
 * @author Andres
 * @version 1.2 (Añadido campo 'estado' y constructor de 7 argumentos)
 */
public class Trasplante {

    private String id; // Nuevo campo: ID único del trasplante
    private String organType;
    private Donante donor;
    private Paciente receiver;
    private String estado; // Nuevo campo: Aprobado, Pendiente, Rechazado
    private String historialClinico; // Antes 'rejectionHistory', ahora usado para Historial Clínico
    private String rejectionReason;
    private Date fecha;

    /**
     * Constructor principal de la clase Trasplante (8 argumentos).
     *
     * @param id ID único del trasplante.
     * @param organType Tipo de órgano trasplantado.
     * @param donor Donante que participa en el trasplante.
     * @param receiver Paciente receptor.
     * @param estado Estado del trasplante ("Pendiente", "Aprobado", "Rechazado").
     * @param historialClinico Registro del historial clínico del procedimiento.
     * @param rejectionReason Motivo del rechazo (si aplica).
     * @param fecha Fecha del trasplante.
     */
    public Trasplante(String id, String organType, Donante donor, Paciente receiver,
                        String estado, String historialClinico, String rejectionReason, Date fecha) {

        if (id == null || id.trim().isEmpty())
            throw new IllegalArgumentException("El ID del trasplante no puede estar vacío.");
        if (organType == null || organType.trim().isEmpty())
            throw new IllegalArgumentException("El tipo de órgano no puede estar vacío.");
        if (donor == null)
            throw new NullPointerException("El donante no puede ser null.");
        if (receiver == null)
            throw new NullPointerException("El receptor no puede ser null.");

        this.id = id;
        this.organType = organType;
        this.donor = donor;
        this.receiver = receiver;
        this.estado = estado;
        this.historialClinico = historialClinico != null ? historialClinico : "";
        this.rejectionReason = rejectionReason != null ? rejectionReason : "";
        this.fecha = fecha;
    }

    // --- Getters y Setters ---
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getOrganType() { return organType; }
    public Donante getDonor() { return donor; }
    public Paciente getReceiver() { return receiver; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getHistorialClinico() { return historialClinico; }
    public String getRejectionReason() { return rejectionReason; }
    public Date getFecha() { return fecha; }

    // --- Persistencia ---

    /**
     * Convierte el objeto a formato de archivo:
     * Paciente: {NombrePaciente} | Donante: {NombreDonante} | Estado: {Estado} | ID: {ID} | Fecha: {Fecha}
     */
    public String toArchivo() {
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");

        return String.format("Paciente: %s | Donante: %s | Estado: %s | ID: %s | Fecha: %s",
                receiver.getName(),
                donor.getName(),
                estado,
                id,
                formatoFecha.format(fecha));
    }

    /**
     * Crea un objeto Trasplante desde una línea del archivo, buscando Donante/Paciente por ID.
     */
    public static Trasplante fromArchivo(String linea) {
        if (linea == null || linea.trim().isEmpty()) return null;

        try {
            String[] partes = linea.split("\\|");
            if (partes.length < 5) return null;

            String pacienteNombre = partes[0].split(":")[1].trim();
            String donanteNombre = partes[1].split(":")[1].trim();
            String estado = partes[2].split(":")[1].trim();
            String id = partes[3].split(":")[1].trim();
            Date fecha = new SimpleDateFormat("dd/MM/yyyy").parse(partes[4].split(":")[1].trim());

            Donante donor = DonanteLoader.cargarDonantes().stream()
                    .filter(d -> d.getName().equalsIgnoreCase(donanteNombre))
                    .findFirst()
                    .orElse(null);

            Paciente receiver = PacienteLoader.cargarPacientes().stream()
                    .filter(p -> p.getName().equalsIgnoreCase(pacienteNombre))
                    .findFirst()
                    .orElse(null);

            if (donor == null || receiver == null) {
                System.err.println("Advertencia: Donante o Receptor no encontrado por nombre. Trasplante omitido.");
                return null;
            }

            return new Trasplante(id, donor.getOrgano(), donor, receiver, estado, "", "", fecha);

        } catch (ParseException e) {
            System.err.println("Error de formato de fecha en trasplante: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("Error al procesar trasplante: " + e.getMessage());
            return null;
        }
    }


    @Override
    public String toString() {
        return String.format("Trasplante ID: %s | Órgano: %s | Donante: %s (%s) | Receptor: %s (%s) | Estado: %s",
                id, organType, donor.getName(), donor.getId(), receiver.getName(), receiver.getId(), estado);
    }

    /**
     * Genera un resumen legible para el JTextArea.
     */
    public String resumen() {
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");

        return String.format("TRASPLANTE ID: %s. Órgano: %s. Fecha: %s. Estado: %s\n" +
                             "  > Donante (ID): %s (%s)\n" +
                             "  > Receptor (ID): %s (%s)\n" +
                             "  > Historial Clínico: %s. Motivo: %s",
                id,
                organType,
                formatoFecha.format(fecha),
                estado,
                donor.getName(), donor.getId(),
                receiver.getName(), receiver.getId(),
                historialClinico.isEmpty() ? "N/A" : historialClinico,
                rejectionReason.isEmpty() ? "N/A" : rejectionReason
        );
    }
}