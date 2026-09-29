package com.garagecoffee.service;

import com.garagecoffee.model.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

// Registra caja, cobros y gastos; solo el efectivo modifica el dinero en caja.
public class FinanzasService {
    private final DatosRestaurante datos;
    private final SesionService sesion;
    private final PedidoService pedidos;
    public FinanzasService(DatosRestaurante datos, SesionService sesion, PedidoService pedidos) {
        this.datos = datos; this.sesion = sesion; this.pedidos = pedidos;
    }
    private void permiso() { sesion.exigir(RolEmpleado.CAJERO); }
    public List<Caja> cajas() { permiso(); return List.copyOf(datos.cajas); }
    public List<MovimientoCaja> movimientos() { permiso(); return List.copyOf(datos.movimientosCaja); }
    public List<Pago> pagos() { permiso(); return List.copyOf(datos.pagos); }
    public List<Factura> facturas() { permiso(); return List.copyOf(datos.facturas); }
    public List<Gasto> gastos() { permiso(); return List.copyOf(datos.gastos); }
    public Caja abierta() { permiso(); return datos.cajas.stream().filter(c -> c.getEstado() == EstadoCaja.ABIERTA).findFirst().orElse(null); }
    public BigDecimal saldo(Caja caja) {
        permiso();
        Validacion.exigir(datos.cajas.contains(caja), "Caja inválida.");
        BigDecimal saldo = caja.getMontoInicial();
        for (MovimientoCaja m : datos.movimientosCaja) if (m.getCaja() == caja)
            saldo = saldo.add(m.getTipo() == TipoMovimientoCaja.INGRESO ? m.getMonto() : m.getMonto().negate());
        return saldo;
    }
    public void abrir(BigDecimal inicial) {
        datos.transaccion(() -> { abrirInterno(inicial); return null; });
    }
    private void abrirInterno(BigDecimal inicial) {
        permiso();
        Validacion.exigir(abierta() == null, "Ya hay una caja abierta.");
        inicial = Validacion.dinero(inicial, "Monto inicial", false);
        Caja caja = new Caja(datos.siguienteId(Caja.class), LocalDateTime.now(), null, inicial, null, EstadoCaja.ABIERTA);
        datos.guardar(caja); datos.cajas.add(caja);
    }
    public BigDecimal cerrar(BigDecimal contado) {
        return datos.transaccion(() -> cerrarInterno(contado));
    }
    private BigDecimal cerrarInterno(BigDecimal contado) {
        permiso();
        Caja caja = exigirCaja();
        contado = Validacion.dinero(contado, "Efectivo contado", false);
        BigDecimal diferencia = contado.subtract(saldo(caja));
        caja.setMontoFinal(contado); caja.setFechaCierre(LocalDateTime.now()); caja.setEstado(EstadoCaja.CERRADA); datos.guardar(caja);
        return diferencia;
    }
    public BigDecimal pendiente(Pedido pedido) {
        sesion.actual();
        Validacion.exigir(datos.pedidos.contains(pedido), "Pedido inválido.");
        BigDecimal pagado = datos.pagos.stream().filter(p -> p.getPedido() == pedido).map(Pago::getMonto).reduce(BigDecimal.ZERO, BigDecimal::add);
        return pedido.getTotal().subtract(pagado);
    }
    public Pago cobrar(Pedido pedido, MetodoPago metodo, BigDecimal recibido) {
        return datos.transaccion(() -> cobrarInterno(pedido, metodo, recibido));
    }
    private Pago cobrarInterno(Pedido pedido, MetodoPago metodo, BigDecimal recibido) {
        permiso();
        Caja caja = exigirCaja();
        Validacion.exigir(datos.pedidos.contains(pedido) && pedido.getEstado() != EstadoPedido.CANCELADO, "No se puede cobrar ese pedido.");
        Validacion.exigir(metodo != null, "Selecciona un método de pago.");
        recibido = Validacion.dinero(recibido, "Monto recibido", true);
        BigDecimal pendiente = pendiente(pedido);
        Validacion.exigir(pendiente.signum() > 0, "El pedido ya está pagado.");
        Validacion.exigir(metodo == MetodoPago.EFECTIVO || recibido.compareTo(pendiente) <= 0, "Un pago no efectivo no puede superar el saldo pendiente.");
        BigDecimal aplicado = recibido.min(pendiente);
        BigDecimal cambio = recibido.subtract(aplicado);
        Pago pago = new Pago(datos.siguienteId(Pago.class), pedido, metodo, aplicado, cambio, LocalDateTime.now());
        datos.guardar(pago); datos.pagos.add(pago);
        if (metodo == MetodoPago.EFECTIVO) movimiento(caja, TipoMovimientoCaja.INGRESO, "Cobro pedido #" + pedido.getId(), aplicado);
        if (pendiente.compareTo(aplicado) == 0) {
            int id = datos.siguienteId(Factura.class);
            Factura factura = new Factura(id, pedido, (datos.esDemo() ? "DEMO-" : "GC-") + java.util.UUID.randomUUID(), LocalDateTime.now(),
                    pedido.getTotal(), BigDecimal.ZERO.setScale(2), pedido.getTotal());
            datos.guardar(factura); datos.facturas.add(factura);
        }
        pedidos.liberarSiPagado(pedido);
        return pago;
    }
    public void registrarGasto(String categoria, String descripcion, BigDecimal monto) {
        datos.transaccion(() -> { registrarGastoInterno(categoria, descripcion, monto); return null; });
    }
    private void registrarGastoInterno(String categoria, String descripcion, BigDecimal monto) {
        permiso();
        Caja caja = exigirCaja();
        categoria = Validacion.texto(categoria, "Categoría", 100);
        descripcion = Validacion.texto(descripcion, "Descripción", 255);
        monto = Validacion.dinero(monto, "Monto", true);
        Validacion.exigir(saldo(caja).compareTo(monto) >= 0, "No hay suficiente efectivo en caja.");
        Gasto gasto = new Gasto(datos.siguienteId(Gasto.class), categoria, descripcion, monto, LocalDateTime.now());
        datos.guardar(gasto); datos.gastos.add(gasto);
        String concepto = "Gasto: " + descripcion;
        movimiento(caja, TipoMovimientoCaja.EGRESO, concepto.substring(0, Math.min(255, concepto.length())), monto);
    }
    public void registrarMovimiento(TipoMovimientoCaja tipo, String concepto, BigDecimal monto) {
        datos.transaccion(() -> { registrarMovimientoInterno(tipo, concepto, monto); return null; });
    }
    private void registrarMovimientoInterno(TipoMovimientoCaja tipo, String concepto, BigDecimal monto) {
        permiso();
        Caja caja = exigirCaja();
        Validacion.exigir(tipo != null, "Selecciona el tipo de movimiento.");
        concepto = Validacion.texto(concepto, "Concepto", 255);
        monto = Validacion.dinero(monto, "Monto", true);
        Validacion.exigir(tipo != TipoMovimientoCaja.EGRESO || saldo(caja).compareTo(monto) >= 0, "No hay suficiente efectivo.");
        movimiento(caja, tipo, concepto, monto);
    }
    private Caja exigirCaja() {
        Caja caja = abierta(); Validacion.exigir(caja != null, "Abre la caja primero."); return caja;
    }
    private void movimiento(Caja caja, TipoMovimientoCaja tipo, String concepto, BigDecimal monto) {
        MovimientoCaja movimiento = new MovimientoCaja(datos.siguienteId(MovimientoCaja.class), caja, tipo, concepto, monto, LocalDateTime.now());
        datos.guardar(movimiento); datos.movimientosCaja.add(movimiento);
    }
    public String ticket(Pedido pedido) {
        permiso();
        Validacion.exigir(datos.pedidos.contains(pedido), "Pedido inválido.");
        Factura factura = datos.facturas.stream().filter(f -> f.getPedido() == pedido).findFirst().orElse(null);
        StringBuilder texto = new StringBuilder("GARAGE COFFEE\nCOMPROBANTE ACADÉMICO — SIN VALIDEZ FISCAL\n");
        texto.append(factura == null ? "Cuenta provisional\n" : "Comprobante " + factura.getNumero() + "\n");
        texto.append("Pedido #").append(pedido.getId()).append(" · ").append(pedido.getTipoEntrega()).append("\n");
        texto.append("Cliente: ").append(pedido.getCliente() == null ? "Consumidor final" : pedido.getCliente().getNombre()).append("\n\n");
        for (DetallePedido d : pedido.getDetalles())
            texto.append(d.getCantidad()).append(" x ").append(d.getProducto().getNombre()).append(" @ Q")
                    .append(d.getPrecioUnitario()).append(" = Q").append(d.getSubtotal()).append("\n");
        if (pedido instanceof PedidoDomicilio d) texto.append("Envío: Q").append(d.getCostoEnvio()).append("\n");
        texto.append("\nTOTAL: Q").append(pedido.getTotal()).append("\n");
        for (Pago pago : datos.pagos) if (pago.getPedido() == pedido)
            texto.append(pago.getMetodo()).append(": Q").append(pago.getMonto()).append(" · Cambio: Q").append(pago.getCambio()).append("\n");
        return texto.append("Pendiente: Q").append(pendiente(pedido)).append("\n\nImpuestos no configurados; no es factura fiscal.\n").toString();
    }
}
