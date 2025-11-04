package model.test;

import excepciones.InvalidDataException;
import excepciones.InvariantViolationException;
import model.Cita;
import model.Paciente;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Date;

public class PacienteTest {

    @Test
    public void testPacienteConstructorPesoNegativo() {
        assertThrows(InvalidDataException.class, () -> {
            new Paciente("P", (byte)20, "P001", "O+", "Calle", "300", -1.0, 1.7, new ArrayList<>(), new ArrayList<>());
        });
    }

    @Test
    public void testCheckInvariantListasNull() {
        Paciente p = new Paciente("P2", (byte)25, "P002", "A+", "Calle", "301", 70.0, 1.8, new ArrayList<>(), new ArrayList<>());
        p.setAllergies(null);
        assertThrows(InvariantViolationException.class, () -> {
            p.checkInvariant();
        });
    }

    @Test
    public void testAgregarCancelarCita() {
        Paciente p = new Paciente("P3", (byte)40, "P003", "B+", "Calle", "302", 80.0, 1.75, new ArrayList<>(), new ArrayList<>());
        Cita c = new Cita("C100", new Date(), new Date(), "Hospital", p, "Dr. X");
        p.agregarCita(c);
        assertTrue(p.getCitas().contains(c));
        p.cancelarCita(c);
    }
}
