package com.ventas.sistema;

public class DetalleVenta {

    private Producto producto;
    private int cantidad;
    private String descripcion;

    public DetalleVenta(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.descripcion = producto.getDescripcion();
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getTotal() {
        return producto.getPrecio() * cantidad;
    }
}
