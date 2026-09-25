package com.garagecoffee.model;

import java.time.LocalDateTime;

public abstract class Pedido {

    private int id;
    private LocalDateTime fechaHora;
    private EstadoPedido estado;
    private TipoEntrega tipoEntrega;
    private double total;
    private String observaciones;

    protected Pedido(int id, TipoEntrega tipoEntrega, String observaciones) {
        this.id = id;
        this.fechaHora = LocalDateTime.now();
        this.estado = EstadoPedido.PENDIENTE;
        this.tipoEntrega = tipoEntrega;
        this.total = 0.0;
        this.observaciones = observaciones;
    }

    public abstract void calcularTotal();

    public void cambiarEstado(EstadoPedido nuevoEstado) {
        this.estado = nuevoEstado;
    }

    public void cancelar() {
        this.estado = EstadoPedido.CANCELADO;
    }

    public int getId() {
        return id;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public TipoEntrega getTipoEntrega() {
        return tipoEntrega;
    }

    public double getTotal() {
        return total;
    }

    public String getObservaciones() {
        return observaciones;
    }

    protected void setTotal(double total) {
        this.total = total;
    }
}
