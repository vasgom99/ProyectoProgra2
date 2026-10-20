package com.garagecoffee.view;

import com.garagecoffee.model.RolEmpleado;
public class DashboardFrame extends javax.swing.JFrame {

    private com.garagecoffee.controller.RestauranteController control;
    private final java.util.Map<String, PanelModulo> modulos = new java.util.LinkedHashMap<>();
    private final java.util.Map<String, javax.swing.JButton> botones = new java.util.LinkedHashMap<>();
    private PanelModulo actual;
    public DashboardFrame() {
        initComponents();
        setTitle("Garage Coffee · Restaurante");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        Ui.secundario(btnActualizar); Ui.navegacion(btnSalir, false);
        btnActualizar.addActionListener(e -> Ui.ejecutar(this, () -> { if (control != null) control.recargar(); if (actual != null) actual.recargar(); }));
        btnSalir.addActionListener(e -> {
            if (control == null) return;
            control.sesion().cerrarSesion();
            new LoginFrame(control).setVisible(true);
            dispose();
        });
        setSize(1340, 850); setLocationRelativeTo(null);
    }
    public DashboardFrame(com.garagecoffee.controller.RestauranteController control) {
        this(); this.control = control;
        lblUsuario.setText(control.sesion().actual().getNombre() + " · " + Ui.nombre(control.sesion().actual().getRol()));
        enlace(btnNav0, "Inicio", () -> new InicioPanel(control), true);
        enlace(btnNav1, "Pedidos", () -> new PedidosPanel(control), control.sesion().permite(RolEmpleado.MESERO, RolEmpleado.CAJERO));
        enlace(btnNav2, "Mesas", () -> new AtencionPanel(control), control.sesion().permite(RolEmpleado.MESERO, RolEmpleado.CAJERO));
        enlace(btnNav3, "Cocina", () -> new CocinaPanel(control), control.sesion().permite(RolEmpleado.COCINA));
        enlace(btnNav5, "Productos", () -> new CatalogoPanel(control), control.sesion().permite(RolEmpleado.MESERO, RolEmpleado.COCINA, RolEmpleado.CAJERO));
        enlace(btnNav8, "Caja y cobros", () -> new CajaPanel(control), control.sesion().permite(RolEmpleado.CAJERO));
        enlace(btnNav9, "Empleados", () -> new EmpleadosPanel(control), control.sesion().permite());
        lblSeccion.setText(Ui.nombre(control.sesion().actual().getRol()));
        organizarMenu();
        switch (control.sesion().actual().getRol()) {
            case MESERO -> mostrar("Mesas", () -> new AtencionPanel(control));
            case COCINA -> mostrar("Cocina", () -> new CocinaPanel(control));
            case CAJERO -> mostrar("Caja y cobros", () -> new CajaPanel(control));
            default -> mostrar("Inicio", () -> new InicioPanel(control));
        }
    }
    private void organizarMenu() {
        navegacion.removeAll();
        botones.values().forEach(navegacion::add);
        navegacion.setPreferredSize(new java.awt.Dimension(184, navegacion.getComponentCount() * 36));
        navegacion.revalidate(); navegacion.repaint();
    }
    private void enlace(javax.swing.JButton boton, String nombre, java.util.function.Supplier<PanelModulo> crear, boolean permitido) {
        if (!permitido) { navegacion.remove(boton); return; }
        botones.put(nombre, boton); Ui.navegacion(boton, false);
        boton.setText(nombre);
        boton.addActionListener(e -> Ui.ejecutar(this, () -> mostrar(nombre, crear)));
    }
    private void mostrar(String nombre, java.util.function.Supplier<PanelModulo> crear) {
        PanelModulo modulo = modulos.get(nombre);
        if (modulo == null) { modulo = crear.get(); modulos.put(nombre, modulo); contenido.add(modulo, nombre); }
        modulo.recargar(); actual = modulo; lblTitulo.setText(nombre);
        lblSubtitulo.setText(nombre.equals("Inicio") ? "Vista general de tu restaurante" : "Consulta y administra · " + nombre.toLowerCase());
        botones.forEach((n, b) -> Ui.navegacion(b, n.equals(nombre)));
        ((java.awt.CardLayout) contenido.getLayout()).show(contenido, nombre);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        lateral = new javax.swing.JPanel();
        identidad = new javax.swing.JPanel();
        lblMarca = new javax.swing.JLabel();
        lblSeccion = new javax.swing.JLabel();
        lblNavegar = new javax.swing.JLabel();
        marcoNavegacion = new javax.swing.JPanel();
        navegacion = new javax.swing.JPanel();
        btnNav0 = new javax.swing.JButton();
        btnNav1 = new javax.swing.JButton();
        btnNav2 = new javax.swing.JButton();
        btnNav3 = new javax.swing.JButton();
        btnNav5 = new javax.swing.JButton();
        btnNav8 = new javax.swing.JButton();
        btnNav9 = new javax.swing.JButton();
        sesionPanel = new javax.swing.JPanel();
        lblLocal = new javax.swing.JLabel();
        btnSalir = new javax.swing.JButton();
        areaTrabajo = new javax.swing.JPanel();
        encabezado = new javax.swing.JPanel();
        titulos = new javax.swing.JPanel();
        lblTitulo = new javax.swing.JLabel();
        lblSubtitulo = new javax.swing.JLabel();
        accionesCabecera = new javax.swing.JPanel();
        lblUsuario = new javax.swing.JLabel();
        btnActualizar = new javax.swing.JButton();
        contenido = new javax.swing.JPanel();
        this.setBackground(new java.awt.Color(246, 244, 238));
        this.setMinimumSize(new java.awt.Dimension(1100, 740));
        getContentPane().setLayout(new java.awt.BorderLayout(0, 0));
        lateral.setBackground(new java.awt.Color(36, 59, 50));
        lateral.setPreferredSize(new java.awt.Dimension(220, 730));
        lateral.setBorder(javax.swing.BorderFactory.createEmptyBorder(28, 18, 20, 18));
        lateral.setLayout(new java.awt.BorderLayout(22, 22));
        identidad.setOpaque(false);
        identidad.setPreferredSize(new java.awt.Dimension(184, 94));
        identidad.setLayout(new java.awt.GridLayout(3, 1, 4, 4));
        lblMarca.setText("GARAGE COFFEE");
        lblMarca.setFont(new java.awt.Font("SansSerif", 1, 19));
        lblMarca.setForeground(new java.awt.Color(255, 255, 255));
        identidad.add(lblMarca);
        lblSeccion.setText("ADMINISTRACIÓN");
        lblSeccion.setFont(new java.awt.Font("SansSerif", 1, 10));
        lblSeccion.setForeground(new java.awt.Color(213, 184, 148));
        identidad.add(lblSeccion);
        lblNavegar.setText("ESPACIO DE TRABAJO");
        lblNavegar.setFont(new java.awt.Font("SansSerif", 0, 10));
        lblNavegar.setForeground(new java.awt.Color(157, 177, 164));
        identidad.add(lblNavegar);
        lateral.add(identidad, java.awt.BorderLayout.NORTH);
        marcoNavegacion.setOpaque(false);
        marcoNavegacion.setLayout(new java.awt.BorderLayout(0, 0));
        navegacion.setOpaque(false);
        navegacion.setPreferredSize(new java.awt.Dimension(184, 450));
        navegacion.setLayout(new java.awt.GridLayout(0, 1, 5, 5));
        btnNav0.setText("Inicio");
        btnNav0.setFont(new java.awt.Font("SansSerif", 0, 14));
        btnNav0.setBackground(new java.awt.Color(255, 255, 255));
        btnNav0.setForeground(new java.awt.Color(38, 58, 50));
        btnNav0.setFocusPainted(false);
        navegacion.add(btnNav0);
        btnNav1.setText("Pedidos");
        btnNav1.setFont(new java.awt.Font("SansSerif", 0, 14));
        btnNav1.setBackground(new java.awt.Color(255, 255, 255));
        btnNav1.setForeground(new java.awt.Color(38, 58, 50));
        btnNav1.setFocusPainted(false);
        navegacion.add(btnNav1);
        btnNav2.setText("Mesas");
        btnNav2.setFont(new java.awt.Font("SansSerif", 0, 14));
        btnNav2.setBackground(new java.awt.Color(255, 255, 255));
        btnNav2.setForeground(new java.awt.Color(38, 58, 50));
        btnNav2.setFocusPainted(false);
        navegacion.add(btnNav2);
        btnNav3.setText("Cocina");
        btnNav3.setFont(new java.awt.Font("SansSerif", 0, 14));
        btnNav3.setBackground(new java.awt.Color(255, 255, 255));
        btnNav3.setForeground(new java.awt.Color(38, 58, 50));
        btnNav3.setFocusPainted(false);
        navegacion.add(btnNav3);
        btnNav5.setText("Productos");
        btnNav5.setFont(new java.awt.Font("SansSerif", 0, 14));
        btnNav5.setBackground(new java.awt.Color(255, 255, 255));
        btnNav5.setForeground(new java.awt.Color(38, 58, 50));
        btnNav5.setFocusPainted(false);
        navegacion.add(btnNav5);
        btnNav8.setText("Caja y cobros");
        btnNav8.setFont(new java.awt.Font("SansSerif", 0, 14));
        btnNav8.setBackground(new java.awt.Color(255, 255, 255));
        btnNav8.setForeground(new java.awt.Color(38, 58, 50));
        btnNav8.setFocusPainted(false);
        navegacion.add(btnNav8);
        btnNav9.setText("Empleados");
        btnNav9.setFont(new java.awt.Font("SansSerif", 0, 14));
        btnNav9.setBackground(new java.awt.Color(255, 255, 255));
        btnNav9.setForeground(new java.awt.Color(38, 58, 50));
        btnNav9.setFocusPainted(false);
        navegacion.add(btnNav9);
        marcoNavegacion.add(navegacion, java.awt.BorderLayout.NORTH);
        lateral.add(marcoNavegacion, java.awt.BorderLayout.CENTER);
        sesionPanel.setOpaque(false);
        sesionPanel.setLayout(new java.awt.GridLayout(2, 1, 10, 10));
        lblLocal.setText("GARAGE · SESIÓN LOCAL");
        lblLocal.setFont(new java.awt.Font("SansSerif", 0, 10));
        lblLocal.setForeground(new java.awt.Color(157, 177, 164));
        sesionPanel.add(lblLocal);
        btnSalir.setText("Cambiar usuario");
        btnSalir.setFont(new java.awt.Font("SansSerif", 0, 14));
        btnSalir.setBackground(new java.awt.Color(255, 255, 255));
        btnSalir.setForeground(new java.awt.Color(38, 58, 50));
        btnSalir.setFocusPainted(false);
        sesionPanel.add(btnSalir);
        lateral.add(sesionPanel, java.awt.BorderLayout.SOUTH);
        getContentPane().add(lateral, java.awt.BorderLayout.WEST);
        areaTrabajo.setBackground(new java.awt.Color(246, 244, 238));
        areaTrabajo.setLayout(new java.awt.BorderLayout(0, 0));
        encabezado.setBackground(new java.awt.Color(255, 255, 255));
        encabezado.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 28, 18, 28));
        encabezado.setPreferredSize(new java.awt.Dimension(850, 100));
        encabezado.setLayout(new java.awt.BorderLayout(20, 20));
        titulos.setOpaque(false);
        titulos.setLayout(new java.awt.GridLayout(2, 1, 4, 4));
        lblTitulo.setText("Inicio");
        lblTitulo.setFont(new java.awt.Font("SansSerif", 1, 27));
        lblTitulo.setForeground(new java.awt.Color(36, 59, 50));
        titulos.add(lblTitulo);
        lblSubtitulo.setText("Vista general de tu restaurante");
        lblSubtitulo.setFont(new java.awt.Font("SansSerif", 0, 13));
        lblSubtitulo.setForeground(new java.awt.Color(109, 121, 114));
        titulos.add(lblSubtitulo);
        encabezado.add(titulos, java.awt.BorderLayout.CENTER);
        accionesCabecera.setOpaque(false);
        accionesCabecera.setLayout(new java.awt.FlowLayout(2, 16, 6));
        lblUsuario.setText("Usuario · ROL");
        lblUsuario.setFont(new java.awt.Font("SansSerif", 0, 12));
        lblUsuario.setForeground(new java.awt.Color(109, 121, 114));
        accionesCabecera.add(lblUsuario);
        btnActualizar.setText("Actualizar");
        btnActualizar.setFont(new java.awt.Font("SansSerif", 0, 14));
        btnActualizar.setBackground(new java.awt.Color(255, 255, 255));
        btnActualizar.setForeground(new java.awt.Color(38, 58, 50));
        btnActualizar.setFocusPainted(false);
        accionesCabecera.add(btnActualizar);
        encabezado.add(accionesCabecera, java.awt.BorderLayout.EAST);
        areaTrabajo.add(encabezado, java.awt.BorderLayout.NORTH);
        contenido.setBackground(new java.awt.Color(246, 244, 238));
        contenido.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 24, 12, 24));
        contenido.setLayout(new java.awt.CardLayout());
        areaTrabajo.add(contenido, java.awt.BorderLayout.CENTER);
        getContentPane().add(areaTrabajo, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel lateral;
    private javax.swing.JPanel identidad;
    private javax.swing.JLabel lblMarca;
    private javax.swing.JLabel lblSeccion;
    private javax.swing.JLabel lblNavegar;
    private javax.swing.JPanel marcoNavegacion;
    private javax.swing.JPanel navegacion;
    private javax.swing.JButton btnNav0;
    private javax.swing.JButton btnNav1;
    private javax.swing.JButton btnNav2;
    private javax.swing.JButton btnNav3;
    private javax.swing.JButton btnNav5;
    private javax.swing.JButton btnNav8;
    private javax.swing.JButton btnNav9;
    private javax.swing.JPanel sesionPanel;
    private javax.swing.JLabel lblLocal;
    private javax.swing.JButton btnSalir;
    private javax.swing.JPanel areaTrabajo;
    private javax.swing.JPanel encabezado;
    private javax.swing.JPanel titulos;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lblSubtitulo;
    private javax.swing.JPanel accionesCabecera;
    private javax.swing.JLabel lblUsuario;
    private javax.swing.JButton btnActualizar;
    private javax.swing.JPanel contenido;
    // End of variables declaration//GEN-END:variables
}
