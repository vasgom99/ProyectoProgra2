package com.garagecoffee.model;

import java.math.BigDecimal;

public class DetalleMenu {

    private int id;
    private BigDecimal precioEspecial;

    private Producto producto;
    private Menu menu;

    public DetalleMenu() {
    }

    public DetalleMenu(int id, BigDecimal precioEspecial,
                       Producto producto, Menu menu) {

        this.id = id;
        this.precioEspecial = precioEspecial;
        this.producto = producto;
        this.menu = menu;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BigDecimal getPrecioEspecial() {
        return precioEspecial;
    }

    public void setPrecioEspecial(BigDecimal precioEspecial) {
        this.precioEspecial = precioEspecial;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }
}