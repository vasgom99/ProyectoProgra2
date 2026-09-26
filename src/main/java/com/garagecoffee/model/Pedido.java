package com.garagecoffee.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Pedido {

    private int id;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaProgramada;
    private EstadoPedido estado;
    private TipoEntrega tipoEntrega;
    private BigDecimal total;
    private String observaciones;
    private Cliente cliente;
    private final List<DetallePedido> detalles;

    protected Pedido(int id, TipoEntrega tipoEntrega, String observaciones) {
        this(id, tipoEntrega, observaciones, null);
    }

    protected Pedido(
            int id,
            TipoEntrega tipoEntrega,
            String observaciones,
            LocalDateTime fechaProgramada) {
        this.id = id;
        this.fechaCreacion = LocalDateTime.now();
        this.fechaProgramada = fechaProgramada;
        this.estado = EstadoPedido.PENDIENTE;
        this.tipoEntrega = tipoEntrega;
        this.total = BigDecimal.ZERO;
        this.observaciones = observaciones;
        this.detalles = new ArrayList<>();
    }

    public BigDecimal calcularTotal() {
        total = detalles.stream()
                .map(DetallePedido::calcularSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return total;
    }

    public void agregarDetalle(DetallePedido detalle) {
        if (detalle == null) {
            throw new IllegalArgumentException("El detalle del pedido es obligatorio");
        }
        detalles.add(detalle);
        calcularTotal();
    }

    public void eliminarDetalle(DetallePedido detalle) {
        detalles.remove(detalle);
        calcularTotal();
    }

    public void cambiarEstado(EstadoPedido nuevoEstado) {
        if (nuevoEstado == null) {
            throw new IllegalArgumentException("El estado del pedido es obligatorio");
        }
        this.estado = nuevoEstado;
    }

    public void cancelar() {
        this.estado = EstadoPedido.CANCELADO;
    }

    public int getId() {
        return id;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public LocalDateTime getFechaProgramada() {
        return fechaProgramada;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public TipoEntrega getTipoEntrega() {
        return tipoEntrega;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public List<DetallePedido> getDetalles() {
        return Collections.unmodifiableList(detalles);
    }

    public void setId(int id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public final void setFechaProgramada(LocalDateTime fechaProgramada) {
        this.fechaProgramada = fechaProgramada;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
