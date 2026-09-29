package com.garagecoffee.view;


// Diseño editable en NetBeans: abrir este archivo y elegir Diseño.
public class AperturaCajaForm extends Formulario {

    public AperturaCajaForm() {
        initComponents();
        registrar("Efectivo inicial Q", txtEfectivoInicialQ);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        etiquetaEfectivoInicialQ = new javax.swing.JLabel();
        txtEfectivoInicialQ = new javax.swing.JTextField();
        this.setBackground(new java.awt.Color(255, 255, 255));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(20).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaEfectivoInicialQ, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(18).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(txtEfectivoInicialQ, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)).addGap(20)));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(20).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaEfectivoInicialQ, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(txtEfectivoInicialQ, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(14).addGap(6, 6, Short.MAX_VALUE)));
        etiquetaEfectivoInicialQ.setText("Efectivo inicial Q");
        etiquetaEfectivoInicialQ.setFont(new java.awt.Font("SansSerif", 1, 13));
        etiquetaEfectivoInicialQ.setForeground(new java.awt.Color(36, 59, 50));
        txtEfectivoInicialQ.setFont(new java.awt.Font("SansSerif", 0, 14));
        txtEfectivoInicialQ.setColumns(22);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel etiquetaEfectivoInicialQ;
    private javax.swing.JTextField txtEfectivoInicialQ;
    // End of variables declaration//GEN-END:variables
}
