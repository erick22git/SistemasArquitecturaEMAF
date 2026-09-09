package com.mycompany.zoologico;

public class Jaula {

    private Animal animal;
    private double alto;
    private double ancho;
    private double largo;

    public Jaula(Animal animal, double alto, double ancho, double largo) {
        this.animal = animal;
        this.alto = alto;
        this.ancho = ancho;
        this.largo = largo;
    }

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    public double getAlto() {
        return alto;
    }

    public void setAlto(double alto) {
        this.alto = alto;
    }

    public double getAncho() {
        return ancho;
    }

    public void setAncho(double ancho) {
        this.ancho = ancho;
    }

    public double getLargo() {
        return largo;
    }

    public void setLargo(double largo) {
        this.largo = largo;
    }

    @Override
    public String toString() {
        return String.format(
                "Jaula [%.2fm x %.2fm x %.2fm] -> %s",
                alto, ancho, largo, animal.mostrarInfo());
    }
}
