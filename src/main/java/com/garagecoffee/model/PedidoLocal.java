package com.garagecoffee.model;

public class PedidoLocal extends Pedido {

    private int ordenLlegada;

    public PedidoLocal(int id, String observaciones, int ordenLlegada) {
        super(id, TipoEntrega.CONSUMIR_LOCAL, observaciones);
        this.ordenLlegada = ordenLlegada;
    }

    @Override
    public void calcularTotal() {
        double subtotalProductos = 0.0;
        setTotal(subtotalProductos);
    }

    public void asignarMesa() {
        // La relación con Mesa se agregará cuando exista ese modelo.
    }

    public int getOrdenLlegada() {
        return ordenLlegada;
    }
}
