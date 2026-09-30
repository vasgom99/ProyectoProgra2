package com.garagecoffee.view;

import com.garagecoffee.controller.RestauranteController;
import com.garagecoffee.model.*;
import com.garagecoffee.service.FinanzasService;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
// Diseño editable en NetBeans: abrir este archivo y elegir Diseño.
public class CajaPanel extends PanelModulo {

    public CajaPanel() { initComponents(); }

    private RestauranteController control;
    private FinanzasService servicio;
    private boolean mostrarPagadas;
    public CajaPanel(RestauranteController control) {
        this();
        this.control = control; servicio = control.finanzas();
        cuentas.configurar(new String[]{"Pedido", "Cliente", "Entrega", "Estado", "Total Q", "Pendiente Q"},
                () -> control.pedidos().pedidos().stream().filter(p -> p.getEstado() != EstadoPedido.CANCELADO)
                        .filter(p -> mostrarPagadas || servicio.pendiente(p).signum() > 0).toList(),
                p -> new Object[]{p.getId(), Ui.nombre(p.getCliente()), Ui.nombre(p.getTipoEntrega()), Ui.nombre(p.getEstado()), p.getTotal(), servicio.pendiente(p)});
        cuentas.accion("Cobrar", this::cobrar);
        cuentas.accion("Cuenta / comprobante", p -> Ui.mostrarTexto(this, "Comprobante académico", servicio.ticket(p)));
        registrar(cuentas);
        cajas.configurar(new String[]{"Caja", "Apertura", "Cierre", "Estado", "Inicial Q", "Esperado Q", "Contado Q", "Diferencia Q"},
                servicio::cajas, c -> new Object[]{c.getId(), Ui.fecha(c.getFechaApertura()), Ui.fecha(c.getFechaCierre()), Ui.nombre(c.getEstado()),
                        c.getMontoInicial(), servicio.saldo(c), c.getMontoFinal(), c.getMontoFinal() == null ? null : c.getMontoFinal().subtract(servicio.saldo(c))});
        cajas.boton("Abrir caja", () -> {
            Formulario f = new AperturaCajaForm(); var inicial = f.texto("Efectivo inicial Q", "0.00");
            f.guardar(this, "Apertura de caja", () -> { servicio.abrir(f.decimal(inicial)); recargar(); });
        });
        cajas.boton("Cerrar caja abierta", () -> {
            Caja caja = servicio.abierta();
            if (caja == null) throw new IllegalArgumentException("No hay una caja abierta.");
            Formulario f = new CierreCajaForm();
            f.campo("Saldo esperado", new JLabel(Ui.dinero(servicio.saldo(caja))));
            var contado = f.texto("Efectivo contado Q", "");
            f.guardar(this, "Cierre de caja #" + caja.getId(), () -> {
                java.math.BigDecimal monto = f.decimal(contado);
                if (monto.signum() < 0 || monto.stripTrailingZeros().scale() > 2) throw new IllegalArgumentException("Indica un monto válido, con hasta dos decimales.");
                if (!Ui.confirmar(this, "¿Cerrar definitivamente esta caja con " + Ui.dinero(monto) + " contados?")) return;
                java.math.BigDecimal diferencia = servicio.cerrar(monto); recargar();
                JOptionPane.showMessageDialog(this, "Caja cerrada. Diferencia: " + Ui.dinero(diferencia));
            });
        });
        registrar(cajas);
        movimientos.configurar(new String[]{"ID", "Caja", "Fecha", "Tipo", "Concepto", "Monto Q"},
                servicio::movimientos, m -> new Object[]{m.getId(), m.getCaja().getId(), Ui.fecha(m.getFechaHora()), Ui.nombre(m.getTipo()), m.getConcepto(), m.getMonto()});
        movimientos.boton("Movimiento manual", () -> {
            Formulario f = new MovimientoCajaForm();
            var tipo = f.combo("Tipo", List.of(TipoMovimientoCaja.values()), TipoMovimientoCaja.INGRESO);
            var concepto = f.texto("Concepto", ""); var monto = f.texto("Monto Q", "");
            f.campo("Uso", new JLabel("<html>Aportes, retiros o pagos a proveedores.<br>No repitas aquí cobros o gastos ya registrados.</html>"));
            f.guardar(this, "Movimiento de efectivo", () -> { servicio.registrarMovimiento(f.elegido(tipo), f.valor(concepto), f.decimal(monto)); recargar(); });
        });
        registrar(movimientos);
        gastos.configurar(new String[]{"ID", "Fecha", "Categoría", "Descripción", "Monto Q"},
                servicio::gastos, g -> new Object[]{g.getId(), Ui.fecha(g.getFechaHora()), g.getCategoria(), g.getDescripcion(), g.getMonto()});
        gastos.boton("Registrar gasto", () -> {
            Formulario f = new GastoForm();
            var categoria = f.texto("Categoría", ""); var descripcion = f.texto("Descripción", ""); var monto = f.texto("Monto Q", "");
            f.campo("Pago", new JLabel("<html>Este gasto sale del efectivo de caja.<br>No registres compras de ingredientes como gasto:<br>su costo ya se calcula al preparar pedidos.</html>"));
            f.guardar(this, "Gasto pagado en efectivo", () -> { servicio.registrarGasto(f.valor(categoria), f.valor(descripcion), f.decimal(monto)); recargar(); });
        });
        registrar(gastos);
        pagos.configurar(new String[]{"ID", "Pedido", "Fecha", "Método", "Aplicado Q", "Cambio Q"},
                servicio::pagos, p -> new Object[]{p.getId(), p.getPedido().getId(), Ui.fecha(p.getFechaHora()), Ui.nombre(p.getMetodo()), p.getMonto(), p.getCambio()});
        registrar(pagos);
        facturas.configurar(new String[]{"Número", "Pedido", "Fecha", "Total Q"},
                servicio::facturas, f -> new Object[]{f.getNumero(), f.getPedido().getId(), Ui.fecha(f.getFecha()), f.getTotal()});
        facturas.accion("Ver / imprimir", f -> Ui.mostrarTexto(this, "Comprobante sin validez fiscal", servicio.ticket(f.getPedido())));
        registrar(facturas);
        cuentas.alternar("Ver también pagadas", "Solo pendientes", mostrar -> mostrarPagadas = mostrar);
        opcionesSecundarias(pestanas, cuentas, movimientos, gastos, pagos, facturas);
        
    }
    private void cobrar(Pedido pedido) {
        java.math.BigDecimal pendiente = servicio.pendiente(pedido);
        if (pendiente.signum() <= 0) throw new IllegalArgumentException("El pedido ya está pagado.");
        Formulario f = new CobroForm();
        f.campo("Pendiente", new JLabel(Ui.dinero(pendiente)));
        var metodo = f.combo("Método", List.of(MetodoPago.values()), MetodoPago.EFECTIVO);
        var monto = f.texto("Monto recibido Q (permite abonos)", pendiente);
        var abono = f.check("Abono", false);
        Runnable calcular = () -> {
            try {
                java.math.BigDecimal recibido = f.decimal(monto);
                if (recibido.signum() <= 0 || (!abono.isSelected() && recibido.compareTo(pendiente) < 0)) {
                    f.etiqueta("Resultado", "Indica el total o marca la opción de abono."); return;
                }
                if (f.elegido(metodo) != MetodoPago.EFECTIVO && recibido.compareTo(pendiente) > 0) {
                    f.etiqueta("Resultado", "Un pago no efectivo no puede superar el saldo."); return;
                }
                f.etiqueta("Resultado", "Cambio: " + Ui.dinero(recibido.subtract(pendiente).max(java.math.BigDecimal.ZERO))
                        + " · Pendiente: " + Ui.dinero(pendiente.subtract(recibido).max(java.math.BigDecimal.ZERO)));
            } catch (IllegalArgumentException ex) { f.etiqueta("Resultado", "Indica un monto válido."); }
        };
        Runnable ajustarMonto = () -> {
            monto.setEditable(abono.isSelected() || f.elegido(metodo) == MetodoPago.EFECTIVO);
            if (!abono.isSelected()) monto.setText(pendiente.toPlainString());
            calcular.run();
        };
        metodo.addActionListener(e -> ajustarMonto.run());
        abono.addActionListener(e -> ajustarMonto.run());
        monto.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { calcular.run(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { calcular.run(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { calcular.run(); }
        });
        ajustarMonto.run();
        f.guardar(this, "Cobro del pedido #" + pedido.getId(), () -> {
            if (!abono.isSelected() && f.decimal(monto).compareTo(pendiente) < 0)
                throw new IllegalArgumentException("Para pagar menos del total, marca «Registrar solo un abono».");
            Pago pago = servicio.cobrar(pedido, f.elegido(metodo), f.decimal(monto)); recargar();
            resumen.setText("Cobro registrado · Pedido #" + pedido.getId() + " · Cambio: " + Ui.dinero(pago.getCambio())
                    + " · Pendiente: " + Ui.dinero(servicio.pendiente(pedido)) + " · El comprobante está en la cuenta.");
        });
    }
    @Override public void recargar() {
        super.recargar();
        Caja caja = servicio.abierta();
        resumen.setText(caja == null ? "Caja cerrada — abre una caja para cobrar o registrar gastos."
                : "Caja #" + caja.getId() + " ABIERTA · Efectivo esperado: " + Ui.dinero(servicio.saldo(caja)) + " · Tarjetas y transferencias no aumentan el efectivo.");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        pestanas = new javax.swing.JTabbedPane();
        cuentas = new com.garagecoffee.view.PanelListado<>();
        cajas = new com.garagecoffee.view.PanelListado<>();
        movimientos = new com.garagecoffee.view.PanelListado<>();
        gastos = new com.garagecoffee.view.PanelListado<>();
        pagos = new com.garagecoffee.view.PanelListado<>();
        facturas = new com.garagecoffee.view.PanelListado<>();
        resumen = new javax.swing.JLabel();
        this.setBackground(new java.awt.Color(246, 244, 238));
        this.setLayout(new java.awt.BorderLayout(12, 12));
        pestanas.setFont(new java.awt.Font("SansSerif", 0, 14));
        pestanas.setBackground(new java.awt.Color(246, 244, 238));
        pestanas.addTab("Cobros", cuentas);
        pestanas.addTab("Apertura y cierre", cajas);
        pestanas.addTab("Movimientos", movimientos);
        pestanas.addTab("Gastos", gastos);
        pestanas.addTab("Pagos", pagos);
        pestanas.addTab("Comprobantes", facturas);
        this.add(pestanas, java.awt.BorderLayout.CENTER);
        resumen.setText("Selecciona una fila para consultar sus acciones.");
        resumen.setFont(new java.awt.Font("SansSerif", 0, 12));
        resumen.setForeground(new java.awt.Color(109, 121, 114));
        this.add(resumen, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane pestanas;
    private com.garagecoffee.view.PanelListado<Pedido> cuentas;
    private com.garagecoffee.view.PanelListado<Caja> cajas;
    private com.garagecoffee.view.PanelListado<MovimientoCaja> movimientos;
    private com.garagecoffee.view.PanelListado<Gasto> gastos;
    private com.garagecoffee.view.PanelListado<Pago> pagos;
    private com.garagecoffee.view.PanelListado<Factura> facturas;
    private javax.swing.JLabel resumen;
    // End of variables declaration//GEN-END:variables
}
