
package com.garagecoffee.model;


public class Categoria {
    private int id; 
    private String nombre; 
    private TipoCategoria tipo; 
    private boolean activo; 
    
    public Categoria(){  
    }

    public Categoria(int id, String nombre, TipoCategoria tipo, boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.activo = activo;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public TipoCategoria getTipo() {
        return tipo;
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

    public void setTipo(TipoCategoria tipo) {
        this.tipo = tipo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
    
    
    
    
    
    
    
}
