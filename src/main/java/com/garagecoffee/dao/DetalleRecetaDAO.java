package com.garagecoffee.dao;

import com.garagecoffee.database.ConexionDB;
import com.garagecoffee.model.DetalleReceta;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/** Guarda y recupera objetos DetalleReceta en MySQL. */
public class DetalleRecetaDAO {

    private static final String CONSULTA_BASE =
            "SELECT id, producto_id, ingrediente_id, cantidad_requerida FROM detalle_receta";

    public List<DetalleReceta> listar() throws SQLException {
        try (Connection conexion = ConexionDB.getConnection()) {
            return listar(conexion);
        }
    }

    List<DetalleReceta> listar(Connection conexion) throws SQLException {
        List<DetalleReceta> registros = new ArrayList<>();
        try (PreparedStatement consulta = conexion.prepareStatement(CONSULTA_BASE + " ORDER BY id");
             ResultSet resultado = consulta.executeQuery()) {
            while (resultado.next()) {
                registros.add(leer(conexion, resultado));
            }
        }
        return registros;
    }

    // Devuelve null si no existe el registro.
    public DetalleReceta buscarPorId(int id) throws SQLException {
        try (Connection conexion = ConexionDB.getConnection()) {
            return buscarPorId(conexion, id);
        }
    }

    // Reutiliza la conexión cuando otra DAO necesita recuperar una relación.
    DetalleReceta buscarPorId(Connection conexion, int id) throws SQLException {
        try (PreparedStatement consulta = conexion.prepareStatement(CONSULTA_BASE + " WHERE id = ?")) {
            consulta.setInt(1, id);
            try (ResultSet resultado = consulta.executeQuery()) {
                return resultado.next() ? leer(conexion, resultado) : null;
            }
        }
    }

    public List<DetalleReceta> listarPorProducto(int id) throws SQLException {
        try (Connection conexion = ConexionDB.getConnection()) {
            return listarPorProducto(conexion, id);
        }
    }

    List<DetalleReceta> listarPorProducto(Connection conexion, int id) throws SQLException {
        List<DetalleReceta> registros = new ArrayList<>();
        String sql = CONSULTA_BASE + " WHERE producto_id = ? ORDER BY id";
        try (PreparedStatement consulta = conexion.prepareStatement(sql)) {
            consulta.setInt(1, id);
            try (ResultSet resultado = consulta.executeQuery()) {
                while (resultado.next()) {
                    registros.add(leer(conexion, resultado));
                }
            }
        }
        return registros;
    }

    // Asigna al objeto el ID generado por MySQL.
    public boolean guardar(DetalleReceta detalleReceta) throws SQLException {
        String sql = "INSERT INTO detalle_receta (producto_id, ingrediente_id, cantidad_requerida) "
                + "VALUES (?, ?, ?)";
        try (Connection conexion = ConexionDB.getConnection();
             PreparedStatement consulta = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            escribir(consulta, detalleReceta);
            if (consulta.executeUpdate() == 0) {
                return false;
            }
            try (ResultSet claves = consulta.getGeneratedKeys()) {
                if (!claves.next()) {
                    throw new SQLException("No se obtuvo el ID de detalle_receta");
                }
                detalleReceta.setId(claves.getInt(1));
            }
            return true;
        }
    }

    public boolean actualizar(DetalleReceta detalleReceta) throws SQLException {
        String sql = "UPDATE detalle_receta SET producto_id = ?, ingrediente_id = ?, cantidad_requerida = ? WHERE id = ?";
        try (Connection conexion = ConexionDB.getConnection();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {
            escribir(consulta, detalleReceta);
            consulta.setInt(4, detalleReceta.getId());
            return consulta.executeUpdate() > 0;
        }
    }

    // Las claves foráneas impiden borrar registros que todavía se utilizan.
    public boolean eliminar(int id) throws SQLException {
        try (Connection conexion = ConexionDB.getConnection();
             PreparedStatement consulta = conexion.prepareStatement("DELETE FROM detalle_receta WHERE id = ?")) {
            consulta.setInt(1, id);
            return consulta.executeUpdate() > 0;
        }
    }

    private void escribir(PreparedStatement consulta, DetalleReceta detalleReceta) throws SQLException {
        consulta.setObject(1, detalleReceta.getProducto() == null ? null : detalleReceta.getProducto().getId());
        consulta.setObject(2, detalleReceta.getIngrediente() == null ? null : detalleReceta.getIngrediente().getId());
        consulta.setBigDecimal(3, detalleReceta.getCantidadRequerida());
    }

    private DetalleReceta leer(Connection conexion, ResultSet resultado) throws SQLException {
        DetalleReceta detalleReceta = new DetalleReceta();
        detalleReceta.setId(resultado.getInt("id"));
        detalleReceta.setProducto(resultado.getObject("producto_id") == null ? null
                : new ProductoDAO().buscarPorId(conexion, resultado.getInt("producto_id")));
        detalleReceta.setIngrediente(resultado.getObject("ingrediente_id") == null ? null
                : new IngredienteDAO().buscarPorId(conexion, resultado.getInt("ingrediente_id")));
        detalleReceta.setCantidadRequerida(resultado.getBigDecimal("cantidad_requerida"));
        return detalleReceta;
    }
}
