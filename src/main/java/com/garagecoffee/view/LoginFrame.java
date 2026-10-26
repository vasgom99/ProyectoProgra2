package com.garagecoffee.view;


public class LoginFrame extends javax.swing.JFrame {

    private com.garagecoffee.controller.RestauranteController control;
    public LoginFrame() {
        initComponents();
        setTitle("Garage Coffee · Acceso");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        Ui.primario(btnEntrar);
        txtUsuario.getAccessibleContext().setAccessibleName("Usuario");
        txtClave.getAccessibleContext().setAccessibleName("Contraseña");
        btnEntrar.addActionListener(e -> ingresar());
        getRootPane().setDefaultButton(btnEntrar);
        setSize(1020, 650); setLocationRelativeTo(null);
    }
    public LoginFrame(com.garagecoffee.controller.RestauranteController control) { this(); this.control = control; }
    private void ingresar() {
        if (control == null) control = new com.garagecoffee.controller.RestauranteController();
        try {
            control.sesion().iniciarSesion(txtUsuario.getText(), txtClave.getPassword());
            txtClave.setText("");
            new DashboardFrame(control).setVisible(true); dispose();
        } catch (IllegalArgumentException | IllegalStateException ex) {
            lblError.setText(ex.getMessage()); txtClave.setText(""); txtClave.requestFocusInWindow();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        panelMarca = new javax.swing.JPanel();
        marcaSuperior = new javax.swing.JPanel();
        lblSello = new javax.swing.JLabel();
        lblMarca = new javax.swing.JLabel();
        lblMarcaDetalle = new javax.swing.JLabel();
        marcaMensaje = new javax.swing.JPanel();
        lblMensaje = new javax.swing.JLabel();
        lblDescripcion = new javax.swing.JLabel();
        panelAcceso = new javax.swing.JPanel();
        cabeceraAcceso = new javax.swing.JPanel();
        lblBienvenido = new javax.swing.JLabel();
        lblInstruccion = new javax.swing.JLabel();
        lblModo = new javax.swing.JLabel();
        camposAcceso = new javax.swing.JPanel();
        lblUsuario = new javax.swing.JLabel();
        txtUsuario = new javax.swing.JTextField();
        lblClave = new javax.swing.JLabel();
        txtClave = new javax.swing.JPasswordField();
        lblError = new javax.swing.JLabel();
        btnEntrar = new javax.swing.JButton();
        this.setBackground(new java.awt.Color(246, 244, 238));
        this.setMinimumSize(new java.awt.Dimension(920, 590));
        getContentPane().setLayout(new java.awt.GridLayout(1, 2, 0, 0));
        panelMarca.setBackground(new java.awt.Color(36, 59, 50));
        panelMarca.setBorder(javax.swing.BorderFactory.createEmptyBorder(42, 42, 38, 42));
        panelMarca.setLayout(new java.awt.BorderLayout(26, 26));
        marcaSuperior.setOpaque(false);
        marcaSuperior.setLayout(new java.awt.GridLayout(3, 1, 12, 12));
        lblSello.setText("G / C");
        lblSello.setFont(new java.awt.Font("SansSerif", 1, 56));
        lblSello.setForeground(new java.awt.Color(213, 184, 148));
        marcaSuperior.add(lblSello);
        lblMarca.setText("GARAGE COFFEE");
        lblMarca.setFont(new java.awt.Font("SansSerif", 1, 26));
        lblMarca.setForeground(new java.awt.Color(255, 255, 255));
        marcaSuperior.add(lblMarca);
        lblMarcaDetalle.setText("CAFÉ  ·  COCINA  ·  SERVICIO");
        lblMarcaDetalle.setFont(new java.awt.Font("SansSerif", 0, 12));
        lblMarcaDetalle.setForeground(new java.awt.Color(213, 184, 148));
        marcaSuperior.add(lblMarcaDetalle);
        panelMarca.add(marcaSuperior, java.awt.BorderLayout.NORTH);
        marcaMensaje.setOpaque(false);
        marcaMensaje.setLayout(new java.awt.GridLayout(2, 1, 18, 18));
        lblMensaje.setText("<html>Un buen día<br>empieza aquí.</html>");
        lblMensaje.setFont(new java.awt.Font("SansSerif", 1, 38));
        lblMensaje.setForeground(new java.awt.Color(255, 255, 255));
        marcaMensaje.add(lblMensaje);
        lblDescripcion.setText("<html>Tu restaurante, organizado.<br>Del primer pedido al cierre de caja.</html>");
        lblDescripcion.setFont(new java.awt.Font("SansSerif", 0, 16));
        lblDescripcion.setForeground(new java.awt.Color(199, 210, 201));
        marcaMensaje.add(lblDescripcion);
        panelMarca.add(marcaMensaje, java.awt.BorderLayout.CENTER);
        getContentPane().add(panelMarca);
        panelAcceso.setBackground(new java.awt.Color(255, 255, 255));
        panelAcceso.setBorder(javax.swing.BorderFactory.createEmptyBorder(38, 32, 30, 32));
        panelAcceso.setLayout(new java.awt.BorderLayout(20, 20));
        cabeceraAcceso.setOpaque(false);
        cabeceraAcceso.setLayout(new java.awt.GridLayout(3, 1, 5, 5));
        lblBienvenido.setText("Bienvenido de nuevo");
        lblBienvenido.setFont(new java.awt.Font("SansSerif", 1, 26));
        lblBienvenido.setForeground(new java.awt.Color(36, 59, 50));
        cabeceraAcceso.add(lblBienvenido);
        lblInstruccion.setText("Ingresa para continuar con el servicio.");
        lblInstruccion.setFont(new java.awt.Font("SansSerif", 0, 14));
        lblInstruccion.setForeground(new java.awt.Color(109, 121, 114));
        cabeceraAcceso.add(lblInstruccion);
        lblModo.setText("ACCESO AL SISTEMA");
        lblModo.setFont(new java.awt.Font("SansSerif", 1, 11));
        lblModo.setForeground(new java.awt.Color(174, 96, 64));
        cabeceraAcceso.add(lblModo);
        panelAcceso.add(cabeceraAcceso, java.awt.BorderLayout.NORTH);
        camposAcceso.setOpaque(false);
        camposAcceso.setLayout(new java.awt.GridLayout(6, 1, 8, 8));
        lblUsuario.setText("Usuario");
        lblUsuario.setFont(new java.awt.Font("SansSerif", 1, 13));
        lblUsuario.setForeground(new java.awt.Color(36, 59, 50));
        camposAcceso.add(lblUsuario);
        txtUsuario.setFont(new java.awt.Font("SansSerif", 0, 15));
        txtUsuario.setToolTipText("Usuario del restaurante");
        camposAcceso.add(txtUsuario);
        lblClave.setText("Contraseña");
        lblClave.setFont(new java.awt.Font("SansSerif", 1, 13));
        lblClave.setForeground(new java.awt.Color(36, 59, 50));
        camposAcceso.add(lblClave);
        txtClave.setFont(new java.awt.Font("SansSerif", 0, 15));
        camposAcceso.add(txtClave);
        lblError.setText(" ");
        lblError.setFont(new java.awt.Font("SansSerif", 0, 12));
        lblError.setForeground(new java.awt.Color(178, 61, 50));
        camposAcceso.add(lblError);
        btnEntrar.setText("Iniciar sesión");
        btnEntrar.setFont(new java.awt.Font("SansSerif", 0, 14));
        btnEntrar.setBackground(new java.awt.Color(255, 255, 255));
        btnEntrar.setForeground(new java.awt.Color(38, 58, 50));
        btnEntrar.setFocusPainted(false);
        camposAcceso.add(btnEntrar);
        panelAcceso.add(camposAcceso, java.awt.BorderLayout.CENTER);
        getContentPane().add(panelAcceso);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel panelMarca;
    private javax.swing.JPanel marcaSuperior;
    private javax.swing.JLabel lblSello;
    private javax.swing.JLabel lblMarca;
    private javax.swing.JLabel lblMarcaDetalle;
    private javax.swing.JPanel marcaMensaje;
    private javax.swing.JLabel lblMensaje;
    private javax.swing.JLabel lblDescripcion;
    private javax.swing.JPanel panelAcceso;
    private javax.swing.JPanel cabeceraAcceso;
    private javax.swing.JLabel lblBienvenido;
    private javax.swing.JLabel lblInstruccion;
    private javax.swing.JLabel lblModo;
    private javax.swing.JPanel camposAcceso;
    private javax.swing.JLabel lblUsuario;
    private javax.swing.JTextField txtUsuario;
    private javax.swing.JLabel lblClave;
    private javax.swing.JPasswordField txtClave;
    private javax.swing.JLabel lblError;
    private javax.swing.JButton btnEntrar;
    // End of variables declaration//GEN-END:variables
}
