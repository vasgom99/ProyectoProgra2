package com.garagecoffee.model;

import java.math.BigDecimal;

public class PedidoLocal extends Pedido {

    private int ordenLlegada;
    private Mesa mesa;

    public PedidoLocal(int id, String observaciones, int ordenLlegada) {
        super(id, TipoEntrega.CONSUMIR_LOCAL, observaciones);
        this.ordenLlegada = ordenLlegada;
    }

    @Override
    public BigDecimal calcularTotal() {
        return super.calcularTotal();
    }

    public void asignarMesa(Mesa mesa) {
        if (mesa == null) {
            throw new IllegalArgumentException("La mesa es obligatoria");
        }
        this.mesa = mesa;
        mesa.ocupar();
    }

    public int getOrdenLlegada() {
        return ordenLlegada;
    }

    public void setOrdenLlegada(int ordenLlegada) {
        this.ordenLlegada = ordenLlegada;
    }

    public Mesa getMesa() {
        return mesa;
    }

    public void setMesa(Mesa mesa) {
        this.mesa = mesa;
    }
}
