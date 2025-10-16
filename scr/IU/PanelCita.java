package IU;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.*;
import model.Cita;
import model.Paciente;
import loaders.CitaLoader;
import java.awt.*;
import java.util.List;

/**
 * Panel para la gestión de citas médicas.
 * Maneja únicamente la interfaz gráfica.
 * @version 1.1 
 * @author Juan
 * @author Andres
 */
public class PanelCita extends JPanel {

    private JTextField txtFecha, txtHora, txtLugar, txtIdPaciente;
    private JTextArea areaCitas;
    private List<Cita> listaCitas;

    private static final SimpleDateFormat FORMATO_FECHA = new SimpleDateFormat("dd/MM/yyyy");
    private static final SimpleDateFormat FORMATO_HORA = new SimpleDateFormat("HH:mm");

    public PanelCita() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        listaCitas = CitaLoader.cargarCitas();

        // --- Panel superior de entrada ---
        JPanel panelEntrada = new JPanel(new GridLayout(5, 2, 5, 5));

        txtFecha = new JTextField();
        txtHora = new JTextField();
        txtLugar = new JTextField();
        txtIdPaciente = new JTextField();

        JButton btnAgregar = new JButton("Agregar Cita");
        JButton btnEliminar = new JButton("Eliminar Cita");

        panelEntrada.add(new JLabel("Fecha (dd/MM/yyyy):"));
        panelEntrada.add(txtFecha);
        panelEntrada.add(new JLabel("Hora (HH:mm):"));
        panelEntrada.add(txtHora);
        panelEntrada.add(new JLabel("Lugar:"));
        panelEntrada.add(txtLugar);
        panelEntrada.add(new JLabel("ID Paciente:"));
        panelEntrada.add(txtIdPaciente);
        panelEntrada.add(btnAgregar);
        panelEntrada.add(btnEliminar);

        add(panelEntrada, BorderLayout.NORTH);

        // --- Área de visualización ---
        areaCitas = new JTextArea(12, 40);
        areaCitas.setEditable(false);
        JScrollPane scroll = new JScrollPane(areaCitas);
        add(scroll, BorderLayout.CENTER);

        actualizarArea();

        // --- Acciones ---
        btnAgregar.addActionListener(e -> agregarCita());
        btnEliminar.addActionListener(e -> eliminarCita());
    }

    private void agregarCita() {
        try {
            Date fecha = FORMATO_FECHA.parse(txtFecha.getText().trim());
            Date hora = FORMATO_HORA.parse(txtHora.getText().trim());
            String lugar = txtLugar.getText().trim();
            String idPaciente = txtIdPaciente.getText().trim();

            if (lugar.isEmpty() || idPaciente.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe ingresar lugar e ID de paciente.");
                return;
            }

            Paciente paciente = Paciente.getPacientes().stream()
                    .filter(p -> p.getId().equals(idPaciente))
                    .findFirst()
                    .orElse(null);

            if (paciente == null) {
                JOptionPane.showMessageDialog(this, "No se encontró el paciente con ese ID.");
                return;
            }

            Cita nueva = new Cita(fecha, hora, lugar, paciente);
            listaCitas.add(nueva);
            CitaLoader.guardarCitas(listaCitas);

            limpiarCampos();
            actualizarArea();
            JOptionPane.showMessageDialog(this, "Cita agregada correctamente.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al agregar cita: " + ex.getMessage());
        }
    }

    private void eliminarCita() {
        try {
            Date fecha = FORMATO_FECHA.parse(txtFecha.getText().trim());
            String idPaciente = txtIdPaciente.getText().trim();

            boolean eliminado = CitaLoader.eliminarCita(idPaciente, fecha);
            if (eliminado) {
                listaCitas = CitaLoader.cargarCitas();
                actualizarArea();
                JOptionPane.showMessageDialog(this, "🗑 Cita eliminada correctamente.");
            } else {
                JOptionPane.showMessageDialog(this, "⚠ No se encontró cita con esos datos.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "⚠ Error al eliminar cita: " + ex.getMessage());
        }
    }

    private void actualizarArea() {
        areaCitas.setText("");
        for (Cita c : listaCitas) {
            areaCitas.append(c.resumen() + "\n");
        }
    }

    private void limpiarCampos() {
        txtFecha.setText("");
        txtHora.setText("");
        txtLugar.setText("");
        txtIdPaciente.setText("");
    }
}
