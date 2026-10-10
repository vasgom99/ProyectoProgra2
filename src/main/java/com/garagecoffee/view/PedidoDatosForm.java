package com.garagecoffee.view;

// Diseño editable en NetBeans: abrir este archivo y elegir Diseño.
public class PedidoDatosForm extends Formulario {
    public PedidoDatosForm() {
        initComponents();
        registrar("Mesa libre", cmbMesaLibre);
        registrar("Observaciones", txtObservaciones);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        etiquetaMesaLibre = new javax.swing.JLabel();
        cmbMesaLibre = new javax.swing.JComboBox<>();
        etiquetaObservaciones = new javax.swing.JLabel();
        txtObservaciones = new javax.swing.JTextField();
        this.setBackground(new java.awt.Color(255, 255, 255));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup().addGap(20)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(etiquetaMesaLibre).addComponent(etiquetaObservaciones))
                        .addGap(18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(cmbMesaLibre, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
                                .addComponent(txtObservaciones, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE))
                        .addGap(20)));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup().addGap(20)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(etiquetaMesaLibre, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(cmbMesaLibre, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(14)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(etiquetaObservaciones, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtObservaciones, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(14).addGap(6, 6, Short.MAX_VALUE)));
        etiquetaMesaLibre.setText("Mesa libre");
        etiquetaMesaLibre.setFont(new java.awt.Font("SansSerif", 1, 13));
        etiquetaMesaLibre.setForeground(new java.awt.Color(36, 59, 50));
        cmbMesaLibre.setFont(new java.awt.Font("SansSerif", 0, 14));
        etiquetaObservaciones.setText("Observaciones");
        etiquetaObservaciones.setFont(new java.awt.Font("SansSerif", 1, 13));
        etiquetaObservaciones.setForeground(new java.awt.Color(36, 59, 50));
        txtObservaciones.setFont(new java.awt.Font("SansSerif", 0, 14));
        txtObservaciones.setColumns(22);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel etiquetaMesaLibre;
    private javax.swing.JComboBox<Object> cmbMesaLibre;
    private javax.swing.JLabel etiquetaObservaciones;
    private javax.swing.JTextField txtObservaciones;
    // End of variables declaration//GEN-END:variables
}
