package com.garagecoffee.view;

import com.garagecoffee.model.*;
public class InicioPanel extends PanelModulo {

    private com.garagecoffee.controller.RestauranteController control;
    public InicioPanel() { initComponents(); }
    public InicioPanel(com.garagecoffee.controller.RestauranteController control) { this(); this.control = control; }
    @Override public void recargar() {
        if (control == null) return;
        pendientes.setText(String.valueOf(control.pedidos().pedidos().stream().filter(p -> p.getEstado() == EstadoPedido.PENDIENTE).count()));
        listos.setText(String.valueOf(control.pedidos().pedidos().stream().filter(p -> p.getEstado() == EstadoPedido.LISTO).count()));
        productos.setText(String.valueOf(control.catalogo().productos().stream().filter(p -> p.isDisponible() && p.getcategoria().isActivo()).count()));
        alertas.setText(String.valueOf(control.atencion().mesas().stream().filter(m -> m.getEstado() == EstadoMesa.LIBRE).count()));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        arriba = new javax.swing.JPanel();
        bienvenida = new javax.swing.JPanel();
        lblHola = new javax.swing.JLabel();
        lblAyuda = new javax.swing.JLabel();
        lblFecha = new javax.swing.JLabel();
        tarjetas = new javax.swing.JPanel();
        cardPendientes = new javax.swing.JPanel();
        cardPendientesTitle = new javax.swing.JLabel();
        pendientes = new javax.swing.JLabel();
        cardPendientesHelp = new javax.swing.JLabel();
        cardListos = new javax.swing.JPanel();
        cardListosTitle = new javax.swing.JLabel();
        listos = new javax.swing.JLabel();
        cardListosHelp = new javax.swing.JLabel();
        cardProductos = new javax.swing.JPanel();
        cardProductosTitle = new javax.swing.JLabel();
        productos = new javax.swing.JLabel();
        cardProductosHelp = new javax.swing.JLabel();
        cardAlertas = new javax.swing.JPanel();
        cardAlertasTitle = new javax.swing.JLabel();
        alertas = new javax.swing.JLabel();
        cardAlertasHelp = new javax.swing.JLabel();
        this.setBackground(new java.awt.Color(246, 244, 238));
        this.setLayout(new java.awt.BorderLayout(22, 22));
        arriba.setOpaque(false);
        arriba.setLayout(new java.awt.BorderLayout(20, 20));
        bienvenida.setBackground(new java.awt.Color(231, 238, 232));
        bienvenida.setBorder(javax.swing.BorderFactory.createEmptyBorder(22, 26, 22, 26));
        bienvenida.setLayout(new java.awt.BorderLayout(10, 10));
        lblHola.setText("Todo listo para un buen servicio.");
        lblHola.setFont(new java.awt.Font("SansSerif", 1, 25));
        lblHola.setForeground(new java.awt.Color(36, 59, 50));
        bienvenida.add(lblHola, java.awt.BorderLayout.NORTH);
        lblAyuda.setText("Pedidos claros, cocina organizada y cuentas al día.");
        lblAyuda.setFont(new java.awt.Font("SansSerif", 0, 14));
        lblAyuda.setForeground(new java.awt.Color(109, 121, 114));
        bienvenida.add(lblAyuda, java.awt.BorderLayout.CENTER);
        lblFecha.setText("GARAGE COFFEE · TU RESUMEN");
        lblFecha.setFont(new java.awt.Font("SansSerif", 1, 10));
        lblFecha.setForeground(new java.awt.Color(174, 96, 64));
        bienvenida.add(lblFecha, java.awt.BorderLayout.SOUTH);
        arriba.add(bienvenida, java.awt.BorderLayout.NORTH);
        tarjetas.setOpaque(false);
        tarjetas.setPreferredSize(new java.awt.Dimension(850, 152));
        tarjetas.setLayout(new java.awt.GridLayout(1, 4, 16, 16));
        cardPendientes.setBackground(new java.awt.Color(255, 255, 255));
        cardPendientes.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 22, 20, 22));
        cardPendientes.setLayout(new java.awt.BorderLayout(10, 10));
        cardPendientesTitle.setText("POR PREPARAR");
        cardPendientesTitle.setFont(new java.awt.Font("SansSerif", 1, 11));
        cardPendientesTitle.setForeground(new java.awt.Color(109, 121, 114));
        cardPendientes.add(cardPendientesTitle, java.awt.BorderLayout.NORTH);
        pendientes.setText("0");
        pendientes.setFont(new java.awt.Font("SansSerif", 1, 36));
        pendientes.setForeground(new java.awt.Color(174, 96, 64));
        cardPendientes.add(pendientes, java.awt.BorderLayout.CENTER);
        cardPendientesHelp.setText("Pedidos en espera");
        cardPendientesHelp.setFont(new java.awt.Font("SansSerif", 0, 11));
        cardPendientesHelp.setForeground(new java.awt.Color(109, 121, 114));
        cardPendientes.add(cardPendientesHelp, java.awt.BorderLayout.SOUTH);
        tarjetas.add(cardPendientes);
        cardListos.setBackground(new java.awt.Color(255, 255, 255));
        cardListos.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 22, 20, 22));
        cardListos.setLayout(new java.awt.BorderLayout(10, 10));
        cardListosTitle.setText("LISTOS PARA ENTREGAR");
        cardListosTitle.setFont(new java.awt.Font("SansSerif", 1, 11));
        cardListosTitle.setForeground(new java.awt.Color(109, 121, 114));
        cardListos.add(cardListosTitle, java.awt.BorderLayout.NORTH);
        listos.setText("0");
        listos.setFont(new java.awt.Font("SansSerif", 1, 36));
        listos.setForeground(new java.awt.Color(36, 59, 50));
        cardListos.add(listos, java.awt.BorderLayout.CENTER);
        cardListosHelp.setText("Continúa el servicio");
        cardListosHelp.setFont(new java.awt.Font("SansSerif", 0, 11));
        cardListosHelp.setForeground(new java.awt.Color(109, 121, 114));
        cardListos.add(cardListosHelp, java.awt.BorderLayout.SOUTH);
        tarjetas.add(cardListos);
        cardProductos.setBackground(new java.awt.Color(255, 255, 255));
        cardProductos.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 22, 20, 22));
        cardProductos.setLayout(new java.awt.BorderLayout(10, 10));
        cardProductosTitle.setText("EN EL CATÁLOGO");
        cardProductosTitle.setFont(new java.awt.Font("SansSerif", 1, 11));
        cardProductosTitle.setForeground(new java.awt.Color(109, 121, 114));
        cardProductos.add(cardProductosTitle, java.awt.BorderLayout.NORTH);
        productos.setText("0");
        productos.setFont(new java.awt.Font("SansSerif", 1, 36));
        productos.setForeground(new java.awt.Color(36, 59, 50));
        cardProductos.add(productos, java.awt.BorderLayout.CENTER);
        cardProductosHelp.setText("Productos disponibles");
        cardProductosHelp.setFont(new java.awt.Font("SansSerif", 0, 11));
        cardProductosHelp.setForeground(new java.awt.Color(109, 121, 114));
        cardProductos.add(cardProductosHelp, java.awt.BorderLayout.SOUTH);
        tarjetas.add(cardProductos);
        cardAlertas.setBackground(new java.awt.Color(255, 255, 255));
        cardAlertas.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 22, 20, 22));
        cardAlertas.setLayout(new java.awt.BorderLayout(10, 10));
        cardAlertasTitle.setText("MESAS LIBRES");
        cardAlertasTitle.setFont(new java.awt.Font("SansSerif", 1, 11));
        cardAlertasTitle.setForeground(new java.awt.Color(109, 121, 114));
        cardAlertas.add(cardAlertasTitle, java.awt.BorderLayout.NORTH);
        alertas.setText("0");
        alertas.setFont(new java.awt.Font("SansSerif", 1, 36));
        alertas.setForeground(new java.awt.Color(174, 96, 64));
        cardAlertas.add(alertas, java.awt.BorderLayout.CENTER);
        cardAlertasHelp.setText("Disponibles para pedidos");
        cardAlertasHelp.setFont(new java.awt.Font("SansSerif", 0, 11));
        cardAlertasHelp.setForeground(new java.awt.Color(109, 121, 114));
        cardAlertas.add(cardAlertasHelp, java.awt.BorderLayout.SOUTH);
        tarjetas.add(cardAlertas);
        arriba.add(tarjetas, java.awt.BorderLayout.CENTER);
        this.add(arriba, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel arriba;
    private javax.swing.JPanel bienvenida;
    private javax.swing.JLabel lblHola;
    private javax.swing.JLabel lblAyuda;
    private javax.swing.JLabel lblFecha;
    private javax.swing.JPanel tarjetas;
    private javax.swing.JPanel cardPendientes;
    private javax.swing.JLabel cardPendientesTitle;
    private javax.swing.JLabel pendientes;
    private javax.swing.JLabel cardPendientesHelp;
    private javax.swing.JPanel cardListos;
    private javax.swing.JLabel cardListosTitle;
    private javax.swing.JLabel listos;
    private javax.swing.JLabel cardListosHelp;
    private javax.swing.JPanel cardProductos;
    private javax.swing.JLabel cardProductosTitle;
    private javax.swing.JLabel productos;
    private javax.swing.JLabel cardProductosHelp;
    private javax.swing.JPanel cardAlertas;
    private javax.swing.JLabel cardAlertasTitle;
    private javax.swing.JLabel alertas;
    private javax.swing.JLabel cardAlertasHelp;
    // End of variables declaration//GEN-END:variables
}
