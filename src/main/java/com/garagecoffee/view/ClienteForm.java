package com.garagecoffee.view;


// Diseño editable en NetBeans: abrir este archivo y elegir Diseño.
public class ClienteForm extends Formulario {

    public ClienteForm() {
        initComponents();
        registrar("Nombre", txtNombre);
        registrar("Teléfono", txtTelefono);
        registrar("Correo", txtCorreo);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        etiquetaNombre = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        etiquetaTelefono = new javax.swing.JLabel();
        txtTelefono = new javax.swing.JTextField();
        etiquetaCorreo = new javax.swing.JLabel();
        txtCorreo = new javax.swing.JTextField();
        this.setBackground(new java.awt.Color(255, 255, 255));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(20).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(etiquetaTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(etiquetaCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(18).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(txtNombre, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE).addComponent(txtTelefono, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE).addComponent(txtCorreo, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)).addGap(20)));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(20).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(14).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(14).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(txtCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(14).addGap(6, 6, Short.MAX_VALUE)));
        etiquetaNombre.setText("Nombre");
        etiquetaNombre.setFont(new java.awt.Font("SansSerif", 1, 13));
        etiquetaNombre.setForeground(new java.awt.Color(36, 59, 50));
        txtNombre.setFont(new java.awt.Font("SansSerif", 0, 14));
        txtNombre.setColumns(22);
        etiquetaTelefono.setText("Teléfono");
        etiquetaTelefono.setFont(new java.awt.Font("SansSerif", 1, 13));
        etiquetaTelefono.setForeground(new java.awt.Color(36, 59, 50));
        txtTelefono.setFont(new java.awt.Font("SansSerif", 0, 14));
        txtTelefono.setColumns(22);
        etiquetaCorreo.setText("Correo");
        etiquetaCorreo.setFont(new java.awt.Font("SansSerif", 1, 13));
        etiquetaCorreo.setForeground(new java.awt.Color(36, 59, 50));
        txtCorreo.setFont(new java.awt.Font("SansSerif", 0, 14));
        txtCorreo.setColumns(22);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel etiquetaNombre;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JLabel etiquetaTelefono;
    private javax.swing.JTextField txtTelefono;
    private javax.swing.JLabel etiquetaCorreo;
    private javax.swing.JTextField txtCorreo;
    // End of variables declaration//GEN-END:variables
}
