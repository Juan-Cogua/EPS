package model.testModel;

import excepciones.InvalidDataException;
import excepciones.InvariantViolationException;
import model.Persona;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase de pruebas para Persona que demuestra los 4 puntos principales de pruebas unitarias:
 * 1. Invariantes de clase: Verifica que los datos de la persona sean válidos
 *    - Nombre no puede ser vacío
 *    - ID debe ser válido
 *    - Edad debe ser un valor positivo
 *    - Tipo de sangre debe ser válido
 *    - Dirección no puede ser vacía
 *    - Teléfono debe ser válido
 * 2. Métodos de verificación: Prueba los métodos principales de la clase
 * 3. Implementación de pruebas automáticas: Uso de JUnit para automatizar pruebas
 * 4. Manejo de excepciones: Verifica el correcto manejo de casos de error
 */
class PersonaTest {
    private Persona personaPrueba;

    /**
     * Configuración inicial para las pruebas (Punto 3: Implementación de pruebas automáticas)
     * Este método se ejecuta antes de cada prueba para garantizar un estado inicial conocido
     */
    @BeforeEach
    void setUp() throws InvalidDataException {
        personaPrueba = new Persona("Juan", (byte)30, "ID123", "O+", "Calle 1", "3001234567");
    }

    /**
     * Punto 1: Invariantes de clase
     * Verifica que una persona con datos válidos cumple con todos los invariantes
     */
    @Test
    void testPersonaValidaCheckInvariant() {
        personaPrueba.checkInvariant();
        assertTrue(personaPrueba.esMayorDeEdad());
    }

    /**
     * Punto 2: Métodos de verificación
     * Verifica el funcionamiento de los métodos set y get
     */
    @Test
    void testSettersGetters() {
        String nuevaDireccion = "Nueva Calle 123";
    personaPrueba.setAddress(nuevaDireccion);
    assertEquals(nuevaDireccion, personaPrueba.getAddress());
        
        String nuevoTelefono = "3219876543";
    personaPrueba.setPhone(nuevoTelefono);
    assertEquals(nuevoTelefono, personaPrueba.getPhone());
    }

    /**
     * Punto 1: Invariantes de clase y Punto 4: Manejo de excepciones
     * Verifica que el constructor mantiene el invariante del nombre no vacío
     */
    @Test
    void testConstructorNombreInvalido() {
        assertThrows(InvalidDataException.class, () -> {
            new Persona("", (byte)25, "ID124", "A+", "Calle", "300");
        });
    }

    /**
     * Punto 2: Métodos de verificación y Punto 4: Manejo de excepciones
     * Verifica el comportamiento cuando se intenta establecer datos inválidos
     */
    @Test
    void testSetDatosInvalidos() {
        assertThrows(InvalidDataException.class, () -> {
            personaPrueba.setBloodType("XY");
        });
        
        assertThrows(InvalidDataException.class, () -> {
            personaPrueba.setAge((byte)-1);
        });
    }

    /**
     * Punto 1: Invariantes de clase y Punto 4: Manejo de excepciones
     * Verifica que el invariante del ID no vacío se mantiene después de modificar
     */
    @Test
    void testCheckInvariantRompeDespuesDeModificar() {
        personaPrueba.setId("");
        assertThrows(InvariantViolationException.class, () -> {
            personaPrueba.checkInvariant();
        });
    }
}