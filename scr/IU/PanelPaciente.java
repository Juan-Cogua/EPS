package IU;

import javax.swing.*;
import java.util.*;
import model.Paciente;
import model.Cita;
import loaders.PacienteLoader;
import java.awt.*;
import java.util.List;


/**
 * Panel encargado de la gestión de pacientes.
 * Interfaz visual separada de la lógica de persistencia.
 * @version 1.1 
 * @author Juan
 * @author Andres
 */
public class PanelPaciente extends JPanel {

    private JTextArea areaPacientes;
    private JTextField txtNombre, txtEdad, txtID, txtTipoSangre, txtDireccion, txtTelefono, txtPeso, txtAltura, txtAlergias;
    private ArrayList<Paciente> listaPacientes;

    public PanelPaciente() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        listaPacientes = PacienteLoader.cargarPacientes();

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

        panelEntrada.add(new JLabel("Nombre:")); panelEntrada.add(txtNombre);
        panelEntrada.add(new JLabel("Edad:")); panelEntrada.add(txtEdad);
        panelEntrada.add(new JLabel("Identificación:")); panelEntrada.add(txtID);
        panelEntrada.add(new JLabel("Tipo de Sangre:")); panelEntrada.add(txtTipoSangre);
        panelEntrada.add(new JLabel("Dirección:")); panelEntrada.add(txtDireccion);
        panelEntrada.add(new JLabel("Teléfono:")); panelEntrada.add(txtTelefono);
        panelEntrada.add(new JLabel("Peso (kg):")); panelEntrada.add(txtPeso);
        panelEntrada.add(new JLabel("Altura (m):")); panelEntrada.add(txtAltura);
        panelEntrada.add(new JLabel("Alergias (separadas por comas):")); panelEntrada.add(txtAlergias);
        panelEntrada.add(btnAgregar); panelEntrada.add(btnEliminar);

        add(panelEntrada, BorderLayout.NORTH);

        // 🔹 Área de visualización
        areaPacientes = new JTextArea(12, 40);
        areaPacientes.setEditable(false);
        JScrollPane scroll = new JScrollPane(areaPacientes);
        add(scroll, BorderLayout.CENTER);

        actualizarArea();

        // 🔹 Eventos
        btnAgregar.addActionListener(e -> agregarPaciente());
        btnEliminar.addActionListener(e -> eliminarPaciente());
    }

    /**
     * Agrega un paciente nuevo a la lista y al archivo.
     */
    private void agregarPaciente() {
        try {
            String nombre = txtNombre.getText().trim();
            byte edad = Byte.parseByte(txtEdad.getText().trim());
            String id = txtID.getText().trim();
            String tipoSangre = txtTipoSangre.getText().trim();
            String direccion = txtDireccion.getText().trim();
            String telefono = txtTelefono.getText().trim();
            double peso = Double.parseDouble(txtPeso.getText().trim());
            double altura = Double.parseDouble(txtAltura.getText().trim());
            List<String> alergias = new ArrayList<>();

            if (!txtAlergias.getText().trim().isEmpty()) {
                for (String a : txtAlergias.getText().split(",")) {
                    alergias.add(a.trim());
                }
            }

            Paciente nuevo = new Paciente(nombre, edad, id, tipoSangre, direccion, telefono, peso, altura, alergias, new ArrayList<Cita>());

            listaPacientes.add(nuevo);
            PacienteLoader.guardarPacientes(listaPacientes);

            limpiarCampos();
            actualizarArea();
            JOptionPane.showMessageDialog(this, "Paciente agregado correctamente.");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Edad, peso y altura deben ser valores numéricos.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al agregar paciente: " + e.getMessage());
        }
    }

    /**
     * Elimina un paciente por ID y actualiza la vista.
     */
    private void eliminarPaciente() {
        String id = txtID.getText().trim();
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el ID del paciente a eliminar.");
            return;
        }

        boolean eliminado = PacienteLoader.eliminarPaciente(id);
        if (eliminado) {
            listaPacientes = PacienteLoader.cargarPacientes();
            actualizarArea();
            JOptionPane.showMessageDialog(this, "🗑 Paciente eliminado correctamente.");
        } else {
            JOptionPane.showMessageDialog(this, "⚠ No se encontró un paciente con ese ID.");
        }
    }

    /**
     * Muestra la lista de pacientes actual en el área de texto.
     */
    private void actualizarArea() {
        areaPacientes.setText("");
        for (Paciente p : listaPacientes)
            areaPacientes.append(p.toString() + "\n");
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
