package IU.testIU;

import IU.PanelTrasplante;
import excepciones.FechaInvalidaException;
import excepciones.SangreIncompatibleException;
import excepciones.TrasplanteInvalidoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests adicionales para PanelTrasplante que buscan exponer problemas en el manejo de excepciones
 * al intentar agregar trasplantes (fechas, selección y compatibilidad sanguínea).
 */
public class PanelTrasplanteExtraTest {

  private PanelTrasplante panel;

  @BeforeEach
  public void setup() {
    // Construir el panel (usa los loaders internos; las listas se reemplazarán en cada test)
    panel = new PanelTrasplante();
    // Evitar dependencias visuales en el entorno de test
    panel.getAreaTrasplantes().setText("");
  }

  @Test
  public void testAgregarTrasplanteSinSeleccionLanzaTrasplanteInvalido() {
    panel.getTxtIdTrasplante().setText("TX100");
    // Asegurarse de que no haya selección
    panel.getListaDonantes().clearSelection();
    panel.getListaPacientes().clearSelection();

    assertThrows(TrasplanteInvalidoException.class, () -> {
      panel.agregarTrasplante();
    }, "Debe lanzar TrasplanteInvalidoException cuando no hay donante/paciente seleccionado");
  }

  @Test
  public void testAgregarTrasplanteFechaInvalidaLanzaFechaInvalidaException() {
    // Proveer etiquetas mínimas para evitar la excepción de selección
    String donorLabel = "Donante X (ID: D1) | Sangre: O+";
    String patientLabel = "Paciente Y (ID: P1) | Sangre: A+";
    JList<String> donList = panel.getListaDonantes();
    JList<String> patList = panel.getListaPacientes();

    donList.setListData(new String[]{donorLabel});
    patList.setListData(new String[]{patientLabel});
    donList.setSelectedIndex(0);
    patList.setSelectedIndex(0);

    panel.getTxtIdTrasplante().setText("TX101");
    panel.getTxtFecha().setText("31-31-2020"); // formato inválido

    assertThrows(FechaInvalidaException.class, () -> {
      panel.agregarTrasplante();
    }, "Debe lanzar FechaInvalidaException para formato de fecha inválido");
  }

  @Test
  public void testAgregarTrasplanteSangreIncompatibleLanzaSangreIncompatibleException() {
    // Donante A -> Receptor B (incompatibles según reglas ABO implementadas)
    String donorLabel = "Donante A (ID: D2) | Sangre: A+";
    String patientLabel = "Paciente B (ID: P2) | Sangre: B+";
    JList<String> donList = panel.getListaDonantes();
    JList<String> patList = panel.getListaPacientes();

    donList.setListData(new String[]{donorLabel});
    patList.setListData(new String[]{patientLabel});
    donList.setSelectedIndex(0);
    patList.setSelectedIndex(0);

    panel.getTxtIdTrasplante().setText("TX102");
    // Fecha válida para no interferir con la validación de sangre
    panel.getTxtFecha().setText(new java.text.SimpleDateFormat("dd/MM/yyyy").format(new Date()));

    assertThrows(SangreIncompatibleException.class, () -> {
      panel.agregarTrasplante();
    }, "Debe lanzar SangreIncompatibleException cuando la sangre es incompatible");
  }

  @Test
  public void testAgregarTrasplanteCompatibleNoLanzaAntesDeBuscar() {
    // Donante O -> Receptor AB (compatible). Esta prueba asegura que la comprobación de
    // compatibilidad ocurre y no es silenciada antes de verificar loaders.
    String donorLabel = "Donante O (ID: D3) | Sangre: O+";
    String patientLabel = "Paciente AB (ID: P3) | Sangre: AB+";
    panel.getListaDonantes().setListData(new String[]{donorLabel});
    panel.getListaPacientes().setListData(new String[]{patientLabel});
    panel.getListaDonantes().setSelectedIndex(0);
    panel.getListaPacientes().setSelectedIndex(0);

    panel.getTxtIdTrasplante().setText("TX103");
    panel.getTxtFecha().setText(new java.text.SimpleDateFormat("dd/MM/yyyy").format(new Date()));

    // No se espera una excepción de compatibilidad ni de formato aquí.
    // Dependencias posteriores (buscarPaciente/buscarDonante) pueden provocar manejo propio;
    // solo se valida que las validaciones previas no lancen excepciones inesperadas.
    assertDoesNotThrow(() -> panel.agregarTrasplante(), "En caso de sangre compatible no debe lanzarse SangreIncompatibleException ni FechaInvalidaException");
  }
}

  /** Donante utilizado como parte del trasplante de prueba. */
  private Donante donante;

  /** Paciente receptor del trasplante de prueba. */
  private Paciente paciente;

  /**

  * Configura los datos iniciales necesarios para cada prueba.
  * Se crean instancias válidas de {@link Donante}, {@link Paciente} y {@link Trasplante}.
    */
    @BeforeEach
    public void setUp() {
    // Crear donante de prueba
    donante = new Donante(
    "Juan Donante", (byte)30, "D001", "O+", "Calle 123",
    "12345678", "Órganos", "Saludable", true, "Riñón"
    );

    // Crear paciente de prueba
    paciente = new Paciente(
    "Ana Paciente", (byte)25, "P001", "AB+", "Calle 456",
    "87654321", 70.0, 1.65, new ArrayList<>(), new ArrayList<>()
    );

    // Crear trasplante de prueba
    trasplantePrueba = new Trasplante(
    "T001", "Riñón", donante, paciente,
    "Pendiente", "", "", new Date()
    );
    }

  // =====================================================
  // 1. PRUEBAS DE INVARIANTES DE LA CLASE
  // =====================================================

  /**

  * Verifica que las invariantes básicas de {@link Trasplante} se cumplan.
  * Ningún campo esencial debe ser nulo.
    */
    @Test
    public void testInvariantesTrasplante() {
    assertNotNull(trasplantePrueba.getId(), "El ID no debe ser nulo");
    assertNotNull(trasplantePrueba.getOrganType(), "El tipo de órgano no debe ser nulo");
    assertNotNull(trasplantePrueba.getDonor(), "El donante no debe ser nulo");
    assertNotNull(trasplantePrueba.getReceiver(), "El receptor no debe ser nulo");
    assertNotNull(trasplantePrueba.getEstado(), "El estado no debe ser nulo");
    assertNotNull(trasplantePrueba.getFecha(), "La fecha no debe ser nula");
    }

  /**

  * Verifica la correcta relación entre los objetos del trasplante.
  * Se comprueba la coherencia de los identificadores y del tipo de órgano.
    */
    @Test
    public void testInvariantesRelaciones() {
    assertEquals("D001", trasplantePrueba.getDonor().getId(), "El donante debe tener el ID correcto");
    assertEquals("P001", trasplantePrueba.getReceiver().getId(), "El receptor debe tener el ID correcto");
    assertEquals("Riñón", trasplantePrueba.getOrganType(), "El órgano debe ser Riñón");
    }

  // =====================================================
  // 2. MÉTODOS DE VERIFICACIÓN
  // =====================================================

  /**

  * Prueba la serialización de un {@link Trasplante} a formato de archivo.
  * Verifica que los campos clave estén presentes en la cadena generada.
    */
    @Test
    public void testSerializacionTrasplante() {
    String serializado = TrasplanteLoader.toArchivo(trasplantePrueba);
    assertNotNull(serializado, "La serialización no debe ser nula");
    assertTrue(serializado.contains("T001"), "Debe contener el ID del trasplante");
    assertTrue(serializado.contains("Juan Donante"), "Debe contener el nombre del donante");
    assertTrue(serializado.contains("Ana Paciente"), "Debe contener el nombre del paciente");
    assertTrue(serializado.contains("Riñón"), "Debe contener el tipo de órgano");
    }

  /**

  * Prueba la deserialización de una línea de archivo a un objeto {@link Trasplante}.
  * Verifica que los datos deserializados se mantengan consistentes.
    */
    @Test
    public void testDeserializacionTrasplante() {
    String linea = TrasplanteLoader.toArchivo(trasplantePrueba);
    Trasplante deserializado = TrasplanteLoader.fromArchivo(linea);

    assertNotNull(deserializado, "El trasplante deserializado no debe ser nulo");
    assertEquals(trasplantePrueba.getId(), deserializado.getId(), "El ID debe mantenerse");
    assertEquals(trasplantePrueba.getOrganType(), deserializado.getOrganType(), "El órgano debe mantenerse");
    assertEquals(trasplantePrueba.getEstado(), deserializado.getEstado(), "El estado debe mantenerse");
    }

  /**

  * Verifica la correcta representación textual del trasplante.
  * Comprueba que {@link Trasplante#toString()} incluya los datos relevantes.
    */
    @Test
    public void testToString() {
    String resultado = trasplantePrueba.toString();
    assertNotNull(resultado, "toString no debe retornar nulo");
    assertTrue(resultado.contains("T001"), "Debe contener el ID");
    assertTrue(resultado.contains("Riñón"), "Debe contener el órgano");
    assertTrue(resultado.contains("Juan Donante"), "Debe contener el nombre del donante");
    assertTrue(resultado.contains("Ana Paciente"), "Debe contener el nombre del paciente");
    }

  // =====================================================
  // 3. PRUEBAS AUTOMÁTICAS DE FUNCIONALIDAD
  // =====================================================

  /**

  * Prueba la persistencia completa de trasplantes:
  * guardar en archivo y luego cargar desde disco.
    */
    @Test
    public void testGuardarYCargarTrasplantes() {
    List<Trasplante> trasplantes = new ArrayList<>();
    trasplantes.add(trasplantePrueba);

    TrasplanteLoader.guardarTrasplantes(trasplantes);
    List<Trasplante> cargados = TrasplanteLoader.cargarTrasplantes();

    assertFalse(cargados.isEmpty(), "La lista cargada no debe estar vacía");

    Trasplante cargado = cargados.stream()
    .filter(t -> t.getId().equals("T001"))
    .findFirst()
    .orElse(null);

    assertNotNull(cargado, "Debe encontrar el trasplante guardado");
    assertEquals(trasplantePrueba.getId(), cargado.getId(), "El ID debe mantenerse");
    assertEquals(trasplantePrueba.getEstado(), cargado.getEstado(), "El estado debe mantenerse");
    }

  /**

  * Verifica los cambios en el estado del trasplante mediante el método setEstado.
    */
    @Test
    public void testCambiarEstado() {
    trasplantePrueba.setEstado("Aprobado");
    assertEquals("Aprobado", trasplantePrueba.getEstado(), "El estado debe cambiar a Aprobado");

    trasplantePrueba.setEstado("Rechazado");
    assertEquals("Rechazado", trasplantePrueba.getEstado(), "El estado debe cambiar a Rechazado");
    }

  /**

  * Prueba la modificación del identificador del trasplante.
    */
    @Test
    public void testCambiarId() {
    trasplantePrueba.setId("T002");
    assertEquals("T002", trasplantePrueba.getId(), "El ID debe actualizarse");
    }

  // =====================================================
  // 4. PRUEBAS DE MANEJO DE EXCEPCIONES
  // =====================================================

  /** Verifica que no se permita un ID nulo en la creación de un trasplante. */
  @Test
  public void testIdNulo() {
  assertThrows(Exception.class, () -> {
  new Trasplante(null, "Riñón", donante, paciente, "Pendiente", "", "", new Date());
  }, "Debe lanzar excepción con ID nulo");
  }

  /** Verifica que no se permita un ID vacío en la creación de un trasplante. */
  @Test
  public void testIdVacio() {
  assertThrows(Exception.class, () -> {
  new Trasplante("", "Riñón", donante, paciente, "Pendiente", "", "", new Date());
  }, "Debe lanzar excepción con ID vacío");
  }

  /** Verifica que el tipo de órgano no pueda ser vacío. */
  @Test
  public void testOrganoVacio() {
  assertThrows(Exception.class, () -> {
  new Trasplante("T999", "", donante, paciente, "Pendiente", "", "", new Date());
  }, "Debe lanzar excepción con órgano vacío");
  }

  /** Verifica que no se permita un donante nulo. */
  @Test
  public void testDonanteNulo() {
  assertThrows(Exception.class, () -> {
  new Trasplante("T999", "Riñón", null, paciente, "Pendiente", "", "", new Date());
  }, "Debe lanzar excepción con donante nulo");
  }

  /** Verifica que no se permita un receptor nulo. */
  @Test
  public void testReceptorNulo() {
  assertThrows(Exception.class, () -> {
  new Trasplante("T999", "Riñón", donante, null, "Pendiente", "", "", new Date());
  }, "Debe lanzar excepción con receptor nulo");
  }

  /** Prueba la deserialización con línea vacía o nula. */
  @Test
  public void testFromArchivoLineaVacia() {
  assertNull(TrasplanteLoader.fromArchivo(""), "Línea vacía debe retornar null");
  assertNull(TrasplanteLoader.fromArchivo(null), "Línea nula debe retornar null");
  }

  /** Prueba la deserialización con formato de línea inválido. */
  @Test
  public void testFromArchivoFormatoInvalido() {
  assertNull(TrasplanteLoader.fromArchivo("Formato|Invalido|Sin|Campos|Correctos"),
  "Formato inválido debe retornar null");
  }

  /** Verifica que un trasplante nulo devuelva cadena vacía al serializarse. */
  @Test
  public void testToArchivoTrasplanteNulo() {
  assertEquals("", TrasplanteLoader.toArchivo(null), "Trasplante nulo debe retornar cadena vacía");
  }

  /** Prueba la verificación de invariantes en un trasplante válido. */
  @Test
  public void testCheckInvariant() {
  assertDoesNotThrow(() -> {
  trasplantePrueba.checkInvariant();
  }, "Las invariantes deben cumplirse en un trasplante válido");
  }

  /** Verifica que el método checkInvariant lance excepción si la fecha es nula. */
  @Test
  public void testCheckInvariantFechaNula() {
  Trasplante trasplanteInvalido = new Trasplante(
  "T998", "Riñón", donante, paciente, "Pendiente", "", "", null
  );


   assertThrows(Exception.class, () -> {
       trasplanteInvalido.checkInvariant();
   }, "Debe lanzar excepción con fecha nula");

  }
  /**
  * Limpia los archivos temporales generados durante las pruebas.
    */
    @AfterEach
    public void tearDown() {
    File archivo = new File(RUTA_TEST);
    if (archivo.exists()) {
    archivo.delete();
    }
    }
    }
