package com.garagecoffee.view;


// Diseño editable en NetBeans: abrir este archivo y elegir Diseño.
public class GastoForm extends Formulario {

    public GastoForm() {
        initComponents();
        registrar("Categoría", txtCategoria);
        registrar("Descripción", txtDescripcion);
        registrar("Monto Q", txtMontoQ);
        registrar("Pago", lblPago);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        etiquetaCategoria = new javax.swing.JLabel();
        txtCategoria = new javax.swing.JTextField();
        etiquetaDescripcion = new javax.swing.JLabel();
        txtDescripcion = new javax.swing.JTextField();
        etiquetaMontoQ = new javax.swing.JLabel();
        txtMontoQ = new javax.swing.JTextField();
        etiquetaPago = new javax.swing.JLabel();
        lblPago = new javax.swing.JLabel();
        this.setBackground(new java.awt.Color(255, 255, 255));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(20).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(etiquetaDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(etiquetaMontoQ, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(etiquetaPago, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(18).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(txtCategoria, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE).addComponent(txtDescripcion, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE).addComponent(txtMontoQ, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE).addComponent(lblPago, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)).addGap(20)));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(20).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(txtCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(14).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(txtDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(14).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaMontoQ, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(txtMontoQ, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(14).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaPago, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(lblPago, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(14).addGap(6, 6, Short.MAX_VALUE)));
        etiquetaCategoria.setText("Categoría");
        etiquetaCategoria.setFont(new java.awt.Font("SansSerif", 1, 13));
        etiquetaCategoria.setForeground(new java.awt.Color(36, 59, 50));
        txtCategoria.setFont(new java.awt.Font("SansSerif", 0, 14));
        txtCategoria.setColumns(22);
        etiquetaDescripcion.setText("Descripción");
        etiquetaDescripcion.setFont(new java.awt.Font("SansSerif", 1, 13));
        etiquetaDescripcion.setForeground(new java.awt.Color(36, 59, 50));
        txtDescripcion.setFont(new java.awt.Font("SansSerif", 0, 14));
        txtDescripcion.setColumns(22);
        etiquetaMontoQ.setText("Monto Q");
        etiquetaMontoQ.setFont(new java.awt.Font("SansSerif", 1, 13));
        etiquetaMontoQ.setForeground(new java.awt.Color(36, 59, 50));
        txtMontoQ.setFont(new java.awt.Font("SansSerif", 0, 14));
        txtMontoQ.setColumns(22);
        etiquetaPago.setText("Pago");
        etiquetaPago.setFont(new java.awt.Font("SansSerif", 1, 13));
        etiquetaPago.setForeground(new java.awt.Color(36, 59, 50));
        lblPago.setFont(new java.awt.Font("SansSerif", 0, 14));
        lblPago.setText("Información");
        lblPago.setForeground(new java.awt.Color(109, 121, 114));
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel etiquetaCategoria;
    private javax.swing.JTextField txtCategoria;
    private javax.swing.JLabel etiquetaDescripcion;
    private javax.swing.JTextField txtDescripcion;
    private javax.swing.JLabel etiquetaMontoQ;
    private javax.swing.JTextField txtMontoQ;
    private javax.swing.JLabel etiquetaPago;
    private javax.swing.JLabel lblPago;
    // End of variables declaration//GEN-END:variables
}
