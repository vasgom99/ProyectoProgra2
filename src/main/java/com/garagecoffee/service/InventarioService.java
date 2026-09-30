package com.garagecoffee.service;

import com.garagecoffee.model.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// Controla existencias, compras y pérdidas sin permitir cantidades negativas.
public class InventarioService {
    private final DatosRestaurante datos;
    private final SesionService sesion;

    public InventarioService(DatosRestaurante datos, SesionService sesion) { this.datos = datos; this.sesion = sesion; }
    public List<Ingrediente> ingredientes() { sesion.actual(); return List.copyOf(datos.ingredientes); }
    public List<Proveedor> proveedores() { sesion.actual(); return List.copyOf(datos.proveedores); }
    public List<MovimientoStock> movimientos() { sesion.exigir(RolEmpleado.COCINA); return List.copyOf(datos.movimientosStock); }
    public List<Perdida> perdidas() { sesion.exigir(RolEmpleado.COCINA); return List.copyOf(datos.perdidas); }
    public List<Compra> compras() { sesion.exigir(); return List.copyOf(datos.compras); }

    public void guardarIngrediente(int id, String nombre, String unidad, BigDecimal minimo, BigDecimal costo) {
        datos.transaccion(() -> { guardarIngredienteInterno(id, nombre, unidad, minimo, costo); return null; });
    }
    private void guardarIngredienteInterno(int id, String nombre, String unidad, BigDecimal minimo, BigDecimal costo) {
        sesion.exigir();
        nombre = Validacion.texto(nombre, "Nombre", 120);
        unidad = Validacion.texto(unidad, "Unidad", 30);
        minimo = Validacion.numero(minimo, "Stock mínimo", 3, false);
        costo = Validacion.numero(costo, "Costo unitario", 4, false);
        Ingrediente ingrediente = id == 0 ? new Ingrediente() : datos.ingredientes.stream().filter(i -> i.getId() == id).findFirst().orElseThrow();
        if (id == 0) {
            ingrediente.setId(datos.siguienteId(Ingrediente.class));
            ingrediente.setStockActual(BigDecimal.ZERO);
        } else {
            boolean usado = ingrediente.getStockActual().signum() != 0
                    || datos.recetas.stream().anyMatch(r -> r.getIngrediente() == ingrediente)
                    || datos.movimientosStock.stream().anyMatch(m -> m.getIngrediente() == ingrediente)
                    || datos.compras.stream().flatMap(c -> c.getDetalles().stream()).anyMatch(d -> d.getIngrediente() == ingrediente);
            Validacion.exigir(ingrediente.getUnidadMedida().equals(unidad) || !usado,
                    "No cambies la unidad si el ingrediente tiene existencias, recetas o historial.");
        }
        ingrediente.setNombre(nombre); ingrediente.setUnidadMedida(unidad);
        ingrediente.setStockMinimo(minimo); ingrediente.setCostoUnitario(costo);
        datos.guardar(ingrediente);
        if (id == 0) datos.ingredientes.add(ingrediente);
    }

    public void eliminarIngrediente(Ingrediente ingrediente) {
        datos.transaccion(() -> { eliminarIngredienteInterno(ingrediente); return null; });
    }
    private void eliminarIngredienteInterno(Ingrediente ingrediente) {
        sesion.exigir();
        Validacion.exigir(ingrediente.getStockActual().signum() == 0, "El ingrediente todavía tiene existencias.");
        boolean usado = datos.recetas.stream().anyMatch(r -> r.getIngrediente() == ingrediente)
                || datos.movimientosStock.stream().anyMatch(m -> m.getIngrediente() == ingrediente)
                || datos.compras.stream().flatMap(c -> c.getDetalles().stream()).anyMatch(d -> d.getIngrediente() == ingrediente);
        Validacion.exigir(!usado, "El ingrediente tiene recetas o movimientos y no puede eliminarse.");
        datos.eliminar(ingrediente); datos.ingredientes.remove(ingrediente);
    }

    public void guardarProveedor(int id, String nombre, String telefono, String email, String direccion, boolean activo) {
        datos.transaccion(() -> { guardarProveedorInterno(id, nombre, telefono, email, direccion, activo); return null; });
    }
    private void guardarProveedorInterno(int id, String nombre, String telefono, String email, String direccion, boolean activo) {
        sesion.exigir();
        nombre = Validacion.texto(nombre, "Nombre", 150);
        telefono = Validacion.opcional(telefono, "Teléfono", 30);
        email = Validacion.opcional(email, "Correo", 150);
        direccion = Validacion.opcional(direccion, "Dirección", 255);
        Proveedor proveedor = id == 0 ? new Proveedor() : datos.proveedores.stream().filter(p -> p.getId() == id).findFirst().orElseThrow();
        if (id == 0) proveedor.setId(datos.siguienteId(Proveedor.class));
        proveedor.setNombre(nombre); proveedor.setTelefono(telefono); proveedor.setEmail(email);
        proveedor.setDireccion(direccion); proveedor.setActivo(activo);
        datos.guardar(proveedor);
        if (id == 0) datos.proveedores.add(proveedor);
    }

    public void eliminarProveedor(Proveedor proveedor) {
        datos.transaccion(() -> { eliminarProveedorInterno(proveedor); return null; });
    }
    private void eliminarProveedorInterno(Proveedor proveedor) {
        sesion.exigir();
        Validacion.exigir(datos.compras.stream().noneMatch(c -> c.getProveedor() == proveedor), "El proveedor tiene compras; puedes desactivarlo.");
        datos.eliminar(proveedor); datos.proveedores.remove(proveedor);
    }

    public void registrarMovimiento(Ingrediente ingrediente, TipoMovimientoStock tipo, BigDecimal cantidad, String motivo) {
        datos.transaccion(() -> { registrarMovimientoInterno(ingrediente, tipo, cantidad, motivo); return null; });
    }
    private void registrarMovimientoInterno(Ingrediente ingrediente, TipoMovimientoStock tipo, BigDecimal cantidad, String motivo) {
        sesion.exigir();
        Validacion.exigir(datos.ingredientes.contains(ingrediente) && tipo != null, "Selecciona ingrediente y tipo.");
        cantidad = Validacion.numero(cantidad, "Cantidad", 3, tipo != TipoMovimientoStock.AJUSTE);
        motivo = Validacion.texto(motivo, "Motivo", 255);
        BigDecimal diferencia = switch (tipo) {
            case ENTRADA -> cantidad;
            case SALIDA, MERMA -> cantidad.negate();
            case AJUSTE -> cantidad.subtract(ingrediente.getStockActual());
        };
        Validacion.exigir(ingrediente.getStockActual().add(diferencia).signum() >= 0, "No hay suficiente stock.");
        Validacion.exigir(diferencia.signum() != 0, "El ajuste no cambia las existencias.");
        ingrediente.setStockActual(ingrediente.getStockActual().add(diferencia));
        datos.guardar(ingrediente);
        movimiento(ingrediente, tipo, tipo == TipoMovimientoStock.AJUSTE ? diferencia : cantidad, motivo);
        if (tipo == TipoMovimientoStock.MERMA) {
            Perdida perdida = new Perdida(datos.siguienteId(Perdida.class), cantidad, motivo, LocalDateTime.now(), ingrediente);
            datos.guardar(perdida); datos.perdidas.add(perdida);
        }
    }

    private void movimiento(Ingrediente ingrediente, TipoMovimientoStock tipo, BigDecimal cantidad, String motivo) {
        BigDecimal costo = cantidad.abs().multiply(ingrediente.getCostoUnitario()).setScale(2, RoundingMode.HALF_UP);
        MovimientoStock movimiento = new MovimientoStock(datos.siguienteId(MovimientoStock.class), tipo,
                cantidad, costo, LocalDateTime.now(), motivo, ingrediente);
        datos.guardar(movimiento); datos.movimientosStock.add(movimiento);
    }

    public Compra guardarCompra(Proveedor proveedor, List<DetalleCompra> detalles) {
        return datos.transaccion(() -> guardarCompraInterno(proveedor, detalles));
    }
    private Compra guardarCompraInterno(Proveedor proveedor, List<DetalleCompra> detalles) {
        return guardarCompraInterno(proveedor, detalles, false, false, null);
    }

    // Guarda compra, recepción y pago confirmado como una sola operación.
    public Compra registrarCompra(Proveedor proveedor, List<DetalleCompra> detalles, boolean recibida,
                                 boolean pagarEnCaja, FinanzasService finanzas) {
        return datos.transaccion(() -> guardarCompraInterno(proveedor, detalles, recibida, pagarEnCaja, finanzas));
    }
    private Compra guardarCompraInterno(Proveedor proveedor, List<DetalleCompra> detalles, boolean recibida,
                                       boolean pagarEnCaja, FinanzasService finanzas) {
        sesion.exigir();
        Validacion.exigir(!pagarEnCaja || recibida, "El pago de esta pantalla corresponde a mercadería recibida.");
        Validacion.exigir(datos.proveedores.contains(proveedor) && proveedor.isActivo(), "Selecciona un proveedor activo.");
        Validacion.exigir(detalles != null && !detalles.isEmpty(), "Agrega al menos un ingrediente.");
        for (DetalleCompra detalle : detalles) {
            Validacion.exigir(detalle != null && datos.ingredientes.contains(detalle.getIngrediente()), "Ingrediente inválido.");
            Validacion.numero(detalle.getCantidad(), "Cantidad", 3, true);
            Validacion.numero(detalle.getCostoUnitario(), "Costo unitario", 4, false);
        }
        Compra compra = new Compra(0, LocalDateTime.now(), BigDecimal.ZERO, EstadoCompra.PENDIENTE, proveedor);
        for (DetalleCompra detalle : detalles) {
            compra.agregarDetalle(new DetalleCompra(detalle.getCantidad(), detalle.getCostoUnitario(), detalle.getIngrediente()));
        }
        compra.calcularTotal();
        Validacion.dinero(compra.getTotal(), "Total", false);
        if (pagarEnCaja) {
            Validacion.exigir(finanzas != null, "No se pudo consultar la caja.");
            finanzas.validarSalida(compra.getTotal());
        }
        compra.setId(datos.siguienteId(Compra.class));
        for (DetalleCompra detalle : compra.getDetalles()) {
            detalle.setId(datos.siguienteId(DetalleCompra.class)); detalle.setCompraId(compra.getId());
        }
        datos.guardar(compra); datos.compras.add(compra);
        if (recibida) recibirCompraInterno(compra);
        if (pagarEnCaja) finanzas.registrarMovimiento(TipoMovimientoCaja.EGRESO,
                "Pago compra #" + compra.getId(), compra.getTotal());
        return compra;
    }

    public void recibirCompra(Compra compra) {
        datos.transaccion(() -> { recibirCompraInterno(compra); return null; });
    }
    private void recibirCompraInterno(Compra compra) {
        sesion.exigir();
        Validacion.exigir(datos.compras.contains(compra) && compra.getEstado() == EstadoCompra.PENDIENTE,
                "Solo puedes recibir una compra pendiente.");
        for (DetalleCompra detalle : compra.getDetalles()) {
            Ingrediente ingrediente = detalle.getIngrediente();
            ingrediente.aumentarStock(detalle.getCantidad());
            ingrediente.setCostoUnitario(detalle.getCostoUnitario());
            datos.guardar(ingrediente);
            movimiento(ingrediente, TipoMovimientoStock.ENTRADA, detalle.getCantidad(), "Compra #" + compra.getId());
        }
        compra.registrarCompra(); datos.guardar(compra);
    }

    public void cancelarCompra(Compra compra) {
        datos.transaccion(() -> { cancelarCompraInterno(compra); return null; });
    }
    private void cancelarCompraInterno(Compra compra) {
        sesion.exigir();
        Validacion.exigir(datos.compras.contains(compra) && compra.getEstado() == EstadoCompra.PENDIENTE,
                "Solo puedes cancelar una compra pendiente; una recibida ya modificó el stock.");
        compra.setEstado(EstadoCompra.CANCELADA); datos.guardar(compra);
    }

    // Primero revisa todos los ingredientes y luego descuenta; nunca deja un descuento parcial.
    void consumirPedido(Pedido pedido) {
        Map<Ingrediente, BigDecimal> requeridos = new LinkedHashMap<>();
        for (DetallePedido detalle : pedido.getDetalles()) {
            List<DetalleReceta> receta = datos.recetas.stream().filter(r -> r.getProducto() == detalle.getProducto()).toList();
            Validacion.exigir(!receta.isEmpty(), "Falta la receta de " + detalle.getProducto().getNombre() + ".");
            for (DetalleReceta linea : receta) {
                BigDecimal cantidad = linea.getCantidadRequerida().multiply(BigDecimal.valueOf(detalle.getCantidad()));
                requeridos.merge(linea.getIngrediente(), cantidad, BigDecimal::add);
            }
        }
        for (var entrada : requeridos.entrySet()) {
            Validacion.exigir(entrada.getKey().getStockActual().compareTo(entrada.getValue()) >= 0,
                    "Stock insuficiente de " + entrada.getKey().getNombre() + ".");
        }
        BigDecimal costo = BigDecimal.ZERO;
        for (var entrada : requeridos.entrySet()) {
            Ingrediente ingrediente = entrada.getKey();
            ingrediente.descontarStock(entrada.getValue());
            datos.guardar(ingrediente);
            BigDecimal parcial = entrada.getValue().multiply(ingrediente.getCostoUnitario()).setScale(2, RoundingMode.HALF_UP);
            costo = costo.add(parcial);
            movimiento(ingrediente, TipoMovimientoStock.SALIDA, entrada.getValue(), "Preparación de pedido #" + pedido.getId());
        }
        datos.guardarCosto(pedido.getId(), costo);
    }
}
