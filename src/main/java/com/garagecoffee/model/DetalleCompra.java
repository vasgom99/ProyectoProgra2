package com.garagecoffee.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DetalleCompra {

    private int id;
    private int compraId;

    private BigDecimal cantidad;
    private BigDecimal costoUnitario;
    private BigDecimal subtotal;

    private Ingrediente ingrediente;

    public DetalleCompra() {
        subtotal = BigDecimal.ZERO;
    }

    public DetalleCompra(BigDecimal cantidad,
                         BigDecimal costoUnitario,
                         Ingrediente ingrediente) {

        this.cantidad = cantidad;
        this.costoUnitario = costoUnitario;
        this.ingrediente = ingrediente;

        calcularSubtotal();
    }

    public BigDecimal calcularSubtotal() {

        if (cantidad == null || costoUnitario == null) {
            subtotal = BigDecimal.ZERO;
        } else {
            subtotal = cantidad.multiply(costoUnitario).setScale(2, RoundingMode.HALF_UP);
        }

        return subtotal;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCompraId() {
        return compraId;
    }

    public void setCompraId(int compraId) {
        this.compraId = compraId;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
        calcularSubtotal();
    }

    public BigDecimal getCostoUnitario() {
        return costoUnitario;
    }

    public void setCostoUnitario(BigDecimal costoUnitario) {
        this.costoUnitario = costoUnitario;
        calcularSubtotal();
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public Ingrediente getIngrediente() {
        return ingrediente;
    }

    public void setIngrediente(Ingrediente ingrediente) {
        this.ingrediente = ingrediente;
    }
}
