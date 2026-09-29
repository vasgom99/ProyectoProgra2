package com.garagecoffee.view;


// Diseño editable en NetBeans: abrir este archivo y elegir Diseño.
public class MenuForm extends Formulario {

    public MenuForm() {
        initComponents();
        registrar("Nombre", txtNombre);
        registrar("Activo", chkActivo);
        registrar("Desde AAAA-MM-DD (opcional)", txtDesdeAAAAMMDDOpcional);
        registrar("Hasta AAAA-MM-DD (opcional)", txtHastaAAAAMMDDOpcional);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        etiquetaNombre = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        etiquetaActivo = new javax.swing.JLabel();
        chkActivo = new javax.swing.JCheckBox();
        etiquetaDesdeAAAAMMDDOpcional = new javax.swing.JLabel();
        txtDesdeAAAAMMDDOpcional = new javax.swing.JTextField();
        etiquetaHastaAAAAMMDDOpcional = new javax.swing.JLabel();
        txtHastaAAAAMMDDOpcional = new javax.swing.JTextField();
        this.setBackground(new java.awt.Color(255, 255, 255));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(20).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(etiquetaActivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(etiquetaDesdeAAAAMMDDOpcional, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(etiquetaHastaAAAAMMDDOpcional, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(18).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(txtNombre, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE).addComponent(chkActivo, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE).addComponent(txtDesdeAAAAMMDDOpcional, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE).addComponent(txtHastaAAAAMMDDOpcional, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)).addGap(20)));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(20).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(14).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaActivo, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(chkActivo, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(14).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaDesdeAAAAMMDDOpcional, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(txtDesdeAAAAMMDDOpcional, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(14).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaHastaAAAAMMDDOpcional, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(txtHastaAAAAMMDDOpcional, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(14).addGap(6, 6, Short.MAX_VALUE)));
        etiquetaNombre.setText("Nombre");
        etiquetaNombre.setFont(new java.awt.Font("SansSerif", 1, 13));
        etiquetaNombre.setForeground(new java.awt.Color(36, 59, 50));
        txtNombre.setFont(new java.awt.Font("SansSerif", 0, 14));
        txtNombre.setColumns(22);
        etiquetaActivo.setText("Activo");
        etiquetaActivo.setFont(new java.awt.Font("SansSerif", 1, 13));
        etiquetaActivo.setForeground(new java.awt.Color(36, 59, 50));
        chkActivo.setFont(new java.awt.Font("SansSerif", 0, 14));
        etiquetaDesdeAAAAMMDDOpcional.setText("Desde AAAA-MM-DD (opcional)");
        etiquetaDesdeAAAAMMDDOpcional.setFont(new java.awt.Font("SansSerif", 1, 13));
        etiquetaDesdeAAAAMMDDOpcional.setForeground(new java.awt.Color(36, 59, 50));
        txtDesdeAAAAMMDDOpcional.setFont(new java.awt.Font("SansSerif", 0, 14));
        txtDesdeAAAAMMDDOpcional.setColumns(22);
        etiquetaHastaAAAAMMDDOpcional.setText("Hasta AAAA-MM-DD (opcional)");
        etiquetaHastaAAAAMMDDOpcional.setFont(new java.awt.Font("SansSerif", 1, 13));
        etiquetaHastaAAAAMMDDOpcional.setForeground(new java.awt.Color(36, 59, 50));
        txtHastaAAAAMMDDOpcional.setFont(new java.awt.Font("SansSerif", 0, 14));
        txtHastaAAAAMMDDOpcional.setColumns(22);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel etiquetaNombre;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JLabel etiquetaActivo;
    private javax.swing.JCheckBox chkActivo;
    private javax.swing.JLabel etiquetaDesdeAAAAMMDDOpcional;
    private javax.swing.JTextField txtDesdeAAAAMMDDOpcional;
    private javax.swing.JLabel etiquetaHastaAAAAMMDDOpcional;
    private javax.swing.JTextField txtHastaAAAAMMDDOpcional;
    // End of variables declaration//GEN-END:variables
}
