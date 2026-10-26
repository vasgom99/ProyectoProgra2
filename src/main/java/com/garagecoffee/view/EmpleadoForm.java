package com.garagecoffee.view;


public class EmpleadoForm extends Formulario {

    public EmpleadoForm() {
        initComponents();
        registrar("Nombre", txtNombre);
        registrar("Apellido", txtApellido);
        registrar("Usuario", txtUsuario);
        registrar("Clave (vacío conserva la anterior)", pwdClaveVacioConservaLaAnterior);
        registrar("Teléfono", txtTelefono);
        registrar("Correo", txtCorreo);
        registrar("Rol", cmbRol);
        registrar("Activo", chkActivo);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        etiquetaNombre = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        etiquetaApellido = new javax.swing.JLabel();
        txtApellido = new javax.swing.JTextField();
        etiquetaUsuario = new javax.swing.JLabel();
        txtUsuario = new javax.swing.JTextField();
        etiquetaClaveVacioConservaLaAnterior = new javax.swing.JLabel();
        pwdClaveVacioConservaLaAnterior = new javax.swing.JPasswordField();
        etiquetaTelefono = new javax.swing.JLabel();
        txtTelefono = new javax.swing.JTextField();
        etiquetaCorreo = new javax.swing.JLabel();
        txtCorreo = new javax.swing.JTextField();
        etiquetaRol = new javax.swing.JLabel();
        cmbRol = new javax.swing.JComboBox<>();
        etiquetaActivo = new javax.swing.JLabel();
        chkActivo = new javax.swing.JCheckBox();
        this.setBackground(new java.awt.Color(255, 255, 255));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(20).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(etiquetaApellido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(etiquetaUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(etiquetaClaveVacioConservaLaAnterior, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(etiquetaTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(etiquetaCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(etiquetaRol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(etiquetaActivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(18).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(txtNombre, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE).addComponent(txtApellido, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE).addComponent(txtUsuario, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE).addComponent(pwdClaveVacioConservaLaAnterior, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE).addComponent(txtTelefono, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE).addComponent(txtCorreo, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE).addComponent(cmbRol, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE).addComponent(chkActivo, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)).addGap(20)));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(20).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(14).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaApellido, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(txtApellido, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(14).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(txtUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(14).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaClaveVacioConservaLaAnterior, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(pwdClaveVacioConservaLaAnterior, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(14).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(14).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(txtCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(14).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaRol, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(cmbRol, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(14).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaActivo, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(chkActivo, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(14).addGap(6, 6, Short.MAX_VALUE)));
        etiquetaNombre.setText("Nombre");
        etiquetaNombre.setFont(new java.awt.Font("SansSerif", 1, 13));
        etiquetaNombre.setForeground(new java.awt.Color(36, 59, 50));
        txtNombre.setFont(new java.awt.Font("SansSerif", 0, 14));
        txtNombre.setColumns(22);
        etiquetaApellido.setText("Apellido");
        etiquetaApellido.setFont(new java.awt.Font("SansSerif", 1, 13));
        etiquetaApellido.setForeground(new java.awt.Color(36, 59, 50));
        txtApellido.setFont(new java.awt.Font("SansSerif", 0, 14));
        txtApellido.setColumns(22);
        etiquetaUsuario.setText("Usuario");
        etiquetaUsuario.setFont(new java.awt.Font("SansSerif", 1, 13));
        etiquetaUsuario.setForeground(new java.awt.Color(36, 59, 50));
        txtUsuario.setFont(new java.awt.Font("SansSerif", 0, 14));
        txtUsuario.setColumns(22);
        etiquetaClaveVacioConservaLaAnterior.setText("Clave (vacío conserva la anterior)");
        etiquetaClaveVacioConservaLaAnterior.setFont(new java.awt.Font("SansSerif", 1, 13));
        etiquetaClaveVacioConservaLaAnterior.setForeground(new java.awt.Color(36, 59, 50));
        pwdClaveVacioConservaLaAnterior.setFont(new java.awt.Font("SansSerif", 0, 14));
        pwdClaveVacioConservaLaAnterior.setColumns(22);
        etiquetaTelefono.setText("Teléfono");
        etiquetaTelefono.setFont(new java.awt.Font("SansSerif", 1, 13));
        etiquetaTelefono.setForeground(new java.awt.Color(36, 59, 50));
        txtTelefono.setFont(new java.awt.Font("SansSerif", 0, 14));
        txtTelefono.setColumns(22);
        etiquetaCorreo.setText("Correo");
        etiquetaCorreo.setFont(new java.awt.Font("SansSerif", 1, 13));
        etiquetaCorreo.setForeground(new java.awt.Color(36, 59, 50));
        txtCorreo.setFont(new java.awt.Font("SansSerif", 0, 14));
        txtCorreo.setColumns(22);
        etiquetaRol.setText("Rol");
        etiquetaRol.setFont(new java.awt.Font("SansSerif", 1, 13));
        etiquetaRol.setForeground(new java.awt.Color(36, 59, 50));
        cmbRol.setFont(new java.awt.Font("SansSerif", 0, 14));
        etiquetaActivo.setText("Activo");
        etiquetaActivo.setFont(new java.awt.Font("SansSerif", 1, 13));
        etiquetaActivo.setForeground(new java.awt.Color(36, 59, 50));
        chkActivo.setFont(new java.awt.Font("SansSerif", 0, 14));
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel etiquetaNombre;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JLabel etiquetaApellido;
    private javax.swing.JTextField txtApellido;
    private javax.swing.JLabel etiquetaUsuario;
    private javax.swing.JTextField txtUsuario;
    private javax.swing.JLabel etiquetaClaveVacioConservaLaAnterior;
    private javax.swing.JPasswordField pwdClaveVacioConservaLaAnterior;
    private javax.swing.JLabel etiquetaTelefono;
    private javax.swing.JTextField txtTelefono;
    private javax.swing.JLabel etiquetaCorreo;
    private javax.swing.JTextField txtCorreo;
    private javax.swing.JLabel etiquetaRol;
    private javax.swing.JComboBox<Object> cmbRol;
    private javax.swing.JLabel etiquetaActivo;
    private javax.swing.JCheckBox chkActivo;
    // End of variables declaration//GEN-END:variables
}
