package IU;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.*;
import model.*;
import java.io.*;

/**
 * Panel encargado de gestionar los trasplantes.
 * Muestra pacientes, donantes y permite verificar compatibilidad, crear,
 * rechazar y eliminar trasplantes con registro en archivo.
 * 
 * @author Juan
 * @author Andres
 */
public class PanelTrasplante extends JPanel {

    private DefaultListModel<Donante> modeloDonantes = new DefaultListModel<>();
    private DefaultListModel<Paciente> modeloPacientes = new DefaultListModel<>();
    private DefaultListModel<String> modeloHistorial = new DefaultListModel<>();

    private JList<Donante> listaDonantes;
    private JList<Paciente> listaPacientes;
    private JList<String> listaHistorial;
    private JTextArea areaInfo;
    private JTextField txtOrgan;
    private JButton btnVerificar, btnCrear, btnRechazar, btnEliminar;

    private ArrayList<Trasplante> trasplantes = new ArrayList<>();
    private final String RUTA = "Trasplante.txt";

    public PanelTrasplante() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        // --- Panel Izquierdo (Pacientes)
        modeloPacientes.clear();
        for (Paciente p : Paciente.getPacientes()) modeloPacientes.addElement(p);
        listaPacientes = new JList<>(modeloPacientes);
        listaPacientes.setBorder(BorderFactory.createTitledBorder("🧍 Pacientes"));
        listaPacientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaPacientes.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Paciente p) {
                    label.setText(String.format("%s (ID: %s) | Sangre: %s",
                            p.getName(), p.getId(), p.getBloodType()));
                }
                return label;
            }
        });

        // --- Panel Derecho (Donantes)
        modeloDonantes.clear();
        for (Donante d : Donante.getDonantes()) modeloDonantes.addElement(d);
        listaDonantes = new JList<>(modeloDonantes);
        listaDonantes.setBorder(BorderFactory.createTitledBorder("🩸 Donantes"));
        listaDonantes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // ✅ Personalizar visualización (nombre + sangre + tipo de donación u órgano específico)
        listaDonantes.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Donante d) {
                    String tipoDonacion = (d.getDonationType() == null || d.getDonationType().trim().isEmpty())
                            ? "Sangre" : d.getDonationType().trim();
                    String organo = d.getOrgano() != null && !d.getOrgano().trim().isEmpty()
                            ? d.getOrgano().trim() : tipoDonacion;

                    label.setText(String.format("%s (ID: %s) | Sangre: %s | Dona: %s",
                            d.getName(), d.getId(), d.getBloodType(), organo));
                }
                return label;
            }
        });

        JPanel panelListas = new JPanel(new GridLayout(1, 2, 10, 10));
        panelListas.add(new JScrollPane(listaPacientes));
        panelListas.add(new JScrollPane(listaDonantes));

        // --- Centro: controles
        JPanel panelCentro = new JPanel(new GridLayout(5, 1, 5, 5));
        txtOrgan = new JTextField();
        btnVerificar = new JButton("Verificar Compatibilidad");
        btnCrear = new JButton("Crear Trasplante");
        btnRechazar = new JButton("Registrar Rechazo");
        btnEliminar = new JButton("Eliminar Trasplante");

        panelCentro.add(new JLabel("Órgano o tipo de donación:"));
        panelCentro.add(txtOrgan);
        panelCentro.add(btnVerificar);
        panelCentro.add(btnCrear);
        panelCentro.add(btnRechazar);
        panelCentro.add(btnEliminar);

        // --- Panel inferior: historial e info
        modeloHistorial.clear();
        listaHistorial = new JList<>(modeloHistorial);
        listaHistorial.setBorder(BorderFactory.createTitledBorder("📜 Historial de trasplantes"));
        JScrollPane scrollHistorial = new JScrollPane(listaHistorial);

        areaInfo = new JTextArea(5, 40);
        areaInfo.setEditable(false);
        JScrollPane scrollInfo = new JScrollPane(areaInfo);
        scrollInfo.setBorder(BorderFactory.createTitledBorder("🧠 Información"));

        JPanel panelInferior = new JPanel(new GridLayout(1, 2, 10, 10));
        panelInferior.add(scrollHistorial);
        panelInferior.add(scrollInfo);

        add(panelListas, BorderLayout.NORTH);
        add(panelCentro, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);

        cargarTrasplantes();

        btnVerificar.addActionListener(e -> verificarCompatibilidad());
        btnCrear.addActionListener(e -> crearTrasplante());
        btnRechazar.addActionListener(e -> registrarRechazo());
        btnEliminar.addActionListener(e -> eliminarTrasplante());
    }

   private void verificarCompatibilidad() {
    Donante donante = listaDonantes.getSelectedValue();
    Paciente paciente = listaPacientes.getSelectedValue();
    String tipoSolicitado = txtOrgan.getText().trim().toLowerCase();
    areaInfo.setText("");

    if (donante == null || paciente == null) {
        JOptionPane.showMessageDialog(this, "Seleccione un paciente y un donante.",
                "Advertencia", JOptionPane.WARNING_MESSAGE);
        return;
    }

    if (tipoSolicitado.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Ingrese 'Sangre' o el órgano que desea trasplantar.",
                "Aviso", JOptionPane.WARNING_MESSAGE);
        return;
    }

    String sangreDonante = donante.getBloodType().trim();
    String sangrePaciente = paciente.getBloodType().trim();
    String organoDonante = (donante.getOrgano() != null) ? donante.getOrgano().trim().toLowerCase() : "";

    // 🩸 Caso 1️⃣ — Donación de sangre
    if (tipoSolicitado.equalsIgnoreCase("sangre")) {
        if (sangreDonante.equalsIgnoreCase(sangrePaciente)) {
            areaInfo.append("✅ Compatible\n");
            areaInfo.append("Tipo de sangre Donante: " + sangreDonante + "\n");
            areaInfo.append("Tipo de sangre Paciente: " + sangrePaciente + "\n");
            areaInfo.append("Tipo de donación: Sangre\n");
        } else {
            areaInfo.append("❌ Incompatible por tipo de sangre.\n");
            areaInfo.append("Donante: " + sangreDonante + " / Paciente: " + sangrePaciente + "\n");
        }

    // 🫀 Caso 2️⃣ — Donación de órgano
    } else {
        if (organoDonante.isEmpty()) {
            areaInfo.append("❌ Este donante no tiene registrado un órgano específico.\n");
        } else if (!organoDonante.equals(tipoSolicitado)) {
            areaInfo.append("❌ El donante ofrece otro órgano: " + donante.getOrgano() + "\n");
        } else {
            if (sangreDonante.equalsIgnoreCase(sangrePaciente)) {
                areaInfo.append("✅ Compatible\n");
                areaInfo.append("Tipo de sangre Donante: " + sangreDonante + "\n");
                areaInfo.append("Tipo de sangre Paciente: " + sangrePaciente + "\n");
                areaInfo.append("Órgano donado: " + donante.getOrgano() + "\n");
            } else {
                areaInfo.append("❌ Incompatible por tipo de sangre.\n");
                areaInfo.append("Donante: " + sangreDonante + " / Paciente: " + sangrePaciente + "\n");
            }
        }
    }
}


    private void crearTrasplante() {
        Donante d = listaDonantes.getSelectedValue();
        Paciente p = listaPacientes.getSelectedValue();
        String organ = txtOrgan.getText().trim();

        if (d == null || p == null || organ.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecciona donante, paciente y órgano.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Trasplante t = new Trasplante(organ, d, p, "Aprobado", "", new Date());
        if (!t.esCompatible()) {
            JOptionPane.showMessageDialog(this, "El trasplante no es compatible.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        trasplantes.add(t);
        modeloHistorial.addElement(formatoResumen(t));
        guardarTrasplantes();
        areaInfo.setText("✅ Trasplante registrado exitosamente.");
    }

    private void registrarRechazo() {
        int index = listaHistorial.getSelectedIndex();
        if (index == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un trasplante en el historial.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String motivo = JOptionPane.showInputDialog(this, "Motivo del rechazo:");
        if (motivo == null || motivo.trim().isEmpty()) return;

        Trasplante t = trasplantes.get(index);
        t.registrarRechazo(motivo);
        modeloHistorial.set(index, formatoResumen(t));
        guardarTrasplantes();
        areaInfo.setText("⚠️ Rechazo registrado: " + motivo);
    }

    private void eliminarTrasplante() {
        int index = listaHistorial.getSelectedIndex();
        if (index == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un trasplante para eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        trasplantes.remove(index);
        modeloHistorial.remove(index);
        guardarTrasplantes();
        areaInfo.setText("🗑️ Trasplante eliminado correctamente.");
    }

    private void cargarTrasplantes() {
        trasplantes.clear();
        modeloHistorial.clear();

        File archivo = new File(RUTA);
        if (!archivo.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                Trasplante t = Trasplante.fromArchivo(linea);
                if (t != null && t.getDonor() != null && t.getReceiver() != null) {
                    trasplantes.add(t);
                    modeloHistorial.addElement(formatoResumen(t));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void guardarTrasplantes() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA))) {
            for (Trasplante t : trasplantes) {
                bw.write(t.toArchivo());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String formatoResumen(Trasplante t) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String estado = t.getRejectionHistory().isEmpty() ? "Aprobado" : t.getRejectionHistory();
        String motivo = t.getRejectionReason().isEmpty() ? "" : " (" + t.getRejectionReason() + ")";
        return String.format("[%s] %s → %s [%s]%s",
                sdf.format(t.getFecha()),
                t.getDonor().getName(),
                t.getReceiver().getName(),
                estado, motivo);
    }
}
