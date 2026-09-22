package com.garagecoffee.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Compra {

    private int id;
    private LocalDateTime fecha;
    private BigDecimal total;
    private EstadoCompra estado;

    private Proveedor proveedor;
    private List<DetalleCompra> detalles;

    public Compra() {
        detalles = new ArrayList<>();
    }

    public Compra(int id,
                  LocalDateTime fecha,
                  BigDecimal total,
                  EstadoCompra estado,
                  Proveedor proveedor) {

        this.id = id;
        this.fecha = fecha;
        this.total = total;
        this.estado = estado;
        this.proveedor = proveedor;
        this.detalles = new ArrayList<>();
    }

    public void agregarDetalle(DetalleCompra detalle) {
        detalles.add(detalle);
    }

    public void calcularTotal() {

        total = BigDecimal.ZERO;

        for (DetalleCompra detalle : detalles) {
            total = total.add(detalle.calcularSubtotal());
        }
    }

    public void registrarCompra() {
        estado = EstadoCompra.RECIBIDA;
        calcularTotal();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public EstadoCompra getEstado() {
        return estado;
    }

    public void setEstado(EstadoCompra estado) {
        this.estado = estado;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public List<DetalleCompra> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleCompra> detalles) {
        this.detalles = detalles;
    }
}