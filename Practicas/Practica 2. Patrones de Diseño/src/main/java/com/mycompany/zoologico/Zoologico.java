package com.mycompany.zoologico;

import java.util.ArrayList;
import java.util.List;

public class Zoologico {

    private String nombre;
    private String direccion;
    private String telefono;
    private List<Jaula> jaulas;

    public Zoologico(String nombre, String direccion, String telefono) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.jaulas = new ArrayList<>();
    }

    public void agregarJaula(Jaula jaula) {
        jaulas.add(jaula);
    }

    public List<Jaula> getJaulas() {
        return jaulas;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public List<Mamifero> listarMamiferos() {
        List<Mamifero> resultado = new ArrayList<>();
        for (Jaula j : jaulas) {
            if (j.getAnimal() instanceof Mamifero) {
                resultado.add((Mamifero) j.getAnimal());
            }
        }
        return resultado;
    }

    public List<Ave> listarAves() {
        List<Ave> resultado = new ArrayList<>();
        for (Jaula j : jaulas) {
            if (j.getAnimal() instanceof Ave) {
                resultado.add((Ave) j.getAnimal());
            }
        }
        return resultado;
    }

    public List<Pez> listarPeces() {
        List<Pez> resultado = new ArrayList<>();
        for (Jaula j : jaulas) {
            if (j.getAnimal() instanceof Pez) {
                resultado.add((Pez) j.getAnimal());
            }
        }
        return resultado;
    }

    public int totalJaulas() {
        return jaulas.size();
    }

    public int totalAnimales() {
        return jaulas.size();
    }

    @Override
    public String toString() {
        return String.format(
                "Zoologico [nombre=%s, direccion=%s, telefono=%s, totalJaulas=%d, totalAnimales=%d]",
                nombre, direccion, telefono, totalJaulas(), totalAnimales());
    }
}
