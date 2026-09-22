package com.garagecoffee.model;

import java.time.LocalDate;

public class Menu {

    private int id;
    private String nombre;
    private boolean activo;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    public Menu() {
    }

    public Menu(int id,
                String nombre,
                boolean activo,
                LocalDate fechaInicio,
                LocalDate fechaFin) {

        this.id = id;
        this.nombre = nombre;
        this.activo = activo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public void activar() {
        this.activo = true;
    }

    public void desactivar() {
        this.activo = false;
    }
}