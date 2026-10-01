package com.garagecoffee;

import com.garagecoffee.controller.RestauranteController;
import com.garagecoffee.model.*;
import com.garagecoffee.service.DatosDemo;
import com.garagecoffee.service.ReporteService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

// Recorre el sistema como administrador, mesero, cocina y cajero.
class AuditoriaFlujosTest {
    private RestauranteController control;

    @BeforeEach void preparar() {
        control = new RestauranteController(new DatosDemo());
        entrar("admin");
    }

    private void entrar(String usuario) {
        control.sesion().iniciarSesion(usuario, "demo".toCharArray());
    }
    private static BigDecimal numero(String valor) { return new BigDecimal(valor); }
    private DetallePedido linea(Producto producto, int cantidad) {
        return new DetallePedido(0, cantidad, producto.getPrecio(), producto);
    }
    private Pedido pedidoLocal(Mesa mesa, Producto producto, int cantidad) {
        return control.pedidos().guardar(0, TipoEntrega.CONSUMIR_LOCAL, null, mesa, "",
                BigDecimal.ZERO, null, "", List.of(linea(producto, cantidad)));
    }

    @Test void recorridoPrincipalMesaCocinaEntregaYCobro() {
        Mesa mesa = control.atencion().mesas().getFirst();
        Producto cafe = control.catalogo().productos().getFirst();
        Ingrediente grano = control.inventario().ingredientes().getFirst();
        BigDecimal stockInicial = grano.getStockActual();

        entrar("mesero");
        Pedido pedido = pedidoLocal(mesa, cafe, 2);
        assertEquals(EstadoMesa.OCUPADA, mesa.getEstado());
        assertEquals(numero("30.00"), pedido.getTotal());
        assertNull(pedido.getCliente(), "Un consumo local no obliga a registrar cliente.");

        entrar("cocina");
        control.pedidos().preparar(pedido);
        assertEquals(EstadoPedido.EN_PREPARACION, pedido.getEstado());
        assertEquals(0, grano.getStockActual().compareTo(stockInicial.subtract(numero("36.000"))));
        control.pedidos().listo(pedido);

        entrar("mesero");
        control.pedidos().entregar(pedido);
        assertEquals(EstadoPedido.ENTREGADO, pedido.getEstado());
        assertEquals(EstadoMesa.OCUPADA, mesa.getEstado(), "La mesa sigue ocupada mientras falte cobrar.");

        entrar("cajero");
        control.finanzas().abrir(numero("100.00"));
        Pago pago = control.finanzas().cobrar(pedido, MetodoPago.EFECTIVO, numero("50.00"));
        assertEquals(numero("30.00"), pago.getMonto());
        assertEquals(numero("20.00"), pago.getCambio());
        assertEquals(0, control.finanzas().pendiente(pedido).signum());
        assertEquals(EstadoMesa.LIBRE, mesa.getEstado());
        assertEquals(1, control.finanzas().facturas().size());
        assertEquals(0, control.finanzas().saldo(control.finanzas().abierta()).compareTo(numero("130.00")));
        assertThrows(IllegalArgumentException.class,
                () -> control.finanzas().cobrar(pedido, MetodoPago.EFECTIVO, numero("30.00")));
    }

    @Test void abonosNoDuplicanComprobanteNiDinero() {
        Pedido pedido = pedidoLocal(control.atencion().mesas().getFirst(), control.catalogo().productos().getFirst(), 1);
        entrar("cajero");
        control.finanzas().abrir(BigDecimal.ZERO);
        control.finanzas().cobrar(pedido, MetodoPago.EFECTIVO, numero("5.00"));
        assertEquals(numero("10.00"), control.finanzas().pendiente(pedido));
        assertTrue(control.finanzas().facturas().isEmpty());
        control.finanzas().cobrar(pedido, MetodoPago.EFECTIVO, numero("10.00"));
        assertEquals(1, control.finanzas().facturas().size());
        assertEquals(2, control.finanzas().pagos().size());
        assertEquals(0, control.finanzas().saldo(control.finanzas().abierta()).compareTo(numero("15.00")));
    }

    @Test void cancelarPedidoPendienteLiberaMesa() {
        Mesa mesa = control.atencion().mesas().get(1);
        Pedido pedido = pedidoLocal(mesa, control.catalogo().productos().getFirst(), 1);
        control.pedidos().cancelar(pedido);
        assertEquals(EstadoPedido.CANCELADO, pedido.getEstado());
        assertEquals(EstadoMesa.LIBRE, mesa.getEstado());
        assertThrows(IllegalArgumentException.class, () -> control.pedidos().preparar(pedido));
    }

    @Test void pedidoDomicilioRequiereClienteYRepartidor() {
        Producto producto = control.catalogo().productos().get(1);
        Cliente cliente = control.atencion().clientes().getFirst();
        assertThrows(IllegalArgumentException.class, () -> control.pedidos().guardar(0,
                TipoEntrega.DOMICILIO, null, null, "Zona 1", numero("5"), null, "", List.of(linea(producto, 1))));
        Pedido pedido = control.pedidos().guardar(0, TipoEntrega.DOMICILIO, cliente, null,
                "Zona 1", numero("5"), null, "Tocar el timbre", List.of(linea(producto, 1)));
        entrar("cocina");
        control.pedidos().preparar(pedido);
        control.pedidos().listo(pedido);
        entrar("mesero");
        assertThrows(IllegalArgumentException.class, () -> control.pedidos().entregar(pedido));
        control.pedidos().asignar(pedido, control.atencion().repartidores().getFirst());
        control.pedidos().entregar(pedido);
        assertEquals(EstadoPedido.ENTREGADO, pedido.getEstado());
        assertEquals(numero("27.00"), pedido.getTotal());
    }

    @Test void compraRecibidaAumentaStockYPagoOpcionalSaleDeCaja() {
        Proveedor proveedor = control.inventario().proveedores().getFirst();
        Ingrediente ingrediente = control.inventario().ingredientes().getFirst();
        BigDecimal stock = ingrediente.getStockActual();
        control.finanzas().abrir(numero("100.00"));
        Compra compra = control.registrarCompra(proveedor,
                List.of(new DetalleCompra(numero("2.000"), numero("3.5000"), ingrediente)), true, true);
        assertEquals(EstadoCompra.RECIBIDA, compra.getEstado());
        assertEquals(0, ingrediente.getStockActual().compareTo(stock.add(numero("2.000"))));
        assertEquals(0, control.finanzas().saldo(control.finanzas().abierta()).compareTo(numero("93.00")));
        assertEquals(1, control.finanzas().movimientos().size());
        assertTrue(control.finanzas().gastos().isEmpty(), "Una compra no se duplica como gasto.");
        assertThrows(IllegalArgumentException.class, () -> control.inventario().recibirCompra(compra));
    }

    @Test void compraPendienteNoModificaStockNiAsumePago() {
        Proveedor proveedor = control.inventario().proveedores().getFirst();
        Ingrediente ingrediente = control.inventario().ingredientes().getFirst();
        BigDecimal stock = ingrediente.getStockActual();
        Compra compra = control.registrarCompra(proveedor,
                List.of(new DetalleCompra(BigDecimal.ONE, numero("2.00"), ingrediente)), false, false);
        assertEquals(EstadoCompra.PENDIENTE, compra.getEstado());
        assertEquals(0, ingrediente.getStockActual().compareTo(stock));
        assertThrows(IllegalArgumentException.class, () -> control.registrarCompra(proveedor,
                List.of(new DetalleCompra(BigDecimal.ONE, numero("2.00"), ingrediente)), false, true));
        control.inventario().recibirCompra(compra);
        assertEquals(0, ingrediente.getStockActual().compareTo(stock.add(BigDecimal.ONE)));
    }

    @Test void inventarioRegistraEntradaMermaYAjuste() {
        Ingrediente ingrediente = control.inventario().ingredientes().getFirst();
        BigDecimal inicial = ingrediente.getStockActual();
        control.inventario().registrarMovimiento(ingrediente, TipoMovimientoStock.ENTRADA, numero("10"), "Compra rápida");
        control.inventario().registrarMovimiento(ingrediente, TipoMovimientoStock.MERMA, numero("2"), "Derrame");
        assertEquals(0, ingrediente.getStockActual().compareTo(inicial.add(numero("8"))));
        assertEquals(1, control.inventario().perdidas().size());
        control.inventario().registrarMovimiento(ingrediente, TipoMovimientoStock.AJUSTE, numero("100"), "Conteo físico");
        assertEquals(0, ingrediente.getStockActual().compareTo(numero("100")));
        assertThrows(IllegalArgumentException.class, () -> control.inventario().registrarMovimiento(
                ingrediente, TipoMovimientoStock.SALIDA, numero("1000"), "Error"));
    }

    @Test void reservasEvitanCrucesPeroNoBloqueanLaMesaHoy() {
        Cliente cliente = control.atencion().clientes().getFirst();
        Mesa mesa = control.atencion().mesas().getFirst();
        LocalDateTime fecha = LocalDateTime.now().plusDays(1).withSecond(0).withNano(0);
        control.atencion().guardarReserva(0, cliente, mesa, fecha, 2);
        Reserva reserva = control.atencion().reservas().getFirst();
        assertEquals(EstadoMesa.LIBRE, mesa.getEstado());
        assertThrows(IllegalArgumentException.class,
                () -> control.atencion().guardarReserva(0, cliente, mesa, fecha.plusMinutes(30), 2));
        control.atencion().cambiarReserva(reserva, EstadoReserva.CONFIRMADA);
        control.atencion().cambiarReserva(reserva, EstadoReserva.CANCELADA);
        assertEquals(EstadoReserva.CANCELADA, reserva.getEstado());
    }

    @Test void ofertasUsanElMenorPrecioSinAcumularDescuentos() {
        Producto producto = control.catalogo().productos().getFirst();
        var menu = control.catalogo().guardarMenu(0, "Promoción", true,
                LocalDate.now().minusDays(1), LocalDate.now().plusDays(1));
        control.catalogo().guardarDetalleMenu(0, menu, producto, numero("12.00"));
        control.catalogo().guardarPlato(0, producto, LocalDate.now(), "Hoy", numero("10.00"));
        assertEquals(numero("10.00"), control.catalogo().precioVigente(producto));
    }

    @Test void cajaSePuedeCerrarYReabrir() {
        control.finanzas().abrir(numero("50.00"));
        control.finanzas().registrarGasto("Limpieza", "Jabón", numero("5.00"));
        assertEquals(numero("45.00"), control.finanzas().saldo(control.finanzas().abierta()));
        assertEquals(BigDecimal.ZERO.setScale(2), control.finanzas().cerrar(numero("45.00")));
        assertNull(control.finanzas().abierta());
        assertDoesNotThrow(() -> control.finanzas().abrir(numero("20.00")));
    }

    @Test void reportesReflejanVentaPreparadaYCobrada() {
        Pedido pedido = pedidoLocal(control.atencion().mesas().getFirst(), control.catalogo().productos().getFirst(), 1);
        entrar("cocina");
        control.pedidos().preparar(pedido);
        control.pedidos().listo(pedido);
        entrar("cajero");
        control.finanzas().abrir(BigDecimal.ZERO);
        control.finanzas().cobrar(pedido, MetodoPago.TARJETA, pedido.getTotal());
        entrar("admin");
        String ventas = control.reportes().generar(ReporteService.Tipo.VENTAS, LocalDate.now(), LocalDate.now());
        String resultado = control.reportes().generar(ReporteService.Tipo.RESULTADO_ESTIMADO, LocalDate.now(), LocalDate.now());
        assertTrue(ventas.contains("Total: Q15.00"));
        assertTrue(resultado.contains("Ingredientes consumidos"));
        assertFalse(resultado.contains("ventas aún no preparadas"));
    }

    @Test void permisosSeparanLasTareasDeCadaRol() {
        entrar("cocina");
        assertDoesNotThrow(() -> control.inventario().ingredientes());
        assertThrows(IllegalArgumentException.class, () -> control.atencion().clientes());
        assertThrows(IllegalArgumentException.class, () -> control.finanzas().cajas());
        entrar("mesero");
        assertDoesNotThrow(() -> control.atencion().clientes());
        assertThrows(IllegalArgumentException.class, () -> control.reportes().generar(
                ReporteService.Tipo.VENTAS, LocalDate.now(), LocalDate.now()));
    }

    @Test void empleadosSeCreanYLaClaveIncorrectaNoMantieneSesion() {
        Empleado empleado = control.sesion().guardarEmpleado(0, "Ana", "López", "ana",
                "1234", "", "", RolEmpleado.MESERO, true);
        assertTrue(empleado.isActivo());
        assertEquals("ana", control.sesion().iniciarSesion("ana", "1234".toCharArray()).getNombreUsuario());
        assertThrows(IllegalArgumentException.class,
                () -> control.sesion().iniciarSesion("ana", "incorrecta".toCharArray()));
        assertThrows(IllegalStateException.class, () -> control.sesion().actual());
    }
}
