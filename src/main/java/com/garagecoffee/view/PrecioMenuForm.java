package com.garagecoffee.view;


// Diseño editable en NetBeans: abrir este archivo y elegir Diseño.
public class PrecioMenuForm extends Formulario {

    public PrecioMenuForm() {
        initComponents();
        registrar("Menú", cmbMenu);
        registrar("Producto", cmbProducto);
        registrar("Precio especial Q", txtPrecioEspecialQ);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        etiquetaMenu = new javax.swing.JLabel();
        cmbMenu = new javax.swing.JComboBox<>();
        etiquetaProducto = new javax.swing.JLabel();
        cmbProducto = new javax.swing.JComboBox<>();
        etiquetaPrecioEspecialQ = new javax.swing.JLabel();
        txtPrecioEspecialQ = new javax.swing.JTextField();
        this.setBackground(new java.awt.Color(255, 255, 255));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(20).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaMenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(etiquetaProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(etiquetaPrecioEspecialQ, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(18).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(cmbMenu, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE).addComponent(cmbProducto, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE).addComponent(txtPrecioEspecialQ, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)).addGap(20)));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(20).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(cmbMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(14).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(cmbProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(14).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaPrecioEspecialQ, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(txtPrecioEspecialQ, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(14).addGap(6, 6, Short.MAX_VALUE)));
        etiquetaMenu.setText("Menú");
        etiquetaMenu.setFont(new java.awt.Font("SansSerif", 1, 13));
        etiquetaMenu.setForeground(new java.awt.Color(36, 59, 50));
        cmbMenu.setFont(new java.awt.Font("SansSerif", 0, 14));
        etiquetaProducto.setText("Producto");
        etiquetaProducto.setFont(new java.awt.Font("SansSerif", 1, 13));
        etiquetaProducto.setForeground(new java.awt.Color(36, 59, 50));
        cmbProducto.setFont(new java.awt.Font("SansSerif", 0, 14));
        etiquetaPrecioEspecialQ.setText("Precio especial Q");
        etiquetaPrecioEspecialQ.setFont(new java.awt.Font("SansSerif", 1, 13));
        etiquetaPrecioEspecialQ.setForeground(new java.awt.Color(36, 59, 50));
        txtPrecioEspecialQ.setFont(new java.awt.Font("SansSerif", 0, 14));
        txtPrecioEspecialQ.setColumns(22);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel etiquetaMenu;
    private javax.swing.JComboBox<Object> cmbMenu;
    private javax.swing.JLabel etiquetaProducto;
    private javax.swing.JComboBox<Object> cmbProducto;
    private javax.swing.JLabel etiquetaPrecioEspecialQ;
    private javax.swing.JTextField txtPrecioEspecialQ;
    // End of variables declaration//GEN-END:variables
}
