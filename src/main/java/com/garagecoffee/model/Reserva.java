package com.garagecoffee.model;

import java.time.LocalDateTime;

public class Reserva {

    private int id;
    private LocalDateTime fechaHora;
    private int numeroPersonas;
    private EstadoReserva estado;
    private Cliente cliente;
    private Mesa mesa;

    public Reserva() {
        this.estado = EstadoReserva.PENDIENTE;
    }

    public Reserva(
            int id,
            LocalDateTime fechaHora,
            int numeroPersonas,
            Cliente cliente,
            Mesa mesa) {
        this();
        this.id = id;
        this.fechaHora = fechaHora;
        validarNumeroPersonas(numeroPersonas);
        this.numeroPersonas = numeroPersonas;
        this.cliente = cliente;
        this.mesa = mesa;
    }

    public void confirmar() {
        if (estado == EstadoReserva.CANCELADA || estado == EstadoReserva.FINALIZADA) {
            throw new IllegalStateException("La reserva ya no se puede confirmar");
        }
        if (cliente == null || mesa == null) {
            throw new IllegalStateException("La reserva debe tener cliente y mesa");
        }
        if (mesa.getEstado() != EstadoMesa.LIBRE
                && mesa.getEstado() != EstadoMesa.RESERVADA) {
            throw new IllegalStateException("La mesa no está disponible para reservar");
        }
        if (numeroPersonas > mesa.getCapacidad()) {
            throw new IllegalStateException("La cantidad de personas supera la capacidad de la mesa");
        }
        estado = EstadoReserva.CONFIRMADA;
        mesa.cambiarEstado(EstadoMesa.RESERVADA);
    }

    public void cancelar() {
        estado = EstadoReserva.CANCELADA;
        liberarMesaReservada();
    }

    public void finalizar() {
        estado = EstadoReserva.FINALIZADA;
        liberarMesaReservada();
    }

    private void liberarMesaReservada() {
        if (mesa != null && mesa.getEstado() == EstadoMesa.RESERVADA) {
            mesa.liberar();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public int getNumeroPersonas() {
        return numeroPersonas;
    }

    public void setNumeroPersonas(int numeroPersonas) {
        validarNumeroPersonas(numeroPersonas);
        this.numeroPersonas = numeroPersonas;
    }

    public EstadoReserva getEstado() {
        return estado;
    }

    public void setEstado(EstadoReserva estado) {
        this.estado = estado;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Mesa getMesa() {
        return mesa;
    }

    public void setMesa(Mesa mesa) {
        this.mesa = mesa;
    }

    private static void validarNumeroPersonas(int numeroPersonas) {
        if (numeroPersonas <= 0) {
            throw new IllegalArgumentException("El número de personas debe ser mayor que cero");
        }
    }
}
