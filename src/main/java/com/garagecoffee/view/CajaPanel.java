package com.garagecoffee.view;

import com.garagecoffee.controller.RestauranteController;
import com.garagecoffee.model.Caja;
import com.garagecoffee.model.EstadoPedido;
import com.garagecoffee.model.Pago;
import com.garagecoffee.model.Pedido;
import com.garagecoffee.model.PedidoLocal;
import com.garagecoffee.service.FinanzasService;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class CajaPanel extends PanelModulo {
    private FinanzasService servicio;

    public CajaPanel() { initComponents(); }

    public CajaPanel(RestauranteController control) {
        this();
        servicio = control.finanzas();
        cuentas.configurar(new String[]{"Pedido", "Mesa", "Estado", "Total Q", "Pendiente Q"},
                () -> control.pedidos().pedidos().stream()
                        .filter(p -> p.getEstado() != EstadoPedido.CANCELADO).toList(),
                p -> new Object[]{p.getId(), ((PedidoLocal) p).getMesa().getNumero(),
                        Ui.nombre(p.getEstado()), p.getTotal(), servicio.pendiente(p)});
        cuentas.accion("Cobrar", this::cobrar);
        cuentas.accion("Comprobante", p -> Ui.mostrarTexto(this, "Comprobante", servicio.ticket(p)));
        registrar(cuentas);

        cajas.configurar(new String[]{"Caja", "Apertura", "Cierre", "Estado", "Inicial Q", "Esperado Q", "Contado Q"},
                servicio::cajas, c -> new Object[]{c.getId(), Ui.fecha(c.getFechaApertura()),
                        Ui.fecha(c.getFechaCierre()), Ui.nombre(c.getEstado()), c.getMontoInicial(),
                        servicio.saldo(c), c.getMontoFinal()});
        cajas.boton("Abrir caja", () -> {
            Formulario f = new AperturaCajaForm();
            var inicial = f.texto("Efectivo inicial Q", "0.00");
            f.guardar(this, "Apertura de caja", () -> { servicio.abrir(f.decimal(inicial)); recargar(); });
        });
        cajas.boton("Cerrar caja", () -> {
            Caja caja = servicio.abierta();
            if (caja == null) throw new IllegalArgumentException("No hay una caja abierta.");
            Formulario f = new CierreCajaForm();
            f.campo("Saldo esperado", new JLabel(Ui.dinero(servicio.saldo(caja))));
            var contado = f.texto("Efectivo contado Q", "");
            f.guardar(this, "Cierre de caja #" + caja.getId(), () -> {
                var monto = f.decimal(contado);
                var diferencia = servicio.cerrar(monto);
                recargar();
                JOptionPane.showMessageDialog(this, "Diferencia: " + Ui.dinero(diferencia));
            });
        });
        registrar(cajas);
    }

    private void cobrar(Pedido pedido) {
        var pendiente = servicio.pendiente(pedido);
        if (pendiente.signum() <= 0) throw new IllegalArgumentException("El pedido ya está pagado.");
        Formulario f = new CobroForm();
        f.campo("Total", new JLabel(Ui.dinero(pendiente)));
        var recibido = f.texto("Efectivo recibido Q", pendiente);
        Runnable actualizar = () -> {
            try {
                var monto = f.decimal(recibido);
                f.etiqueta("Cambio", monto.compareTo(pendiente) < 0 ? "Falta efectivo para cubrir el total."
                        : Ui.dinero(monto.subtract(pendiente)));
            } catch (IllegalArgumentException ex) {
                f.etiqueta("Cambio", "Indica un monto válido.");
            }
        };
        recibido.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { actualizar.run(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { actualizar.run(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { actualizar.run(); }
        });
        actualizar.run();
        f.guardar(this, "Cobro del pedido #" + pedido.getId(), () -> {
            Pago pago = servicio.cobrar(pedido, f.decimal(recibido));
            recargar();
            resumen.setText("Pedido #" + pedido.getId() + " cobrado · Cambio: " + Ui.dinero(pago.getCambio()));
        });
    }

    @Override public void recargar() {
        super.recargar();
        Caja caja = servicio.abierta();
        resumen.setText(caja == null ? "Caja cerrada. Ábrela para cobrar."
                : "Caja #" + caja.getId() + " abierta · Efectivo esperado: " + Ui.dinero(servicio.saldo(caja)));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        pestanas = new javax.swing.JTabbedPane();
        cuentas = new com.garagecoffee.view.PanelListado<>();
        cajas = new com.garagecoffee.view.PanelListado<>();
        resumen = new javax.swing.JLabel();
        this.setBackground(new java.awt.Color(246, 244, 238));
        this.setLayout(new java.awt.BorderLayout(12, 12));
        pestanas.setFont(new java.awt.Font("SansSerif", 0, 14));
        pestanas.setBackground(new java.awt.Color(246, 244, 238));
        pestanas.addTab("Cobros", cuentas);
        pestanas.addTab("Caja", cajas);
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
    private javax.swing.JLabel resumen;
    // End of variables declaration//GEN-END:variables
}
