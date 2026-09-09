package com.biblioteca.proxy;

public class TestBibliotecaProxy {

    public static void main(String[] args) {
        LibroReal libroReal = new LibroReal(
                "Cien años de soledad",
                "Gabriel Garcia Marquez",
                1967,
                "Muchos años despues, frente al peloton de fusilamiento, el coronel Aureliano Buendia habia de acordarse de aquella tarde en que su padre lo llevo a conocer el hielo.");

        Libro proxy = new ProxyLibro(libroReal);

        System.out.println(proxy.leer());
    }
}
