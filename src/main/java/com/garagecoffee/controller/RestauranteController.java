package com.garagecoffee.controller;

import com.garagecoffee.service.*;

   

// reúne los servicios del sistema.
public class RestauranteController {

    private final DatosRestaurante datos;
    private final SesionService sesion;
    private final CatalogoService catalogo;
    private final AtencionService atencion;
    private final PedidoService pedidos;
    private final FinanzasService finanzas;
    public RestauranteController() { this(new DatosMySQL()); }
    public RestauranteController(DatosRestaurante datos) {
        this.datos = datos;
        sesion = new SesionService(datos);
        catalogo = new CatalogoService(datos, sesion);
        atencion = new AtencionService(datos, sesion);
        pedidos = new PedidoService(datos, sesion, catalogo);
        finanzas = new FinanzasService(datos, sesion, pedidos);
    }
    public void recargar() { datos.recargar(); }
    public SesionService sesion() { return sesion; }
    public CatalogoService catalogo() { return catalogo; }
    public AtencionService atencion() { return atencion; }
    public PedidoService pedidos() { return pedidos; }
    public FinanzasService finanzas() { return finanzas; }
}
