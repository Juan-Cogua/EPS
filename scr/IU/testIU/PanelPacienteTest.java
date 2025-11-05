package IU.testIU;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import IU.PanelPaciente;
import model.Paciente;
import loaders.PacienteLoader;

/**
 * Clase de pruebas unitarias para {@link PanelPaciente} y la clase {@link Paciente}.
 * 
 * Esta clase cubre los siguientes aspectos:
 * -Invariantes de la clase {@link Paciente} y del panel.
 * -Verificación de serialización, deserialización y representación textual.
 * -Pruebas automáticas sobre la persistencia con {@link PacienteLoader}.
 * -Pruebas de manejo de excepciones e integridad de datos.
 * 
 * El objetivo es asegurar que el modelo y la interfaz relacionada con los pacientes
 * mantengan consistencia, integridad y correcto funcionamiento bajo distintos escenarios.
 * 
 * @version 2.0
 * @author Andres
 * @author Juan
 */
public class PanelPacienteTest {

    /** Ruta temporal utilizada para pruebas de persistencia. */
    private static final String RUTA_TEST = "PacienteTest.txt";
    /** Panel de prueba correspondiente a la interfaz del paciente. */
    private PanelPaciente panel;
    /** Paciente de ejemplo utilizado para las pruebas unitarias. */
    private Paciente pacientePrueba;

    /**
     * Configura los objetos necesarios antes de cada prueba.
     * 
     *Se inicializa un paciente de prueba con datos válidos, incluyendo una lista de alergias
     * y una lista vacía de citas. También se instancia el panel asociado.
     */
    @BeforeEach
    public void setUp() {
        List<String> alergias = new ArrayList<>();
        alergias.add("Polen");
        alergias.add("Penicilina");
        
        pacientePrueba = new Paciente(
            "Juan Pérez",      // Nombre
            (byte)30,          // Edad
            "P001",            // ID
            "O+",              // Tipo de sangre
            "Calle 123",       // Dirección
            "3001234567",      // Teléfono
            70.5,              // Peso
            1.75,              // Altura
            alergias,          // Alergias
            new ArrayList<>()  // Citas
        );
        
        panel = new PanelPaciente();
    }

    // -------------------------------
    // 1. Invariantes de la clase
    // -------------------------------

    /**
     * Verifica las invariantes de {@link Paciente}, asegurando que todos los
     * atributos obligatorios estén inicializados correctamente y que los valores
     * numéricos sean positivos.
     */
    @Test
    public void testInvariantesPaciente() {
        assertNotNull(pacientePrueba.getName(), "El nombre no debe ser nulo");
        assertNotNull(pacientePrueba.getId(), "El ID no debe ser nulo");
        assertNotNull(pacientePrueba.getBloodType(), "El tipo de sangre no debe ser nulo");
        assertTrue(pacientePrueba.getWeight() > 0, "El peso debe ser positivo");
        assertTrue(pacientePrueba.getHeight() > 0, "La altura debe ser positiva");
        assertNotNull(pacientePrueba.getAllergies(), "La lista de alergias no debe ser nula");
    }

    /**
     * Verifica la invariante del panel, garantizando que la instancia
     * de {@link PanelPaciente} no sea nula.
     */
    @Test
    public void testInvariantesPanel() {
        assertNotNull(panel, "El panel no debe ser nulo");
    }

    // -------------------------------
    // 2. Métodos para verificación
    // -------------------------------

    /**
     * Verifica la correcta serialización de un objeto {@link Paciente} al formato de archivo.
     * 
     * Comprueba que el resultado no sea nulo y que contenga los campos esenciales
     * como nombre, ID, tipo de sangre y peso.
     */
    @Test
    public void testSerializacionPaciente() {
        String serializado = pacientePrueba.toArchivo();
        assertNotNull(serializado, "La serialización no debe ser nula");
        assertTrue(serializado.contains("Juan Pérez"), "Debe contener el nombre");
        assertTrue(serializado.contains("P001"), "Debe contener el ID");
        assertTrue(serializado.contains("O+"), "Debe contener el tipo de sangre");
        assertTrue(serializado.contains("70.5"), "Debe contener el peso");
    }

    /**
     * Verifica la deserialización de un {@link Paciente} desde una línea de texto,
     * asegurando que los valores sean consistentes con los datos originales.
     */
    @Test
    public void testDeserializacionPaciente() {
        String linea = pacientePrueba.toArchivo();
        Paciente deserializado = Paciente.fromArchivo(linea);

        assertNotNull(deserializado, "El paciente deserializado no debe ser nulo");
        assertEquals(pacientePrueba.getName(), deserializado.getName(), "El nombre debe mantenerse");
        assertEquals(pacientePrueba.getId(), deserializado.getId(), "El ID debe mantenerse");
        assertEquals(pacientePrueba.getBloodType(), deserializado.getBloodType(), "El tipo de sangre debe mantenerse");
    }

    /**
     * Verifica que el método {@link Paciente#toString()} produzca una
     * representación textual válida y contenga los datos esenciales.
     */
    @Test
    public void testToString() {
        String resultado = pacientePrueba.toString();
        assertNotNull(resultado, "toString no debe retornar nulo");
        assertTrue(resultado.contains("Juan Pérez"), "Debe contener el nombre");
        assertTrue(resultado.contains("P001"), "Debe contener el ID");
    }

    // -------------------------------
    // 3. Implementación de pruebas automáticas
    // -------------------------------

    /**
     * Verifica el proceso completo de guardado y carga de pacientes usando {@link PacienteLoader}.
     * 
     * Se comprueba que el paciente almacenado pueda recuperarse correctamente,
     * manteniendo su información intacta.
     */
    @Test
    public void testGuardarYCargarPacientes() {
        List<Paciente> pacientes = new ArrayList<>();
        pacientes.add(pacientePrueba);

        PacienteLoader.guardarPacientes(pacientes);
        List<Paciente> cargados = PacienteLoader.cargarPacientes();
        assertFalse(cargados.isEmpty(), "La lista cargada no debe estar vacía");

        Paciente cargado = cargados.stream()
            .filter(p -> p.getId().equals("P001"))
            .findFirst()
            .orElse(null);
        
        assertNotNull(cargado, "Debe encontrar el paciente guardado");
        assertEquals(pacientePrueba.getName(), cargado.getName(), "El nombre debe mantenerse");
    }

    /**
     * Verifica la función {@link PacienteLoader#agregarPaciente(Paciente)} asegurando
     * que un nuevo paciente se añada correctamente a la lista de persistencia.
     */
    @Test
    public void testAgregarPaciente() {
        PacienteLoader.agregarPaciente(pacientePrueba);
        List<Paciente> pacientes = PacienteLoader.cargarPacientes();
        
        boolean encontrado = pacientes.stream()
            .anyMatch(p -> p.getId().equals("P001"));
        
        assertTrue(encontrado, "El paciente debe estar en la lista");
    }

    // -------------------------------
    // 4. Pruebas automáticas para el manejo de excepciones
    // -------------------------------

    /**
     * Verifica que se lance una excepción al intentar crear un paciente con peso negativo.
     */
    @Test
    public void testPesoNegativo() {
        assertThrows(Exception.class, () -> {
            new Paciente("Test", (byte)25, "P999", "A+", "Calle", "123",
                        -10.0, 1.70, new ArrayList<>(), new ArrayList<>());
        }, "Debe lanzar excepción con peso negativo");
    }

    /**
     * Verifica que se lance una excepción al intentar crear un paciente con altura negativa.
     */
    @Test
    public void testAlturaNegativa() {
        assertThrows(Exception.class, () -> {
            new Paciente("Test", (byte)25, "P999", "A+", "Calle", "123",
                        70.0, -1.70, new ArrayList<>(), new ArrayList<>());
        }, "Debe lanzar excepción con altura negativa");
    }

    /**
     * Verifica que el método {@link Paciente#fromArchivo(String)} retorne {@code null}
     * cuando se le pasa una línea vacía o nula.
     */
    @Test
    public void testFromArchivoLineaVacia() {
        assertNull(Paciente.fromArchivo(""), "Línea vacía debe retornar null");
        assertNull(Paciente.fromArchivo(null), "Línea nula debe retornar null");
    }

    /**
     * Verifica que {@link Paciente#fromArchivo(String)} retorne {@code null}
     * cuando el formato del texto sea inválido.
     */
    @Test
    public void testFromArchivoFormatoInvalido() {
        assertNull(Paciente.fromArchivo("Formato|Invalido"), 
                  "Formato inválido debe retornar null");
    }

    /**
     * Verifica que se lance una excepción al intentar agregar una cita nula
     * a la lista de citas del paciente.
     */
    @Test
    public void testAgregarCitaNula() {
        assertThrows(Exception.class, () -> {
            pacientePrueba.agregarCita(null);
        }, "Debe lanzar excepción al agregar cita nula");
    }

    /**
     * Verifica que el método {@link Paciente#checkInvariant()} no lance excepciones
     * cuando el paciente cumple todas las condiciones de validez.
     */
    @Test
    public void testCheckInvariant() {
        assertDoesNotThrow(() -> {
            pacientePrueba.checkInvariant();
        }, "Las invariantes deben cumplirse en un paciente válido");
    }

    /**
     * Elimina los archivos temporales creados durante las pruebas
     * para mantener un entorno limpio y evitar contaminación entre ejecuciones.
     */
    @AfterEach
    public void tearDown() {
        File archivo = new File(RUTA_TEST);
        if (archivo.exists()) {
            archivo.delete();
        }
    }
}
