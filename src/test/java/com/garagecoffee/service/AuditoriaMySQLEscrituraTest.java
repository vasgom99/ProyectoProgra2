package com.garagecoffee.service;

import com.garagecoffee.controller.RestauranteController;
import com.garagecoffee.dao.EmpleadoDAO;
import com.garagecoffee.database.ConexionDB;
import com.garagecoffee.model.*;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

// Prueba opcional del recorrido completo en MySQL. Todo se revierte al terminar.
class AuditoriaMySQLEscrituraTest {
    private static final class RetrocesoDeAuditoria extends RuntimeException { }
    private static BigDecimal n(String valor) { return new BigDecimal(valor); }

    @Test void pedidoCompraInventarioCajaYCobroSeGuardanEnUnaTransaccionReal() throws Exception {
        assumeTrue(Boolean.getBoolean("garage.test.mysql"));
        String usuario = "__audit_tx_" + System.nanoTime();
        assertThrows(RetrocesoDeAuditoria.class, () -> ConexionDB.transaccion(() -> {
            Empleado empleado = new Empleado(0, "Auditoría", "Temporal", "", "", usuario,
                    Claves.proteger("audit-temp-2026".toCharArray()), RolEmpleado.ADMINISTRADOR, true);
            assertTrue(new EmpleadoDAO().guardar(empleado));

            RestauranteController control = new RestauranteController(new DatosMySQL());
            control.sesion().iniciarSesion(usuario, "audit-temp-2026".toCharArray());

            Categoria categoria = control.catalogo().guardarCategoria(0, "Categoría " + usuario,
                    TipoCategoria.COMIDA, true);
            control.inventario().guardarIngrediente(0, "Ingrediente " + usuario, "unidad", n("5"), n("2"));
            Ingrediente ingrediente = control.inventario().ingredientes().stream()
                    .filter(i -> i.getNombre().equals("Ingrediente " + usuario)).findFirst().orElseThrow();
            control.inventario().registrarMovimiento(ingrediente, TipoMovimientoStock.ENTRADA, n("20"), "Carga de auditoría");
            Producto producto = control.catalogo().guardarProducto(0, "Producto " + usuario, "", "",
                    n("25"), categoria, true);
            control.catalogo().guardarReceta(0, producto, ingrediente, n("1"));

            int numeroMesa = control.atencion().mesas().stream().mapToInt(Mesa::getNumero).max().orElse(0) + 1000;
            control.atencion().guardarMesa(0, numeroMesa, 4, true);
            Mesa mesa = control.atencion().mesas().stream().filter(m -> m.getNumero() == numeroMesa).findFirst().orElseThrow();
            control.inventario().guardarProveedor(0, "Proveedor " + usuario, "", "", "", true);
            Proveedor proveedor = control.inventario().proveedores().stream()
                    .filter(p -> p.getNombre().equals("Proveedor " + usuario)).findFirst().orElseThrow();

            control.finanzas().abrir(n("100"));
            Compra compra = control.registrarCompra(proveedor,
                    List.of(new DetalleCompra(n("5"), n("2"), ingrediente)), true, true);
            assertEquals(EstadoCompra.RECIBIDA, compra.getEstado());

            Pedido pedido = control.pedidos().guardar(0, TipoEntrega.CONSUMIR_LOCAL, null, mesa, "",
                    BigDecimal.ZERO, null, "", List.of(new DetallePedido(0, 2, producto.getPrecio(), producto)));
            control.pedidos().preparar(pedido);
            control.pedidos().listo(pedido);
            control.pedidos().entregar(pedido);
            control.finanzas().cobrar(pedido, MetodoPago.EFECTIVO, pedido.getTotal());
            assertEquals(0, control.finanzas().pendiente(pedido).signum());
            assertEquals(EstadoMesa.LIBRE, mesa.getEstado());
            assertTrue(control.finanzas().ticket(pedido).contains("Comprobante GC-"));

            // Obliga a MySQL a deshacer también el empleado y todos los datos creados arriba.
            throw new RetrocesoDeAuditoria();
        }));
        assertNull(new EmpleadoDAO().buscarPorNombreUsuario(usuario), "La auditoría no debe dejar datos temporales.");
    }
}
