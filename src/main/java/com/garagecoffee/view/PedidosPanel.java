package com.garagecoffee.view;

import com.garagecoffee.controller.RestauranteController;
import com.garagecoffee.model.*;
import javax.swing.SwingUtilities;
// Diseño editable en NetBeans: abrir este archivo y elegir Diseño.
public class PedidosPanel extends PanelModulo {

    public PedidosPanel() { initComponents(); }

    public PedidosPanel(RestauranteController control, boolean soloDomicilio) {
        this();
        pedidos.configurar(new String[]{"ID", "Entrega", "Cliente", "Mesa / dirección", "Programado", "Estado", "Total Q", "Pendiente Q"},
                () -> control.pedidos().pedidos().stream().filter(p -> !soloDomicilio || p instanceof PedidoDomicilio).toList(),
                p -> new Object[]{p.getId(), Ui.nombre(p.getTipoEntrega()), Ui.nombre(p.getCliente()), destino(p), Ui.fecha(p.getFechaProgramada()),
                        Ui.nombre(p.getEstado()), p.getTotal(), control.finanzas().pendiente(p)});
        if (control.sesion().permite(RolEmpleado.MESERO, RolEmpleado.CAJERO)) {
            pedidos.boton("Nuevo", () -> new PedidoDialog(SwingUtilities.getWindowAncestor(this), control, null).setVisible(true));
            pedidos.accion("Editar", p -> {
                if (p.getEstado() != EstadoPedido.PENDIENTE || control.finanzas().pendiente(p).compareTo(p.getTotal()) != 0)
                    throw new IllegalArgumentException("Solo se puede editar un pedido pendiente sin pagos.");
                new PedidoDialog(SwingUtilities.getWindowAncestor(this), control, p).setVisible(true);
            });
            pedidos.accion("Cancelar", p -> {
                if (Ui.confirmar(this, "¿Cancelar el pedido #" + p.getId() + "? Solo se permiten pedidos pendientes sin pagos.")) control.pedidos().cancelar(p);
            });
            pedidos.accion("Asignar repartidor", p -> {
                if (!(p instanceof PedidoDomicilio)) throw new IllegalArgumentException("Selecciona un pedido a domicilio.");
                Formulario f = new AsignarRepartidorForm();
                var repartidor = f.combo("Repartidor activo", control.atencion().repartidores().stream().filter(Repartidor::isActivo).toList(), null);
                f.guardar(this, "Asignar reparto", () -> control.pedidos().asignar(p, f.elegido(repartidor)));
            });
        }
        pedidos.accion("Entregar", p -> {
            if (Ui.confirmar(this, "¿Marcar el pedido #" + p.getId() + " como entregado?")) control.pedidos().entregar(p);
        });
        pedidos.accion("Detalle", p -> Ui.mostrarTexto(this, "Detalle del pedido", detalle(p)));
        registrar(pedidos);
        pestanas.setTitleAt(0, soloDomicilio ? "Pedidos a domicilio" : "Todos los pedidos");
        
    }
    static String destino(Pedido p) {
        if (p instanceof PedidoLocal local) return Ui.nombre(local.getMesa());
        if (p instanceof PedidoDomicilio d) return d.getDireccionEntrega();
        return "Código " + ((PedidoRecoger) p).getCodigoRetiro();
    }
    static String detalle(Pedido p) {
        StringBuilder texto = new StringBuilder("Pedido #" + p.getId() + " · " + Ui.nombre(p.getEstado()) + "\n");
        texto.append(Ui.nombre(p.getCliente())).append("\n").append(destino(p)).append("\n");
        texto.append("Creado: ").append(Ui.fecha(p.getFechaCreacion())).append("\nProgramado: ").append(Ui.fecha(p.getFechaProgramada())).append("\n");
        if (p instanceof PedidoDomicilio d) texto.append("Repartidor: ").append(Ui.nombre(d.getRepartidor())).append("\n");
        texto.append("Observaciones: ").append(p.getObservaciones()).append("\n\n");
        for (DetallePedido d : p.getDetalles()) texto.append(d.getCantidad()).append(" x ").append(d.getProducto().getNombre())
                .append(" = ").append(Ui.dinero(d.getSubtotal())).append("\n");
        if (p instanceof PedidoDomicilio d) texto.append("Envío: ").append(Ui.dinero(d.getCostoEnvio())).append("\n");
        return texto.append("\nTOTAL: ").append(Ui.dinero(p.getTotal())).append("\nDEMOSTRACIÓN — sin guardado permanente.").toString();
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
        lblAyuda.setText("La cocina inicia la preparación y marca «Listo». La cancelación solo está habilitada antes de preparar y cobrar.");
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
