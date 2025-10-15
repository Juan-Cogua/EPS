package IU;

import javax.swing.*;
import java.awt.*;

/**
 * Ventana principal del sistema EPS.
 * Contiene pestañas para gestionar Pacientes, Donantes, Trasplantes y Citas.
 * 
 * Autor: Juan Cogua / Andres Rojas
 * Versión: 2.0 (reorganizada por paneles)
 */
public class EPS_GUI extends JFrame {

    private JTabbedPane pestañas;

    public EPS_GUI() {
        setTitle("Sistema EPS");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        pestañas = new JTabbedPane();

        pestañas.addTab("Pacientes", new PanelPaciente());
        pestañas.addTab("Donantes", new PanelDonante());
        pestañas.addTab("Trasplantes", new PanelTrasplante());
        pestañas.addTab("Citas", new PanelCita());

        add(pestañas, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            EPS_GUI ventana = new EPS_GUI();
            ventana.setVisible(true);
        });
    }
}
