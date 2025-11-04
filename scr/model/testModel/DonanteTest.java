package model.testModel;

import excepciones.DonanteMenorEdadException;
import excepciones.InvariantViolationException;
import excepciones.InvalidDataException;
import model.Donante;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase de pruebas para Donante que demuestra los 4 puntos principales de pruebas unitarias:
 * 1. Invariantes de clase: Verifica que los datos del donante sean válidos
 *    - Debe ser mayor de edad
 *    - Tipo de donación válido (Sangre u Órganos)
 *    - Estado de salud no puede ser vacío
 *    - Órgano a donar debe ser válido
 *    - Debe tener consentimiento
 * 2. Métodos de verificación: Prueba los métodos principales de la clase
 * 3. Implementación de pruebas automáticas: Uso de JUnit para automatizar pruebas
 * 4. Manejo de excepciones: Verifica el correcto manejo de casos de error
 */
class DonanteTest {
    private Donante donantePrueba;

    /**
     * Configuración inicial para las pruebas (Punto 3: Implementación de pruebas automáticas)
     * Este método se ejecuta antes de cada prueba para garantizar un estado inicial conocido
     */
    @BeforeEach
    void setUp() {
        donantePrueba = new Donante("Luis", (byte)25, "D001", "O+", "Calle", "300", 
            "Órganos", "Bueno", true, "Riñón");
    }

    /**
     * Punto 1: Invariantes de clase
     * Verifica que un donante con datos válidos cumple con todos los invariantes
     */
    @Test
    void testDonanteValidoCheckInvariant() {
        donantePrueba.checkInvariant();
        assertTrue(donantePrueba.esMayorDeEdad());
    assertTrue(donantePrueba.isEligibility());
    }

    /**
     * Punto 1: Invariantes de clase y Punto 4: Manejo de excepciones
     * Verifica que se lanza excepción cuando el donante es menor de edad
     */
    @Test
    void testDonanteMenorEdadLanzaExcepcion() {
        assertThrows(DonanteMenorEdadException.class, () -> {
            new Donante("Miguel", (byte)16, "D002", "O+", "Calle", "300", 
                "Sangre", "Bueno", true, "Riñón");
        });
    }

    /**
     * Punto 2: Métodos de verificación y Punto 4: Manejo de excepciones
     * Verifica el comportamiento cuando se intenta establecer datos inválidos
     */
    @Test
    void testSetDatosInvalidos() {
        assertThrows(InvalidDataException.class, () -> {
            donantePrueba.setDonationType("");
        });
        
        assertThrows(InvalidDataException.class, () -> {
            donantePrueba.setHealthStatus("");
        });
    }

    /**
     * Punto 2: Métodos de verificación
     * Verifica el funcionamiento de los métodos set y get
     */
    @Test
    void testSettersGetters() {
        String nuevoEstadoSalud = "Excelente";
    donantePrueba.setHealthStatus(nuevoEstadoSalud);
    assertEquals(nuevoEstadoSalud, donantePrueba.getHealthStatus());

    donantePrueba.setEligibility(false);
    assertFalse(donantePrueba.isEligibility());
    }

    /**
     * Punto 1: Invariantes de clase y Punto 4: Manejo de excepciones
     * Verifica que se valida correctamente el órgano a donar
     */
    @Test
    void testCheckInvariantOrganoInvalido() {
        assertThrows(InvariantViolationException.class, () -> {
            Donante d = new Donante("Luisa", (byte)30, "D003", "A+", "Calle", "301", 
                "Órganos", "Bueno", true, "ÓrganoInválido");
            d.checkInvariant();
        });
    }
}
