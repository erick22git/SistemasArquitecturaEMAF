package com.mycompany.zoologico;

import java.util.Map;

public class PezFactory implements AnimalFactory {

    @Override
    public Animal crearAnimal(String tipo, Map<String, Object> atributos) {
        String nombre = (String) atributos.get("nombre");
        double longitud = (Double) atributos.get("longitud");
        return new Pez(nombre, tipo, longitud);
    }
}
