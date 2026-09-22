package com.garagecoffee.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PlatoDelDia {

    private int id;
    private LocalDate fecha;
    private String notaEspecial;
    private BigDecimal precioEspecial;

    private Producto producto;

    public PlatoDelDia() {
    }

    public PlatoDelDia(int id,
                       LocalDate fecha,
                       String notaEspecial,
                       BigDecimal precioEspecial,
                       Producto producto) {

        this.id = id;
        this.fecha = fecha;
        this.notaEspecial = notaEspecial;
        this.precioEspecial = precioEspecial;
        this.producto = producto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getNotaEspecial() {
        return notaEspecial;
    }

    public void setNotaEspecial(String notaEspecial) {
        this.notaEspecial = notaEspecial;
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
}