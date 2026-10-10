package com.garagecoffee;

import com.garagecoffee.controller.RestauranteController;
import com.garagecoffee.model.*;
import com.garagecoffee.service.DatosDemo;
import com.garagecoffee.view.*;
import java.math.BigDecimal;
import java.util.List;
import javax.swing.SwingUtilities;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// Verifica el recorrido principal sin depender de MySQL ni modificar datos reales.
class FlujoAcademicoTest {
    private RestauranteController iniciar() {
        RestauranteController control = new RestauranteController(new DatosDemo());
        control.sesion().iniciarSesion("admin", "demo".toCharArray());
        return control;
    }

    @Test
    void pedidoLocalSePreparaEntregaYCobraSinReceta() {
        RestauranteController control = iniciar();
        Categoria categoria = control.catalogo().guardarCategoria(0, "Sin receta", TipoCategoria.COMIDA, true);
        Producto producto = control.catalogo().guardarProducto(0, "Plato sencillo", "", "",
                new BigDecimal("10.00"), categoria, true);
        Mesa mesa = control.atencion().mesas().getFirst();
        Pedido pedido = control.pedidos().guardar(mesa, "",
                List.of(new DetallePedido(0, 2, producto.getPrecio(), producto)));

        assertEquals(EstadoMesa.OCUPADA, mesa.getEstado());
        control.pedidos().preparar(pedido);
        control.pedidos().listo(pedido);
        control.pedidos().entregar(pedido);
        control.finanzas().abrir(BigDecimal.ZERO);
        Pago pago = control.finanzas().cobrar(pedido, new BigDecimal("25.00"));

        assertEquals(new BigDecimal("5.00"), pago.getCambio());
        assertEquals(0, control.finanzas().pendiente(pedido).signum());
        assertEquals(EstadoMesa.LIBRE, mesa.getEstado());
        assertEquals(1, control.finanzas().pagos().size());
        assertEquals(new BigDecimal("20.00"), control.finanzas().saldo(control.finanzas().abierta()));
    }

    @Test
    void noPermiteCobrarSinCaja() {
        RestauranteController control = iniciar();
        Producto producto = control.catalogo().productos().getFirst();
        Pedido pedido = control.pedidos().guardar(control.atencion().mesas().getFirst(), "",
                List.of(new DetallePedido(0, 1, producto.getPrecio(), producto)));
        assertThrows(IllegalArgumentException.class,
                () -> control.finanzas().cobrar(pedido, pedido.getTotal()));
        assertEquals(1, control.finanzas().pendiente(pedido).signum());
    }

    @Test
    void evitaPedidoDuplicadoEnMesaYNoCobraDosVeces() {
        RestauranteController control = iniciar();
        Producto producto = control.catalogo().productos().getFirst();
        Mesa mesa = control.atencion().mesas().getFirst();
        List<DetallePedido> detalles = List.of(new DetallePedido(0, 1, producto.getPrecio(), producto));
        Pedido pedido = control.pedidos().guardar(mesa, "", detalles);
        assertThrows(IllegalArgumentException.class, () -> control.pedidos().guardar(mesa, "", detalles));
        control.finanzas().abrir(BigDecimal.ZERO);
        assertThrows(IllegalArgumentException.class,
                () -> control.finanzas().cobrar(pedido, pedido.getTotal().subtract(BigDecimal.ONE)));
        Pago pago = control.finanzas().cobrar(pedido, pedido.getTotal());
        assertNotNull(pago);
        assertEquals(EstadoMesa.OCUPADA, mesa.getEstado());
        assertThrows(IllegalArgumentException.class, () -> control.finanzas().cobrar(pedido, pedido.getTotal()));
        control.pedidos().preparar(pedido);
        control.pedidos().listo(pedido);
        control.pedidos().entregar(pedido);
        assertEquals(EstadoMesa.LIBRE, mesa.getEstado());
    }

    @Test
    void panelesPrincipalesSeConstruyenConDatosTemporales() throws Exception {
        RestauranteController control = iniciar();
        SwingUtilities.invokeAndWait(() -> {
            new PedidoDatosForm();
            new LineaPedidoForm();
            new CobroForm();
            List<PanelModulo> paneles = List.of(new InicioPanel(control), new PedidosPanel(control),
                    new AtencionPanel(control), new CocinaPanel(control), new CatalogoPanel(control),
                    new CajaPanel(control), new EmpleadosPanel(control));
            paneles.forEach(PanelModulo::recargar);
        });
    }
}
