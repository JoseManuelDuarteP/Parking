package org.example;

public class Vehiculo {
    private String matricula;
    private String marca;
    private String modelo;
    private Enum<TipoVehiculo> tipo;

    Vehiculo(String matricula, String marca, String modelo, TipoVehiculo tipo) {
        this.matricula = matricula;
        this.marca = marca;
        this.modelo = modelo;
        this.tipo = tipo;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Enum<TipoVehiculo> getTipo() {
        return tipo;
    }

    public void setTipo(Enum<TipoVehiculo> tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return "Matrícula: " + this.matricula + " - Marca: " +
                this.marca + " - Modelo: " + this.modelo + " - Tipo: " + this.tipo;
    }
}
