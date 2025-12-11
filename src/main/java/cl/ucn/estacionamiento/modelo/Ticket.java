package cl.ucn.estacionamiento.modelo;

import java.time.LocalDateTime;

public class Ticket {
    private final int id;
    private final String patente;
    private final TipoVehiculo tipoVehiculo;
    private final LocalDateTime fechaHoraEntrada;
    private LocalDateTime fechaHoraSalida;
    private int montoCobrado;
    private EstadoTicket estado;

    public Ticket(int id, String patente, TipoVehiculo tipoVehiculo, LocalDateTime fechaHoraEntrada) {
        this.id = id;
        this.patente = patente;
        this.tipoVehiculo = tipoVehiculo;
        this.fechaHoraEntrada = fechaHoraEntrada;
        this.estado = EstadoTicket.ABIERTO;
    }

    public int getId() {
        return id;
    }

    public String getPatente() {
        return patente;
    }

    public TipoVehiculo getTipoVehiculo() {
        return tipoVehiculo;
    }

    public LocalDateTime getFechaHoraEntrada() {
        return fechaHoraEntrada;
    }

    public LocalDateTime getFechaHoraSalida() {
        return fechaHoraSalida;
    }

    public void setFechaHoraSalida(LocalDateTime fechaHoraSalida) {
        this.fechaHoraSalida = fechaHoraSalida;
    }

    public int getMontoCobrado() {
        return montoCobrado;
    }

    public void setMontoCobrado(int montoCobrado) {
        this.montoCobrado = montoCobrado;
    }

    public EstadoTicket getEstado() {
        return estado;
    }

    public void setEstado(EstadoTicket estado) {
        this.estado = estado;
    }

    public boolean estaAbierto() {
        return estado == EstadoTicket.ABIERTO;
    }
}

