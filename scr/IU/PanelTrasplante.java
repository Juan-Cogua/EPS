package IU;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import loaders.PacienteLoader;
import loaders.DonanteLoader;
import model.Trasplante;
import model.Donante;
import model.Paciente;
import loaders.TrasplanteLoader;
import excepciones.TrasplanteInvalidoException;
import excepciones.SangreIncompatibleException;
import excepciones.FechaInvalidaException;
import excepciones.InvariantViolationException;

/**

* PanelTrasplante es un componente Swing que permite gestionar los trasplantes
* de órganos entre donantes y pacientes. Incluye funcionalidad para agregar,
* eliminar y actualizar trasplantes, así como verificar compatibilidad
* sanguínea y formato de fecha.
*
* <p>Este panel combina diferentes secciones:
* <ul>
* <li>Listas de pacientes y donantes</li>
* <li>Formulario de registro de trasplantes</li>
* <li>Área de visualización de trasplantes existentes</li>
* </ul>
* </p>
*
* @author
* @version 1.0
  */
  public class PanelTrasplante extends JPanel {

  private JList<String> listaPacientes, listaDonantes;
  private JTextField txtIdTrasplante, txtHistorial, txtMotivo, txtFecha;
  private JComboBox<String> cmbOrganos, cmbEstado;
  private JTextArea areaTrasplantes;
  private List<Trasplante> listaTrasplantes;

  private static final SimpleDateFormat FORMATO_FECHA = new SimpleDateFormat("dd/MM/yyyy");
  private static final String[] ESTADOS = {"Pendiente", "Aprobado", "Cancelada"};


  /**

  * Constructor principal. Configura la interfaz gráfica, carga datos iniciales y
  * agrega los listeners correspondientes.
    */
    public PanelTrasplante() {
    setLayout(new BorderLayout(10, 10));
    setBackground(Color.LIGHT_GRAY);

    listaTrasplantes = TrasplanteLoader.cargarTrasplantes();

    // --- Listas de Pacientes y Donantes ---
    JPanel panelListas = new JPanel(new GridLayout(1, 2, 10, 10));
    panelListas.setBorder(BorderFactory.createTitledBorder("Pacientes y Donantes"));

    listaPacientes = new JList<>(cargarNombresPacientes());
    listaDonantes = new JList<>(cargarNombresDonantes());

    panelListas.add(new JScrollPane(listaPacientes));
    panelListas.add(new JScrollPane(listaDonantes));

    // --- Formulario de Trasplante ---
    JPanel panelFormulario = new JPanel(new GridLayout(6, 2, 10, 10));
    panelFormulario.setBorder(BorderFactory.createTitledBorder("Formulario de Trasplante"));

    txtIdTrasplante = new JTextField();
    cmbOrganos = new JComboBox<>(Donante.getOrganosDisponibles().toArray(new String[0]));
    cmbEstado = new JComboBox<>(ESTADOS);
    txtHistorial = new JTextField();
    txtMotivo = new JTextField();
    txtFecha = new JTextField(FORMATO_FECHA.format(new Date()));

    panelFormulario.add(new JLabel("ID Trasplante:"));
    panelFormulario.add(txtIdTrasplante);
    panelFormulario.add(new JLabel("Órgano:"));
    panelFormulario.add(cmbOrganos);
    panelFormulario.add(new JLabel("Estado:"));
    panelFormulario.add(cmbEstado);
    panelFormulario.add(new JLabel("Historial Clínico:"));
    panelFormulario.add(txtHistorial);
    panelFormulario.add(new JLabel("Motivo de Rechazo:"));
    panelFormulario.add(txtMotivo);
    panelFormulario.add(new JLabel("Fecha (dd/MM/yyyy):"));
    panelFormulario.add(txtFecha);

    // --- Panel inferior con botones y lista de trasplantes ---
    JPanel panelInferior = new JPanel(new BorderLayout(10, 10));
    JPanel panelBotones = new JPanel(new FlowLayout());

    JButton btnAgregar = new JButton("Agregar Trasplante");
    JButton btnEliminar = new JButton("Eliminar Trasplante");
    JButton btnActualizar = new JButton("Actualizar Lista");

    panelBotones.add(btnAgregar);
    panelBotones.add(btnEliminar);
    panelBotones.add(btnActualizar);

    areaTrasplantes = new JTextArea(10, 40);
    areaTrasplantes.setEditable(false);
    JScrollPane scrollTrasplantes = new JScrollPane(areaTrasplantes);
    scrollTrasplantes.setBorder(BorderFactory.createTitledBorder("Lista de Trasplantes"));

    panelInferior.add(panelBotones, BorderLayout.NORTH);
    panelInferior.add(scrollTrasplantes, BorderLayout.CENTER);

    // --- Integración en el panel principal ---
    add(panelListas, BorderLayout.WEST);
    add(panelFormulario, BorderLayout.CENTER);
    add(panelInferior, BorderLayout.SOUTH);

    // --- Listeners de botones ---
    btnAgregar.addActionListener(e -> {
    try {
    agregarTrasplante();
    } catch (TrasplanteInvalidoException | SangreIncompatibleException | FechaInvalidaException ex) {
    JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
    });
    btnEliminar.addActionListener(e -> eliminarTrasplante());
    btnActualizar.addActionListener(e -> actualizarLista());

    actualizarLista();
    }

  /**

  * Carga los nombres de los pacientes desde el archivo correspondiente.
  * @return un arreglo con los nombres formateados de los pacientes.
    */
    private String[] cargarNombresPacientes() {
    List<Paciente> pacientes = PacienteLoader.cargarPacientes();
    return pacientes.stream()
    .map(p -> p.getName() + " (ID: " + p.getId() + ") | Sangre: " + p.getBloodType())
    .toArray(String[]::new);
    }

  /**

  * Carga los nombres de los donantes desde el archivo correspondiente.
  * @return un arreglo con los nombres formateados de los donantes.
    */
    private String[] cargarNombresDonantes() {
    List<Donante> donantes = DonanteLoader.cargarDonantes();
    return donantes.stream()
    .map(d -> d.getName() + " (ID: " + d.getId() + ") | Sangre: " + d.getBloodType() + " | Dona: " + d.getOrgano())
    .toArray(String[]::new);
    }

  // --- Getters de campos de interfaz ---
  public JTextField getTxtIdTrasplante() { return txtIdTrasplante; }
  public JTextField getTxtHistorial() { return txtHistorial; }
  public JTextField getTxtMotivo() { return txtMotivo; }
  public JTextField getTxtFecha() { return txtFecha; }
  public JComboBox<String> getCmbOrganos() { return cmbOrganos; }
  public JComboBox<String> getCmbEstado() { return cmbEstado; }
  public JList<String> getListaPacientes() { return listaPacientes; }
  public JList<String> getListaDonantes() { return listaDonantes; }
  public JTextArea getAreaTrasplantes() { return areaTrasplantes; }

  /**

  * Agrega un nuevo trasplante verificando la validez de los datos introducidos.
  *
  * @throws TrasplanteInvalidoException si faltan campos o no hay selección de paciente/donante.
  * @throws SangreIncompatibleException si la sangre del donante y del paciente son incompatibles.
  * @throws FechaInvalidaException si la fecha no cumple el formato dd/MM/yyyy.
    */
    public void agregarTrasplante() throws TrasplanteInvalidoException, SangreIncompatibleException, FechaInvalidaException {
    String id = txtIdTrasplante.getText().trim();
    if (id.isEmpty()) {
    throw new TrasplanteInvalidoException("El ID del trasplante no puede estar vacío");
    }

    String donanteLabel = listaDonantes.getSelectedValue();
    String pacienteLabel = listaPacientes.getSelectedValue();
    if (donanteLabel == null || pacienteLabel == null) {
    throw new TrasplanteInvalidoException("Debe seleccionar un donante y un paciente");
    }

    try {
    FORMATO_FECHA.parse(txtFecha.getText().trim());
    } catch (ParseException e) {
    throw new FechaInvalidaException("El formato de fecha debe ser dd/MM/yyyy");
    }

    // Creación de nuevo trasplante
    String organo = (String) cmbOrganos.getSelectedItem();
    String estado = (String) cmbEstado.getSelectedItem();
    String historial = txtHistorial.getText().trim();
    String motivo = txtMotivo.getText().trim();
    Date fecha;
    try {
    fecha = FORMATO_FECHA.parse(txtFecha.getText().trim());
    } catch (ParseException e) {
    throw new FechaInvalidaException("El formato de fecha debe ser dd/MM/yyyy");
    }

    String idPaciente = extraerIdDesdeLabel(pacienteLabel);
    String idDonante = extraerIdDesdeLabel(donanteLabel);

    Paciente paciente;
    Donante donante;
    try {
    paciente = PacienteLoader.buscarPacientePorId(idPaciente);
    } catch (Exception ex) {
    JOptionPane.showMessageDialog(this, "Paciente con ID '" + idPaciente + "' no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
    return;
    }
    try {
    donante = DonanteLoader.buscarDonantePorId(idDonante);
    } catch (Exception ex) {
    JOptionPane.showMessageDialog(this, "Donante con ID '" + idDonante + "' no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
    return;
    }

    Trasplante nuevo = new Trasplante(id, organo, donante, paciente, estado, historial, motivo, fecha);
    try {
        // delega la validación al modelo (incluye compatibilidad sanguínea)
        nuevo.checkInvariant();
        listaTrasplantes.add(nuevo);
        TrasplanteLoader.guardarTrasplantes(listaTrasplantes);
    } catch (SangreIncompatibleException | InvariantViolationException ex) {
        // mostrar mensaje y no crear el trasplante
        JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    limpiarCampos();
    actualizarLista();
    JOptionPane.showMessageDialog(this, "Trasplante agregado correctamente.");
    }

  /**

  * Extrae el tipo de sangre desde una cadena con formato: "... | Sangre: T ...".
  * @param label etiqueta de la lista.
  * @return tipo de sangre encontrado o cadena vacía si no se detecta.
    */


  /**

  * Marca un trasplante como cancelado según el ID ingresado.
  * Si el ID no existe, se muestra un mensaje de error.
    */
    private void eliminarTrasplante() {
    String idTrasplante = txtIdTrasplante.getText().trim();
    if (idTrasplante.isEmpty()) {
    JOptionPane.showMessageDialog(this, "Debe ingresar el ID del trasplante a eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
    return;
    }

    boolean eliminado = listaTrasplantes.stream()
    .filter(t -> t.getId().equalsIgnoreCase(idTrasplante))
    .peek(t -> t.setEstado("Cancelada"))
    .count() > 0;

    if (eliminado) {
    TrasplanteLoader.guardarTrasplantes(listaTrasplantes);
    actualizarLista();
    JOptionPane.showMessageDialog(this, "Trasplante eliminado correctamente.");
    } else {
    JOptionPane.showMessageDialog(this, "No se encontró un trasplante con ese ID.", "Error", JOptionPane.ERROR_MESSAGE);
    }
    }

  /**

  * Actualiza la lista de trasplantes en pantalla y actualiza su estado si la fecha ya pasó.
    */
    private void actualizarLista() {
    listaTrasplantes = TrasplanteLoader.cargarTrasplantes();
    areaTrasplantes.setText("");
    Date hoy = new Date();
    for (Trasplante t : listaTrasplantes) {
    if (t.getFecha().before(hoy) && t.getEstado().equals("Pendiente")) {
    t.setEstado("Aprobado");
    }
    areaTrasplantes.append(resumenTrasplante(t));
    areaTrasplantes.append("\n----------------------------------------\n");
    }
    TrasplanteLoader.guardarTrasplantes(listaTrasplantes);
    }

  /** Limpia todos los campos del formulario. */
  private void limpiarCampos() {
  txtIdTrasplante.setText("");
  cmbOrganos.setSelectedIndex(0);
  cmbEstado.setSelectedIndex(0);
  txtHistorial.setText("");
  txtMotivo.setText("");
  txtFecha.setText(FORMATO_FECHA.format(new Date()));
  }

  /**

  * Extrae el ID de una cadena formateada como: "Nombre (ID: P015) | Sangre: AB+".
  * @param label texto de entrada desde la lista.
  * @return ID extraído o cadena vacía si no se encuentra.
    */
    private String extraerIdDesdeLabel(String label) {
    if (label == null) return "";
    int start = label.indexOf("(ID: ");
    if (start >= 0) {
    int end = label.indexOf(')', start);
    if (end > start) {
    return label.substring(start + 5, end).trim();
    } else {
    int pipe = label.indexOf('|', start);
    if (pipe > start) return label.substring(start + 5, pipe).trim();
    return label.substring(start + 5).trim();
    }
    }
    String[] parts = label.split("\\s+");
    for (String p : parts) {
    if (p.matches("[A-Za-z]\\d+")) return p.trim();
    }
    return parts.length > 0 ? parts[0].trim() : "";
    }

  /**

  * Genera un resumen legible para mostrar la información de un trasplante.
  * @param t trasplante del cual generar el resumen.
  * @return cadena formateada con la información relevante.
    */
    private String resumenTrasplante(Trasplante t) {
    if (t == null) return "";
    String id = t.getId() != null ? t.getId() : "N/A";
    String organo = t.getOrganType() != null ? t.getOrganType() : "N/A";
    String fecha = (t.getFecha() != null) ? FORMATO_FECHA.format(t.getFecha()) : "N/A";
    String estado = t.getEstado() != null ? t.getEstado() : "N/A";

    String donorName = t.getDonor() != null ? t.getDonor().getName() : "N/A";
    String donorId = t.getDonor() != null ? t.getDonor().getId() : "N/A";

    String receiverName = t.getReceiver() != null ? t.getReceiver().getName() : "N/A";
    String receiverId = t.getReceiver() != null ? t.getReceiver().getId() : "N/A";

    String historial = (t.getHistorialClinico() != null && !t.getHistorialClinico().isEmpty()) ? t.getHistorialClinico() : "N/A";
    String motivo = (t.getRejectionReason() != null && !t.getRejectionReason().isEmpty()) ? t.getRejectionReason() : "N/A";

    return String.format("TRASPLANTE ID: %s. Órgano: %s. Fecha: %s. Estado: %s\n" +
    "  > Donante (ID): %s (%s)\n" +
    "  > Receptor (ID): %s (%s)\n" +
    "  > Historial Clínico: %s. Motivo: %s",
    id, organo, fecha, estado,
    donorName, donorId,
    receiverName, receiverId,
    historial, motivo);
    }

    // Método público para recargar las listas desde los txt (llamado por otros paneles)
    public void reloadLists() {
        javax.swing.SwingUtilities.invokeLater(() -> {
            try {
                java.util.List<Paciente> pacientes = PacienteLoader.cargarPacientes();
                java.util.List<Donante> donantes = DonanteLoader.cargarDonantes();

                javax.swing.DefaultListModel<String> modelP = new javax.swing.DefaultListModel<>();
                for (Paciente p : pacientes) {
                    String nombre = (p == null ? "N/A" : (safeName(p)));
                    String id = (p == null ? "N/A" : (p.getId() != null ? p.getId() : "N/A"));
                    String sangre = "";
                    try { sangre = (p.getBloodType() != null ? p.getBloodType() : p.getBloodType()); } catch (Exception ex) { sangre = ""; }

                    String alergias = obtenerAlergias(p);
                    if (alergias == null || alergias.trim().isEmpty()) alergias = "Ninguna";

                    modelP.addElement(nombre + " (ID: " + id + ") | Sangre: " + sangre + " | Alergias: " + alergias);
                }

                javax.swing.DefaultListModel<String> modelD = new javax.swing.DefaultListModel<>();
                for (Donante d : donantes) {
                    String nombre = (d == null ? "N/A" : (d.getName() != null ? d.getName() : d.getName()));
                    String id = (d == null ? "N/A" : (d.getId() != null ? d.getId() : "N/A"));
                    String sangre = "";
                    try { sangre = (d.getBloodType() != null ? d.getBloodType() : d.getBloodType()); } catch (Exception ex) { sangre = ""; }
                    String organo = "";
                    try { organo = (d.getOrgano() != null ? d.getOrgano() : ""); } catch (Exception ex) { organo = ""; }
                    modelD.addElement(nombre + " (ID: " + id + ") | Sangre: " + sangre + " | Dona: " + organo);
                }

                if (listaPacientes != null) listaPacientes.setModel(modelP);
                if (listaDonantes != null) listaDonantes.setModel(modelD);

                revalidate();
                repaint();
                System.out.println("reloadLists(): pacientes=" + modelP.getSize() + " donantes=" + modelD.getSize());
            } catch (Exception ex) {
                System.err.println("reloadLists error: " + ex.getMessage());
            }
        });
    }

    // helper: intenta obtener el campo "alergias" probando varios getters por reflexión
    private String obtenerAlergias(Paciente p) {
        if (p == null) return "";
        String[] candidates = {"getAlergias", "getAlergiasString", "getAllergies", "getAllergy", "getAlergia", "getAlergenos"};
        for (String mName : candidates) {
            try {
                java.lang.reflect.Method m = p.getClass().getMethod(mName);
                Object res = m.invoke(p);
                if (res != null) return res.toString();
            } catch (NoSuchMethodException e) {
                // siguiente candidato
            } catch (Exception e) {
                // si falla la invocación, seguir intentando con otros nombres
            }
        }
        // si no hay getter, intentar campo público "alergias" (raro)
        try {
            java.lang.reflect.Field f = p.getClass().getField("alergias");
            Object val = f.get(p);
            if (val != null) return val.toString();
        } catch (Exception ex) {
            // ignorar
        }
        return "";
    }

    // helper para nombre seguro (soporta getName()/getNombre())
    private String safeName(Paciente p) {
        try { if (p.getName() != null) return p.getName(); } catch (Throwable t) {}
        try { if (p.getName() != null) return p.getName(); } catch (Throwable t) {}
        return "N/A";
    }
  }