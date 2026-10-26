package com.garagecoffee.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// representa el pago de un pedido.
public class Pago {
    private int id;
    private Pedido pedido;
    private Caja caja;
    private BigDecimal monto;
    private BigDecimal cambio;
    private LocalDateTime fechaHora;

    public Pago() { }

    public Pago(int id, Pedido pedido, Caja caja, BigDecimal monto, BigDecimal cambio, LocalDateTime fechaHora) {
        this.id = id;
        this.pedido = pedido;
        this.caja = caja;
        this.monto = monto;
        this.cambio = cambio;
        this.fechaHora = fechaHora;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Pedido getPedido() { return pedido; }
    public void setPedido(Pedido pedido) { this.pedido = pedido; }
    public Caja getCaja() { return caja; }
    public void setCaja(Caja caja) { this.caja = caja; }
    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }
    public BigDecimal getCambio() { return cambio; }
    public void setCambio(BigDecimal cambio) { this.cambio = cambio; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }
}
