package com.garagecoffee.view;


public class MesaForm extends Formulario {

    public MesaForm() {
        initComponents();
        registrar("Número", txtNumero);
        registrar("Capacidad", txtCapacidad);
        registrar("En servicio", chkEnServicio);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        etiquetaNumero = new javax.swing.JLabel();
        txtNumero = new javax.swing.JTextField();
        etiquetaCapacidad = new javax.swing.JLabel();
        txtCapacidad = new javax.swing.JTextField();
        etiquetaEnServicio = new javax.swing.JLabel();
        chkEnServicio = new javax.swing.JCheckBox();
        this.setBackground(new java.awt.Color(255, 255, 255));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(20).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaNumero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(etiquetaCapacidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(etiquetaEnServicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(18).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(txtNumero, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE).addComponent(txtCapacidad, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE).addComponent(chkEnServicio, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)).addGap(20)));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(20).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaNumero, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(txtNumero, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(14).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaCapacidad, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(txtCapacidad, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(14).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaEnServicio, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(chkEnServicio, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(14).addGap(6, 6, Short.MAX_VALUE)));
        etiquetaNumero.setText("Número");
        etiquetaNumero.setFont(new java.awt.Font("SansSerif", 1, 13));
        etiquetaNumero.setForeground(new java.awt.Color(36, 59, 50));
        txtNumero.setFont(new java.awt.Font("SansSerif", 0, 14));
        txtNumero.setColumns(22);
        etiquetaCapacidad.setText("Capacidad");
        etiquetaCapacidad.setFont(new java.awt.Font("SansSerif", 1, 13));
        etiquetaCapacidad.setForeground(new java.awt.Color(36, 59, 50));
        txtCapacidad.setFont(new java.awt.Font("SansSerif", 0, 14));
        txtCapacidad.setColumns(22);
        etiquetaEnServicio.setText("En servicio");
        etiquetaEnServicio.setFont(new java.awt.Font("SansSerif", 1, 13));
        etiquetaEnServicio.setForeground(new java.awt.Color(36, 59, 50));
        chkEnServicio.setFont(new java.awt.Font("SansSerif", 0, 14));
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel etiquetaNumero;
    private javax.swing.JTextField txtNumero;
    private javax.swing.JLabel etiquetaCapacidad;
    private javax.swing.JTextField txtCapacidad;
    private javax.swing.JLabel etiquetaEnServicio;
    private javax.swing.JCheckBox chkEnServicio;
    // End of variables declaration//GEN-END:variables
}
