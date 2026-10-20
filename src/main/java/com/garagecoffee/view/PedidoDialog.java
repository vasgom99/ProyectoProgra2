package com.garagecoffee.view;

import com.garagecoffee.controller.RestauranteController;
import com.garagecoffee.model.*;
import java.awt.Dimension;
import java.awt.Window;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

// Diseño editable en NetBeans: abrir este archivo y elegir Diseño.
public class PedidoDialog extends javax.swing.JDialog {
    private final List<DetallePedido> detalles = new ArrayList<>();
    private boolean guardado;

    public PedidoDialog() { super((Window) null); initComponents(); }

    public PedidoDialog(Window padre, RestauranteController control, Mesa elegida) {
        super(padre, "Nuevo pedido", ModalityType.APPLICATION_MODAL);
        initComponents();
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        var mesa = f.combo("Mesa libre", control.atencion().mesas().stream()
                .filter(m -> m.getEstado() == EstadoMesa.LIBRE).toList(), elegida);
        var observaciones = f.texto("Observaciones", "");
        carrito.configurar(new String[]{"Producto", "Cantidad", "Precio Q", "Subtotal Q"},
                () -> List.copyOf(detalles),
                d -> new Object[]{d.getProducto().getNombre(), d.getCantidad(), d.getPrecioUnitario(), d.getSubtotal()});
        Runnable actualizarTotal = () -> {
            BigDecimal suma = detalles.stream().map(DetallePedido::getSubtotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            total.setText("Total estimado: " + Ui.dinero(suma));
        };
        var producto = selector.combo("Producto", control.catalogo().productos().stream()
                .filter(p -> p.isDisponible() && p.getcategoria().isActivo()).toList(), null);
        var cantidad = selector.texto("Cantidad", "1");
        Runnable agregar = () -> {
            Producto elegido = selector.elegido(producto);
            if (elegido == null) throw new IllegalArgumentException("No hay productos disponibles.");
            int unidades = selector.entero(cantidad);
            if (unidades <= 0 || unidades > 9999)
                throw new IllegalArgumentException("La cantidad debe estar entre 1 y 9999.");
            DetallePedido existente = detalles.stream().filter(d -> d.getProducto() == elegido)
                    .findFirst().orElse(null);
            if (existente == null)
                detalles.add(new DetallePedido(0, unidades, elegido.getPrecio(), elegido));
            else {
                if (existente.getCantidad() + unidades > 9999)
                    throw new IllegalArgumentException("La cantidad acumulada no puede superar 9999.");
                existente.setCantidad(existente.getCantidad() + unidades);
            }
            cantidad.setText("1");
            carrito.recargar();
            actualizarTotal.run();
        };
        carrito.boton("Agregar producto", agregar);
        cantidad.addActionListener(e -> Ui.ejecutar(this, agregar));
        carrito.accion("Cantidad", d -> {
            Formulario formulario = new CantidadForm();
            var nueva = formulario.texto("Cantidad", d.getCantidad());
            formulario.guardar(this, "Cambiar cantidad", () -> {
                int unidades = formulario.entero(nueva);
                if (unidades <= 0 || unidades > 9999)
                    throw new IllegalArgumentException("La cantidad debe estar entre 1 y 9999.");
                d.setCantidad(unidades);
                actualizarTotal.run();
            });
        });
        carrito.accion("Quitar", d -> { detalles.remove(d); actualizarTotal.run(); });
        carrito.recargar();
        actualizarTotal.run();
        Ui.primario(btnConfirmar);
        Ui.secundario(btnCancelar);
        btnConfirmar.setText("Enviar a cocina");
        aviso.setText(control.esDemo() ? "Demostración · Datos temporales" : "El pedido se guarda en MySQL.");
        btnCancelar.addActionListener(e -> cancelar());
        btnConfirmar.addActionListener(e -> Ui.ejecutar(this, () -> {
            control.pedidos().guardar(f.elegido(mesa), f.valor(observaciones), detalles);
            guardado = true;
            dispose();
        }));
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override public void windowClosing(java.awt.event.WindowEvent e) { cancelar(); }
        });
        setSize(1100, 660);
        setMinimumSize(new Dimension(900, 570));
        setLocationRelativeTo(padre);
    }

    private void cancelar() {
        if (guardado || Ui.confirmar(this, "¿Cerrar sin guardar el pedido?")) dispose();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        cabecera = new javax.swing.JPanel();
        titulo = new javax.swing.JLabel();
        ayuda = new javax.swing.JLabel();
        centro = new javax.swing.JPanel();
        datos = new javax.swing.JScrollPane();
        f = new com.garagecoffee.view.PedidoDatosForm();
        carrito = new com.garagecoffee.view.PanelListado<>();
        productosPanel = new javax.swing.JPanel();
        selector = new com.garagecoffee.view.LineaPedidoForm();
        pie = new javax.swing.JPanel();
        totales = new javax.swing.JPanel();
        total = new javax.swing.JLabel();
        aviso = new javax.swing.JLabel();
        acciones = new javax.swing.JPanel();
        btnCancelar = new javax.swing.JButton();
        btnConfirmar = new javax.swing.JButton();
        this.setBackground(new java.awt.Color(246, 244, 238));
        getContentPane().setLayout(new java.awt.BorderLayout(16, 16));
        cabecera.setBackground(new java.awt.Color(36, 59, 50));
        cabecera.setBorder(javax.swing.BorderFactory.createEmptyBorder(22, 28, 22, 28));
        cabecera.setLayout(new java.awt.BorderLayout(8, 8));
        titulo.setText("Un nuevo pedido, un buen comienzo.");
        titulo.setFont(new java.awt.Font("SansSerif", 1, 23));
        titulo.setForeground(new java.awt.Color(255, 255, 255));
        cabecera.add(titulo, java.awt.BorderLayout.NORTH);
        ayuda.setText("Elige una mesa, agrega productos y confirma la orden.");
        ayuda.setFont(new java.awt.Font("SansSerif", 0, 13));
        ayuda.setForeground(new java.awt.Color(199, 210, 201));
        cabecera.add(ayuda, java.awt.BorderLayout.CENTER);
        getContentPane().add(cabecera, java.awt.BorderLayout.NORTH);
        centro.setBackground(new java.awt.Color(246, 244, 238));
        centro.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 20, 0, 20));
        centro.setLayout(new java.awt.BorderLayout(18, 18));
        datos.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        datos.setPreferredSize(new java.awt.Dimension(420, 360));
        datos.setViewportView(f);
        centro.add(datos, java.awt.BorderLayout.WEST);
        productosPanel.setLayout(new java.awt.BorderLayout(0, 8));
        productosPanel.add(selector, java.awt.BorderLayout.NORTH);
        productosPanel.add(carrito, java.awt.BorderLayout.CENTER);
        centro.add(productosPanel, java.awt.BorderLayout.CENTER);
        getContentPane().add(centro, java.awt.BorderLayout.CENTER);
        pie.setBackground(new java.awt.Color(255, 255, 255));
        pie.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 24, 16, 24));
        pie.setLayout(new java.awt.BorderLayout(16, 16));
        totales.setOpaque(false);
        totales.setLayout(new java.awt.GridLayout(2, 1, 5, 5));
        total.setText("Total estimado: Q 0.00");
        total.setFont(new java.awt.Font("SansSerif", 1, 19));
        total.setForeground(new java.awt.Color(36, 59, 50));
        totales.add(total);
        aviso.setText("El pedido se guarda en MySQL");
        aviso.setFont(new java.awt.Font("SansSerif", 0, 11));
        aviso.setForeground(new java.awt.Color(109, 121, 114));
        totales.add(aviso);
        pie.add(totales, java.awt.BorderLayout.CENTER);
        acciones.setOpaque(false);
        acciones.setLayout(new java.awt.FlowLayout(2, 12, 6));
        btnCancelar.setText("Cancelar");
        btnCancelar.setFont(new java.awt.Font("SansSerif", 0, 14));
        btnCancelar.setBackground(new java.awt.Color(255, 255, 255));
        btnCancelar.setForeground(new java.awt.Color(38, 58, 50));
        btnCancelar.setFocusPainted(false);
        acciones.add(btnCancelar);
        btnConfirmar.setText("Confirmar pedido");
        btnConfirmar.setFont(new java.awt.Font("SansSerif", 0, 14));
        btnConfirmar.setBackground(new java.awt.Color(255, 255, 255));
        btnConfirmar.setForeground(new java.awt.Color(38, 58, 50));
        btnConfirmar.setFocusPainted(false);
        acciones.add(btnConfirmar);
        pie.add(acciones, java.awt.BorderLayout.EAST);
        getContentPane().add(pie, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel cabecera;
    private javax.swing.JLabel titulo;
    private javax.swing.JLabel ayuda;
    private javax.swing.JPanel centro;
    private javax.swing.JScrollPane datos;
    private com.garagecoffee.view.PedidoDatosForm f;
    private com.garagecoffee.view.PanelListado<DetallePedido> carrito;
    private javax.swing.JPanel productosPanel;
    private com.garagecoffee.view.LineaPedidoForm selector;
    private javax.swing.JPanel pie;
    private javax.swing.JPanel totales;
    private javax.swing.JLabel total;
    private javax.swing.JLabel aviso;
    private javax.swing.JPanel acciones;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnConfirmar;
    // End of variables declaration//GEN-END:variables
}
