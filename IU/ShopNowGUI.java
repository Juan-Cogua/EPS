package UI;

import model.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;	

public class EPS_GUI extends JFrame {
    // Componentes principales
    private JTabbedPane tabbedPane;
    private JTextArea displayArea;
    
    // Paneles para cada funcionalidad
    private JPanel pacientePanel;
    private JPanel donantePanel;
    private JPanel citaPanel;
    private JPanel trasplantePanel;
    
    public EPS_GUI() {
        initializeComponents();
        setupLayout();
        setWindowProperties();
    }
    
    private void initializeComponents() {
        tabbedPane = new JTabbedPane();
        displayArea = new JTextArea(10, 50);
        displayArea.setEditable(false);
        displayArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        // Crear paneles
        pacientePanel = createPacientePanel();
        donantePanel = createDonantePanel();
        citaPanel = createCitaPanel();
        trasplantePanel = createTrasplantePanel();
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Agregar pestañas
        tabbedPane.addTab("Pacientes", pacientePanel);
        tabbedPane.addTab("Donantes", donantePanel);
        tabbedPane.addTab("Citas", citaPanel);
        tabbedPane.addTab("Trasplantes", trasplantePanel);
        
        // Panel principal
        add(tabbedPane, BorderLayout.CENTER);
        
        // Area de información en la parte inferior
        JScrollPane scrollPane = new JScrollPane(displayArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Información del Sistema"));
        add(scrollPane, BorderLayout.SOUTH);
        
        // Panel de botones generales
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton clearButton = new JButton("Limpiar Pantalla");
        clearButton.addActionListener(e -> displayArea.setText(""));
        buttonPanel.add(clearButton);
        
        add(buttonPanel, BorderLayout.NORTH);
        
    }
    
    private JPanel createPacientePanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel menuPacientesPanel = new JPanel(new FlowLayout());
        JButton btnElegirPaciente = new JButton("Elegir Paciente");
        JButton btnAñadirPaciente = new JButton("Añadir Paciente");
        JButton btnEliminarPaciente = new JButton("Eliminar Paciente");
        JButton listButton = new JButton("Listar Pacientes");
        menuPacientesPanel.add(btnElegirPaciente);
        menuPacientesPanel.add(btnAñadirPaciente);
        menuPacientesPanel.add(btnEliminarPaciente);

        JPanel cardPacientePanel = new JPanel();
        CardLayout cardPacienteLayout = new CardLayout();
        cardPacientePanel.setLayout(cardPacienteLayout);

        JPanel elegirPacientePanel = new JPanel();

        JPanel añadirPacientePanel = new JPanel(new BorderLayout());
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JTextField nameField = new JTextField(20);
        JTextField idField = new JTextField(20);
        JSpinner ageSpinner = new JSpinner(new SpinnerNumberModel(18, 0, 120, 1));
        JTextField bloodTypeField = new JTextField(20);
        JTextField addressField = new JTextField(20);
        JTextField phoneField = new JTextField(20);
        JSpinner weightSpinner = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 500.0, 0.1));
        JSpinner heightSpinner = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 3.0, 0.01));
        JTextField allergiesField = new JTextField(20);

        int y = 0;
        gbc.gridx = 0; gbc.gridy = y;
        formPanel.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);
        gbc.gridx = 0; gbc.gridy = ++y;
        formPanel.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1;
        formPanel.add(idField, gbc);
        gbc.gridx = 0; gbc.gridy = ++y;
        formPanel.add(new JLabel("Edad:"), gbc);
        gbc.gridx = 1;
        formPanel.add(ageSpinner, gbc);
        gbc.gridx = 0; gbc.gridy = ++y;
        formPanel.add(new JLabel("Tipo de Sangre:"), gbc);
        gbc.gridx = 1;
        formPanel.add(bloodTypeField, gbc);
        gbc.gridx = 0; gbc.gridy = ++y;
        formPanel.add(new JLabel("Dirección:"), gbc);
        gbc.gridx = 1;
        formPanel.add(addressField, gbc);
        gbc.gridx = 0; gbc.gridy = ++y;
        formPanel.add(new JLabel("Teléfono:"), gbc);
        gbc.gridx = 1;
        formPanel.add(phoneField, gbc);
        gbc.gridx = 0; gbc.gridy = ++y;
        formPanel.add(new JLabel("Peso (kg):"), gbc);
        gbc.gridx = 1;
        formPanel.add(weightSpinner, gbc);
        gbc.gridx = 0; gbc.gridy = ++y;
        formPanel.add(new JLabel("Altura (m):"), gbc);
        gbc.gridx = 1;
        formPanel.add(heightSpinner, gbc);
        gbc.gridx = 0; gbc.gridy = ++y;
        formPanel.add(new JLabel("Alergias (separadas por coma):"), gbc);
        gbc.gridx = 1;
        formPanel.add(allergiesField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addButton = new JButton("Agregar Paciente");
        //JButton listButton = new JButton("Listar Pacientes");
        JButton deleteButton = new JButton("Eliminar Paciente");
        JButton showHistoryButton = new JButton("Mostrar Historial");
        

        buttonPanel.add(addButton);
        buttonPanel.add(listButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(showHistoryButton);

        gbc.gridx = 0; gbc.gridy = ++y;
        gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);

        añadirPacientePanel.add(formPanel, BorderLayout.NORTH);

        btnElegirPaciente.addActionListener(e -> {
            List<Paciente> pacientes = Paciente.getPacientes();
            if (pacientes.isEmpty()) {
                displayArea.append("[INFO] No hay pacientes para elegir.\n");
                return;
            }
            String[] opcionesPacientes = new String[pacientes.size()];
            for (int i = 0; i < pacientes.size(); i++) {
                opcionesPacientes[i] = pacientes.get(i).getName() + " (ID: " + pacientes.get(i).getId() + ")";
            }

            String seleccionado = (String) JOptionPane.showInputDialog(
                null,
                "Seleccione un paciente:",
                "Elegir Paciente",
                JOptionPane.PLAIN_MESSAGE,
                null,
                opcionesPacientes,
                opcionesPacientes[0]);

            if (seleccionado != null) {
                Paciente pacienteSeleccionado = null;
                for (Paciente p : pacientes) {
                    String etiqueta = p.getName() + " (ID: " + p.getId() + ")";
                    if (etiqueta.equals(seleccionado)) {
                        pacienteSeleccionado = p;
                        break;
                    }
                }
                if (pacienteSeleccionado != null) {
                    displayArea.append("[INFO] Paciente seleccionado: " + pacienteSeleccionado.resumen() + "\n");
                    Object[] options = {"Crear Cita", "Modificar Datos", "Cancelar"};
                    int opcion = JOptionPane.showOptionDialog(
                        null,
                        "¿Qué acción desea realizar con el paciente?",
                        "Acciones Paciente",
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[0]);

                    switch (opcion) {
                    case 0: // Crear Cita
                        tabbedPane.setSelectedIndex(tabbedPane.indexOfTab("Citas"));
                        displayArea.append("[INFO] Redirigiendo a crear cita...\n");
                        break;
                    case 1: // Modificar Datos
                        JTextField nameFieldMod = new JTextField(pacienteSeleccionado.getName());
                        JTextField idFieldMod = new JTextField(pacienteSeleccionado.getId());
                        JTextField phoneFieldMod = new JTextField(pacienteSeleccionado.getPhone());

                        JPanel modPanel = new JPanel(new GridLayout(0, 2));
                        modPanel.add(new JLabel("Nombre:"));
                        modPanel.add(nameFieldMod);
                        modPanel.add(new JLabel("ID:"));
                        modPanel.add(idFieldMod);
                        modPanel.add(new JLabel("Teléfono:"));
                        modPanel.add(phoneFieldMod);

                        int result = JOptionPane.showConfirmDialog(
                                null,
                                modPanel,
                                "Modificar Datos de Paciente",
                                JOptionPane.OK_CANCEL_OPTION,
                                JOptionPane.PLAIN_MESSAGE);

                        if (result == JOptionPane.OK_OPTION) {
                            pacienteSeleccionado.setName(nameFieldMod.getText().trim());
                            pacienteSeleccionado.setId(idFieldMod.getText().trim());
                            pacienteSeleccionado.setPhone(phoneFieldMod.getText().trim());

                            displayArea.append("[INFO] Datos actualizados: " + pacienteSeleccionado.resumen() + "\n");
                        } else {
                            displayArea.append("[INFO] Modificación cancelada.\n");
                        }
                        break;
                    default:
                        displayArea.append("[INFO] Acción cancelada.\n");
                        break;
                }
            }
        }
    });
        addButton.addActionListener(e -> {
            try {
                String name = nameField.getText().trim();
                String id = idField.getText().trim();
                byte age = ((Integer) ageSpinner.getValue()).byteValue();
                String bloodType = bloodTypeField.getText().trim().toUpperCase();
                String address = addressField.getText().trim();
                String phone = phoneField.getText().replaceAll("\\s+", "");
                double weight = ((Double) weightSpinner.getValue()).doubleValue();
                double height = ((Double) heightSpinner.getValue()).doubleValue();
                String allergiesText = allergiesField.getText().trim();
                List<String> allergies = new ArrayList<>();
                if (!allergiesText.isEmpty()) {
                    for (String allergy : allergiesText.split(",")) {
                        allergies.add(allergy.trim());
                    }
                }

                if (id.length() > 9) {
                    displayArea.append("[ERROR] ID debe tener máximo 9 caracteres.\n");
                    return;
                }
                if (!phone.matches("^\\d{10}$")) {
                    displayArea.append("[ERROR] Teléfono inválido. Debe contener exactamente 10 dígitos numéricos.\n");
                    return;
                }
                if (!bloodType.matches("^(A|B|AB|O)[+-]$")) {
                    displayArea.append("[ERROR] Tipo de sangre inválido. Solo se admiten A, B, AB, O con + o - (ejemplo: A+).\n");
                    return;
                }
                if (name.isEmpty() || id.isEmpty()) {
                    displayArea.append("[ERROR] Nombre e ID son obligatorios.\n");
                    return;
                }
                List<Cita> citas = new ArrayList<>();
                Paciente paciente = new Paciente(name, age, id, bloodType, address, phone,
                        weight, height, allergies, citas);
                boolean agregado = Paciente.añadir(paciente);
                if (agregado) {
                    displayArea.append("[ÉXITO] Paciente agregado: " + paciente.resumen() + "\n");
                    // Limpiar campos
                    nameField.setText("");
                    idField.setText("");
                    ageSpinner.setValue(18);
                    bloodTypeField.setText("");
                    addressField.setText("");
                    phoneField.setText("");
                    weightSpinner.setValue(0.0);
                    heightSpinner.setValue(0.0);
                    allergiesField.setText("");
                } else {
                    displayArea.append("[ERROR] No se pudo agregar el paciente. ID duplicado o error interno.\n");
                }
            } catch (Exception ex) {
                displayArea.append("[ERROR] Excepción al agregar paciente: " + ex.getMessage() + "\n");
            }
        });

        deleteButton.addActionListener(e -> {
            String id = idField.getText().trim();
            if (id.isEmpty()) {
                displayArea.append("[ERROR] Ingrese un ID para eliminar.\n");
                return;
            }
            List<Paciente> pacientes = Paciente.getPacientes();
            Paciente toRemove = null;
            for (Paciente p : pacientes) {
                if (p.getId().equals(id)) {
                    toRemove = p;
                    break;
                }
            }
            if (toRemove != null) {
                Paciente.eliminar(toRemove);
                displayArea.append("[ÉXITO] Paciente eliminado: " + toRemove.getName() + "\n");
            } else {
                displayArea.append("[ERROR] No se encontró paciente con ID: " + id + "\n");
            }
        });

        showHistoryButton.addActionListener(e -> {
            String id = idField.getText().trim();
            if (id.isEmpty()) {
                displayArea.append("[ERROR] Por favor ingrese el ID del paciente.\n");
                return;
            }
            Paciente paciente = null;
            for (Paciente p : Paciente.getPacientes()) {
                if (p.getId().equals(id)) {
                    paciente = p;
                    break;
                }
            }
            if (paciente == null) {
                displayArea.append("[ERROR] No se encontró paciente con ID: " + id + "\n");
                return;
            }
            String historial = paciente.historialCitas();
            displayArea.append("\nHistorial de Citas - " + paciente.getName() + ":\n" + historial + "\n");
        });

        listButton.addActionListener(e -> {
            List<Paciente> pacientes = Paciente.getPacientes();
            displayArea.append("\n=== LISTA DE PACIENTES ===\n");
            if (pacientes.isEmpty()) {
                displayArea.append("No hay pacientes registrados.\n");
            } else {
                for (Paciente p : pacientes) {
                    displayArea.append(p.resumen() + "\n");
                }
            }
            displayArea.append("Total: " + pacientes.size() + " pacientes\n\n");
        });

        cardPacientePanel.add(elegirPacientePanel, "ELEGIR");
        cardPacientePanel.add(añadirPacientePanel, "AÑADIR");

        // Panel eliminar paciente simple
        JPanel eliminarPacientePanel = new JPanel(new BorderLayout());
        JPanel eliminarForm = new JPanel(new FlowLayout());
        JTextField eliminarIdField = new JTextField(20);
        JButton eliminarBtn = new JButton("Eliminar Paciente");
        eliminarForm.add(new JLabel("ID Paciente a eliminar:"));
        eliminarForm.add(eliminarIdField);
        eliminarForm.add(eliminarBtn);
        eliminarPacientePanel.add(eliminarForm, BorderLayout.NORTH);
        eliminarBtn.addActionListener(e -> {
            String id = eliminarIdField.getText().trim();
            if (id.isEmpty()) {
                displayArea.append("[ERROR] Ingrese ID para eliminar.\n");
                return;
            }
            List<Paciente> pacientes = Paciente.getPacientes();
            Paciente toRemove = null;
            for (Paciente p : pacientes) {
                if (p.getId().equals(id)) {
                    toRemove = p;
                    break;
                }
            }
            if (toRemove != null) {
                Paciente.eliminar(toRemove);
                displayArea.append("[ÉXITO] Paciente eliminado: " + toRemove.getName() + "\n");
                eliminarIdField.setText("");
            } else {
                displayArea.append("[ERROR] Paciente no encontrado con ID: " + id + "\n");
            }
        });
        cardPacientePanel.add(eliminarPacientePanel, "ELIMINAR");

        btnElegirPaciente.addActionListener(e -> cardPacienteLayout.show(cardPacientePanel, "ELEGIR"));
        btnAñadirPaciente.addActionListener(e -> cardPacienteLayout.show(cardPacientePanel, "AÑADIR"));
        btnEliminarPaciente.addActionListener(e -> cardPacienteLayout.show(cardPacientePanel, "ELIMINAR"));

        panel.add(menuPacientesPanel, BorderLayout.NORTH);
        panel.add(cardPacientePanel, BorderLayout.CENTER);

        return panel;
    }
    private JPanel createDonantePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Panel de formulario
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Campos del formulario
        JTextField nameField = new JTextField(20);
        JTextField idField = new JTextField(20);
        JSpinner ageSpinner = new JSpinner(new SpinnerNumberModel(18, 0, 120, 1));
        JTextField bloodTypeField = new JTextField(20);
        JTextField addressField = new JTextField(20);
        JTextField phoneField = new JTextField(20);
        JTextField donationTypeField = new JTextField(20);
        JTextField healthStatusField = new JTextField(20);
        JCheckBox eligibilityCheck = new JCheckBox("Elegible");
        
        // Layout del formulario
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1;
        formPanel.add(idField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Edad:"), gbc);
        gbc.gridx = 1;
        formPanel.add(ageSpinner, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Tipo de Sangre:"), gbc);
        gbc.gridx = 1;
        formPanel.add(bloodTypeField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Dirección:"), gbc);
        gbc.gridx = 1;
        formPanel.add(addressField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("Teléfono:"), gbc);
        gbc.gridx = 1;
        formPanel.add(phoneField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 6;
        formPanel.add(new JLabel("Tipo de Donación:"), gbc);
        gbc.gridx = 1;
        formPanel.add(donationTypeField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 7;
        formPanel.add(new JLabel("Estado de Salud:"), gbc);
        gbc.gridx = 1;
        formPanel.add(healthStatusField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 8;
        gbc.gridwidth = 2;
        formPanel.add(eligibilityCheck, gbc);
        
        // Botones
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addButton = new JButton("Agregar Donante");
        JButton listButton = new JButton("Listar Donantes");
        JButton deleteButton = new JButton("Eliminar Donante");
        
        addButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String id = idField.getText().trim();
            
            if (name.isEmpty() || id.isEmpty()) {
                displayArea.append("Error: Nombre e ID son obligatorios.\n");
                return;
            }
            
            byte age = ((Integer) ageSpinner.getValue()).byteValue();
            String bloodType = bloodTypeField.getText().trim();
            String address = addressField.getText().trim();
            String phone = phoneField.getText().trim();
            Date birthDate = new Date(); // Simplificado para el ejemplo
            String donationType = donationTypeField.getText().trim();
            String healthStatus = healthStatusField.getText().trim();
            boolean eligibility = eligibilityCheck.isSelected();
            // Validar ID: máximo 9 caracteres
            if (id.length() > 9) {
                JOptionPane.showMessageDialog(panel, "ID debe tener máximo 9 caracteres.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validar teléfono: 10 dígitos solo números
            if (!phone.matches("^\\d{10}$")) {
                JOptionPane.showMessageDialog(panel, "Teléfono inválido. Debe contener exactamente 10 dígitos numéricos sin espacios.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validar tipo de sangre: A, B, AB, O con + o -
            if (!bloodType.matches("^(A|B|AB|O)[+-]$")) {
                JOptionPane.showMessageDialog(panel, "Tipo de sangre inválido. Solo se admiten A, B, AB y O con + o - (ejemplo: A+).", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (name.isEmpty() || id.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Nombre e ID son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Donante donante = new Donante(name, age, id, bloodType, address, phone, 
                                        birthDate, donationType, healthStatus, eligibility);
            Donante.añadir(donante);
            displayArea.append("Donante agregado: " + name + " (ID: " + id + ")\n");
            
            // Limpiar campos
            nameField.setText("");
            idField.setText("");
            ageSpinner.setValue(18);
            bloodTypeField.setText("");
            addressField.setText("");
            phoneField.setText("");
            donationTypeField.setText("");
            healthStatusField.setText("");
            eligibilityCheck.setSelected(false);
        });
        
        listButton.addActionListener(e -> {
            List<Donante> donantes = Donante.getDonantes();
            displayArea.append("\n=== LISTA DE DONANTES ===\n");
            if (donantes.isEmpty()) {
                displayArea.append("No hay donantes registrados.\n");
            } else {
                for (Donante d : donantes) {
                    displayArea.append("Donante: " + d.getName() + " - ID: " + d.getId() + 
                                    " - Tipo: " + d.getDonationType() + 
                                    " - Elegible: " + (d.isEligibility() ? "Sí" : "No") + "\n");
                }
            }
            displayArea.append("Total: " + donantes.size() + " donantes\n\n");
        });
        
        deleteButton.addActionListener(e -> {
            String id = idField.getText().trim();
            if (id.isEmpty()) {
                displayArea.append("Error: Ingrese un ID para eliminar.\n");
                return;
            }
            
            List<Donante> donantes = Donante.getDonantes();
            Donante toRemove = null;
            for (Donante d : donantes) {
                if (d.getId().equals(id)) {
                    toRemove = d;
                    break;
                }
            }
            
            if (toRemove != null) {
                Donante.eliminar(toRemove);
                displayArea.append("Donante eliminado: " + toRemove.getName() + "\n");
            } else {
                displayArea.append("No se encontró donante con ID: " + id + "\n");
            }
        });
        
        buttonPanel.add(addButton);
        buttonPanel.add(listButton);
        buttonPanel.add(deleteButton);
        
        gbc.gridy = 9;
        formPanel.add(buttonPanel, gbc);
        
        panel.add(formPanel, BorderLayout.NORTH);
        return panel;
    }
    
    private JPanel createCitaPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JTextField pacienteIdField = new JTextField(20);
        JTextField locationField = new JTextField(20);

        SpinnerDateModel dateModel = new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_MONTH);
        JSpinner dateSpinner = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy");
        dateSpinner.setEditor(dateEditor);

        SpinnerDateModel timeModel = new SpinnerDateModel(new Date(), null, null, Calendar.HOUR_OF_DAY);
        JSpinner timeSpinner = new JSpinner(timeModel);
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm");
        timeSpinner.setEditor(timeEditor);

        // Layout formulario
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("ID del Paciente:"), gbc);
        gbc.gridx = 1;
        formPanel.add(pacienteIdField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Ubicación:"), gbc);
        gbc.gridx = 1;
        formPanel.add(locationField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Fecha:"), gbc);
        gbc.gridx = 1;
        formPanel.add(dateSpinner, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Hora:"), gbc);
        gbc.gridx = 1;
        formPanel.add(timeSpinner, gbc);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton createButton = new JButton("Crear Cita");
        JButton listButton = new JButton("Listar Citas");
        JButton confirmButton = new JButton("Confirmar Cita");
        JButton cancelButton = new JButton("Cancelar Cita");

        buttonPanel.add(createButton);
        buttonPanel.add(listButton);
        buttonPanel.add(cancelButton);

        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Acción para crear cita
        createButton.addActionListener(e -> {
            String pacienteId = pacienteIdField.getText().trim();
            String location = locationField.getText().trim();

         // Validar ID paciente: máximo 9 caracteres
            if (pacienteId.length() > 9) {
                JOptionPane.showMessageDialog(panel, "ID del paciente debe tener máximo 9 caracteres.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (pacienteId.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "El ID del paciente es obligatorio.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Buscar paciente
            Paciente paciente = null;
            for (Paciente p : Paciente.getPacientes()) {
                if (p.getId().equals(pacienteId)) {
                    paciente = p;
                    break;
                }
            }
            if (paciente == null) {
                JOptionPane.showMessageDialog(panel, "No se encontró paciente con ID: " + pacienteId, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Date fecha = (Date) dateSpinner.getValue();
            Date hora = (Date) timeSpinner.getValue();

            // Crear cita y agregarla al paciente
            Cita cita = new Cita(fecha, hora, location, paciente);
            paciente.agregarCita(cita);

            JOptionPane.showMessageDialog(panel, "Cita creada: " + cita.resumen(), "Éxito", JOptionPane.INFORMATION_MESSAGE);

            // Limpiar campos
            pacienteIdField.setText("");
            locationField.setText("");
        });

        // Acción para listar citas (en displayArea)
        listButton.addActionListener(e -> {
            displayArea.append("\n=== TODAS LAS CITAS ===\n");
            List<Paciente> pacientes = Paciente.getPacientes();
            int totalCitas = 0;

            for (Paciente p : pacientes) {
                List<Cita> citas = p.getCitas();
                if (!citas.isEmpty()) {
                    displayArea.append("Paciente: " + p.getName() + "\n");
                    for (Cita c : citas) {
                        displayArea.append("  " + c.resumen() + "\n");
                        totalCitas++;
                    }
                }
            }

            if (totalCitas == 0) {
                displayArea.append("No hay citas registradas.\n");
            }
            displayArea.append("Total de citas: " + totalCitas + "\n\n");
        });
        cancelButton.addActionListener(e -> {
            String pacienteId = pacienteIdField.getText().trim();
            if (pacienteId.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Ingrese ID del paciente para cancelar cita.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Paciente paciente = null;
            for (Paciente p : Paciente.getPacientes()) {
                if (p.getId().equals(pacienteId)) {
                    paciente = p;
                    break;
                }
            }
            if (paciente == null) {
                JOptionPane.showMessageDialog(panel, "Paciente no encontrado: " + pacienteId, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            List<Cita> citas = paciente.getCitas();
            if (citas.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Paciente no tiene citas para cancelar.", "Información", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            Cita ultimaCita = citas.get(citas.size() - 1);
            ultimaCita.cancelarCita();
            JOptionPane.showMessageDialog(panel, "Cita cancelada:\n" + ultimaCita.resumen(), "Éxito", JOptionPane.INFORMATION_MESSAGE);
        });
        JButton rescheduleButton = new JButton("Reprogramar Cita");
        buttonPanel.add(rescheduleButton);

        rescheduleButton.addActionListener(e -> {
            String pacienteId = pacienteIdField.getText().trim();
            if (pacienteId.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Ingrese ID del paciente para reprogramar cita.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Paciente paciente = null;
            for (Paciente p : Paciente.getPacientes()) {
                if (p.getId().equals(pacienteId)) {
                    paciente = p;
                    break;
                }
            }
            if (paciente == null) {
                JOptionPane.showMessageDialog(panel, "Paciente no encontrado: " + pacienteId, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            List<Cita> citas = paciente.getCitas();
            if (citas.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Paciente no tiene citas para reprogramar.", "Información", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            Date nuevaFecha = (Date) dateSpinner.getValue();
            Date nuevaHora = (Date) timeSpinner.getValue();

            // Reprogramar la última cita
            Cita ultimaCita = citas.get(citas.size() - 1);
            ultimaCita.reprogramarCita(nuevaFecha, nuevaHora);

            JOptionPane.showMessageDialog(panel, "Cita reprogramada:\n" + ultimaCita.resumen(), "Éxito", JOptionPane.INFORMATION_MESSAGE);
        });
		return panel;

    }


    private JPanel createTrasplantePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        JTextField organTypeField = new JTextField(20);
        JTextField donorField = new JTextField(20);
        JTextField receiverField = new JTextField(20);
        JTextArea rejectionHistoryArea = new JTextArea(3, 20);
        JTextField rejectionReasonField = new JTextField(20);
        
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Tipo de Órgano:"), gbc);
        gbc.gridx = 1;
        formPanel.add(organTypeField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Donante:"), gbc);
        gbc.gridx = 1;
        formPanel.add(donorField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Receptor:"), gbc);
        gbc.gridx = 1;
        formPanel.add(receiverField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Historial de Rechazo:"), gbc);
        gbc.gridx = 1;
        formPanel.add(new JScrollPane(rejectionHistoryArea), gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Razón de Rechazo:"), gbc);
        gbc.gridx = 1;
        formPanel.add(rejectionReasonField, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton createButton = new JButton("Crear Trasplante");
        JButton validateButton = new JButton("Validar Compatibilidad");
        JButton rejectButton = new JButton("Registrar Rechazo");
        
        createButton.addActionListener(e -> {
            String organType = organTypeField.getText().trim();
            String donor = donorField.getText().trim();
            String receiver = receiverField.getText().trim();
            String rejectionHistory = rejectionHistoryArea.getText().trim();
            String rejectionReason = rejectionReasonField.getText().trim();
            
            if (organType.isEmpty() || donor.isEmpty() || receiver.isEmpty()) {
                displayArea.append("Error: Tipo de órgano, donante y receptor son obligatorios.\n");
                return;
            }
            
            Trasplante trasplante = new Trasplante(organType, donor, receiver, 
                                                 rejectionHistory, rejectionReason);
            
            displayArea.append("Trasplante creado: " + trasplante.resumen() + "\n");
            
            // Limpiar campos
            organTypeField.setText("");
            donorField.setText("");
            receiverField.setText("");
            rejectionHistoryArea.setText("");
            rejectionReasonField.setText("");
        });
        
        validateButton.addActionListener(e -> {
            String donor = donorField.getText().trim();
            String receiver = receiverField.getText().trim();
            
            if (donor.isEmpty() || receiver.isEmpty()) {
                displayArea.append("Error: Especifique donante y receptor para validar.\n");
                return;
            }
            
            Trasplante trasplante = new Trasplante("Temporal", donor, receiver, "", "");
            boolean compatible = trasplante.esCompatible();
            
            displayArea.append("Validación de compatibilidad: " + 
                             (compatible ? "COMPATIBLE" : "NO COMPATIBLE") + "\n");
        });
        
        buttonPanel.add(createButton);
        buttonPanel.add(validateButton);
        buttonPanel.add(rejectButton);
        
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);
        
        panel.add(formPanel, BorderLayout.NORTH);
        return panel;
    }
    
    private void setWindowProperties() {
        setTitle("Sistema EPS - Gestión Hospitalaria");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(true);
        
        // Mensaje inicial
        displayArea.setText("=== Sistema EPS Inicializado ===\n" +
                           "Seleccione una pestaña para comenzar a trabajar.\n\n");
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new EPS_GUI().setVisible(true);
        });
    }
}
