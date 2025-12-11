package cl.ucn.estacionamiento;

import cl.ucn.estacionamiento.modelo.EstadoTicket;
import cl.ucn.estacionamiento.modelo.Ticket;
import cl.ucn.estacionamiento.modelo.TipoVehiculo;
import cl.ucn.estacionamiento.servicio.EstacionamientoServicio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class EstacionamientoServicioTest {
    private EstacionamientoServicio servicio;

    @BeforeEach
    void setUp() {
        servicio = new EstacionamientoServicio();
    }

    @Test
    void testEntradaCreaTicket() {
        LocalDateTime entrada = LocalDateTime.of(2024, 12, 2, 10, 0);
        Ticket ticket = servicio.registrarEntrada("ABCD12", TipoVehiculo.AUTO, entrada);

        assertNotNull(ticket);
        assertEquals(1, ticket.getId());
        assertEquals("ABCD12", ticket.getPatente());
        assertEquals(TipoVehiculo.AUTO, ticket.getTipoVehiculo());
        assertEquals(entrada, ticket.getFechaHoraEntrada());
        assertEquals(EstadoTicket.ABIERTO, ticket.getEstado());
    }

    @Test
    void testEntradaIncrementaId() {
        LocalDateTime entrada = LocalDateTime.of(2024, 12, 2, 10, 0);
        Ticket t1 = servicio.registrarEntrada("ABCD12", TipoVehiculo.AUTO, entrada);
        Ticket t2 = servicio.registrarEntrada("EFGH34", TipoVehiculo.MOTO, entrada);

        assertEquals(1, t1.getId());
        assertEquals(2, t2.getId());
    }

    @Test
    void testSalidaRetornaMonto() {
        LocalDateTime entrada = LocalDateTime.of(2024, 12, 2, 10, 0);
        LocalDateTime salida = LocalDateTime.of(2024, 12, 2, 10, 30);
        Ticket ticket = servicio.registrarEntrada("ABCD12", TipoVehiculo.AUTO, entrada);

        int monto = servicio.registrarSalida(ticket.getId(), salida);

        assertEquals(800, monto);
    }

    @Test
    void testSalidaCierraTicket() {
        LocalDateTime entrada = LocalDateTime.of(2024, 12, 2, 10, 0);
        LocalDateTime salida = LocalDateTime.of(2024, 12, 2, 10, 30);
        Ticket ticket = servicio.registrarEntrada("ABCD12", TipoVehiculo.AUTO, entrada);
        servicio.registrarSalida(ticket.getId(), salida);

        assertEquals(EstadoTicket.CERRADO, ticket.getEstado());
        assertEquals(salida, ticket.getFechaHoraSalida());
        assertEquals(800, ticket.getMontoCobrado());
    }

    @Test
    void testSalidaTicketInexistente() {
        int monto = servicio.registrarSalida(999, LocalDateTime.now());
        assertEquals(-1, monto);
    }

    @Test
    void testSalidaTicketYaCerrado() {
        LocalDateTime entrada = LocalDateTime.of(2024, 12, 2, 10, 0);
        LocalDateTime salida = LocalDateTime.of(2024, 12, 2, 10, 30);
        Ticket ticket = servicio.registrarEntrada("ABCD12", TipoVehiculo.AUTO, entrada);
        servicio.registrarSalida(ticket.getId(), salida);

        int segundoIntento = servicio.registrarSalida(ticket.getId(), salida.plusHours(1));
        assertEquals(-1, segundoIntento);
    }

    @Test
    void testSalidaDuracionInvalida() {
        LocalDateTime entrada = LocalDateTime.of(2024, 12, 2, 10, 0);
        LocalDateTime salidaAnterior = LocalDateTime.of(2024, 12, 2, 9, 0);
        Ticket ticket = servicio.registrarEntrada("ABCD12", TipoVehiculo.AUTO, entrada);

        int monto = servicio.registrarSalida(ticket.getId(), salidaAnterior);
        assertEquals(-1, monto);
    }

    @Test
    void testBuscarTicketExistente() {
        LocalDateTime entrada = LocalDateTime.of(2024, 12, 2, 10, 0);
        Ticket ticket = servicio.registrarEntrada("ABCD12", TipoVehiculo.AUTO, entrada);

        Ticket encontrado = servicio.buscarTicket(ticket.getId());
        assertNotNull(encontrado);
        assertEquals(ticket.getId(), encontrado.getId());
    }

    @Test
    void testBuscarTicketInexistente() {
        Ticket encontrado = servicio.buscarTicket(999);
        assertNull(encontrado);
    }

    @Test
    void testListarAbiertosSinTickets() {
        List<Ticket> abiertos = servicio.listarTicketsAbiertos();
        assertTrue(abiertos.isEmpty());
    }

    @Test
    void testListarAbiertosConTickets() {
        LocalDateTime entrada = LocalDateTime.of(2024, 12, 2, 10, 0);
        servicio.registrarEntrada("ABCD12", TipoVehiculo.AUTO, entrada);
        servicio.registrarEntrada("EFGH34", TipoVehiculo.MOTO, entrada);

        List<Ticket> abiertos = servicio.listarTicketsAbiertos();
        assertEquals(2, abiertos.size());
    }

    @Test
    void testListarAbiertosExcluyeCerrados() {
        LocalDateTime entrada = LocalDateTime.of(2024, 12, 2, 10, 0);
        LocalDateTime salida = LocalDateTime.of(2024, 12, 2, 10, 30);
        Ticket t1 = servicio.registrarEntrada("ABCD12", TipoVehiculo.AUTO, entrada);
        servicio.registrarEntrada("EFGH34", TipoVehiculo.MOTO, entrada);
        servicio.registrarSalida(t1.getId(), salida);

        List<Ticket> abiertos = servicio.listarTicketsAbiertos();
        assertEquals(1, abiertos.size());
        assertEquals("EFGH34", abiertos.get(0).getPatente());
    }

    @Test
    void testListarCerradosSinTickets() {
        List<Ticket> cerrados = servicio.listarTicketsCerrados();
        assertTrue(cerrados.isEmpty());
    }

    @Test
    void testListarCerradosConTickets() {
        LocalDateTime entrada = LocalDateTime.of(2024, 12, 2, 10, 0);
        LocalDateTime salida = LocalDateTime.of(2024, 12, 2, 10, 30);
        Ticket t1 = servicio.registrarEntrada("ABCD12", TipoVehiculo.AUTO, entrada);
        servicio.registrarSalida(t1.getId(), salida);

        List<Ticket> cerrados = servicio.listarTicketsCerrados();
        assertEquals(1, cerrados.size());
    }

    @Test
    void testRecaudacionSinTickets() {
        int total = servicio.totalRecaudadoEnFecha(LocalDate.of(2024, 12, 2));
        assertEquals(0, total);
    }

    @Test
    void testRecaudacionConTickets() {
        LocalDateTime entrada = LocalDateTime.of(2024, 12, 2, 10, 0);
        LocalDateTime salida = LocalDateTime.of(2024, 12, 2, 10, 30);
        
        Ticket t1 = servicio.registrarEntrada("ABCD12", TipoVehiculo.AUTO, entrada);
        Ticket t2 = servicio.registrarEntrada("EFGH34", TipoVehiculo.MOTO, entrada);
        
        servicio.registrarSalida(t1.getId(), salida);
        servicio.registrarSalida(t2.getId(), salida);

        int total = servicio.totalRecaudadoEnFecha(LocalDate.of(2024, 12, 2));
        assertEquals(1300, total);
    }

    @Test
    void testRecaudacionExcluyeOtrasFechas() {
        LocalDateTime entrada1 = LocalDateTime.of(2024, 12, 2, 10, 0);
        LocalDateTime salida1 = LocalDateTime.of(2024, 12, 2, 10, 30);
        LocalDateTime entrada2 = LocalDateTime.of(2024, 12, 3, 10, 0);
        LocalDateTime salida2 = LocalDateTime.of(2024, 12, 3, 10, 30);
        
        Ticket t1 = servicio.registrarEntrada("ABCD12", TipoVehiculo.AUTO, entrada1);
        Ticket t2 = servicio.registrarEntrada("EFGH34", TipoVehiculo.AUTO, entrada2);
        
        servicio.registrarSalida(t1.getId(), salida1);
        servicio.registrarSalida(t2.getId(), salida2);

        int total = servicio.totalRecaudadoEnFecha(LocalDate.of(2024, 12, 2));
        assertEquals(800, total);
    }

    @Test
    void testRecaudacionExcluyeAbiertos() {
        LocalDateTime entrada = LocalDateTime.of(2024, 12, 2, 10, 0);
        LocalDateTime salida = LocalDateTime.of(2024, 12, 2, 10, 30);
        
        Ticket t1 = servicio.registrarEntrada("ABCD12", TipoVehiculo.AUTO, entrada);
        servicio.registrarEntrada("EFGH34", TipoVehiculo.MOTO, entrada);
        
        servicio.registrarSalida(t1.getId(), salida);

        int total = servicio.totalRecaudadoEnFecha(LocalDate.of(2024, 12, 2));
        assertEquals(800, total);
    }
}
