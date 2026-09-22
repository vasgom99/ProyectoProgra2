package com.garagecoffee.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MovimientoStock {

    private int id;
    private TipoMovimientoStock tipo;
    private BigDecimal cantidad;
    private BigDecimal costoTotal;
    private LocalDateTime fechaHora;
    private String motivo;

    private Ingrediente ingrediente;

    public MovimientoStock() {
    }

    public MovimientoStock(int id,
                           TipoMovimientoStock tipo,
                           BigDecimal cantidad,
                           BigDecimal costoTotal,
                           LocalDateTime fechaHora,
                           String motivo,
                           Ingrediente ingrediente) {

        this.id = id;
        this.tipo = tipo;
        this.cantidad = cantidad;
        this.costoTotal = costoTotal;
        this.fechaHora = fechaHora;
        this.motivo = motivo;
        this.ingrediente = ingrediente;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TipoMovimientoStock getTipo() {
        return tipo;
    }

    public void setTipo(TipoMovimientoStock tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getCostoTotal() {
        return costoTotal;
    }

    public void setCostoTotal(BigDecimal costoTotal) {
        this.costoTotal = costoTotal;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Ingrediente getIngrediente() {
        return ingrediente;
    }

    public void setIngrediente(Ingrediente ingrediente) {
        this.ingrediente = ingrediente;
    }
}