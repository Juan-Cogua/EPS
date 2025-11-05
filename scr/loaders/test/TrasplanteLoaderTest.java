package loaders.test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import loaders.TrasplanteLoader;
import model.Trasplante;
import model.Donante;
import model.Paciente;
import excepciones.NotFoundException;
    /**
     * Clase de pruebas para `TrasplanteLoader` que sigue el patrón de `PanelTrasplanteTest`.
     * Cubre:
     * 1. Invariantes de una clase (constantes y formatos esperados)
     * 2. Métodos para verificación (serialización/deserialización, guardar/cargar)
     * 3. Implementación de pruebas automáticas (@BeforeEach y @AfterEach)
     * 4. Pruebas automáticas para el manejo de excepciones (entradas inválidas)
     */
public class TrasplanteLoaderTest {
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
    public void testInvariantes() {
        assertNotNull(TrasplanteLoader.RUTA, "La ruta del archivo no debe ser nula");
        assertNotNull(TrasplanteLoader.FORMATO_FECHA, "El formato de fecha no debe ser nulo");
    }

    // 2. Métodos para verificación
    @Test
    public void testSerializacionTrasplante() {
        String serializado = TrasplanteLoader.toArchivo(trasplantePrueba);
        assertNotNull(serializado, "La serialización no debe ser nula");
        assertTrue(serializado.contains("T001"), "La serialización debe contener el ID del trasplante");
        assertTrue(serializado.contains("Juan Donante"), "La serialización debe contener el nombre del donante");
        assertTrue(serializado.contains("Ana Paciente"), "La serialización debe contener el nombre del paciente");
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
        assertEquals(trasplantes.size(), cargados.size(), "Debe cargar la misma cantidad de trasplantes");

        Trasplante cargado = cargados.get(0);
        assertEquals(trasplantePrueba.getId(), cargado.getId(), "El ID debe mantenerse después de cargar");
        assertEquals(trasplantePrueba.getEstado(), cargado.getEstado(), "El estado debe mantenerse después de cargar");
    }

    // 4. Pruebas automáticas para el manejo de excepciones
    @Test
    public void testFromArchivoLineaVacia() {
        assertNull(TrasplanteLoader.fromArchivo(""), "Línea vacía debe retornar null");
        assertNull(TrasplanteLoader.fromArchivo(null), "Línea nula debe retornar null");
    }

    @Test
    public void testFromArchivoFormatoInvalido() {
        assertNull(TrasplanteLoader.fromArchivo("Formato|Invalido|Sin|Campos|Correctos"), "Formato inválido debe retornar null");
    }

    @Test
    public void testToArchivoTrasplanteNulo() {
        assertEquals("", TrasplanteLoader.toArchivo(null), "Trasplante nulo debe retornar cadena vacía");
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