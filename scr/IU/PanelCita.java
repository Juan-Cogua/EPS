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
import java.util.stream.Collectors;
import javax.swing.border.TitledBorder;

/**
 * Panel para la gestión de citas médicas.
 * @version 1.9 (Corregido: Historial muestra COMPLETADAS y CANCELADAS)
 * @author Juan
 * @author Andres
 */
public class PanelCita extends JPanel {

    private JTextField txtFecha, txtHora, txtLugar, txtIdPaciente, txtDoctor, txtIdCita; 
    private JTextArea areaCitas;
    private JTextArea areaHistorial;
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
        JButton btnBuscarHistorial = new JButton("Mostrar Historial (por ID Paciente)");
        
        btnAgregar.setBackground(COLOR_BOTON);
        btnAgregar.setForeground(COLOR_TEXTO_BOTON);
        btnEliminar.setBackground(COLOR_BOTON);
        btnEliminar.setForeground(COLOR_TEXTO_BOTON);
        btnBuscarHistorial.setBackground(COLOR_BOTON);
        btnBuscarHistorial.setForeground(COLOR_TEXTO_BOTON);
    

        JPanel panelBotones = new JPanel(new GridLayout(1, 3, 10, 10));
        panelBotones.add(btnAgregar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnBuscarHistorial); 
        panelBotones.setBackground(COLOR_BASE);

        panelSuperior.add(panelEntrada);
        panelSuperior.add(Box.createVerticalStrut(10)); 
        panelSuperior.add(panelBotones);


        // --- Panel de listas (Áreas de resultado) ---
        JPanel panelResultados = new JPanel(new GridLayout(2, 1, 10, 10));
        panelResultados.setBackground(COLOR_BASE);
        
        areaCitas = new JTextArea(10, 40);
        areaCitas.setEditable(false);
        JScrollPane scrollCitas = new JScrollPane(areaCitas);
        scrollCitas.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(COLOR_BORDE, 1), 
            "Citas Pendientes", 
            TitledBorder.LEFT, 
            TitledBorder.TOP,
            FUENTE_TITULO));
        
        areaHistorial = new JTextArea(10, 40);
        areaHistorial.setEditable(false);
        JScrollPane scrollHistorial = new JScrollPane(areaHistorial);
        scrollHistorial.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(COLOR_BORDE, 1), 
            "Historial de Citas", 
            TitledBorder.LEFT, 
            TitledBorder.TOP,
            FUENTE_TITULO));

        panelResultados.add(scrollCitas);
        panelResultados.add(scrollHistorial);
        
        add(panelSuperior, BorderLayout.NORTH); 
        add(panelResultados, BorderLayout.CENTER); 
        
        // Listeners
        actualizarArea();

        btnAgregar.addActionListener(e -> agregarCita());
        btnEliminar.addActionListener(e -> eliminarCita());
        btnBuscarHistorial.addActionListener(e -> mostrarHistorialPaciente()); 
    }

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
            
            listaCitas.add(nueva);
            CitaLoader.guardarCitas(listaCitas);

            limpiarCampos();
            actualizarArea();
            JOptionPane.showMessageDialog(this, "Cita agregada correctamente.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al agregar cita: Revise el formato de Fecha (dd/MM/yyyy) y Hora (HH:mm).\nDetalle: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

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
            JOptionPane.showMessageDialog(this, "Cita pendiente con ID " + idCita + " CANCELADA y movida a historial.");
        } else {
            JOptionPane.showMessageDialog(this, "No se encontró cita pendiente con ID: " + idCita + " o ya no está PENDIENTE.");
        }
    }

    private void actualizarArea() {
        listaCitas = CitaLoader.cargarCitas(); 
        
        areaCitas.setText("");
        areaHistorial.setText("Use el botón 'Mostrar Historial' e ingrese el ID del paciente para ver su historial.");
        
        List<Cita> citasPendientes = listaCitas.stream()
                .filter(c -> c.getEstado().equals("PENDIENTE"))
                .collect(Collectors.toList());
        
        if (citasPendientes.isEmpty()) {
            areaCitas.append("No hay citas pendientes.");
        } else {
            areaCitas.append("--- Citas Pendientes ---\n");
            for (Cita c : citasPendientes) {
                areaCitas.append("(ID Cita: " + c.getId() + ") " + c.resumen() + " (ID Paciente: " + c.getPaciente().getId() + ")\n");
            }
        }
        
        CitaLoader.guardarCitas(listaCitas);
    }
    
    private void mostrarHistorialPaciente() {
        String idPaciente = txtIdPaciente.getText().trim();
        areaHistorial.setText("");
        
        if (idPaciente.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar el ID del paciente para ver su historial.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // CORRECCIÓN CLAVE: El filtro busca cualquier cita que NO sea PENDIENTE.
        // Esto incluye 'COMPLETADA' y 'CANCELADA'.
        List<Cita> historial = listaCitas.stream()
                .filter(c -> c.getPaciente().getId().equalsIgnoreCase(idPaciente) && !c.getEstado().equals("PENDIENTE"))
                .collect(Collectors.toList());

        if (historial.isEmpty()) {
            areaHistorial.setText("No se encontró historial de citas (COMPLETADA/CANCELADA) para el paciente con ID: " + idPaciente);
            return;
        }

        areaHistorial.append("--- Historial de Citas para Paciente ID: " + idPaciente + " ---\n");
        for (Cita c : historial) {
            areaHistorial.append("(ID Cita: " + c.getId() + ") " + c.resumen() + "\n");
        }
    }

    private void limpiarCampos() {
        txtIdCita.setText("");
        txtFecha.setText("");
        txtHora.setText("");
        txtLugar.setText("");
        txtIdPaciente.setText("");
        txtDoctor.setText("");
    }
}