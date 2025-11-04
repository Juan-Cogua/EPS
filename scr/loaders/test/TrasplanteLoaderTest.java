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



public class TrasplanteLoaderTest {
    private static final String RUTA_TEST = "TrasplanteTest.txt";
    private Trasplante trasplantePrueba;
    private Donante donante;
    private Paciente paciente;

    /**
     * Clase de pruebas para `TrasplanteLoader` que sigue el patrón de `PanelTrasplanteTest`.
     * Cubre los 4 puntos de las pruebas unitarias:
     * 1. Invariantes de una clase (constantes y formatos esperados)
     * 2. Métodos para verificación (serialización/deserialización, guardar/cargar)
     * 3. Implementación de pruebas automáticas (@BeforeEach y @AfterEach)
     * 4. Pruebas automáticas para el manejo de excepciones (entradas inválidas)
     */
    @BeforeEach
    public void setUp() {
        // Crear datos de prueba
        donante = new Donante("D001", "Juan Donante", "12345678", new Date(), "O+", "Riñón", true);
        paciente = new Paciente("P001", "Ana Paciente", "87654321", new Date(), "AB+", "Riñón");
        trasplantePrueba = new Trasplante("T001", "Riñón", donante, paciente, "Pendiente", "", "", new Date());
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
        assertNotNull("La serialización no debe ser nula", serializado);
        assertTrue("La serialización debe contener el ID del trasplante", serializado.contains("T001"));
        assertTrue("La serialización debe contener el nombre del donante", serializado.contains("Juan Donante"));
        assertTrue("La serialización debe contener el nombre del paciente", serializado.contains("Ana Paciente"));
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