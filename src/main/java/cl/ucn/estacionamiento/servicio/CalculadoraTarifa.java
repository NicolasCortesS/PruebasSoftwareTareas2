package cl.ucn.estacionamiento.servicio;

import cl.ucn.estacionamiento.modelo.TipoVehiculo;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class CalculadoraTarifa {
    private static final int TOPE_DIARIO = 15000;
    private static final int MINUTOS_POR_BLOQUE = 30;
    private static final double DESCUENTO_FIN_SEMANA = 0.10;

    public int calcularMonto(LocalDateTime entrada, LocalDateTime salida, TipoVehiculo tipo) {
        long minutos = ChronoUnit.MINUTES.between(entrada, salida);
        
        if (minutos <= 0) {
            return -1;
        }

        int bloques = (int) Math.ceil((double) minutos / MINUTOS_POR_BLOQUE);
        int monto = bloques * tipo.getTarifaPorBloque();

        if (monto > TOPE_DIARIO) {
            monto = TOPE_DIARIO;
        }

        if (esFinDeSemana(entrada)) {
            monto = (int) Math.floor(monto * (1 - DESCUENTO_FIN_SEMANA));
        }

        return monto;
    }

    public int calcularBloques(long minutos) {
        if (minutos <= 0) {
            return 0;
        }
        return (int) Math.ceil((double) minutos / MINUTOS_POR_BLOQUE);
    }

    public boolean esFinDeSemana(LocalDateTime fecha) {
        DayOfWeek dia = fecha.getDayOfWeek();
        return dia == DayOfWeek.SATURDAY || dia == DayOfWeek.SUNDAY;
    }
}

