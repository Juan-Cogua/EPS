package IU.testIU;
import IU.PanelTrasplante;
import excepciones.FechaInvalidaException;
import excepciones.SangreIncompatibleException;
import excepciones.TrasplanteInvalidoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase de pruebas para PanelTrasplante que demuestra los 4 puntos principales de pruebas unitarias:
 * 1. Invariantes de clase: Verifica que los datos del panel sean válidos
 * 2. Métodos de verificación: Prueba los métodos principales del panel
 * 3. Implementación de pruebas automáticas: Uso de JUnit para automatizar pruebas
 * 4. Manejo de excepciones: Verifica el correcto manejo de casos de error
 */
class PanelTrasplanteTest {
    private PanelTrasplante panel;

    /**
     * Configuración inicial para las pruebas (Punto 3: Implementación de pruebas automáticas)
     * Este método se ejecuta antes de cada prueba para garantizar un estado inicial conocido
     */
    @BeforeEach
    void setUp() {
        panel = new PanelTrasplante();
        // Simular datos mínimos para donante y paciente (Punto 1: Invariantes de clase)
        // Asegura que el panel tenga los datos mínimos necesarios para ser válido
        DefaultListModel<String> modeloDonantes = new DefaultListModel<>();
        modeloDonantes.addElement("Donante1 (ID: D001) | Sangre: O+ | Dona: Riñón");
        panel.getListaDonantes().setModel(modeloDonantes);
        panel.getListaDonantes().setSelectedIndex(0);

        DefaultListModel<String> modeloPacientes = new DefaultListModel<>();
        modeloPacientes.addElement("Paciente1 (ID: P001) | Sangre: A+ ");
        panel.getListaPacientes().setModel(modeloPacientes);
        panel.getListaPacientes().setSelectedIndex(0);
    }

    /**
     * Punto 1: Invariantes de clase
     * Verifica que el ID del trasplante no puede ser vacío
     * Punto 4: Manejo de excepciones
     * Prueba que se lance la excepción apropiada cuando falta el ID
     */
    @Test
    void testAgregarTrasplante_IdVacio() {
        panel.getTxtIdTrasplante().setText("");
        Exception ex = assertThrows(TrasplanteInvalidoException.class, () -> panel.agregarTrasplante());
        assertTrue(ex.getMessage().contains("ID"));
    }

    /**
     * Punto 2: Métodos de verificación
     * Verifica el comportamiento del método agregarTrasplante cuando falta un donante
     * Punto 4: Manejo de excepciones
     * Prueba que se lance la excepción apropiada cuando falta el donante
     */
    @Test
    void testAgregarTrasplante_SinDonanteOPaciente() {
        panel.getTxtIdTrasplante().setText("T001");
        panel.getListaDonantes().clearSelection();
        Exception ex = assertThrows(TrasplanteInvalidoException.class, () -> panel.agregarTrasplante());
        assertTrue(ex.getMessage().contains("donante"));
    }

    /**
     * Punto 2: Métodos de verificación
     * Verifica la validación del formato de fecha
     * Punto 4: Manejo de excepciones
     * Prueba que se lance la excepción apropiada cuando la fecha es inválida
     */
    @Test
    void testAgregarTrasplante_FechaInvalida() {
        panel.getTxtIdTrasplante().setText("T002");
        panel.getTxtFecha().setText("2025-11-04"); // Formato incorrecto
        Exception ex = assertThrows(FechaInvalidaException.class, () -> panel.agregarTrasplante());
        assertTrue(ex.getMessage().contains("fecha"));
    }

    /**
     * Punto 1: Invariantes de clase
     * Verifica la compatibilidad de tipos de sangre
     * Punto 4: Manejo de excepciones
     * Prueba que se lance la excepción apropiada cuando los tipos de sangre son incompatibles
     */
    @Test
    void testAgregarTrasplante_SangreIncompatible() {
        panel.getTxtIdTrasplante().setText("T003");
        // Cambiar paciente a tipo de sangre incompatible
        DefaultListModel<String> modeloPacientes = new DefaultListModel<>();
        modeloPacientes.addElement("Paciente2 (ID: P002) | Sangre: AB+");
        panel.getListaPacientes().setModel(modeloPacientes);
        panel.getListaPacientes().setSelectedIndex(0);
        Exception ex = assertThrows(SangreIncompatibleException.class, () -> panel.agregarTrasplante());
        assertTrue(ex.getMessage().contains("incompatible"));
    }

}