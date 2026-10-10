package com.garagecoffee.service;

import com.garagecoffee.model.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Datos compartidos: la versión MySQL guarda cambios y la demo permite pruebas sin conexión.
public abstract class DatosRestaurante {
    final List<Empleado> empleados = new ArrayList<>();
    final List<Categoria> categorias = new ArrayList<>();
    final List<Producto> productos = new ArrayList<>();
    final List<Mesa> mesas = new ArrayList<>();
    final List<Pedido> pedidos = new ArrayList<>();
    final List<Pago> pagos = new ArrayList<>();
    final List<Caja> cajas = new ArrayList<>();
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
    <T> T transaccion(java.util.function.Supplier<T> accion) { return accion.get(); }
}
