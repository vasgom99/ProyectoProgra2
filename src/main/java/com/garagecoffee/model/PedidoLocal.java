package com.garagecoffee.model;

// Pedido atendido en una mesa del restaurante.
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
