package com.mycompany.proyecto2progra2.model;

public class PedidoLocal extends Pedido {
    private int ordenLlegada;

    public PedidoLocal(int id, String observaciones, int ordenLlegada) {
        super(id, TipoEntrega.CONSUMIR_LOCAL, observaciones);
        this.ordenLlegada = ordenLlegada;
    }

    @Override
    public void calcularTotal() {
        // El total se calculará sumando los productos más adelante
        double subtotalProductos = 0.0;
        setTotal(subtotalProductos);
    }

    public void asignarMesa() {
        // Aquí irá la lógica para vincular una Mesa cuando la creemos
    }
}