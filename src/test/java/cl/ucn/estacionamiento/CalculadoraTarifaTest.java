package cl.ucn.estacionamiento;

import cl.ucn.estacionamiento.modelo.TipoVehiculo;
import cl.ucn.estacionamiento.servicio.CalculadoraTarifa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class CalculadoraTarifaTest {
    private CalculadoraTarifa calculadora;

    @BeforeEach
    void setUp() {
        calculadora = new CalculadoraTarifa();
    }

    @Test
    void testBloques1Min() {
        assertEquals(1, calculadora.calcularBloques(1));
    }

    @Test
    void testBloques30Min() {
        assertEquals(1, calculadora.calcularBloques(30));
    }

    @Test
    void testBloques31Min() {
        assertEquals(2, calculadora.calcularBloques(31));
    }

    @Test
    void testBloques60Min() {
        assertEquals(2, calculadora.calcularBloques(60));
    }

    @Test
    void testBloques91Min() {
        assertEquals(4, calculadora.calcularBloques(91));
    }

    @Test
    void testBloques0Min() {
        assertEquals(0, calculadora.calcularBloques(0));
    }

    @Test
    void testBloquesNegativos() {
        assertEquals(0, calculadora.calcularBloques(-10));
    }

    @Test
    void testMontoAuto30Min() {
        LocalDateTime entrada = LocalDateTime.of(2024, 12, 2, 10, 0);
        LocalDateTime salida = LocalDateTime.of(2024, 12, 2, 10, 30);
        assertEquals(800, calculadora.calcularMonto(entrada, salida, TipoVehiculo.AUTO));
    }

    @Test
    void testMontoMoto30Min() {
        LocalDateTime entrada = LocalDateTime.of(2024, 12, 2, 10, 0);
        LocalDateTime salida = LocalDateTime.of(2024, 12, 2, 10, 30);
        assertEquals(500, calculadora.calcularMonto(entrada, salida, TipoVehiculo.MOTO));
    }

    @Test
    void testMontoCamioneta30Min() {
        LocalDateTime entrada = LocalDateTime.of(2024, 12, 2, 10, 0);
        LocalDateTime salida = LocalDateTime.of(2024, 12, 2, 10, 30);
        assertEquals(1000, calculadora.calcularMonto(entrada, salida, TipoVehiculo.CAMIONETA));
    }

    @Test
    void testMontoAuto60Min() {
        LocalDateTime entrada = LocalDateTime.of(2024, 12, 2, 10, 0);
        LocalDateTime salida = LocalDateTime.of(2024, 12, 2, 11, 0);
        assertEquals(1600, calculadora.calcularMonto(entrada, salida, TipoVehiculo.AUTO));
    }

    @Test
    void testMontoAuto45Min() {
        LocalDateTime entrada = LocalDateTime.of(2024, 12, 2, 10, 0);
        LocalDateTime salida = LocalDateTime.of(2024, 12, 2, 10, 45);
        assertEquals(1600, calculadora.calcularMonto(entrada, salida, TipoVehiculo.AUTO));
    }

    @Test
    void testMontoDuracionCero() {
        LocalDateTime entrada = LocalDateTime.of(2024, 12, 2, 10, 0);
        LocalDateTime salida = LocalDateTime.of(2024, 12, 2, 10, 0);
        assertEquals(-1, calculadora.calcularMonto(entrada, salida, TipoVehiculo.AUTO));
    }

    @Test
    void testMontoDuracionNegativa() {
        LocalDateTime entrada = LocalDateTime.of(2024, 12, 2, 11, 0);
        LocalDateTime salida = LocalDateTime.of(2024, 12, 2, 10, 0);
        assertEquals(-1, calculadora.calcularMonto(entrada, salida, TipoVehiculo.AUTO));
    }

    @Test
    void testMontoTopeDiario() {
        LocalDateTime entrada = LocalDateTime.of(2024, 12, 2, 8, 0);
        LocalDateTime salida = LocalDateTime.of(2024, 12, 2, 23, 0);
        assertEquals(15000, calculadora.calcularMonto(entrada, salida, TipoVehiculo.CAMIONETA));
    }

    @Test
    void testMontoDescuentoSabado() {
        LocalDateTime entrada = LocalDateTime.of(2024, 11, 30, 10, 0);
        LocalDateTime salida = LocalDateTime.of(2024, 11, 30, 10, 30);
        assertEquals(720, calculadora.calcularMonto(entrada, salida, TipoVehiculo.AUTO));
    }

    @Test
    void testMontoDescuentoDomingo() {
        LocalDateTime entrada = LocalDateTime.of(2024, 12, 1, 10, 0);
        LocalDateTime salida = LocalDateTime.of(2024, 12, 1, 10, 30);
        assertEquals(720, calculadora.calcularMonto(entrada, salida, TipoVehiculo.AUTO));
    }

    @Test
    void testMontoDescuentoConTope() {
        LocalDateTime entrada = LocalDateTime.of(2024, 11, 30, 8, 0);
        LocalDateTime salida = LocalDateTime.of(2024, 11, 30, 23, 0);
        assertEquals(13500, calculadora.calcularMonto(entrada, salida, TipoVehiculo.CAMIONETA));
    }

    @Test
    void testFinDeSemanaSabado() {
        LocalDateTime sabado = LocalDateTime.of(2024, 11, 30, 10, 0);
        assertTrue(calculadora.esFinDeSemana(sabado));
    }

    @Test
    void testFinDeSemanaDomingo() {
        LocalDateTime domingo = LocalDateTime.of(2024, 12, 1, 10, 0);
        assertTrue(calculadora.esFinDeSemana(domingo));
    }

    @Test
    void testNoFinDeSemanaLunes() {
        LocalDateTime lunes = LocalDateTime.of(2024, 12, 2, 10, 0);
        assertFalse(calculadora.esFinDeSemana(lunes));
    }

    @Test
    void testNoFinDeSemanaViernes() {
        LocalDateTime viernes = LocalDateTime.of(2024, 11, 29, 10, 0);
        assertFalse(calculadora.esFinDeSemana(viernes));
    }
}
