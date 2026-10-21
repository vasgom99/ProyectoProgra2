package com.garagecoffee.service;

import com.garagecoffee.model.Categoria;
import com.garagecoffee.model.Producto;
import com.garagecoffee.model.TipoCategoria;
import java.math.BigDecimal;
import java.util.List;

// controla las categorías y productos.
public class CatalogoService {
    private final DatosRestaurante datos;
    private final SesionService sesion;

    public CatalogoService(DatosRestaurante datos, SesionService sesion) {
        this.datos = datos;
        this.sesion = sesion;
    }

    public List<Categoria> categorias() { sesion.actual(); return List.copyOf(datos.categorias); }
    public List<Producto> productos() { sesion.actual(); return List.copyOf(datos.productos); }

    public Categoria guardarCategoria(int id, String nombre, TipoCategoria tipo, boolean activo) {
        return datos.transaccion(() -> {
            sesion.exigir();
            String valido = Validacion.texto(nombre, "Nombre", 100);
            Validacion.exigir(tipo != null, "Selecciona un tipo.");
            Validacion.exigir(datos.categorias.stream().noneMatch(c -> c.getId() != id
                    && c.getNombre().equalsIgnoreCase(valido)), "La categoría ya existe.");
            Categoria categoria = id == 0 ? new Categoria() : datos.categorias.stream()
                    .filter(c -> c.getId() == id).findFirst().orElseThrow();
            if (id == 0) categoria.setId(datos.siguienteId(Categoria.class));
            categoria.setNombre(valido);
            categoria.setTipo(tipo);
            categoria.setActivo(activo);
            datos.guardar(categoria);
            if (id == 0) datos.categorias.add(categoria);
            return categoria;
        });
    }

    public void eliminarCategoria(Categoria categoria) {
        datos.transaccion(() -> {
            sesion.exigir();
            Validacion.exigir(datos.productos.stream().noneMatch(p -> p.getcategoria() == categoria),
                    "La categoría tiene productos; puedes desactivarla.");
            datos.eliminar(categoria);
            datos.categorias.remove(categoria);
            return null;
        });
    }

    public Producto guardarProducto(int id, String nombre, String descripcion, String imagen, BigDecimal precio,
                                    Categoria categoria, boolean disponible) {
        return datos.transaccion(() -> {
            sesion.exigir();
            String nombreValido = Validacion.texto(nombre, "Nombre", 150);
            String descripcionValida = Validacion.opcional(descripcion, "Descripción", 255);
            String imagenValida = Validacion.opcional(imagen, "Imagen", 255);
            BigDecimal precioValido = Validacion.dinero(precio, "Precio", true);
            Categoria categoriaValida = categoria == null ? null : datos.categorias.stream()
                    .filter(c -> c.getId() == categoria.getId()).findFirst().orElse(null);
            Validacion.exigir(categoriaValida != null, "Selecciona una categoría.");
            Validacion.exigir(!disponible || categoriaValida.isActivo(),
                    "Activa la categoría antes de ofrecer el producto.");
            Producto producto = id == 0 ? new Producto() : datos.productos.stream()
                    .filter(p -> p.getId() == id).findFirst().orElseThrow();
            if (id == 0) producto.setId(datos.siguienteId(Producto.class));
            producto.setNombre(nombreValido);
            producto.setDescripcion(descripcionValida);
            producto.setImagenUrl(imagenValida);
            producto.setPrecio(precioValido);
            producto.setCategoria(categoriaValida);
            producto.setDisponible(disponible);
            datos.guardar(producto);
            if (id == 0) datos.productos.add(producto);
            return producto;
        });
    }

    public void eliminarProducto(Producto producto) {
        datos.transaccion(() -> {
            sesion.exigir();
            Validacion.exigir(datos.pedidos.stream().flatMap(p -> p.getDetalles().stream())
                    .noneMatch(d -> d.getProducto() == producto),
                    "El producto tiene pedidos; desactívalo para conservar el historial.");
            datos.eliminar(producto);
            datos.productos.remove(producto);
            return null;
        });
    }

    public BigDecimal precioVigente(Producto producto) {
        sesion.actual();
        return producto.getPrecio();
    }
}
