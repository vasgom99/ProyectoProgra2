package com.garagecoffee.model;

public class DireccionCliente {

    private int id;
    private int clienteId;
    private String direccion;
    private String referencia;

    public DireccionCliente() {
    }

    public DireccionCliente(int id, String direccion, String referencia) {
        this.id = id;
        this.direccion = direccion;
        this.referencia = referencia;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }
}
