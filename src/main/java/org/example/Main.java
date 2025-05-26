package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    static List<Vehiculo> vehiculos = new ArrayList<>();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int opcion;

        do {

            System.out.println("\n1. Registrar entrada");
            System.out.println("2. Registrar salida");
            System.out.println("0. Salir");
            opcion = sc.nextInt();

            switch (opcion) {
                case 1:
                    registrarEntrada(sc);
                    break;

                default:
                    System.out.println("Opción incorrecta");
                    break;
            }

        } while (true);
    }

    private static void registrarEntrada(Scanner sc) {
        String matricula;

        System.out.println("Ingrese matrícula: ");
        matricula = sc.nextLine();

        Optional<Vehiculo> select = vehiculos.stream()
                .filter(vehiculo -> vehiculo.getMatricula().equals(matricula)).findFirst();

        if (select.isPresent()) {
            Estancia es = new Estancia(select.get());
            select.get().addEstancia(es);
            System.out.println("Estancia registrada exitosamente");
        } else {
            System.out.println("El vehiculo no existe");
        }
    }
}