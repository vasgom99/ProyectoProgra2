package com.garagecoffee.view;


// Diseño editable en NetBeans: abrir este archivo y elegir Diseño.
public class CompraForm extends Formulario {

    public CompraForm() {
        initComponents();
        registrar("Proveedor activo", cmbProveedorActivo);
        registrar("Ingredientes", tablaIngredientes);
        registrar("Resumen", lblResumen);
        registrar("Recibida", chkRecibida);
        registrar("Pago de caja", chkPagoCaja);
        ingredientes(java.util.List.of());
    }

    // La tabla pertenece a este formulario, no a una pestaña de la ventana principal.
    public PanelListado<com.garagecoffee.model.DetalleCompra> ingredientes(
            java.util.List<com.garagecoffee.model.DetalleCompra> detalles) {
        tablaIngredientes.configurar(new String[]{"Ingrediente", "Cantidad", "Costo unitario Q", "Subtotal Q"},
                () -> java.util.List.copyOf(detalles), d -> new Object[]{Ui.nombre(d.getIngrediente()),
                    d.getCantidad(), d.getCostoUnitario(), d.getSubtotal()});
        tablaIngredientes.mensajeVacio("Agrega ingredientes con el botón «Agregar ingrediente».");
        tablaIngredientes.recargar();
        return tablaIngredientes;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        etiquetaProveedorActivo = new javax.swing.JLabel();
        cmbProveedorActivo = new javax.swing.JComboBox<>();
        etiquetaIngredientes = new javax.swing.JLabel();
        tablaIngredientes = new com.garagecoffee.view.PanelListado<>();
        etiquetaResumen = new javax.swing.JLabel();
        lblResumen = new javax.swing.JLabel();
        etiquetaRecepcion = new javax.swing.JLabel();
        chkRecibida = new javax.swing.JCheckBox();
        etiquetaRecepcion.setText("Recepción");
        chkRecibida.setText("Mercadería recibida: sumar al inventario");
        chkRecibida.setOpaque(false);
        etiquetaPagoCaja = new javax.swing.JLabel();
        chkPagoCaja = new javax.swing.JCheckBox();
        etiquetaPagoCaja.setText("Pago");
        chkPagoCaja.setText("Pagué esta compra con efectivo de caja");
        chkPagoCaja.setOpaque(false);
        this.setBackground(new java.awt.Color(255, 255, 255));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(20).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaProveedorActivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(etiquetaIngredientes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(etiquetaResumen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(etiquetaRecepcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(etiquetaPagoCaja, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(18).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(cmbProveedorActivo, javax.swing.GroupLayout.DEFAULT_SIZE, 610, Short.MAX_VALUE).addComponent(tablaIngredientes, javax.swing.GroupLayout.DEFAULT_SIZE, 610, Short.MAX_VALUE).addComponent(lblResumen, javax.swing.GroupLayout.DEFAULT_SIZE, 610, Short.MAX_VALUE).addComponent(chkRecibida, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE).addComponent(chkPagoCaja, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)).addGap(20)));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(20).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaProveedorActivo, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(cmbProveedorActivo, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(14).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaIngredientes, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(tablaIngredientes, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(14).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaResumen, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(lblResumen, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(14).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaRecepcion, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(chkRecibida, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(14).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaPagoCaja, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(chkPagoCaja, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(14).addGap(6, 6, Short.MAX_VALUE)));
        etiquetaProveedorActivo.setText("Proveedor activo");
        etiquetaProveedorActivo.setFont(new java.awt.Font("SansSerif", 1, 13));
        etiquetaProveedorActivo.setForeground(new java.awt.Color(36, 59, 50));
        cmbProveedorActivo.setFont(new java.awt.Font("SansSerif", 0, 14));
        etiquetaIngredientes.setText("Ingredientes");
        etiquetaIngredientes.setFont(new java.awt.Font("SansSerif", 1, 13));
        etiquetaIngredientes.setForeground(new java.awt.Color(36, 59, 50));
        tablaIngredientes.setFont(new java.awt.Font("SansSerif", 0, 14));
        etiquetaResumen.setText("Resumen");
        etiquetaResumen.setFont(new java.awt.Font("SansSerif", 1, 13));
        etiquetaResumen.setForeground(new java.awt.Color(36, 59, 50));
        lblResumen.setFont(new java.awt.Font("SansSerif", 0, 14));
        lblResumen.setText("Información");
        lblResumen.setForeground(new java.awt.Color(109, 121, 114));
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel etiquetaProveedorActivo;
    private javax.swing.JComboBox<Object> cmbProveedorActivo;
    private javax.swing.JLabel etiquetaIngredientes;
    private com.garagecoffee.view.PanelListado<com.garagecoffee.model.DetalleCompra> tablaIngredientes;
    private javax.swing.JLabel etiquetaResumen;
    private javax.swing.JLabel lblResumen;
    private javax.swing.JLabel etiquetaRecepcion;
    private javax.swing.JCheckBox chkRecibida;
    private javax.swing.JLabel etiquetaPagoCaja;
    private javax.swing.JCheckBox chkPagoCaja;
    // End of variables declaration//GEN-END:variables
}
