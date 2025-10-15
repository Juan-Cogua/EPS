package IU;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import model.Paciente;

/**
 * Panel encargado de la gestión de pacientes.
 * Carga y muestra los datos desde el archivo Paciente.txt
 * sin modificar la apariencia visual original.
 */
public class PanelPaciente extends JPanel {

    private JTextArea areaPacientes;
    private JTextField txtNombre, txtID, txtEdad, txtTipoSangre, txtDireccion, txtTelefono, txtPeso, txtAltura, txtAlergias;

    private static final String RUTA_ARCHIVO = "Paciente.txt";

    public PanelPaciente() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        // 🔹 Panel de entrada
        JPanel panelEntrada = new JPanel(new GridLayout(10, 2, 5, 5));

        txtNombre = new JTextField();
        txtEdad = new JTextField();
        txtID = new JTextField();
        txtTipoSangre = new JTextField();
        txtDireccion = new JTextField();
        txtTelefono = new JTextField();
        txtPeso = new JTextField();
        txtAltura = new JTextField();
        txtAlergias = new JTextField();

        JButton btnAgregar = new JButton("Agregar Paciente");
        JButton btnEliminar = new JButton("Eliminar Paciente");

        panelEntrada.add(new JLabel("Nombre:"));
        panelEntrada.add(txtNombre);
        panelEntrada.add(new JLabel("Edad:"));
        panelEntrada.add(txtEdad);
        panelEntrada.add(new JLabel("Identificación:"));
        panelEntrada.add(txtID);
        panelEntrada.add(new JLabel("Tipo de Sangre:"));
        panelEntrada.add(txtTipoSangre);
        panelEntrada.add(new JLabel("Dirección:"));
        panelEntrada.add(txtDireccion);
        panelEntrada.add(new JLabel("Teléfono:"));
        panelEntrada.add(txtTelefono);
        panelEntrada.add(new JLabel("Peso (kg):"));
        panelEntrada.add(txtPeso);
        panelEntrada.add(new JLabel("Altura (m):"));
        panelEntrada.add(txtAltura);
        panelEntrada.add(new JLabel("Alergias (separadas por comas):"));
        panelEntrada.add(txtAlergias);
        panelEntrada.add(btnAgregar);
        panelEntrada.add(btnEliminar);

        // 🔹 Área de texto
        areaPacientes = new JTextArea(10, 40);
        areaPacientes.setEditable(false);
        JScrollPane scroll = new JScrollPane(areaPacientes);

        add(panelEntrada, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        // 🔹 Cargar datos desde el archivo
        Paciente.cargarPacientesDesdeArchivo(RUTA_ARCHIVO);
        actualizarListaVisual();

        // 🎯 Acción: Agregar
        btnAgregar.addActionListener(e -> agregarPaciente());

        // 🎯 Acción: Eliminar
        btnEliminar.addActionListener(e -> eliminarPaciente());
    }

    /**
     * Muestra en el área de texto todos los pacientes cargados.
     */
    private void actualizarListaVisual() {
        areaPacientes.setText("");
        List<Paciente> pacientes = Paciente.getPacientes();

        if (pacientes.isEmpty()) {
            areaPacientes.append("No hay pacientes registrados.\n");
        } else {
            for (Paciente p : pacientes) {
                areaPacientes.append(p.getName() + " | ID: " + p.getId() + " | Edad: " + p.getAge()
                        + " | Sangre: " + p.getBloodType() + "\n");
            }
        }
    }

    /**
     * Crea un nuevo paciente desde los campos de texto y lo agrega al archivo.
     */
    private void agregarPaciente() {
        try {
            String nombre = txtNombre.getText().trim();
            byte edad = Byte.parseByte(txtEdad.getText().trim());
            String id = txtID.getText().trim();
            String tipo = txtTipoSangre.getText().trim();
            String direccion = txtDireccion.getText().trim();
            String telefono = txtTelefono.getText().trim();
            double peso = Double.parseDouble(txtPeso.getText().trim());
            double altura = Double.parseDouble(txtAltura.getText().trim());
            String alergiasTexto = txtAlergias.getText().trim();
            java.util.List<String> alergias = java.util.Arrays.asList(alergiasTexto.split(","));

            Paciente nuevo = new Paciente(nombre, edad, id, tipo, direccion, telefono, peso, altura, alergias, new java.util.ArrayList<>());
            Paciente.añadir(nuevo);

            Paciente.guardarPacientesEnArchivo(RUTA_ARCHIVO);
            actualizarListaVisual();

            limpiarCampos();

            JOptionPane.showMessageDialog(this, "Paciente agregado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor ingrese valores numéricos válidos en Edad, Peso y Altura.",
                    "Error de formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al agregar paciente: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Elimina un paciente según su ID.
     */
    private void eliminarPaciente() {
        String idEliminar = txtID.getText().trim();

        if (idEliminar.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un ID para eliminar un paciente.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<Paciente> pacientes = Paciente.getPacientes();
        boolean eliminado = pacientes.removeIf(p -> p.getId().equalsIgnoreCase(idEliminar));

        if (eliminado) {
            Paciente.guardarPacientesEnArchivo(RUTA_ARCHIVO);
            actualizarListaVisual();
            JOptionPane.showMessageDialog(this, "Paciente eliminado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "No se encontró un paciente con ese ID.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Limpia los campos del formulario.
     */
    private void limpiarCampos() {
        txtNombre.setText("");
        txtEdad.setText("");
        txtID.setText("");
        txtTipoSangre.setText("");
        txtDireccion.setText("");
        txtTelefono.setText("");
        txtPeso.setText("");
        txtAltura.setText("");
        txtAlergias.setText("");
    }
}
