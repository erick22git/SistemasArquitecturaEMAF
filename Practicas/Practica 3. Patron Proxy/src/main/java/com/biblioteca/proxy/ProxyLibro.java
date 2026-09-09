package com.biblioteca.proxy;

public class ProxyLibro implements Libro {

    private LibroReal libroReal;

    public ProxyLibro(LibroReal libroReal) {
        this.libroReal = libroReal;
    }

    @Override
    public String leer() {
        System.out.println("Verificando permisos...");
        return libroReal.leer();
    }
}
