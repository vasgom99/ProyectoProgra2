package com.garagecoffee.model;

public class Repartidor {

    private int id;
    private String nombre;
    private String telefono;
    private String vehiculo;
    private boolean activo;

    public Repartidor() {
        this.activo = true;
    }

    public Repartidor(int id, String nombre, String telefono, String vehiculo) {
        this();
        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
        this.vehiculo = vehiculo;
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

    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setVehiculo(String vehiculo) {
        this.vehiculo = vehiculo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
