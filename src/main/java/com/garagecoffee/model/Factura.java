package com.garagecoffee.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Factura {

    private int id;
    private Pedido pedido;
    private String numero;
    private LocalDateTime fecha;
    private BigDecimal subtotal;
    private BigDecimal impuesto;
    private BigDecimal total;

    public Factura() {
    }

    public Factura(
            int id,
            Pedido pedido,
            String numero,
            LocalDateTime fecha,
            BigDecimal subtotal,
            BigDecimal impuesto,
            BigDecimal total) {
        this.id = id;
        this.pedido = pedido;
        this.numero = numero;
        this.fecha = fecha;
        this.subtotal = subtotal;
        this.impuesto = impuesto;
        this.total = total;
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

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getImpuesto() {
        return impuesto;
    }

    public void setImpuesto(BigDecimal impuesto) {
        this.impuesto = impuesto;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
