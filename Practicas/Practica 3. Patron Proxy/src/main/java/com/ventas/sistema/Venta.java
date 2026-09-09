package com.ventas.sistema;

import java.time.LocalDate;
import java.util.ArrayList;

public class Venta {

    private String nombre;
    private LocalDate fecha;
    private String tipoDocumento;
    private String numeroDocumento;
    private ArrayList<DetalleVenta> detalles;

    public Venta(String nombre, LocalDate fecha, String tipoDocumento, String numeroDocumento) {
        this.nombre = nombre;
        this.fecha = fecha;
        this.tipoDocumento = tipoDocumento;
        this.numeroDocumento = numeroDocumento;
        this.detalles = new ArrayList<>();
    }

    public void agregarDetalle(DetalleVenta detalle) {
        detalles.add(detalle);
    }

    public double calcularTotal() {
        double total = 0;
        for (DetalleVenta d : detalles) {
            total += d.getTotal();
        }
        return total;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public ArrayList<DetalleVenta> getDetalles() {
        return detalles;
    }
}
