package com.biblioteca.proxy;

public class LibroReal implements Libro {

    private String contenido;
    private String titulo;
    private String autor;
    private int anio;

    public LibroReal(String titulo, String autor, int anio, String contenido) {
        this.titulo = titulo;
        this.autor = autor;
        this.anio = anio;
        this.contenido = contenido;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    @Override
    public String leer() {
        return contenido;
    }
}
