package com.garagecoffee.service;

import com.garagecoffee.model.*;
import java.util.List;

// Lleva el pedido local desde la mesa hasta su entrega.
public class PedidoService {
    private final DatosRestaurante datos;
    private final SesionService sesion;
    private final CatalogoService catalogo;

    public PedidoService(DatosRestaurante datos, SesionService sesion, CatalogoService catalogo) {
        this.datos = datos;
        this.sesion = sesion;
        this.catalogo = catalogo;
    }

    public List<Pedido> pedidos() {
        sesion.actual();
        return List.copyOf(datos.pedidos);
    }

    public Pedido guardar(Mesa mesa, String observaciones, List<DetallePedido> detalles) {
        return datos.transaccion(() -> {
            sesion.exigir(RolEmpleado.MESERO, RolEmpleado.CAJERO);
            Validacion.exigir(datos.mesas.contains(mesa) && mesa.getEstado() == EstadoMesa.LIBRE,
                    "Selecciona una mesa libre.");
            String nota = Validacion.opcional(observaciones, "Observaciones", 255);
            Validacion.exigir(detalles != null && !detalles.isEmpty(), "Agrega productos al pedido.");
            PedidoLocal pedido = new PedidoLocal(datos.siguienteId(Pedido.class), nota, mesa);
            for (DetallePedido detalle : detalles) {
                Validacion.exigir(detalle != null && datos.productos.contains(detalle.getProducto()),
                        "Producto inválido.");
                Producto producto = detalle.getProducto();
                Validacion.exigir(producto.isDisponible() && producto.getcategoria().isActivo(),
                        producto.getNombre() + " no está disponible.");
                Validacion.exigir(detalle.getCantidad() > 0 && detalle.getCantidad() <= 9999,
                        "La cantidad debe estar entre 1 y 9999.");
                pedido.agregarDetalle(new DetallePedido(0, detalle.getCantidad(),
                        catalogo.precioVigente(producto), producto));
            }
            Validacion.dinero(pedido.calcularTotal(), "Total del pedido", true);
            for (DetallePedido detalle : pedido.getDetalles()) {
                detalle.setId(datos.siguienteId(DetallePedido.class));
                detalle.setPedidoId(pedido.getId());
            }
            datos.guardar(pedido);
            mesa.setEstado(EstadoMesa.OCUPADA);
            datos.guardar(mesa);
            datos.pedidos.add(pedido);
            return pedido;
        });
    }

    public void preparar(Pedido pedido) {
        cambiarEstado(pedido, EstadoPedido.PENDIENTE, EstadoPedido.EN_PREPARACION, RolEmpleado.COCINA);
    }

    public void listo(Pedido pedido) {
        cambiarEstado(pedido, EstadoPedido.EN_PREPARACION, EstadoPedido.LISTO, RolEmpleado.COCINA);
    }

    public void entregar(Pedido pedido) {
        datos.transaccion(() -> {
            sesion.exigir(RolEmpleado.MESERO, RolEmpleado.CAJERO);
            exigirEstado(pedido, EstadoPedido.LISTO);
            pedido.setEstado(EstadoPedido.ENTREGADO);
            datos.guardar(pedido);
            liberarSiPagado(pedido);
            return null;
        });
    }

    public void cancelar(Pedido pedido) {
        datos.transaccion(() -> {
            sesion.exigir(RolEmpleado.MESERO, RolEmpleado.CAJERO);
            exigirEstado(pedido, EstadoPedido.PENDIENTE);
            Validacion.exigir(datos.pagos.stream().noneMatch(p -> p.getPedido() == pedido),
                    "No se puede cancelar un pedido con pagos.");
            pedido.cancelar();
            datos.guardar(pedido);
            liberarMesa(pedido);
            return null;
        });
    }

    private void cambiarEstado(Pedido pedido, EstadoPedido esperado, EstadoPedido nuevo, RolEmpleado rol) {
        datos.transaccion(() -> {
            sesion.exigir(rol);
            exigirEstado(pedido, esperado);
            pedido.setEstado(nuevo);
            datos.guardar(pedido);
            return null;
        });
    }

    private void exigirEstado(Pedido pedido, EstadoPedido esperado) {
        Validacion.exigir(datos.pedidos.contains(pedido) && pedido.getEstado() == esperado,
                "El pedido debe estar en estado " + esperado + ".");
    }

    void liberarSiPagado(Pedido pedido) {
        var pagado = datos.pagos.stream().filter(p -> p.getPedido() == pedido)
                .map(Pago::getMonto).reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
        if (pedido.getEstado() == EstadoPedido.ENTREGADO && pagado.compareTo(pedido.getTotal()) >= 0)
            liberarMesa(pedido);
    }

    private void liberarMesa(Pedido pedido) {
        Mesa mesa = ((PedidoLocal) pedido).getMesa();
        mesa.liberar();
        datos.guardar(mesa);
    }
}
