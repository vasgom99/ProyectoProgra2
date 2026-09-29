package com.garagecoffee.view;


// Diseño editable en NetBeans: abrir este archivo y elegir Diseño.
public class MovimientoStockForm extends Formulario {

    public MovimientoStockForm() {
        initComponents();
        registrar("Tipo", cmbTipo);
        registrar("Cantidad", txtCantidad);
        registrar("Motivo", txtMotivo);
        registrar("Importante", lblImportante);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        etiquetaTipo = new javax.swing.JLabel();
        cmbTipo = new javax.swing.JComboBox<>();
        etiquetaCantidad = new javax.swing.JLabel();
        txtCantidad = new javax.swing.JTextField();
        etiquetaMotivo = new javax.swing.JLabel();
        txtMotivo = new javax.swing.JTextField();
        etiquetaImportante = new javax.swing.JLabel();
        lblImportante = new javax.swing.JLabel();
        this.setBackground(new java.awt.Color(255, 255, 255));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(20).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(etiquetaCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(etiquetaMotivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(etiquetaImportante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(18).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(cmbTipo, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE).addComponent(txtCantidad, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE).addComponent(txtMotivo, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE).addComponent(lblImportante, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)).addGap(20)));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(20).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(cmbTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(14).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(txtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(14).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaMotivo, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(txtMotivo, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(14).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaImportante, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(lblImportante, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(14).addGap(6, 6, Short.MAX_VALUE)));
        etiquetaTipo.setText("Tipo");
        etiquetaTipo.setFont(new java.awt.Font("SansSerif", 1, 13));
        etiquetaTipo.setForeground(new java.awt.Color(36, 59, 50));
        cmbTipo.setFont(new java.awt.Font("SansSerif", 0, 14));
        etiquetaCantidad.setText("Cantidad");
        etiquetaCantidad.setFont(new java.awt.Font("SansSerif", 1, 13));
        etiquetaCantidad.setForeground(new java.awt.Color(36, 59, 50));
        txtCantidad.setFont(new java.awt.Font("SansSerif", 0, 14));
        txtCantidad.setColumns(22);
        etiquetaMotivo.setText("Motivo");
        etiquetaMotivo.setFont(new java.awt.Font("SansSerif", 1, 13));
        etiquetaMotivo.setForeground(new java.awt.Color(36, 59, 50));
        txtMotivo.setFont(new java.awt.Font("SansSerif", 0, 14));
        txtMotivo.setColumns(22);
        etiquetaImportante.setText("Importante");
        etiquetaImportante.setFont(new java.awt.Font("SansSerif", 1, 13));
        etiquetaImportante.setForeground(new java.awt.Color(36, 59, 50));
        lblImportante.setFont(new java.awt.Font("SansSerif", 0, 14));
        lblImportante.setText("Información");
        lblImportante.setForeground(new java.awt.Color(109, 121, 114));
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel etiquetaTipo;
    private javax.swing.JComboBox<Object> cmbTipo;
    private javax.swing.JLabel etiquetaCantidad;
    private javax.swing.JTextField txtCantidad;
    private javax.swing.JLabel etiquetaMotivo;
    private javax.swing.JTextField txtMotivo;
    private javax.swing.JLabel etiquetaImportante;
    private javax.swing.JLabel lblImportante;
    // End of variables declaration//GEN-END:variables
}
