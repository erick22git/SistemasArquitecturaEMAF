package com.mycompany.zoologico;

public class Ave extends Animal {

    private String tipo;
    private double peso;
    private double tamañoAlas;

    public Ave(String nombre, String tipo, double peso, double tamañoAlas) {
        super(nombre);
        this.tipo = tipo;
        this.peso = peso;
        this.tamañoAlas = tamañoAlas;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public double getTamañoAlas() {
        return tamañoAlas;
    }

    public void setTamañoAlas(double tamañoAlas) {
        this.tamañoAlas = tamañoAlas;
    }

    public String volar() {
        return nombre + " (" + tipo + ") esta volando con una envergadura de " + tamañoAlas + " m.";
    }

    @Override
    public String mostrarInfo() {
        return String.format(
                "Ave [tipo=%s, nombre=%s, peso=%.2fkg, tamañoAlas=%.2fm]",
                tipo, nombre, peso, tamañoAlas);
    }
}
