package com.garagecoffee.view;

import com.garagecoffee.controller.RestauranteController;
import com.garagecoffee.model.*;
import javax.swing.SwingUtilities;
// Diseño editable en NetBeans: abrir este archivo y elegir Diseño.
public class PedidosPanel extends PanelModulo {

    public PedidosPanel() { initComponents(); }
    private boolean historial;

    public PedidosPanel(RestauranteController control, boolean soloDomicilio) {
        this();
        pedidos.configurar(new String[]{"ID", "Entrega", "Cliente", "Mesa / dirección", "Programado", "Estado", "Total Q", "Pendiente Q"},
                () -> control.pedidos().pedidos().stream().filter(p -> !soloDomicilio || p instanceof PedidoDomicilio)
                        .filter(p -> historial || (p.getEstado() != EstadoPedido.CANCELADO
                                && (p.getEstado() != EstadoPedido.ENTREGADO || control.finanzas().pendiente(p).signum() > 0))).toList(),
                p -> new Object[]{p.getId(), Ui.nombre(p.getTipoEntrega()), Ui.nombre(p.getCliente()), destino(p), Ui.fecha(p.getFechaProgramada()),
                        Ui.nombre(p.getEstado()), p.getTotal(), control.finanzas().pendiente(p)});
        var secundarios = new java.util.ArrayList<javax.swing.JButton>();
        if (control.sesion().permite(RolEmpleado.MESERO, RolEmpleado.CAJERO)) {
            pedidos.boton("Nuevo", () -> new PedidoDialog(SwingUtilities.getWindowAncestor(this), control, null).setVisible(true));
            secundarios.add(pedidos.accion("Editar", p -> {
                if (p.getEstado() != EstadoPedido.PENDIENTE || control.finanzas().pendiente(p).compareTo(p.getTotal()) != 0)
                    throw new IllegalArgumentException("Solo se puede editar un pedido pendiente sin pagos.");
                new PedidoDialog(SwingUtilities.getWindowAncestor(this), control, p).setVisible(true);
            }));
            secundarios.add(pedidos.accion("Cancelar", p -> {
                if (Ui.confirmar(this, "¿Cancelar el pedido #" + p.getId() + "? Solo se permiten pedidos pendientes sin pagos.")) control.pedidos().cancelar(p);
            }));
            secundarios.add(pedidos.accion("Asignar repartidor", p -> {
                if (!(p instanceof PedidoDomicilio)) throw new IllegalArgumentException("Selecciona un pedido a domicilio.");
                Formulario f = new AsignarRepartidorForm();
                var repartidor = f.combo("Repartidor activo", control.atencion().repartidores().stream().filter(Repartidor::isActivo).toList(), null);
                f.guardar(this, "Asignar reparto", () -> control.pedidos().asignar(p, f.elegido(repartidor)));
            }));
        }
        pedidos.accion("Entregar", p -> {
            if (Ui.confirmar(this, "¿Marcar el pedido #" + p.getId() + " como entregado?")) control.pedidos().entregar(p);
        });
        pedidos.accion("Detalle", p -> Ui.mostrarTexto(this, "Detalle del pedido", detalle(p)));
        secundarios.add(pedidos.boton("Activos / historial", () -> {
            historial = !historial;
            pestanas.setTitleAt(0, historial ? "Historial de pedidos" : "Pedidos en curso");
        }));
        pedidos.accionesSecundarias(secundarios);
        registrar(pedidos);
        pestanas.setTitleAt(0, soloDomicilio ? "Entregas en curso" : "Pedidos en curso");
        
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
        return texto.append("\nTOTAL: ").append(Ui.dinero(p.getTotal())).toString();
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
