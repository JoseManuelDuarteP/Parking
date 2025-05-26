package org.example;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Estancia {
    private LocalDateTime fechaEntrada = LocalDateTime.now();
    private LocalDateTime fechaSalida;
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

    public LocalDateTime getFechaEntrada() {
        return fechaEntrada;
    }

    public void setFechaEntrada(LocalDateTime fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }

    public LocalDateTime getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(LocalDateTime fechaSalida) {
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
            this.duracion = Duration.between(fechaEntrada, fechaSalida);
            return cobro;

        } else if (this.vehiculo.getTipo() == TipoVehiculo.RESIDENTE) {
            cobro = duracion * TipoVehiculo.RESIDENTE.getTarifa();
            this.importe += cobro;
            this.duracion = Duration.between(fechaEntrada, fechaSalida);
            return cobro;

        } else {
            cobro = duracion * TipoVehiculo.NO_RESIDENTE.getTarifa();
            this.importe += cobro;
            System.out.println("Importe: " + cobro);
            return cobro;
        }
    }
}
