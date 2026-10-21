package com.garagecoffee.model;

// representa un pedido de mesa.
public class PedidoLocal extends Pedido {
    private Mesa mesa;

    public PedidoLocal(int id, String observaciones, Mesa mesa) {
        super(id, observaciones);
        if (mesa == null) throw new IllegalArgumentException("La mesa es obligatoria.");
        this.mesa = mesa;
    }

    public Mesa getMesa() { return mesa; }
    public void setMesa(Mesa mesa) {
        if (mesa == null) throw new IllegalArgumentException("La mesa es obligatoria.");
        this.mesa = mesa;
    }
}
