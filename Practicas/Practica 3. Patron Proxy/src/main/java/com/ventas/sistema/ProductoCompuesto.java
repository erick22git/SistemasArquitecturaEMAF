package com.ventas.sistema;

import java.util.ArrayList;

public class ProductoCompuesto implements Producto {

    private ArrayList<Producto> componentes;

    public ProductoCompuesto() {
        this.componentes = new ArrayList<>();
    }

    public void agregar(Producto producto) {
        componentes.add(producto);
    }

    public void remover(Producto producto) {
        componentes.remove(producto);
    }

    public ArrayList<Producto> getComponentes() {
        return componentes;
    }

    @Override
    public String getDescripcion() {
        StringBuilder sb = new StringBuilder("Compuesto: ");
        for (int i = 0; i < componentes.size(); i++) {
            sb.append(componentes.get(i).getDescripcion());
            if (i < componentes.size() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    @Override
    public double getPrecio() {
        double total = 0;
        for (Producto p : componentes) {
            total += p.getPrecio();
        }
        return total;
    }
}
