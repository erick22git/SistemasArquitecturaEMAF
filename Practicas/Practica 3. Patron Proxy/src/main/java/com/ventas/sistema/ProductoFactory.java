package com.ventas.sistema;

public class ProductoFactory {

    private ProductoFactory() {
    }

    public static ProductoSimple crearProductoSimple(String descripcion, double precio) {
        return new ProductoSimple(descripcion, precio);
    }

    public static ProductoCompuesto crearProductoCompuesto() {
        return new ProductoCompuesto();
    }
}
