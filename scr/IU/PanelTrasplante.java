package IU;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.awt.*;
import java.util.List;
   
import model.Trasplante;
import model.Donante;
import model.Paciente;
import loaders.TrasplanteLoader;
import loaders.DonanteLoader; 
import loaders.PacienteLoader; 


/**
 * Panel para la gestión de trasplantes.
 * Implementa el nuevo campo "Historial Clínico", usa JComboBox para el estado 
 * y realiza la persistencia mediante IDs, corrigiendo el error del constructor.
 * @version 1.3 (Corregido constructor de 7 argumentos y uso de IDs)
 * @author Juan
 * @author Andres
 */
public class PanelTrasplante extends JPanel {

    // Componentes de entrada
    private JTextField txtOrgano, txtDonante, txtReceptor, txtHistorial, txtMotivo, txtFecha;
    private JComboBox<String> cmbEstado; 
    private JTextArea areaTrasplantes;
    private List<Trasplante> listaTrasplantes;

    private static final SimpleDateFormat FORMATO_FECHA = new SimpleDateFormat("dd/MM/yyyy");

    // --- Colores y Fuentes Unificadas (Monocromático/Sutil) ---
    private static final Color COLOR_BASE = new Color(245, 245, 250); // Fondo claro
    private static final Color COLOR_CONTENEDOR = Color.WHITE; // Fondo de paneles internos
    private static final Color COLOR_BORDE = new Color(180, 180, 180); // Gris claro
    private static final Color COLOR_BOTON = new Color(80, 100, 120); // Azul Grisáceo Sutil
    private static final Color COLOR_TEXTO_BOTON = Color.WHITE;
    private static final Font FUENTE_TITULO = new Font("Segoe UI", Font.BOLD, 14);


    public PanelTrasplante() {
        setLayout(new BorderLayout(10, 10));
        setBackground(COLOR_BASE);

        // 1. Cargar datos iniciales
        listaTrasplantes = TrasplanteLoader.cargarTrasplantes();

        // 2. Configuración del Panel de Entrada (7 FILAS TOTALES para los 7 campos)
        JPanel panelEntrada = new JPanel(new GridLayout(7, 2, 5, 5));
        panelEntrada.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(COLOR_BORDE, 1), 
            "Gestión de Trasplantes", 
            TitledBorder.LEFT, 
            TitledBorder.TOP, 
            FUENTE_TITULO));
        panelEntrada.setBackground(COLOR_CONTENEDOR);

        // Inicializar componentes
        txtOrgano = new JTextField();
        txtDonante = new JTextField(); // Recibe el ID del Donante
        txtReceptor = new JTextField(); // Recibe el ID del Receptor
        cmbEstado = new JComboBox<>(new String[]{"Pendiente", "Aprobado", "Rechazado"});
        txtHistorial = new JTextField(); // Corresponde al 5º argumento del constructor (Historial Clínico)
        txtMotivo = new JTextField();    // Corresponde al 6º argumento del constructor (Motivo/Rechazo)
        txtFecha = new JTextField(FORMATO_FECHA.format(new Date())); // 7º argumento

        // Añadir componentes al Panel de Entrada
        panelEntrada.add(new JLabel("Órgano:"));
        panelEntrada.add(txtOrgano);
        panelEntrada.add(new JLabel("ID Donante:")); 
        panelEntrada.add(txtDonante);
        panelEntrada.add(new JLabel("ID Receptor:")); 
        panelEntrada.add(txtReceptor);
        panelEntrada.add(new JLabel("Estado:"));
        panelEntrada.add(cmbEstado); 
        panelEntrada.add(new JLabel("Historial Clínico:"));
        panelEntrada.add(txtHistorial); 
        panelEntrada.add(new JLabel("Motivo Rechazo (si aplica):")); // Mejoramos el nombre
        panelEntrada.add(txtMotivo);
        panelEntrada.add(new JLabel("Fecha (dd/MM/yyyy):"));
        panelEntrada.add(txtFecha);
        
        // Botones
        JButton btnAgregar = new JButton("Agregar Trasplante");
        JButton btnEliminar = new JButton("Eliminar Trasplante (por ID)");
        JButton btnActualizar = new JButton("Actualizar Lista");
        
        // Aplicar estilo de botón unificado
        btnAgregar.setBackground(COLOR_BOTON);
        btnAgregar.setForeground(COLOR_TEXTO_BOTON);
        btnEliminar.setBackground(COLOR_BOTON);
        btnEliminar.setForeground(COLOR_TEXTO_BOTON);
        btnActualizar.setBackground(COLOR_BOTON);
        btnActualizar.setForeground(COLOR_TEXTO_BOTON);
        
        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.setBackground(COLOR_BASE);
        panelBotones.add(btnAgregar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnActualizar);
        
        // Área de visualización
        areaTrasplantes = new JTextArea();
        areaTrasplantes.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(areaTrasplantes);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(COLOR_BORDE, 1), 
            "Lista de Trasplantes", 
            TitledBorder.LEFT, 
            TitledBorder.TOP,
            FUENTE_TITULO));
        
        // 3. Configuración del Layout
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBackground(COLOR_BASE);
        panelSuperior.add(panelEntrada, BorderLayout.NORTH);
        panelSuperior.add(panelBotones, BorderLayout.SOUTH);

        add(panelSuperior, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        
        // 4. Configurar Listeners
        btnAgregar.addActionListener(e -> agregarTrasplante());
        btnEliminar.addActionListener(e -> eliminarTrasplante());
        btnActualizar.addActionListener(e -> actualizarArea());

        // Cargar datos al inicio
        actualizarArea();
    }

    // --- Lógica de Negocio ---

    private void agregarTrasplante() {
        try {
            String organo = txtOrgano.getText().trim();
            String idDonante = txtDonante.getText().trim(); 
            String idReceptor = txtReceptor.getText().trim(); 
            String estado = cmbEstado.getSelectedItem().toString(); // 4º argumento
            String historial = txtHistorial.getText().trim(); // 5º argumento
            String motivo = txtMotivo.getText().trim(); // 6º argumento
            Date fecha = FORMATO_FECHA.parse(txtFecha.getText().trim()); // 7º argumento

            // 1. Búsqueda por ID (Corregida: usa los Loaders con la función buscarPorId)
            Donante donante = DonanteLoader.buscarDonantePorId(idDonante);
            Paciente receptor = PacienteLoader.buscarPacientePorId(idReceptor);

            if (donante == null) {
                JOptionPane.showMessageDialog(this, "Error: ID de Donante no encontrado (" + idDonante + ").", "Error de Búsqueda", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // ⚠️ Error de Paciente receptor corregido aquí
            if (receptor == null) {
                JOptionPane.showMessageDialog(this, "Error: ID de Receptor no encontrado (" + idReceptor + ").", "Error de Búsqueda", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 2. Llamada al constructor de 7 argumentos (ERROR CORREGIDO)
            Trasplante nuevo = new Trasplante(organo, donante, receptor, estado, historial, motivo, fecha); 
            
            listaTrasplantes.add(nuevo);
            TrasplanteLoader.guardarTrasplantes(listaTrasplantes);

            limpiarCampos();
            actualizarArea();
            JOptionPane.showMessageDialog(this, "Trasplante agregado correctamente.");
        } catch (ParseException e) {
             JOptionPane.showMessageDialog(this, "Error: Formato de fecha inválido (use dd/MM/yyyy).", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al agregar trasplante: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarTrasplante() {
        String idDonante = txtDonante.getText().trim(); 
        String idReceptor = txtReceptor.getText().trim(); 

        if (idDonante.isEmpty() || idReceptor.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar ID de donante y ID de receptor para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Llamada a la función del Loader corregida para usar IDs
        boolean eliminado = TrasplanteLoader.eliminarTrasplante(idDonante, idReceptor); 
        if (eliminado) {
            listaTrasplantes = TrasplanteLoader.cargarTrasplantes();
            actualizarArea();
            JOptionPane.showMessageDialog(this, "Trasplante eliminado correctamente.");
        } else {
            JOptionPane.showMessageDialog(this, "No se encontró un trasplante con esos IDs.");
        }
    }

    private void actualizarArea() {
        listaTrasplantes = TrasplanteLoader.cargarTrasplantes(); // Recargar para asegurar el estado actual
        areaTrasplantes.setText("--- LISTA DE TRASPLANTES REGISTRADOS ---\n\n");
        if (listaTrasplantes != null) {
            for (Trasplante t : listaTrasplantes) {
                areaTrasplantes.append(t.resumen() + "\n\n");
            }
        }
    }

    private void limpiarCampos() {
        txtOrgano.setText("");
        txtDonante.setText("");
        txtReceptor.setText("");
        cmbEstado.setSelectedIndex(0); 
        txtHistorial.setText("");
        txtMotivo.setText("");
        txtFecha.setText(FORMATO_FECHA.format(new Date()));
    }
}