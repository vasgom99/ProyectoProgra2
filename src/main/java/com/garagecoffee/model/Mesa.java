package com.garagecoffee.model;

public class Mesa {

    private int id;
    private int numero;
    private int capacidad;
    private EstadoMesa estado;

    public Mesa() {
        this.estado = EstadoMesa.LIBRE;
    }

    public Mesa(int id, int numero, int capacidad, EstadoMesa estado) {
        this.id = id;
        this.numero = numero;
        validarCapacidad(capacidad);
        this.capacidad = capacidad;
        this.estado = estado == null ? EstadoMesa.LIBRE : estado;
    }

    public void ocupar() {
        if (estado == EstadoMesa.FUERA_SERVICIO) {
            throw new IllegalStateException("No se puede ocupar una mesa fuera de servicio");
        }
        if (estado == EstadoMesa.OCUPADA) {
            throw new IllegalStateException("La mesa ya está ocupada");
        }
        estado = EstadoMesa.OCUPADA;
    }

    public void liberar() {
        estado = EstadoMesa.LIBRE;
    }

    public void cambiarEstado(EstadoMesa nuevoEstado) {
        if (nuevoEstado == null) {
            throw new IllegalArgumentException("El estado de la mesa es obligatorio");
        }
        estado = nuevoEstado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        validarCapacidad(capacidad);
        this.capacidad = capacidad;
    }

    public EstadoMesa getEstado() {
        return estado;
    }

    public void setEstado(EstadoMesa estado) {
        this.estado = estado;
    }

    private static void validarCapacidad(int capacidad) {
        if (capacidad <= 0) {
            throw new IllegalArgumentException("La capacidad debe ser mayor que cero");
        }
    }
}
