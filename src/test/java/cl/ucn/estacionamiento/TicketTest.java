package cl.ucn.estacionamiento;

import cl.ucn.estacionamiento.modelo.EstadoTicket;
import cl.ucn.estacionamiento.modelo.Ticket;
import cl.ucn.estacionamiento.modelo.TipoVehiculo;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class TicketTest {

    @Test
    void testConstructor() {
        LocalDateTime entrada = LocalDateTime.of(2024, 12, 2, 10, 0);
        Ticket ticket = new Ticket(1, "ABCD12", TipoVehiculo.AUTO, entrada);

        assertEquals(1, ticket.getId());
        assertEquals("ABCD12", ticket.getPatente());
        assertEquals(TipoVehiculo.AUTO, ticket.getTipoVehiculo());
        assertEquals(entrada, ticket.getFechaHoraEntrada());
        assertEquals(EstadoTicket.ABIERTO, ticket.getEstado());
        assertNull(ticket.getFechaHoraSalida());
        assertEquals(0, ticket.getMontoCobrado());
    }

    @Test
    void testEstaAbiertoNuevo() {
        Ticket ticket = new Ticket(1, "ABCD12", TipoVehiculo.AUTO, LocalDateTime.now());
        assertTrue(ticket.estaAbierto());
    }

    @Test
    void testEstaAbiertoCerrado() {
        Ticket ticket = new Ticket(1, "ABCD12", TipoVehiculo.AUTO, LocalDateTime.now());
        ticket.setEstado(EstadoTicket.CERRADO);
        assertFalse(ticket.estaAbierto());
    }

    @Test
    void testSetFechaSalida() {
        Ticket ticket = new Ticket(1, "ABCD12", TipoVehiculo.AUTO, LocalDateTime.now());
        LocalDateTime salida = LocalDateTime.now().plusHours(1);
        ticket.setFechaHoraSalida(salida);
        assertEquals(salida, ticket.getFechaHoraSalida());
    }

    @Test
    void testSetMonto() {
        Ticket ticket = new Ticket(1, "ABCD12", TipoVehiculo.AUTO, LocalDateTime.now());
        ticket.setMontoCobrado(1500);
        assertEquals(1500, ticket.getMontoCobrado());
    }
}
