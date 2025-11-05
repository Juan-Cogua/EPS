package IU.testIU;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import model.Trasplante;
import model.Donante;
import model.Paciente;
import loaders.TrasplanteLoader;

/**
 * Clase de pruebas para PanelTrasplante siguiendo los 4 puntos:
 * 1. Invariantes de una clase
 * 2. Métodos para verificación
 * 3. Implementación de pruebas automáticas
 * 4. Pruebas automáticas para el manejo de excepciones
 */
public class PanelTrasplanteTest {
    private static final String RUTA_TEST = "TrasplanteTest.txt";
    private Trasplante trasplantePrueba;
    private Donante donante;
    private Paciente paciente;

    @BeforeEach
    public void setUp() {
        // Crear datos de prueba
        // Donante(name, age, id, bloodType, address, phone, donationType, healthStatus, eligibility, organo)
        donante = new Donante(
            "Juan Donante",    // name
            (byte)30,          // age (>= 18)
            "D001",            // id
            "O+",              // bloodType
            "Calle 123",       // address
            "12345678",        // phone
            "Órganos",         // donationType
            "Saludable",       // healthStatus
            true,              // eligibility
            "Riñón"            // organo
        );
        
        // Paciente(name, age, id, bloodType, address, phone, weight, height, allergies, citas)
        paciente = new Paciente(
            "Ana Paciente",    // name
            (byte)25,          // age
            "P001",            // id
            "AB+",             // bloodType
            "Calle 456",       // address
            "87654321",        // phone
            70.0,              // weight
            1.65,              // height
            new ArrayList<>(), // allergies
            new ArrayList<>()  // citas
        );
        
        // Trasplante(id, organType, donor, receiver, estado, historialClinico, rejectionReason, fecha)
        trasplantePrueba = new Trasplante(
            "T001",            // id
            "Riñón",           // organType
            donante,           // donor
            paciente,          // receiver
            "Pendiente",       // estado
            "",                // historialClinico
            "",                // rejectionReason
            new Date()         // fecha
        );
    }

    // 1. Invariantes de la clase
    @Test
    public void testInvariantesTrasplante() {
        assertNotNull(trasplantePrueba.getId(), "El ID no debe ser nulo");
        assertNotNull(trasplantePrueba.getOrganType(), "El tipo de órgano no debe ser nulo");
        assertNotNull(trasplantePrueba.getDonor(), "El donante no debe ser nulo");
        assertNotNull(trasplantePrueba.getReceiver(), "El receptor no debe ser nulo");
        assertNotNull(trasplantePrueba.getEstado(), "El estado no debe ser nulo");
        assertNotNull(trasplantePrueba.getFecha(), "La fecha no debe ser nula");
    }

    @Test
    public void testInvariantesRelaciones() {
        assertEquals("D001", trasplantePrueba.getDonor().getId(), "El donante debe tener el ID correcto");
        assertEquals("P001", trasplantePrueba.getReceiver().getId(), "El receptor debe tener el ID correcto");
        assertEquals("Riñón", trasplantePrueba.getOrganType(), "El órgano debe ser Riñón");
    }

    // 2. Métodos para verificación
    @Test
    public void testSerializacionTrasplante() {
        String serializado = TrasplanteLoader.toArchivo(trasplantePrueba);
        assertNotNull(serializado, "La serialización no debe ser nula");
        assertTrue(serializado.contains("T001"), "Debe contener el ID del trasplante");
        assertTrue(serializado.contains("Juan Donante"), "Debe contener el nombre del donante");
        assertTrue(serializado.contains("Ana Paciente"), "Debe contener el nombre del paciente");
        assertTrue(serializado.contains("Riñón"), "Debe contener el tipo de órgano");
    }

    @Test
    public void testDeserializacionTrasplante() {
        String linea = TrasplanteLoader.toArchivo(trasplantePrueba);
        Trasplante deserializado = TrasplanteLoader.fromArchivo(linea);

        assertNotNull(deserializado, "El trasplante deserializado no debe ser nulo");
        assertEquals(trasplantePrueba.getId(), deserializado.getId(), "El ID debe mantenerse");
        assertEquals(trasplantePrueba.getOrganType(), deserializado.getOrganType(), "El órgano debe mantenerse");
        assertEquals(trasplantePrueba.getEstado(), deserializado.getEstado(), "El estado debe mantenerse");
    }

    @Test
    public void testToString() {
        String resultado = trasplantePrueba.toString();
        assertNotNull(resultado, "toString no debe retornar nulo");
        assertTrue(resultado.contains("T001"), "Debe contener el ID");
        assertTrue(resultado.contains("Riñón"), "Debe contener el órgano");
        assertTrue(resultado.contains("Juan Donante"), "Debe contener el nombre del donante");
        assertTrue(resultado.contains("Ana Paciente"), "Debe contener el nombre del paciente");
    }

    // 3. Implementación de pruebas automáticas
    @Test
    public void testGuardarYCargarTrasplantes() {
        List<Trasplante> trasplantes = new ArrayList<>();
        trasplantes.add(trasplantePrueba);

        // Guardar
        TrasplanteLoader.guardarTrasplantes(trasplantes);

        // Cargar
        List<Trasplante> cargados = TrasplanteLoader.cargarTrasplantes();
        assertFalse(cargados.isEmpty(), "La lista cargada no debe estar vacía");

        Trasplante cargado = cargados.stream()
            .filter(t -> t.getId().equals("T001"))
            .findFirst()
            .orElse(null);
        
        assertNotNull(cargado, "Debe encontrar el trasplante guardado");
        assertEquals(trasplantePrueba.getId(), cargado.getId(), "El ID debe mantenerse después de cargar");
        assertEquals(trasplantePrueba.getEstado(), cargado.getEstado(), "El estado debe mantenerse después de cargar");
    }

    @Test
    public void testCambiarEstado() {
        trasplantePrueba.setEstado("Aprobado");
        assertEquals("Aprobado", trasplantePrueba.getEstado(), "El estado debe cambiar a Aprobado");
        
        trasplantePrueba.setEstado("Rechazado");
        assertEquals("Rechazado", trasplantePrueba.getEstado(), "El estado debe cambiar a Rechazado");
    }

    @Test
    public void testCambiarId() {
        trasplantePrueba.setId("T002");
        assertEquals("T002", trasplantePrueba.getId(), "El ID debe actualizarse");
    }

    // 4. Pruebas automáticas para el manejo de excepciones
    @Test
    public void testIdNulo() {
        assertThrows(Exception.class, () -> {
            new Trasplante(null, "Riñón", donante, paciente, "Pendiente", "", "", new Date());
        }, "Debe lanzar excepción con ID nulo");
    }

    @Test
    public void testIdVacio() {
        assertThrows(Exception.class, () -> {
            new Trasplante("", "Riñón", donante, paciente, "Pendiente", "", "", new Date());
        }, "Debe lanzar excepción con ID vacío");
    }

    @Test
    public void testOrganoVacio() {
        assertThrows(Exception.class, () -> {
            new Trasplante("T999", "", donante, paciente, "Pendiente", "", "", new Date());
        }, "Debe lanzar excepción con órgano vacío");
    }

    @Test
    public void testDonanteNulo() {
        assertThrows(Exception.class, () -> {
            new Trasplante("T999", "Riñón", null, paciente, "Pendiente", "", "", new Date());
        }, "Debe lanzar excepción con donante nulo");
    }

    @Test
    public void testReceptorNulo() {
        assertThrows(Exception.class, () -> {
            new Trasplante("T999", "Riñón", donante, null, "Pendiente", "", "", new Date());
        }, "Debe lanzar excepción con receptor nulo");
    }

    @Test
    public void testFromArchivoLineaVacia() {
        assertNull(TrasplanteLoader.fromArchivo(""), "Línea vacía debe retornar null");
        assertNull(TrasplanteLoader.fromArchivo(null), "Línea nula debe retornar null");
    }

    @Test
    public void testFromArchivoFormatoInvalido() {
        assertNull(TrasplanteLoader.fromArchivo("Formato|Invalido|Sin|Campos|Correctos"), 
                  "Formato inválido debe retornar null");
    }

    @Test
    public void testToArchivoTrasplanteNulo() {
        assertEquals("", TrasplanteLoader.toArchivo(null), "Trasplante nulo debe retornar cadena vacía");
    }

    @Test
    public void testCheckInvariant() {
        assertDoesNotThrow(() -> {
            trasplantePrueba.checkInvariant();
        }, "Las invariantes deben cumplirse en un trasplante válido");
    }

    @Test
    public void testCheckInvariantFechaNula() {
        Trasplante trasplanteInvalido = new Trasplante(
            "T998", "Riñón", donante, paciente, "Pendiente", "", "", null
        );
        
        assertThrows(Exception.class, () -> {
            trasplanteInvalido.checkInvariant();
        }, "Debe lanzar excepción con fecha nula");
    }

    @AfterEach
    public void tearDown() {
        // Limpiar archivos de prueba
        File archivo = new File(RUTA_TEST);
        if (archivo.exists()) {
            archivo.delete();
        }
    }
}