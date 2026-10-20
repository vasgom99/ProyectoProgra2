package com.garagecoffee.view;

// Diseño editable en NetBeans: abrir este archivo y elegir Diseño.
public class CobroForm extends Formulario {
    public CobroForm() {
        initComponents();
        registrar("Total", lblTotal);
        registrar("Efectivo recibido Q", txtEfectivo);
        registrar("Cambio", lblCambio);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        etiquetaTotal = new javax.swing.JLabel();
        lblTotal = new javax.swing.JLabel();
        etiquetaEfectivo = new javax.swing.JLabel();
        txtEfectivo = new javax.swing.JTextField();
        etiquetaCambio = new javax.swing.JLabel();
        lblCambio = new javax.swing.JLabel();
        this.setBackground(new java.awt.Color(255, 255, 255));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup().addGap(20)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(etiquetaTotal).addComponent(etiquetaEfectivo).addComponent(etiquetaCambio))
                        .addGap(18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(lblTotal, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
                                .addComponent(txtEfectivo, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
                                .addComponent(lblCambio, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE))
                        .addGap(20)));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup().addGap(20)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(etiquetaTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(14)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(etiquetaEfectivo, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtEfectivo, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(14)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(etiquetaCambio, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblCambio, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(14).addGap(6, 6, Short.MAX_VALUE)));
        etiquetaTotal.setText("Total");
        etiquetaEfectivo.setText("Efectivo recibido Q");
        etiquetaCambio.setText("Cambio");
        etiquetaTotal.setFont(new java.awt.Font("SansSerif", 1, 13));
        etiquetaTotal.setForeground(new java.awt.Color(36, 59, 50));
        etiquetaEfectivo.setFont(new java.awt.Font("SansSerif", 1, 13));
        etiquetaEfectivo.setForeground(new java.awt.Color(36, 59, 50));
        etiquetaCambio.setFont(new java.awt.Font("SansSerif", 1, 13));
        etiquetaCambio.setForeground(new java.awt.Color(36, 59, 50));
        lblTotal.setText("Q 0.00");
        lblCambio.setText("Q 0.00");
        txtEfectivo.setFont(new java.awt.Font("SansSerif", 0, 14));
        txtEfectivo.setColumns(22);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel etiquetaTotal;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JLabel etiquetaEfectivo;
    private javax.swing.JTextField txtEfectivo;
    private javax.swing.JLabel etiquetaCambio;
    private javax.swing.JLabel lblCambio;
    // End of variables declaration//GEN-END:variables
}
