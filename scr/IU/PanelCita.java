package IU;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.*;
import java.util.*;
import model.*;

/**
 * Panel para gestionar citas médicas.
 * Permite crear, mostrar y eliminar automáticamente citas pasadas.
 * 
 * @author Juan Cogua
 * @author Andres Rojas
 * @version 1.0
 */
public class PanelCita extends JPanel {

    private JTextField txtNombrePaciente, txtFecha, txtHora, txtLugar;
    private JTextArea areaHistorial;
    private ArrayList<Cita> listaCitas = new ArrayList<>();
    private final String RUTA_ARCHIVO = "Cita.txt";
    private final SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
    private final SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm");

    public PanelCita() {
        setLayout(new BorderLayout(10, 10));

        // ----- PANEL DE ENTRADA -----
        JPanel panelEntrada = new JPanel(new GridLayout(5, 2, 10, 10));

        txtNombrePaciente = new JTextField();
        txtFecha = new JTextField();
        txtHora = new JTextField();
        txtLugar = new JTextField();

        JButton btnGenerar = new JButton("Generar Cita");
        JButton btnActualizar = new JButton("Actualizar (eliminar pasadas)");

        panelEntrada.add(new JLabel("Nombre del Paciente:"));
        panelEntrada.add(txtNombrePaciente);
        panelEntrada.add(new JLabel("Fecha (dd/MM/yyyy):"));
        panelEntrada.add(txtFecha);
        panelEntrada.add(new JLabel("Hora (HH:mm):"));
        panelEntrada.add(txtHora);
        panelEntrada.add(new JLabel("Lugar:"));
        panelEntrada.add(txtLugar);
        panelEntrada.add(btnGenerar);
        panelEntrada.add(btnActualizar);

        add(panelEntrada, BorderLayout.NORTH);

        // ----- PANEL HISTORIAL -----
        areaHistorial = new JTextArea(10, 40);
        areaHistorial.setEditable(false);
        JScrollPane scroll = new JScrollPane(areaHistorial);
        add(scroll, BorderLayout.CENTER);

        // ----- CARGAR CITAS -----
        cargarCitas();
        actualizarHistorial();

        // Acción: crear cita
        btnGenerar.addActionListener(e -> generarCita());

        // Acción: eliminar citas pasadas
        btnActualizar.addActionListener(e -> {
            eliminarCitasPasadas();
            guardarCitas();
            actualizarHistorial();
        });
    }

    /** Genera una nueva cita */
    private void generarCita() {
        try {
            String nombre = txtNombrePaciente.getText().trim();
            String fechaStr = txtFecha.getText().trim();
            String horaStr = txtHora.getText().trim();
            String lugar = txtLugar.getText().trim();

            if (nombre.isEmpty() || fechaStr.isEmpty() || horaStr.isEmpty() || lugar.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor complete todos los campos.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Date fecha = formatoFecha.parse(fechaStr);
            Date hora = formatoHora.parse(horaStr);

            // Crear un paciente temporal con datos mínimos
            Paciente paciente = new Paciente(
                    nombre,
                    (byte) 0,
                    "N/A",
                    "N/A",
                    "N/A",
                    "N/A",
                    0.0,
                    0.0,
                    new ArrayList<>(),
                    new ArrayList<>()
            );

            Cita cita = new Cita(fecha, hora, lugar, paciente);
            cita.confirmarCita();
            listaCitas.add(cita);
            guardarCitas();
            actualizarHistorial();

            JOptionPane.showMessageDialog(this, "✅ Cita creada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

            // Limpiar campos
            txtNombrePaciente.setText("");
            txtFecha.setText("");
            txtHora.setText("");
            txtLugar.setText("");

        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Formato de fecha u hora inválido.\nUse dd/MM/yyyy y HH:mm",
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al crear la cita: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Actualiza el área de historial con todas las citas */
    private void actualizarHistorial() {
        areaHistorial.setText("");
        if (listaCitas.isEmpty()) {
            areaHistorial.append("No hay citas registradas.\n");
        } else {
            for (Cita c : listaCitas) {
                areaHistorial.append(c.resumen() + "\n");
            }
        }
    }

    /** Elimina las citas con fecha anterior a la actual */
    private void eliminarCitasPasadas() {
        Date hoy = new Date();
        listaCitas.removeIf(c -> c.getDate().before(hoy));
    }

    /** Guarda todas las citas en el archivo Cita.txt */
    private void guardarCitas() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA_ARCHIVO))) {
            for (Cita c : listaCitas) {
                bw.write(formatoFecha.format(c.getDate()) + ";" +
                        formatoHora.format(c.getTime()) + ";" +
                        c.getLocation() + ";" +
                        c.getPaciente().getName());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al guardar citas: " + e.getMessage());
        }
    }

    /** Carga las citas desde el archivo Cita.txt */
    private void cargarCitas() {
        listaCitas.clear();
        File archivo = new File(RUTA_ARCHIVO);
        if (!archivo.exists()) {
            System.out.println("Archivo de citas no encontrado. Se creará uno nuevo al guardar.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(RUTA_ARCHIVO))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(";");
                if (partes.length < 4) continue;

                Date fecha = formatoFecha.parse(partes[0]);
                Date hora = formatoHora.parse(partes[1]);
                String lugar = partes[2];
                String nombrePaciente = partes[3];

                Paciente paciente = new Paciente(
                        nombrePaciente,
                        (byte) 0,
                        "N/A",
                        "N/A",
                        "N/A",
                        "N/A",
                        0.0,
                        0.0,
                        new ArrayList<>(),
                        new ArrayList<>()
                );

                Cita cita = new Cita(fecha, hora, lugar, paciente);
                listaCitas.add(cita);
            }
        } catch (IOException | ParseException e) {
            System.err.println("Error al cargar citas: " + e.getMessage());
        }
    }
}
