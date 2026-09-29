package com.garagecoffee.view;


// Diseño editable en NetBeans: abrir este archivo y elegir Diseño.
public class DireccionForm extends Formulario {

    public DireccionForm() {
        initComponents();
        registrar("Cliente", cmbCliente);
        registrar("Dirección", txtDireccion);
        registrar("Referencia", txtReferencia);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        etiquetaCliente = new javax.swing.JLabel();
        cmbCliente = new javax.swing.JComboBox<>();
        etiquetaDireccion = new javax.swing.JLabel();
        txtDireccion = new javax.swing.JTextField();
        etiquetaReferencia = new javax.swing.JLabel();
        txtReferencia = new javax.swing.JTextField();
        this.setBackground(new java.awt.Color(255, 255, 255));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(20).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(etiquetaDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(etiquetaReferencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(18).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(cmbCliente, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE).addComponent(txtDireccion, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE).addComponent(txtReferencia, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)).addGap(20)));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(20).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(cmbCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(14).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(txtDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(14).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaReferencia, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(txtReferencia, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(14).addGap(6, 6, Short.MAX_VALUE)));
        etiquetaCliente.setText("Cliente");
        etiquetaCliente.setFont(new java.awt.Font("SansSerif", 1, 13));
        etiquetaCliente.setForeground(new java.awt.Color(36, 59, 50));
        cmbCliente.setFont(new java.awt.Font("SansSerif", 0, 14));
        etiquetaDireccion.setText("Dirección");
        etiquetaDireccion.setFont(new java.awt.Font("SansSerif", 1, 13));
        etiquetaDireccion.setForeground(new java.awt.Color(36, 59, 50));
        txtDireccion.setFont(new java.awt.Font("SansSerif", 0, 14));
        txtDireccion.setColumns(22);
        etiquetaReferencia.setText("Referencia");
        etiquetaReferencia.setFont(new java.awt.Font("SansSerif", 1, 13));
        etiquetaReferencia.setForeground(new java.awt.Color(36, 59, 50));
        txtReferencia.setFont(new java.awt.Font("SansSerif", 0, 14));
        txtReferencia.setColumns(22);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel etiquetaCliente;
    private javax.swing.JComboBox<Object> cmbCliente;
    private javax.swing.JLabel etiquetaDireccion;
    private javax.swing.JTextField txtDireccion;
    private javax.swing.JLabel etiquetaReferencia;
    private javax.swing.JTextField txtReferencia;
    // End of variables declaration//GEN-END:variables
}
