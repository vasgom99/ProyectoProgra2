package com.garagecoffee.model;

import java.math.BigDecimal;

public class PedidoDomicilio extends Pedido {

    private String direccionEntrega;
    private BigDecimal costoEnvio;
    private Repartidor repartidor;

    public PedidoDomicilio(
            int id,
            String observaciones,
            String direccionEntrega,
            double costoEnvio) {
        this(id, observaciones, direccionEntrega, BigDecimal.valueOf(costoEnvio));
    }

    public PedidoDomicilio(
            int id,
            String observaciones,
            String direccionEntrega,
            BigDecimal costoEnvio) {
        super(id, TipoEntrega.DOMICILIO, observaciones);
        this.direccionEntrega = direccionEntrega;
        validarCostoEnvio(costoEnvio);
        this.costoEnvio = costoEnvio;
    }

    @Override
    public BigDecimal calcularTotal() {
        BigDecimal totalConEnvio = super.calcularTotal().add(costoEnvio);
        setTotal(totalConEnvio);
        return totalConEnvio;
    }

    public void asignarRepartidor(Repartidor repartidor) {
        if (repartidor == null || !repartidor.isActivo()) {
            throw new IllegalArgumentException("El repartidor debe estar activo");
        }
        this.repartidor = repartidor;
    }

    public String getDireccionEntrega() {
        return direccionEntrega;
    }

    public void setDireccionEntrega(String direccionEntrega) {
        this.direccionEntrega = direccionEntrega;
    }

    public BigDecimal getCostoEnvio() {
        return costoEnvio;
    }

    public void setCostoEnvio(BigDecimal costoEnvio) {
        validarCostoEnvio(costoEnvio);
        this.costoEnvio = costoEnvio;
    }

    public Repartidor getRepartidor() {
        return repartidor;
    }

    // Permite recuperar pedidos históricos aunque el repartidor ya esté inactivo.
    public void setRepartidor(Repartidor repartidor) {
        this.repartidor = repartidor;
    }

    private static void validarCostoEnvio(BigDecimal costoEnvio) {
        if (costoEnvio == null || costoEnvio.signum() < 0) {
            throw new IllegalArgumentException("El costo de envío no puede ser negativo");
        }
    }
}
