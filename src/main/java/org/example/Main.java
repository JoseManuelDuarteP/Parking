package org.example;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    static List<Vehiculo> vehiculos = new ArrayList<>();
    static List<Estancia> estanciasActuales = new ArrayList<>();

    public static void main(String[] args) {
        Vehiculo v = new Vehiculo("ZZZ", TipoVehiculo.OFICIAL);
        vehiculos.add(v);
        v = new Vehiculo("AAA", TipoVehiculo.NO_RESIDENTE);
        vehiculos.add(v);

        Scanner sc = new Scanner(System.in);
        int opcion;

        do {

            System.out.println("\n1. Registrar entrada");
            System.out.println("2. Registrar salida");
            System.out.println("3. Dar de alta coche oficial");
            System.out.println("4. Dar de alta coche residente");
            System.out.println("0. Salir");
            opcion = sc.nextInt();
            sc.nextLine();

            switch (opcion) {
                case 1:
                    registrarEntrada(sc);
                    break;

                case 2:
                    registrarSalida(sc);
                    break;

                case 3:
                    darDeAltaOficial(sc);
                    break;

                case 4:
                    darDeAltaResidente(sc);
                    break;

                default:
                    System.out.println("Opción incorrecta");
                    break;
            }

        } while (true);
    }

    private static void registrarEntrada(Scanner sc) {
        String opcion;

        System.out.println("¿Se trata de un vehiculo no propietario? (S/N)");
        opcion = sc.nextLine();

        if (opcion.equalsIgnoreCase("S")) {
            System.out.println("Ingrese matrícula: ");
            String matricula = sc.nextLine();

            Vehiculo v = new Vehiculo(matricula, TipoVehiculo.NO_RESIDENTE);
            vehiculos.add(v);

            Estancia es = new Estancia(v);
            estanciasActuales.add(es);

            System.out.println("Estancia registrada exitosamente");

        } else if (opcion.equalsIgnoreCase("N")) {
            String matricula;

            System.out.println("Ingrese matrícula: ");
            matricula = sc.nextLine();

            Optional<Vehiculo> vehiculoAct = vehiculos.stream()
                    .filter(p -> p.getMatricula().equals(matricula)).findFirst();

            if (vehiculoAct.isPresent()) {
                Estancia es = new Estancia(vehiculoAct.get());
                estanciasActuales.add(es);
                System.out.println("Estancia registrada exitosamente");

            } else {
                System.out.println("El vehiculo no existe");
            }

        } else {
            System.out.println("Opcion incorrecta");
        }


    }

    private static void registrarSalida(Scanner sc) {
        String matricula;
        LocalDateTime fechaSalida = LocalDateTime.now();

        System.out.println("Ingrese matrícula: ");
        matricula = sc.nextLine();

        Optional<Estancia> estanciaAct = estanciasActuales.stream()
                .filter(p -> p.getVehiculo().getMatricula().equals(matricula)).findFirst();

        if (estanciaAct.isPresent()) {
            Optional<Vehiculo> vehiculoAct = vehiculos.stream()
                    .filter(p -> p.getMatricula().equals(matricula)).findFirst();

            estanciaAct.get().setFechaSalida(fechaSalida);
            estanciaAct.get().cobro();

            if (vehiculoAct.get().getTipo().equals(TipoVehiculo.NO_RESIDENTE)) { //Si el vehículo es no residente no nos interesa guardar su estancia ni a él
                vehiculos.remove(vehiculoAct.get());
                estanciasActuales.remove(estanciaAct.get());
                System.out.println("Salida registrada exitosamente");
                return;
            }

            vehiculoAct.get().addEstancia(estanciaAct.get());
            estanciasActuales.remove(estanciaAct.get());
            System.out.println("Salida registrada exitosamente");

        } else {
            System.out.println("El vehiculo no existe o no se encuentra aparcado");
        }
    }

    public static void darDeAltaOficial(Scanner sc) {
        System.out.println("Introduzca la matrícula: ");
        String matricula = sc.nextLine();

        Vehiculo v = new Vehiculo(matricula, TipoVehiculo.OFICIAL);
        vehiculos.add(v);
    }

    public static void darDeAltaResidente(Scanner sc) {
        System.out.println("Introduzca la matrícula: ");
        String matricula = sc.nextLine();

        Vehiculo v = new Vehiculo(matricula, TipoVehiculo.RESIDENTE);
        vehiculos.add(v);
    }

    public static void empezarMes(Scanner sc) {
        System.out.println("¿Está seguro? (S/N)");
        String opcion = sc.nextLine();

        if (opcion.equalsIgnoreCase("S")) {
            for (Vehiculo v : vehiculos) {
                
            }

        } else if (opcion.equalsIgnoreCase("N")) {
            return;
        } else {
            System.out.println("Opcion incorrecta");
        }
    }
}