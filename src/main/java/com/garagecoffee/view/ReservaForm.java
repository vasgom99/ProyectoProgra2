package com.garagecoffee.view;


// Diseño editable en NetBeans: abrir este archivo y elegir Diseño.
public class ReservaForm extends Formulario {

    public ReservaForm() {
        initComponents();
        registrar("Cliente", cmbCliente);
        registrar("Mesa", cmbMesa);
        registrar("Fecha / hora AAAA-MM-DD HH:MM", txtFechaHoraAAAAMMDD);
        registrar("Personas", txtPersonas);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        etiquetaCliente = new javax.swing.JLabel();
        cmbCliente = new javax.swing.JComboBox<>();
        etiquetaMesa = new javax.swing.JLabel();
        cmbMesa = new javax.swing.JComboBox<>();
        etiquetaFechaHoraAAAAMMDD = new javax.swing.JLabel();
        txtFechaHoraAAAAMMDD = new javax.swing.JTextField();
        etiquetaPersonas = new javax.swing.JLabel();
        txtPersonas = new javax.swing.JTextField();
        this.setBackground(new java.awt.Color(255, 255, 255));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(20).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(etiquetaMesa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(etiquetaFechaHoraAAAAMMDD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(etiquetaPersonas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(18).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(cmbCliente, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE).addComponent(cmbMesa, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE).addComponent(txtFechaHoraAAAAMMDD, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE).addComponent(txtPersonas, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)).addGap(20)));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(20).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(cmbCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(14).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaMesa, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(cmbMesa, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(14).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaFechaHoraAAAAMMDD, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(txtFechaHoraAAAAMMDD, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(14).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaPersonas, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(txtPersonas, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(14).addGap(6, 6, Short.MAX_VALUE)));
        etiquetaCliente.setText("Cliente");
        etiquetaCliente.setFont(new java.awt.Font("SansSerif", 1, 13));
        etiquetaCliente.setForeground(new java.awt.Color(36, 59, 50));
        cmbCliente.setFont(new java.awt.Font("SansSerif", 0, 14));
        etiquetaMesa.setText("Mesa");
        etiquetaMesa.setFont(new java.awt.Font("SansSerif", 1, 13));
        etiquetaMesa.setForeground(new java.awt.Color(36, 59, 50));
        cmbMesa.setFont(new java.awt.Font("SansSerif", 0, 14));
        etiquetaFechaHoraAAAAMMDD.setText("Fecha / hora AAAA-MM-DD HH:MM");
        etiquetaFechaHoraAAAAMMDD.setFont(new java.awt.Font("SansSerif", 1, 13));
        etiquetaFechaHoraAAAAMMDD.setForeground(new java.awt.Color(36, 59, 50));
        txtFechaHoraAAAAMMDD.setFont(new java.awt.Font("SansSerif", 0, 14));
        txtFechaHoraAAAAMMDD.setColumns(22);
        etiquetaPersonas.setText("Personas");
        etiquetaPersonas.setFont(new java.awt.Font("SansSerif", 1, 13));
        etiquetaPersonas.setForeground(new java.awt.Color(36, 59, 50));
        txtPersonas.setFont(new java.awt.Font("SansSerif", 0, 14));
        txtPersonas.setColumns(22);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel etiquetaCliente;
    private javax.swing.JComboBox<Object> cmbCliente;
    private javax.swing.JLabel etiquetaMesa;
    private javax.swing.JComboBox<Object> cmbMesa;
    private javax.swing.JLabel etiquetaFechaHoraAAAAMMDD;
    private javax.swing.JTextField txtFechaHoraAAAAMMDD;
    private javax.swing.JLabel etiquetaPersonas;
    private javax.swing.JTextField txtPersonas;
    // End of variables declaration//GEN-END:variables
}
