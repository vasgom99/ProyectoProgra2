package com.garagecoffee.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Base del pedido: agrupa sus detalles y calcula el total.
public abstract class Pedido {
    private int id;
    private LocalDateTime fechaCreacion;
    private EstadoPedido estado;
    private BigDecimal total;
    private String observaciones;
    private final List<DetallePedido> detalles = new ArrayList<>();

    protected Pedido(int id, String observaciones) {
        this.id = id;
        this.fechaCreacion = LocalDateTime.now();
        this.estado = EstadoPedido.PENDIENTE;
        this.total = BigDecimal.ZERO;
        this.observaciones = observaciones;
    }

    public BigDecimal calcularTotal() {
        total = detalles.stream().map(DetallePedido::calcularSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return total;
    }

    public void agregarDetalle(DetallePedido detalle) {
        if (detalle == null) throw new IllegalArgumentException("El detalle del pedido es obligatorio.");
        detalles.add(detalle);
        calcularTotal();
    }

    public void cancelar() { estado = EstadoPedido.CANCELADO; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public EstadoPedido getEstado() { return estado; }
    public void setEstado(EstadoPedido estado) { this.estado = estado; }
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
    public String getObservaciones() { return observaciones; }
    public List<DetallePedido> getDetalles() { return Collections.unmodifiableList(detalles); }
}
