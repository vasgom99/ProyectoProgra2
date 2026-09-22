package com.garagecoffee.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Perdida {

    private int id;
    private BigDecimal cantidad;
    private String motivo;
    private LocalDateTime fechaHora;

    private Ingrediente ingrediente;

    public Perdida() {
    }

    public Perdida(int id,
                   BigDecimal cantidad,
                   String motivo,
                   LocalDateTime fechaHora,
                   Ingrediente ingrediente) {

        this.id = id;
        this.cantidad = cantidad;
        this.motivo = motivo;
        this.fechaHora = fechaHora;
        this.ingrediente = ingrediente;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public Ingrediente getIngrediente() {
        return ingrediente;
    }

    public void setIngrediente(Ingrediente ingrediente) {
        this.ingrediente = ingrediente;
    }
}