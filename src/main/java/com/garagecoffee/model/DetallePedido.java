package com.garagecoffee.model;

import java.math.BigDecimal;

public class DetallePedido {

    private int id;
    private int pedidoId;
    private int cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;
    private Producto producto;

    public DetallePedido() {
        this.precioUnitario = BigDecimal.ZERO;
        this.subtotal = BigDecimal.ZERO;
    }

    public DetallePedido(
            int id,
            int cantidad,
            BigDecimal precioUnitario,
            Producto producto) {
        this();
        this.id = id;
        validarCantidad(cantidad);
        validarPrecioUnitario(precioUnitario);
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.producto = producto;
        this.subtotal = precioUnitario.multiply(BigDecimal.valueOf(cantidad));
    }

    public BigDecimal calcularSubtotal() {
        if (cantidad <= 0 || precioUnitario == null) {
            subtotal = BigDecimal.ZERO;
        } else {
            subtotal = precioUnitario.multiply(BigDecimal.valueOf(cantidad));
        }
        return subtotal;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(int pedidoId) {
        this.pedidoId = pedidoId;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        validarCantidad(cantidad);
        this.cantidad = cantidad;
        calcularSubtotal();
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        validarPrecioUnitario(precioUnitario);
        this.precioUnitario = precioUnitario;
        calcularSubtotal();
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    private static void validarCantidad(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero");
        }
    }

    private static void validarPrecioUnitario(BigDecimal precioUnitario) {
        if (precioUnitario == null || precioUnitario.signum() < 0) {
            throw new IllegalArgumentException("El precio unitario no puede ser negativo");
        }
    }
}
