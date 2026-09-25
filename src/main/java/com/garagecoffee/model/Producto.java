
package com.garagecoffee.model;
import java.math.BigDecimal;


public class Producto {
    
    private int id; 
    private String nombre; 
    private String descripcion;
    private String imagenUrl; 
    private BigDecimal precio; 
    private boolean disponible; 
    private Categoria categoria;
    
    public Producto(){
    }

    public Producto(int id, String nombre, String descripcion, String imagenUrl, BigDecimal precio, boolean disponible, Categoria categoria) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imagenUrl = imagenUrl;
        this.precio = precio;
        this.disponible = disponible;
        this.categoria = categoria; 
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public boolean isDisponible() {
        return disponible;
    }
    
    public Categoria getcategoria(){    
    return categoria; 
    }


    
    
    
    
    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }
    
    public void ActualizarPrecio(BigDecimal nuevoPrecio){
        this.precio = nuevoPrecio;
    }
    
    public void cambiarDisponibilidad(){
        this.disponible = !this.disponible;
    
    }
    
    public void setCategoria (Categoria categoria){   
    this.categoria = categoria; 
    }

    
}
