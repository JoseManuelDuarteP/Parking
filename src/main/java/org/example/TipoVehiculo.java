package org.example;

public enum TipoVehiculo {
    OFICIAL(0),
    RESIDENTE(0.002),
    NO_RESIDENTE(0.02);

    private double tarifa;

    TipoVehiculo(double tarifa) {
        this.tarifa = tarifa;
    }

    public double getTarifa() {
        return tarifa;
    }

    public void setTarifa(double tarifa) {
        this.tarifa = tarifa;
    }
}
