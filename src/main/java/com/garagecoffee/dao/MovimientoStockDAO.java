package com.garagecoffee.dao;

import com.garagecoffee.database.ConexionDB;
import com.garagecoffee.model.MovimientoStock;
import com.garagecoffee.model.TipoMovimientoStock;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/** Guarda y recupera objetos MovimientoStock en MySQL. */
public class MovimientoStockDAO {

    private static final String CONSULTA_BASE =
            "SELECT id, ingrediente_id, tipo, cantidad, costo_total, fecha_hora, motivo FROM movimiento_stock";

    public List<MovimientoStock> listar() throws SQLException {
        try (Connection conexion = ConexionDB.getConnection()) {
            return listar(conexion);
        }
    }

    List<MovimientoStock> listar(Connection conexion) throws SQLException {
        List<MovimientoStock> registros = new ArrayList<>();
        try (PreparedStatement consulta = conexion.prepareStatement(CONSULTA_BASE + " ORDER BY id");
             ResultSet resultado = consulta.executeQuery()) {
            while (resultado.next()) {
                registros.add(leer(conexion, resultado));
            }
        }
        return registros;
    }

    // Devuelve null si no existe el registro.
    public MovimientoStock buscarPorId(int id) throws SQLException {
        try (Connection conexion = ConexionDB.getConnection()) {
            return buscarPorId(conexion, id);
        }
    }

    // Reutiliza la conexión cuando otra DAO necesita recuperar una relación.
    MovimientoStock buscarPorId(Connection conexion, int id) throws SQLException {
        try (PreparedStatement consulta = conexion.prepareStatement(CONSULTA_BASE + " WHERE id = ?")) {
            consulta.setInt(1, id);
            try (ResultSet resultado = consulta.executeQuery()) {
                return resultado.next() ? leer(conexion, resultado) : null;
            }
        }
    }

    public List<MovimientoStock> listarPorIngrediente(int id) throws SQLException {
        try (Connection conexion = ConexionDB.getConnection()) {
            return listarPorIngrediente(conexion, id);
        }
    }

    List<MovimientoStock> listarPorIngrediente(Connection conexion, int id) throws SQLException {
        List<MovimientoStock> registros = new ArrayList<>();
        String sql = CONSULTA_BASE + " WHERE ingrediente_id = ? ORDER BY id";
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
    public boolean guardar(MovimientoStock movimientoStock) throws SQLException {
        String sql = "INSERT INTO movimiento_stock (ingrediente_id, tipo, cantidad, costo_total, fecha_hora, motivo) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conexion = ConexionDB.getConnection();
             PreparedStatement consulta = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            escribir(consulta, movimientoStock);
            if (consulta.executeUpdate() == 0) {
                return false;
            }
            try (ResultSet claves = consulta.getGeneratedKeys()) {
                if (!claves.next()) {
                    throw new SQLException("No se obtuvo el ID de movimiento_stock");
                }
                movimientoStock.setId(claves.getInt(1));
            }
            return true;
        }
    }

    public boolean actualizar(MovimientoStock movimientoStock) throws SQLException {
        String sql = "UPDATE movimiento_stock SET ingrediente_id = ?, tipo = ?, cantidad = ?, costo_total = ?, fecha_hora = ?, motivo = ? WHERE id = ?";
        try (Connection conexion = ConexionDB.getConnection();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {
            escribir(consulta, movimientoStock);
            consulta.setInt(7, movimientoStock.getId());
            return consulta.executeUpdate() > 0;
        }
    }

    // Las claves foráneas impiden borrar registros que todavía se utilizan.
    public boolean eliminar(int id) throws SQLException {
        try (Connection conexion = ConexionDB.getConnection();
             PreparedStatement consulta = conexion.prepareStatement("DELETE FROM movimiento_stock WHERE id = ?")) {
            consulta.setInt(1, id);
            return consulta.executeUpdate() > 0;
        }
    }

    private void escribir(PreparedStatement consulta, MovimientoStock movimientoStock) throws SQLException {
        consulta.setObject(1, movimientoStock.getIngrediente() == null ? null : movimientoStock.getIngrediente().getId());
        consulta.setString(2, movimientoStock.getTipo() == null ? null : movimientoStock.getTipo().name());
        consulta.setBigDecimal(3, movimientoStock.getCantidad());
        consulta.setBigDecimal(4, movimientoStock.getCostoTotal());
        consulta.setObject(5, movimientoStock.getFechaHora());
        consulta.setString(6, movimientoStock.getMotivo());
    }

    private MovimientoStock leer(Connection conexion, ResultSet resultado) throws SQLException {
        MovimientoStock movimientoStock = new MovimientoStock();
        movimientoStock.setId(resultado.getInt("id"));
        movimientoStock.setIngrediente(resultado.getObject("ingrediente_id") == null ? null
                : new IngredienteDAO().buscarPorId(conexion, resultado.getInt("ingrediente_id")));
        movimientoStock.setTipo(TipoMovimientoStock.valueOf(resultado.getString("tipo")));
        movimientoStock.setCantidad(resultado.getBigDecimal("cantidad"));
        movimientoStock.setCostoTotal(resultado.getBigDecimal("costo_total"));
        movimientoStock.setFechaHora(resultado.getObject("fecha_hora", LocalDateTime.class));
        movimientoStock.setMotivo(resultado.getString("motivo"));
        return movimientoStock;
    }
}
