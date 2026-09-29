package com.garagecoffee.service;

import com.garagecoffee.model.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

// Valida y administra productos, categorías, menús y recetas.
public class CatalogoService {
    private final DatosRestaurante datos;
    private final SesionService sesion;

    public CatalogoService(DatosRestaurante datos, SesionService sesion) { this.datos = datos; this.sesion = sesion; }
    public List<Categoria> categorias() { sesion.actual(); return List.copyOf(datos.categorias); }
    public List<Producto> productos() { sesion.actual(); return List.copyOf(datos.productos); }
    public List<Menu> menus() { sesion.actual(); return List.copyOf(datos.menus); }
    public List<DetalleMenu> detallesMenu() { sesion.actual(); return List.copyOf(datos.detallesMenu); }
    public List<PlatoDelDia> platos() { sesion.actual(); return List.copyOf(datos.platos); }
    public List<DetalleReceta> recetas() { sesion.actual(); return List.copyOf(datos.recetas); }

    public Categoria guardarCategoria(int id, String nombre, TipoCategoria tipo, boolean activo) {
        return datos.transaccion(() -> guardarCategoriaInterno(id, nombre, tipo, activo));
    }
    private Categoria guardarCategoriaInterno(int id, String nombre, TipoCategoria tipo, boolean activo) {
        sesion.exigir();
        nombre = Validacion.texto(nombre, "Nombre", 100);
        Validacion.exigir(tipo != null, "Selecciona un tipo.");
        for (Categoria c : datos.categorias) Validacion.exigir(c.getId() == id || !c.getNombre().equalsIgnoreCase(nombre), "La categoría ya existe.");
        Categoria categoria = id == 0 ? new Categoria() : datos.categorias.stream().filter(c -> c.getId() == id).findFirst().orElseThrow();
        if (id == 0) categoria.setId(datos.siguienteId(Categoria.class));
        categoria.setNombre(nombre); categoria.setTipo(tipo); categoria.setActivo(activo);
        datos.guardar(categoria);
        if (id == 0) datos.categorias.add(categoria);
        return categoria;
    }

    public void eliminarCategoria(Categoria categoria) {
        datos.transaccion(() -> { eliminarCategoriaInterno(categoria); return null; });
    }
    private void eliminarCategoriaInterno(Categoria categoria) {
        sesion.exigir();
        Validacion.exigir(datos.productos.stream().noneMatch(p -> p.getcategoria() == categoria), "La categoría tiene productos; puedes desactivarla.");
        datos.eliminar(categoria); datos.categorias.remove(categoria);
    }

    public Producto guardarProducto(int id, String nombre, String descripcion, String imagen, BigDecimal precio,
                                    Categoria categoria, boolean disponible) {
        return datos.transaccion(() -> guardarProductoInterno(id, nombre, descripcion, imagen, precio, categoria, disponible));
    }
    private Producto guardarProductoInterno(int id, String nombre, String descripcion, String imagen, BigDecimal precio,
                                    Categoria categoria, boolean disponible) {
        sesion.exigir();
        nombre = Validacion.texto(nombre, "Nombre", 150);
        descripcion = Validacion.opcional(descripcion, "Descripción", 255);
        imagen = Validacion.opcional(imagen, "Imagen", 255);
        precio = Validacion.dinero(precio, "Precio", true);
        Validacion.exigir(datos.categorias.contains(categoria), "Selecciona una categoría.");
        Validacion.exigir(!disponible || categoria.isActivo(), "Activa la categoría antes de ofrecer el producto.");
        Producto producto = id == 0 ? new Producto() : datos.productos.stream().filter(p -> p.getId() == id).findFirst().orElseThrow();
        if (id == 0) producto.setId(datos.siguienteId(Producto.class));
        producto.setNombre(nombre); producto.setDescripcion(descripcion); producto.setImagenUrl(imagen);
        producto.setPrecio(precio); producto.setCategoria(categoria); producto.setDisponible(disponible);
        datos.guardar(producto);
        if (id == 0) datos.productos.add(producto);
        return producto;
    }

    public void eliminarProducto(Producto producto) {
        datos.transaccion(() -> { eliminarProductoInterno(producto); return null; });
    }
    private void eliminarProductoInterno(Producto producto) {
        sesion.exigir();
        boolean utilizado = datos.pedidos.stream().flatMap(p -> p.getDetalles().stream()).anyMatch(d -> d.getProducto() == producto)
                || datos.recetas.stream().anyMatch(r -> r.getProducto() == producto)
                || datos.detallesMenu.stream().anyMatch(d -> d.getProducto() == producto)
                || datos.platos.stream().anyMatch(p -> p.getProducto() == producto);
        Validacion.exigir(!utilizado, "El producto ya se utiliza; desactívalo para conservar sus relaciones.");
        datos.eliminar(producto); datos.productos.remove(producto);
    }

    public Menu guardarMenu(int id, String nombre, boolean activo, LocalDate inicio, LocalDate fin) {
        return datos.transaccion(() -> guardarMenuInterno(id, nombre, activo, inicio, fin));
    }
    private Menu guardarMenuInterno(int id, String nombre, boolean activo, LocalDate inicio, LocalDate fin) {
        sesion.exigir();
        nombre = Validacion.texto(nombre, "Nombre", 120);
        Validacion.exigir(inicio == null || fin == null || !fin.isBefore(inicio), "La fecha final no puede ser anterior a la inicial.");
        Menu menu = id == 0 ? new Menu() : datos.menus.stream().filter(m -> m.getId() == id).findFirst().orElseThrow();
        if (id == 0) menu.setId(datos.siguienteId(Menu.class));
        menu.setNombre(nombre); menu.setActivo(activo); menu.setFechaInicio(inicio); menu.setFechaFin(fin);
        datos.guardar(menu);
        if (id == 0) datos.menus.add(menu);
        return menu;
    }

    public void eliminarMenu(Menu menu) {
        datos.transaccion(() -> { eliminarMenuInterno(menu); return null; });
    }
    private void eliminarMenuInterno(Menu menu) {
        sesion.exigir();
        for (DetalleMenu d : datos.detallesMenu.stream().filter(d -> d.getMenu() == menu).toList()) datos.eliminar(d);
        datos.detallesMenu.removeIf(d -> d.getMenu() == menu);
        datos.eliminar(menu); datos.menus.remove(menu);
    }

    public void guardarDetalleMenu(int id, Menu menu, Producto producto, BigDecimal precio) {
        datos.transaccion(() -> { guardarDetalleMenuInterno(id, menu, producto, precio); return null; });
    }
    private void guardarDetalleMenuInterno(int id, Menu menu, Producto producto, BigDecimal precio) {
        sesion.exigir();
        Validacion.exigir(datos.menus.contains(menu) && datos.productos.contains(producto), "Selecciona menú y producto.");
        precio = Validacion.dinero(precio, "Precio especial", true);
        Validacion.exigir(datos.detallesMenu.stream().noneMatch(d -> d.getId() != id && d.getMenu() == menu && d.getProducto() == producto),
                "El producto ya está en ese menú.");
        DetalleMenu detalle = id == 0 ? new DetalleMenu() : datos.detallesMenu.stream().filter(d -> d.getId() == id).findFirst().orElseThrow();
        if (id == 0) detalle.setId(datos.siguienteId(DetalleMenu.class));
        detalle.setMenu(menu); detalle.setProducto(producto); detalle.setPrecioEspecial(precio);
        datos.guardar(detalle);
        if (id == 0) datos.detallesMenu.add(detalle);
    }

    public void eliminarDetalleMenu(DetalleMenu detalle) {
        datos.transaccion(() -> { eliminarDetalleMenuInterno(detalle); return null; });
    }
    private void eliminarDetalleMenuInterno(DetalleMenu detalle) { sesion.exigir(); datos.eliminar(detalle); datos.detallesMenu.remove(detalle); }

    public void guardarPlato(int id, Producto producto, LocalDate fecha, String nota, BigDecimal precio) {
        datos.transaccion(() -> { guardarPlatoInterno(id, producto, fecha, nota, precio); return null; });
    }
    private void guardarPlatoInterno(int id, Producto producto, LocalDate fecha, String nota, BigDecimal precio) {
        sesion.exigir();
        Validacion.exigir(datos.productos.contains(producto) && fecha != null, "Selecciona producto y fecha.");
        precio = Validacion.dinero(precio, "Precio especial", true);
        nota = Validacion.opcional(nota, "Nota", 255);
        Validacion.exigir(datos.platos.stream().noneMatch(p -> p.getId() != id && p.getProducto() == producto && p.getFecha().equals(fecha)),
                "Ese producto ya tiene una oferta para esa fecha.");
        PlatoDelDia plato = id == 0 ? new PlatoDelDia() : datos.platos.stream().filter(p -> p.getId() == id).findFirst().orElseThrow();
        if (id == 0) plato.setId(datos.siguienteId(PlatoDelDia.class));
        plato.setProducto(producto); plato.setFecha(fecha); plato.setNotaEspecial(nota); plato.setPrecioEspecial(precio);
        datos.guardar(plato);
        if (id == 0) datos.platos.add(plato);
    }

    public void eliminarPlato(PlatoDelDia plato) {
        datos.transaccion(() -> { eliminarPlatoInterno(plato); return null; });
    }
    private void eliminarPlatoInterno(PlatoDelDia plato) { sesion.exigir(); datos.eliminar(plato); datos.platos.remove(plato); }

    public void guardarReceta(int id, Producto producto, Ingrediente ingrediente, BigDecimal cantidad) {
        datos.transaccion(() -> { guardarRecetaInterno(id, producto, ingrediente, cantidad); return null; });
    }
    private void guardarRecetaInterno(int id, Producto producto, Ingrediente ingrediente, BigDecimal cantidad) {
        sesion.exigir();
        Validacion.exigir(datos.productos.contains(producto) && datos.ingredientes.contains(ingrediente), "Selecciona producto e ingrediente.");
        cantidad = Validacion.numero(cantidad, "Cantidad", 3, true);
        Validacion.exigir(datos.recetas.stream().noneMatch(r -> r.getId() != id && r.getProducto() == producto && r.getIngrediente() == ingrediente),
                "Ese ingrediente ya está en la receta.");
        DetalleReceta receta = id == 0 ? new DetalleReceta() : datos.recetas.stream().filter(r -> r.getId() == id).findFirst().orElseThrow();
        if (id == 0) receta.setId(datos.siguienteId(DetalleReceta.class));
        receta.setProducto(producto); receta.setIngrediente(ingrediente); receta.setCantidadRequerida(cantidad);
        datos.guardar(receta);
        if (id == 0) datos.recetas.add(receta);
    }

    public void eliminarReceta(DetalleReceta receta) {
        datos.transaccion(() -> { eliminarRecetaInterno(receta); return null; });
    }
    private void eliminarRecetaInterno(DetalleReceta receta) { sesion.exigir(); datos.eliminar(receta); datos.recetas.remove(receta); }

    // Se aplica el menor precio vigente, sin acumular descuentos.
    public BigDecimal precioVigente(Producto producto) {
        sesion.actual();
        return calcularPrecio(producto, datos.detallesMenu, datos.platos, LocalDate.now());
    }
    // La misma regla se utiliza en Swing y en las consultas del servidor.
    public static BigDecimal calcularPrecio(Producto producto, List<DetalleMenu> detalles, List<PlatoDelDia> platos, LocalDate hoy) {
        BigDecimal precio = producto.getPrecio();
        for (DetalleMenu detalle : detalles) {
            Menu menu = detalle.getMenu();
            if (detalle.getProducto().getId() == producto.getId() && menu.isActivo()
                    && (menu.getFechaInicio() == null || !hoy.isBefore(menu.getFechaInicio()))
                    && (menu.getFechaFin() == null || !hoy.isAfter(menu.getFechaFin()))) precio = precio.min(detalle.getPrecioEspecial());
        }
        for (PlatoDelDia plato : platos) {
            if (plato.getProducto().getId() == producto.getId() && hoy.equals(plato.getFecha())) precio = precio.min(plato.getPrecioEspecial());
        }
        return precio;
    }
}
