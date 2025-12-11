package cl.ucn.estacionamiento;

import cl.ucn.estacionamiento.modelo.Ticket;
import cl.ucn.estacionamiento.modelo.TipoVehiculo;
import cl.ucn.estacionamiento.servicio.EstacionamientoServicio;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static EstacionamientoServicio servicio = new EstacionamientoServicio();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int opcion;
        do {
            mostrarMenu();
            opcion = leerEntero();
            ejecutarOpcion(opcion);
        } while (opcion != 7);
    }

    private static void mostrarMenu() {
        System.out.println("\n=== ESTACIONAMIENTO ===");
        System.out.println("1. Registrar entrada de vehiculo");
        System.out.println("2. Registrar salida de vehiculo");
        System.out.println("3. Listar tickets abiertos");
        System.out.println("4. Listar tickets cerrados");
        System.out.println("5. Mostrar detalle de un ticket");
        System.out.println("6. Mostrar total recaudado del dia");
        System.out.println("7. Salir");
        System.out.print("Seleccione opcion: ");
    }

    private static void ejecutarOpcion(int opcion) {
        switch (opcion) {
            case 1 -> registrarEntrada();
            case 2 -> registrarSalida();
            case 3 -> listarAbiertos();
            case 4 -> listarCerrados();
            case 5 -> mostrarDetalle();
            case 6 -> mostrarRecaudacion();
            case 7 -> System.out.println("Adios!");
            default -> System.out.println("Opcion no valida");
        }
    }

    private static void registrarEntrada() {
        System.out.print("Ingrese patente: ");
        String patente = scanner.nextLine();
        
        System.out.println("Tipo de vehiculo:");
        System.out.println("1. Auto");
        System.out.println("2. Moto");
        System.out.println("3. Camioneta");
        System.out.print("Seleccione: ");
        int tipo = leerEntero();

        TipoVehiculo tipoVehiculo = switch (tipo) {
            case 1 -> TipoVehiculo.AUTO;
            case 2 -> TipoVehiculo.MOTO;
            case 3 -> TipoVehiculo.CAMIONETA;
            default -> null;
        };

        if (tipoVehiculo == null) {
            System.out.println("Tipo de vehiculo no valido");
            return;
        }

        Ticket ticket = servicio.registrarEntrada(patente, tipoVehiculo);
        System.out.println("Ticket creado con ID: " + ticket.getId());
    }

    private static void registrarSalida() {
        System.out.print("Ingrese ID del ticket: ");
        int id = leerEntero();

        Ticket ticket = servicio.buscarTicket(id);
        if (ticket == null) {
            System.out.println("Error: Ticket no existe.");
            return;
        }
        if (!ticket.estaAbierto()) {
            System.out.println("Error: Ticket ya esta cerrado.");
            return;
        }

        int monto = servicio.registrarSalida(id);
        if (monto < 0) {
            System.out.println("Error: Tiempo de estacionamiento menor a 1 minuto");
        } else {
            System.out.println("Salida registrada. Monto a pagar: $" + monto);
        }
    }

    private static void listarAbiertos() {
        List<Ticket> abiertos = servicio.listarTicketsAbiertos();
        if (abiertos.isEmpty()) {
            System.out.println("No hay tickets abiertos");
            return;
        }
        System.out.println("\n--- Tickets Abiertos ---");
        for (Ticket t : abiertos) {
            System.out.println("ID: " + t.getId() + " | Patente: " + t.getPatente() + 
                             " | Tipo: " + t.getTipoVehiculo() + 
                             " | Entrada: " + t.getFechaHoraEntrada());
        }
    }

    private static void listarCerrados() {
        List<Ticket> cerrados = servicio.listarTicketsCerrados();
        if (cerrados.isEmpty()) {
            System.out.println("No hay tickets cerrados");
            return;
        }
        System.out.println("\n--- Tickets Cerrados ---");
        for (Ticket t : cerrados) {
            System.out.println("ID: " + t.getId() + " | Patente: " + t.getPatente() + 
                             " | Tipo: " + t.getTipoVehiculo() + 
                             " | Monto: $" + t.getMontoCobrado());
        }
    }

    private static void mostrarDetalle() {
        System.out.print("Ingrese ID del ticket: ");
        int id = leerEntero();

        Ticket t = servicio.buscarTicket(id);
        if (t == null) {
            System.out.println("Ticket no encontrado");
            return;
        }

        System.out.println("\n--- Detalle Ticket #" + t.getId() + " ---");
        System.out.println("Patente: " + t.getPatente());
        System.out.println("Tipo: " + t.getTipoVehiculo());
        System.out.println("Entrada: " + t.getFechaHoraEntrada());
        System.out.println("Estado: " + t.getEstado());
        if (t.estaAbierto()) {
            long minutos = ChronoUnit.MINUTES.between(t.getFechaHoraEntrada(), LocalDateTime.now());
            System.out.println("Tiempo estacionado: " + minutos + " minutos (en curso)");
        } else {
            long minutos = ChronoUnit.MINUTES.between(t.getFechaHoraEntrada(), t.getFechaHoraSalida());
            System.out.println("Salida: " + t.getFechaHoraSalida());
            System.out.println("Tiempo estacionado: " + minutos + " minutos");
            System.out.println("Monto cobrado: $" + t.getMontoCobrado());
        }
    }

    private static void mostrarRecaudacion() {
        int total = servicio.totalRecaudadoHoy();
        System.out.println("Total recaudado hoy: $" + total);
    }

    private static int leerEntero() {
        try {
            String linea = scanner.nextLine();
            return Integer.parseInt(linea);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}

