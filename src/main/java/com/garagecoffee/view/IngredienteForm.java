package com.garagecoffee.view;


// Diseño editable en NetBeans: abrir este archivo y elegir Diseño.
public class IngredienteForm extends Formulario {

    public IngredienteForm() {
        initComponents();
        registrar("Nombre", txtNombre);
        registrar("Unidad (g, ml, unidad…)", txtUnidadGMlUnidad);
        registrar("Stock mínimo", txtStockMinimo);
        registrar("Costo por unidad Q", txtCostoPorUnidadQ);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        etiquetaNombre = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        etiquetaUnidadGMlUnidad = new javax.swing.JLabel();
        txtUnidadGMlUnidad = new javax.swing.JTextField();
        etiquetaStockMinimo = new javax.swing.JLabel();
        txtStockMinimo = new javax.swing.JTextField();
        etiquetaCostoPorUnidadQ = new javax.swing.JLabel();
        txtCostoPorUnidadQ = new javax.swing.JTextField();
        this.setBackground(new java.awt.Color(255, 255, 255));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(20).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(etiquetaUnidadGMlUnidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(etiquetaStockMinimo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(etiquetaCostoPorUnidadQ, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(18).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(txtNombre, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE).addComponent(txtUnidadGMlUnidad, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE).addComponent(txtStockMinimo, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE).addComponent(txtCostoPorUnidadQ, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)).addGap(20)));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(20).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(14).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaUnidadGMlUnidad, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(txtUnidadGMlUnidad, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(14).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaStockMinimo, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(txtStockMinimo, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(14).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaCostoPorUnidadQ, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(txtCostoPorUnidadQ, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(14).addGap(6, 6, Short.MAX_VALUE)));
        etiquetaNombre.setText("Nombre");
        etiquetaNombre.setFont(new java.awt.Font("SansSerif", 1, 13));
        etiquetaNombre.setForeground(new java.awt.Color(36, 59, 50));
        txtNombre.setFont(new java.awt.Font("SansSerif", 0, 14));
        txtNombre.setColumns(22);
        etiquetaUnidadGMlUnidad.setText("Unidad (g, ml, unidad…)");
        etiquetaUnidadGMlUnidad.setFont(new java.awt.Font("SansSerif", 1, 13));
        etiquetaUnidadGMlUnidad.setForeground(new java.awt.Color(36, 59, 50));
        txtUnidadGMlUnidad.setFont(new java.awt.Font("SansSerif", 0, 14));
        txtUnidadGMlUnidad.setColumns(22);
        etiquetaStockMinimo.setText("Stock mínimo");
        etiquetaStockMinimo.setFont(new java.awt.Font("SansSerif", 1, 13));
        etiquetaStockMinimo.setForeground(new java.awt.Color(36, 59, 50));
        txtStockMinimo.setFont(new java.awt.Font("SansSerif", 0, 14));
        txtStockMinimo.setColumns(22);
        etiquetaCostoPorUnidadQ.setText("Costo por unidad Q");
        etiquetaCostoPorUnidadQ.setFont(new java.awt.Font("SansSerif", 1, 13));
        etiquetaCostoPorUnidadQ.setForeground(new java.awt.Color(36, 59, 50));
        txtCostoPorUnidadQ.setFont(new java.awt.Font("SansSerif", 0, 14));
        txtCostoPorUnidadQ.setColumns(22);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel etiquetaNombre;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JLabel etiquetaUnidadGMlUnidad;
    private javax.swing.JTextField txtUnidadGMlUnidad;
    private javax.swing.JLabel etiquetaStockMinimo;
    private javax.swing.JTextField txtStockMinimo;
    private javax.swing.JLabel etiquetaCostoPorUnidadQ;
    private javax.swing.JTextField txtCostoPorUnidadQ;
    // End of variables declaration//GEN-END:variables
}
