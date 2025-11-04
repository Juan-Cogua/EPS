package model.test;

import excepciones.InvalidDataException;
import model.Donante;
import model.Paciente;
import model.Trasplante;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

public class TrasplanteTest {

    @Test(expected = InvalidDataException.class)
    public void testConstructorDonanteNull() {
        Paciente receiver = new Paciente("R", (byte)40, "R1", "O+", "C", "300", 70.0, 1.7, new ArrayList<>(), new ArrayList<>());
        new Trasplante("T1", "Riñón", null, receiver, "Pendiente", "", "", new Date());
    }

    @Test
    public void testCheckInvariantValido() {
        Donante donor = new Donante("D", (byte)30, "D1", "O+", "C", "300", "Órganos", "Bueno", true, "Riñón");
        Paciente receiver = new Paciente("R2", (byte)45, "R2", "A+", "C", "301", 80.0, 1.8, new ArrayList<>(), new ArrayList<>());
        Trasplante t = new Trasplante("T2", "Riñón", donor, receiver, "Aprobado", "Historial", "", new Date());
        t.checkInvariant();
    }
}
