package com.garagecoffee.view;


// Diseño editable en NetBeans: abrir este archivo y elegir Diseño.
public class CobroForm extends Formulario {

    public CobroForm() {
        initComponents();
        registrar("Pendiente", lblPendiente);
        registrar("Método", cmbMetodo);
        registrar("Monto recibido Q (permite abonos)", txtMontoRecibidoQPermiteAbonos);
        registrar("Abono", chkAbono);
        registrar("Resultado", lblResultado);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        etiquetaPendiente = new javax.swing.JLabel();
        lblPendiente = new javax.swing.JLabel();
        etiquetaMetodo = new javax.swing.JLabel();
        cmbMetodo = new javax.swing.JComboBox<>();
        etiquetaMontoRecibidoQPermiteAbonos = new javax.swing.JLabel();
        txtMontoRecibidoQPermiteAbonos = new javax.swing.JTextField();
        etiquetaAbono = new javax.swing.JLabel();
        chkAbono = new javax.swing.JCheckBox();
        etiquetaAbono.setText("Opcional");
        chkAbono.setText("Registrar solo un abono");
        chkAbono.setOpaque(false);
        etiquetaResultado = new javax.swing.JLabel();
        lblResultado = new javax.swing.JLabel();
        etiquetaResultado.setText("Resultado");
        lblResultado.setText("Cambio: Q 0.00");
        lblResultado.setOpaque(false);
        this.setBackground(new java.awt.Color(255, 255, 255));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(20).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaPendiente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(etiquetaMetodo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(etiquetaMontoRecibidoQPermiteAbonos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(etiquetaAbono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(etiquetaResultado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(18).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(lblPendiente, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE).addComponent(cmbMetodo, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE).addComponent(txtMontoRecibidoQPermiteAbonos, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE).addComponent(chkAbono, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE).addComponent(lblResultado, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)).addGap(20)));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(20).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaPendiente, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(lblPendiente, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(14).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaMetodo, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(cmbMetodo, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(14).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaMontoRecibidoQPermiteAbonos, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(txtMontoRecibidoQPermiteAbonos, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(14).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaAbono, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(chkAbono, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(14).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaResultado, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(lblResultado, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(14).addGap(6, 6, Short.MAX_VALUE)));
        etiquetaPendiente.setText("Pendiente");
        etiquetaPendiente.setFont(new java.awt.Font("SansSerif", 1, 13));
        etiquetaPendiente.setForeground(new java.awt.Color(36, 59, 50));
        lblPendiente.setFont(new java.awt.Font("SansSerif", 0, 14));
        lblPendiente.setText("Información");
        lblPendiente.setForeground(new java.awt.Color(109, 121, 114));
        etiquetaMetodo.setText("Método");
        etiquetaMetodo.setFont(new java.awt.Font("SansSerif", 1, 13));
        etiquetaMetodo.setForeground(new java.awt.Color(36, 59, 50));
        cmbMetodo.setFont(new java.awt.Font("SansSerif", 0, 14));
        etiquetaMontoRecibidoQPermiteAbonos.setText("Monto recibido Q");
        etiquetaMontoRecibidoQPermiteAbonos.setFont(new java.awt.Font("SansSerif", 1, 13));
        etiquetaMontoRecibidoQPermiteAbonos.setForeground(new java.awt.Color(36, 59, 50));
        txtMontoRecibidoQPermiteAbonos.setFont(new java.awt.Font("SansSerif", 0, 14));
        txtMontoRecibidoQPermiteAbonos.setColumns(22);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel etiquetaPendiente;
    private javax.swing.JLabel lblPendiente;
    private javax.swing.JLabel etiquetaMetodo;
    private javax.swing.JComboBox<Object> cmbMetodo;
    private javax.swing.JLabel etiquetaMontoRecibidoQPermiteAbonos;
    private javax.swing.JTextField txtMontoRecibidoQPermiteAbonos;
    private javax.swing.JLabel etiquetaAbono;
    private javax.swing.JCheckBox chkAbono;
    private javax.swing.JLabel etiquetaResultado;
    private javax.swing.JLabel lblResultado;
    // End of variables declaration//GEN-END:variables
}
