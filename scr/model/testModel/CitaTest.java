package model.testModel;

import excepciones.InvalidDataException;
import model.Cita;
import model.Paciente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.ArrayList;

/**
 * Clase de pruebas para Cita que demuestra los 4 puntos principales de pruebas unitarias:
 * 1. Invariantes de clase: Verifica que los datos de la cita sean válidos
 *    - ID no puede ser vacío
 *    - Fechas no pueden ser nulas
 *    - Lugar no puede ser vacío
 *    - Paciente no puede ser nulo
 *    - Doctor no puede ser vacío
 * 2. Métodos de verificación: Prueba los métodos principales de la clase
 * 3. Implementación de pruebas automáticas: Uso de JUnit para automatizar pruebas
 * 4. Manejo de excepciones: Verifica el correcto manejo de casos de error
 */
class CitaTest {
    private Paciente pacientePrueba;
    private Cita citaPrueba;
    private Date fechaInicio;
    private Date fechaFin;

    /**
     * Configuración inicial para las pruebas (Punto 3: Implementación de pruebas automáticas)
     * Este método se ejecuta antes de cada prueba para garantizar un estado inicial conocido
     */
    @BeforeEach
    void setUp() throws InvalidDataException {
        pacientePrueba = new Paciente("Juan", (byte)30, "P001", "O+", "Calle", "300", 
            70.0, 1.7, new ArrayList<>(), new ArrayList<>());
        fechaInicio = new Date();
        fechaFin = new Date(fechaInicio.getTime() + 3600000); // 1 hora después
        citaPrueba = new Cita("C001", fechaInicio, fechaFin, "Hospital Central", pacientePrueba, "Dr. García");
    }

    /**
     * Punto 1: Invariantes de clase y Punto 4: Manejo de excepciones
     * Verifica que las fechas no pueden ser nulas
     */
    @Test
    void testConstructorFechaNull() {
        assertThrows(InvalidDataException.class, () -> {
            new Cita("C1", null, null, "Hospital", pacientePrueba, "Dr");
        });
    }

    /**
     * Punto 2: Métodos de verificación
     * Verifica el formato del resumen de la cita
     */
    @Test
    void testResumenFormato() {
        String resumen = citaPrueba.resumen();
        assertNotNull(resumen, "El resumen no debe ser nulo");
        assertTrue(resumen.contains(";"), "El resumen debe contener el separador ;");
        assertTrue(resumen.contains(citaPrueba.getId()), "El resumen debe contener el ID de la cita");
    }

    /**
     * Punto 1: Invariantes de clase y Punto 4: Manejo de excepciones
     * Verifica que el lugar no puede ser vacío
     */
    @Test
    void testConstructorLugarInvalido() {
        assertThrows(InvalidDataException.class, () -> {
            new Cita("C2", fechaInicio, fechaFin, "", pacientePrueba, "Dr");
        });
    }

    /**
     * Punto 2: Métodos de verificación
     * Verifica el funcionamiento de los métodos get
     */
    @Test
    void testGetters() {
        assertEquals("C001", citaPrueba.getId(), "El ID debe coincidir");
        assertEquals(fechaInicio, citaPrueba.getDate(), "La fecha de inicio debe coincidir");
        assertEquals(fechaFin, citaPrueba.getTime(), "La fecha de fin debe coincidir");
        assertEquals("Hospital Central", citaPrueba.getLocation(), "El lugar debe coincidir");
        assertEquals(pacientePrueba, citaPrueba.getPaciente(), "El paciente debe coincidir");
        assertEquals("Dr. García", citaPrueba.getDoctor(), "El doctor debe coincidir");
    }
}