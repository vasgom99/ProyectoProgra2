package com.garagecoffee.view;

import com.garagecoffee.controller.RestauranteController;
import com.garagecoffee.model.*;
import com.garagecoffee.service.CatalogoService;
import java.util.List;
// Diseño editable en NetBeans: abrir este archivo y elegir Diseño.
public class CatalogoPanel extends PanelModulo {

    public CatalogoPanel() { initComponents(); }

    private CatalogoService servicio;
    public CatalogoPanel(RestauranteController control) {
        this();
        servicio = control.catalogo();
        boolean admin = control.sesion().permite();
        productos.configurar(new String[]{"ID", "Producto", "Categoría", "Precio Q", "Disponible"},
                servicio::productos, p -> new Object[]{p.getId(), p.getNombre(), Ui.nombre(p.getcategoria()), p.getPrecio(), p.isDisponible()});
        if (admin) {
            productos.boton("Nuevo", () -> producto(null)); productos.accion("Editar", this::producto);
            productos.accion("Eliminar", p -> { if (Ui.confirmar(this, "¿Eliminar " + p.getNombre() + "?")) servicio.eliminarProducto(p); });
        }
        registrar(productos);
        categorias.configurar(new String[]{"ID", "Categoría", "Tipo", "Activa"},
                servicio::categorias, c -> new Object[]{c.getId(), c.getNombre(), Ui.nombre(c.getTipo()), c.isActivo()});
        if (admin) {
            categorias.boton("Nueva", () -> categoria(null)); categorias.accion("Editar", this::categoria);
            categorias.accion("Eliminar", c -> { if (Ui.confirmar(this, "¿Eliminar la categoría?")) servicio.eliminarCategoria(c); });
        }
        registrar(categorias);
    }
    private void producto(Producto p) {
        Formulario f = new ProductoForm();
        var nombre = f.texto("Nombre", p == null ? "" : p.getNombre());
        var descripcion = f.texto("Descripción", p == null ? "" : p.getDescripcion());
        var imagen = f.texto("Referencia de imagen (opcional)", p == null ? "" : p.getImagenUrl());
        var precio = f.texto("Precio Q", p == null ? "" : p.getPrecio());
        var categoria = f.combo("Categoría", servicio.categorias(), p == null ? null : p.getcategoria());
        var activo = f.check("Disponible", p == null || p.isDisponible());
        f.guardar(this, "Producto", () -> { servicio.guardarProducto(p == null ? 0 : p.getId(), f.valor(nombre), f.valor(descripcion),
                f.valor(imagen), f.decimal(precio), f.elegido(categoria), activo.isSelected()); recargar(); });
    }
    private void categoria(Categoria c) {
        Formulario f = new CategoriaForm();
        var nombre = f.texto("Nombre", c == null ? "" : c.getNombre());
        var tipo = f.combo("Tipo", List.of(TipoCategoria.values()), c == null ? TipoCategoria.COMIDA : c.getTipo());
        var activo = f.check("Activa", c == null || c.isActivo());
        f.guardar(this, "Categoría", () -> { servicio.guardarCategoria(c == null ? 0 : c.getId(), f.valor(nombre), f.elegido(tipo), activo.isSelected()); recargar(); });
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        pestanas = new javax.swing.JTabbedPane();
        productos = new com.garagecoffee.view.PanelListado<>();
        categorias = new com.garagecoffee.view.PanelListado<>();
        lblAyuda = new javax.swing.JLabel();
        this.setBackground(new java.awt.Color(246, 244, 238));
        this.setLayout(new java.awt.BorderLayout(12, 12));
        pestanas.setFont(new java.awt.Font("SansSerif", 0, 14));
        pestanas.setBackground(new java.awt.Color(246, 244, 238));
        pestanas.addTab("Productos", productos);
        pestanas.addTab("Categorías", categorias);
        this.add(pestanas, java.awt.BorderLayout.CENTER);
        lblAyuda.setText("Crea una categoría y después sus productos para tomar pedidos.");
        lblAyuda.setFont(new java.awt.Font("SansSerif", 0, 12));
        lblAyuda.setForeground(new java.awt.Color(109, 121, 114));
        this.add(lblAyuda, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane pestanas;
    private com.garagecoffee.view.PanelListado<Producto> productos;
    private com.garagecoffee.view.PanelListado<Categoria> categorias;
    private javax.swing.JLabel lblAyuda;
    // End of variables declaration//GEN-END:variables
}
