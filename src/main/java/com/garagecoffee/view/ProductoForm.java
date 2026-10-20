package com.garagecoffee.view;


// Diseño editable en NetBeans: abrir este archivo y elegir Diseño.
public class ProductoForm extends Formulario {

    public ProductoForm() {
        initComponents();
        registrar("Nombre", txtNombre);
        registrar("Descripción", txtDescripcion);
        registrar("Referencia de imagen (opcional)", txtReferenciaDeImagenOpcional);
        registrar("Precio Q", txtPrecioQ);
        registrar("Categoría", cmbCategoria);
        registrar("Disponible", chkDisponible);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        etiquetaNombre = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        etiquetaDescripcion = new javax.swing.JLabel();
        txtDescripcion = new javax.swing.JTextField();
        etiquetaReferenciaDeImagenOpcional = new javax.swing.JLabel();
        txtReferenciaDeImagenOpcional = new javax.swing.JTextField();
        etiquetaPrecioQ = new javax.swing.JLabel();
        txtPrecioQ = new javax.swing.JTextField();
        etiquetaCategoria = new javax.swing.JLabel();
        cmbCategoria = new javax.swing.JComboBox<>();
        etiquetaDisponible = new javax.swing.JLabel();
        chkDisponible = new javax.swing.JCheckBox();
        this.setBackground(new java.awt.Color(255, 255, 255));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(20).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(etiquetaDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(etiquetaReferenciaDeImagenOpcional, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(etiquetaPrecioQ, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(etiquetaCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(etiquetaDisponible, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(18).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(txtNombre, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE).addComponent(txtDescripcion, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE).addComponent(txtReferenciaDeImagenOpcional, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE).addComponent(txtPrecioQ, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE).addComponent(cmbCategoria, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE).addComponent(chkDisponible, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)).addGap(20)));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(20).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(14).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(txtDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(14).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaReferenciaDeImagenOpcional, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(txtReferenciaDeImagenOpcional, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(14).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaPrecioQ, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(txtPrecioQ, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(14).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(cmbCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(14).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(etiquetaDisponible, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(chkDisponible, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(14).addGap(6, 6, Short.MAX_VALUE)));
        etiquetaNombre.setText("Nombre");
        etiquetaNombre.setFont(new java.awt.Font("SansSerif", 1, 13));
        etiquetaNombre.setForeground(new java.awt.Color(36, 59, 50));
        txtNombre.setFont(new java.awt.Font("SansSerif", 0, 14));
        txtNombre.setColumns(22);
        etiquetaDescripcion.setText("Descripción");
        etiquetaDescripcion.setFont(new java.awt.Font("SansSerif", 1, 13));
        etiquetaDescripcion.setForeground(new java.awt.Color(36, 59, 50));
        txtDescripcion.setFont(new java.awt.Font("SansSerif", 0, 14));
        txtDescripcion.setColumns(22);
        etiquetaReferenciaDeImagenOpcional.setText("Referencia de imagen (opcional)");
        etiquetaReferenciaDeImagenOpcional.setFont(new java.awt.Font("SansSerif", 1, 13));
        etiquetaReferenciaDeImagenOpcional.setForeground(new java.awt.Color(36, 59, 50));
        txtReferenciaDeImagenOpcional.setFont(new java.awt.Font("SansSerif", 0, 14));
        txtReferenciaDeImagenOpcional.setColumns(22);
        etiquetaPrecioQ.setText("Precio Q");
        etiquetaPrecioQ.setFont(new java.awt.Font("SansSerif", 1, 13));
        etiquetaPrecioQ.setForeground(new java.awt.Color(36, 59, 50));
        txtPrecioQ.setFont(new java.awt.Font("SansSerif", 0, 14));
        txtPrecioQ.setColumns(22);
        etiquetaCategoria.setText("Categoría");
        etiquetaCategoria.setFont(new java.awt.Font("SansSerif", 1, 13));
        etiquetaCategoria.setForeground(new java.awt.Color(36, 59, 50));
        cmbCategoria.setFont(new java.awt.Font("SansSerif", 0, 14));
        etiquetaDisponible.setText("Disponible");
        etiquetaDisponible.setFont(new java.awt.Font("SansSerif", 1, 13));
        etiquetaDisponible.setForeground(new java.awt.Color(36, 59, 50));
        chkDisponible.setFont(new java.awt.Font("SansSerif", 0, 14));
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel etiquetaNombre;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JLabel etiquetaDescripcion;
    private javax.swing.JTextField txtDescripcion;
    private javax.swing.JLabel etiquetaReferenciaDeImagenOpcional;
    private javax.swing.JTextField txtReferenciaDeImagenOpcional;
    private javax.swing.JLabel etiquetaPrecioQ;
    private javax.swing.JTextField txtPrecioQ;
    private javax.swing.JLabel etiquetaCategoria;
    private javax.swing.JComboBox<Object> cmbCategoria;
    private javax.swing.JLabel etiquetaDisponible;
    private javax.swing.JCheckBox chkDisponible;
    // End of variables declaration//GEN-END:variables
}
