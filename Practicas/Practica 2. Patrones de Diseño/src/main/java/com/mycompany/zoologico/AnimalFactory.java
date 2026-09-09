package com.mycompany.zoologico;

import java.util.Map;

public interface AnimalFactory {

    Animal crearAnimal(String tipo, Map<String, Object> atributos);
}
