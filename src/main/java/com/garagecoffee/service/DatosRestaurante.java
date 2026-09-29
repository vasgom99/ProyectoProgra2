package com.garagecoffee.service;

import com.garagecoffee.model.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Datos compartidos: la versión MySQL guarda cambios y la demo permite pruebas sin conexión.
public abstract class DatosRestaurante {
    final List<Empleado> empleados = new ArrayList<>();
    final List<Categoria> categorias = new ArrayList<>();
    final List<Producto> productos = new ArrayList<>();
    final List<Cliente> clientes = new ArrayList<>();
    final List<DireccionCliente> direcciones = new ArrayList<>();
    final List<Mesa> mesas = new ArrayList<>();
    final List<Reserva> reservas = new ArrayList<>();
    final List<Repartidor> repartidores = new ArrayList<>();
    final List<Pedido> pedidos = new ArrayList<>();
    final List<Menu> menus = new ArrayList<>();
    final List<DetalleMenu> detallesMenu = new ArrayList<>();
    final List<PlatoDelDia> platos = new ArrayList<>();
    final List<Ingrediente> ingredientes = new ArrayList<>();
    final List<DetalleReceta> recetas = new ArrayList<>();
    final List<MovimientoStock> movimientosStock = new ArrayList<>();
    final List<Perdida> perdidas = new ArrayList<>();
    final List<Proveedor> proveedores = new ArrayList<>();
    final List<Compra> compras = new ArrayList<>();
    final List<Pago> pagos = new ArrayList<>();
    final List<Factura> facturas = new ArrayList<>();
    final List<Caja> cajas = new ArrayList<>();
    final List<MovimientoCaja> movimientosCaja = new ArrayList<>();
    final List<Gasto> gastos = new ArrayList<>();
    final Map<Integer, BigDecimal> costosPedidos = new HashMap<>();
    private final Map<Class<?>, Integer> consecutivos = new HashMap<>();

    int siguienteId(Class<?> tipo) {
        int id = consecutivos.getOrDefault(tipo, 0) + 1;
        consecutivos.put(tipo, id);
        return id;
    }


    public boolean esDemo() { return true; }
    public void recargar() { }
    void guardar(Object registro) { }
    void eliminar(Object registro) { }
    void guardarCosto(int pedido, BigDecimal costo) { costosPedidos.put(pedido, costo); }
    <T> T transaccion(java.util.function.Supplier<T> accion) { return accion.get(); }
}
