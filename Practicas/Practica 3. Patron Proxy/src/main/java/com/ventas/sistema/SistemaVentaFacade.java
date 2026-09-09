package com.ventas.sistema;

import java.time.LocalDate;
import java.util.ArrayList;

public class SistemaVentaFacade {

    private ArrayList<Producto> productosDisponibles;

    public SistemaVentaFacade() {
        this.productosDisponibles = new ArrayList<>();
    }

    public void registrarProducto(Producto producto) {
        productosDisponibles.add(producto);
    }

    public ArrayList<Producto> getProductosDisponibles() {
        return productosDisponibles;
    }

    public Venta crearVenta(String nombre, LocalDate fecha, String tipoDocumento, String numeroDocumento) {
        return new Venta(nombre, fecha, tipoDocumento, numeroDocumento);
    }

    public void agregarDetalleVenta(Venta venta, Producto producto, int cantidad) {
        DetalleVenta detalle = new DetalleVenta(producto, cantidad);
        venta.agregarDetalle(detalle);
    }

    public void mostrarDetalleVenta(Venta venta) {
        System.out.println("Venta: " + venta.getNombre());
        System.out.println("Fecha: " + venta.getFecha());
        System.out.println("Documento: " + venta.getTipoDocumento() + " " + venta.getNumeroDocumento());
        System.out.println("--- Detalles ---");
        for (DetalleVenta d : venta.getDetalles()) {
            System.out.printf("%s x%d = %.2f%n", d.getDescripcion(), d.getCantidad(), d.getTotal());
        }
        System.out.printf("TOTAL: %.2f%n", venta.calcularTotal());
    }

    public void procesarPago(Venta venta, TipoPago tipoPago) {
        System.out.printf("Procesando pago de %.2f mediante %s...%n", venta.calcularTotal(), tipoPago);
        System.out.println("Pago procesado correctamente.");
    }
}
