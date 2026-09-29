package com.garagecoffee.view;

import com.garagecoffee.controller.RestauranteController;
import com.garagecoffee.model.*;
// Diseño editable en NetBeans: abrir este archivo y elegir Diseño.
public class CocinaPanel extends PanelModulo {

    public CocinaPanel() { initComponents(); }

    public CocinaPanel(RestauranteController control) {
        this();
        cola.configurar(new String[]{"Pedido", "Creado", "Programado", "Entrega", "Estado", "Productos", "Observaciones"},
                () -> control.pedidos().pedidos().stream().filter(p -> p.getEstado() == EstadoPedido.PENDIENTE || p.getEstado() == EstadoPedido.EN_PREPARACION || p.getEstado() == EstadoPedido.LISTO)
                        .sorted(java.util.Comparator.comparing(p -> p.getFechaProgramada() == null ? p.getFechaCreacion() : p.getFechaProgramada())).toList(),
                p -> new Object[]{p.getId(), Ui.fecha(p.getFechaCreacion()), Ui.fecha(p.getFechaProgramada()), Ui.nombre(p.getTipoEntrega()), Ui.nombre(p.getEstado()),
                        p.getDetalles().stream().map(d -> d.getCantidad() + " x " + d.getProducto().getNombre()).collect(java.util.stream.Collectors.joining(", ")), p.getObservaciones()});
        cola.accion("Iniciar preparación", p -> {
            if (Ui.confirmar(this, "¿Preparar el pedido #" + p.getId() + "? Se descontarán sus ingredientes ahora.")) control.pedidos().preparar(p);
        });
        cola.accion("Marcar listo", control.pedidos()::listo);
        cola.accion("Detalle", p -> Ui.mostrarTexto(this, "Comanda", PedidosPanel.detalle(p)));
        registrar(cola);
        
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        pestanas = new javax.swing.JTabbedPane();
        cola = new com.garagecoffee.view.PanelListado<>();
        lblAyuda = new javax.swing.JLabel();
        this.setBackground(new java.awt.Color(246, 244, 238));
        this.setLayout(new java.awt.BorderLayout(12, 12));
        pestanas.setFont(new java.awt.Font("SansSerif", 0, 14));
        pestanas.setBackground(new java.awt.Color(246, 244, 238));
        pestanas.addTab("Cola de cocina", cola);
        this.add(pestanas, java.awt.BorderLayout.CENTER);
        lblAyuda.setText("Ordenada por hora de creación o programada. Revisa esa hora antes de preparar un pedido futuro.");
        lblAyuda.setFont(new java.awt.Font("SansSerif", 0, 12));
        lblAyuda.setForeground(new java.awt.Color(109, 121, 114));
        this.add(lblAyuda, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane pestanas;
    private com.garagecoffee.view.PanelListado<Pedido> cola;
    private javax.swing.JLabel lblAyuda;
    // End of variables declaration//GEN-END:variables
}
