package com.mycompany.zoologico;

public class Mamifero extends Animal {

    private String tipo;
    private double temperatura;
    private int numeroPatas;
    private String color;

    public Mamifero(String nombre, String tipo, double temperatura, int numeroPatas, String color) {
        super(nombre);
        this.tipo = tipo;
        this.temperatura = temperatura;
        this.numeroPatas = numeroPatas;
        this.color = color;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(double temperatura) {
        this.temperatura = temperatura;
    }

    public int getNumeroPatas() {
        return numeroPatas;
    }

    public void setNumeroPatas(int numeroPatas) {
        this.numeroPatas = numeroPatas;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String mostrarInfo() {
        return String.format(
                "Mamifero [tipo=%s, nombre=%s, temperatura=%.1f°C, numeroPatas=%d, color=%s]",
                tipo, nombre, temperatura, numeroPatas, color);
    }
}
