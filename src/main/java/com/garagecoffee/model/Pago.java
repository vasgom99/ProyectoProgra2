package com.garagecoffee.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Pago {

    private int id;
    private Pedido pedido;
    private MetodoPago metodo;
    private BigDecimal monto;
    private BigDecimal cambio;
    private LocalDateTime fechaHora;

    public Pago() {
    }

    public Pago(
            int id,
            Pedido pedido,
            MetodoPago metodo,
            BigDecimal monto,
            BigDecimal cambio,
            LocalDateTime fechaHora) {
        this.id = id;
        this.pedido = pedido;
        this.metodo = metodo;
        this.monto = monto;
        this.cambio = cambio;
        this.fechaHora = fechaHora;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public MetodoPago getMetodo() {
        return metodo;
    }

    public void setMetodo(MetodoPago metodo) {
        this.metodo = metodo;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public BigDecimal getCambio() {
        return cambio;
    }

    public void setCambio(BigDecimal cambio) {
        this.cambio = cambio;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }
}
