package com.garagecoffee.controller;

import com.garagecoffee.service.*;

// Reúne los servicios que usan las pantallas y mantiene una sola sesión.
public class RestauranteController {    
    private final DatosRestaurante datos;
    private final SesionService sesion;
    private final CatalogoService catalogo;
    private final AtencionService atencion;
    private final InventarioService inventario;
    private final PedidoService pedidos;
    private final FinanzasService finanzas;
    private final ReporteService reportes;
    public RestauranteController() { this(new DatosMySQL()); }
    public RestauranteController(DatosRestaurante datos) {
        this.datos = datos;
        sesion = new SesionService(datos);
        catalogo = new CatalogoService(datos, sesion);
        atencion = new AtencionService(datos, sesion);
        inventario = new InventarioService(datos, sesion);
        pedidos = new PedidoService(datos, sesion, catalogo, inventario);
        finanzas = new FinanzasService(datos, sesion, pedidos);
        reportes = new ReporteService(datos, sesion);
    }
    public boolean esDemo() { return datos.esDemo(); }
    public void recargar() { datos.recargar(); }
    public SesionService sesion() { return sesion; }
    public CatalogoService catalogo() { return catalogo; }
    public AtencionService atencion() { return atencion; }
    public InventarioService inventario() { return inventario; }
    public PedidoService pedidos() { return pedidos; }
    public FinanzasService finanzas() { return finanzas; }
    public ReporteService reportes() { return reportes; }
}
