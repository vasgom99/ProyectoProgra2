package com.garagecoffee.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** @author Miguel */
public class Gasto {

    private int id;
    private String categoria;
    private String descripcion;
    private BigDecimal monto;
    private LocalDateTime fechaHora;

    public Gasto() {
    }

    public Gasto(
            int id,
            String categoria,
            String descripcion,
            BigDecimal monto,
            LocalDateTime fechaHora) {
        this.id = id;
        this.categoria = categoria;
        this.descripcion = descripcion;
        this.monto = monto;
        this.fechaHora = fechaHora;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }
}
