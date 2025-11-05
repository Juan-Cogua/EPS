package loaders.test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import loaders.CitaLoader;
import loaders.PacienteLoader;
import model.Cita;
import model.Paciente;

/**
 * Clase de pruebas unitarias para CitaLoader.
 * Verifica la correcta funcionalidad de carga, guardado, serialización
 * y deserialización de citas médicas.
 * 
 * Cubre los 4 puntos de las pruebas unitarias:
 * 1. Invariantes de una clase (constantes y formatos esperados)
 * 2. Métodos para verificación (serialización/deserialización)
 * 3. Implementación de pruebas automáticas (@BeforeEach y @AfterEach)
 * 4. Pruebas automáticas para el manejo de excepciones (entradas inválidas)
 * 
 * @author Juan Cogua
 * @author Andres Rojas
 * @version 2.0
 */
public class CitaLoaderTest {
    /** Ruta del archivo de prueba */
    private static final String RUTA_TEST = "CitaTest.txt";
    
    /** Cita utilizada para las pruebas */
    private Cita citaPrueba;
    
    /** Paciente asociado a la cita de prueba */
    private Paciente paciente;
    
    /** Formato de fecha utilizado en las pruebas */
    private static final SimpleDateFormat FORMATO_FECHA = new SimpleDateFormat("dd/MM/yyyy");
    
    /** Formato de hora utilizado en las pruebas */
    private static final SimpleDateFormat FORMATO_HORA = new SimpleDateFormat("HH:mm");

    /**
     * Configuración inicial antes de cada prueba.
     * Crea un paciente y una cita de prueba.
     */
    @BeforeEach
    public void setUp() {
        // Crear paciente de prueba y guardarlo
        paciente = new Paciente(
            "Pedro Martínez",  // name
            (byte)32,          // age
            "P001",            // id
            "O+",              // bloodType
            "Calle 111",       // address
            "3001112233",      // phone
            68.0,              // weight
            1.70,              // height
            new ArrayList<>(), // allergies
            new ArrayList<>()  // citas
        );
        
        // Asegurarse de que el paciente existe en el sistema
        List<Paciente> pacientes = new ArrayList<>();
        pacientes.add(paciente);
        PacienteLoader.guardarPacientes(pacientes);
        
        // Crear cita de prueba
        citaPrueba = new Cita(
            "C001",            // id
            new Date(),        // date
            new Date(),        // time
            "Hospital Central",// location
            paciente,          // paciente
            "Dr. García"       // doctor
        );
    }

    // ========== 1. INVARIANTES DE LA CLASE ==========
    
    /**
     * Verifica que las constantes de formato no sean nulas.
     * Prueba las invariantes de la clase.
     */
    @Test
    public void testInvariantesConstantes() {
        assertNotNull(FORMATO_FECHA, "El formato de fecha no debe ser nulo");
        assertNotNull(FORMATO_HORA, "El formato de hora no debe ser nulo");
    }

    // ========== 2. MÉTODOS PARA VERIFICACIÓN ==========
    
    /**
     * Verifica que la serialización de una cita genere
     * una cadena con todos los campos requeridos.
     */
    @Test
    public void testSerializacionCita() {
        String serializado = citaPrueba.toArchivo();
        assertNotNull(serializado, "La serialización no debe ser nula");
        assertTrue(serializado.contains("C001"), "Debe contener el ID de la cita");
        assertTrue(serializado.contains("Hospital Central"), "Debe contener la ubicación");
        assertTrue(serializado.contains("Dr. García"), "Debe contener el doctor");
        assertTrue(serializado.contains("P001"), "Debe contener el ID del paciente");
        assertTrue(serializado.contains("Pendiente"), "Debe contener el estado");
    }

    /**
     * Verifica que la deserialización reconstruya correctamente
     * un objeto Cita desde una cadena serializada.
     */
    @Test
    public void testDeserializacionCita() {
        String linea = citaPrueba.toArchivo();
        Cita deserializada = CitaLoader.fromArchivo(linea);

        assertNotNull(deserializada, "La cita deserializada no debe ser nula");
        assertEquals(citaPrueba.getId(), deserializada.getId(), "El ID debe mantenerse");
        assertEquals(citaPrueba.getLocation(), deserializada.getLocation(), "La ubicación debe mantenerse");
        assertEquals(citaPrueba.getDoctor(), deserializada.getDoctor(), "El doctor debe mantenerse");
    }

    /**
     * Verifica que los estados de las citas se normalicen correctamente
     * al guardar y cargar (PENDIENTE -> Pendiente).
     */
    @Test
    public void testNormalizacionEstados() {
        citaPrueba.setEstado("PENDIENTE");
        List<Cita> citas = new ArrayList<>();
        citas.add(citaPrueba);
        CitaLoader.guardarCitas(citas);
        
        List<Cita> cargadas = CitaLoader.cargarCitas();
        Cita cargada = cargadas.stream()
            .filter(c -> c.getId().equals("C001"))
            .findFirst()
            .orElse(null);
        
        assertNotNull(cargada, "La cita debe existir");
        assertEquals("Pendiente", cargada.getEstado(), "El estado debe normalizarse a 'Pendiente'");
    }

    // ========== 3. IMPLEMENTACIÓN DE PRUEBAS AUTOMÁTICAS ==========
    
    /**
     * Verifica que las citas se guarden y carguen correctamente
     * manteniendo su integridad.
     */
    @Test
    public void testGuardarYCargarCitas() {
        List<Cita> citas = new ArrayList<>();
        citas.add(citaPrueba);

        // Guardar
        CitaLoader.guardarCitas(citas);

        // Cargar
        List<Cita> cargadas = CitaLoader.cargarCitas();
        assertFalse(cargadas.isEmpty(), "La lista cargada no debe estar vacía");

        Cita cargada = cargadas.stream()
            .filter(c -> c.getId().equals("C001"))
            .findFirst()
            .orElse(null);
        
        assertNotNull(cargada, "Debe encontrar la cita guardada");
        assertEquals(citaPrueba.getId(), cargada.getId(), "El ID debe mantenerse");
        assertEquals(citaPrueba.getDoctor(), cargada.getDoctor(), "El doctor debe mantenerse");
    }

    /**
     * Verifica que una cita se pueda eliminar (cancelar) correctamente por ID.
     */
    @Test
    public void testEliminarCitaPorId() {
        List<Cita> citas = new ArrayList<>();
        citas.add(citaPrueba);
        CitaLoader.guardarCitas(citas);

        // Eliminar (cancelar) la cita
        boolean cancelada = CitaLoader.eliminarCitaPorId("C001");
        assertTrue(cancelada, "La cita debe ser cancelada");

        // Verificar que está cancelada
        List<Cita> cargadas = CitaLoader.cargarCitas();
        Cita citaCancelada = cargadas.stream()
            .filter(c -> c.getId().equals("C001"))
            .findFirst()
            .orElse(null);
        
        assertNotNull(citaCancelada, "La cita debe existir");
        assertEquals("Cancelada", citaCancelada.getEstado(), "El estado debe ser Cancelada");
    }

    /**
     * Verifica que intentar eliminar una cita inexistente retorne false.
     */
    @Test
    public void testEliminarCitaInexistente() {
        boolean resultado = CitaLoader.eliminarCitaPorId("C999");
        assertFalse(resultado, "No debe cancelar una cita que no existe");
    }

    /**
     * Verifica que no se pueda cancelar una cita que ya está cancelada.
     */
    @Test
    public void testEliminarCitaYaCancelada() {
        citaPrueba.setEstado("Cancelada");
        List<Cita> citas = new ArrayList<>();
        citas.add(citaPrueba);
        CitaLoader.guardarCitas(citas);

        boolean resultado = CitaLoader.eliminarCitaPorId("C001");
        assertFalse(resultado, "No debe cancelar una cita ya cancelada");
    }

    // ========== 4. PRUEBAS AUTOMÁTICAS PARA EL MANEJO DE EXCEPCIONES ==========
    
    /**
     * Verifica que líneas vacías o nulas retornen null en la deserialización.
     */
    @Test
    public void testFromArchivoLineaVacia() {
        assertNull(CitaLoader.fromArchivo(""), "Línea vacía debe retornar null");
        assertNull(CitaLoader.fromArchivo(null), "Línea nula debe retornar null");
    }

    /**
     * Verifica que formatos inválidos retornen null en la deserialización.
     */
    @Test
    public void testFromArchivoFormatoInvalido() {
        assertNull(CitaLoader.fromArchivo("Formato|Invalido"), 
                  "Formato inválido debe retornar null");
    }

    /**
     * Verifica que si el paciente no existe, la deserialización retorne null.
     */
    @Test
    public void testFromArchivoPacienteNoEncontrado() {
        String lineaInvalida = "C999;01/01/2025;10:00;Hospital;P999;Dr. Test;Pendiente";
        Cita resultado = CitaLoader.fromArchivo(lineaInvalida);
        assertNull(resultado, "Debe retornar null si el paciente no existe");
    }

    /**
     * Verifica que fechas inválidas retornen null en la deserialización.
     */
    @Test
    public void testFromArchivoFechaInvalida() {
        String lineaInvalida = "C999;FECHA_INVALIDA;10:00;Hospital;P001;Dr. Test;Pendiente";
        Cita resultado = CitaLoader.fromArchivo(lineaInvalida);
        assertNull(resultado, "Debe retornar null con fecha inválida");
    }

    /**
     * Verifica que horas inválidas retornen null en la deserialización.
     */
    @Test
    public void testFromArchivoHoraInvalida() {
        String lineaInvalida = "C999;01/01/2025;HORA_INVALIDA;Hospital;P001;Dr. Test;Pendiente";
        Cita resultado = CitaLoader.fromArchivo(lineaInvalida);
        assertNull(resultado, "Debe retornar null con hora inválida");
    }

    /**
     * Verifica que líneas con campos insuficientes retornen null.
     */
    @Test
    public void testFromArchivoCamposInsuficientes() {
        String lineaInvalida = "C999;01/01/2025;10:00";
        Cita resultado = CitaLoader.fromArchivo(lineaInvalida);
        assertNull(resultado, "Debe retornar null con campos insuficientes");
    }

    /**
     * Verifica que si el archivo no existe, se retorne una lista vacía.
     */
    @Test
    public void testCargarCitasArchivoNoExiste() {
        File archivo = new File("ArchivoInexistente.txt");
        if (archivo.exists()) {
            archivo.delete();
        }
        
        List<Cita> citas = CitaLoader.cargarCitas();
        assertNotNull(citas, "Debe retornar una lista vacía si el archivo no existe");
        assertTrue(citas.isEmpty(), "La lista debe estar vacía");
    }

    /**
     * Limpieza después de cada prueba.
     * Elimina los archivos temporales creados.
     */
    @AfterEach
    public void tearDown() {
        // Limpiar archivos de prueba
        File archivoCita = new File("Cita.txt");
        if (archivoCita.exists()) {
            archivoCita.delete();
        }
        
        File archivoPaciente = new File("Paciente.txt");
        if (archivoPaciente.exists()) {
            archivoPaciente.delete();
        }
    }
}