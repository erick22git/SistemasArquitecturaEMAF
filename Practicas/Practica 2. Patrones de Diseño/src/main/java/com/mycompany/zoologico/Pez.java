package com.mycompany.zoologico;

public class Pez extends Animal {

    private String tipo;
    private double longitud;

    public Pez(String nombre, String tipo, double longitud) {
        super(nombre);
        this.tipo = tipo;
        this.longitud = longitud;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public String nadar() {
        return nombre + " (" + tipo + ") esta nadando, mide " + longitud + " cm.";
    }

    @Override
    public String mostrarInfo() {
        return String.format("Pez [tipo=%s, nombre=%s, longitud=%.2fcm]", tipo, nombre, longitud);
    }
}
