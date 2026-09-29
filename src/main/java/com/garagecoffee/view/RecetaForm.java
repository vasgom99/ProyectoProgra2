package com.garagecoffee.view;


// Diseño editable en NetBeans: abrir este archivo y elegir Diseño.
public class RecetaForm extends Formulario {

    public RecetaForm() {
        initComponents();
        registrar("Producto", cmbProducto);
        registrar("Ingrediente", cmbIngrediente);
        registrar("Cantidad por producto", txtCantidadPorProducto);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        etiquetaProducto = new javax.swing.JLabel();
        cmbProducto = new javax.swing.JComboBox<>();
        etiquetaIngrediente = new javax.swing.JLabel();
        cmbIngrediente = new javax.swing.JComboBox<>();
        etiquetaCantidadPorProducto = new javax.swing.JLabel();
        txtCantidadPorProducto = new javax.swing.JTextField();
        this.setBackground(new java.awt.Color(255, 255, 255));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(20).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(etiquetaIngrediente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(etiquetaCantidadPorProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(18).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(cmbProducto, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE).addComponent(cmbIngrediente, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE).addComponent(txtCantidadPorProducto, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)).addGap(20)));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(20).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(cmbProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(14).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaIngrediente, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(cmbIngrediente, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(14).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaCantidadPorProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(txtCantidadPorProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(14).addGap(6, 6, Short.MAX_VALUE)));
        etiquetaProducto.setText("Producto");
        etiquetaProducto.setFont(new java.awt.Font("SansSerif", 1, 13));
        etiquetaProducto.setForeground(new java.awt.Color(36, 59, 50));
        cmbProducto.setFont(new java.awt.Font("SansSerif", 0, 14));
        etiquetaIngrediente.setText("Ingrediente");
        etiquetaIngrediente.setFont(new java.awt.Font("SansSerif", 1, 13));
        etiquetaIngrediente.setForeground(new java.awt.Color(36, 59, 50));
        cmbIngrediente.setFont(new java.awt.Font("SansSerif", 0, 14));
        etiquetaCantidadPorProducto.setText("Cantidad por producto");
        etiquetaCantidadPorProducto.setFont(new java.awt.Font("SansSerif", 1, 13));
        etiquetaCantidadPorProducto.setForeground(new java.awt.Color(36, 59, 50));
        txtCantidadPorProducto.setFont(new java.awt.Font("SansSerif", 0, 14));
        txtCantidadPorProducto.setColumns(22);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel etiquetaProducto;
    private javax.swing.JComboBox<Object> cmbProducto;
    private javax.swing.JLabel etiquetaIngrediente;
    private javax.swing.JComboBox<Object> cmbIngrediente;
    private javax.swing.JLabel etiquetaCantidadPorProducto;
    private javax.swing.JTextField txtCantidadPorProducto;
    // End of variables declaration//GEN-END:variables
}
