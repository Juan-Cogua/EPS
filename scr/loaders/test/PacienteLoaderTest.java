package loaders.test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import loaders.PacienteLoader;
import model.Paciente;
import excepciones.InvalidDataException;
import excepciones.NotFoundException;

/**
 * Clase de pruebas unitarias para PacienteLoader.
 * Verifica la correcta funcionalidad de carga, guardado, serialización
 * y deserialización de pacientes.
 * 
 * Cubre los 4 puntos de las pruebas unitarias:
 * 1. Invariantes de una clase (constantes y formatos esperados)
 * 2. Métodos para verificación (serialización/deserialización)
 * 3. Implementación de pruebas automáticas (@BeforeEach y @AfterEach)
 * 4. Pruebas automáticas para el manejo de excepciones (entradas inválidas)
 * 
 * @author Juan Cogua
 * @author Andres Rojas
 * @version 1.0
 */
public class PacienteLoaderTest {
    /** Ruta del archivo de prueba */
    private static final String RUTA_TEST = "PacienteTest.txt";
    
    /** Paciente utilizado para las pruebas */
    private Paciente pacientePrueba;

    /**
     * Configuración inicial antes de cada prueba.
     * Crea un paciente de prueba con datos válidos incluyendo alergias.
     */
    @BeforeEach
    public void setUp() throws InvalidDataException {
        // Crear datos de prueba
        List<String> alergias = new ArrayList<>();
        alergias.add("Maní");
        alergias.add("Lactosa");
        
        pacientePrueba = new Paciente(
            "Sofía Ramírez",   // name
            (byte)35,          // age
            "P001",            // id
            "B-",              // bloodType
            "Carrera 20",      // address
            "3009998877",      // phone
            62.5,              // weight
            1.68,              // height
            alergias,          // allergies
            new ArrayList<>()  // citas
        );
    }

    // ========== 1. INVARIANTES DE LA CLASE ==========
    
    /**
     * Verifica que la clase PacienteLoader exista y sea funcional.
     * Prueba las invariantes básicas del loader.
     */
    @Test
    public void testInvariantesConstantes() {
        assertNotNull(PacienteLoader.class, "La clase PacienteLoader debe existir");
        assertDoesNotThrow(() -> {
            PacienteLoader.cargarPacientes();
        }, "Debe poder cargar pacientes sin errores");
    }

    // ========== 2. MÉTODOS PARA VERIFICACIÓN ==========
    
    /**
     * Verifica que la serialización de un paciente genere
     * una cadena con todos los campos requeridos incluyendo alergias.
     */
    @Test
    public void testSerializacionPaciente() {
        String serializado = pacientePrueba.toArchivo();
        assertNotNull(serializado, "La serialización no debe ser nula");
        assertTrue(serializado.contains("Sofía Ramírez"), "Debe contener el nombre");
        assertTrue(serializado.contains("P001"), "Debe contener el ID");
        assertTrue(serializado.contains("B-"), "Debe contener el tipo de sangre");
        assertTrue(serializado.contains("62.5"), "Debe contener el peso");
        assertTrue(serializado.contains("1.68"), "Debe contener la altura");
        assertTrue(serializado.contains("Maní"), "Debe contener las alergias");
    }

    /**
     * Verifica que la deserialización reconstruya correctamente
     * un objeto Paciente desde una cadena serializada.
     */
    @Test
    public void testDeserializacionPaciente() {
        String linea = pacientePrueba.toArchivo();
        Paciente deserializado = Paciente.fromArchivo(linea);

        assertNotNull(deserializado, "El paciente deserializado no debe ser nulo");
        assertEquals(pacientePrueba.getName(), deserializado.getName(), "El nombre debe mantenerse");
        assertEquals(pacientePrueba.getId(), deserializado.getId(), "El ID debe mantenerse");
        assertEquals(pacientePrueba.getBloodType(), deserializado.getBloodType(), "El tipo de sangre debe mantenerse");
        assertEquals(pacientePrueba.getWeight(), deserializado.getWeight(), 0.01, "El peso debe mantenerse");
        assertEquals(pacientePrueba.getHeight(), deserializado.getHeight(), 0.01, "La altura debe mantenerse");
    }

    /**
     * Verifica que la lista de alergias se deserialice correctamente
     * manteniendo todos sus elementos.
     */
    @Test
    public void testDeserializacionAlergias() {
        String linea = pacientePrueba.toArchivo();
        Paciente deserializado = Paciente.fromArchivo(linea);

        assertNotNull(deserializado.getAllergies(), "Las alergias no deben ser nulas");
        assertEquals(2, deserializado.getAllergies().size(), "Debe tener 2 alergias");
        assertTrue(deserializado.getAllergies().contains("Maní"), "Debe contener la alergia a Maní");
        assertTrue(deserializado.getAllergies().contains("Lactosa"), "Debe contener la alergia a Lactosa");
    }

    /**
     * Verifica que pacientes sin alergias se serialicen correctamente.
     */
    @Test
    public void testSerializacionSinAlergias()throws InvalidDataException {
        Paciente sinAlergias = new Paciente(
            "Test", (byte)25, "P002", "O+", "Calle", "123",
            70.0, 1.75, new ArrayList<>(), new ArrayList<>()
        );
        
        String serializado = sinAlergias.toArchivo();
        assertNotNull(serializado, "La serialización no debe ser nula");
        // El último campo (alergias) debe estar vacío o ser la última separación
        assertTrue(serializado.endsWith(";") || !serializado.contains(",,"), 
                  "Debe manejar correctamente pacientes sin alergias");
    }

    // ========== 3. IMPLEMENTACIÓN DE PRUEBAS AUTOMÁTICAS ==========
    
    /**
     * Verifica que los pacientes se guarden y carguen correctamente
     * manteniendo todos sus datos.
     */
    @Test
    public void testGuardarYCargarPacientes() {
        List<Paciente> pacientes = new ArrayList<>();
        pacientes.add(pacientePrueba);

        // Guardar
        PacienteLoader.guardarPacientes(pacientes);

        // Cargar
        ArrayList<Paciente> cargados = PacienteLoader.cargarPacientes();
        assertFalse(cargados.isEmpty(), "La lista cargada no debe estar vacía");

        Paciente cargado = cargados.stream()
            .filter(p -> p.getId().equals("P001"))
            .findFirst()
            .orElse(null);
        
        assertNotNull(cargado, "Debe encontrar el paciente guardado");
        assertEquals(pacientePrueba.getName(), cargado.getName(), "El nombre debe mantenerse");
        assertEquals(pacientePrueba.getWeight(), cargado.getWeight(), 0.01, "El peso debe mantenerse");
    }

    /**
     * Verifica que se pueda agregar un nuevo paciente correctamente.
     */
    @Test
    public void testAgregarPaciente() {
        PacienteLoader.agregarPaciente(pacientePrueba);
        ArrayList<Paciente> pacientes = PacienteLoader.cargarPacientes();
        
        boolean encontrado = pacientes.stream()
            .anyMatch(p -> p.getId().equals("P001"));
        
        assertTrue(encontrado, "El paciente debe estar en la lista");
    }

    /**
     * Verifica que un paciente se pueda eliminar correctamente por ID.
     */
    @Test
    public void testEliminarPaciente() {
        PacienteLoader.agregarPaciente(pacientePrueba);
        
        boolean eliminado = PacienteLoader.eliminarPaciente("P001");
        assertTrue(eliminado, "El paciente debe ser eliminado");
        
        ArrayList<Paciente> pacientes = PacienteLoader.cargarPacientes();
        boolean existe = pacientes.stream()
            .anyMatch(p -> p.getId().equals("P001"));
        
        assertFalse(existe, "El paciente no debe existir después de eliminarlo");
    }

    /**
     * Verifica que intentar eliminar un paciente inexistente retorne false.
     */
    @Test
    public void testEliminarPacienteInexistente() {
        boolean eliminado = PacienteLoader.eliminarPaciente("P999");
        assertFalse(eliminado, "Debe retornar false al eliminar paciente inexistente");
    }

    /**
     * Verifica que se pueda buscar un paciente por ID correctamente.
     */
    @Test
    public void testBuscarPacientePorId() {
        PacienteLoader.agregarPaciente(pacientePrueba);
        
        Paciente encontrado = PacienteLoader.buscarPacientePorId("P001");
        assertNotNull(encontrado, "Debe encontrar el paciente");
        assertEquals("Sofía Ramírez", encontrado.getName(), "Debe tener el nombre correcto");
    }

    /**
     * Verifica que se puedan guardar múltiples pacientes
     * y cargarlos correctamente.
     */
    @Test
    public void testGuardarMultiplesPacientes() throws InvalidDataException {
        List<Paciente> pacientes = new ArrayList<>();
        pacientes.add(pacientePrueba);
        
        Paciente paciente2 = new Paciente (
            "Carlos López", (byte)40, "P002", "A+", "Calle 2", "3001234567",
            80.0, 1.80, new ArrayList<>(), new ArrayList<>()
        );
        pacientes.add(paciente2);

        PacienteLoader.guardarPacientes(pacientes);
        ArrayList<Paciente> cargados = PacienteLoader.cargarPacientes();
        
        assertEquals(2, cargados.size(), "Debe cargar 2 pacientes");
    }

    // ========== 4. PRUEBAS AUTOMÁTICAS PARA EL MANEJO DE EXCEPCIONES ==========
    
    /**
     * Verifica que intentar buscar un paciente inexistente
     * lance una NotFoundException.
     */
    @Test
    public void testBuscarPacienteInexistente() {
        assertThrows(NotFoundException.class, () -> {
            PacienteLoader.buscarPacientePorId("P999");
        }, "Debe lanzar excepción al buscar paciente inexistente");
    }

    /**
     * Verifica que si el archivo no existe, se retorne una lista vacía.
     */
    @Test
    public void testCargarPacientesArchivoNoExiste() {
        File archivo = new File("PacienteInexistente.txt");
        if (archivo.exists()) {
            archivo.delete();
        }
        
        ArrayList<Paciente> pacientes = PacienteLoader.cargarPacientes();
        assertNotNull(pacientes, "Debe retornar una lista vacía si el archivo no existe");
        assertTrue(pacientes.isEmpty(), "La lista debe estar vacía");
    }

    /**
     * Verifica que líneas vacías o nulas retornen null en la deserialización.
     */
    @Test
    public void testFromArchivoLineaVacia() {
        assertNull(Paciente.fromArchivo(""), "Línea vacía debe retornar null");
        assertNull(Paciente.fromArchivo(null), "Línea nula debe retornar null");
    }

    /**
     * Verifica que formatos con pocos campos retornen null.
     */
    @Test
    public void testFromArchivoFormatoInvalido() {
        assertNull(Paciente.fromArchivo("Dato1;Dato2;Dato3"), 
                  "Formato con pocos campos debe retornar null");
    }

    /**
     * Verifica que edades inválidas retornen null en la deserialización.
     */
    @Test
    public void testFromArchivoEdadInvalida() {
        String lineaInvalida = "Test;EDAD_INVALIDA;P003;O+;Calle;123;70.0;1.75;";
        Paciente resultado = Paciente.fromArchivo(lineaInvalida);
        assertNull(resultado, "Debe retornar null con edad inválida");
    }

    /**
     * Verifica que pesos inválidos retornen null en la deserialización.
     */
    @Test
    public void testFromArchivoPesoInvalido() {
        String lineaInvalida = "Test;25;P003;O+;Calle;123;PESO_INVALIDO;1.75;";
        Paciente resultado = Paciente.fromArchivo(lineaInvalida);
        assertNull(resultado, "Debe retornar null con peso inválido");
    }

    /**
     * Verifica que alturas inválidas retornen null en la deserialización.
     */
    @Test
    public void testFromArchivoAlturaInvalida() {
        String lineaInvalida = "Test;25;P003;O+;Calle;123;70.0;ALTURA_INVALIDA;";
        Paciente resultado = Paciente.fromArchivo(lineaInvalida);
        assertNull(resultado, "Debe retornar null con altura inválida");
    }

    /**
     * Verifica que líneas vacías en el archivo sean ignoradas
     * y solo se carguen las líneas válidas.
     */
    @Test
    public void testCargarPacienteLineaVacia() {
        try {
            File archivo = new File("Paciente.txt");
            java.io.PrintWriter pw = new java.io.PrintWriter(archivo);
            pw.println(""); // Línea vacía
            pw.println(pacientePrueba.toArchivo()); // Línea válida
            pw.close();
            
            ArrayList<Paciente> pacientes = PacienteLoader.cargarPacientes();
            assertEquals(1, pacientes.size(), "Debe ignorar líneas vacías y cargar solo la válida");
        } catch (Exception e) {
            fail("No debe lanzar excepción al cargar archivo con líneas vacías");
        }
    }

    /**
     * Verifica que se pueda guardar una lista vacía sin errores.
     */
    @Test
    public void testGuardarListaVacia() {
        assertDoesNotThrow(() -> {
            PacienteLoader.guardarPacientes(new ArrayList<>());
        }, "Debe poder guardar lista vacía sin errores");
        
        ArrayList<Paciente> cargados = PacienteLoader.cargarPacientes();
        assertTrue(cargados.isEmpty(), "Debe cargar lista vacía correctamente");
    }

    /**
     * Limpieza después de cada prueba.
     * Elimina los archivos temporales creados.
     */
    @AfterEach
    public void tearDown() {
        // Limpiar archivos de prueba
        File archivo = new File("Paciente.txt");
        if (archivo.exists()) {
            archivo.delete();
        }
    }
}