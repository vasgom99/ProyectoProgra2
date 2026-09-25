package com.garagecoffee.model;

public class PedidoDomicilio extends Pedido {

    private String direccionEntrega;
    private double costoEnvio;
    private Repartidor repartidor;

    public PedidoDomicilio(
            int id,
            String observaciones,
            String direccionEntrega,
            double costoEnvio) {
        super(id, TipoEntrega.DOMICILIO, observaciones);
        this.direccionEntrega = direccionEntrega;
        this.costoEnvio = costoEnvio;
    }

    @Override
    public void calcularTotal() {
        double subtotalProductos = 0.0;
        setTotal(subtotalProductos + costoEnvio);
    }

    public void asignarRepartidor(Repartidor repartidor) {
        this.repartidor = repartidor;
    }

    public String getDireccionEntrega() {
        return direccionEntrega;
    }

    public double getCostoEnvio() {
        return costoEnvio;
    }

    public Repartidor getRepartidor() {
        return repartidor;
    }
}
