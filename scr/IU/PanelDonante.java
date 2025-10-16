package IU;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import model.Donante;
import loaders.DonanteLoader;

/**
 * 
 * Panel encargado de la gestión de donantes.
 * 
 * @author Juan
 * @author Andres
 */
public class PanelDonante extends JPanel {

    private JTextArea areaDonantes;
    private JTextField txtNombre, txtEdad, txtID, txtTipoSangre, txtDireccion, txtTelefono, txtTipoDonacion, txtSalud, txtOrgano;
    private JCheckBox chkElegible;
    private ArrayList<Donante> listaDonantes;

    public PanelDonante() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        listaDonantes = DonanteLoader.cargarDonantes();

        JPanel panelEntrada = new JPanel(new GridLayout(11, 2, 5, 5));

        txtNombre = new JTextField();
        txtEdad = new JTextField();
        txtID = new JTextField();
        txtTipoSangre = new JTextField();
        txtDireccion = new JTextField();
        txtTelefono = new JTextField();
        txtTipoDonacion = new JTextField();
        txtOrgano = new JTextField();
        txtSalud = new JTextField();
        chkElegible = new JCheckBox("Apto para donar");

        JButton btnAgregar = new JButton("Agregar Donante");
        JButton btnEliminar = new JButton("Eliminar Donante");

        panelEntrada.add(new JLabel("Nombre:")); panelEntrada.add(txtNombre);
        panelEntrada.add(new JLabel("Edad:")); panelEntrada.add(txtEdad);
        panelEntrada.add(new JLabel("Identificación:")); panelEntrada.add(txtID);
        panelEntrada.add(new JLabel("Tipo de Sangre:")); panelEntrada.add(txtTipoSangre);
        panelEntrada.add(new JLabel("Dirección:")); panelEntrada.add(txtDireccion);
        panelEntrada.add(new JLabel("Teléfono:")); panelEntrada.add(txtTelefono);
        panelEntrada.add(new JLabel("Tipo de Donación:")); panelEntrada.add(txtTipoDonacion);
        panelEntrada.add(new JLabel("Órgano:")); panelEntrada.add(txtOrgano);
        panelEntrada.add(new JLabel("Salud:")); panelEntrada.add(txtSalud);
        panelEntrada.add(chkElegible); panelEntrada.add(new JLabel());
        panelEntrada.add(btnAgregar); panelEntrada.add(btnEliminar);

        add(panelEntrada, BorderLayout.NORTH);

        areaDonantes = new JTextArea(12, 40);
        areaDonantes.setEditable(false);
        add(new JScrollPane(areaDonantes), BorderLayout.CENTER);

        actualizarArea();

        btnAgregar.addActionListener(e -> agregarDonante());
        btnEliminar.addActionListener(e -> eliminarDonante());
    }

    private void agregarDonante() {
        try {
            String nombre = txtNombre.getText().trim();
            byte edad = Byte.parseByte(txtEdad.getText().trim());
            String id = txtID.getText().trim();
            String tipoSangre = txtTipoSangre.getText().trim();
            String direccion = txtDireccion.getText().trim();
            String telefono = txtTelefono.getText().trim();
            String tipoDonacion = txtTipoDonacion.getText().trim();
            String organo = txtOrgano.getText().trim();
            String salud = txtSalud.getText().trim();
            boolean elegible = chkElegible.isSelected();

            Donante nuevo = new Donante(nombre, edad, id, tipoSangre, direccion, telefono,
                    new Date(), tipoDonacion, salud, elegible, organo);

            listaDonantes.add(nuevo);
            DonanteLoader.guardarDonantes(listaDonantes);

            limpiarCampos();
            actualizarArea();
            JOptionPane.showMessageDialog(this, "Donante agregado correctamente.");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "La edad debe ser un número válido.");
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
        txtTipoSangre.setText("");
        txtDireccion.setText("");
        txtTelefono.setText("");
        txtTipoDonacion.setText("");
        txtOrgano.setText("");
        txtSalud.setText("");
        chkElegible.setSelected(false);
    }
}
