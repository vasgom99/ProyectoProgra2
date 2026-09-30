package com.garagecoffee.view;


// Diseño editable en NetBeans: abrir este archivo y elegir Diseño.
public class PedidoDatosForm extends Formulario {

    public PedidoDatosForm() {
        initComponents();
        registrar("Entrega", cmbEntrega);
        registrar("Cliente (— = consumidor final)", cmbClienteConsumidorFinal);
        registrar("Mesa libre", cmbMesaLibre);
        registrar("Dirección guardada", cmbDireccionGuardada);
        registrar("Dirección de entrega", txtDireccionDeEntrega);
        registrar("Costo de envío Q", txtCostoDeEnvioQ);
        registrar("Programado (fecha y hora)", txtProgramadoFechaYHora);
        registrar("Observaciones", txtObservaciones);
    }

    // El pedido local solo necesita mesa; los demás datos aparecen cuando aplican.
    public void mostrarCampos(com.garagecoffee.model.TipoEntrega tipo, boolean opciones) {
        boolean local = tipo == com.garagecoffee.model.TipoEntrega.CONSUMIR_LOCAL;
        boolean domicilio = tipo == com.garagecoffee.model.TipoEntrega.DOMICILIO;
        fila(etiquetaMesaLibre, cmbMesaLibre, local);
        fila(etiquetaClienteConsumidorFinal, cmbClienteConsumidorFinal,
                !local || opciones || cmbClienteConsumidorFinal.getSelectedItem() != null);
        fila(etiquetaDireccionGuardada, cmbDireccionGuardada, domicilio && opciones);
        fila(etiquetaDireccionDeEntrega, txtDireccionDeEntrega, domicilio);
        fila(etiquetaCostoDeEnvioQ, txtCostoDeEnvioQ, domicilio);
        fila(etiquetaProgramadoFechaYHora, txtProgramadoFechaYHora,
                !local && (opciones || !txtProgramadoFechaYHora.getText().isBlank()));
        revalidate(); repaint();
    }
    private void fila(javax.swing.JLabel etiqueta, javax.swing.JComponent campo, boolean visible) {
        etiqueta.setVisible(visible); campo.setVisible(visible);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        etiquetaEntrega = new javax.swing.JLabel();
        cmbEntrega = new javax.swing.JComboBox<>();
        etiquetaClienteConsumidorFinal = new javax.swing.JLabel();
        cmbClienteConsumidorFinal = new javax.swing.JComboBox<>();
        etiquetaMesaLibre = new javax.swing.JLabel();
        cmbMesaLibre = new javax.swing.JComboBox<>();
        etiquetaDireccionGuardada = new javax.swing.JLabel();
        cmbDireccionGuardada = new javax.swing.JComboBox<>();
        etiquetaDireccionDeEntrega = new javax.swing.JLabel();
        txtDireccionDeEntrega = new javax.swing.JTextField();
        etiquetaCostoDeEnvioQ = new javax.swing.JLabel();
        txtCostoDeEnvioQ = new javax.swing.JTextField();
        etiquetaProgramadoFechaYHora = new javax.swing.JLabel();
        txtProgramadoFechaYHora = new javax.swing.JTextField();
        etiquetaObservaciones = new javax.swing.JLabel();
        txtObservaciones = new javax.swing.JTextField();
        this.setBackground(new java.awt.Color(255, 255, 255));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(20).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaEntrega, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(etiquetaClienteConsumidorFinal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(etiquetaMesaLibre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(etiquetaDireccionGuardada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(etiquetaDireccionDeEntrega, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(etiquetaCostoDeEnvioQ, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(etiquetaProgramadoFechaYHora, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(etiquetaObservaciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(18).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(cmbEntrega, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE).addComponent(cmbClienteConsumidorFinal, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE).addComponent(cmbMesaLibre, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE).addComponent(cmbDireccionGuardada, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE).addComponent(txtDireccionDeEntrega, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE).addComponent(txtCostoDeEnvioQ, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE).addComponent(txtProgramadoFechaYHora, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE).addComponent(txtObservaciones, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)).addGap(20)));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(20).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaEntrega, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(cmbEntrega, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(14).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaClienteConsumidorFinal, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(cmbClienteConsumidorFinal, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(14).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaMesaLibre, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(cmbMesaLibre, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(14).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaDireccionGuardada, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(cmbDireccionGuardada, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(14).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaDireccionDeEntrega, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(txtDireccionDeEntrega, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(14).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaCostoDeEnvioQ, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(txtCostoDeEnvioQ, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(14).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaProgramadoFechaYHora, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(txtProgramadoFechaYHora, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(14).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaObservaciones, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(txtObservaciones, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(14).addGap(6, 6, Short.MAX_VALUE)));
        etiquetaEntrega.setText("Entrega");
        etiquetaEntrega.setFont(new java.awt.Font("SansSerif", 1, 13));
        etiquetaEntrega.setForeground(new java.awt.Color(36, 59, 50));
        cmbEntrega.setFont(new java.awt.Font("SansSerif", 0, 14));
        etiquetaClienteConsumidorFinal.setText("Cliente (— = consumidor final)");
        etiquetaClienteConsumidorFinal.setFont(new java.awt.Font("SansSerif", 1, 13));
        etiquetaClienteConsumidorFinal.setForeground(new java.awt.Color(36, 59, 50));
        cmbClienteConsumidorFinal.setFont(new java.awt.Font("SansSerif", 0, 14));
        etiquetaMesaLibre.setText("Mesa libre");
        etiquetaMesaLibre.setFont(new java.awt.Font("SansSerif", 1, 13));
        etiquetaMesaLibre.setForeground(new java.awt.Color(36, 59, 50));
        cmbMesaLibre.setFont(new java.awt.Font("SansSerif", 0, 14));
        etiquetaDireccionGuardada.setText("Dirección guardada");
        etiquetaDireccionGuardada.setFont(new java.awt.Font("SansSerif", 1, 13));
        etiquetaDireccionGuardada.setForeground(new java.awt.Color(36, 59, 50));
        cmbDireccionGuardada.setFont(new java.awt.Font("SansSerif", 0, 14));
        etiquetaDireccionDeEntrega.setText("Dirección de entrega");
        etiquetaDireccionDeEntrega.setFont(new java.awt.Font("SansSerif", 1, 13));
        etiquetaDireccionDeEntrega.setForeground(new java.awt.Color(36, 59, 50));
        txtDireccionDeEntrega.setFont(new java.awt.Font("SansSerif", 0, 14));
        txtDireccionDeEntrega.setColumns(22);
        etiquetaCostoDeEnvioQ.setText("Costo de envío Q");
        etiquetaCostoDeEnvioQ.setFont(new java.awt.Font("SansSerif", 1, 13));
        etiquetaCostoDeEnvioQ.setForeground(new java.awt.Color(36, 59, 50));
        txtCostoDeEnvioQ.setFont(new java.awt.Font("SansSerif", 0, 14));
        txtCostoDeEnvioQ.setColumns(22);
        etiquetaProgramadoFechaYHora.setText("Programado (fecha y hora)");
        etiquetaProgramadoFechaYHora.setFont(new java.awt.Font("SansSerif", 1, 13));
        etiquetaProgramadoFechaYHora.setForeground(new java.awt.Color(36, 59, 50));
        txtProgramadoFechaYHora.setFont(new java.awt.Font("SansSerif", 0, 14));
        txtProgramadoFechaYHora.setColumns(22);
        etiquetaObservaciones.setText("Observaciones");
        etiquetaObservaciones.setFont(new java.awt.Font("SansSerif", 1, 13));
        etiquetaObservaciones.setForeground(new java.awt.Color(36, 59, 50));
        txtObservaciones.setFont(new java.awt.Font("SansSerif", 0, 14));
        txtObservaciones.setColumns(22);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel etiquetaEntrega;
    private javax.swing.JComboBox<Object> cmbEntrega;
    private javax.swing.JLabel etiquetaClienteConsumidorFinal;
    private javax.swing.JComboBox<Object> cmbClienteConsumidorFinal;
    private javax.swing.JLabel etiquetaMesaLibre;
    private javax.swing.JComboBox<Object> cmbMesaLibre;
    private javax.swing.JLabel etiquetaDireccionGuardada;
    private javax.swing.JComboBox<Object> cmbDireccionGuardada;
    private javax.swing.JLabel etiquetaDireccionDeEntrega;
    private javax.swing.JTextField txtDireccionDeEntrega;
    private javax.swing.JLabel etiquetaCostoDeEnvioQ;
    private javax.swing.JTextField txtCostoDeEnvioQ;
    private javax.swing.JLabel etiquetaProgramadoFechaYHora;
    private javax.swing.JTextField txtProgramadoFechaYHora;
    private javax.swing.JLabel etiquetaObservaciones;
    private javax.swing.JTextField txtObservaciones;
    // End of variables declaration//GEN-END:variables
}
