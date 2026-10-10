package com.garagecoffee;

import com.garagecoffee.controller.RestauranteController;
import com.garagecoffee.database.ConexionDB;
import com.garagecoffee.model.*;
import com.garagecoffee.service.DatosMySQL;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

// Prueba opcional de lectura real; no modifica la base.
class MySQLConexionTest {
    private static class RevertirPrueba extends RuntimeException { }

    @Test
    void cargaEsquemaYPermiteAccesoDeEjemplo() {
        assumeTrue(Boolean.getBoolean("garage.test.mysql"));
        RestauranteController control = new RestauranteController(new DatosMySQL());
        assertNotNull(control.sesion().iniciarSesion("admin.demo", "demo".toCharArray()));
        assertFalse(control.catalogo().productos().isEmpty());
        assertFalse(control.atencion().mesas().isEmpty());
    }

    @Test
    void guardaPedidoYRespuestaDeCajaEnUnaTransaccionReversible() throws Exception {
        assumeTrue(Boolean.getBoolean("garage.test.mysql"));
        RestauranteController control = new RestauranteController(new DatosMySQL());
        control.sesion().iniciarSesion("admin.demo", "demo".toCharArray());
        int pedidosIniciales = control.pedidos().pedidos().size();
        Mesa mesa = control.atencion().mesas().stream()
                .filter(m -> m.getEstado() == EstadoMesa.LIBRE).findFirst().orElseThrow();
        Producto producto = control.catalogo().productos().getFirst();
        assertThrows(RevertirPrueba.class, () -> ConexionDB.transaccion(() -> {
            Pedido pedido = control.pedidos().guardar(mesa, "Prueba reversible",
                    List.of(new DetallePedido(0, 1, producto.getPrecio(), producto)));
            control.pedidos().preparar(pedido);
            control.pedidos().listo(pedido);
            control.pedidos().entregar(pedido);
            if (control.finanzas().abierta() == null) control.finanzas().abrir(BigDecimal.ZERO);
            Pago pago = control.finanzas().cobrar(pedido, pedido.getTotal());
            assertTrue(pedido.getId() > 0);
            assertTrue(pago.getId() > 0);
            assertEquals(EstadoMesa.LIBRE, mesa.getEstado());
            throw new RevertirPrueba();
        }));
        RestauranteController despues = new RestauranteController(new DatosMySQL());
        despues.sesion().iniciarSesion("admin.demo", "demo".toCharArray());
        assertEquals(pedidosIniciales, despues.pedidos().pedidos().size());
    }
}
