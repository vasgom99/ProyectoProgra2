package com.garagecoffee.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Cliente {

    private int id;
    private String nombre;
    private String telefono;
    private String email;
    private LocalDateTime fechaRegistro;
    private final List<DireccionCliente> direcciones;

    public Cliente() {
        this.fechaRegistro = LocalDateTime.now();
        this.direcciones = new ArrayList<>();
    }

    public Cliente(int id, String nombre, String telefono, String email) {
        this();
        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
        this.email = email;
    }

    public void registrarse() {
        if (fechaRegistro == null) {
            fechaRegistro = LocalDateTime.now();
        }
    }

    public void actualizarDatos(String nuevoTelefono, String nuevoEmail) {
        this.telefono = nuevoTelefono;
        this.email = nuevoEmail;
    }

    public void agregarDireccion(DireccionCliente direccion) {
        if (direccion == null) {
            throw new IllegalArgumentException("La dirección es obligatoria");
        }
        direcciones.add(direccion);
    }

    public void eliminarDireccion(DireccionCliente direccion) {
        direcciones.remove(direccion);
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

    public List<DireccionCliente> getDirecciones() {
        return Collections.unmodifiableList(direcciones);
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

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
}
