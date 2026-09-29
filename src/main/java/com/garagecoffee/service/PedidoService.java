package com.garagecoffee.service;

import com.garagecoffee.model.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

// Controla el ciclo del pedido y usa la herencia de sus tres tipos.
public class PedidoService {
    private final DatosRestaurante datos;
    private final SesionService sesion;
    private final CatalogoService catalogo;
    private final InventarioService inventario;
    public PedidoService(DatosRestaurante datos, SesionService sesion, CatalogoService catalogo, InventarioService inventario) {
        this.datos = datos; this.sesion = sesion; this.catalogo = catalogo; this.inventario = inventario;
    }
    public List<Pedido> pedidos() {
        sesion.actual();
        if (sesion.actual().getRol() == RolEmpleado.REPARTIDOR)
            return datos.pedidos.stream().filter(p -> p instanceof PedidoDomicilio).toList();
        return List.copyOf(datos.pedidos);
    }

    public Pedido guardar(int id, TipoEntrega tipo, Cliente cliente, Mesa mesa, String direccion,
                          BigDecimal envio, LocalDateTime fecha, String observaciones, List<DetallePedido> detalles) {
        return datos.transaccion(() -> guardarInterno(id, tipo, cliente, mesa, direccion, envio, fecha, observaciones, detalles));
    }
    private Pedido guardarInterno(int id, TipoEntrega tipo, Cliente cliente, Mesa mesa, String direccion,
                          BigDecimal envio, LocalDateTime fecha, String observaciones, List<DetallePedido> detalles) {
        sesion.exigir(RolEmpleado.MESERO, RolEmpleado.CAJERO);
        Pedido anterior = id == 0 ? null : datos.pedidos.stream().filter(p -> p.getId() == id).findFirst().orElseThrow();
        Validacion.exigir(anterior == null || (anterior.getEstado() == EstadoPedido.PENDIENTE
                && datos.pagos.stream().noneMatch(p -> p.getPedido() == anterior)), "Solo se puede editar un pedido pendiente sin pagos.");
        Validacion.exigir(tipo != null, "Selecciona el tipo de entrega.");
        Validacion.exigir(cliente == null || datos.clientes.contains(cliente), "Cliente inválido.");
        Validacion.exigir(tipo == TipoEntrega.CONSUMIR_LOCAL || cliente != null, "Para recoger o enviar se requiere un cliente.");
        Validacion.exigir(fecha == null || fecha.isAfter(LocalDateTime.now()), "La hora programada debe ser futura.");
        observaciones = Validacion.opcional(observaciones, "Observaciones", 255);
        Validacion.exigir(detalles != null && !detalles.isEmpty(), "Agrega productos al pedido.");
        if (tipo == TipoEntrega.CONSUMIR_LOCAL) {
            boolean mismaMesa = anterior instanceof PedidoLocal local && local.getMesa() == mesa;
            Validacion.exigir(datos.mesas.contains(mesa) && (mesa.getEstado() == EstadoMesa.LIBRE || mismaMesa), "Selecciona una mesa libre.");
            LocalDateTime ahora = LocalDateTime.now();
            Validacion.exigir(datos.reservas.stream().noneMatch(r -> r.getMesa() == mesa && AtencionService.vigente(r)
                    && r.getCliente() != cliente && r.getFechaHora().isBefore(ahora.plusMinutes(90))
                    && r.getFechaHora().plusMinutes(90).isAfter(ahora)), "La mesa tiene una reserva próxima de otro cliente.");
        }
        Pedido pedido = switch (tipo) {
            case CONSUMIR_LOCAL -> {
                PedidoLocal local = new PedidoLocal(id, observaciones, anterior instanceof PedidoLocal p ? p.getOrdenLlegada() : datos.pedidos.size() + 1);
                local.setMesa(mesa);
                yield local;
            }
            case RECOGER -> new PedidoRecoger(id, observaciones, fecha);
            case DOMICILIO -> new PedidoDomicilio(id, observaciones,
                    Validacion.texto(direccion, "Dirección de entrega", 255), Validacion.dinero(envio, "Envío", false));
        };
        pedido.setCliente(cliente); pedido.setFechaProgramada(fecha);
        for (DetallePedido detalle : detalles) {
            Validacion.exigir(detalle != null && datos.productos.contains(detalle.getProducto()), "Producto inválido.");
            Producto producto = detalle.getProducto();
            Validacion.exigir(producto.isDisponible() && producto.getcategoria().isActivo(), producto.getNombre() + " no está disponible.");
            Validacion.exigir(detalle.getCantidad() > 0 && detalle.getCantidad() <= 9999, "La cantidad debe estar entre 1 y 9999.");
            pedido.agregarDetalle(new DetallePedido(0, detalle.getCantidad(), catalogo.precioVigente(producto), producto));
        }
        Validacion.dinero(pedido.calcularTotal(), "Total del pedido", true);
        if (anterior == null) pedido.setId(datos.siguienteId(Pedido.class));
        else { pedido.setFechaCreacion(anterior.getFechaCreacion()); liberarMesa(anterior); }
        if (pedido instanceof PedidoRecoger recoger) recoger.setCodigoRetiro("R-" + pedido.getId());
        for (DetallePedido detalle : pedido.getDetalles()) {
            detalle.setId(datos.siguienteId(DetallePedido.class)); detalle.setPedidoId(pedido.getId());
        }
        datos.guardar(pedido);
        if (pedido instanceof PedidoRecoger recoger) {
            recoger.setCodigoRetiro("R-" + pedido.getId()); datos.guardar(pedido);
        }
        if (pedido instanceof PedidoLocal local) { local.getMesa().setEstado(EstadoMesa.OCUPADA); datos.guardar(local.getMesa()); }
        if (anterior == null) datos.pedidos.add(pedido); else datos.pedidos.set(datos.pedidos.indexOf(anterior), pedido);
        return pedido;
    }

    public void preparar(Pedido pedido) {
        datos.transaccion(() -> { prepararInterno(pedido); return null; });
    }
    private void prepararInterno(Pedido pedido) {
        sesion.exigir(RolEmpleado.COCINA);
        estado(pedido, EstadoPedido.PENDIENTE);
        inventario.consumirPedido(pedido);
        pedido.setEstado(EstadoPedido.EN_PREPARACION); datos.guardar(pedido);
    }
    public void listo(Pedido pedido) {
        datos.transaccion(() -> { listoInterno(pedido); return null; });
    }
    private void listoInterno(Pedido pedido) {
        sesion.exigir(RolEmpleado.COCINA);
        estado(pedido, EstadoPedido.EN_PREPARACION);
        pedido.setEstado(EstadoPedido.LISTO); datos.guardar(pedido);
    }
    public void asignar(Pedido pedido, Repartidor repartidor) {
        datos.transaccion(() -> { asignarInterno(pedido, repartidor); return null; });
    }
    private void asignarInterno(Pedido pedido, Repartidor repartidor) {
        sesion.exigir(RolEmpleado.MESERO, RolEmpleado.CAJERO);
        Validacion.exigir(datos.pedidos.contains(pedido) && pedido instanceof PedidoDomicilio, "Selecciona un pedido a domicilio.");
        Validacion.exigir(pedido.getEstado() != EstadoPedido.CANCELADO && pedido.getEstado() != EstadoPedido.ENTREGADO, "El pedido ya terminó.");
        Validacion.exigir(datos.repartidores.contains(repartidor) && repartidor.isActivo(), "Selecciona un repartidor activo.");
        ((PedidoDomicilio) pedido).asignarRepartidor(repartidor); datos.guardar(pedido);
    }
    public void entregar(Pedido pedido) {
        datos.transaccion(() -> { entregarInterno(pedido); return null; });
    }
    private void entregarInterno(Pedido pedido) {
        sesion.exigir(RolEmpleado.MESERO, RolEmpleado.CAJERO, RolEmpleado.REPARTIDOR);
        estado(pedido, EstadoPedido.LISTO);
        Validacion.exigir(sesion.actual().getRol() != RolEmpleado.REPARTIDOR || pedido instanceof PedidoDomicilio, "Este rol solo entrega pedidos a domicilio.");
        if (pedido instanceof PedidoDomicilio domicilio)
            Validacion.exigir(domicilio.getRepartidor() != null && domicilio.getRepartidor().isActivo(), "Asigna primero un repartidor activo.");
        pedido.setEstado(EstadoPedido.ENTREGADO); datos.guardar(pedido);
        liberarSiPagado(pedido);
    }
    public void cancelar(Pedido pedido) {
        datos.transaccion(() -> { cancelarInterno(pedido); return null; });
    }
    private void cancelarInterno(Pedido pedido) {
        sesion.exigir(RolEmpleado.MESERO, RolEmpleado.CAJERO);
        estado(pedido, EstadoPedido.PENDIENTE);
        Validacion.exigir(datos.pagos.stream().noneMatch(p -> p.getPedido() == pedido), "El pedido tiene pagos. Las devoluciones no están habilitadas.");
        pedido.cancelar(); datos.guardar(pedido); liberarMesa(pedido);
    }
    private void estado(Pedido pedido, EstadoPedido esperado) {
        Validacion.exigir(datos.pedidos.contains(pedido) && pedido.getEstado() == esperado, "El pedido debe estar en estado " + esperado + ".");
    }
    void liberarSiPagado(Pedido pedido) {
        BigDecimal pagado = datos.pagos.stream().filter(p -> p.getPedido() == pedido).map(Pago::getMonto).reduce(BigDecimal.ZERO, BigDecimal::add);
        if (pedido.getEstado() == EstadoPedido.ENTREGADO && pagado.compareTo(pedido.getTotal()) >= 0) liberarMesa(pedido);
    }
    private void liberarMesa(Pedido pedido) {
        if (pedido instanceof PedidoLocal local && local.getMesa() != null) { local.getMesa().liberar(); datos.guardar(local.getMesa()); }
    }
}
