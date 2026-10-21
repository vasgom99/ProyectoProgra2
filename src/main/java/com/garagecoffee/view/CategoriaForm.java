package com.garagecoffee.view;


public class CategoriaForm extends Formulario {

    public CategoriaForm() {
        initComponents();
        registrar("Nombre", txtNombre);
        registrar("Tipo", cmbTipo);
        registrar("Activa", chkActiva);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        etiquetaNombre = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        etiquetaTipo = new javax.swing.JLabel();
        cmbTipo = new javax.swing.JComboBox<>();
        etiquetaActiva = new javax.swing.JLabel();
        chkActiva = new javax.swing.JCheckBox();
        this.setBackground(new java.awt.Color(255, 255, 255));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(20).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(etiquetaTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(etiquetaActiva, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(18).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(txtNombre, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE).addComponent(cmbTipo, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE).addComponent(chkActiva, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)).addGap(20)));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(20).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(14).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(cmbTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(14).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaActiva, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(chkActiva, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(14).addGap(6, 6, Short.MAX_VALUE)));
        etiquetaNombre.setText("Nombre");
        etiquetaNombre.setFont(new java.awt.Font("SansSerif", 1, 13));
        etiquetaNombre.setForeground(new java.awt.Color(36, 59, 50));
        txtNombre.setFont(new java.awt.Font("SansSerif", 0, 14));
        txtNombre.setColumns(22);
        etiquetaTipo.setText("Tipo");
        etiquetaTipo.setFont(new java.awt.Font("SansSerif", 1, 13));
        etiquetaTipo.setForeground(new java.awt.Color(36, 59, 50));
        cmbTipo.setFont(new java.awt.Font("SansSerif", 0, 14));
        etiquetaActiva.setText("Activa");
        etiquetaActiva.setFont(new java.awt.Font("SansSerif", 1, 13));
        etiquetaActiva.setForeground(new java.awt.Color(36, 59, 50));
        chkActiva.setFont(new java.awt.Font("SansSerif", 0, 14));
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel etiquetaNombre;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JLabel etiquetaTipo;
    private javax.swing.JComboBox<Object> cmbTipo;
    private javax.swing.JLabel etiquetaActiva;
    private javax.swing.JCheckBox chkActiva;
    // End of variables declaration//GEN-END:variables
}
