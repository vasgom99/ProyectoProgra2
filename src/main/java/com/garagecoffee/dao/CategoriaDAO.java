package com.garagecoffee.dao;

import com.garagecoffee.database.ConexionDB;
import com.garagecoffee.model.Categoria;
import com.garagecoffee.model.TipoCategoria;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/** guarda y consulta categorías. */
public class CategoriaDAO {

    public List<Categoria> listar() throws SQLException {
        List<Categoria> categorias = new ArrayList<>();
        String sql = "SELECT id, nombre, tipo, activo FROM categoria ORDER BY id";

        try (ConexionDB.Recurso recurso = ConexionDB.abrir();
             PreparedStatement consulta = recurso.conexion.prepareStatement(sql);
             ResultSet resultado = consulta.executeQuery()) {
            while (resultado.next()) {
                categorias.add(leerCategoria(resultado));
            }
        }
        return categorias;
    }

    // devuelve null si no existe.
    public Categoria buscarPorId(int id) throws SQLException {
        String sql = "SELECT id, nombre, tipo, activo FROM categoria WHERE id = ?";

        try (ConexionDB.Recurso recurso = ConexionDB.abrir();
             PreparedStatement consulta = recurso.conexion.prepareStatement(sql)) {
            consulta.setInt(1, id);
            try (ResultSet resultado = consulta.executeQuery()) {
                return resultado.next() ? leerCategoria(resultado) : null;
            }
        }
    }

    // asigna el id generado.
    public boolean guardar(Categoria categoria) throws SQLException {
        String sql = "INSERT INTO categoria (nombre, tipo, activo) VALUES (?, ?, ?)";

        try (ConexionDB.Recurso recurso = ConexionDB.abrir();
             PreparedStatement consulta = recurso.conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            consulta.setString(1, categoria.getNombre());
            consulta.setString(2, categoria.getTipo().name());
            consulta.setBoolean(3, categoria.isActivo());

            if (consulta.executeUpdate() == 0) {
                return false;
            }
            try (ResultSet claves = consulta.getGeneratedKeys()) {
                if (!claves.next()) {
                    throw new SQLException("No se obtuvo el ID de la categoría guardada");
                }
                categoria.setId(claves.getInt(1));
            }
            return true;
        }
    }

    public boolean actualizar(Categoria categoria) throws SQLException {
        String sql = "UPDATE categoria SET nombre = ?, tipo = ?, activo = ? WHERE id = ?";

        try (ConexionDB.Recurso recurso = ConexionDB.abrir();
             PreparedStatement consulta = recurso.conexion.prepareStatement(sql)) {
            consulta.setString(1, categoria.getNombre());
            consulta.setString(2, categoria.getTipo().name());
            consulta.setBoolean(3, categoria.isActivo());
            consulta.setInt(4, categoria.getId());
            return consulta.executeUpdate() > 0;
        }
    }

    // respeta las relaciones existentes.
    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM categoria WHERE id = ?";

        try (ConexionDB.Recurso recurso = ConexionDB.abrir();
             PreparedStatement consulta = recurso.conexion.prepareStatement(sql)) {
            consulta.setInt(1, id);
            return consulta.executeUpdate() > 0;
        }
    }

    private Categoria leerCategoria(ResultSet resultado) throws SQLException {
        return new Categoria(
                resultado.getInt("id"),
                resultado.getString("nombre"),
                TipoCategoria.valueOf(resultado.getString("tipo")),
                resultado.getBoolean("activo"));
    }
}
