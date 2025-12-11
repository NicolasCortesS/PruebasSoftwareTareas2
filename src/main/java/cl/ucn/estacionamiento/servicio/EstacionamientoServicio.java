package cl.ucn.estacionamiento.servicio;

import cl.ucn.estacionamiento.modelo.EstadoTicket;
import cl.ucn.estacionamiento.modelo.Ticket;
import cl.ucn.estacionamiento.modelo.TipoVehiculo;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EstacionamientoServicio {
    private final List<Ticket> tickets;
    private final CalculadoraTarifa calculadora;
    private int contadorId;

    public EstacionamientoServicio() {
        this.tickets = new ArrayList<>();
        this.calculadora = new CalculadoraTarifa();
        this.contadorId = 1;
    }

    public Ticket registrarEntrada(String patente, TipoVehiculo tipo) {
        return registrarEntrada(patente, tipo, LocalDateTime.now());
    }

    public Ticket registrarEntrada(String patente, TipoVehiculo tipo, LocalDateTime fechaEntrada) {
        Ticket ticket = new Ticket(contadorId++, patente, tipo, fechaEntrada);
        tickets.add(ticket);
        return ticket;
    }

    public int registrarSalida(int idTicket) {
        return registrarSalida(idTicket, LocalDateTime.now());
    }

    public int registrarSalida(int idTicket, LocalDateTime fechaSalida) {
        Ticket ticket = buscarTicket(idTicket);
        
        if (ticket == null || !ticket.estaAbierto()) {
            return -1;
        }

        int monto = calculadora.calcularMonto(ticket.getFechaHoraEntrada(), fechaSalida, ticket.getTipoVehiculo());
        
        if (monto < 0) {
            return -1;
        }

        ticket.setFechaHoraSalida(fechaSalida);
        ticket.setMontoCobrado(monto);
        ticket.setEstado(EstadoTicket.CERRADO);
        
        return monto;
    }

    public Ticket buscarTicket(int id) {
        for (Ticket t : tickets) {
            if (t.getId() == id) {
                return t;
            }
        }
        return null;
    }

    public List<Ticket> listarTicketsAbiertos() {
        List<Ticket> abiertos = new ArrayList<>();
        for (Ticket t : tickets) {
            if (t.estaAbierto()) {
                abiertos.add(t);
            }
        }
        return abiertos;
    }

    public List<Ticket> listarTicketsCerrados() {
        List<Ticket> cerrados = new ArrayList<>();
        for (Ticket t : tickets) {
            if (!t.estaAbierto()) {
                cerrados.add(t);
            }
        }
        return cerrados;
    }

    public int totalRecaudadoHoy() {
        LocalDate hoy = LocalDate.now();
        int total = 0;
        for (Ticket t : tickets) {
            if (!t.estaAbierto() && t.getFechaHoraSalida().toLocalDate().equals(hoy)) {
                total += t.getMontoCobrado();
            }
        }
        return total;
    }

    public int totalRecaudadoEnFecha(LocalDate fecha) {
        int total = 0;
        for (Ticket t : tickets) {
            if (!t.estaAbierto() && t.getFechaHoraSalida().toLocalDate().equals(fecha)) {
                total += t.getMontoCobrado();
            }
        }
        return total;
    }
}

