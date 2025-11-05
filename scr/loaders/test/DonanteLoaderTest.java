package loaders.test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import loaders.DonanteLoader;
import model.Donante;
import excepciones.InvalidDataException;
import excepciones.NotFoundException;

/**
 * Clase de pruebas unitarias para DonanteLoader.
 * Verifica la correcta funcionalidad de carga, guardado, serialización
 * y deserialización de donantes.
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
public class DonanteLoaderTest {
    /** Ruta del archivo de prueba */
    private static final String RUTA_TEST = "DonanteTest.txt";
    
    /** Donante utilizado para las pruebas */
    private Donante donantePrueba;

    /**
     * Configuración inicial antes de cada prueba.
     * Crea un donante de prueba con datos válidos.
     */
    @BeforeEach
    public void setUp() {
        // Crear datos de prueba
        donantePrueba = new Donante(
            "Laura Fernández",  // name
            (byte)28,           // age (>= 18)
            "D001",             // id
            "A-",               // bloodType
            "Avenida 50",       // address
            "3007778899",       // phone
            "Órganos",          // donationType
            "Excelente",        // healthStatus
            true,               // eligibility
            "Hígado"            // organo
        );
    }

    // ========== 1. INVARIANTES DE LA CLASE ==========
    
    /**
     * Verifica que la clase DonanteLoader exista y sea funcional.
     * Prueba las invariantes básicas del loader.
     */
    @Test
    public void testInvariantesConstantes() {
        assertNotNull(DonanteLoader.class, "La clase DonanteLoader debe existir");
        // Verificar que la ruta es accesible
        assertDoesNotThrow(() -> {
            DonanteLoader.cargarDonantes();
        }, "Debe poder cargar donantes sin errores");
    }

    // ========== 2. MÉTODOS PARA VERIFICACIÓN ==========
    
    /**
     * Verifica que la serialización de un donante genere
     * una cadena con todos los campos requeridos.
     */
    @Test
    public void testSerializacionDonante() {
        String serializado = donantePrueba.toArchivo();
        assertNotNull(serializado, "La serialización no debe ser nula");
        assertTrue(serializado.contains("Laura Fernández"), "Debe contener el nombre");
        assertTrue(serializado.contains("D001"), "Debe contener el ID");
        assertTrue(serializado.contains("A-"), "Debe contener el tipo de sangre");
        assertTrue(serializado.contains("Órganos"), "Debe contener el tipo de donación");
        assertTrue(serializado.contains("Hígado"), "Debe contener el órgano");
        assertTrue(serializado.contains("1"), "Debe contener la elegibilidad (1=true)");
    }

    /**
     * Verifica que la deserialización contenga el número correcto
     * de campos y en el orden esperado.
     */
    @Test
    public void testDeserializacionDonante() {
        String linea = donantePrueba.toArchivo();
        String[] datos = linea.split(";");
        
        assertEquals(10, datos.length, "Debe tener 10 campos");
        assertEquals("Laura Fernández", datos[0], "Primer campo debe ser el nombre");
        assertEquals("28", datos[1], "Segundo campo debe ser la edad");
        assertEquals("D001", datos[2], "Tercer campo debe ser el ID");
    }

    /**
     * Verifica que la elegibilidad se serialice correctamente
     * como 1 (elegible) o 0 (no elegible).
     */
    @Test
    public void testFormatoElegibilidad() {
        // Donante elegible
        String lineaElegible = donantePrueba.toArchivo();
        assertTrue(lineaElegible.contains(";1;"), "Donante elegible debe contener '1'");
        
        // Donante no elegible
        Donante donanteNoElegible = new Donante(
            "Test", (byte)25, "D002", "B+", "Calle", "123",
            "Sangre", "Bueno", false, ""
        );
        String lineaNoElegible = donanteNoElegible.toArchivo();
        assertTrue(lineaNoElegible.contains(";0;"), "Donante no elegible debe contener '0'");
    }

    // ========== 3. IMPLEMENTACIÓN DE PRUEBAS AUTOMÁTICAS ==========
    
    /**
     * Verifica que los donantes se guarden y carguen correctamente
     * manteniendo todos sus datos.
     */
    @Test
    public void testGuardarYCargarDonantes() {
        List<Donante> donantes = new ArrayList<>();
        donantes.add(donantePrueba);

        // Guardar
        DonanteLoader.guardarDonantes(donantes);

        // Cargar
        ArrayList<Donante> cargados = DonanteLoader.cargarDonantes();
        assertFalse(cargados.isEmpty(), "La lista cargada no debe estar vacía");

        Donante cargado = cargados.stream()
            .filter(d -> d.getId().equals("D001"))
            .findFirst()
            .orElse(null);
        
        assertNotNull(cargado, "Debe encontrar el donante guardado");
        assertEquals(donantePrueba.getName(), cargado.getName(), "El nombre debe mantenerse");
        assertEquals(donantePrueba.getAge(), cargado.getAge(), "La edad debe mantenerse");
        assertEquals(donantePrueba.getOrgano(), cargado.getOrgano(), "El órgano debe mantenerse");
    }

    /**
     * Verifica que se pueda agregar un nuevo donante correctamente.
     */
    @Test
    public void testAgregarDonante() {
        DonanteLoader.agregarDonante(donantePrueba);
        ArrayList<Donante> donantes = DonanteLoader.cargarDonantes();
        
        boolean encontrado = donantes.stream()
            .anyMatch(d -> d.getId().equals("D001"));
        
        assertTrue(encontrado, "El donante debe estar en la lista");
    }

    /**
     * Verifica que un donante se pueda eliminar correctamente por ID.
     */
    @Test
    public void testEliminarDonante() {
        DonanteLoader.agregarDonante(donantePrueba);
        
        boolean eliminado = DonanteLoader.eliminarDonante("D001");
        assertTrue(eliminado, "El donante debe ser eliminado");
        
        ArrayList<Donante> donantes = DonanteLoader.cargarDonantes();
        boolean existe = donantes.stream()
            .anyMatch(d -> d.getId().equals("D001"));
        
        assertFalse(existe, "El donante no debe existir después de eliminarlo");
    }

    /**
     * Verifica que se pueda buscar un donante por ID correctamente.
     */
    @Test
    public void testBuscarDonantePorId() {
        DonanteLoader.agregarDonante(donantePrueba);
        
        Donante encontrado = DonanteLoader.buscarDonantePorId("D001");
        assertNotNull(encontrado, "Debe encontrar el donante");
        assertEquals("Laura Fernández", encontrado.getName(), "Debe tener el nombre correcto");
    }

    // ========== 4. PRUEBAS AUTOMÁTICAS PARA EL MANEJO DE EXCEPCIONES ==========
    
    /**
     * Verifica que intentar agregar un donante con ID duplicado
     * lance una InvalidDataException.
     */
    @Test
    public void testAgregarDonanteDuplicado() {
        DonanteLoader.agregarDonante(donantePrueba);
        
        assertThrows(InvalidDataException.class, () -> {
            DonanteLoader.agregarDonante(donantePrueba);
        }, "Debe lanzar excepción al agregar donante con ID duplicado");
    }

    /**
     * Verifica que intentar eliminar un donante inexistente
     * lance una NotFoundException.
     */
    @Test
    public void testEliminarDonanteInexistente() {
        assertThrows(NotFoundException.class, () -> {
            DonanteLoader.eliminarDonante("D999");
        }, "Debe lanzar excepción al eliminar donante inexistente");
    }

    /**
     * Verifica que intentar buscar un donante inexistente
     * lance una NotFoundException.
     */
    @Test
    public void testBuscarDonanteInexistente() {
        assertThrows(NotFoundException.class, () -> {
            DonanteLoader.buscarDonantePorId("D999");
        }, "Debe lanzar excepción al buscar donante inexistente");
    }

    /**
     * Verifica que si el archivo no existe, se retorne una lista vacía.
     */
    @Test
    public void testCargarDonantesArchivoNoExiste() {
        File archivo = new File("DonanteInexistente.txt");
        if (archivo.exists()) {
            archivo.delete();
        }
        
        ArrayList<Donante> donantes = DonanteLoader.cargarDonantes();
        assertNotNull(donantes, "Debe retornar una lista vacía si el archivo no existe");
        assertTrue(donantes.isEmpty(), "La lista debe estar vacía");
    }

    /**
     * Verifica que líneas con formato inválido sean ignoradas
     * sin lanzar excepciones.
     */
    @Test
    public void testCargarDonanteLineaInvalida() {
        // Guardar una línea con formato inválido
        try {
            File archivo = new File("Donante.txt");
            java.io.PrintWriter pw = new java.io.PrintWriter(archivo);
            pw.println("Dato1;Dato2;Dato3"); // Solo 3 campos, se requieren 10
            pw.close();
            
            ArrayList<Donante> donantes = DonanteLoader.cargarDonantes();
            // Debe ignorar la línea inválida y retornar lista vacía
            assertTrue(donantes.isEmpty(), "Debe ignorar líneas con formato inválido");
        } catch (Exception e) {
            fail("No debe lanzar excepción al cargar líneas inválidas");
        }
    }

    /**
     * Verifica que líneas con edad inválida sean ignoradas
     * sin lanzar excepciones.
     */
    @Test
    public void testCargarDonanteEdadInvalida() {
        try {
            File archivo = new File("Donante.txt");
            java.io.PrintWriter pw = new java.io.PrintWriter(archivo);
            pw.println("Test;EDAD_INVALIDA;D003;O+;Calle;123;Sangre;Bueno;1;");
            pw.close();
            
            ArrayList<Donante> donantes = DonanteLoader.cargarDonantes();
            // Debe ignorar la línea con edad inválida
            assertTrue(donantes.isEmpty(), "Debe ignorar líneas con edad inválida");
        } catch (Exception e) {
            fail("No debe lanzar excepción al cargar líneas con edad inválida");
        }
    }

    /**
     * Verifica que se pueda guardar una lista vacía sin errores.
     */
    @Test
    public void testGuardarListaVacia() {
        assertDoesNotThrow(() -> {
            DonanteLoader.guardarDonantes(new ArrayList<>());
        }, "Debe poder guardar lista vacía sin errores");
    }

    /**
     * Limpieza después de cada prueba.
     * Elimina los archivos temporales creados.
     */
    @AfterEach
    public void tearDown() {
        // Limpiar archivos de prueba
        File archivo = new File("Donante.txt");
        if (archivo.exists()) {
            archivo.delete();
        }
    }
}