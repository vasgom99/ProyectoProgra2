package com.garagecoffee.service;

import com.garagecoffee.model.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

// mantiene los datos que usan los servicios.
public abstract class DatosRestaurante {
    final List<Empleado> empleados = new ArrayList<>();
    final List<Categoria> categorias = new ArrayList<>();
    final List<Producto> productos = new ArrayList<>();
    final List<Mesa> mesas = new ArrayList<>();
    final List<Pedido> pedidos = new ArrayList<>();
    final List<Pago> pagos = new ArrayList<>();
    final List<Caja> cajas = new ArrayList<>();
    abstract int siguienteId(Class<?> tipo);
    public abstract void recargar();
    abstract void guardar(Object registro);
    abstract void eliminar(Object registro);
    abstract <T> T transaccion(Supplier<T> accion);
}
