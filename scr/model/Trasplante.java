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

    private String organType;
    private Donante donor;
    private Paciente receiver;
    private String estado; // Nuevo campo: Aprobado, Pendiente, Rechazado
    private String historialClinico; // Antes 'rejectionHistory', ahora usado para Historial Clínico
    private String rejectionReason;
    private Date fecha;

    /**
     * Constructor principal de la clase Trasplante (7 argumentos).
     *
     * @param organType Tipo de órgano trasplantado.
     * @param donor Donante que participa en el trasplante.
     * @param receiver Paciente receptor.
     * @param estado Estado del trasplante ("Pendiente", "Aprobado", "Rechazado").
     * @param historialClinico Registro del historial clínico del procedimiento.
     * @param rejectionReason Motivo del rechazo (si aplica).
     * @param fecha Fecha del trasplante.
     */
    public Trasplante(String organType, Donante donor, Paciente receiver,
                      String estado, String historialClinico, String rejectionReason, Date fecha) {

        if (organType == null || organType.trim().isEmpty())
            throw new IllegalArgumentException("El tipo de órgano no puede estar vacío.");
        if (donor == null)
            throw new NullPointerException("El donante no puede ser null.");
        if (receiver == null)
            throw new NullPointerException("El receptor no puede ser null.");

        this.organType = organType;
        this.donor = donor;
        this.receiver = receiver;
        this.estado = estado;
        this.historialClinico = historialClinico != null ? historialClinico : "";
        this.rejectionReason = rejectionReason != null ? rejectionReason : "";
        this.fecha = fecha;
    }

    // --- Getters ---
    public String getOrganType() { return organType; }
    public Donante getDonor() { return donor; }
    public Paciente getReceiver() { return receiver; }
    public String getEstado() { return estado; }
    public String getHistorialClinico() { return historialClinico; }
    public String getRejectionReason() { return rejectionReason; }
    public Date getFecha() { return fecha; }

    // --- Persistencia ---

    /**
     * Convierte el objeto a formato de archivo:
     * Organo;ID_Donante;ID_Receptor;Estado;Historial;Motivo;Fecha
     */
    public String toArchivo() {
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");

        return String.join(";",
                organType,
                donor.getId(), // Usamos ID del Donante
                receiver.getId(), // Usamos ID del Receptor
                estado,
                historialClinico,
                rejectionReason,
                formatoFecha.format(fecha)
        );
    }

    /**
     * Crea un objeto Trasplante desde una línea del archivo, buscando Donante/Paciente por ID.
     */
    public static Trasplante fromArchivo(String linea) {
        if (linea == null || linea.trim().isEmpty()) return null;

        try {
            String[] partes = linea.split(";");
            // Ahora esperamos 7 partes: Organo;ID_Donante;ID_Receptor;Estado;Historial;Motivo;Fecha
            if (partes.length < 7) return null;

            String organ = partes[0];
            String donorId = partes[1]; // Es un ID
            String receiverId = partes[2]; // Es un ID
            String estado = partes[3];
            String historial = partes[4];
            String reason = partes[5];
            Date fecha = new SimpleDateFormat("dd/MM/yyyy").parse(partes[6]);

            // ⚠️ Usar Loaders para buscar por ID
            Donante donor = DonanteLoader.buscarDonantePorId(donorId);
            Paciente receiver = PacienteLoader.buscarPacientePorId(receiverId);

            if (donor == null || receiver == null) {
                System.err.println("Advertencia: Donante o Receptor no encontrado por ID. Trasplante omitido.");
                return null;
            }

            return new Trasplante(organ, donor, receiver, estado, historial, reason, fecha);

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
        // ... (Tu implementación original puede ir aquí, la ajustamos ligeramente)
        return String.format("Trasplante de %s | Donante: %s (%s) | Receptor: %s (%s) | Estado: %s",
                organType,
                donor.getName(), donor.getId(),
                receiver.getName(), receiver.getId(),
                estado
        );
    }

    /**
     * Genera un resumen legible para el JTextArea.
     */
    public String resumen() {
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");

        return String.format("TRASPLANTE: %s. Fecha: %s. Estado: %s\n" +
                             "  > Donante (ID): %s (%s)\n" +
                             "  > Receptor (ID): %s (%s)\n" +
                             "  > Historial Clínico: %s. Motivo: %s",
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