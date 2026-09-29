package com.garagecoffee.view;


// Diseño editable en NetBeans: abrir este archivo y elegir Diseño.
public class ReportesPanel extends PanelModulo {

    private com.garagecoffee.controller.RestauranteController control;
    public ReportesPanel() {
        initComponents();
        tipo.setModel(new javax.swing.DefaultComboBoxModel<>(com.garagecoffee.service.ReporteService.Tipo.values()));
        tipo.setRenderer(new javax.swing.DefaultListCellRenderer() {
            @Override public java.awt.Component getListCellRendererComponent(javax.swing.JList<?> lista, Object valor, int indice, boolean elegido, boolean foco) {
                return super.getListCellRendererComponent(lista, Ui.nombre(valor), indice, elegido, foco);
            }
        });
        inicio.setText(java.time.LocalDate.now().withDayOfMonth(1).toString());
        fin.setText(java.time.LocalDate.now().toString());
        inicio.getAccessibleContext().setAccessibleName("Desde AAAA-MM-DD");
        fin.getAccessibleContext().setAccessibleName("Hasta AAAA-MM-DD");
        resultado.setMargin(new java.awt.Insets(24, 26, 24, 26));
        Ui.primario(btnGenerar); Ui.secundario(btnGuardar); Ui.secundario(btnImprimir);
        btnGenerar.addActionListener(e -> Ui.ejecutar(this, this::recargar));
        btnGuardar.addActionListener(e -> Ui.ejecutar(this, () -> Ui.guardarTexto(this, resultado.getText())));
        btnImprimir.addActionListener(e -> Ui.ejecutar(this, () -> Ui.imprimir(this, resultado, "Garage Coffee")));
    }
    public ReportesPanel(com.garagecoffee.controller.RestauranteController control) { this(); this.control = control; }
    @Override public void recargar() {
        if (control == null) return;
        try {
            resultado.setText(control.reportes().generar(tipo.getItemAt(tipo.getSelectedIndex()),
                    java.time.LocalDate.parse(inicio.getText().trim()), java.time.LocalDate.parse(fin.getText().trim())));
            resultado.setCaretPosition(0);
        } catch (java.time.format.DateTimeParseException ex) {
            throw new IllegalArgumentException("Indica fechas válidas con formato AAAA-MM-DD.");
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        panelFiltros = new javax.swing.JPanel();
        campos = new javax.swing.JPanel();
        lblTipo = new javax.swing.JLabel();
        lblDesde = new javax.swing.JLabel();
        lblHasta = new javax.swing.JLabel();
        tipo = new javax.swing.JComboBox<>();
        inicio = new javax.swing.JTextField();
        fin = new javax.swing.JTextField();
        acciones = new javax.swing.JPanel();
        btnGenerar = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        btnImprimir = new javax.swing.JButton();
        scrollReporte = new javax.swing.JScrollPane();
        resultado = new javax.swing.JTextArea();
        lblAyuda = new javax.swing.JLabel();
        this.setBackground(new java.awt.Color(246, 244, 238));
        this.setLayout(new java.awt.BorderLayout(16, 16));
        panelFiltros.setBackground(new java.awt.Color(255, 255, 255));
        panelFiltros.setBorder(javax.swing.BorderFactory.createEmptyBorder(18, 20, 18, 20));
        panelFiltros.setLayout(new java.awt.BorderLayout(12, 12));
        campos.setOpaque(false);
        campos.setLayout(new java.awt.GridLayout(2, 3, 12, 12));
        lblTipo.setText("REPORTE");
        lblTipo.setFont(new java.awt.Font("SansSerif", 1, 11));
        lblTipo.setForeground(new java.awt.Color(109, 121, 114));
        campos.add(lblTipo);
        lblDesde.setText("DESDE · AAAA-MM-DD");
        lblDesde.setFont(new java.awt.Font("SansSerif", 1, 11));
        lblDesde.setForeground(new java.awt.Color(109, 121, 114));
        campos.add(lblDesde);
        lblHasta.setText("HASTA · AAAA-MM-DD");
        lblHasta.setFont(new java.awt.Font("SansSerif", 1, 11));
        lblHasta.setForeground(new java.awt.Color(109, 121, 114));
        campos.add(lblHasta);
        tipo.setFont(new java.awt.Font("SansSerif", 0, 14));
        campos.add(tipo);
        inicio.setFont(new java.awt.Font("SansSerif", 0, 14));
        campos.add(inicio);
        fin.setFont(new java.awt.Font("SansSerif", 0, 14));
        campos.add(fin);
        panelFiltros.add(campos, java.awt.BorderLayout.CENTER);
        acciones.setOpaque(false);
        acciones.setLayout(new java.awt.FlowLayout(2, 8, 6));
        btnGenerar.setText("Generar");
        btnGenerar.setFont(new java.awt.Font("SansSerif", 0, 14));
        btnGenerar.setBackground(new java.awt.Color(255, 255, 255));
        btnGenerar.setForeground(new java.awt.Color(38, 58, 50));
        btnGenerar.setFocusPainted(false);
        acciones.add(btnGenerar);
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
        panelFiltros.add(acciones, java.awt.BorderLayout.SOUTH);
        this.add(panelFiltros, java.awt.BorderLayout.NORTH);
        scrollReporte.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        resultado.setEditable(false);
        resultado.setFont(new java.awt.Font("SansSerif", 0, 14));
        resultado.setBackground(new java.awt.Color(255, 255, 255));
        resultado.setForeground(new java.awt.Color(36, 59, 50));
        resultado.setLineWrap(true);
        resultado.setWrapStyleWord(true);
        scrollReporte.setViewportView(resultado);
        this.add(scrollReporte, java.awt.BorderLayout.CENTER);
        lblAyuda.setText("PDF depende del sistema. Los reportes y comprobantes no tienen validez fiscal.");
        lblAyuda.setFont(new java.awt.Font("SansSerif", 0, 12));
        lblAyuda.setForeground(new java.awt.Color(109, 121, 114));
        this.add(lblAyuda, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel panelFiltros;
    private javax.swing.JPanel campos;
    private javax.swing.JLabel lblTipo;
    private javax.swing.JLabel lblDesde;
    private javax.swing.JLabel lblHasta;
    private javax.swing.JComboBox<com.garagecoffee.service.ReporteService.Tipo> tipo;
    private javax.swing.JTextField inicio;
    private javax.swing.JTextField fin;
    private javax.swing.JPanel acciones;
    private javax.swing.JButton btnGenerar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnImprimir;
    private javax.swing.JScrollPane scrollReporte;
    private javax.swing.JTextArea resultado;
    private javax.swing.JLabel lblAyuda;
    // End of variables declaration//GEN-END:variables
}
