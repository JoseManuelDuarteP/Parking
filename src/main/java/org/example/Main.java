package org.example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    static List<Vehiculo> vehiculos = new ArrayList<>();
    static List<Estancia> estanciasActuales = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        Vehiculo v = new Vehiculo("ZZZ", TipoVehiculo.OFICIAL);
        vehiculos.add(v);
        v = new Vehiculo("AAA", TipoVehiculo.RESIDENTE);
        vehiculos.add(v);

        Scanner sc = new Scanner(System.in);
        int opcion;
        String nom;

        do {

            System.out.println("\n1. Registrar entrada");
            System.out.println("2. Registrar salida");
            System.out.println("3. Dar de alta coche oficial");
            System.out.println("4. Dar de alta coche residente");
            System.out.println("5. Empezar mes");
            System.out.println("6. Genera informe de pagos de residentes");
            System.out.println("7. Ver parking");
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

                case 5:
                    empezarMes(sc);
                    break;

                case 6:
                    System.out.println("Introduzca nombre del archivo");
                    nom = sc.nextLine();
                    generarInforme(nom);
                    break;

                case 7:
                    verParking();
                    break;

                case 0:
                    System.out.println("Saliendo...");
                    return;

                default:
                    System.out.println("Opción incorrecta");
                    break;
            }

        } while (true);
    }

    //Registrar entradas al parking
    private static void registrarEntrada(Scanner sc) {
        String opcion;

        System.out.println("¿Se trata de un vehiculo no propietario? (S/N)");
        opcion = sc.nextLine();

        //Si el vehículo que ingresa es no residente, se crea una instancia temporal
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

            //Comprueba si el vehiculo está dado de alta o existe
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

    //Registrar salidas
    private static void registrarSalida(Scanner sc) {
        String matricula;
        LocalDateTime fechaSalida = LocalDateTime.now();

        System.out.println("Ingrese matrícula: ");
        matricula = sc.nextLine();

        Optional<Estancia> estanciaAct = estanciasActuales.stream()
                .filter(p -> p.getVehiculo().getMatricula().equals(matricula)).findFirst();

        //Comprueba que el vehiculo exista o esté aparcado
        if (estanciaAct.isPresent()) {
            Optional<Vehiculo> vehiculoAct = vehiculos.stream()
                    .filter(p -> p.getMatricula().equals(matricula)).findFirst();

            estanciaAct.get().setFechaSalida(fechaSalida);
            estanciaAct.get().cobro();

            //Si el vehículo es no residente no nos interesa guardar su estancia ni a él
            if (vehiculoAct.get().getTipo().equals(TipoVehiculo.NO_RESIDENTE)) {
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

    private static void darDeAltaOficial(Scanner sc) {
        System.out.println("Introduzca la matrícula: ");
        String matricula = sc.nextLine();

        Vehiculo v = new Vehiculo(matricula, TipoVehiculo.OFICIAL);
        vehiculos.add(v);
    }

    private static void darDeAltaResidente(Scanner sc) {
        System.out.println("Introduzca la matrícula: ");
        String matricula = sc.nextLine();

        Vehiculo v = new Vehiculo(matricula, TipoVehiculo.RESIDENTE);
        vehiculos.add(v);
    }

    //Borra todas las estancias de todos los vehiculos registrados
    private static void empezarMes(Scanner sc) {
        System.out.println("¿Está seguro? (S/N)");
        String opcion = sc.nextLine();

        if (opcion.equalsIgnoreCase("S")) {
            for (Vehiculo v : vehiculos) {
                v.limpiarEstancias();
            }

        } else if (opcion.equalsIgnoreCase("N")) {
            return;
        } else {
            System.out.println("Opcion incorrecta");
        }
    }

    //Genera un .txt con la matrícula, tiempo total y pago total de todos los vehiculos registrados
    //No tiene en cuenta la estancia actual (vehiculos aparcados actualmente)
    private static void generarInforme(String nombre) throws IOException {
        File informe = new File(nombre + ".txt");

        try {
            FileWriter fw = new FileWriter(informe);
            fw.write("Matrícula:    Tiempo estacionado (min.):    Cantidad a pagar:\n");

            List<Vehiculo> vehiculosRes = vehiculos.stream()
                    .filter(p -> p.getTipo().equals(TipoVehiculo.RESIDENTE)).toList();

            for (Vehiculo v : vehiculosRes) {
                fw.write(v.getMatricula() + "           " + v.tiempoTotal() + "                             " + v.pagoTotal() + "\n");
            }

            fw.write("\nAVISO: ESTE INFORME NO TIENE EN CUENTA LA ESTANCIA ACTUAL EN CASO DE HABERLA");

            fw.flush();
            fw.close();

        } catch (IOException e) {
            System.out.println("No se pudo guardar el informe");
            System.out.println(e.getMessage());
        }
    }

    private static void verParking() {
        for (Vehiculo v : vehiculos) {
            System.out.println(v);
        }
    }
}