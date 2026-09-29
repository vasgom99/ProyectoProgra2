package com.garagecoffee.view;

import com.garagecoffee.controller.RestauranteController;
import com.garagecoffee.model.*;
import com.garagecoffee.service.CatalogoService;
import java.util.List;
// Diseño editable en NetBeans: abrir este archivo y elegir Diseño.
public class CatalogoPanel extends PanelModulo {

    public CatalogoPanel() { initComponents(); }

    private RestauranteController control;
    private CatalogoService servicio;
    public CatalogoPanel(RestauranteController control) {
        this();
        this.control = control; servicio = control.catalogo();
        boolean admin = control.sesion().permite();
        productos.configurar(new String[]{"ID", "Producto", "Categoría", "Precio Q", "Vigente Q", "Disponible"},
                servicio::productos, p -> new Object[]{p.getId(), p.getNombre(), Ui.nombre(p.getcategoria()), p.getPrecio(), servicio.precioVigente(p), p.isDisponible()});
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
        menus.configurar(new String[]{"ID", "Menú", "Activo", "Desde", "Hasta"},
                servicio::menus, m -> new Object[]{m.getId(), m.getNombre(), m.isActivo(), m.getFechaInicio(), m.getFechaFin()});
        if (admin) {
            menus.boton("Nuevo", () -> menu(null)); menus.accion("Editar", this::menu);
            menus.accion("Eliminar", m -> { if (Ui.confirmar(this, "¿Eliminar el menú y sus precios especiales?")) { servicio.eliminarMenu(m); recargar(); } });
        }
        registrar(menus);
        precios.configurar(new String[]{"Menú", "Producto", "Precio especial Q"},
                servicio::detallesMenu, d -> new Object[]{Ui.nombre(d.getMenu()), Ui.nombre(d.getProducto()), d.getPrecioEspecial()});
        if (admin) {
            precios.boton("Agregar", () -> precio(null)); precios.accion("Editar", this::precio);
            precios.accion("Quitar", d -> { if (Ui.confirmar(this, "¿Quitar el producto de este menú?")) servicio.eliminarDetalleMenu(d); });
        }
        registrar(precios);
        platos.configurar(new String[]{"Fecha", "Producto", "Precio especial Q", "Nota"},
                servicio::platos, p -> new Object[]{p.getFecha(), Ui.nombre(p.getProducto()), p.getPrecioEspecial(), p.getNotaEspecial()});
        if (admin) {
            platos.boton("Nuevo", () -> plato(null)); platos.accion("Editar", this::plato);
            platos.accion("Eliminar", p -> { if (Ui.confirmar(this, "¿Eliminar la oferta?")) servicio.eliminarPlato(p); });
        }
        registrar(platos);
        recetas.configurar(new String[]{"Producto", "Ingrediente / unidad", "Cantidad por producto"},
                servicio::recetas, r -> new Object[]{Ui.nombre(r.getProducto()), Ui.nombre(r.getIngrediente()), r.getCantidadRequerida()});
        if (admin) {
            recetas.boton("Agregar ingrediente", () -> receta(null)); recetas.accion("Editar", this::receta);
            recetas.accion("Quitar", r -> { if (Ui.confirmar(this, "¿Quitar el ingrediente de la receta?")) servicio.eliminarReceta(r); });
        }
        registrar(recetas);
        
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
    private void menu(Menu m) {
        Formulario f = new MenuForm();
        var nombre = f.texto("Nombre", m == null ? "" : m.getNombre());
        var activo = f.check("Activo", m == null || m.isActivo());
        var inicio = f.texto("Desde AAAA-MM-DD (opcional)", m == null ? null : m.getFechaInicio());
        var fin = f.texto("Hasta AAAA-MM-DD (opcional)", m == null ? null : m.getFechaFin());
        f.guardar(this, "Menú", () -> { servicio.guardarMenu(m == null ? 0 : m.getId(), f.valor(nombre), activo.isSelected(), f.fecha(inicio), f.fecha(fin)); recargar(); });
    }
    private void precio(DetalleMenu d) {
        Formulario f = new PrecioMenuForm();
        var menu = f.combo("Menú", servicio.menus(), d == null ? null : d.getMenu());
        var producto = f.combo("Producto", servicio.productos(), d == null ? null : d.getProducto());
        var precio = f.texto("Precio especial Q", d == null ? "" : d.getPrecioEspecial());
        f.guardar(this, "Precio de menú", () -> { servicio.guardarDetalleMenu(d == null ? 0 : d.getId(), f.elegido(menu), f.elegido(producto), f.decimal(precio)); recargar(); });
    }
    private void plato(PlatoDelDia p) {
        Formulario f = new PlatoDiaForm();
        var producto = f.combo("Producto", servicio.productos(), p == null ? null : p.getProducto());
        var fecha = f.texto("Fecha AAAA-MM-DD", p == null ? java.time.LocalDate.now() : p.getFecha());
        var precio = f.texto("Precio especial Q", p == null ? "" : p.getPrecioEspecial());
        var nota = f.texto("Nota", p == null ? "" : p.getNotaEspecial());
        f.guardar(this, "Plato del día", () -> { servicio.guardarPlato(p == null ? 0 : p.getId(), f.elegido(producto), f.fecha(fecha), f.valor(nota), f.decimal(precio)); recargar(); });
    }
    private void receta(DetalleReceta r) {
        Formulario f = new RecetaForm();
        var producto = f.combo("Producto", servicio.productos(), r == null ? null : r.getProducto());
        var ingrediente = f.combo("Ingrediente", control.inventario().ingredientes(), r == null ? null : r.getIngrediente());
        var cantidad = f.texto("Cantidad por producto", r == null ? "" : r.getCantidadRequerida());
        f.guardar(this, "Ingrediente de receta", () -> servicio.guardarReceta(r == null ? 0 : r.getId(), f.elegido(producto), f.elegido(ingrediente), f.decimal(cantidad)));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        pestanas = new javax.swing.JTabbedPane();
        productos = new com.garagecoffee.view.PanelListado<>();
        categorias = new com.garagecoffee.view.PanelListado<>();
        menus = new com.garagecoffee.view.PanelListado<>();
        precios = new com.garagecoffee.view.PanelListado<>();
        platos = new com.garagecoffee.view.PanelListado<>();
        recetas = new com.garagecoffee.view.PanelListado<>();
        lblAyuda = new javax.swing.JLabel();
        this.setBackground(new java.awt.Color(246, 244, 238));
        this.setLayout(new java.awt.BorderLayout(12, 12));
        pestanas.setFont(new java.awt.Font("SansSerif", 0, 14));
        pestanas.setBackground(new java.awt.Color(246, 244, 238));
        pestanas.addTab("Productos", productos);
        pestanas.addTab("Categorías", categorias);
        pestanas.addTab("Menús", menus);
        pestanas.addTab("Precios de menú", precios);
        pestanas.addTab("Plato del día", platos);
        pestanas.addTab("Recetas", recetas);
        this.add(pestanas, java.awt.BorderLayout.CENTER);
        lblAyuda.setText("Los pedidos usan el menor precio vigente. Cada receta indica el consumo de una unidad del producto.");
        lblAyuda.setFont(new java.awt.Font("SansSerif", 0, 12));
        lblAyuda.setForeground(new java.awt.Color(109, 121, 114));
        this.add(lblAyuda, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane pestanas;
    private com.garagecoffee.view.PanelListado<Producto> productos;
    private com.garagecoffee.view.PanelListado<Categoria> categorias;
    private com.garagecoffee.view.PanelListado<Menu> menus;
    private com.garagecoffee.view.PanelListado<DetalleMenu> precios;
    private com.garagecoffee.view.PanelListado<PlatoDelDia> platos;
    private com.garagecoffee.view.PanelListado<DetalleReceta> recetas;
    private javax.swing.JLabel lblAyuda;
    // End of variables declaration//GEN-END:variables
}
