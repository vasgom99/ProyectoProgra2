package com.garagecoffee;

import com.garagecoffee.controller.RestauranteController;
import com.garagecoffee.model.*;
import com.garagecoffee.service.DatosDemo;
import com.garagecoffee.service.ReporteService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// Comprueba las funciones secundarias que siguen disponibles en «Más opciones».
class AuditoriaFuncionesTest {
    private RestauranteController control;

    @BeforeEach void preparar() {
        control = new RestauranteController(new DatosDemo());
        control.sesion().iniciarSesion("admin", "demo".toCharArray());
    }

    private static BigDecimal n(String valor) { return new BigDecimal(valor); }
    private DetallePedido linea(Producto producto, int cantidad) {
        return new DetallePedido(0, cantidad, producto.getPrecio(), producto);
    }

    @Test void clientesDireccionesMesasYRepartidoresSePuedenAdministrar() {
        int clientesIniciales = control.atencion().clientes().size();
        control.atencion().guardarCliente(0, "  María Pérez  ", "5555-1000", "maria@ejemplo.test");
        Cliente cliente = control.atencion().clientes().stream()
                .filter(c -> c.getNombre().equals("María Pérez")).findFirst().orElseThrow();
        assertNotNull(cliente.getFechaRegistro());

        control.atencion().guardarDireccion(0, cliente, "Zona 1", "Casa verde");
        DireccionCliente direccion = control.atencion().direcciones().stream()
                .filter(d -> d.getClienteId() == cliente.getId()).findFirst().orElseThrow();
        control.atencion().guardarDireccion(direccion.getId(), cliente, "Zona 2", "Portón negro");
        assertEquals("Zona 2", direccion.getDireccion());
        assertEquals(1, cliente.getDirecciones().size());
        control.atencion().eliminarDireccion(direccion);
        assertTrue(cliente.getDirecciones().isEmpty());

        control.atencion().guardarCliente(cliente.getId(), "María López", "", "");
        assertEquals("María López", cliente.getNombre());
        control.atencion().eliminarCliente(cliente);
        assertEquals(clientesIniciales, control.atencion().clientes().size());

        int mesasIniciales = control.atencion().mesas().size();
        control.atencion().guardarMesa(0, 99, 6, true);
        Mesa mesa = control.atencion().mesas().stream().filter(m -> m.getNumero() == 99).findFirst().orElseThrow();
        control.atencion().guardarMesa(mesa.getId(), 99, 8, false);
        assertEquals(8, mesa.getCapacidad());
        assertEquals(EstadoMesa.FUERA_SERVICIO, mesa.getEstado());
        control.atencion().eliminarMesa(mesa);
        assertEquals(mesasIniciales, control.atencion().mesas().size());

        control.atencion().guardarRepartidor(0, "Luis", "5555-2000", "Bicicleta", true);
        Repartidor repartidor = control.atencion().repartidores().stream()
                .filter(r -> r.getNombre().equals("Luis")).findFirst().orElseThrow();
        control.atencion().guardarRepartidor(repartidor.getId(), "Luis Gómez", "", "Moto", false);
        assertEquals("Luis Gómez", repartidor.getNombre());
        assertFalse(repartidor.isActivo());
    }

    @Test void catalogoPromocionesYRecetasTienenAltaEdicionYBajaCoherente() {
        control.inventario().guardarIngrediente(0, "Jarabe", "ml", n("10"), n("0.20"));
        Ingrediente ingrediente = control.inventario().ingredientes().stream()
                .filter(i -> i.getNombre().equals("Jarabe")).findFirst().orElseThrow();
        Categoria categoria = control.catalogo().guardarCategoria(0, "Temporada", TipoCategoria.BEBIDA, true);
        Producto producto = control.catalogo().guardarProducto(0, "Bebida de prueba", "", "", n("20"), categoria, true);
        assertThrows(IllegalArgumentException.class, () -> control.catalogo().eliminarCategoria(categoria));

        var menu = control.catalogo().guardarMenu(0, "Oferta de prueba", true,
                LocalDate.now().minusDays(1), LocalDate.now().plusDays(1));
        control.catalogo().guardarDetalleMenu(0, menu, producto, n("18"));
        DetalleMenu detalle = control.catalogo().detallesMenu().stream()
                .filter(d -> d.getMenu() == menu).findFirst().orElseThrow();
        assertThrows(IllegalArgumentException.class,
                () -> control.catalogo().guardarDetalleMenu(0, menu, producto, n("17")));
        control.catalogo().guardarPlato(0, producto, LocalDate.now(), "Especial", n("16"));
        PlatoDelDia plato = control.catalogo().platos().stream()
                .filter(p -> p.getProducto() == producto).findFirst().orElseThrow();
        control.catalogo().guardarReceta(0, producto, ingrediente, n("5"));
        DetalleReceta receta = control.catalogo().recetas().stream()
                .filter(r -> r.getProducto() == producto && r.getIngrediente() == ingrediente).findFirst().orElseThrow();
        assertEquals(0, control.catalogo().precioVigente(producto).compareTo(n("16")));

        control.catalogo().eliminarDetalleMenu(detalle);
        control.catalogo().eliminarPlato(plato);
        control.catalogo().eliminarReceta(receta);
        control.catalogo().eliminarMenu(menu);
        control.catalogo().eliminarProducto(producto);
        control.catalogo().eliminarCategoria(categoria);
        control.inventario().eliminarIngrediente(ingrediente);
        assertFalse(control.catalogo().productos().contains(producto));
        assertFalse(control.catalogo().categorias().contains(categoria));
    }

    @Test void ingredientesProveedoresYComprasPendientesConservanElHistorialCorrecto() {
        control.inventario().guardarIngrediente(0, "Canela", "g", n("5"), n("0.10"));
        Ingrediente canela = control.inventario().ingredientes().stream()
                .filter(i -> i.getNombre().equals("Canela")).findFirst().orElseThrow();
        control.inventario().guardarIngrediente(canela.getId(), "Canela molida", "kg", n("1"), n("100"));
        assertEquals("kg", canela.getUnidadMedida(), "Sin stock ni historial sí puede corregirse la unidad.");
        control.inventario().eliminarIngrediente(canela);

        int proveedoresIniciales = control.inventario().proveedores().size();
        control.inventario().guardarProveedor(0, "Proveedor temporal", "", "", "", true);
        Proveedor temporal = control.inventario().proveedores().stream()
                .filter(p -> p.getNombre().equals("Proveedor temporal")).findFirst().orElseThrow();
        control.inventario().guardarProveedor(temporal.getId(), "Proveedor corregido", "", "", "", false);
        assertFalse(temporal.isActivo());
        control.inventario().eliminarProveedor(temporal);
        assertEquals(proveedoresIniciales, control.inventario().proveedores().size());

        Proveedor proveedor = control.inventario().proveedores().getFirst();
        Ingrediente ingrediente = control.inventario().ingredientes().getFirst();
        Compra compra = control.inventario().guardarCompra(proveedor,
                List.of(new DetalleCompra(n("3"), n("2.50"), ingrediente)));
        assertEquals(EstadoCompra.PENDIENTE, compra.getEstado());
        control.inventario().cancelarCompra(compra);
        assertEquals(EstadoCompra.CANCELADA, compra.getEstado());
        assertThrows(IllegalArgumentException.class, () -> control.inventario().recibirCompra(compra));
        assertThrows(IllegalArgumentException.class, () -> control.inventario().eliminarProveedor(proveedor));
    }

    @Test void pedidoParaRecogerSeEditaAntesDelPagoYConservaSuTipo() {
        Cliente cliente = control.atencion().clientes().getFirst();
        Producto producto = control.catalogo().productos().getFirst();
        LocalDateTime hora = LocalDateTime.now().plusHours(1);
        Pedido pedido = control.pedidos().guardar(0, TipoEntrega.RECOGER, cliente, null, "",
                BigDecimal.ZERO, hora, "Sin azúcar", List.of(linea(producto, 1)));
        assertInstanceOf(PedidoRecoger.class, pedido);
        assertEquals("R-" + pedido.getId(), ((PedidoRecoger) pedido).getCodigoRetiro());

        Pedido editado = control.pedidos().guardar(pedido.getId(), TipoEntrega.RECOGER, cliente, null, "",
                BigDecimal.ZERO, hora.plusMinutes(15), "Con tapa", List.of(linea(producto, 2)));
        assertEquals(2, editado.getDetalles().getFirst().getCantidad());
        assertEquals(0, editado.getTotal().compareTo(n("30.00")));
        assertSame(editado, control.pedidos().pedidos().getFirst());
        assertThrows(IllegalArgumentException.class, () -> control.pedidos().guardar(0, TipoEntrega.RECOGER,
                null, null, "", BigDecimal.ZERO, null, "", List.of(linea(producto, 1))));

        control.finanzas().abrir(BigDecimal.ZERO);
        control.finanzas().cobrar(editado, MetodoPago.TARJETA, n("5"));
        assertThrows(IllegalArgumentException.class, () -> control.pedidos().guardar(editado.getId(),
                TipoEntrega.RECOGER, cliente, null, "", BigDecimal.ZERO, hora, "", List.of(linea(producto, 1))));
    }

    @Test void pagosElectronicosNoCambianEfectivoYElTicketDistingueCuentaYComprobante() {
        Mesa mesa = control.atencion().mesas().getFirst();
        Producto producto = control.catalogo().productos().getFirst();
        Pedido pedido = control.pedidos().guardar(0, TipoEntrega.CONSUMIR_LOCAL, null, mesa, "",
                BigDecimal.ZERO, null, "", List.of(linea(producto, 1)));
        control.finanzas().abrir(n("100"));
        String provisional = control.finanzas().ticket(pedido);
        assertTrue(provisional.contains("Cuenta provisional"));
        control.finanzas().cobrar(pedido, MetodoPago.TARJETA, n("5"));
        assertEquals(0, control.finanzas().saldo(control.finanzas().abierta()).compareTo(n("100")));
        assertThrows(IllegalArgumentException.class,
                () -> control.finanzas().cobrar(pedido, MetodoPago.TRANSFERENCIA, n("11")));
        control.finanzas().cobrar(pedido, MetodoPago.TRANSFERENCIA, n("10"));
        assertEquals(0, control.finanzas().saldo(control.finanzas().abierta()).compareTo(n("100")));
        String finalizado = control.finanzas().ticket(pedido);
        assertTrue(finalizado.contains("Comprobante DEMO-"));
        assertTrue(finalizado.contains("Pendiente: Q0.00"));

        control.finanzas().registrarMovimiento(TipoMovimientoCaja.INGRESO, "Cambio añadido", n("20"));
        control.finanzas().registrarMovimiento(TipoMovimientoCaja.EGRESO, "Retiro", n("10"));
        assertEquals(0, control.finanzas().saldo(control.finanzas().abierta()).compareTo(n("110")));
    }

    @Test void todosLosReportesSeGeneranConIntervalosValidos() {
        LocalDate hoy = LocalDate.now();
        for (ReporteService.Tipo tipo : ReporteService.Tipo.values()) {
            String reporte = control.reportes().generar(tipo, hoy.minusDays(1), hoy.plusDays(1));
            assertTrue(reporte.contains(tipo.toString().replace('_', ' ')));
            assertTrue(reporte.contains("Datos temporales"));
        }
        assertThrows(IllegalArgumentException.class,
                () -> control.reportes().generar(ReporteService.Tipo.VENTAS, hoy, hoy.minusDays(1)));
    }

    @Test void empleadoPuedeEditarSinCambiarClavePeroNoDesactivarSuPropiaAdministracion() {
        Empleado empleado = control.sesion().guardarEmpleado(0, "Carlos", "Méndez", "carlos",
                "1234", "", "", RolEmpleado.CAJERO, true);
        String clave = empleado.getPassword();
        control.sesion().guardarEmpleado(empleado.getId(), "Carlos A.", "Méndez", "carlos",
                "", "", "", RolEmpleado.CAJERO, true);
        assertEquals(clave, empleado.getPassword());
        assertThrows(IllegalArgumentException.class, () -> control.sesion().guardarEmpleado(0,
                "Otro", "Usuario", "CARLOS", "1234", "", "", RolEmpleado.MESERO, true));

        Empleado admin = control.sesion().actual();
        assertThrows(IllegalArgumentException.class, () -> control.sesion().guardarEmpleado(admin.getId(),
                admin.getNombre(), admin.getApellido(), admin.getNombreUsuario(), "",
                admin.getTelefono(), admin.getEmail(), RolEmpleado.MESERO, true));
        assertThrows(IllegalArgumentException.class, () -> control.sesion().guardarEmpleado(admin.getId(),
                admin.getNombre(), admin.getApellido(), admin.getNombreUsuario(), "",
                admin.getTelefono(), admin.getEmail(), RolEmpleado.ADMINISTRADOR, false));
    }
}
