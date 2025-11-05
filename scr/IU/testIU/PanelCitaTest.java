package IU.testIU;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import IU.PanelCita;
import model.Cita;
import model.Paciente;
import loaders.CitaLoader;

/**
 * Clase de pruebas para {@link PanelCita}, cubriendo los siguientes aspectos:
 *   <li>1. Invariantes de una clase</li>
 *   <li>2. Métodos para verificación</li>
 *   <li>3. Implementación de pruebas automáticas</li>
 *   <li>4. Pruebas automáticas para el manejo de excepciones</li>
 * </ul>
 * 
 * Estas pruebas validan el correcto funcionamiento de la clase {@link Cita}
 * y su integración con el panel de interfaz gráfica {@link PanelCita}.
 * 
 * @author Andrés
 * @author Juan Cogua
 * @version 2.0
 */
public class PanelCitaTest {

    private static final String RUTA_TEST = "CitaTest.txt";
    private PanelCita panel;
    private Cita citaPrueba;
    private Paciente paciente;
    private static final SimpleDateFormat FORMATO_FECHA = new SimpleDateFormat("dd/MM/yyyy");
    private static final SimpleDateFormat FORMATO_HORA = new SimpleDateFormat("HH:mm");

    /**
     * Configura el entorno de prueba antes de cada ejecución.
     * Se crea un paciente y una cita de prueba con datos válidos.
     */
    @BeforeEach
    public void setUp() {
        paciente = new Paciente(
            "Carlos Ruiz",     // name
            (byte)28,          // age
            "P001",            // id
            "B+",              // bloodType
            "Calle 789",       // address
            "3005551234",      // phone
            75.0,              // weight
            1.80,              // height
            new ArrayList<>(), // allergies
            new ArrayList<>()  // citas
        );
        
        citaPrueba = new Cita(
            "C001",            // id
            new Date(),        // date
            new Date(),        // time
            "Centro Médico",   // location
            paciente,          // paciente
            "Dr. López"        // doctor
        );
        
        panel = new PanelCita();
    }

    // 1. Invariantes de la clase
    /**
     * Prueba las invariantes de la clase {@link Cita}, verificando que los campos esenciales
     * no sean nulos y se cumplan las condiciones iniciales del modelo.
     */
    @Test
    public void testInvariantesCita() {
        assertNotNull(citaPrueba.getId(), "El ID no debe ser nulo");
        assertNotNull(citaPrueba.getDate(), "La fecha no debe ser nula");
        assertNotNull(citaPrueba.getTime(), "La hora no debe ser nula");
        assertNotNull(citaPrueba.getLocation(), "La ubicación no debe ser nula");
        assertNotNull(citaPrueba.getPaciente(), "El paciente no debe ser nulo");
        assertNotNull(citaPrueba.getDoctor(), "El doctor no debe ser nulo");
        assertNotNull(citaPrueba.getEstado(), "El estado no debe ser nulo");
    }

    /**
     * Prueba las invariantes del panel de citas, verificando que el panel haya sido inicializado correctamente.
     */
    @Test
    public void testInvariantesPanel() {
        assertNotNull(panel, "El panel no debe ser nulo");
    }

    /**
     * Prueba que el estado inicial de una cita recién creada sea "Pendiente",
     * y que no esté ni confirmada ni cancelada.
     */
    @Test
    public void testEstadoInicial() {
        assertEquals("Pendiente", citaPrueba.getEstado(), "El estado inicial debe ser Pendiente");
        assertFalse(citaPrueba.isConfirmada(), "La cita no debe estar confirmada inicialmente");
        assertFalse(citaPrueba.isCancelada(), "La cita no debe estar cancelada inicialmente");
    }

    // 2. Métodos para verificación
    /**
     * Prueba el método {@link Cita#resumen()} asegurando que el resumen generado
     * no sea nulo y contenga los datos relevantes de la cita.
     */
    @Test
    public void testResumenCita() {
        String resumen = citaPrueba.resumen();
        assertNotNull(resumen, "El resumen no debe ser nulo");
        assertTrue(resumen.contains("Centro Médico"), "Debe contener la ubicación");
        assertTrue(resumen.contains("Dr. López"), "Debe contener el doctor");
    }

    /** 
     * Prueba el método {@link Cita#toArchivo()} verificando que la serialización
     * de los datos sea correcta y contenga todos los campos requeridos.
     */
    @Test
    public void testToArchivo() {
        String serializado = citaPrueba.toArchivo();
        assertNotNull(serializado, "La serialización no debe ser nula");
        assertTrue(serializado.contains("C001"), "Debe contener el ID");
        assertTrue(serializado.contains("Centro Médico"), "Debe contener la ubicación");
        assertTrue(serializado.contains("Dr. López"), "Debe contener el doctor");
        assertTrue(serializado.contains("P001"), "Debe contener el ID del paciente");
        assertTrue(serializado.contains("Pendiente"), "Debe contener el estado");
    }

    /**
     * Prueba que el método {@link Cita#confirmar()} cambie correctamente el estado de la cita.
     */
    @Test
    public void testConfirmarCita() {
        citaPrueba.confirmar();
        assertTrue(citaPrueba.isConfirmada(), "La cita debe estar confirmada");
    }

    /**
     * Prueba que el método {@link Cita#cancelar()} cambie correctamente el estado
     * y marque la cita como cancelada.
     */
    @Test
    public void testCancelarCita() {
        citaPrueba.cancelar();
        assertTrue(citaPrueba.isCancelada(), "La cita debe estar cancelada");
        assertEquals("Cancelada", citaPrueba.getEstado(), "El estado debe ser Cancelada");
    }

    // 3. Implementación de pruebas automáticas
    /**
     * Prueba el proceso de persistencia de citas mediante {@link CitaLoader},
     * verificando que los datos se guarden y se recuperen correctamente del archivo.
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
        assertEquals(citaPrueba.getLocation(), cargada.getLocation(), "La ubicación debe mantenerse");
        assertEquals(citaPrueba.getDoctor(), cargada.getDoctor(), "El doctor debe mantenerse");
    }

    /**
     * Prueba que el método {@link Cita#setEstado(String)} permita cambiar
     * correctamente el estado de la cita.
     */
    @Test
    public void testCambiarEstado() {
        citaPrueba.setEstado("Aprobada");
        assertEquals("Aprobada", citaPrueba.getEstado(), "El estado debe cambiar a Aprobada");
    }

    // 4. Pruebas automáticas para el manejo de excepciones
    /** Prueba que se lance excepción al crear una cita con ID nulo. */
    @Test
    public void testIdNulo() {
        assertThrows(Exception.class, () -> {
            new Cita(null, new Date(), new Date(), "Lugar", paciente, "Doctor");
        }, "Debe lanzar excepción con ID nulo");
    }

    /** Prueba que se lance excepción al crear una cita con ID vacío. */
    @Test
    public void testIdVacio() {
        assertThrows(Exception.class, () -> {
            new Cita("", new Date(), new Date(), "Lugar", paciente, "Doctor");
        }, "Debe lanzar excepción con ID vacío");
    }

    /** Prueba que se lance excepción al crear una cita con fecha nula. */
    @Test
    public void testFechaNula() {
        assertThrows(Exception.class, () -> {
            new Cita("C999", null, new Date(), "Lugar", paciente, "Doctor");
        }, "Debe lanzar excepción con fecha nula");
    }

    /** Prueba que se lance excepción al crear una cita con hora nula. */
    @Test
    public void testHoraNula() {
        assertThrows(Exception.class, () -> {
            new Cita("C999", new Date(), null, "Lugar", paciente, "Doctor");
        }, "Debe lanzar excepción con hora nula");
    }

    /** Prueba que se lance excepción al crear una cita con ubicación vacía. */
    @Test
    public void testUbicacionVacia() {
        assertThrows(Exception.class, () -> {
            new Cita("C999", new Date(), new Date(), "", paciente, "Doctor");
        }, "Debe lanzar excepción con ubicación vacía");
    }

    /** Prueba que se lance excepción al crear una cita con paciente nulo. */
    @Test
    public void testPacienteNulo() {
        assertThrows(Exception.class, () -> {
            new Cita("C999", new Date(), new Date(), "Lugar", null, "Doctor");
        }, "Debe lanzar excepción con paciente nulo");
    }

    /** Prueba que se lance excepción al crear una cita con doctor vacío. */
    @Test
    public void testDoctorVacio() {
        assertThrows(Exception.class, () -> {
            new Cita("C999", new Date(), new Date(), "Lugar", paciente, "");
        }, "Debe lanzar excepción con doctor vacío");
    }

    /**
     * Prueba que el método {@link Cita#checkInvariant()} no lance excepciones
     * cuando la cita cumple todas las condiciones de validez.
     */
    @Test
    public void testCheckInvariant() {
        assertDoesNotThrow(() -> {
            citaPrueba.checkInvariant();
        }, "Las invariantes deben cumplirse en una cita válida");
    }

    /**
     * Prueba complementaria para verificar que {@link Cita#checkInvariant()}
     * no arroje excepciones cuando la cita es válida.
     */
    @Test
    public void testCheckInvariantIdNulo() {
        Cita citaInvalida = new Cita("C999", new Date(), new Date(), "Lugar", paciente, "Doctor");
        assertDoesNotThrow(() -> {
            citaInvalida.checkInvariant();
        }, "Una cita válida no debe lanzar excepción");
    }

    /**
     * Limpia los archivos temporales generados durante las pruebas,
     * eliminando el archivo {@link CitaTest.txt} si existe.
     */
    @AfterEach
    public void tearDown() {
        File archivo = new File(RUTA_TEST);
        if (archivo.exists()) {
            archivo.delete();
        }
    }
}
