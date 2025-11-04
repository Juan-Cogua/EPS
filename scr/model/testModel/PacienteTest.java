package model.testModel;

import excepciones.InvalidDataException;
import excepciones.InvariantViolationException;
import model.Cita;
import model.Paciente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Date;

/**
 * Clase de pruebas para `Paciente` que demuestra los 4 puntos principales de pruebas unitarias:
 * 1. Invariantes de una clase: validaciones internas que deben cumplirse siempre
 * 2. Métodos para verificación: getters/setters y comportamiento público
 * 3. Implementación de pruebas automáticas: uso de @BeforeEach y @Test
 * 4. Pruebas automáticas para el manejo de excepciones: assertThrows con excepciones personalizadas
 */
class PacienteTest {
    private Paciente pacientePrueba;
    private Cita citaPrueba;
    private Date inicio;
    private Date fin;

    /**
     * Punto 3: preparación reutilizable antes de cada prueba
     */
    @BeforeEach
    void setUp() {
        pacientePrueba = new Paciente("P", (byte)30, "P001", "O+", "Calle", "300", 70.0, 1.7, new ArrayList<>(), new ArrayList<>());
        inicio = new Date();
        fin = new Date(inicio.getTime() + 3600000);
        citaPrueba = new Cita("C100", inicio, fin, "Hospital", pacientePrueba, "Dr. X");
    }

    /**
     * Punto 1 y 4: invariantes y manejo de excepciones
     * Verifica que el constructor no permite un peso negativo
     */
    @Test
    void testPacienteConstructorPesoNegativo() {
        assertThrows(InvalidDataException.class, () -> {
            new Paciente("P", (byte)20, "P002", "O+", "Calle", "300", -1.0, 1.7, new ArrayList<>(), new ArrayList<>());
        });
    }

    /**
     * Punto 1 y 4: invariantes y manejo de excepciones
     * Verifica que las listas (alergias, citas) no pueden ser nulas
     */
    @Test
    void testCheckInvariantListasNull() {
        pacientePrueba.setAllergies(null);
        assertThrows(InvariantViolationException.class, () -> pacientePrueba.checkInvariant());
    }

    /**
     * Punto 2: métodos de verificación
     * Comprueba agregar y cancelar citas
     */
    @Test
    void testAgregarCancelarCita() {
        pacientePrueba.agregarCita(citaPrueba);
        assertTrue(pacientePrueba.getCitas().contains(citaPrueba));
        pacientePrueba.cancelarCita(citaPrueba);
        assertFalse(pacientePrueba.getCitas().contains(citaPrueba));
    }

    /**
     * Punto 2: métodos para verificación
     * Comprueba getters y setters básicos
     */
    @Test
    void testSettersGetters() {
    pacientePrueba.setAddress("Calle Nueva 123");
    assertEquals("Calle Nueva 123", pacientePrueba.getAddress());

    pacientePrueba.setWeight(75.5);
    assertEquals(75.5, pacientePrueba.getWeight(), 0.001);
    }
}
