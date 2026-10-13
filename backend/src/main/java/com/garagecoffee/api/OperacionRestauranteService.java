package com.garagecoffee.api;

import com.garagecoffee.dao.*;
import com.garagecoffee.database.ConexionDB;
import com.garagecoffee.model.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

// ejecuta las operaciones de la app.
@Service
public class OperacionRestauranteService {
    private final MesaDAO mesas;
    private final ProductoDAO productos;
    private final PedidoDAO pedidos;
    private final PagoDAO pagos;
    private final CajaDAO cajas;
    private final ControlCambiosDAO cambios;

    public OperacionRestauranteService(MesaDAO mesas, ProductoDAO productos,
            PedidoDAO pedidos, PagoDAO pagos, CajaDAO cajas, ControlCambiosDAO cambios) {
        this.mesas = mesas;
        this.productos = productos;
        this.pedidos = pedidos;
        this.pagos = pagos;
        this.cajas = cajas;
        this.cambios = cambios;
    }

    public record DetallePedidoEntrada(int productoId, int cantidad) { }
    public record PedidoEntrada(int mesaId, String observaciones, List<DetallePedidoEntrada> detalles) { }
    public record AgregarPedidoEntrada(String observaciones, List<DetallePedidoEntrada> detalles) { }
    public record PedidoRespuesta(int id, int mesaId, String estado, BigDecimal total) { }
    public record EstadoMesaEntrada(String estado) { }
    public record DetallePedidoRespuesta(String producto, int cantidad, BigDecimal precio, BigDecimal subtotal) { }
    public record PedidoMesaRespuesta(boolean activo, int id, int mesaId, String estado,
            BigDecimal total, BigDecimal pagado, BigDecimal pendiente, String observaciones,
            List<DetallePedidoRespuesta> detalles) { }
    public record CobroEntrada(BigDecimal efectivo) { }
    public record CobroRespuesta(int id, int pedidoId, BigDecimal monto, BigDecimal efectivo,
            BigDecimal cambio, String estadoMesa) { }

    public PedidoMesaRespuesta pedidoActivo(int mesaId) throws SQLException {
        Mesa mesa = mesas.buscarPorId(mesaId);
        if (mesa == null) throw new IllegalArgumentException("La mesa ya no existe.");
        List<Pago> pagosActuales = pagos.listar();
        PedidoLocal encontrado = null;
        for (Pedido pedido : pedidos.listar()) {
            if (!(pedido instanceof PedidoLocal local) || local.getMesa().getId() != mesaId) continue;
            BigDecimal pendiente = pedido.getTotal().subtract(pagado(pedido.getId(), pagosActuales));
            boolean activo = pedido.getEstado() != EstadoPedido.CANCELADO
                    && (pedido.getEstado() != EstadoPedido.ENTREGADO || pendiente.signum() > 0);
            if (activo && (encontrado == null || pedido.getId() > encontrado.getId())) encontrado = local;
        }
        return respuesta(encontrado, pagosActuales, mesaId);
    }

    public PedidoRespuesta crearPedido(PedidoEntrada entrada) throws SQLException {
        if (entrada == null) throw new IllegalArgumentException("El pedido es obligatorio.");
        return ConexionDB.transaccion(() -> {
            cambios.revision(true);
            Mesa mesa = mesas.buscarPorId(entrada.mesaId());
            if (mesa == null) throw new IllegalArgumentException("La mesa ya no existe.");
            if (mesa.getEstado() != EstadoMesa.LIBRE)
                throw new IllegalStateException("La mesa debe estar libre para iniciar un pedido.");
            if (entrada.detalles() == null || entrada.detalles().isEmpty())
                throw new IllegalArgumentException("Agrega productos al pedido.");

            String observaciones = entrada.observaciones() == null ? "" : entrada.observaciones().trim();
            if (observaciones.length() > 255)
                throw new IllegalArgumentException("Las observaciones admiten hasta 255 caracteres.");

            Map<Integer, Integer> cantidades = new LinkedHashMap<>();
            for (DetallePedidoEntrada detalle : entrada.detalles()) {
                if (detalle == null || detalle.productoId() <= 0 || detalle.cantidad() <= 0)
                    throw new IllegalArgumentException("Producto o cantidad inválidos.");
                int acumulada = cantidades.getOrDefault(detalle.productoId(), 0) + detalle.cantidad();
                if (acumulada > 9999)
                    throw new IllegalArgumentException("La cantidad acumulada no puede superar 9999.");
                cantidades.put(detalle.productoId(), acumulada);
            }

            PedidoLocal pedido = new PedidoLocal(0, observaciones, mesa);
            for (var detalle : cantidades.entrySet()) {
                Producto producto = productos.buscarPorId(detalle.getKey());
                if (producto == null) throw new IllegalArgumentException("Uno de los productos ya no existe.");
                if (!producto.isDisponible() || producto.getcategoria() == null || !producto.getcategoria().isActivo())
                    throw new IllegalStateException(producto.getNombre() + " no está disponible.");
                pedido.agregarDetalle(new DetallePedido(0, detalle.getValue(), producto.getPrecio(), producto));
            }

            if (!pedidos.guardar(pedido)) throw new SQLException("No se pudo guardar el pedido.");
            mesa.ocupar();
            if (!mesas.actualizar(mesa)) throw new SQLException("No se pudo actualizar la mesa.");
            cambios.avanzar();
            return new PedidoRespuesta(pedido.getId(), mesa.getId(), pedido.getEstado().name(), pedido.getTotal());
        });
    }

    public ConsultaRestauranteService.MesaRespuesta cambiarEstadoMesa(
            int id, EstadoMesaEntrada entrada) throws SQLException {
        if (entrada == null || entrada.estado() == null)
            throw new IllegalArgumentException("Selecciona un estado.");
        final EstadoMesa nuevo;
        try {
            nuevo = EstadoMesa.valueOf(entrada.estado().trim().toUpperCase());
        } catch (IllegalArgumentException error) {
            throw new IllegalArgumentException("El estado de la mesa no es válido.");
        }
        if (nuevo == EstadoMesa.OCUPADA)
            throw new IllegalArgumentException("Una mesa se ocupa automáticamente al enviar un pedido.");

        return ConexionDB.transaccion(() -> {
            cambios.revision(true);
            Mesa mesa = mesas.buscarPorId(id);
            if (mesa == null) throw new IllegalArgumentException("La mesa ya no existe.");
            if (mesa.getEstado() == EstadoMesa.OCUPADA)
                throw new IllegalStateException("Una mesa con pedido activo no puede cambiarse manualmente.");
            mesa.cambiarEstado(nuevo);
            if (!mesas.actualizar(mesa)) throw new SQLException("No se pudo actualizar la mesa.");
            cambios.avanzar();
            return new ConsultaRestauranteService.MesaRespuesta(
                    mesa.getId(), mesa.getNumero(), mesa.getCapacidad(), mesa.getEstado().name());
        });
    }

    public PedidoMesaRespuesta agregarAlPedido(int id, AgregarPedidoEntrada entrada) throws SQLException {
        if (entrada == null || entrada.detalles() == null || entrada.detalles().isEmpty())
            throw new IllegalArgumentException("Agrega productos al pedido.");
        return ConexionDB.transaccion(() -> {
            cambios.revision(true);
            Pedido pedido = pedidos.buscarPorId(id);
            if (!(pedido instanceof PedidoLocal local)) throw new IllegalArgumentException("El pedido ya no existe.");
            List<Pago> pagosActuales = pagos.listar();
            BigDecimal pendiente = pedido.getTotal().subtract(pagado(id, pagosActuales));
            if (pedido.getEstado() == EstadoPedido.CANCELADO
                    || (pedido.getEstado() == EstadoPedido.ENTREGADO && pendiente.signum() <= 0)
                    || local.getMesa().getEstado() != EstadoMesa.OCUPADA)
                throw new IllegalStateException("La mesa ya cerró este pedido.");

            Map<Integer, Integer> cantidades = cantidadesValidas(entrada.detalles());
            List<DetallePedido> nuevos = new java.util.ArrayList<>();
            for (var detalle : cantidades.entrySet()) {
                Producto producto = productos.buscarPorId(detalle.getKey());
                if (producto == null) throw new IllegalArgumentException("Uno de los productos ya no existe.");
                if (!producto.isDisponible() || producto.getcategoria() == null || !producto.getcategoria().isActivo())
                    throw new IllegalStateException(producto.getNombre() + " no está disponible.");
                DetallePedido nuevo = new DetallePedido(0, detalle.getValue(), producto.getPrecio(), producto);
                nuevos.add(nuevo);
                pedido.agregarDetalle(nuevo);
            }

            String nota = entrada.observaciones() == null ? "" : entrada.observaciones().trim();
            if (!nota.isEmpty()) {
                String anterior = pedido.getObservaciones() == null ? "" : pedido.getObservaciones().trim();
                String combinada = anterior.isEmpty() ? nota : anterior + " | " + nota;
                pedido.setObservaciones(combinada.substring(0, Math.min(combinada.length(), 255)));
            }
            pedido.setEstado(EstadoPedido.PENDIENTE);
            if (!pedidos.agregarDetalles(pedido, nuevos))
                throw new SQLException("No se pudieron agregar los productos al pedido.");
            cambios.avanzar();
            return respuesta(local, pagosActuales, local.getMesa().getId());
        });
    }

    public PedidoMesaRespuesta entregarPedido(int id) throws SQLException {
        return ConexionDB.transaccion(() -> {
            cambios.revision(true);
            Pedido pedido = pedidos.buscarPorId(id);
            if (!(pedido instanceof PedidoLocal)) throw new IllegalArgumentException("El pedido ya no existe.");
            if (pedido.getEstado() != EstadoPedido.LISTO)
                throw new IllegalStateException("El pedido debe estar listo antes de entregarlo.");
            pedido.setEstado(EstadoPedido.ENTREGADO);
            if (!pedidos.actualizar(pedido)) throw new SQLException("No se pudo actualizar el pedido.");
            cambios.avanzar();
            return respuesta((PedidoLocal) pedido, pagos.listar(), ((PedidoLocal) pedido).getMesa().getId());
        });
    }

    public CobroRespuesta cobrarPedido(int id, CobroEntrada entrada) throws SQLException {
        if (entrada == null || entrada.efectivo() == null)
            throw new IllegalArgumentException("Indica el efectivo recibido.");
        final BigDecimal efectivo;
        try {
            efectivo = entrada.efectivo().setScale(2, java.math.RoundingMode.UNNECESSARY);
        } catch (ArithmeticException error) {
            throw new IllegalArgumentException("El efectivo admite hasta dos decimales.");
        }
        if (efectivo.signum() <= 0) throw new IllegalArgumentException("El efectivo debe ser mayor que cero.");

        return ConexionDB.transaccion(() -> {
            cambios.revision(true);
            Pedido pedido = pedidos.buscarPorId(id);
            if (!(pedido instanceof PedidoLocal local)) throw new IllegalArgumentException("El pedido ya no existe.");
            if (pedido.getEstado() != EstadoPedido.ENTREGADO)
                throw new IllegalStateException("Marca el pedido como entregado antes de cobrar.");
            List<Pago> pagosActuales = pagos.listar();
            BigDecimal pendiente = pedido.getTotal().subtract(pagado(id, pagosActuales));
            if (pendiente.signum() <= 0) throw new IllegalStateException("El pedido ya está pagado.");
            if (efectivo.compareTo(pendiente) < 0)
                throw new IllegalArgumentException("El efectivo recibido debe cubrir Q" + pendiente + ".");
            Caja caja = cajas.listar().stream().filter(c -> c.getEstado() == EstadoCaja.ABIERTA)
                    .findFirst().orElseThrow(() -> new IllegalStateException("No hay una caja abierta para registrar el cobro."));
            Pago pago = new Pago(0, pedido, caja, pendiente, efectivo.subtract(pendiente), LocalDateTime.now());
            if (!pagos.guardar(pago)) throw new SQLException("No se pudo guardar el cobro.");
            local.getMesa().liberar();
            if (!mesas.actualizar(local.getMesa())) throw new SQLException("No se pudo liberar la mesa.");
            cambios.avanzar();
            return new CobroRespuesta(pago.getId(), pedido.getId(), pendiente, efectivo,
                    pago.getCambio(), local.getMesa().getEstado().name());
        });
    }

    private BigDecimal pagado(int pedidoId, List<Pago> pagosActuales) {
        return pagosActuales.stream().filter(p -> p.getPedido().getId() == pedidoId)
                .map(Pago::getMonto).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Map<Integer, Integer> cantidadesValidas(List<DetallePedidoEntrada> detalles) {
        Map<Integer, Integer> cantidades = new LinkedHashMap<>();
        for (DetallePedidoEntrada detalle : detalles) {
            if (detalle == null || detalle.productoId() <= 0 || detalle.cantidad() <= 0)
                throw new IllegalArgumentException("Producto o cantidad inválidos.");
            int acumulada = cantidades.getOrDefault(detalle.productoId(), 0) + detalle.cantidad();
            if (acumulada > 9999)
                throw new IllegalArgumentException("La cantidad acumulada no puede superar 9999.");
            cantidades.put(detalle.productoId(), acumulada);
        }
        return cantidades;
    }

    private PedidoMesaRespuesta respuesta(PedidoLocal pedido, List<Pago> pagosActuales, int mesaId) {
        if (pedido == null)
            return new PedidoMesaRespuesta(false, 0, mesaId, "", BigDecimal.ZERO,
                    BigDecimal.ZERO, BigDecimal.ZERO, "", List.of());
        BigDecimal pagado = pagado(pedido.getId(), pagosActuales);
        return new PedidoMesaRespuesta(true, pedido.getId(), mesaId, pedido.getEstado().name(),
                pedido.getTotal(), pagado, pedido.getTotal().subtract(pagado), pedido.getObservaciones(),
                pedido.getDetalles().stream().map(d -> new DetallePedidoRespuesta(
                        d.getProducto().getNombre(), d.getCantidad(), d.getPrecioUnitario(), d.getSubtotal())).toList());
    }
}
