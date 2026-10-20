package com.garagecoffee.view;


// Diseño editable en NetBeans: abrir este archivo y elegir Diseño.
public class CierreCajaForm extends Formulario {

    public CierreCajaForm() {
        initComponents();
        registrar("Saldo esperado", lblSaldoEsperado);
        registrar("Efectivo contado Q", txtEfectivoContadoQ);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        etiquetaSaldoEsperado = new javax.swing.JLabel();
        lblSaldoEsperado = new javax.swing.JLabel();
        etiquetaEfectivoContadoQ = new javax.swing.JLabel();
        txtEfectivoContadoQ = new javax.swing.JTextField();
        this.setBackground(new java.awt.Color(255, 255, 255));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(20).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaSaldoEsperado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(etiquetaEfectivoContadoQ, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(18).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(lblSaldoEsperado, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE).addComponent(txtEfectivoContadoQ, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)).addGap(20)));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(20).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaSaldoEsperado, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(lblSaldoEsperado, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(14).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaEfectivoContadoQ, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(txtEfectivoContadoQ, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(14).addGap(6, 6, Short.MAX_VALUE)));
        etiquetaSaldoEsperado.setText("Saldo esperado");
        etiquetaSaldoEsperado.setFont(new java.awt.Font("SansSerif", 1, 13));
        etiquetaSaldoEsperado.setForeground(new java.awt.Color(36, 59, 50));
        lblSaldoEsperado.setFont(new java.awt.Font("SansSerif", 0, 14));
        lblSaldoEsperado.setText("Información");
        lblSaldoEsperado.setForeground(new java.awt.Color(109, 121, 114));
        etiquetaEfectivoContadoQ.setText("Efectivo contado Q");
        etiquetaEfectivoContadoQ.setFont(new java.awt.Font("SansSerif", 1, 13));
        etiquetaEfectivoContadoQ.setForeground(new java.awt.Color(36, 59, 50));
        txtEfectivoContadoQ.setFont(new java.awt.Font("SansSerif", 0, 14));
        txtEfectivoContadoQ.setColumns(22);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel etiquetaSaldoEsperado;
    private javax.swing.JLabel lblSaldoEsperado;
    private javax.swing.JLabel etiquetaEfectivoContadoQ;
    private javax.swing.JTextField txtEfectivoContadoQ;
    // End of variables declaration//GEN-END:variables
}
