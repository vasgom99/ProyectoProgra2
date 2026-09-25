package com.garagecoffee.model;

import java.time.LocalDateTime;

public class PedidoRecoger extends Pedido {

    private LocalDateTime horaProgramada;

    public PedidoRecoger(
            int id,
            String observaciones,
            LocalDateTime horaProgramada) {
        super(id, TipoEntrega.RECOGER, observaciones);
        this.horaProgramada = horaProgramada;
    }

    @Override
    public void calcularTotal() {
        double subtotalProductos = 0.0;
        setTotal(subtotalProductos);
    }

    public void confirmarListo() {
        cambiarEstado(EstadoPedido.LISTO);
    }

    public LocalDateTime getHoraProgramada() {
        return horaProgramada;
    }
}
