package model.test;

import excepciones.InvalidDataException;
import excepciones.InvariantViolationException;
import model.Persona;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests básicos para Persona: invariantes y manejo de excepciones del constructor.
 */
public class PersonaTest {

	@Test
	public void testPersonaValidaCheckInvariant() {
		Persona p = new Persona("Juan", (byte)30, "ID123", "O+", "Calle 1", "3001234567");
		// No debería lanzar
		p.checkInvariant();
		assertTrue(p.esMayorDeEdad());
	}

	@Test
	public void testConstructorNombreInvalido() {
		assertThrows(InvalidDataException.class, () -> {
			new Persona("", (byte)25, "ID124", "A+", "Calle", "300");
		});
	}

	@Test
	public void testCheckInvariantRompeDespuesDeModificar() {
		Persona p = new Persona("Ana", (byte)20, "ID125", "B+", "Calle 2", "301");
		// Rompemos la invariante
		p.setId("");
		assertThrows(InvariantViolationException.class, () -> {
			p.checkInvariant();
		});
	}
}
