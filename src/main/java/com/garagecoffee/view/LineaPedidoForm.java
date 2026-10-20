package com.garagecoffee.view;


// Diseño editable en NetBeans: abrir este archivo y elegir Diseño.
public class LineaPedidoForm extends Formulario {

    public LineaPedidoForm() {
        initComponents();
        registrar("Producto", cmbProducto);
        registrar("Cantidad", txtCantidad);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        etiquetaProducto = new javax.swing.JLabel();
        cmbProducto = new javax.swing.JComboBox<>();
        etiquetaCantidad = new javax.swing.JLabel();
        txtCantidad = new javax.swing.JTextField();
        this.setBackground(new java.awt.Color(255, 255, 255));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createSequentialGroup().addGap(20).addComponent(etiquetaProducto).addGap(8).addComponent(cmbProducto, 0, 220, Short.MAX_VALUE).addGap(12).addComponent(etiquetaCantidad).addGap(8).addComponent(txtCantidad, 50, 50, 50).addGap(20));
        layout.setVerticalGroup(layout.createSequentialGroup().addGap(16).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER).addComponent(etiquetaProducto).addComponent(cmbProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(etiquetaCantidad).addComponent(txtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(16));
        etiquetaProducto.setText("Producto");
        etiquetaProducto.setFont(new java.awt.Font("SansSerif", 1, 13));
        etiquetaProducto.setForeground(new java.awt.Color(36, 59, 50));
        cmbProducto.setFont(new java.awt.Font("SansSerif", 0, 14));
        etiquetaCantidad.setText("Cantidad");
        etiquetaCantidad.setFont(new java.awt.Font("SansSerif", 1, 13));
        etiquetaCantidad.setForeground(new java.awt.Color(36, 59, 50));
        txtCantidad.setFont(new java.awt.Font("SansSerif", 0, 14));
        txtCantidad.setColumns(3);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel etiquetaProducto;
    private javax.swing.JComboBox<Object> cmbProducto;
    private javax.swing.JLabel etiquetaCantidad;
    private javax.swing.JTextField txtCantidad;
    // End of variables declaration//GEN-END:variables
}
