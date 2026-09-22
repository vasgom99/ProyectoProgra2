package com.garagecoffee.model;

import java.math.BigDecimal;

public class Ingrediente {

    private int id;
    private String nombre;
    private String unidadMedida;
    private BigDecimal stockActual;
    private BigDecimal stockMinimo;
    private BigDecimal costoUnitario;

    public Ingrediente() {
    }

    public Ingrediente(int id,
                       String nombre,
                       String unidadMedida,
                       BigDecimal stockActual,
                       BigDecimal stockMinimo,
                       BigDecimal costoUnitario) {

        this.id = id;
        this.nombre = nombre;
        this.unidadMedida = unidadMedida;
        this.stockActual = stockActual;
        this.stockMinimo = stockMinimo;
        this.costoUnitario = costoUnitario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public BigDecimal getStockActual() {
        return stockActual;
    }

    public void setStockActual(BigDecimal stockActual) {
        this.stockActual = stockActual;
    }

    public BigDecimal getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(BigDecimal stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public BigDecimal getCostoUnitario() {
        return costoUnitario;
    }

    public void setCostoUnitario(BigDecimal costoUnitario) {
        this.costoUnitario = costoUnitario;
    }

    public void descontarStock(BigDecimal cantidad) {
        this.stockActual = this.stockActual.subtract(cantidad);
    }

    public void aumentarStock(BigDecimal cantidad) {
        this.stockActual = this.stockActual.add(cantidad);
    }

    public boolean verificarAlertaStock() {
        return stockActual.compareTo(stockMinimo) <= 0;
    }
}