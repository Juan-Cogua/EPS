package model.test;

import excepciones.InvalidDataException;
import model.Cita;
import model.Paciente;
import org.junit.Test;

import java.util.Date;
import java.util.ArrayList;

public class CitaTest {

    @Test(expected = InvalidDataException.class)
    public void testConstructorFechaNull() {
        Paciente p = new Paciente("P", (byte)30, "PX", "O+", "C", "300", 70.0, 1.7, new ArrayList<>(), new ArrayList<>());
        new Cita("C1", null, null, "Hospital", p, "Dr");
    }

    @Test
    public void testResumenFormato() {
        Paciente p = new Paciente("P2", (byte)30, "PX2", "A+", "C", "301", 65.0, 1.6, new ArrayList<>(), new ArrayList<>());
        Cita c = new Cita("C2", new Date(), new Date(), "Clinica", p, "Dra");
        String resumen = c.resumen();
        assert resumen != null && resumen.contains(";");
    }
}
