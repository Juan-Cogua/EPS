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
 * Clase de pruebas unitarias para PanelCita.
 * Verifica la correcta funcionalidad del panel de gestión de citas médicas
 * y su integración con el modelo de datos.
 * 
 * Cubre los 4 puntos de las pruebas unitarias:
 * 1. Invariantes de una clase
 * 2. Métodos para verificación
 * 3. Implementación de pruebas automáticas
 * 4. Pruebas automáticas para el manejo de excepciones
 * 
 * @author Juan Cogua
 * @author Andres Rojas
 * @version 1.0
 */
public class PanelCitaTest {
    /** Ruta del archivo de prueba */
    private static final String RUTA_TEST = "CitaTest.txt";
    
    /** Panel de citas para las pruebas */
    private PanelCita panel;
    
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
     * Crea un paciente, una cita y el panel correspondiente.
     */
    /**
     * Configuración inicial antes de cada prueba.
     * Crea un paciente, una cita y el panel correspondiente.
     */
    @BeforeEach
    public void setUp() throws excepciones.InvalidDataException{
        // Limpiar archivos antes de cada prueba
        File archivoCita = new File("Cita.txt");
        if (archivoCita.exists()) {
            archivoCita.delete();
        }
        
        File archivoPaciente = new File("Paciente.txt");
        if (archivoPaciente.exists()) {
            archivoPaciente.delete();
        }
        
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
        
        // Guardar el paciente para que exista en el sistema
        List<Paciente> pacientes = new ArrayList<>();
        pacientes.add(paciente);
        loaders.PacienteLoader.guardarPacientes(pacientes);
        
        // Crear cita de prueba con fecha futura
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.add(java.util.Calendar.DAY_OF_MONTH, 7);
        Date fechaFutura = cal.getTime();
        
        citaPrueba = new Cita(
            "C001",            // id
            fechaFutura,       // date futura
            fechaFutura,       // time futura
            "Centro Medico",   // location
            paciente,          // paciente
            "Dr. Lopez"        // doctor
        );
        
        panel = new PanelCita();
    }

    // ========== 1. INVARIANTES DE LA CLASE ==========
    
    /**
     * Verifica las invariantes del objeto Cita.
     * Todos los campos obligatorios deben ser no nulos y válidos.
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
     * Verifica que el panel se inicialice correctamente.
     */
    @Test
    public void testInvariantesPanel() {
        assertNotNull(panel, "El panel no debe ser nulo");
    }

    /**
     * Verifica que el estado inicial de una cita sea correcto.
     */
    @Test
    public void testEstadoInicial() {
        assertEquals("Pendiente", citaPrueba.getEstado(), "El estado inicial debe ser Pendiente");
        assertFalse(citaPrueba.isConfirmada(), "La cita no debe estar confirmada inicialmente");
        assertFalse(citaPrueba.isCancelada(), "La cita no debe estar cancelada inicialmente");
    }

    // ========== 2. MÉTODOS PARA VERIFICACIÓN ==========
    
    /**
     * Verifica que el método resumen() genere una cadena
     * con la información relevante de la cita.
     */
    @Test
    public void testResumenCita() {
        String resumen = citaPrueba.resumen();
        assertNotNull(resumen, "El resumen no debe ser nulo");
        assertTrue(resumen.contains("Centro Médico"), "Debe contener la ubicación");
        assertTrue(resumen.contains("Dr. López"), "Debe contener el doctor");
    }

    /**
     * Verifica que la serialización de una cita contenga
     * todos los campos necesarios.
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
     * Verifica que el método confirmar() actualice
     * correctamente el estado de la cita.
     */
    @Test
    public void testConfirmarCita() {
        citaPrueba.confirmar();
        assertTrue(citaPrueba.isConfirmada(), "La cita debe estar confirmada");
    }

    /**
     * Verifica que el método cancelar() actualice
     * correctamente el estado de la cita.
     */
    @Test
    public void testCancelarCita() {
        citaPrueba.cancelar();
        assertTrue(citaPrueba.isCancelada(), "La cita debe estar cancelada");
        assertEquals("Cancelada", citaPrueba.getEstado(), "El estado debe ser Cancelada");
    }

    // ========== 3. IMPLEMENTACIÓN DE PRUEBAS AUTOMÁTICAS ==========
    
    /**
     * Verifica el ciclo completo de guardar y cargar citas,
     * asegurando la persistencia de datos.
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
     * Verifica que se pueda cambiar el estado de una cita.
     */
    @Test
    public void testCambiarEstado() {
        citaPrueba.setEstado("Aprobada");
        assertEquals("Aprobada", citaPrueba.getEstado(), "El estado debe cambiar a Aprobada");
    }

    // ========== 4. PRUEBAS AUTOMÁTICAS PARA EL MANEJO DE EXCEPCIONES ==========
    
    /**
     * Verifica que se lance excepción al crear una cita con ID nulo.
     */
    @Test
    public void testIdNulo() {
        assertThrows(Exception.class, () -> {
            new Cita(null, new Date(), new Date(), "Lugar", paciente, "Doctor");
        }, "Debe lanzar excepción con ID nulo");
    }

    /**
     * Verifica que se lance excepción al crear una cita con ID vacío.
     */
    @Test
    public void testIdVacio() {
        assertThrows(Exception.class, () -> {
            new Cita("", new Date(), new Date(), "Lugar", paciente, "Doctor");
        }, "Debe lanzar excepción con ID vacío");
    }

    /**
     * Verifica que se lance excepción al crear una cita con fecha nula.
     */
    @Test
    public void testFechaNula() {
        assertThrows(Exception.class, () -> {
            new Cita("C999", null, new Date(), "Lugar", paciente, "Doctor");
        }, "Debe lanzar excepción con fecha nula");
    }

    /**
     * Verifica que se lance excepción al crear una cita con hora nula.
     */
    @Test
    public void testHoraNula() {
        assertThrows(Exception.class, () -> {
            new Cita("C999", new Date(), null, "Lugar", paciente, "Doctor");
        }, "Debe lanzar excepción con hora nula");
    }

    /**
     * Verifica que se lance excepción al crear una cita con ubicación vacía.
     */
    @Test
    public void testUbicacionVacia() {
        assertThrows(Exception.class, () -> {
            new Cita("C999", new Date(), new Date(), "", paciente, "Doctor");
        }, "Debe lanzar excepción con ubicación vacía");
    }

    /**
     * Verifica que se lance excepción al crear una cita con paciente nulo.
     */
    @Test
    public void testPacienteNulo() {
        assertThrows(Exception.class, () -> {
            new Cita("C999", new Date(), new Date(), "Lugar", null, "Doctor");
        }, "Debe lanzar excepción con paciente nulo");
    }

    /**
     * Verifica que se lance excepción al crear una cita con doctor vacío.
     */
    @Test
    public void testDoctorVacio() {
        assertThrows(Exception.class, () -> {
            new Cita("C999", new Date(), new Date(), "Lugar", paciente, "");
        }, "Debe lanzar excepción con doctor vacío");
    }

    /**
     * Verifica que las invariantes de la cita se cumplan sin lanzar excepciones.
     */
    @Test
    public void testCheckInvariant() {
        assertDoesNotThrow(() -> {
            citaPrueba.checkInvariant();
        }, "Las invariantes deben cumplirse en una cita válida");
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