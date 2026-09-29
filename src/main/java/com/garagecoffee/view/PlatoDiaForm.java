package com.garagecoffee.view;


// Diseño editable en NetBeans: abrir este archivo y elegir Diseño.
public class PlatoDiaForm extends Formulario {

    public PlatoDiaForm() {
        initComponents();
        registrar("Producto", cmbProducto);
        registrar("Fecha AAAA-MM-DD", txtFechaAAAAMMDD);
        registrar("Precio especial Q", txtPrecioEspecialQ);
        registrar("Nota", txtNota);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        etiquetaProducto = new javax.swing.JLabel();
        cmbProducto = new javax.swing.JComboBox<>();
        etiquetaFechaAAAAMMDD = new javax.swing.JLabel();
        txtFechaAAAAMMDD = new javax.swing.JTextField();
        etiquetaPrecioEspecialQ = new javax.swing.JLabel();
        txtPrecioEspecialQ = new javax.swing.JTextField();
        etiquetaNota = new javax.swing.JLabel();
        txtNota = new javax.swing.JTextField();
        this.setBackground(new java.awt.Color(255, 255, 255));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(20).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(etiquetaFechaAAAAMMDD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(etiquetaPrecioEspecialQ, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(etiquetaNota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(18).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(cmbProducto, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE).addComponent(txtFechaAAAAMMDD, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE).addComponent(txtPrecioEspecialQ, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE).addComponent(txtNota, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)).addGap(20)));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(20).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(cmbProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(14).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaFechaAAAAMMDD, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(txtFechaAAAAMMDD, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(14).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaPrecioEspecialQ, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(txtPrecioEspecialQ, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(14).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaNota, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(txtNota, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(14).addGap(6, 6, Short.MAX_VALUE)));
        etiquetaProducto.setText("Producto");
        etiquetaProducto.setFont(new java.awt.Font("SansSerif", 1, 13));
        etiquetaProducto.setForeground(new java.awt.Color(36, 59, 50));
        cmbProducto.setFont(new java.awt.Font("SansSerif", 0, 14));
        etiquetaFechaAAAAMMDD.setText("Fecha AAAA-MM-DD");
        etiquetaFechaAAAAMMDD.setFont(new java.awt.Font("SansSerif", 1, 13));
        etiquetaFechaAAAAMMDD.setForeground(new java.awt.Color(36, 59, 50));
        txtFechaAAAAMMDD.setFont(new java.awt.Font("SansSerif", 0, 14));
        txtFechaAAAAMMDD.setColumns(22);
        etiquetaPrecioEspecialQ.setText("Precio especial Q");
        etiquetaPrecioEspecialQ.setFont(new java.awt.Font("SansSerif", 1, 13));
        etiquetaPrecioEspecialQ.setForeground(new java.awt.Color(36, 59, 50));
        txtPrecioEspecialQ.setFont(new java.awt.Font("SansSerif", 0, 14));
        txtPrecioEspecialQ.setColumns(22);
        etiquetaNota.setText("Nota");
        etiquetaNota.setFont(new java.awt.Font("SansSerif", 1, 13));
        etiquetaNota.setForeground(new java.awt.Color(36, 59, 50));
        txtNota.setFont(new java.awt.Font("SansSerif", 0, 14));
        txtNota.setColumns(22);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel etiquetaProducto;
    private javax.swing.JComboBox<Object> cmbProducto;
    private javax.swing.JLabel etiquetaFechaAAAAMMDD;
    private javax.swing.JTextField txtFechaAAAAMMDD;
    private javax.swing.JLabel etiquetaPrecioEspecialQ;
    private javax.swing.JTextField txtPrecioEspecialQ;
    private javax.swing.JLabel etiquetaNota;
    private javax.swing.JTextField txtNota;
    // End of variables declaration//GEN-END:variables
}
