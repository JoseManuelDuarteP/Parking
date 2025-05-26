package org.example;

import java.util.ArrayList;
import java.util.List;

public class Vehiculo {
    private String matricula;
    private Enum<TipoVehiculo> tipo;
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

    public Enum<TipoVehiculo> getTipo() {
        return tipo;
    }

    public void setTipo(Enum<TipoVehiculo> tipo) {
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
}
