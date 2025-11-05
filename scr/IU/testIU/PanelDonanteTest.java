package IU.testIU;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import IU.PanelDonante;
import model.Donante;
import loaders.DonanteLoader;


/**
 * Clase de pruebas para PanelDonante donde se implementan:
 * 1. Invariantes de una clase.
 * 2. Métodos para verificación.
 * 3. Implementación de pruebas Unitarias.
 * 4. Pruebas automáticas para el manejo de excepciones.
 */
public class PanelDonanteTest {
    private static final String RUTA_TEST = "DonanteTest.txt";
    private PanelDonante panel;
    private Donante donantePrueba;

    @BeforeEach
    public void setUp() {
        // Crear datos de prueba
        donantePrueba = new Donante(
            "María González",  // name
            (byte)35,          // age (debe ser >= 18)
            "D001",            // id
            "A+",              // bloodType
            "Calle 456",       // address
            "3009876543",      // phone
            "Órganos",         // donationType
            "Saludable",       // healthStatus
            true,              // eligibility
            "Riñón"            // organo
        );
        
        panel = new PanelDonante();
    }

    // 1. Invariantes de la clase
    @Test
    public void testInvariantesDonante() {
        assertNotNull(donantePrueba.getName(), "El nombre no debe ser nulo");
        assertNotNull(donantePrueba.getId(), "El ID no debe ser nulo");
        assertNotNull(donantePrueba.getBloodType(), "El tipo de sangre no debe ser nulo");
        assertNotNull(donantePrueba.getDonationType(), "El tipo de donación no debe ser nulo");
        assertNotNull(donantePrueba.getHealthStatus(), "El estado de salud no debe ser nulo");
        assertTrue(donantePrueba.getAge() >= 18, "El donante debe ser mayor de edad");
    }

    @Test
    public void testInvariantesPanel() {
        assertNotNull(panel, "El panel no debe ser nulo");
    }

    @Test
    public void testOrganosDisponibles() {
        List<String> organos = Donante.getOrganosDisponibles();
        assertNotNull(organos, "La lista de órganos no debe ser nula");
        assertFalse(organos.isEmpty(), "Debe haber órganos disponibles");
        assertTrue(organos.contains("Riñón"), "Debe incluir Riñón");
        assertTrue(organos.contains("Corazón"), "Debe incluir Corazón");
    }

    // 2. Métodos para verificación
    @Test
    public void testSerializacionDonante() {
        String serializado = donantePrueba.toArchivo();
        assertNotNull(serializado, "La serialización no debe ser nula");
        assertTrue(serializado.contains("María González"), "Debe contener el nombre");
        assertTrue(serializado.contains("D001"), "Debe contener el ID");
        assertTrue(serializado.contains("A+"), "Debe contener el tipo de sangre");
        assertTrue(serializado.contains("Órganos"), "Debe contener el tipo de donación");
        assertTrue(serializado.contains("Riñón"), "Debe contener el órgano");
    }

    @Test
    public void testDeserializacionDonante() {
        String linea = donantePrueba.toArchivo();
        Donante deserializado = Donante.fromArchivo(linea);

        assertNotNull(deserializado, "El donante deserializado no debe ser nulo");
        assertEquals(donantePrueba.getName(), deserializado.getName(), "El nombre debe mantenerse");
        assertEquals(donantePrueba.getId(), deserializado.getId(), "El ID debe mantenerse");
        assertEquals(donantePrueba.getBloodType(), deserializado.getBloodType(), "El tipo de sangre debe mantenerse");
        assertEquals(donantePrueba.getDonationType(), deserializado.getDonationType(), "El tipo de donación debe mantenerse");
    }

    @Test
    public void testToString() {
        String resultado = donantePrueba.toString();
        assertNotNull(resultado, "toString no debe retornar nulo");
        assertTrue(resultado.contains("María González"), "Debe contener el nombre");
        assertTrue(resultado.contains("D001"), "Debe contener el ID");
    }

    // 3. Implementación de pruebas automáticas
    @Test
    public void testGuardarYCargarDonantes() {
        List<Donante> donantes = new ArrayList<>();
        donantes.add(donantePrueba);

        // Guardar
        DonanteLoader.guardarDonantes(donantes);

        // Cargar
        List<Donante> cargados = DonanteLoader.cargarDonantes();
        assertFalse(cargados.isEmpty(), "La lista cargada no debe estar vacía");

        Donante cargado = cargados.stream()
            .filter(d -> d.getId().equals("D001"))
            .findFirst()
            .orElse(null);
        
        assertNotNull(cargado, "Debe encontrar el donante guardado");
        assertEquals(donantePrueba.getName(), cargado.getName(), "El nombre debe mantenerse");
    }

    @Test
    public void testAgregarDonante() {
        DonanteLoader.agregarDonante(donantePrueba);
        List<Donante> donantes = DonanteLoader.cargarDonantes();
        
        boolean encontrado = donantes.stream()
            .anyMatch(d -> d.getId().equals("D001"));
        
        assertTrue(encontrado, "El donante debe estar en la lista");
    }

    // 4. Pruebas automáticas para el manejo de excepciones
    @Test
    public void testDonanteMenorDeEdad() {
        assertThrows(Exception.class, () -> {
            new Donante("Menor", (byte)16, "D999", "O+", "Calle", "123",
                       "Sangre", "Saludable", true, "");
        }, "Debe lanzar excepción con donante menor de 18 años");
    }

    @Test
    public void testTipoDonacionVacio() {
        assertThrows(Exception.class, () -> {
            new Donante("Test", (byte)25, "D999", "O+", "Calle", "123",
                       "", "Saludable", true, "");
        }, "Debe lanzar excepción con tipo de donación vacío");
    }

    @Test
    public void testEstadoSaludVacio() {
        assertThrows(Exception.class, () -> {
            new Donante("Test", (byte)25, "D999", "O+", "Calle", "123",
                       "Sangre", "", true, "");
        }, "Debe lanzar excepción con estado de salud vacío");
    }

    @Test
    public void testFromArchivoLineaVacia() {
        assertNull(Donante.fromArchivo(""), "Línea vacía debe retornar null");
        assertNull(Donante.fromArchivo(null), "Línea nula debe retornar null");
    }

    @Test
    public void testFromArchivoFormatoInvalido() {
        assertNull(Donante.fromArchivo("Formato|Invalido"), 
                  "Formato inválido debe retornar null");
    }

    @Test
    public void testCheckInvariant() {
        assertDoesNotThrow(() -> {
            donantePrueba.checkInvariant();
        }, "Las invariantes deben cumplirse en un donante válido");
    }

    @Test
    public void testOrganoInvalido() {
        Donante donanteOrganoInvalido = new Donante(
            "Test", (byte)25, "D998", "O+", "Calle", "123",
            "Órganos", "Saludable", true, "Bazo"
        );
        
        assertThrows(Exception.class, () -> {
            donanteOrganoInvalido.checkInvariant();
        }, "Debe lanzar excepción con órgano no disponible");
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