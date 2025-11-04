package IU;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import model.Trasplante;
import model.Donante;
import model.Paciente;
import loaders.TrasplanteLoader;
import loaders.DonanteLoader;
import loaders.PacienteLoader;

public class PanelTrasplante extends JPanel {

    private JList<String> listaPacientes, listaDonantes;
    private JTextField txtIdTrasplante, txtHistorial, txtMotivo, txtFecha;
    private JComboBox<String> cmbOrganos, cmbEstado;
    private JTextArea areaTrasplantes;
    private List<Trasplante> listaTrasplantes;

    private static final SimpleDateFormat FORMATO_FECHA = new SimpleDateFormat("dd/MM/yyyy");
    private static final String[] ESTADOS = {"Pendiente", "Aprobado", "Cancelada"};

    public PanelTrasplante() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.LIGHT_GRAY);

        listaTrasplantes = TrasplanteLoader.cargarTrasplantes();

        // --- Parte 1: Listas de Pacientes y Donantes ---
        JPanel panelListas = new JPanel(new GridLayout(1, 2, 10, 10));
        panelListas.setBorder(BorderFactory.createTitledBorder("Pacientes y Donantes"));

        listaPacientes = new JList<>(cargarNombresPacientes());
        listaDonantes = new JList<>(cargarNombresDonantes());

        panelListas.add(new JScrollPane(listaPacientes));
        panelListas.add(new JScrollPane(listaDonantes));

        // --- Parte 2: Formulario ---
        JPanel panelFormulario = new JPanel(new GridLayout(6, 2, 10, 10));
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Formulario de Trasplante"));

        txtIdTrasplante = new JTextField();
        cmbOrganos = new JComboBox<>(Donante.getOrganosDisponibles().toArray(new String[0]));
        cmbEstado = new JComboBox<>(ESTADOS);
        txtHistorial = new JTextField();
        txtMotivo = new JTextField();
        txtFecha = new JTextField(FORMATO_FECHA.format(new Date()));

        panelFormulario.add(new JLabel("ID Trasplante:"));
        panelFormulario.add(txtIdTrasplante);
        panelFormulario.add(new JLabel("Órgano:"));
        panelFormulario.add(cmbOrganos);
        panelFormulario.add(new JLabel("Estado:"));
        panelFormulario.add(cmbEstado);
        panelFormulario.add(new JLabel("Historial Clínico:"));
        panelFormulario.add(txtHistorial);
        panelFormulario.add(new JLabel("Motivo de Rechazo:"));
        panelFormulario.add(txtMotivo);
        panelFormulario.add(new JLabel("Fecha (dd/MM/yyyy):"));
        panelFormulario.add(txtFecha);

        // --- Parte 3: Botones y Lista de Trasplantes ---
        JPanel panelInferior = new JPanel(new BorderLayout(10, 10));
        JPanel panelBotones = new JPanel(new FlowLayout());

        JButton btnAgregar = new JButton("Agregar Trasplante");
        JButton btnEliminar = new JButton("Eliminar Trasplante");
        JButton btnActualizar = new JButton("Actualizar Lista");

        panelBotones.add(btnAgregar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnActualizar);

        areaTrasplantes = new JTextArea(10, 40);
        areaTrasplantes.setEditable(false);
        JScrollPane scrollTrasplantes = new JScrollPane(areaTrasplantes);
        scrollTrasplantes.setBorder(BorderFactory.createTitledBorder("Lista de Trasplantes"));

        panelInferior.add(panelBotones, BorderLayout.NORTH);
        panelInferior.add(scrollTrasplantes, BorderLayout.CENTER);

        // --- Agregar todo al panel principal ---
        add(panelListas, BorderLayout.WEST);
        add(panelFormulario, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);

        // --- Listeners ---
        btnAgregar.addActionListener(e -> agregarTrasplante());
        btnEliminar.addActionListener(e -> eliminarTrasplante());
        btnActualizar.addActionListener(e -> actualizarLista());

        actualizarLista();
    }

    private String[] cargarNombresPacientes() {
        List<Paciente> pacientes = PacienteLoader.cargarPacientes();
        // Formato solicitado: Nombre (ID: Pxxx) | Sangre: T
        return pacientes.stream()
                .map(p -> p.getName() + " (ID: " + p.getId() + ") | Sangre: " + p.getBloodType())
                .toArray(String[]::new);
    }

    private String[] cargarNombresDonantes() {
        List<Donante> donantes = DonanteLoader.cargarDonantes();
        // Formato solicitado: Nombre (ID: Dxxx) | Sangre: T | Dona: Órgano
        return donantes.stream()
                .map(d -> d.getName() + " (ID: " + d.getId() + ") | Sangre: " + d.getBloodType() + " | Dona: " + d.getOrgano())
                .toArray(String[]::new);
    }

    private void agregarTrasplante() {
        try {
            String idTrasplante = txtIdTrasplante.getText().trim();
            String organo = (String) cmbOrganos.getSelectedItem();
            String estado = (String) cmbEstado.getSelectedItem();
            String historial = txtHistorial.getText().trim();
            String motivo = txtMotivo.getText().trim();
            Date fecha = FORMATO_FECHA.parse(txtFecha.getText().trim());

            String pacienteSeleccionado = listaPacientes.getSelectedValue();
            String donanteSeleccionado = listaDonantes.getSelectedValue();

            if (pacienteSeleccionado == null || donanteSeleccionado == null) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar un paciente y un donante.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String idPaciente = pacienteSeleccionado.split("\\(ID: ")[1].replace(")", "");
            String idDonante = donanteSeleccionado.split("\\(ID: ")[1].replace(")", "");

            Paciente paciente = PacienteLoader.buscarPacientePorId(idPaciente);
            Donante donante = DonanteLoader.buscarDonantePorId(idDonante);

            if (paciente == null || donante == null) {
                JOptionPane.showMessageDialog(this, "Paciente o Donante no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Trasplante nuevo = new Trasplante(idTrasplante, organo, donante, paciente, estado, historial, motivo, fecha);
            listaTrasplantes.add(nuevo);
            TrasplanteLoader.guardarTrasplantes(listaTrasplantes);

            limpiarCampos();
            actualizarLista();
            JOptionPane.showMessageDialog(this, "Trasplante agregado correctamente.");
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Formato de fecha inválido. Use dd/MM/yyyy.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al agregar trasplante: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarTrasplante() {
        String idTrasplante = txtIdTrasplante.getText().trim();
        if (idTrasplante.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar el ID del trasplante a eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean eliminado = listaTrasplantes.stream()
                .filter(t -> t.getId().equalsIgnoreCase(idTrasplante))
                .peek(t -> t.setEstado("Cancelada"))
                .count() > 0;

        if (eliminado) {
            TrasplanteLoader.guardarTrasplantes(listaTrasplantes);
            actualizarLista();
            JOptionPane.showMessageDialog(this, "Trasplante eliminado correctamente.");
        } else {
            JOptionPane.showMessageDialog(this, "No se encontró un trasplante con ese ID.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarLista() {
        listaTrasplantes = TrasplanteLoader.cargarTrasplantes(); // Cargar desde el archivo
        areaTrasplantes.setText("");
        Date hoy = new Date();
        for (Trasplante t : listaTrasplantes) {
            if (t.getFecha().before(hoy) && t.getEstado().equals("Pendiente")) {
                t.setEstado("Aprobado");
            }
            areaTrasplantes.append(String.format("Paciente: %s | Donante: %s | Estado: %s | ID: %s | Fecha: %s\n",
                    t.getReceiver().getName(), t.getDonor().getName(), t.getEstado(), t.getId(),
                    FORMATO_FECHA.format(t.getFecha())));
        }
        TrasplanteLoader.guardarTrasplantes(listaTrasplantes); // Guardar cambios en los estados
    }

    private void limpiarCampos() {
        txtIdTrasplante.setText("");
        cmbOrganos.setSelectedIndex(0);
        cmbEstado.setSelectedIndex(0);
        txtHistorial.setText("");
        txtMotivo.setText("");
        txtFecha.setText(FORMATO_FECHA.format(new Date()));
    }
}