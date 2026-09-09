package com.mycompany.zoologico;

import java.util.Map;

public class MamiferoFactory implements AnimalFactory {

    @Override
    public Animal crearAnimal(String tipo, Map<String, Object> atributos) {
        String nombre = (String) atributos.get("nombre");
        double temperatura = (Double) atributos.get("temperatura");
        int numeroPatas = (Integer) atributos.get("numeroPatas");
        String color = (String) atributos.get("color");
        return new Mamifero(nombre, tipo, temperatura, numeroPatas, color);
    }
}
