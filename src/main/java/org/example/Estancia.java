package org.example;

import java.time.Duration;
import java.time.LocalDateTime;

public class Estancia {
    private LocalDateTime fechaEntrada = LocalDateTime.now();
    private LocalDateTime fechaSalida;
    private int duracion;
    private Vehiculo vehiculo;
    private double importe;

    public Estancia(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
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

    public void cobro() {
        int duracion = (int) Duration.between(fechaEntrada, fechaSalida).toMinutes();
        double cobro = duracion * this.getVehiculo().getTipo().getTarifa();

        this.duracion = duracion;
        this.importe = cobro;

        if(this.vehiculo.getTipo().equals(TipoVehiculo.NO_RESIDENTE)) {
            System.out.println("Importe: " + cobro);
        }
    }
}
