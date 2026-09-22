
package com.mycompany.proyecto2progra2.model;

/**
 *
 * @author D4vid
 */
public class Repartidor {
    private int id;
    private String nombre;
    private String telefono;
    private String vehiculo;
    private boolean activo;

    // Constructor
    public Repartidor(int id, String nombre, String telefono, String vehiculo) {
        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
        this.vehiculo = vehiculo;
        this.activo = true; // Por defecto, el repartidor está activo al registrarse
    }

    // Método del UML para cambiar su disponibilidad (de activo a inactivo)
    public void cambiarEstado() {
        this.activo = !this.activo; 
    }

    // Getters para obtener la información
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getTelefono() { return telefono; }
    public String getVehiculo() { return vehiculo; }
    public boolean isActivo() { return activo; }
}
