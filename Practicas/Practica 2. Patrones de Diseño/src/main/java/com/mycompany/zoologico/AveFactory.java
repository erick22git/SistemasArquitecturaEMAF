package com.mycompany.zoologico;

import java.util.Map;

public class AveFactory implements AnimalFactory {

    @Override
    public Animal crearAnimal(String tipo, Map<String, Object> atributos) {
        String nombre = (String) atributos.get("nombre");
        double peso = (Double) atributos.get("peso");
        double tamañoAlas = (Double) atributos.get("tamañoAlas");
        return new Ave(nombre, tipo, peso, tamañoAlas);
    }
}
