package com.garagecoffee.service;

import com.garagecoffee.model.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

// controla la caja y los cobros.
public class FinanzasService {
    private final DatosRestaurante datos;
    private final SesionService sesion;
    private final PedidoService pedidos;

    public FinanzasService(DatosRestaurante datos, SesionService sesion, PedidoService pedidos) {
        this.datos = datos;
        this.sesion = sesion;
        this.pedidos = pedidos;
    }

    private void permiso() { sesion.exigir(RolEmpleado.CAJERO); }
    public List<Caja> cajas() { permiso(); return List.copyOf(datos.cajas); }
    public List<Pago> pagos() { permiso(); return List.copyOf(datos.pagos); }

    public Caja abierta() {
        permiso();
        return datos.cajas.stream().filter(c -> c.getEstado() == EstadoCaja.ABIERTA).findFirst().orElse(null);
    }

    public BigDecimal saldo(Caja caja) {
        permiso();
        Validacion.exigir(datos.cajas.contains(caja), "Caja inválida.");
        BigDecimal ingresos = datos.pagos.stream().filter(p -> p.getCaja() == caja)
                .map(Pago::getMonto).reduce(BigDecimal.ZERO, BigDecimal::add);
        return caja.getMontoInicial().add(ingresos);
    }

    public void abrir(BigDecimal inicial) {
        datos.transaccion(() -> {
            permiso();
            Validacion.exigir(abierta() == null, "Ya hay una caja abierta.");
            BigDecimal monto = Validacion.dinero(inicial, "Monto inicial", false);
            Caja caja = new Caja(datos.siguienteId(Caja.class), LocalDateTime.now(), null,
                    monto, null, EstadoCaja.ABIERTA);
            datos.guardar(caja);
            datos.cajas.add(caja);
            return null;
        });
    }

    public BigDecimal cerrar(BigDecimal contado) {
        return datos.transaccion(() -> {
            permiso();
            Caja caja = exigirCaja();
            BigDecimal monto = Validacion.dinero(contado, "Efectivo contado", false);
            BigDecimal diferencia = monto.subtract(saldo(caja));
            caja.setMontoFinal(monto);
            caja.setFechaCierre(LocalDateTime.now());
            caja.setEstado(EstadoCaja.CERRADA);
            datos.guardar(caja);
            return diferencia;
        });
    }

    public BigDecimal pendiente(Pedido pedido) {
        sesion.actual();
        Validacion.exigir(datos.pedidos.contains(pedido), "Pedido inválido.");
        BigDecimal pagado = datos.pagos.stream().filter(p -> p.getPedido() == pedido)
                .map(Pago::getMonto).reduce(BigDecimal.ZERO, BigDecimal::add);
        return pedido.getTotal().subtract(pagado);
    }

    public Pago cobrar(Pedido pedido, BigDecimal recibido) {
        return datos.transaccion(() -> {
            permiso();
            Caja caja = exigirCaja();
            Validacion.exigir(datos.pedidos.contains(pedido) && pedido.getEstado() != EstadoPedido.CANCELADO,
                    "No se puede cobrar ese pedido.");
            BigDecimal total = pendiente(pedido);
            Validacion.exigir(total.signum() > 0, "El pedido ya está pagado.");
            BigDecimal efectivo = Validacion.dinero(recibido, "Efectivo recibido", true);
            Validacion.exigir(efectivo.compareTo(total) >= 0,
                    "El efectivo recibido debe cubrir el total del pedido.");
            Pago pago = new Pago(datos.siguienteId(Pago.class), pedido, caja, total,
                    efectivo.subtract(total), LocalDateTime.now());
            datos.guardar(pago);
            datos.pagos.add(pago);
            pedidos.liberarSiPagado(pedido);
            return pago;
        });
    }

    private Caja exigirCaja() {
        Caja caja = abierta();
        Validacion.exigir(caja != null, "Abre la caja primero.");
        return caja;
    }

    public String ticket(Pedido pedido) {
        permiso();
        Validacion.exigir(datos.pedidos.contains(pedido), "Pedido inválido.");
        StringBuilder texto = new StringBuilder("GARAGE COFFEE\nCOMPROBANTE — SIN VALIDEZ FISCAL\n");
        texto.append("Pedido #").append(pedido.getId()).append(" · Mesa ")
                .append(((PedidoLocal) pedido).getMesa().getNumero()).append("\n\n");
        for (DetallePedido detalle : pedido.getDetalles())
            texto.append(detalle.getCantidad()).append(" x ").append(detalle.getProducto().getNombre())
                    .append(" = ").append(detalle.getSubtotal()).append("\n");
        texto.append("\nTOTAL: Q").append(pedido.getTotal()).append("\n");
        for (Pago pago : datos.pagos) if (pago.getPedido() == pedido)
            texto.append("Efectivo: Q").append(pago.getMonto().add(pago.getCambio()))
                    .append(" · Cambio: Q").append(pago.getCambio()).append("\n");
        return texto.append("Pendiente: Q").append(pendiente(pedido)).append("\n").toString();
    }
}
