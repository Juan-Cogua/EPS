package IU;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;
import model.Donante;
import loaders.DonanteLoader;

public class PanelDonante extends JPanel {

    private JTextField txtNombre, txtEdad, txtID, txtDireccion, txtTelefono, txtTipoDonacion, txtSalud;
    private JComboBox<String> cmbTipoSangre, cmbOrgano;
    private JCheckBox chkElegible;
    private JTextArea areaDonantes;
    private List<Donante> listaDonantes;

    private static final String[] TIPOS_SANGRE = {"A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-"};

    public PanelDonante() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.LIGHT_GRAY);

        listaDonantes = DonanteLoader.cargarDonantes();

        // --- Panel de Entrada ---
        JPanel panelEntrada = new JPanel(new GridLayout(10, 2, 10, 10));
        panelEntrada.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.DARK_GRAY, 1),
            "Datos del Donante",
            TitledBorder.LEFT,
            TitledBorder.TOP
        ));
        panelEntrada.setBackground(Color.WHITE);

        txtNombre = new JTextField();
        txtEdad = new JTextField();
        txtID = new JTextField();
        txtDireccion = new JTextField();
        txtTelefono = new JTextField();
        txtTipoDonacion = new JTextField();
        txtSalud = new JTextField();
        chkElegible = new JCheckBox("Elegible");

        cmbTipoSangre = new JComboBox<>(TIPOS_SANGRE);
        cmbOrgano = new JComboBox<>(Donante.getOrganosDisponibles().toArray(new String[0]));

        panelEntrada.add(new JLabel("Nombre:"));
        panelEntrada.add(txtNombre);
        panelEntrada.add(new JLabel("Edad:"));
        panelEntrada.add(txtEdad);
        panelEntrada.add(new JLabel("ID:"));
        panelEntrada.add(txtID);
        panelEntrada.add(new JLabel("Teléfono:"));
        panelEntrada.add(txtTelefono);
        panelEntrada.add(new JLabel("Dirección:"));
        panelEntrada.add(txtDireccion);
        panelEntrada.add(new JLabel("Estado de Salud:"));
        panelEntrada.add(txtSalud);
        panelEntrada.add(new JLabel("Tipo de Sangre:"));
        panelEntrada.add(cmbTipoSangre);
        panelEntrada.add(new JLabel("Tipo de Donación:"));
        panelEntrada.add(txtTipoDonacion);
        panelEntrada.add(new JLabel("Órgano a Donar:"));
        panelEntrada.add(cmbOrgano);
        panelEntrada.add(new JLabel("Elegible:"));
        panelEntrada.add(chkElegible);

        // --- Panel de Botones ---
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton btnAgregar = new JButton("Agregar Donante");
        JButton btnEliminar = new JButton("Eliminar Donante");
        panelBotones.add(btnAgregar);
        panelBotones.add(btnEliminar);

        // --- Área de Donantes ---
        areaDonantes = new JTextArea(15, 40);
        areaDonantes.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(areaDonantes);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.DARK_GRAY, 1),
            "Lista de Donantes",
            TitledBorder.LEFT,
            TitledBorder.TOP
        ));

        // --- Panel Superior ---
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBackground(Color.LIGHT_GRAY);
        panelSuperior.add(panelEntrada, BorderLayout.NORTH);
        panelSuperior.add(panelBotones, BorderLayout.SOUTH);

        add(panelSuperior, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // --- Listeners ---
        btnAgregar.addActionListener(e -> agregarDonante());
        btnEliminar.addActionListener(e -> eliminarDonante());

        actualizarArea();
    }

    private void agregarDonante() {
        try {
            String nombre = txtNombre.getText().trim();
            byte edad = Byte.parseByte(txtEdad.getText().trim());
            String id = txtID.getText().trim();
            String tipoSangre = (String) cmbTipoSangre.getSelectedItem();
            String direccion = txtDireccion.getText().trim();
            String telefono = txtTelefono.getText().trim();
            String tipoDonacion = txtTipoDonacion.getText().trim();
            String salud = txtSalud.getText().trim();
            boolean elegible = chkElegible.isSelected();
            String organo = (String) cmbOrgano.getSelectedItem();

            Donante nuevo = new Donante(nombre, edad, id, tipoSangre, direccion, telefono, 
                                        tipoDonacion, salud, elegible, organo);

            DonanteLoader.agregarDonante(nuevo);
            listaDonantes = DonanteLoader.cargarDonantes();

            limpiarCampos();
            actualizarArea();
            JOptionPane.showMessageDialog(this, "Donante agregado correctamente.");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "La edad tiene un formato inválido.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
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
            JOptionPane.showMessageDialog(this, "No se encontró un donante con ese ID.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarArea() {
        areaDonantes.setText("");
        for (Donante d : listaDonantes) {
            areaDonantes.append(d.toString() + "\n");
        }
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtEdad.setText("");
        txtID.setText("");
        cmbTipoSangre.setSelectedIndex(0);
        txtDireccion.setText("");
        txtTelefono.setText("");
        txtTipoDonacion.setText("");
        txtSalud.setText("");
        cmbOrgano.setSelectedIndex(0);
        chkElegible.setSelected(false);
    }
}