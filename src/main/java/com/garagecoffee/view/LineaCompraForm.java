package com.garagecoffee.view;


// Diseño editable en NetBeans: abrir este archivo y elegir Diseño.
public class LineaCompraForm extends Formulario {

    public LineaCompraForm() {
        initComponents();
        registrar("Ingrediente", cmbIngrediente);
        registrar("Cantidad", txtCantidad);
        registrar("Costo por unidad Q", txtCostoPorUnidadQ);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        etiquetaIngrediente = new javax.swing.JLabel();
        cmbIngrediente = new javax.swing.JComboBox<>();
        etiquetaCantidad = new javax.swing.JLabel();
        txtCantidad = new javax.swing.JTextField();
        etiquetaCostoPorUnidadQ = new javax.swing.JLabel();
        txtCostoPorUnidadQ = new javax.swing.JTextField();
        this.setBackground(new java.awt.Color(255, 255, 255));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(20).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaIngrediente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(etiquetaCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(etiquetaCostoPorUnidadQ, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(18).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(cmbIngrediente, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE).addComponent(txtCantidad, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE).addComponent(txtCostoPorUnidadQ, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)).addGap(20)));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(20).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaIngrediente, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(cmbIngrediente, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(14).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(txtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(14).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaCostoPorUnidadQ, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(txtCostoPorUnidadQ, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(14).addGap(6, 6, Short.MAX_VALUE)));
        etiquetaIngrediente.setText("Ingrediente");
        etiquetaIngrediente.setFont(new java.awt.Font("SansSerif", 1, 13));
        etiquetaIngrediente.setForeground(new java.awt.Color(36, 59, 50));
        cmbIngrediente.setFont(new java.awt.Font("SansSerif", 0, 14));
        etiquetaCantidad.setText("Cantidad");
        etiquetaCantidad.setFont(new java.awt.Font("SansSerif", 1, 13));
        etiquetaCantidad.setForeground(new java.awt.Color(36, 59, 50));
        txtCantidad.setFont(new java.awt.Font("SansSerif", 0, 14));
        txtCantidad.setColumns(22);
        etiquetaCostoPorUnidadQ.setText("Costo por unidad Q");
        etiquetaCostoPorUnidadQ.setFont(new java.awt.Font("SansSerif", 1, 13));
        etiquetaCostoPorUnidadQ.setForeground(new java.awt.Color(36, 59, 50));
        txtCostoPorUnidadQ.setFont(new java.awt.Font("SansSerif", 0, 14));
        txtCostoPorUnidadQ.setColumns(22);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel etiquetaIngrediente;
    private javax.swing.JComboBox<Object> cmbIngrediente;
    private javax.swing.JLabel etiquetaCantidad;
    private javax.swing.JTextField txtCantidad;
    private javax.swing.JLabel etiquetaCostoPorUnidadQ;
    private javax.swing.JTextField txtCostoPorUnidadQ;
    // End of variables declaration//GEN-END:variables
}
