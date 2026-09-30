package com.garagecoffee.view;

import com.garagecoffee.controller.RestauranteController;
import com.garagecoffee.model.*;
import java.util.ArrayList;
import java.util.List;
// Diseño editable en NetBeans: abrir este archivo y elegir Diseño.
public class ComprasPanel extends PanelModulo {

    public ComprasPanel() { initComponents(); }

    private RestauranteController control;
    public ComprasPanel(RestauranteController control) {
        this();
        this.control = control;
        compras.configurar(new String[]{"ID", "Fecha", "Proveedor", "Estado", "Total Q"},
                control.inventario()::compras, c -> new Object[]{c.getId(), Ui.fecha(c.getFecha()), Ui.nombre(c.getProveedor()), Ui.nombre(c.getEstado()), c.getTotal()});
        compras.boton("Nueva compra", this::nueva);
        compras.accion("Recibir", c -> { if (Ui.confirmar(this, "¿Recibir la compra #" + c.getId() + " y aumentar el stock?")) control.inventario().recibirCompra(c); });
        compras.accion("Cancelar", c -> { if (Ui.confirmar(this, "¿Cancelar la compra pendiente?")) control.inventario().cancelarCompra(c); });
        compras.accion("Detalle", c -> {
            StringBuilder texto = new StringBuilder("Compra #" + c.getId() + " · " + c.getEstado() + "\nProveedor: " + Ui.nombre(c.getProveedor()) + "\n\n");
            for (DetalleCompra d : c.getDetalles()) texto.append(Ui.nombre(d.getIngrediente())).append(" · ").append(d.getCantidad())
                    .append(" x Q").append(d.getCostoUnitario()).append(" = ").append(Ui.dinero(d.getSubtotal())).append("\n");
            Ui.mostrarTexto(this, "Compra", texto.append("\nTotal: ").append(Ui.dinero(c.getTotal())).toString());
        });
        registrar(compras);
        lblAyuda.setText("Confirma si la mercadería llegó y si se pagó de caja. El sistema registra los movimientos una sola vez.");
        
    }
    private void nueva() {
        var proveedores = control.inventario().proveedores().stream().filter(Proveedor::isActivo).toList();
        if (proveedores.isEmpty()) throw new IllegalArgumentException("Registra un proveedor activo en Inventario antes de crear una compra.");
        if (control.inventario().ingredientes().isEmpty()) throw new IllegalArgumentException("Registra los ingredientes en Inventario antes de crear una compra.");
        Formulario f = new CompraForm();
        var proveedor = f.combo("Proveedor activo", proveedores, null);
        var recibida = f.check("Recibida", true);
        var pagoCaja = f.check("Pago de caja", false);
        recibida.addActionListener(e -> {
            pagoCaja.setEnabled(recibida.isSelected());
            if (!recibida.isSelected()) pagoCaja.setSelected(false);
        });
        List<DetalleCompra> detalles = new ArrayList<>();
        PanelListado<DetalleCompra> lista = f.listado("Ingredientes");
        lista.configurar(new String[]{"Ingrediente", "Cantidad", "Costo unitario Q", "Subtotal Q"},
                () -> List.copyOf(detalles), d -> new Object[]{Ui.nombre(d.getIngrediente()), d.getCantidad(), d.getCostoUnitario(), d.getSubtotal()});
        javax.swing.JLabel total = f.etiqueta("Resumen", "Total: Q 0.00");
        Runnable actualizar = () -> total.setText("Total: " + Ui.dinero(detalles.stream().map(DetalleCompra::getSubtotal).reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add)));
        lista.boton("Agregar ingrediente", () -> {
            Formulario linea = new LineaCompraForm();
            var ingrediente = linea.combo("Ingrediente", control.inventario().ingredientes(), null);
            var cantidad = linea.texto("Cantidad", "1");
            var costo = linea.texto("Costo por unidad Q", "0");
            ingrediente.addActionListener(e -> { Ingrediente i = linea.elegido(ingrediente); if (i != null) costo.setText(i.getCostoUnitario().toPlainString()); });
            if (linea.elegido(ingrediente) != null) costo.setText(linea.elegido(ingrediente).getCostoUnitario().toPlainString());
            linea.guardar(f, "Ingrediente de compra", () -> {
                if (linea.elegido(ingrediente) == null || linea.decimal(cantidad).signum() <= 0 || linea.decimal(costo).signum() < 0)
                    throw new IllegalArgumentException("Selecciona un ingrediente, cantidad positiva y costo no negativo.");
                try { linea.decimal(cantidad).setScale(3, java.math.RoundingMode.UNNECESSARY); linea.decimal(costo).setScale(4, java.math.RoundingMode.UNNECESSARY); }
                catch (ArithmeticException ex) { throw new IllegalArgumentException("Cantidad: hasta 3 decimales. Costo: hasta 4 decimales."); }
                if (detalles.stream().anyMatch(d -> d.getIngrediente() == linea.elegido(ingrediente)))
                    throw new IllegalArgumentException("El ingrediente ya está agregado. Quítalo antes de cambiar su cantidad.");
                detalles.add(new DetalleCompra(linea.decimal(cantidad), linea.decimal(costo), linea.elegido(ingrediente)));
                actualizar.run();
            });
        });
        lista.accion("Quitar", d -> { detalles.remove(d); actualizar.run(); });
        lista.recargar();
        f.guardar(this, "Registrar compra", () -> control.registrarCompra(f.elegido(proveedor), detalles,
                recibida.isSelected(), pagoCaja.isSelected()));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        pestanas = new javax.swing.JTabbedPane();
        compras = new com.garagecoffee.view.PanelListado<>();
        lblAyuda = new javax.swing.JLabel();
        this.setBackground(new java.awt.Color(246, 244, 238));
        this.setLayout(new java.awt.BorderLayout(12, 12));
        pestanas.setFont(new java.awt.Font("SansSerif", 0, 14));
        pestanas.setBackground(new java.awt.Color(246, 244, 238));
        pestanas.addTab("Compras", compras);
        this.add(pestanas, java.awt.BorderLayout.CENTER);
        lblAyuda.setText("Recibir aumenta stock. Si pagas al proveedor en efectivo, registra un egreso manual en Caja (no otro gasto).");
        lblAyuda.setFont(new java.awt.Font("SansSerif", 0, 12));
        lblAyuda.setForeground(new java.awt.Color(109, 121, 114));
        this.add(lblAyuda, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane pestanas;
    private com.garagecoffee.view.PanelListado<Compra> compras;
    private javax.swing.JLabel lblAyuda;
    // End of variables declaration//GEN-END:variables
}
