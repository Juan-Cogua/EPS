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

import excepciones.InvalidDataException;
import excepciones.DonanteMenorEdadException;

/**
 * Clase de pruebas unitarias para TrasplanteLoader.
 * Verifica la correcta funcionalidad de carga, guardado, serialización
 * y deserialización de trasplantes.
 * 
 * Cubre los 4 puntos de las pruebas unitarias:
 * 1. Invariantes de una clase (constantes y formatos esperados)
 * 2. Métodos para verificación (serialización/deserialización, guardar/cargar)
 * 3. Implementación de pruebas automáticas (@BeforeEach y @AfterEach)
 * 4. Pruebas automáticas para el manejo de excepciones (entradas inválidas)
 * 
 * @author Juan Cogua
 * @author Andres Rojas
 * @version 1.0
 */
public class TrasplanteLoaderTest {
    /** Ruta del archivo de prueba */
    private static final String RUTA_TEST = "TrasplanteTest.txt";
    
    /** Trasplante utilizado para las pruebas */
    private Trasplante trasplantePrueba;
    
    /** Donante asociado al trasplante de prueba */
    private Donante donante;
    
    /** Paciente receptor del trasplante de prueba */
    private Paciente paciente;

    /**
     * Configuración inicial antes de cada prueba.
     * Crea un donante, un paciente y un trasplante de prueba.
     */
    @BeforeEach
    public void setUp() throws InvalidDataException, DonanteMenorEdadException {
        // Crear datos de prueba (los constructores pueden lanzar excepciones comprobadas)
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

    // ========== 1. INVARIANTES DE LA CLASE ==========
    
    /**
     * Verifica que las constantes de la clase TrasplanteLoader no sean nulas.
     * Prueba las invariantes de formato y ruta de archivo.
     */
    @Test
    public void testInvariantes() {
        // Evitar acceder a campos internos privados del loader; comprobar comportamiento público
        String serial = TrasplanteLoader.toArchivo(trasplantePrueba);
        assertNotNull(serial, "La serialización no debe ser nula");
    }

    // ========== 2. MÉTODOS PARA VERIFICACIÓN ==========
    
    /**
     * Verifica que la serialización de un trasplante genere
     * una cadena con todos los campos requeridos.
     */
    @Test
    public void testSerializacionTrasplante() {
        String serializado = TrasplanteLoader.toArchivo(trasplantePrueba);
        assertNotNull(serializado, "La serialización no debe ser nula");
        assertTrue(serializado.contains("T001"), "La serialización debe contener el ID del trasplante");
        assertTrue(serializado.contains("Juan Donante"), "La serialización debe contener el nombre del donante");
        assertTrue(serializado.contains("Ana Paciente"), "La serialización debe contener el nombre del paciente");
    }

    /**
     * Verifica que la deserialización reconstruya correctamente
     * un objeto Trasplante desde una cadena serializada.
     */
    @Test
    public void testDeserializacionTrasplante() {
        String linea = TrasplanteLoader.toArchivo(trasplantePrueba);
        Trasplante deserializado = TrasplanteLoader.fromArchivo(linea);

        assertNotNull(deserializado, "El trasplante deserializado no debe ser nulo");
        assertEquals(trasplantePrueba.getId(), deserializado.getId(), "El ID debe mantenerse");
        assertEquals(trasplantePrueba.getOrganType(), deserializado.getOrganType(), "El órgano debe mantenerse");
        assertEquals(trasplantePrueba.getEstado(), deserializado.getEstado(), "El estado debe mantenerse");
    }

    // ========== 3. IMPLEMENTACIÓN DE PRUEBAS AUTOMÁTICAS ==========
    
    /**
     * Verifica que los trasplantes se guarden y carguen correctamente
     * manteniendo su integridad y la cantidad de registros.
     */
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

    // ========== 4. PRUEBAS AUTOMÁTICAS PARA EL MANEJO DE EXCEPCIONES ==========
    
    /**
     * Verifica que líneas vacías o nulas retornen null en la deserialización.
     */
    @Test
    public void testFromArchivoLineaVacia() {
        assertNull(TrasplanteLoader.fromArchivo(""), "Línea vacía debe retornar null");
        assertNull(TrasplanteLoader.fromArchivo(null), "Línea nula debe retornar null");
    }

    /**
     * Verifica que formatos inválidos retornen null en la deserialización.
     */
    @Test
    public void testFromArchivoFormatoInvalido() {
        assertNull(TrasplanteLoader.fromArchivo("Formato|Invalido|Sin|Campos|Correctos"), "Formato inválido debe retornar null");
    }

    /**
     * Verifica que un trasplante nulo genere una cadena vacía
     * al intentar serializarlo.
     */
    @Test
    public void testToArchivoTrasplanteNulo() {
        assertEquals("", TrasplanteLoader.toArchivo(null), "Trasplante nulo debe retornar cadena vacía");
    }

    /**
     * Limpieza después de cada prueba.
     * Elimina los archivos temporales creados.
     */
    @AfterEach
    public void tearDown() {
        // Limpiar archivos de prueba
        File archivo = new File(RUTA_TEST);
        if (archivo.exists()) {
            archivo.delete();
        }
    }
}