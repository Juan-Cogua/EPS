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
 * Clase de pruebas para PanelPaciente siguiendo los 4 puntos:
 * 1. Invariantes de una clase
 * 2. Métodos para verificación
 * 3. Implementación de pruebas automáticas
 * 4. Pruebas automáticas para el manejo de excepciones
 */
public class PanelPacienteTest {
    private static final String RUTA_TEST = "PacienteTest.txt";
    private PanelPaciente panel;
    private Paciente pacientePrueba;

    @BeforeEach
    public void setUp() {
        // Crear datos de prueba
        List<String> alergias = new ArrayList<>();
        alergias.add("Polen");
        alergias.add("Penicilina");
        
        pacientePrueba = new Paciente(
            "Juan Pérez",      // name
            (byte)30,          // age
            "P001",            // id
            "O+",              // bloodType
            "Calle 123",       // address
            "3001234567",      // phone
            70.5,              // weight
            1.75,              // height
            alergias,          // allergies
            new ArrayList<>()  // citas
        );
        
        panel = new PanelPaciente();
    }

    // 1. Invariantes de la clase
    @Test
    public void testInvariantesPaciente() {
        assertNotNull(pacientePrueba.getName(), "El nombre no debe ser nulo");
        assertNotNull(pacientePrueba.getId(), "El ID no debe ser nulo");
        assertNotNull(pacientePrueba.getBloodType(), "El tipo de sangre no debe ser nulo");
        assertTrue(pacientePrueba.getWeight() > 0, "El peso debe ser positivo");
        assertTrue(pacientePrueba.getHeight() > 0, "La altura debe ser positiva");
        assertNotNull(pacientePrueba.getAllergies(), "La lista de alergias no debe ser nula");
    }

    @Test
    public void testInvariantesPanel() {
        assertNotNull(panel, "El panel no debe ser nulo");
    }

    // 2. Métodos para verificación
    @Test
    public void testSerializacionPaciente() {
        String serializado = pacientePrueba.toArchivo();
        assertNotNull(serializado, "La serialización no debe ser nula");
        assertTrue(serializado.contains("Juan Pérez"), "Debe contener el nombre");
        assertTrue(serializado.contains("P001"), "Debe contener el ID");
        assertTrue(serializado.contains("O+"), "Debe contener el tipo de sangre");
        assertTrue(serializado.contains("70.5"), "Debe contener el peso");
    }

    @Test
    public void testDeserializacionPaciente() {
        String linea = pacientePrueba.toArchivo();
        Paciente deserializado = Paciente.fromArchivo(linea);

        assertNotNull(deserializado, "El paciente deserializado no debe ser nulo");
        assertEquals(pacientePrueba.getName(), deserializado.getName(), "El nombre debe mantenerse");
        assertEquals(pacientePrueba.getId(), deserializado.getId(), "El ID debe mantenerse");
        assertEquals(pacientePrueba.getBloodType(), deserializado.getBloodType(), "El tipo de sangre debe mantenerse");
    }

    @Test
    public void testToString() {
        String resultado = pacientePrueba.toString();
        assertNotNull(resultado, "toString no debe retornar nulo");
        assertTrue(resultado.contains("Juan Pérez"), "Debe contener el nombre");
        assertTrue(resultado.contains("P001"), "Debe contener el ID");
    }

    // 3. Implementación de pruebas automáticas
    @Test
    public void testGuardarYCargarPacientes() {
        List<Paciente> pacientes = new ArrayList<>();
        pacientes.add(pacientePrueba);

        // Guardar
        PacienteLoader.guardarPacientes(pacientes);

        // Cargar
        List<Paciente> cargados = PacienteLoader.cargarPacientes();
        assertFalse(cargados.isEmpty(), "La lista cargada no debe estar vacía");

        Paciente cargado = cargados.stream()
            .filter(p -> p.getId().equals("P001"))
            .findFirst()
            .orElse(null);
        
        assertNotNull(cargado, "Debe encontrar el paciente guardado");
        assertEquals(pacientePrueba.getName(), cargado.getName(), "El nombre debe mantenerse");
    }

    @Test
    public void testAgregarPaciente() {
        PacienteLoader.agregarPaciente(pacientePrueba);
        List<Paciente> pacientes = PacienteLoader.cargarPacientes();
        
        boolean encontrado = pacientes.stream()
            .anyMatch(p -> p.getId().equals("P001"));
        
        assertTrue(encontrado, "El paciente debe estar en la lista");
    }

    // 4. Pruebas automáticas para el manejo de excepciones
    @Test
    public void testPesoNegativo() {
        assertThrows(Exception.class, () -> {
            new Paciente("Test", (byte)25, "P999", "A+", "Calle", "123",
                        -10.0, 1.70, new ArrayList<>(), new ArrayList<>());
        }, "Debe lanzar excepción con peso negativo");
    }

    @Test
    public void testAlturaNegativa() {
        assertThrows(Exception.class, () -> {
            new Paciente("Test", (byte)25, "P999", "A+", "Calle", "123",
                        70.0, -1.70, new ArrayList<>(), new ArrayList<>());
        }, "Debe lanzar excepción con altura negativa");
    }

    @Test
    public void testFromArchivoLineaVacia() {
        assertNull(Paciente.fromArchivo(""), "Línea vacía debe retornar null");
        assertNull(Paciente.fromArchivo(null), "Línea nula debe retornar null");
    }

    @Test
    public void testFromArchivoFormatoInvalido() {
        assertNull(Paciente.fromArchivo("Formato|Invalido"), 
                  "Formato inválido debe retornar null");
    }

    @Test
    public void testAgregarCitaNula() {
        assertThrows(Exception.class, () -> {
            pacientePrueba.agregarCita(null);
        }, "Debe lanzar excepción al agregar cita nula");
    }

    @Test
    public void testCheckInvariant() {
        assertDoesNotThrow(() -> {
            pacientePrueba.checkInvariant();
        }, "Las invariantes deben cumplirse en un paciente válido");
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
