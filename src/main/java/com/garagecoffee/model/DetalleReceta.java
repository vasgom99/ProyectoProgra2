package com.garagecoffee.model;

import java.math.BigDecimal;

public class DetalleReceta {

    private int id;
    private BigDecimal cantidadRequerida;

    private Producto producto;
    private Ingrediente ingrediente;

    public DetalleReceta() {
    }

    public DetalleReceta(int id, BigDecimal cantidadRequerida,
                         Producto producto,
                         Ingrediente ingrediente) {

        this.id = id;
        this.cantidadRequerida = cantidadRequerida;
        this.producto = producto;
        this.ingrediente = ingrediente;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BigDecimal getCantidadRequerida() {
        return cantidadRequerida;
    }

    public void setCantidadRequerida(BigDecimal cantidadRequerida) {
        this.cantidadRequerida = cantidadRequerida;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Ingrediente getIngrediente() {
        return ingrediente;
    }

    public void setIngrediente(Ingrediente ingrediente) {
        this.ingrediente = ingrediente;
    }
}