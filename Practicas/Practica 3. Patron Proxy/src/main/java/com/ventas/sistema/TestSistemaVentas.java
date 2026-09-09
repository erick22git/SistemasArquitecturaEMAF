package com.ventas.sistema;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class TestSistemaVentas {

    private static final Scanner sc = new Scanner(System.in);
    private static final SistemaVentaFacade facade = new SistemaVentaFacade();
    private static Venta ventaActual;

    public static void main(String[] args) {
        cargarProductosDeEjemplo();

        boolean salir = false;
        while (!salir) {
            mostrarMenu();
            String opcion = sc.nextLine().trim().toLowerCase();

            switch (opcion) {
                case "a":
                    crearVenta();
                    break;
                case "b":
                    agregarProductoSimple();
                    break;
                case "c":
                    agregarProductoCompuesto();
                    break;
                case "d":
                    verDetalleVenta();
                    break;
                case "e":
                    procesarPago();
                    break;
                case "f":
                    salir = true;
                    System.out.println("Saliendo del sistema...");
                    break;
                default:
                    System.out.println("Opcion invalida. Intenta de nuevo.");
            }
        }
        sc.close();
    }

    private static void cargarProductosDeEjemplo() {
        facade.registrarProducto(ProductoFactory.crearProductoSimple("Teclado mecanico", 45.00));
        facade.registrarProducto(ProductoFactory.crearProductoSimple("Mouse optico", 15.50));
        facade.registrarProducto(ProductoFactory.crearProductoSimple("Monitor 24 pulgadas", 120.00));
        facade.registrarProducto(ProductoFactory.crearProductoSimple("Cable HDMI", 8.75));
    }

    private static void mostrarMenu() {
        System.out.println();
        System.out.println("===== SISTEMA DE VENTAS =====");
        System.out.println("a) Crear venta");
        System.out.println("b) Agregar producto simple a venta");
        System.out.println("c) Agregar producto compuesto a venta");
        System.out.println("d) Ver detalle de venta");
        System.out.println("e) Seleccionar tipo de pago y procesar pago");
        System.out.println("f) Salir");
        System.out.print("Elige una opcion: ");
    }

    private static void crearVenta() {
        System.out.print("Nombre del cliente: ");
        String nombre = sc.nextLine().trim();

        System.out.print("Fecha (yyyy-MM-dd): ");
        LocalDate fecha;
        try {
            fecha = LocalDate.parse(sc.nextLine().trim());
        } catch (DateTimeParseException e) {
            System.out.println("Fecha invalida. Operacion cancelada.");
            return;
        }

        System.out.print("Tipo de documento (Boleta/Factura): ");
        String tipoDocumento = sc.nextLine().trim();

        System.out.print("Numero de documento: ");
        String numeroDocumento = sc.nextLine().trim();

        ventaActual = facade.crearVenta(nombre, fecha, tipoDocumento, numeroDocumento);
        System.out.println("Venta creada correctamente.");
    }

    private static void agregarProductoSimple() {
        if (ventaActual == null) {
            System.out.println("Primero debes crear una venta.");
            return;
        }

        ArrayList<Producto> disponibles = facade.getProductosDisponibles();
        mostrarProductosDisponibles(disponibles);

        Integer indice = leerInt("Selecciona un producto (numero): ");
        if (indice == null || indice < 1 || indice > disponibles.size()) {
            System.out.println("Seleccion invalida.");
            return;
        }

        Integer cantidad = leerInt("Cantidad: ");
        if (cantidad == null || cantidad <= 0) {
            System.out.println("Cantidad invalida.");
            return;
        }

        Producto producto = disponibles.get(indice - 1);
        facade.agregarDetalleVenta(ventaActual, producto, cantidad);
        System.out.println("Producto agregado a la venta.");
    }

    private static void agregarProductoCompuesto() {
        if (ventaActual == null) {
            System.out.println("Primero debes crear una venta.");
            return;
        }

        ArrayList<Producto> disponibles = facade.getProductosDisponibles();
        ProductoCompuesto compuesto = ProductoFactory.crearProductoCompuesto();

        boolean seguirAgregando = true;
        while (seguirAgregando) {
            mostrarProductosDisponibles(disponibles);
            Integer indice = leerInt("Selecciona un producto para el compuesto (0 para terminar): ");
            if (indice == null) {
                return;
            }
            if (indice == 0) {
                seguirAgregando = false;
            } else if (indice >= 1 && indice <= disponibles.size()) {
                compuesto.agregar(disponibles.get(indice - 1));
            } else {
                System.out.println("Seleccion invalida.");
            }
        }

        if (compuesto.getComponentes().isEmpty()) {
            System.out.println("No se agrego ningun componente. Operacion cancelada.");
            return;
        }

        Integer cantidad = leerInt("Cantidad del producto compuesto: ");
        if (cantidad == null || cantidad <= 0) {
            System.out.println("Cantidad invalida.");
            return;
        }

        facade.agregarDetalleVenta(ventaActual, compuesto, cantidad);
        System.out.println("Producto compuesto agregado a la venta: " + compuesto.getDescripcion());
    }

    private static void verDetalleVenta() {
        if (ventaActual == null) {
            System.out.println("Primero debes crear una venta.");
            return;
        }
        facade.mostrarDetalleVenta(ventaActual);
    }

    private static void procesarPago() {
        if (ventaActual == null) {
            System.out.println("Primero debes crear una venta.");
            return;
        }

        System.out.println("Tipos de pago disponibles:");
        TipoPago[] tipos = TipoPago.values();
        for (int i = 0; i < tipos.length; i++) {
            System.out.println((i + 1) + ") " + tipos[i]);
        }

        Integer indice = leerInt("Selecciona el tipo de pago (numero): ");
        if (indice == null || indice < 1 || indice > tipos.length) {
            System.out.println("Seleccion invalida.");
            return;
        }

        facade.procesarPago(ventaActual, tipos[indice - 1]);
    }

    private static void mostrarProductosDisponibles(ArrayList<Producto> disponibles) {
        System.out.println("--- Productos disponibles ---");
        for (int i = 0; i < disponibles.size(); i++) {
            Producto p = disponibles.get(i);
            System.out.printf("%d) %s - %.2f%n", i + 1, p.getDescripcion(), p.getPrecio());
        }
    }

    private static Integer leerInt(String mensaje) {
        System.out.print(mensaje);
        String entrada = sc.nextLine().trim();
        try {
            return Integer.parseInt(entrada);
        } catch (NumberFormatException e) {
            System.out.println("Valor entero invalido: '" + entrada + "'.");
            return null;
        }
    }
}
