package com.garagecoffee.view;


public class TextoDialog extends javax.swing.JDialog {

    public TextoDialog() { this(null, "Documento", ""); }
    public TextoDialog(java.awt.Window padre, String titulo, String texto) {
        this(padre, titulo, texto, true);
    }
    public TextoDialog(java.awt.Window padre, String titulo, String texto, boolean permitirGuardarTxt) {
        super(padre, titulo, ModalityType.APPLICATION_MODAL);
        initComponents();
        lblTitulo.setText(titulo); area.setText(texto); area.setCaretPosition(0);
        area.setMargin(new java.awt.Insets(20, 24, 20, 24));
        btnGuardar.setVisible(permitirGuardarTxt);
        Ui.secundario(btnGuardar); Ui.secundario(btnImprimir); Ui.primario(btnCerrar);
        btnGuardar.addActionListener(e -> Ui.ejecutar(this, () -> Ui.guardarTexto(this, area.getText())));
        btnImprimir.addActionListener(e -> Ui.ejecutar(this, () -> Ui.imprimir(this, area, titulo)));
        btnCerrar.addActionListener(e -> dispose());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(820, 640); setLocationRelativeTo(padre);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        encabezado = new javax.swing.JPanel();
        lblTitulo = new javax.swing.JLabel();
        scrollTexto = new javax.swing.JScrollPane();
        area = new javax.swing.JTextArea();
        acciones = new javax.swing.JPanel();
        btnGuardar = new javax.swing.JButton();
        btnImprimir = new javax.swing.JButton();
        btnCerrar = new javax.swing.JButton();
        this.setBackground(new java.awt.Color(246, 244, 238));
        this.setMinimumSize(new java.awt.Dimension(700, 500));
        getContentPane().setLayout(new java.awt.BorderLayout(14, 14));
        encabezado.setBackground(new java.awt.Color(36, 59, 50));
        encabezado.setBorder(javax.swing.BorderFactory.createEmptyBorder(18, 24, 18, 24));
        encabezado.setLayout(new java.awt.BorderLayout(0, 0));
        lblTitulo.setText("Documento del restaurante");
        lblTitulo.setFont(new java.awt.Font("SansSerif", 1, 20));
        lblTitulo.setForeground(new java.awt.Color(255, 255, 255));
        encabezado.add(lblTitulo, java.awt.BorderLayout.CENTER);
        getContentPane().add(encabezado, java.awt.BorderLayout.NORTH);
        scrollTexto.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        area.setEditable(false);
        area.setFont(new java.awt.Font("SansSerif", 0, 14));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBackground(new java.awt.Color(255, 255, 255));
        area.setForeground(new java.awt.Color(36, 59, 50));
        scrollTexto.setViewportView(area);
        getContentPane().add(scrollTexto, java.awt.BorderLayout.CENTER);
        acciones.setOpaque(false);
        acciones.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 16, 12, 16));
        acciones.setLayout(new java.awt.FlowLayout(2, 10, 6));
        btnGuardar.setText("Guardar TXT");
        btnGuardar.setFont(new java.awt.Font("SansSerif", 0, 14));
        btnGuardar.setBackground(new java.awt.Color(255, 255, 255));
        btnGuardar.setForeground(new java.awt.Color(38, 58, 50));
        btnGuardar.setFocusPainted(false);
        acciones.add(btnGuardar);
        btnImprimir.setText("Imprimir / PDF…");
        btnImprimir.setFont(new java.awt.Font("SansSerif", 0, 14));
        btnImprimir.setBackground(new java.awt.Color(255, 255, 255));
        btnImprimir.setForeground(new java.awt.Color(38, 58, 50));
        btnImprimir.setFocusPainted(false);
        acciones.add(btnImprimir);
        btnCerrar.setText("Cerrar");
        btnCerrar.setFont(new java.awt.Font("SansSerif", 0, 14));
        btnCerrar.setBackground(new java.awt.Color(255, 255, 255));
        btnCerrar.setForeground(new java.awt.Color(38, 58, 50));
        btnCerrar.setFocusPainted(false);
        acciones.add(btnCerrar);
        getContentPane().add(acciones, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel encabezado;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JScrollPane scrollTexto;
    private javax.swing.JTextArea area;
    private javax.swing.JPanel acciones;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnImprimir;
    private javax.swing.JButton btnCerrar;
    // End of variables declaration//GEN-END:variables
}
