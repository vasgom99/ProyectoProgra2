package com.garagecoffee.view;

import com.garagecoffee.controller.RestauranteController;
import com.garagecoffee.model.*;
public class CocinaPanel extends PanelModulo {

    public CocinaPanel() { initComponents(); }

    public CocinaPanel(RestauranteController control) {
        this();
        cola.configurar(new String[]{"Pedido", "Creado", "Estado", "Productos", "Observaciones"},
                () -> control.pedidos().pedidos().stream().filter(p -> p.getEstado() == EstadoPedido.PENDIENTE || p.getEstado() == EstadoPedido.EN_PREPARACION || p.getEstado() == EstadoPedido.LISTO)
                        .sorted(java.util.Comparator.comparing(Pedido::getFechaCreacion)).toList(),
                p -> new Object[]{p.getId(), Ui.fecha(p.getFechaCreacion()), Ui.nombre(p.getEstado()),
                        p.getDetalles().stream().map(d -> d.getCantidad() + " x " + d.getProducto().getNombre()).collect(java.util.stream.Collectors.joining(", ")), p.getObservaciones()});
        cola.accion("Iniciar preparación", control.pedidos()::preparar);
        cola.accion("Marcar listo", control.pedidos()::listo);
        cola.accion("Detalle", p -> Ui.mostrarTexto(this, "Comanda", PedidosPanel.detalle(p), false));
        registrar(cola);
        
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        pestanas = new javax.swing.JTabbedPane();
        cola = new com.garagecoffee.view.PanelListado<>();
        this.setBackground(new java.awt.Color(246, 244, 238));
        this.setLayout(new java.awt.BorderLayout(12, 12));
        pestanas.setFont(new java.awt.Font("SansSerif", 0, 14));
        pestanas.setBackground(new java.awt.Color(246, 244, 238));
        pestanas.addTab("Cola de cocina", cola);
        this.add(pestanas, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane pestanas;
    private com.garagecoffee.view.PanelListado<Pedido> cola;
    // End of variables declaration//GEN-END:variables
}
