package com.garagecoffee.model;

public class Repartidor {

    private int id;
    private String nombre;
    private String telefono;
    private String vehiculo;
    private boolean activo;

    public Repartidor(int id, String nombre, String telefono, String vehiculo) {
        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
        this.vehiculo = vehiculo;
        this.activo = true;
    }

    public void cambiarEstado() {
        this.activo = !this.activo;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getVehiculo() {
        return vehiculo;
    }

    public boolean isActivo() {
        return activo;
    }
}
