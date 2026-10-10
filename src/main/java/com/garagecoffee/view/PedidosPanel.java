package com.garagecoffee.view;

import com.garagecoffee.controller.RestauranteController;
import com.garagecoffee.model.DetallePedido;
import com.garagecoffee.model.Pedido;
import com.garagecoffee.model.PedidoLocal;
import javax.swing.SwingUtilities;

// Diseño editable en NetBeans: abrir este archivo y elegir Diseño.
public class PedidosPanel extends PanelModulo {
    public PedidosPanel() { initComponents(); }

    public PedidosPanel(RestauranteController control) {
        this();
        pedidos.configurar(new String[]{"ID", "Mesa", "Estado", "Total Q", "Pendiente Q"},
                control.pedidos()::pedidos,
                p -> new Object[]{p.getId(), ((PedidoLocal) p).getMesa().getNumero(),
                        Ui.nombre(p.getEstado()), p.getTotal(), control.finanzas().pendiente(p)});
        pedidos.boton("Nuevo pedido", () ->
                new PedidoDialog(SwingUtilities.getWindowAncestor(this), control, null).setVisible(true));
        pedidos.accion("Entregar", p -> {
            if (Ui.confirmar(this, "¿Marcar el pedido #" + p.getId() + " como entregado?"))
                control.pedidos().entregar(p);
        });
        pedidos.accion("Cancelar", p -> {
            if (Ui.confirmar(this, "¿Cancelar el pedido pendiente #" + p.getId() + "?"))
                control.pedidos().cancelar(p);
        });
        pedidos.accion("Detalle", p -> Ui.mostrarTexto(this, "Pedido", detalle(p)));
        registrar(pedidos);
    }

    static String detalle(Pedido pedido) {
        StringBuilder texto = new StringBuilder("Pedido #" + pedido.getId() + " · " + Ui.nombre(pedido.getEstado()));
        texto.append("\nMesa ").append(((PedidoLocal) pedido).getMesa().getNumero());
        texto.append("\nCreado: ").append(Ui.fecha(pedido.getFechaCreacion()));
        texto.append("\nObservaciones: ").append(pedido.getObservaciones()).append("\n\n");
        for (DetallePedido d : pedido.getDetalles())
            texto.append(d.getCantidad()).append(" x ").append(d.getProducto().getNombre())
                    .append(" = ").append(Ui.dinero(d.getSubtotal())).append("\n");
        return texto.append("\nTOTAL: ").append(Ui.dinero(pedido.getTotal())).toString();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        pestanas = new javax.swing.JTabbedPane();
        pedidos = new com.garagecoffee.view.PanelListado<>();
        lblAyuda = new javax.swing.JLabel();
        this.setBackground(new java.awt.Color(246, 244, 238));
        this.setLayout(new java.awt.BorderLayout(12, 12));
        pestanas.setFont(new java.awt.Font("SansSerif", 0, 14));
        pestanas.setBackground(new java.awt.Color(246, 244, 238));
        pestanas.addTab("Pedidos", pedidos);
        this.add(pestanas, java.awt.BorderLayout.CENTER);
        lblAyuda.setText("Cocina prepara y marca listo. Entrega el pedido antes o después del cobro.");
        lblAyuda.setFont(new java.awt.Font("SansSerif", 0, 12));
        lblAyuda.setForeground(new java.awt.Color(109, 121, 114));
        this.add(lblAyuda, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane pestanas;
    private com.garagecoffee.view.PanelListado<Pedido> pedidos;
    private javax.swing.JLabel lblAyuda;
    // End of variables declaration//GEN-END:variables
}
