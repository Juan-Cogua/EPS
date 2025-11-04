package model.test;

import excepciones.DonanteMenorEdadException;
import excepciones.InvariantViolationException;
import model.Donante;
import org.junit.Test;

/**
 * Pruebas para Donante: constructor y checkInvariant.
 */
public class DonanteTest {

    @Test(expected = DonanteMenorEdadException.class)
    public void testDonanteMenorEdadLanzaExcepcion() {
        new Donante("Miguel", (byte)16, "D001", "O+", "Calle", "300", "Sangre", "Bueno", true, "Riñón");
    }

    @Test(expected = InvariantViolationException.class)
    public void testCheckInvariantOrganoInvalido() {
        Donante d = new Donante("Luisa", (byte)30, "D002", "A+", "Calle", "301", "Órganos", "Bueno", true, "ÓrganoX");
        d.checkInvariant();
    }
}
