package com.mycompany.zoologico;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {

    private static final Scanner sc = new Scanner(System.in);
    private static final AnimalFactory mamiferoFactory = new MamiferoFactory();
    private static final AnimalFactory aveFactory = new AveFactory();
    private static final AnimalFactory pezFactory = new PezFactory();

    public static void main(String[] args) {
        Zoologico zoologico = new Zoologico("Zoologico Central", "Av. Principal 123", "555-1234");

        boolean salir = false;
        while (!salir) {
            mostrarMenu();
            String opcion = sc.nextLine().trim();

            switch (opcion) {
                case "1":
                    agregarMamifero(zoologico);
                    break;
                case "2":
                    agregarAve(zoologico);
                    break;
                case "3":
                    agregarPez(zoologico);
                    break;
                case "4":
                    mostrarMamiferos(zoologico);
                    break;
                case "5":
                    mostrarAves(zoologico);
                    break;
                case "6":
                    mostrarPeces(zoologico);
                    break;
                case "7":
                    mostrarInfoZoologico(zoologico);
                    break;
                case "0":
                    salir = true;
                    System.out.println("Saliendo del sistema...");
                    break;
                default:
                    System.out.println("Opcion invalida. Intenta de nuevo.");
            }
        }
        sc.close();
    }

    private static void mostrarMenu() {
        System.out.println();
        System.out.println("===== ZOOLOGICO =====");
        System.out.println("1. Añadir mamifero");
        System.out.println("2. Añadir ave");
        System.out.println("3. Añadir pez");
        System.out.println("4. Mostrar mamiferos");
        System.out.println("5. Mostrar aves");
        System.out.println("6. Mostrar peces");
        System.out.println("7. Mostrar informacion del zoologico");
        System.out.println("0. Salir");
        System.out.print("Elige una opcion: ");
    }

    private static void agregarMamifero(Zoologico zoologico) {
        System.out.print("Tipo (leon/oso/mono): ");
        String tipo = sc.nextLine().trim().toLowerCase();
        if (!tipo.equals("leon") && !tipo.equals("oso") && !tipo.equals("mono")) {
            System.out.println("Tipo de mamifero no reconocido.");
            return;
        }

        System.out.print("Nombre: ");
        String nombre = sc.nextLine().trim();

        Double temperatura = leerDouble("Temperatura (°C): ");
        if (temperatura == null) return;

        Integer numeroPatas = leerInt("Numero de patas: ");
        if (numeroPatas == null) return;

        System.out.print("Color: ");
        String color = sc.nextLine().trim();

        double[] dimensiones = leerDimensionesJaula();
        if (dimensiones == null) return;

        Map<String, Object> atributos = new HashMap<>();
        atributos.put("nombre", nombre);
        atributos.put("temperatura", temperatura);
        atributos.put("numeroPatas", numeroPatas);
        atributos.put("color", color);

        Animal animal = mamiferoFactory.crearAnimal(tipo, atributos);
        Jaula jaula = new Jaula(animal, dimensiones[0], dimensiones[1], dimensiones[2]);
        zoologico.agregarJaula(jaula);
        System.out.println("Mamifero agregado correctamente.");
    }

    private static void agregarAve(Zoologico zoologico) {
        System.out.print("Tipo (loro/aguila/condor): ");
        String tipo = sc.nextLine().trim().toLowerCase();
        if (!tipo.equals("loro") && !tipo.equals("aguila") && !tipo.equals("condor")) {
            System.out.println("Tipo de ave no reconocido.");
            return;
        }

        System.out.print("Nombre: ");
        String nombre = sc.nextLine().trim();

        Double peso = leerDouble("Peso (kg): ");
        if (peso == null) return;

        Double tamañoAlas = leerDouble("Tamaño de alas (m): ");
        if (tamañoAlas == null) return;

        double[] dimensiones = leerDimensionesJaula();
        if (dimensiones == null) return;

        Map<String, Object> atributos = new HashMap<>();
        atributos.put("nombre", nombre);
        atributos.put("peso", peso);
        atributos.put("tamañoAlas", tamañoAlas);

        Animal animal = aveFactory.crearAnimal(tipo, atributos);
        Jaula jaula = new Jaula(animal, dimensiones[0], dimensiones[1], dimensiones[2]);
        zoologico.agregarJaula(jaula);
        System.out.println("Ave agregada correctamente.");
    }

    private static void agregarPez(Zoologico zoologico) {
        System.out.print("Tipo (pacu/sabalo): ");
        String tipo = sc.nextLine().trim().toLowerCase();
        if (!tipo.equals("pacu") && !tipo.equals("sabalo")) {
            System.out.println("Tipo de pez no reconocido.");
            return;
        }

        System.out.print("Nombre: ");
        String nombre = sc.nextLine().trim();

        Double longitud = leerDouble("Longitud (cm): ");
        if (longitud == null) return;

        double[] dimensiones = leerDimensionesJaula();
        if (dimensiones == null) return;

        Map<String, Object> atributos = new HashMap<>();
        atributos.put("nombre", nombre);
        atributos.put("longitud", longitud);

        Animal animal = pezFactory.crearAnimal(tipo, atributos);
        Jaula jaula = new Jaula(animal, dimensiones[0], dimensiones[1], dimensiones[2]);
        zoologico.agregarJaula(jaula);
        System.out.println("Pez agregado correctamente.");
    }

    private static void mostrarMamiferos(Zoologico zoologico) {
        var mamiferos = zoologico.listarMamiferos();
        if (mamiferos.isEmpty()) {
            System.out.println("No hay mamiferos registrados.");
            return;
        }
        System.out.println("--- Mamiferos ---");
        for (Mamifero m : mamiferos) {
            System.out.println(m);
        }
    }

    private static void mostrarAves(Zoologico zoologico) {
        var aves = zoologico.listarAves();
        if (aves.isEmpty()) {
            System.out.println("No hay aves registradas.");
            return;
        }
        System.out.println("--- Aves ---");
        for (Ave a : aves) {
            System.out.println(a);
        }
    }

    private static void mostrarPeces(Zoologico zoologico) {
        var peces = zoologico.listarPeces();
        if (peces.isEmpty()) {
            System.out.println("No hay peces registrados.");
            return;
        }
        System.out.println("--- Peces ---");
        for (Pez p : peces) {
            System.out.println(p);
        }
    }

    private static void mostrarInfoZoologico(Zoologico zoologico) {
        System.out.println("--- Informacion del zoologico ---");
        System.out.println(zoologico);
    }

    private static double[] leerDimensionesJaula() {
        Double alto = leerDouble("Alto de la jaula (m): ");
        if (alto == null) return null;
        Double ancho = leerDouble("Ancho de la jaula (m): ");
        if (ancho == null) return null;
        Double largo = leerDouble("Largo de la jaula (m): ");
        if (largo == null) return null;
        return new double[]{alto, ancho, largo};
    }

    private static Double leerDouble(String mensaje) {
        System.out.print(mensaje);
        String entrada = sc.nextLine().trim();
        try {
            return Double.parseDouble(entrada);
        } catch (NumberFormatException e) {
            System.out.println("Valor numerico invalido: '" + entrada + "'. Operacion cancelada.");
            return null;
        }
    }

    private static Integer leerInt(String mensaje) {
        System.out.print(mensaje);
        String entrada = sc.nextLine().trim();
        try {
            return Integer.parseInt(entrada);
        } catch (NumberFormatException e) {
            System.out.println("Valor entero invalido: '" + entrada + "'. Operacion cancelada.");
            return null;
        }
    }
}
