package com.garagecoffee.dao;

import com.garagecoffee.database.ConexionDB;
import com.garagecoffee.model.Categoria;
import com.garagecoffee.model.Producto;
import com.garagecoffee.model.TipoCategoria;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/** guarda y consulta productos. */
public class ProductoDAO {

    private static final String CONSULTA_BASE =
            "SELECT p.id, p.nombre, p.descripcion, p.imagen_url, p.precio, p.disponible, "
            + "p.categoria_id, c.nombre AS categoria_nombre, c.tipo AS categoria_tipo, "
            + "c.activo AS categoria_activo "
            + "FROM producto p JOIN categoria c ON p.categoria_id = c.id";

    public List<Producto> listar() throws SQLException {
        List<Producto> productos = new ArrayList<>();

        try (ConexionDB.Recurso recurso = ConexionDB.abrir();
             PreparedStatement consulta = recurso.conexion.prepareStatement(CONSULTA_BASE + " ORDER BY p.id");
             ResultSet resultado = consulta.executeQuery()) {
            while (resultado.next()) {
                productos.add(leerProducto(resultado));
            }
        }
        return productos;
    }

    // devuelve null si no existe.
    public Producto buscarPorId(int id) throws SQLException {
        try (ConexionDB.Recurso recurso = ConexionDB.abrir()) {
            return buscarPorId(recurso.conexion, id);
        }
    }

    Producto buscarPorId(Connection conexion, int id) throws SQLException {
        try (PreparedStatement consulta = conexion.prepareStatement(CONSULTA_BASE + " WHERE p.id = ?")) {
            consulta.setInt(1, id);
            try (ResultSet resultado = consulta.executeQuery()) {
                return resultado.next() ? leerProducto(resultado) : null;
            }
        }
    }

    // asigna el id generado.
    public boolean guardar(Producto producto) throws SQLException {
        String sql = "INSERT INTO producto "
                + "(categoria_id, nombre, descripcion, imagen_url, precio, disponible) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (ConexionDB.Recurso recurso = ConexionDB.abrir();
             PreparedStatement consulta = recurso.conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            consulta.setInt(1, producto.getcategoria().getId());
            consulta.setString(2, producto.getNombre());
            consulta.setString(3, producto.getDescripcion());
            consulta.setString(4, producto.getImagenUrl());
            consulta.setBigDecimal(5, producto.getPrecio());
            consulta.setBoolean(6, producto.isDisponible());

            if (consulta.executeUpdate() == 0) {
                return false;
            }
            try (ResultSet claves = consulta.getGeneratedKeys()) {
                if (!claves.next()) {
                    throw new SQLException("No se obtuvo el ID del producto guardado");
                }
                producto.setId(claves.getInt(1));
            }
            return true;
        }
    }

    public boolean actualizar(Producto producto) throws SQLException {
        String sql = "UPDATE producto SET categoria_id = ?, nombre = ?, descripcion = ?, "
                + "imagen_url = ?, precio = ?, disponible = ? WHERE id = ?";

        try (ConexionDB.Recurso recurso = ConexionDB.abrir();
             PreparedStatement consulta = recurso.conexion.prepareStatement(sql)) {
            consulta.setInt(1, producto.getcategoria().getId());
            consulta.setString(2, producto.getNombre());
            consulta.setString(3, producto.getDescripcion());
            consulta.setString(4, producto.getImagenUrl());
            consulta.setBigDecimal(5, producto.getPrecio());
            consulta.setBoolean(6, producto.isDisponible());
            consulta.setInt(7, producto.getId());
            return consulta.executeUpdate() > 0;
        }
    }

    // respeta las relaciones existentes.
    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM producto WHERE id = ?";

        try (ConexionDB.Recurso recurso = ConexionDB.abrir();
             PreparedStatement consulta = recurso.conexion.prepareStatement(sql)) {
            consulta.setInt(1, id);
            return consulta.executeUpdate() > 0;
        }
    }

    private Producto leerProducto(ResultSet resultado) throws SQLException {
        Categoria categoria = new Categoria(
                resultado.getInt("categoria_id"),
                resultado.getString("categoria_nombre"),
                TipoCategoria.valueOf(resultado.getString("categoria_tipo")),
                resultado.getBoolean("categoria_activo"));

        return new Producto(
                resultado.getInt("id"),
                resultado.getString("nombre"),
                resultado.getString("descripcion"),
                resultado.getString("imagen_url"),
                resultado.getBigDecimal("precio"),
                resultado.getBoolean("disponible"),
                categoria);
    }
}
