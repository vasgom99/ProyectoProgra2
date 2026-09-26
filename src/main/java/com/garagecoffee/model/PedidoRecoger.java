package com.garagecoffee.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PedidoRecoger extends Pedido {

    private String codigoRetiro;

    public PedidoRecoger(
            int id,
            String observaciones,
            LocalDateTime horaProgramada) {
        this(id, observaciones, null, horaProgramada);
    }

    public PedidoRecoger(
            int id,
            String observaciones,
            String codigoRetiro,
            LocalDateTime horaProgramada) {
        super(id, TipoEntrega.RECOGER, observaciones, horaProgramada);
        this.codigoRetiro = codigoRetiro;
    }

    @Override
    public BigDecimal calcularTotal() {
        return super.calcularTotal();
    }

    public void confirmarListo() {
        cambiarEstado(EstadoPedido.LISTO);
    }

    public void confirmarRetiro() {
        cambiarEstado(EstadoPedido.ENTREGADO);
    }

    public String getCodigoRetiro() {
        return codigoRetiro;
    }

    public void setCodigoRetiro(String codigoRetiro) {
        this.codigoRetiro = codigoRetiro;
    }

    public LocalDateTime getHoraProgramada() {
        return getFechaProgramada();
    }

    public void setHoraProgramada(LocalDateTime horaProgramada) {
        setFechaProgramada(horaProgramada);
    }
}
