package IU;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.*;
import model.Cita;
import model.Paciente;
import loaders.CitaLoader;
import loaders.PacienteLoader;
import java.awt.*;
import java.util.List;
import javax.swing.border.TitledBorder;

/**Panel para la gestión de citas médicas.
 * @version 2.0
 * @author Juan
 * @author Andres
 */
public class PanelCita extends JPanel {

    private JTextField txtFecha, txtHora, txtLugar, txtIdPaciente, txtDoctor, txtIdCita; 
    private JTextArea areaCitas;
    private List<Cita> listaCitas;

    private static final SimpleDateFormat FORMATO_FECHA = new SimpleDateFormat("dd/MM/yyyy");
    private static final SimpleDateFormat FORMATO_HORA = new SimpleDateFormat("HH:mm");

    // --- Colores y Fuentes Unificadas (Monocromático/Sutil) ---
    private static final Color COLOR_BASE = new Color(245, 245, 250); 
    private static final Color COLOR_CONTENEDOR = Color.WHITE; 
    private static final Color COLOR_BORDE = new Color(180, 180, 180); 
    private static final Color COLOR_BOTON = new Color(80, 100, 120); 
    private static final Color COLOR_TEXTO_BOTON = Color.WHITE;
    private static final Font FUENTE_TITULO = new Font("Segoe UI", Font.BOLD, 14);


    /**metodo que maneja las funciones del panel de cita:
     * -carga y modifica el csv.
     * -manejo de botones
     * -amanejo de formularios para la entrada de informacion.
     * -color, y distribucion de los objetos en el panel(botones,formularios,scrolls, panel de listas).
     */
    public PanelCita() {
        setLayout(new BorderLayout(15, 15)); 
        setBackground(COLOR_BASE); 

        listaCitas = CitaLoader.cargarCitas();

        // Panel superior (Contenedor que apila Formulario y Botones)
        JPanel panelSuperior = new JPanel();
        panelSuperior.setLayout(new BoxLayout(panelSuperior, BoxLayout.Y_AXIS)); 
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); 
        panelSuperior.setBackground(COLOR_BASE);

        // --- Panel de entrada (Formulario) ---
        JPanel panelEntrada = new JPanel(new GridLayout(7, 2, 10, 10)); 
        panelEntrada.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(COLOR_BORDE, 1), 
            "Programación de Citas", 
            TitledBorder.LEFT, 
            TitledBorder.TOP, 
            FUENTE_TITULO)); 
        panelEntrada.setBackground(COLOR_CONTENEDOR);

        txtIdCita = new JTextField(); 
        txtFecha = new JTextField();
        txtHora = new JTextField();
        txtLugar = new JTextField();
        txtIdPaciente = new JTextField();
        txtDoctor = new JTextField(); 

        panelEntrada.add(new JLabel("ID Cita:")); 
        panelEntrada.add(txtIdCita);
        panelEntrada.add(new JLabel("Fecha (dd/MM/yyyy):"));
        panelEntrada.add(txtFecha);
        panelEntrada.add(new JLabel("Hora (HH:mm):"));
        panelEntrada.add(txtHora);
        panelEntrada.add(new JLabel("Lugar:"));
        panelEntrada.add(txtLugar);
        panelEntrada.add(new JLabel("ID Paciente:"));
        panelEntrada.add(txtIdPaciente);
        panelEntrada.add(new JLabel("Doctor/Especialista:"));
        panelEntrada.add(txtDoctor);

        // --- Panel de botones ---
    JButton btnAgregar = new JButton("Agregar Cita");
    JButton btnEliminar = new JButton("Eliminar Cita (por ID)"); 
    JButton btnActualizar = new JButton("Actualizar Lista");
        
        btnAgregar.setBackground(COLOR_BOTON);
        btnAgregar.setForeground(COLOR_TEXTO_BOTON);
    btnEliminar.setBackground(COLOR_BOTON);
    btnEliminar.setForeground(COLOR_TEXTO_BOTON);
    btnActualizar.setBackground(COLOR_BOTON);
    btnActualizar.setForeground(COLOR_TEXTO_BOTON);
    

    JPanel panelBotones = new JPanel(new GridLayout(1, 3, 10, 10));
    panelBotones.add(btnAgregar);
    panelBotones.add(btnEliminar);
    panelBotones.add(btnActualizar);
        panelBotones.setBackground(COLOR_BASE);

        panelSuperior.add(panelEntrada);
        panelSuperior.add(Box.createVerticalStrut(10)); 
        panelSuperior.add(panelBotones);


        // --- Panel de listas (Áreas de resultado) ---
        JPanel panelResultados = new JPanel(new GridLayout(2, 1, 10, 10));
        panelResultados.setBackground(COLOR_BASE);
        
        areaCitas = new JTextArea(15, 80);
        areaCitas.setEditable(false);
        JScrollPane scrollCitas = new JScrollPane(areaCitas);
        scrollCitas.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(COLOR_BORDE, 1), 
            "Lista de Citas", 
            TitledBorder.LEFT, 
            TitledBorder.TOP,
            FUENTE_TITULO));

        panelResultados.add(scrollCitas);
        
        add(panelSuperior, BorderLayout.NORTH); 
        add(panelResultados, BorderLayout.CENTER); 
        
        // Listeners
        actualizarArea();

    btnAgregar.addActionListener(e -> agregarCita());
    btnEliminar.addActionListener(e -> eliminarCita());
    btnActualizar.addActionListener(e -> actualizarArea());
    }

    /**
     * metodo que maneja la funcion de agregar citas a la persistencia de citas.
     */
    private void agregarCita() {
        try {
            String idCita = txtIdCita.getText().trim();
            Date fecha = FORMATO_FECHA.parse(txtFecha.getText().trim());
            Date hora = FORMATO_HORA.parse(txtHora.getText().trim());
            String lugar = txtLugar.getText().trim();
            String idPaciente = txtIdPaciente.getText().trim();
            String doctor = txtDoctor.getText().trim(); 

            if (idCita.isEmpty() || doctor.isEmpty() || idPaciente.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Error: Campos ID Cita, ID Paciente y Doctor son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean idExiste = listaCitas.stream().anyMatch(c -> c.getId().equalsIgnoreCase(idCita));
            if (idExiste) {
                 JOptionPane.showMessageDialog(this, "Error: Ya existe una cita con ID: " + idCita + ". Debe ser único.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }


            Paciente paciente = PacienteLoader.buscarPacientePorId(idPaciente); 

            if (paciente == null) {
                JOptionPane.showMessageDialog(this, "Error: Paciente con ID " + idPaciente + " no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Cita nueva = new Cita(idCita, fecha, hora, lugar, paciente, doctor); 
            // Asegurar estado inicial Pendiente y añadir en memoria
            nueva.setEstado("Pendiente");
            listaCitas.add(nueva);
            CitaLoader.guardarCitas(listaCitas);

            limpiarCampos();
            // Actualizar la vista usando la lista en memoria para evitar recarga inmediata
            actualizarAreaDesdeMemoria();
            JOptionPane.showMessageDialog(this, "Cita agregada correctamente.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al agregar cita: Revise el formato de Fecha (dd/MM/yyyy) y Hora (HH:mm).\nDetalle: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * metodo que maneja la funcion de poder eliminar una cita de la persistencia.
     * 
     */
    private void eliminarCita() {
        String idCita = txtIdCita.getText().trim();

        if (idCita.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el ID de la cita a eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean cancelada = CitaLoader.eliminarCitaPorId(idCita);

        if (cancelada) {
            listaCitas = CitaLoader.cargarCitas();
            actualizarArea();
            JOptionPane.showMessageDialog(this, "La cita con ID " + idCita + " ha sido marcada como Cancelada.");
        } else {
            JOptionPane.showMessageDialog(this, "No se encontró una cita con ID: " + idCita + " o ya estaba Cancelada. Asegúrese del ID.");
        }
    }

    /**
     * metodo que permite modificar la informacion de una cita ya creada ya añadida a la persistencia.
     */
    private void actualizarArea() {
        listaCitas = CitaLoader.cargarCitas(); 
        areaCitas.setText("");

        if (listaCitas.isEmpty()) {
            areaCitas.append("No hay citas registradas.");
        } else {
            for (Cita c : listaCitas) {
                // Formato requerido:
                // (ID Cita: C020) 14/12/2025;15:00;Centro Médico Occidente;Dra. Sofía Mora (ID Paciente: P009)| Estado: Pendiente
                java.text.SimpleDateFormat fFecha = new java.text.SimpleDateFormat("dd/MM/yyyy");
                java.text.SimpleDateFormat fHora = new java.text.SimpleDateFormat("HH:mm");

                String fecha = fFecha.format(c.getDate());
                String hora = fHora.format(c.getTime());
                String lugar = c.getLocation();
                String doctor = c.getDoctor();
                String idPaciente = c.getPaciente().getId();
                String estado = c.getEstado();

                areaCitas.append(String.format("(ID Cita: %s) %s;%s;%s;%s (ID Paciente: %s) | Estado: %s\n",
                        c.getId(), fecha, hora, lugar, doctor, idPaciente, estado));
            }
        }

        CitaLoader.guardarCitas(listaCitas);
    }

    /**
     * Actualiza el área de citas usando la lista en memoria (no recarga desde archivo).
     * 
     */
    private void actualizarAreaDesdeMemoria() {
        areaCitas.setText("");

        if (listaCitas == null || listaCitas.isEmpty()) {
            areaCitas.append("No hay citas registradas.");
            return;
        }

        java.text.SimpleDateFormat fFecha = new java.text.SimpleDateFormat("dd/MM/yyyy");
        java.text.SimpleDateFormat fHora = new java.text.SimpleDateFormat("HH:mm");

        for (Cita c : listaCitas) {
            String fecha = fFecha.format(c.getDate());
            String hora = fHora.format(c.getTime());
            String lugar = c.getLocation();
            String doctor = c.getDoctor();
            String idPaciente = c.getPaciente().getId();
            String estado = c.getEstado();

            areaCitas.append(String.format("(ID Cita: %s) %s;%s;%s;%s (ID Paciente: %s) | Estado: %s\n",
                    c.getId(), fecha, hora, lugar, doctor, idPaciente, estado));
        }
    }
    
    // Eliminado: mostrarHistorialPaciente() — la UI ahora muestra la lista completa de citas en una vista única.

    private void limpiarCampos() {
        txtIdCita.setText("");
        txtFecha.setText("");
        txtHora.setText("");
        txtLugar.setText("");
        txtIdPaciente.setText("");
        txtDoctor.setText("");
    }

    // Métodos auxiliares usados por los tests: delegan en Cita para asegurar que
    // ubicación aparece en el resumen / formato de archivo.
    public String resumenCita(model.Cita c) {
        if (c == null) return "";
        return c.resumen();
    }

    public String toArchivoCita(model.Cita c) {
        if (c == null) return "";
        return c.toArchivo();
    }
}