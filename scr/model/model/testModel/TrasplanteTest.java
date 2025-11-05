package model.testModel;

import excepciones.InvalidDataException;
import excepciones.SangreIncompatibleException;
import excepciones.InvariantViolationException;
import model.Donante;
import model.Paciente;
import model.Trasplante;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Date;

/**
 * Clase de pruebas para Trasplante que demuestra los 4 puntos principales de pruebas unitarias:
 * 1. Invariantes de clase: comprobaciones internas del objeto Trasplante
 * 2. Métodos para verificación: getters/setters y comportamiento
 * 3. Implementación de pruebas automáticas: @BeforeEach y @Test
 * 4. Pruebas automáticas para el manejo de excepciones: uso de excepciones personalizadas
 */
class TrasplanteTest {
    private Donante donanteValido;
    private Paciente receptorValido;
    private Trasplante trasplantePrueba;
    private Date fecha;

    @BeforeEach
    void setUp() {
        fecha = new Date();
        donanteValido = new Donante("D", (byte)30, "D1", "O+", "Calle", "300", "Órganos", "Bueno", true, "Riñón");
        receptorValido = new Paciente("R", (byte)45, "R1", "A+", "Calle", "301", 80.0, 1.8, new ArrayList<>(), new ArrayList<>());
        trasplantePrueba = new Trasplante("T2", "Riñón", donanteValido, receptorValido, "Aprobado", "Historial", "", fecha);
    }

    /**
     * Punto 4: manejo de excepciones y Punto 1: invariantes
     * Verifica que no se puede construir un trasplante sin donante
     */
    @Test
    void testConstructorDonanteNull() {
        assertThrows(InvalidDataException.class, () -> {
            new Trasplante("T1", "Riñón", null, receptorValido, "Pendiente", "", "", fecha);
        });
    }

    /**
     * Punto 1: invariantes de clase
     * Verifica que un trasplante válido cumple sus invariantes
     */
    @Test
    void testCheckInvariantValido() {
        trasplantePrueba.checkInvariant();
    }

    /**
     * Punto 2 y 4: métodos para verificación y manejo de excepciones
     * Verifica la incompatibilidad de tipo de sangre entre donante y receptor
     */
    @Test
    void testSangreIncompatibleLanzaExcepcion() {
        Donante dIncompatible = new Donante("D2", (byte)35, "D2", "O+", "Calle", "302", "Órganos", "Bueno", true, "Riñón");
        Paciente receptorIncompatible = new Paciente("R2", (byte)50, "R2", "AB+", "Calle", "303", 75.0, 1.75, new ArrayList<>(), new ArrayList<>());
        Trasplante t = new Trasplante("T3", "Riñón", dIncompatible, receptorIncompatible, "Pendiente", "", "", fecha);
        assertThrows(SangreIncompatibleException.class, () -> t.checkInvariant());
    }

    /**
     * Punto 2: métodos para verificación
     * Verifica getters y setters de Trasplante
     */
    @Test
    void testSettersGetters() {
        assertEquals("T2", trasplantePrueba.getId());
        trasplantePrueba.setEstado("Completado");
        assertEquals("Completado", trasplantePrueba.getEstado());
    }
}
