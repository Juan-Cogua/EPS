package IU;

import javax.swing.*;
import java.util.*;
import model.Paciente;
import loaders.PacienteLoader;
import java.awt.*;
import java.util.List;
import javax.swing.border.TitledBorder;

/**
 * Panel encargado de la gestión de pacientes.
 * Interfaz visual separada de la lógica de persistencia.
 * @version 1.4 (Mejorado: JComboBox Tipo Sangre, Diseño Armonioso)
 * @author Juan
 * @author Andres
 */
public class PanelPaciente extends JPanel {

    private JTextArea areaPacientes;
    private JTextField txtNombre, txtEdad, txtID, txtDireccion, txtTelefono, txtAlergias;
    private JComboBox<String> cmbTipoSangre; // JComboBox para Tipo de Sangre
    private JSpinner spinnerPeso; 
    private JSpinner spinnerAltura; 
    private ArrayList<Paciente> listaPacientes;

    // Tipos de sangre disponibles
    private static final String[] TIPOS_SANGRE = {"O+", "O-", "A+", "A-", "B+", "B-", "AB+", "AB-"};
    
    // --- Colores y Fuentes Unificadas (Monocromático/Sutil) ---
    private static final Color COLOR_BASE = new Color(245, 245, 250); // Fondo claro
    private static final Color COLOR_CONTENEDOR = Color.WHITE; // Fondo de paneles internos
    private static final Color COLOR_BORDE = new Color(180, 180, 180); // Gris claro
    private static final Color COLOR_BOTON = new Color(80, 100, 120); // Azul Grisáceo Sutil
    private static final Color COLOR_TEXTO_BOTON = Color.WHITE;
    private static final Font FUENTE_TITULO = new Font("Segoe UI", Font.BOLD, 14);

    public PanelPaciente() {
        setLayout(new BorderLayout(15, 15));
        setBackground(COLOR_BASE); 
    
        listaPacientes = PacienteLoader.cargarPacientes();
    
        // 🔹 Panel de entrada (Formulario)
        JPanel panelEntrada = new JPanel(new GridLayout(10, 2, 10, 10)); 
        panelEntrada.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(COLOR_BORDE, 1), 
            "Datos del Paciente", 
            TitledBorder.LEFT, 
            TitledBorder.TOP, 
            FUENTE_TITULO)); 
        panelEntrada.setBackground(COLOR_CONTENEDOR);
    
        txtNombre = new JTextField();
        txtEdad = new JTextField();
        txtID = new JTextField();
        txtDireccion = new JTextField();
        txtTelefono = new JTextField();
        txtAlergias = new JTextField();
        
        cmbTipoSangre = new JComboBox<>(TIPOS_SANGRE);
        cmbTipoSangre.setSelectedIndex(0); 
    
        SpinnerModel modelPeso = new SpinnerNumberModel(70.0, 30.0, 300.0, 0.1);
        spinnerPeso = new JSpinner(modelPeso);
        ((JSpinner.DefaultEditor) spinnerPeso.getEditor()).getTextField().setEditable(true);
    
        SpinnerModel modelAltura = new SpinnerNumberModel(1.70, 0.5, 2.5, 0.01);
        spinnerAltura = new JSpinner(modelAltura);
        ((JSpinner.DefaultEditor) spinnerAltura.getEditor()).getTextField().setEditable(true);
    
        panelEntrada.add(new JLabel("Nombre:"));
        panelEntrada.add(txtNombre);
        panelEntrada.add(new JLabel("Edad:"));
        panelEntrada.add(txtEdad);
        panelEntrada.add(new JLabel("Identificación:"));
        panelEntrada.add(txtID);
        panelEntrada.add(new JLabel("Tipo de Sangre:"));
        panelEntrada.add(cmbTipoSangre); 
        panelEntrada.add(new JLabel("Dirección:"));
        panelEntrada.add(txtDireccion);
        panelEntrada.add(new JLabel("Teléfono:"));
        panelEntrada.add(txtTelefono);
        panelEntrada.add(new JLabel("Peso (kg):"));
        panelEntrada.add(spinnerPeso); 
        panelEntrada.add(new JLabel("Altura (m):"));
        panelEntrada.add(spinnerAltura); 
        panelEntrada.add(new JLabel("Alergias (sep. por ','):"));
        panelEntrada.add(txtAlergias);
    
        // 🔹 Panel de botones y lista (compacto)
        JPanel panelInferior = new JPanel(new BorderLayout(10, 10));
        panelInferior.setBackground(COLOR_BASE);
    
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBotones.setBackground(COLOR_BASE);
        JButton btnAgregar = new JButton("Agregar Paciente");
        JButton btnEliminar = new JButton("Eliminar Paciente");
        btnAgregar.setBackground(COLOR_BOTON);
        btnAgregar.setForeground(COLOR_TEXTO_BOTON);
        btnEliminar.setBackground(COLOR_BOTON);
        btnEliminar.setForeground(COLOR_TEXTO_BOTON);
        panelBotones.add(btnAgregar);
        panelBotones.add(btnEliminar);
    
        areaPacientes = new JTextArea(10, 40);
        areaPacientes.setEditable(false);
        JScrollPane scrollPacientes = new JScrollPane(areaPacientes);
        scrollPacientes.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(COLOR_BORDE, 1), 
            "Lista de Pacientes", 
            TitledBorder.LEFT, 
            TitledBorder.TOP,
            FUENTE_TITULO));
    
        panelInferior.add(panelBotones, BorderLayout.NORTH);
        panelInferior.add(scrollPacientes, BorderLayout.CENTER);
    
        // 🔹 Organización final del Panel
        add(panelEntrada, BorderLayout.NORTH);
        add(panelInferior, BorderLayout.CENTER);
    
        actualizarArea();
    
        btnAgregar.addActionListener(e -> agregarPaciente());
        btnEliminar.addActionListener(e -> eliminarPaciente());
    }

    /**
     * Agrega un nuevo paciente a la lista y al archivo.
     */
    private void agregarPaciente() {
        try {
            String nombre = txtNombre.getText().trim();
            byte edad = Byte.parseByte(txtEdad.getText().trim());
            String id = txtID.getText().trim();
            
            // Obtener valor del JComboBox
            String tipoSangre = (String) cmbTipoSangre.getSelectedItem(); 
            
            String direccion = txtDireccion.getText().trim();
            String telefono = txtTelefono.getText().trim();
            
            double peso = ((Number) spinnerPeso.getValue()).doubleValue();
            double altura = ((Number) spinnerAltura.getValue()).doubleValue();
            
            String alergiasStr = txtAlergias.getText().trim();
            List<String> alergiasList = new ArrayList<>();
            if (!alergiasStr.isEmpty()) {
                for (String a : alergiasStr.split(",")) alergiasList.add(a.trim());
            }

            Paciente nuevo = new Paciente(nombre, edad, id, tipoSangre, direccion, telefono,
                                          peso, altura, alergiasList, new ArrayList<>());

            PacienteLoader.agregarPaciente(nuevo);
            listaPacientes = PacienteLoader.cargarPacientes(); 

            limpiarCampos();
            actualizarArea();
            JOptionPane.showMessageDialog(this, "Paciente agregado correctamente.");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error de formato: Edad, Peso o Altura deben ser números válidos.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al agregar paciente: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
            JOptionPane.showMessageDialog(this, "Paciente eliminado correctamente.");
        } else {
            JOptionPane.showMessageDialog(this, "No se encontró un paciente con ese ID.");
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
        cmbTipoSangre.setSelectedIndex(0); // Reset ComboBox
        txtDireccion.setText("");
        txtTelefono.setText("");
        spinnerPeso.setValue(70.0);
        spinnerAltura.setValue(1.70);
        txtAlergias.setText("");
    }
}