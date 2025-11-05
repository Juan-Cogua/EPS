package IU.testIU;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import IU.PanelDonante;
import model.Cita;
import model.Donante;
import loaders.DonanteLoader;

/**
 * Clase de pruebas para {@link PanelDonante} y la clase {@link Donante}.
 * Contiene:
 * <ul>
 *   <li>Pruebas de invariantes y consistencia de datos.</li>
 *   <li>Pruebas unitarias sobre serialización, deserialización y persistencia.</li>
 *   <li>Verificación de métodos de utilidad y conversiones.</li>
 *   <li>Pruebas automáticas para el manejo de excepciones y validaciones.</li>
 * </ul>
 * 
 * <p>Esta clase garantiza la correcta funcionalidad de las operaciones básicas
 * del módulo de donantes dentro del sistema de donación.</p>
 * 
 * @author Andres
 * @author Juan Cogua
 * @version 2.0
 */
public class PanelDonanteTest {
    /** Ruta temporal utilizada para pruebas de persistencia. */
    private static final String RUTA_TEST = "DonanteTest.txt";
    /** Panel de prueba para la interfaz de donante. */
    private PanelDonante panel;
    /** Objeto Donante utilizado como caso de prueba. */
    private Donante donantePrueba;

    /**
     * Configura los objetos necesarios antes de cada prueba.
     * Se inicializa un {@link Donante} válido y una instancia del panel.
     */
    @BeforeEach
    public void setUp() {
        donantePrueba = new Donante(
            "María González",  // Nombre
            (byte)35,          // Edad (válida)
            "D001",            // ID
            "A+",              // Tipo de sangre
            "Calle 456",       // Dirección
            "3009876543",      // Teléfono
            "Órganos",         // Tipo de donación
            "Saludable",       // Estado de salud
            true,              // Elegibilidad
            "Riñón"            // Órgano
        );
        
        panel = new PanelDonante();
    }

    // -------------------------------
    // 1. Invariantes de la clase
    // -------------------------------

    /**
     * Verifica las invariantes de {@link Donante}, asegurando que
     * los campos esenciales no sean nulos y que el donante sea mayor de edad.
     */
    @Test
    public void testInvariantesDonante() {
        assertNotNull(donantePrueba.getName(), "El nombre no debe ser nulo");
        assertNotNull(donantePrueba.getId(), "El ID no debe ser nulo");
        assertNotNull(donantePrueba.getBloodType(), "El tipo de sangre no debe ser nulo");
        assertNotNull(donantePrueba.getDonationType(), "El tipo de donación no debe ser nulo");
        assertNotNull(donantePrueba.getHealthStatus(), "El estado de salud no debe ser nulo");
        assertTrue(donantePrueba.getAge() >= 18, "El donante debe ser mayor de edad");
    }

    /**
     * Verifica la invariante del panel, asegurando que el objeto
     * {@link PanelDonante} se haya inicializado correctamente.
     */
    @Test
    public void testInvariantesPanel() {
        assertNotNull(panel, "El panel no debe ser nulo");
    }

    /**
     * Verifica que la lista de órganos disponibles en {@link Donante#getOrganosDisponibles()}
     * no sea nula, esté poblada y contenga órganos comunes como "Riñón" y "Corazón".
     */
    @Test
    public void testOrganosDisponibles() {
        List<String> organos = Donante.getOrganosDisponibles();
        assertNotNull(organos, "La lista de órganos no debe ser nula");
        assertFalse(organos.isEmpty(), "Debe haber órganos disponibles");
        assertTrue(organos.contains("Riñón"), "Debe incluir Riñón");
        assertTrue(organos.contains("Corazón"), "Debe incluir Corazón");
    }

    // -------------------------------
    // 2. Métodos para verificación
    // -------------------------------

    /**
     * Verifica la correcta serialización de un {@link Donante} a formato de texto,
     * asegurando que todos los campos importantes estén representados.
     */
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

    /**
     * Verifica la deserialización de un {@link Donante} a partir de una línea de texto.
     * Comprueba que los datos se mantengan consistentes tras la conversión.
     */
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

    /**
     * Verifica que el método {@link Donante#toString()} genere una cadena no nula
     * y contenga los datos esenciales del donante.
     */
    @Test
    public void testToString() {
        String resultado = donantePrueba.toString();
        assertNotNull(resultado, "toString no debe retornar nulo");
        assertTrue(resultado.contains("María González"), "Debe contener el nombre");
        assertTrue(resultado.contains("D001"), "Debe contener el ID");
    }

    // -------------------------------
    // 3. Pruebas sobre persistencia
    // -------------------------------

    /**
     * Verifica el correcto guardado y carga de donantes usando la clase {@link DonanteLoader}.
     * Se asegura que el donante guardado pueda ser recuperado con los mismos datos.
     */
    @Test
    public void testGuardarYCargarDonantes() {
        List<Donante> donantes = new ArrayList<>();
        donantes.add(donantePrueba);

        DonanteLoader.guardarDonantes(donantes);
        List<Donante> cargados = DonanteLoader.cargarDonantes();

        assertFalse(cargados.isEmpty(), "La lista cargada no debe estar vacía");

        Donante cargado = cargados.stream()
            .filter(d -> d.getId().equals("D001"))
            .findFirst()
            .orElse(null);
        
        assertNotNull(cargado, "Debe encontrar el donante guardado");
        assertEquals(donantePrueba.getName(), cargado.getName(), "El nombre debe mantenerse");
    }

    /**
     * Verifica la función de agregar un nuevo donante a la persistencia
     * mediante {@link DonanteLoader#agregarDonante(Donante)}.
     */
    @Test
    public void testAgregarDonante() {
        DonanteLoader.agregarDonante(donantePrueba);
        List<Donante> donantes = DonanteLoader.cargarDonantes();
        
        boolean encontrado = donantes.stream()
            .anyMatch(d -> d.getId().equals("D001"));
        
        assertTrue(encontrado, "El donante debe estar en la lista");
    }

    // -------------------------------
    // 4. Pruebas de excepciones
    // -------------------------------

    /**
     * Verifica que se lance una excepción si se intenta crear un donante menor de edad.
     */
    @Test
    public void testDonanteMenorDeEdad() {
        assertThrows(Exception.class, () -> {
            new Donante("Menor", (byte)16, "D999", "O+", "Calle", "123",
                       "Sangre", "Saludable", true, "");
        }, "Debe lanzar excepción con donante menor de 18 años");
    }

    /**
     * Verifica que se lance una excepción si el campo de tipo de donación está vacío.
     */
    @Test
    public void testTipoDonacionVacio() {
        assertThrows(Exception.class, () -> {
            new Donante("Test", (byte)25, "D999", "O+", "Calle", "123",
                       "", "Saludable", true, "");
        }, "Debe lanzar excepción con tipo de donación vacío");
    }

    /**
     * Verifica que se lance una excepción si el campo de estado de salud está vacío.
     */
    @Test
    public void testEstadoSaludVacio() {
        assertThrows(Exception.class, () -> {
            new Donante("Test", (byte)25, "D999", "O+", "Calle", "123",
                       "Sangre", "", true, "");
        }, "Debe lanzar excepción con estado de salud vacío");
    }

    /**
     * Verifica que {@link Donante#fromArchivo(String)} retorne null
     * cuando la línea de entrada sea nula o vacía.
     */
    @Test
    public void testFromArchivoLineaVacia() {
        assertNull(Donante.fromArchivo(""), "Línea vacía debe retornar null");
        assertNull(Donante.fromArchivo(null), "Línea nula debe retornar null");
    }

    /**
     * Verifica que {@link Donante#fromArchivo(String)} retorne null
     * cuando el formato de entrada sea inválido.
     */
    @Test
    public void testFromArchivoFormatoInvalido() {
        assertNull(Donante.fromArchivo("Formato|Invalido"), 
                  "Formato inválido debe retornar null");
    }

    /**
     * Verifica que el método {@link Donante#checkInvariant()} no lance excepciones
     * para un donante válido.
     */
    @Test
    public void testCheckInvariant() {
        assertDoesNotThrow(() -> {
            donantePrueba.checkInvariant();
        }, "Las invariantes deben cumplirse en un donante válido");
    }

    /**
     * Verifica que {@link Donante#checkInvariant()} lance una excepción
     * si el órgano especificado no pertenece a la lista de órganos disponibles.
     */
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

    /**
     * Elimina los archivos temporales generados durante las pruebas
     * para mantener un entorno limpio y evitar contaminación entre tests.
     */
    @AfterEach
    public void tearDown() {
        File archivo = new File(RUTA_TEST);
        if (archivo.exists()) {
            archivo.delete();
        }
    }
}
