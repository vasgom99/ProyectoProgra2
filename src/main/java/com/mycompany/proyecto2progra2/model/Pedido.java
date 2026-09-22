
package com.mycompany.proyecto2progra2.model;
import java.time.LocalDateTime;

/**
 *
 * @author D4vid
 */
// Se declara como 'abstract' porque es una clase padre
public abstract class Pedido {
    private int id;
    private LocalDateTime fechaHora;
    private EstadoPedido estado;
    private TipoEntrega tipoEntrega;
    private double total;
    private String observaciones;

    // Constructor
    public Pedido(int id, TipoEntrega tipoEntrega, String observaciones) {
        this.id = id;
        this.fechaHora = LocalDateTime.now(); 
        this.estado = EstadoPedido.PENDIENTE; // Todo pedido nuevo nace como pendiente
        this.tipoEntrega = tipoEntrega;
        this.total = 0.0;
        this.observaciones = observaciones;
    }

    // Métodos que marca el UML
    public abstract void calcularTotal(); // Método abstracto para que los hijos lo programen

    public void cambiarEstado(EstadoPedido nuevoEstado) {
        this.estado = nuevoEstado;
    }

    public void cancelar() {
        this.estado = EstadoPedido.CANCELADO;
    }

    // Getters
    public int getId() { return id; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public EstadoPedido getEstado() { return estado; }
    public TipoEntrega getTipoEntrega() { return tipoEntrega; }
    public double getTotal() { return total; }
    public String getObservaciones() { return observaciones; }
    
    // Setter para el total
    public void setTotal(double total) { this.total = total; }
}
