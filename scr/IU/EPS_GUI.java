package IU;

import javax.swing.*;
import java.awt.*;

/**
 * Ventana principal del sistema EPS.
 * Contiene pestañas para gestionar Pacientes, Donantes, Trasplantes y Citas.
 * 
 * @author andres
 * @author Juan Cogua
 * @Versión: 2.0
 */
public class EPS_GUI extends JFrame {

    private JTabbedPane pestañas;
    // <-- mantener referencias a los paneles para notificaciones
    private PanelPaciente panelPaciente;
    private PanelDonante panelDonante;
    private PanelTrasplante panelTrasplante;
    private PanelCita panelCita;

    public EPS_GUI() {
        setTitle("Sistema EPS");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        pestañas = new JTabbedPane();

        // usar campos para poder obtener referencias desde otros paneles
        panelPaciente = new PanelPaciente();
        panelDonante = new PanelDonante();
        panelTrasplante = new PanelTrasplante();
        panelCita = new PanelCita();

        pestañas.addTab("Pacientes", panelPaciente);
        pestañas.addTab("Donantes", panelDonante);
        pestañas.addTab("Trasplantes", panelTrasplante);
        pestañas.addTab("Citas", panelCita);

        add(pestañas, BorderLayout.CENTER);
    }

    // getters para notificar recarga
    public PanelPaciente getPanelPaciente() { return panelPaciente; }
    public PanelDonante getPanelDonante() { return panelDonante; }
    public PanelTrasplante getPanelTrasplante() { return panelTrasplante; }
    public PanelCita getPanelCita() { return panelCita; }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            EPS_GUI ventana = new EPS_GUI();
            ventana.setVisible(true);
        });
    }
}
