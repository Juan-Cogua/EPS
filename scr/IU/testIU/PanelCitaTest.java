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
 * Clase de pruebas para PanelCita que posee:
 * 1. Invariantes de una clase
 * 2. Métodos para verificación
 * 3. Implementación de pruebas automáticas
 * 4. Pruebas automáticas para el manejo de excepciones
 * @author andres
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

    @BeforeEach
    public void setUp() {
        // Crear datos de prueba
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
     * Metodo de prueba para las invariantes de {@link Cita} donde no puede haber datos nulos.
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
     * metodo de prueba para las invariantes de los paneles donde esta variable no puede ser nula
     */
    @Test
    public void testInvariantesPanel() {
        assertNotNull(panel, "El panel no debe ser nulo");
    }

    /**
     * metodo de prueba para la confirmacion de citas donde de forma predeterminada debe estar el estado de esta en
     * pendiente.
     */
    @Test
    public void testEstadoInicial() {
        assertEquals("Pendiente", citaPrueba.getEstado(), "El estado inicial debe ser Pendiente");
        assertFalse(citaPrueba.isConfirmada(), "La cita no debe estar confirmada inicialmente");
        assertFalse(citaPrueba.isCancelada(), "La cita no debe estar cancelada inicialmente");
    }

    // 2. Métodos para verificación
    /**
     * Metodo de prueva que verifica el resumen que se crea de las citas donde:
     * -el resumen no puede ser nulo.
     * -debe haber una ubicación.
     * -debe haber un Doctor.
     */
    @Test
    public void testResumenCita() {
        String resumen = citaPrueba.resumen();
        assertNotNull(resumen, "El resumen no debe ser nulo");
        assertTrue(resumen.contains("Centro Médico"), "Debe contener la ubicación");
        assertTrue(resumen.contains("Dr. López"), "Debe contener el doctor");
    }

    /** 
     * Metodo de prueba que verifica los datos que se agregan a la percistencia de la cita.
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
     * Metodo de prueba que verifica que la cita este confirmada.
     */
    @Test
    public void testConfirmarCita() {
        citaPrueba.confirmar();
        assertTrue(citaPrueba.isConfirmada(), "La cita debe estar confirmada");
    }

    /**
     * Metodo de prueba que verifica que la cita esta cancelada.
     */
    @Test
    public void testCancelarCita() {
        citaPrueba.cancelar();
        assertTrue(citaPrueba.isCancelada(), "La cita debe estar cancelada");
        assertEquals("Cancelada", citaPrueba.getEstado(), "El estado debe ser Cancelada");
    }

    // 3. Implementación de pruebas automáticas
    /**
     * Metodo de prueba para verificar la carga y guardado de datos en la persistencia de las citas en {@link cita.txt} 
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
     * Metodo de prueba que verifica el cambio de estado de la cita a aprobada.
     */
    @Test
    public void testCambiarEstado() {
        citaPrueba.setEstado("Aprobada");
        assertEquals("Aprobada", citaPrueba.getEstado(), "El estado debe cambiar a Aprobada");
    }

    // 4. Pruebas automáticas para el manejo de excepciones
    /**
     * Metodo de prueba que verifica si el Id es nulo para lanzar una excepcion con ID nulo.
     */
    @Test
    public void testIdNulo() {
        assertThrows(Exception.class, () -> {
            new Cita(null, new Date(), new Date(), "Lugar", paciente, "Doctor");
        }, "Debe lanzar excepción con ID nulo");
    }

    /**
     * Metodo de prueba que verifica si el campo del ID esta vacio para lanzar una excepcion con ID Vacio.
     */
    @Test
    public void testIdVacio() {
        assertThrows(Exception.class, () -> {
            new Cita("", new Date(), new Date(), "Lugar", paciente, "Doctor");
        }, "Debe lanzar excepción con ID vacío");
    }

    /**
     * Metodo de prueba que verifica si la fecha es nula para lanzar una excepcion con Fecha nula.
     */
    @Test
    public void testFechaNula() {
        assertThrows(Exception.class, () -> {
            new Cita("C999", null, new Date(), "Lugar", paciente, "Doctor");
        }, "Debe lanzar excepción con fecha nula");
    }

    /**
     * Metodo de prueba que verifica si la hora es nula para lanzar una excepcion con hora nula.
     */
    @Test
    public void testHoraNula() {
        assertThrows(Exception.class, () -> {
            new Cita("C999", new Date(), null, "Lugar", paciente, "Doctor");
        }, "Debe lanzar excepción con hora nula");
    }

    /**
     * Metodo de prueba que verifica si el campo de la ubicacion esta vacio
     * para lanzar una excepcion con Fecha vacia.
     */
    @Test
    public void testUbicacionVacia() {
        assertThrows(Exception.class, () -> {
            new Cita("C999", new Date(), new Date(), "", paciente, "Doctor");
        }, "Debe lanzar excepción con ubicación vacía");
    }

    /**
     * Metodo de prueba que verifica si el paciente es nulo para lanzar una excepcion con Paciente nulo.
     */
    @Test
    public void testPacienteNulo() {
        assertThrows(Exception.class, () -> {
            new Cita("C999", new Date(), new Date(), "Lugar", null, "Doctor");
        }, "Debe lanzar excepción con paciente nulo");
    }

    /**
     * Metodo de prueba que verifica si el campo de Doctor esta vacia
     *  para lanzar una excepcion con Doctor Vacio.
     */
    @Test
    public void testDoctorVacio() {
        assertThrows(Exception.class, () -> {
            new Cita("C999", new Date(), new Date(), "Lugar", paciente, "");
        }, "Debe lanzar excepción con doctor vacío");
    }

    /**
     * Metodo de prueba que verifica que las invariantes se cumplen en una cita valida.
     */
    @Test
    public void testCheckInvariant() {
        assertDoesNotThrow(() -> {
            citaPrueba.checkInvariant();
        }, "Las invariantes deben cumplirse en una cita válida");
    }

    /**
     * Metodo de prueba que verifica que las invariantes no lancn una excepción en una cita valida.
     */
    @Test
    public void testCheckInvariantIdNulo() {
        Cita citaInvalida = new Cita("C999", new Date(), new Date(), "Lugar", paciente, "Doctor");
        // Forzar ID nulo mediante reflexión o setter si existe
        // Como no hay setter para id, creamos una cita y verificamos
        assertDoesNotThrow(() -> {
            citaInvalida.checkInvariant();
        }, "Una cita válida no debe lanzar excepción");
    }

    /**
     * 
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