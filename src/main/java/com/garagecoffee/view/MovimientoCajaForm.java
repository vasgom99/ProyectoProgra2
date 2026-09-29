package com.garagecoffee.view;


// Diseño editable en NetBeans: abrir este archivo y elegir Diseño.
public class MovimientoCajaForm extends Formulario {

    public MovimientoCajaForm() {
        initComponents();
        registrar("Tipo", cmbTipo);
        registrar("Concepto", txtConcepto);
        registrar("Monto Q", txtMontoQ);
        registrar("Uso", lblUso);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        etiquetaTipo = new javax.swing.JLabel();
        cmbTipo = new javax.swing.JComboBox<>();
        etiquetaConcepto = new javax.swing.JLabel();
        txtConcepto = new javax.swing.JTextField();
        etiquetaMontoQ = new javax.swing.JLabel();
        txtMontoQ = new javax.swing.JTextField();
        etiquetaUso = new javax.swing.JLabel();
        lblUso = new javax.swing.JLabel();
        this.setBackground(new java.awt.Color(255, 255, 255));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(20).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(etiquetaConcepto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(etiquetaMontoQ, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(etiquetaUso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(18).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(cmbTipo, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE).addComponent(txtConcepto, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE).addComponent(txtMontoQ, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE).addComponent(lblUso, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)).addGap(20)));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(20).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(cmbTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(14).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaConcepto, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(txtConcepto, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(14).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaMontoQ, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(txtMontoQ, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(14).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaUso, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(lblUso, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(14).addGap(6, 6, Short.MAX_VALUE)));
        etiquetaTipo.setText("Tipo");
        etiquetaTipo.setFont(new java.awt.Font("SansSerif", 1, 13));
        etiquetaTipo.setForeground(new java.awt.Color(36, 59, 50));
        cmbTipo.setFont(new java.awt.Font("SansSerif", 0, 14));
        etiquetaConcepto.setText("Concepto");
        etiquetaConcepto.setFont(new java.awt.Font("SansSerif", 1, 13));
        etiquetaConcepto.setForeground(new java.awt.Color(36, 59, 50));
        txtConcepto.setFont(new java.awt.Font("SansSerif", 0, 14));
        txtConcepto.setColumns(22);
        etiquetaMontoQ.setText("Monto Q");
        etiquetaMontoQ.setFont(new java.awt.Font("SansSerif", 1, 13));
        etiquetaMontoQ.setForeground(new java.awt.Color(36, 59, 50));
        txtMontoQ.setFont(new java.awt.Font("SansSerif", 0, 14));
        txtMontoQ.setColumns(22);
        etiquetaUso.setText("Uso");
        etiquetaUso.setFont(new java.awt.Font("SansSerif", 1, 13));
        etiquetaUso.setForeground(new java.awt.Color(36, 59, 50));
        lblUso.setFont(new java.awt.Font("SansSerif", 0, 14));
        lblUso.setText("Información");
        lblUso.setForeground(new java.awt.Color(109, 121, 114));
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel etiquetaTipo;
    private javax.swing.JComboBox<Object> cmbTipo;
    private javax.swing.JLabel etiquetaConcepto;
    private javax.swing.JTextField txtConcepto;
    private javax.swing.JLabel etiquetaMontoQ;
    private javax.swing.JTextField txtMontoQ;
    private javax.swing.JLabel etiquetaUso;
    private javax.swing.JLabel lblUso;
    // End of variables declaration//GEN-END:variables
}
