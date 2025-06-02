package org.example;

import java.util.ArrayList;
import java.util.List;

public class Vehiculo {
    private String matricula;
    private TipoVehiculo tipo;
    private List<Estancia> estancias = new ArrayList<>();

    Vehiculo(String matricula,TipoVehiculo tipo) {
        this.matricula = matricula;
        this.tipo = tipo;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public TipoVehiculo getTipo() {
        return tipo;
    }

    public void setTipo(TipoVehiculo tipo) {
        this.tipo = tipo;
    }

    public List<Estancia> getEstancias() {
        return estancias;
    }

    public void addEstancia(Estancia estancia) {
        estancias.add(estancia);
    }

    @Override
    public String toString() {
        return "Matrícula: " + this.matricula + " - Tipo: " + this.tipo;
    }

    public void limpiarEstancias() {
        estancias.clear();
    }

    //Tiempo total de aparcamiento de un Vehiculo
    public int tiempoTotal() {
        int tiempo = 0;

        for (Estancia estancia : estancias) {
            tiempo += estancia.getDuracion();
        }

        return tiempo;
    }

    //Pago total que debe realizar un vehículo, basado en el tiempo total de sus estancias * tarifa del tipo
    public double pagoTotal() {
        int tiempo = 0;

        for (Estancia estancia : estancias) {
            tiempo += estancia.getDuracion();
        }

        return tiempo * this.getTipo().getTarifa();
    }
}
