package IU;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.swing.border.TitledBorder;

import model.Donante;
import loaders.DonanteLoader;

/**
 * Panel encargado de la gestión de donantes.
 * @author Juan
 * @author Andres
 */
public class PanelDonante extends JPanel {

    private JTextArea areaDonantes;
    // Se reemplaza txtTipoSangre por cmbTipoSangre
    private JTextField txtNombre, txtEdad, txtID, txtDireccion, txtTelefono, txtTipoDonacion, txtSalud, txtOrgano, txtFechaNacimiento;
    private JComboBox<String> cmbTipoSangre; // ⚠️ VUELVE EL JCOMBOBOX
    private JCheckBox chkElegible;
    private ArrayList<Donante> listaDonantes;
    
    private static final SimpleDateFormat FORMATO_FECHA = new SimpleDateFormat("dd/MM/yyyy");
    
    // Opciones del JComboBox para Tipo de Sangre
    private static final String[] TIPOS_SANGRE = {"O+", "O-", "A+", "A-", "B+", "B-", "AB+", "AB-"};
    
    // --- Colores y Fuentes Unificadas (Monocromático/Sutil) ---
    private static final Color COLOR_BASE = new Color(245, 245, 250); // Fondo claro
    private static final Color COLOR_CONTENEDOR = Color.WHITE; // Fondo de paneles internos
    private static final Color COLOR_BORDE = new Color(180, 180, 180); // Gris claro
    private static final Color COLOR_BOTON = new Color(80, 100, 120); // Azul Grisáceo Sutil
    private static final Color COLOR_TEXTO_BOTON = Color.WHITE;
    private static final Font FUENTE_TITULO = new Font("Segoe UI", Font.BOLD, 14);


    public PanelDonante() {
        setLayout(new BorderLayout(10, 10));
        setBackground(COLOR_BASE);

        listaDonantes = DonanteLoader.cargarDonantes();

        // 🔹 Panel de entrada (NORTH)
        JPanel panelEntrada = new JPanel(new GridLayout(12, 2, 5, 5)); 
        panelEntrada.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(COLOR_BORDE, 1), 
            "Datos del Donante", 
            TitledBorder.LEFT, 
            TitledBorder.TOP, 
            FUENTE_TITULO)); 
        panelEntrada.setBackground(COLOR_CONTENEDOR);


        txtNombre = new JTextField();
        txtEdad = new JTextField();
        txtID = new JTextField();
        txtDireccion = new JTextField();
        txtTelefono = new JTextField();
        txtTipoDonacion = new JTextField();
        txtOrgano = new JTextField();
        txtSalud = new JTextField();
        txtFechaNacimiento = new JTextField("dd/MM/yyyy"); 
        
        // ⚠️ Inicialización del JComboBox
        cmbTipoSangre = new JComboBox<>(TIPOS_SANGRE);
        cmbTipoSangre.setSelectedIndex(0); 
        
        chkElegible = new JCheckBox("Apto para donar");
        chkElegible.setBackground(COLOR_CONTENEDOR); // Color de fondo del contenedor

        
        panelEntrada.add(new JLabel("Nombre:"));
        panelEntrada.add(txtNombre);
        panelEntrada.add(new JLabel("Edad:"));
        panelEntrada.add(txtEdad);
        panelEntrada.add(new JLabel("ID:"));
        panelEntrada.add(txtID);
        
        // ⚠️ Se añade el JComboBox al panel
        panelEntrada.add(new JLabel("Tipo Sangre:"));
        panelEntrada.add(cmbTipoSangre); 
        
        panelEntrada.add(new JLabel("Dirección:"));
        panelEntrada.add(txtDireccion);
        panelEntrada.add(new JLabel("Teléfono:"));
        panelEntrada.add(txtTelefono);
        
        panelEntrada.add(new JLabel("Fecha Nac. (dd/MM/yyyy):")); 
        panelEntrada.add(txtFechaNacimiento);
        
        panelEntrada.add(new JLabel("Tipo Donación:"));
        panelEntrada.add(txtTipoDonacion);
        panelEntrada.add(new JLabel("Órgano Donado (si aplica):"));
        panelEntrada.add(txtOrgano);
        panelEntrada.add(new JLabel("Estado de Salud:"));
        panelEntrada.add(txtSalud);
        panelEntrada.add(new JLabel("Elegibilidad:"));
        panelEntrada.add(chkElegible);


        JButton btnAgregar = new JButton("Agregar Donante");
        JButton btnEliminar = new JButton("Eliminar Donante (por ID)");
        
        // Aplicar estilo de botón unificado
        btnAgregar.setBackground(COLOR_BOTON);
        btnAgregar.setForeground(COLOR_TEXTO_BOTON);
        btnEliminar.setBackground(COLOR_BOTON);
        btnEliminar.setForeground(COLOR_TEXTO_BOTON);
        
        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.setBackground(COLOR_BASE);
        panelBotones.add(btnAgregar);
        panelBotones.add(btnEliminar);

        areaDonantes = new JTextArea();
        areaDonantes.setEditable(false);
        
        // ✅ JScrollPane para solucionar el error de desbordamiento (Scroll)
        JScrollPane scrollPane = new JScrollPane(areaDonantes);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(COLOR_BORDE, 1), 
            "Lista de Donantes", 
            TitledBorder.LEFT, 
            TitledBorder.TOP,
            FUENTE_TITULO));

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBackground(COLOR_BASE);
        panelSuperior.add(panelEntrada, BorderLayout.NORTH);
        panelSuperior.add(panelBotones, BorderLayout.SOUTH);
        
        add(panelSuperior, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER); 

        btnAgregar.addActionListener(e -> agregarDonante());
        btnEliminar.addActionListener(e -> eliminarDonante());

        actualizarArea();
    }

    private void agregarDonante() {
        try {
            String nombre = txtNombre.getText().trim();
            byte edad = Byte.parseByte(txtEdad.getText().trim());
            String id = txtID.getText().trim();
            
            // ⚠️ Obtener el valor del JComboBox
            String tipoSangre = (String) cmbTipoSangre.getSelectedItem(); 
            
            String direccion = txtDireccion.getText().trim();
            String telefono = txtTelefono.getText().trim();
            String tipoDonacion = txtTipoDonacion.getText().trim();
            String salud = txtSalud.getText().trim();
            boolean elegible = chkElegible.isSelected();
            String organo = txtOrgano.getText().trim();
            
            Date fechaNacimiento = FORMATO_FECHA.parse(txtFechaNacimiento.getText().trim());

            // Llamada al constructor de 11 argumentos
            Donante nuevo = new Donante(nombre, edad, id, tipoSangre, direccion, telefono, 
                                        fechaNacimiento, tipoDonacion, salud, elegible, organo);

            DonanteLoader.agregarDonante(nuevo);
            listaDonantes = DonanteLoader.cargarDonantes(); 

            limpiarCampos();
            actualizarArea();
            JOptionPane.showMessageDialog(this, "Donante agregado correctamente.");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "La edad tiene un formato inválido.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Formato de Fecha de Nacimiento inválido (use dd/MM/yyyy).", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al agregar donante: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void eliminarDonante() {
        String id = txtID.getText().trim();
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el ID del donante a eliminar.");
            return;
        }
        boolean eliminado = DonanteLoader.eliminarDonante(id);
        if (eliminado) {
            listaDonantes = DonanteLoader.cargarDonantes();
            actualizarArea();
            JOptionPane.showMessageDialog(this, "Donante eliminado correctamente.");
        } else {
            JOptionPane.showMessageDialog(this, "No se encontró un donante con ese ID.");
        }
    }

    private void actualizarArea() {
        areaDonantes.setText("");
        for (Donante d : listaDonantes)
            areaDonantes.append(d.toString() + "\n");
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtEdad.setText("");
        txtID.setText("");
        // ⚠️ Restablecer el JComboBox
        cmbTipoSangre.setSelectedIndex(0); 
        txtDireccion.setText("");
        txtTelefono.setText("");
        txtTipoDonacion.setText("");
        txtOrgano.setText("");
        txtSalud.setText("");
        txtFechaNacimiento.setText("dd/MM/yyyy");
        chkElegible.setSelected(false);
    }
}