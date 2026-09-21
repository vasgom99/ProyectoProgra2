package com.mycompany.proyecto2progra2.model;

public class PedidoDomicilio extends Pedido {
    private String direccionEntrega;
    private double costoEnvio;
    private Repartidor repartidor; // Conecta con la clase Repartidor que hiciste antes

    // Constructor
    public PedidoDomicilio(int id, String observaciones, String direccionEntrega, double costoEnvio) {
        // El 'super' llama al constructor del padre y fija el tipo a DOMICILIO
        super(id, TipoEntrega.DOMICILIO, observaciones);
        this.direccionEntrega = direccionEntrega;
        this.costoEnvio = costoEnvio;
    }

    // Cumplimos con el método abstracto obligatorio de la clase Pedido
    @Override
    public void calcularTotal() {
        // Por ahora solo le sumamos el costo de envío. 
        // Más adelante la Persona 3 conectará aquí los precios del menú de cenas.
        double subtotalProductos = 0.0; 
        setTotal(subtotalProductos + costoEnvio);
    }

    // Método específico para asignar quién hará la entrega
    public void asignarRepartidor(Repartidor repartidor) {
        this.repartidor = repartidor;
    }

    // Getters
    public String getDireccionEntrega() { return direccionEntrega; }
    public double getCostoEnvio() { return costoEnvio; }
    public Repartidor getRepartidor() { return repartidor; }
}