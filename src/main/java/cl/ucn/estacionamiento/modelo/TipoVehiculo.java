package cl.ucn.estacionamiento.modelo;

public enum TipoVehiculo {
    AUTO(800),
    MOTO(500),
    CAMIONETA(1000);

    private final int tarifaPorBloque;

    TipoVehiculo(int tarifaPorBloque) {
        this.tarifaPorBloque = tarifaPorBloque;
    }

    public int getTarifaPorBloque() {
        return tarifaPorBloque;
    }
}

