package org.example;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Date;

public class Estancia {
    private LocalDate fechaEntrada = LocalDate.now();
    private LocalDate fechaSalida;
    private Duration duracion;
    private Vehiculo vehiculo;
    private double importe;

    public Estancia(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    public Duration getDuracion() {
        return duracion;
    }

    public void setDuracion(Duration duracion) {
        this.duracion = duracion;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    public LocalDate getFechaEntrada() {
        return fechaEntrada;
    }

    public void setFechaEntrada(LocalDate fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }

    public LocalDate getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(LocalDate fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public double getImporte() {
        return importe;
    }

    public void setImporte(double importe) {
        this.importe = importe;
    }

    @Override
    public String toString() {
        return "Duración: " + duracion + " - Vehiculo: " + vehiculo.getMatricula();
    }

    public double cobro() {
        int duracion = (int) Duration.between(fechaEntrada, fechaSalida).toMinutes();
        double cobro = 0;

        if (this.vehiculo.getTipo() == TipoVehiculo.OFICIAL) {
            this.importe = cobro;
            return cobro;

        } else if (this.vehiculo.getTipo() == TipoVehiculo.RESIDENTE) {
            cobro = duracion * TipoVehiculo.RESIDENTE.getTarifa();
            this.importe = cobro;
            return cobro;

        } else {
            cobro = duracion * TipoVehiculo.NO_RESIDENTE.getTarifa();
            this.importe = cobro;
            return cobro;
        }
    }
}
