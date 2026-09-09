package com.mycompany.zoologico;

public abstract class Animal {

    protected String nombre;

    public Animal(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public abstract String mostrarInfo();

    @Override
    public String toString() {
        return mostrarInfo();
    }
}
