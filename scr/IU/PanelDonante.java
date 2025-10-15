package IU;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Date;
import java.util.List;
import model.Donante;

/**
 * Panel encargado de la gestión de donantes.
 * Carga y muestra los datos desde el archivo Donante.txt
 * Compatible con el nuevo atributo "organo" en la clase Donante.
 */
public class PanelDonante extends JPanel {

    private JTextArea areaDonantes;
    private JTextField txtNombre, txtEdad, txtID, txtTipoSangre, txtDireccion, txtTelefono, txtTipoDonacion, txtSalud, txtOrgano;
    private JCheckBox chkElegible;

    private static final String RUTA_ARCHIVO = "Donante.txt";

    public PanelDonante() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        // 🔹 Panel de entrada
        JPanel panelEntrada = new JPanel(new GridLayout(11, 2, 5, 5));

        txtNombre = new JTextField();
        txtEdad = new JTextField();
        txtID = new JTextField();
        txtTipoSangre = new JTextField();
        txtDireccion = new JTextField();
        txtTelefono = new JTextField();
        txtTipoDonacion = new JTextField();
        txtOrgano = new JTextField(); // 🆕 Nuevo campo
        txtSalud = new JTextField();
        chkElegible = new JCheckBox("Apto para donar");

        JButton btnAgregar = new JButton("Agregar Donante");
        JButton btnEliminar = new JButton("Eliminar Donante");

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
        panelEntrada.add(new JLabel("Tipo de Donación (Sangre/Órganos):"));
        panelEntrada.add(txtTipoDonacion);
        panelEntrada.add(new JLabel("Órgano (si aplica):"));
        panelEntrada.add(txtOrgano);
        panelEntrada.add(new JLabel("Estado de Salud:"));
        panelEntrada.add(txtSalud);
        panelEntrada.add(new JLabel("Elegibilidad:"));
        panelEntrada.add(chkElegible);
        panelEntrada.add(btnAgregar);
        panelEntrada.add(btnEliminar);

        // 🔹 Área de texto
        areaDonantes = new JTextArea(10, 40);
        areaDonantes.setEditable(false);
        JScrollPane scroll = new JScrollPane(areaDonantes);

        add(panelEntrada, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        // 🔹 Cargar datos desde archivo
        Donante.cargarDonantesDesdeArchivo(RUTA_ARCHIVO);
        actualizarListaVisual();

        // 🎯 Acción: Agregar
        btnAgregar.addActionListener(e -> agregarDonante());

        // 🎯 Acción: Eliminar
        btnEliminar.addActionListener(e -> eliminarDonante());
    }

    /**
     * Actualiza el área de texto con la lista de donantes cargados.
     */
    private void actualizarListaVisual() {
        areaDonantes.setText("");
        List<Donante> donantes = Donante.getDonantes();

        if (donantes.isEmpty()) {
            areaDonantes.append("No hay donantes registrados.\n");
        } else {
            for (Donante d : donantes) {
                areaDonantes.append(
                    d.getName() + " | ID: " + d.getId() +
                    " | Edad: " + d.getAge() +
                    " | Sangre: " + d.getBloodType() +
                    " | Tipo: " + d.getDonationType() +
                    (d.getOrgano() != null && !d.getOrgano().trim().isEmpty()
                        ? " (" + d.getOrgano() + ")" : "") +
                    " | Salud: " + d.getHealthStatus() +
                    " | Elegible: " + (d.isEligibility() ? "Sí" : "No") + "\n"
                );
            }
        }
    }

    /**
     * Agrega un nuevo donante y actualiza el archivo.
     */
    private void agregarDonante() {
        try {
            String nombre = txtNombre.getText().trim();
            String edadTexto = txtEdad.getText().trim();
            String id = txtID.getText().trim();
            String sangre = txtTipoSangre.getText().trim();
            String direccion = txtDireccion.getText().trim();
            String telefono = txtTelefono.getText().trim();
            String tipoDonacion = txtTipoDonacion.getText().trim();
            String salud = txtSalud.getText().trim();
            String organo = txtOrgano.getText().trim(); // 🆕
            boolean elegible = chkElegible.isSelected();

            if (nombre.isEmpty() || edadTexto.isEmpty() || id.isEmpty() ||
                sangre.isEmpty() || tipoDonacion.isEmpty() || salud.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Complete todos los campos obligatorios.",
                        "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            byte edad = Byte.parseByte(edadTexto);

            // 🆕 Usar el nuevo constructor con el campo "organo"
            Donante nuevo = new Donante(nombre, edad, id, sangre, direccion, telefono,
                    new Date(), tipoDonacion, salud, elegible, organo);

            Donante.añadir(nuevo);
            Donante.guardarDonantesEnArchivo(RUTA_ARCHIVO);
            actualizarListaVisual();
            limpiarCampos();

            JOptionPane.showMessageDialog(this, "Donante agregado correctamente.",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Edad inválida. Ingrese un número válido.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al agregar donante: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Elimina un donante según su ID.
     */
    private void eliminarDonante() {
        String idEliminar = txtID.getText().trim();

        if (idEliminar.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un ID para eliminar un donante.",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<Donante> donantes = Donante.getDonantes();
        boolean eliminado = donantes.removeIf(d -> d.getId().equalsIgnoreCase(idEliminar));

        if (eliminado) {
            Donante.guardarDonantesEnArchivo(RUTA_ARCHIVO);
            actualizarListaVisual();
            JOptionPane.showMessageDialog(this, "Donante eliminado correctamente.",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "No se encontró un donante con ese ID.",
                    "Error", JOptionPane.ERROR_MESSAGE);
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
        txtTipoDonacion.setText("");
        txtOrgano.setText("");
        txtSalud.setText("");
        chkElegible.setSelected(false);
    }
}
