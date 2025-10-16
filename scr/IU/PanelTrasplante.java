package IU;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.awt.*;
import java.util.List;
   

import model.Trasplante;
import model.Donante;
import model.Paciente;
import loaders.TrasplanteLoader;

/**
 * Panel para la gestión de trasplantes.
 * Maneja únicamente la interfaz gráfica.
 * @version 1.1 
 * @author Juan
 * @author Andres
 */

public class PanelTrasplante extends JPanel {

    private JTextField txtOrgano, txtDonante, txtReceptor, txtHistorial, txtMotivo, txtFecha;
    private JTextArea areaTrasplantes;
    private List<Trasplante> listaTrasplantes;

    private static final SimpleDateFormat FORMATO_FECHA = new SimpleDateFormat("dd/MM/yyyy");

    public PanelTrasplante() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        listaTrasplantes = TrasplanteLoader.cargarTrasplantes();

        // --- Panel de entrada ---
        JPanel panelEntrada = new JPanel(new GridLayout(7, 2, 5, 5));

        txtOrgano = new JTextField();
        txtDonante = new JTextField();
        txtReceptor = new JTextField();
        txtHistorial = new JTextField();
        txtMotivo = new JTextField();
        txtFecha = new JTextField();

        JButton btnAgregar = new JButton("Agregar Trasplante");
        JButton btnEliminar = new JButton("Eliminar Trasplante");

        panelEntrada.add(new JLabel("Órgano:"));
        panelEntrada.add(txtOrgano);
        panelEntrada.add(new JLabel("Nombre Donante:"));
        panelEntrada.add(txtDonante);
        panelEntrada.add(new JLabel("Nombre Receptor:"));
        panelEntrada.add(txtReceptor);
        panelEntrada.add(new JLabel("Estado (Aprobado/Rechazado):"));
        panelEntrada.add(txtHistorial);
        panelEntrada.add(new JLabel("Motivo Rechazo (si aplica):"));
        panelEntrada.add(txtMotivo);
        panelEntrada.add(new JLabel("Fecha (dd/MM/yyyy):"));
        panelEntrada.add(txtFecha);
        panelEntrada.add(btnAgregar);
        panelEntrada.add(btnEliminar);

        add(panelEntrada, BorderLayout.NORTH);

        // --- Área de visualización ---
        areaTrasplantes = new JTextArea(12, 40);
        areaTrasplantes.setEditable(false);
        JScrollPane scroll = new JScrollPane(areaTrasplantes);
        add(scroll, BorderLayout.CENTER);

        actualizarArea();

        // --- Eventos ---
        btnAgregar.addActionListener(e -> agregarTrasplante());
        btnEliminar.addActionListener(e -> eliminarTrasplante());
    }

    private void agregarTrasplante() {
        try {
            String organo = txtOrgano.getText().trim();
            String donanteNombre = txtDonante.getText().trim();
            String receptorNombre = txtReceptor.getText().trim();
            String historial = txtHistorial.getText().trim();
            String motivo = txtMotivo.getText().trim();
            Date fecha = FORMATO_FECHA.parse(txtFecha.getText().trim());

            if (organo.isEmpty() || donanteNombre.isEmpty() || receptorNombre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe ingresar órgano, donante y receptor.");
                return;
            }

            Donante donante = Donante.getDonantes().stream()
                    .filter(d -> d.getName().equalsIgnoreCase(donanteNombre))
                    .findFirst()
                    .orElse(null);

            Paciente receptor = Paciente.getPacientes().stream()
                    .filter(p -> p.getName().equalsIgnoreCase(receptorNombre))
                    .findFirst()
                    .orElse(null);

            if (donante == null || receptor == null) {
                JOptionPane.showMessageDialog(this, "No se encontró el donante o el receptor.");
                return;
            }

            Trasplante nuevo = new Trasplante(organo, donante, receptor, historial, motivo, fecha);
            listaTrasplantes.add(nuevo);
            TrasplanteLoader.guardarTrasplantes(listaTrasplantes);

            limpiarCampos();
            actualizarArea();
            JOptionPane.showMessageDialog(this, "Trasplante agregado correctamente.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al agregar trasplante: " + ex.getMessage());
        }
    }

    private void eliminarTrasplante() {
        String donante = txtDonante.getText().trim();
        String receptor = txtReceptor.getText().trim();

        if (donante.isEmpty() || receptor.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar nombre de donante y receptor.");
            return;
        }

        boolean eliminado = TrasplanteLoader.eliminarTrasplante(donante, receptor);
        if (eliminado) {
            listaTrasplantes = TrasplanteLoader.cargarTrasplantes();
            actualizarArea();
            JOptionPane.showMessageDialog(this, "🗑 Trasplante eliminado correctamente.");
        } else {
            JOptionPane.showMessageDialog(this, "⚠ No se encontró un trasplante con esos nombres.");
        }
    }

    private void actualizarArea() {
        areaTrasplantes.setText("");
        for (Trasplante t : listaTrasplantes) {
            areaTrasplantes.append(t.resumen() + "\n");
        }
    }

    private void limpiarCampos() {
        txtOrgano.setText("");
        txtDonante.setText("");
        txtReceptor.setText("");
        txtHistorial.setText("");
        txtMotivo.setText("");
        txtFecha.setText("");
    }
}
