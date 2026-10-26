package com.garagecoffee.view;


public class PanelListado<T> extends javax.swing.JPanel {

    private java.util.function.Supplier<java.util.List<T>> consulta = java.util.List::of;
    private java.util.function.Function<T, Object[]> columnas;
    private java.util.List<T> filas = java.util.List.of();
    private javax.swing.table.DefaultTableModel modelo;
    private int siguienteAccion;
    private javax.swing.JButton[] botones;
    private javax.swing.table.TableRowSorter<javax.swing.table.DefaultTableModel> orden;
    private String mensajeVacio = "No hay registros todavía.";
    public PanelListado() {
        initComponents();
        botones = new javax.swing.JButton[]{accion0, accion1, accion2, accion3, accion4, accion5, accion6, accion7};
        for (javax.swing.JButton boton : botones) { Ui.secundario(boton); boton.setVisible(false); }
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override public void componentResized(java.awt.event.ComponentEvent e) { ajustarAcciones(); }
        });
        tabla.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tabla.getTableHeader().setReorderingAllowed(false);
        tabla.getTableHeader().setPreferredSize(new java.awt.Dimension(0, 38));
        scrollTabla.setColumnHeaderView(tabla.getTableHeader());
        tabla.setGridColor(new java.awt.Color(232, 237, 230));
        tabla.getTableHeader().setDefaultRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            @Override public java.awt.Component getTableCellRendererComponent(javax.swing.JTable t, Object valor, boolean seleccionado, boolean foco, int fila, int columna) {
                javax.swing.JLabel etiqueta = (javax.swing.JLabel) super.getTableCellRendererComponent(t, valor, seleccionado, foco, fila, columna);
                etiqueta.setBackground(new java.awt.Color(237, 242, 236)); etiqueta.setForeground(Ui.CAFE);
                etiqueta.setFont(new java.awt.Font(java.awt.Font.SANS_SERIF, java.awt.Font.BOLD, 12));
                etiqueta.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
                return etiqueta;
            }
        });
      javax.swing.table.DefaultTableCellRenderer renderLimpio = new javax.swing.table.DefaultTableCellRenderer() {
            @Override public java.awt.Component getTableCellRendererComponent(javax.swing.JTable t, Object valor, boolean seleccionado, boolean foco, int fila, int columna) {
                if (valor instanceof java.math.BigDecimal bd) valor = bd.stripTrailingZeros().toPlainString();
                return super.getTableCellRendererComponent(t, valor, seleccionado, foco, fila, columna);
            }
        };
        tabla.setDefaultRenderer(Object.class, renderLimpio);
        tabla.setDefaultRenderer(java.math.BigDecimal.class, renderLimpio);
        buscar.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void filtrar() {
                if (orden == null) return;
                orden.setRowFilter(javax.swing.RowFilter.regexFilter("(?iu)" + java.util.regex.Pattern.quote(buscar.getText())));
                actualizarContador();
            }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
        });
        tabla.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override public void mouseMoved(java.awt.event.MouseEvent e) {
                int fila = tabla.rowAtPoint(e.getPoint()), columna = tabla.columnAtPoint(e.getPoint());
                tabla.setToolTipText(fila < 0 || columna < 0 ? null : String.valueOf(tabla.getValueAt(fila, columna)));
            }
        });
    }
    public PanelListado(String[] encabezados, java.util.function.Supplier<java.util.List<T>> consulta, java.util.function.Function<T, Object[]> columnas) {
        this(); configurar(encabezados, consulta, columnas);
    }
    public void configurar(String[] encabezados, java.util.function.Supplier<java.util.List<T>> consulta, java.util.function.Function<T, Object[]> columnas) {
        this.consulta = consulta; this.columnas = columnas;
        modelo = new javax.swing.table.DefaultTableModel(encabezados, 0) {
            @Override public boolean isCellEditable(int fila, int columna) { return false; }
            @Override public Class<?> getColumnClass(int columna) {
                for (int i = 0; i < getRowCount(); i++) if (getValueAt(i, columna) != null) return getValueAt(i, columna).getClass();
                return Object.class;
            }
        };
        tabla.setModel(modelo);
        orden = new javax.swing.table.TableRowSorter<>(modelo); tabla.setRowSorter(orden);
        for (int i = 0; i < encabezados.length; i++) {
            if (java.util.List.of("ID", "Pedido", "Caja").contains(encabezados[i])) {
                tabla.getColumnModel().getColumn(i).setPreferredWidth(65);
                tabla.getColumnModel().getColumn(i).setMaxWidth(80);
            }
        }
    }
    public javax.swing.JButton boton(String titulo, Runnable accion) {
        if (siguienteAccion >= botones.length) throw new IllegalStateException("No hay más botones disponibles en el formulario.");
        javax.swing.JButton boton = botones[siguienteAccion++];
        boton.setText(titulo); boton.setVisible(true);
        if (siguienteAccion == 1) Ui.primario(boton);
        boton.addActionListener(e -> Ui.ejecutar(this, () -> { accion.run(); recargar(); }));
        ajustarAcciones();
        return boton;
    }
    // muestra las acciones disponibles.
    public void alternar(String mostrar, String ocultar, java.util.function.Consumer<Boolean> cambio) {
        javax.swing.JButton boton = boton(mostrar, () -> { });
        for (var listener : boton.getActionListeners()) boton.removeActionListener(listener);
        boton.addActionListener(e -> Ui.ejecutar(this, () -> {
            boolean abierto = !Boolean.TRUE.equals(boton.getClientProperty("abierto"));
            cambio.accept(abierto);
            boton.putClientProperty("abierto", abierto);
            boton.setText(abierto ? ocultar : mostrar);
            ajustarAcciones();
            recargar();
        }));
    }
    public javax.swing.JButton accion(String titulo, java.util.function.Consumer<T> accion) {
        return boton(titulo, () -> accion.accept(seleccionado()));
    }
    public void accionesSecundarias(java.util.List<javax.swing.JButton> secundarios) {
        secundarios.forEach(b -> b.setVisible(false));
        alternar("Más acciones", "Menos acciones", mostrar -> {
            secundarios.forEach(b -> b.setVisible(mostrar));
            ajustarAcciones();
        });
    }
    // ajusta los botones al espacio disponible.
    private void ajustarAcciones() {
        int ancho = getWidth() > 0 ? getWidth() - getInsets().left - getInsets().right : 550;
        int usado = 8, alturaFila = 0, altura = 12;
        for (var boton : botones) if (boton.isVisible()) {
            var tamano = boton.getPreferredSize();
            if (usado > 8 && usado + tamano.width + 8 > ancho) {
                altura += alturaFila + 6; usado = 8; alturaFila = 0;
            }
            usado += tamano.width + 8; alturaFila = Math.max(alturaFila, tamano.height);
        }
        altura += alturaFila;
        if (!acciones.isPreferredSizeSet() || acciones.getPreferredSize().height != altura) {
            acciones.setPreferredSize(new java.awt.Dimension(0, altura));
            acciones.revalidate();
        }
    }
    public T seleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) throw new IllegalArgumentException("Selecciona primero una fila.");
        return filas.get(tabla.convertRowIndexToModel(fila));
    }
    public void mensajeVacio(String mensaje) { mensajeVacio = mensaje; actualizarContador(); }
    private void actualizarContador() {
        contador.setText(tabla.getRowCount() > 0 ? tabla.getRowCount() + " registros"
                : buscar.getText().isBlank() ? mensajeVacio : "Sin coincidencias. Borra la búsqueda para ver todos los registros.");
    }
    public void recargar() {
        if (modelo == null) return;
        T elegido = tabla.getSelectedRow() < 0 ? null : seleccionado();
        filas = consulta.get(); modelo.setRowCount(0);
        for (T fila : filas) modelo.addRow(columnas.apply(fila));
        int indice = elegido == null ? -1 : filas.indexOf(elegido);
        int visible = indice < 0 ? -1 : tabla.convertRowIndexToView(indice);
        if (visible >= 0) tabla.setRowSelectionInterval(visible, visible);
        actualizarContador();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        barra = new javax.swing.JPanel();
        acciones = new javax.swing.JPanel();
        accion0 = new javax.swing.JButton();
        accion1 = new javax.swing.JButton();
        accion2 = new javax.swing.JButton();
        accion3 = new javax.swing.JButton();
        accion4 = new javax.swing.JButton();
        accion5 = new javax.swing.JButton();
        accion6 = new javax.swing.JButton();
        accion7 = new javax.swing.JButton();
        busqueda = new javax.swing.JPanel();
        lblBuscar = new javax.swing.JLabel();
        buscar = new javax.swing.JTextField();
        scrollTabla = new javax.swing.JScrollPane();
        tabla = new javax.swing.JTable();
        contador = new javax.swing.JLabel();
        this.setBackground(new java.awt.Color(255, 255, 255));
        this.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 16, 20));
        this.setLayout(new java.awt.BorderLayout(16, 16));
        barra.setOpaque(false);
        barra.setLayout(new java.awt.BorderLayout(12, 12));
        acciones.setOpaque(false);
        acciones.setLayout(new java.awt.FlowLayout(0, 8, 6));
        accion0.setText("Acción 1");
        accion0.setFont(new java.awt.Font("SansSerif", 0, 14));
        accion0.setBackground(new java.awt.Color(255, 255, 255));
        accion0.setForeground(new java.awt.Color(38, 58, 50));
        accion0.setFocusPainted(false);
        acciones.add(accion0);
        accion1.setText("Acción 2");
        accion1.setFont(new java.awt.Font("SansSerif", 0, 14));
        accion1.setBackground(new java.awt.Color(255, 255, 255));
        accion1.setForeground(new java.awt.Color(38, 58, 50));
        accion1.setFocusPainted(false);
        acciones.add(accion1);
        accion2.setText("Acción 3");
        accion2.setFont(new java.awt.Font("SansSerif", 0, 14));
        accion2.setBackground(new java.awt.Color(255, 255, 255));
        accion2.setForeground(new java.awt.Color(38, 58, 50));
        accion2.setFocusPainted(false);
        acciones.add(accion2);
        accion3.setText("Acción 4");
        accion3.setFont(new java.awt.Font("SansSerif", 0, 14));
        accion3.setBackground(new java.awt.Color(255, 255, 255));
        accion3.setForeground(new java.awt.Color(38, 58, 50));
        accion3.setFocusPainted(false);
        acciones.add(accion3);
        accion4.setText("Acción 5");
        accion4.setFont(new java.awt.Font("SansSerif", 0, 14));
        accion4.setBackground(new java.awt.Color(255, 255, 255));
        accion4.setForeground(new java.awt.Color(38, 58, 50));
        accion4.setFocusPainted(false);
        acciones.add(accion4);
        accion5.setText("Acción 6");
        accion5.setFont(new java.awt.Font("SansSerif", 0, 14));
        accion5.setBackground(new java.awt.Color(255, 255, 255));
        accion5.setForeground(new java.awt.Color(38, 58, 50));
        accion5.setFocusPainted(false);
        acciones.add(accion5);
        accion6.setText("Acción 7");
        accion6.setFont(new java.awt.Font("SansSerif", 0, 14));
        accion6.setBackground(new java.awt.Color(255, 255, 255));
        accion6.setForeground(new java.awt.Color(38, 58, 50));
        accion6.setFocusPainted(false);
        acciones.add(accion6);
        accion7.setText("Acción 8");
        accion7.setFont(new java.awt.Font("SansSerif", 0, 14));
        accion7.setBackground(new java.awt.Color(255, 255, 255));
        accion7.setForeground(new java.awt.Color(38, 58, 50));
        accion7.setFocusPainted(false);
        acciones.add(accion7);
        barra.add(acciones, java.awt.BorderLayout.NORTH);
        busqueda.setOpaque(false);
        busqueda.setLayout(new java.awt.BorderLayout(12, 12));
        lblBuscar.setText("BUSCAR EN ESTA LISTA");
        lblBuscar.setFont(new java.awt.Font("SansSerif", 1, 11));
        lblBuscar.setForeground(new java.awt.Color(109, 121, 114));
        busqueda.add(lblBuscar, java.awt.BorderLayout.WEST);
        buscar.setFont(new java.awt.Font("SansSerif", 0, 14));
        buscar.setPreferredSize(new java.awt.Dimension(260, 36));
        buscar.setToolTipText("Filtrar por cualquier columna");
        busqueda.add(buscar, java.awt.BorderLayout.CENTER);
        barra.add(busqueda, java.awt.BorderLayout.SOUTH);
        this.add(barra, java.awt.BorderLayout.NORTH);
        scrollTabla.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        tabla.setRowHeight(38);
        tabla.setFillsViewportHeight(true);
        tabla.setShowHorizontalLines(true);
        tabla.setShowVerticalLines(false);
        tabla.setBackground(new java.awt.Color(255, 255, 255));
        tabla.setForeground(new java.awt.Color(36, 59, 50));
        tabla.setSelectionBackground(new java.awt.Color(219, 232, 222));
        tabla.setSelectionForeground(new java.awt.Color(36, 59, 50));
        tabla.setFont(new java.awt.Font("SansSerif", 0, 14));
        scrollTabla.setViewportView(tabla);
        this.add(scrollTabla, java.awt.BorderLayout.CENTER);
        contador.setText("0 registros");
        contador.setFont(new java.awt.Font("SansSerif", 0, 12));
        contador.setForeground(new java.awt.Color(109, 121, 114));
        this.add(contador, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel barra;
    private javax.swing.JPanel acciones;
    private javax.swing.JButton accion0;
    private javax.swing.JButton accion1;
    private javax.swing.JButton accion2;
    private javax.swing.JButton accion3;
    private javax.swing.JButton accion4;
    private javax.swing.JButton accion5;
    private javax.swing.JButton accion6;
    private javax.swing.JButton accion7;
    private javax.swing.JPanel busqueda;
    private javax.swing.JLabel lblBuscar;
    private javax.swing.JTextField buscar;
    private javax.swing.JScrollPane scrollTabla;
    private javax.swing.JTable tabla;
    private javax.swing.JLabel contador;
    // End of variables declaration//GEN-END:variables
}
