package com.garagecoffee.model;

import java.time.LocalDateTime;

public class Cliente {

    private int id;
    private String nombre;
    private String telefono;
    private String email;
    private LocalDateTime fechaRegistro;

    public Cliente(int id, String nombre, String telefono, String email) {
        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
        this.email = email;
        this.fechaRegistro = LocalDateTime.now();
    }

    public void actualizarDatos(String nuevoTelefono, String nuevoEmail) {
        this.telefono = nuevoTelefono;
        this.email = nuevoEmail;
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

    public String getEmail() {
        return email;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }
}
