package com.garagecoffee.dao;

import com.garagecoffee.database.ConexionDB;
import com.garagecoffee.model.MovimientoCaja;
import com.garagecoffee.model.TipoMovimientoCaja;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/** Guarda y recupera objetos MovimientoCaja en MySQL. */
public class MovimientoCajaDAO {

    private static final String CONSULTA_BASE =
            "SELECT id, caja_id, tipo, concepto, monto, fecha_hora FROM movimiento_caja";

    public List<MovimientoCaja> listar() throws SQLException {
        try (Connection conexion = ConexionDB.getConnection()) {
            return listar(conexion);
        }
    }

    List<MovimientoCaja> listar(Connection conexion) throws SQLException {
        List<MovimientoCaja> registros = new ArrayList<>();
        try (PreparedStatement consulta = conexion.prepareStatement(CONSULTA_BASE + " ORDER BY id");
             ResultSet resultado = consulta.executeQuery()) {
            while (resultado.next()) {
                registros.add(leer(conexion, resultado));
            }
        }
        return registros;
    }

    // Devuelve null si no existe el registro.
    public MovimientoCaja buscarPorId(int id) throws SQLException {
        try (Connection conexion = ConexionDB.getConnection()) {
            return buscarPorId(conexion, id);
        }
    }

    // Reutiliza la conexión cuando otra DAO necesita recuperar una relación.
    MovimientoCaja buscarPorId(Connection conexion, int id) throws SQLException {
        try (PreparedStatement consulta = conexion.prepareStatement(CONSULTA_BASE + " WHERE id = ?")) {
            consulta.setInt(1, id);
            try (ResultSet resultado = consulta.executeQuery()) {
                return resultado.next() ? leer(conexion, resultado) : null;
            }
        }
    }

    public List<MovimientoCaja> listarPorCaja(int id) throws SQLException {
        try (Connection conexion = ConexionDB.getConnection()) {
            return listarPorCaja(conexion, id);
        }
    }

    List<MovimientoCaja> listarPorCaja(Connection conexion, int id) throws SQLException {
        List<MovimientoCaja> registros = new ArrayList<>();
        String sql = CONSULTA_BASE + " WHERE caja_id = ? ORDER BY id";
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
    public boolean guardar(MovimientoCaja movimientoCaja) throws SQLException {
        String sql = "INSERT INTO movimiento_caja (caja_id, tipo, concepto, monto, fecha_hora) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (Connection conexion = ConexionDB.getConnection();
             PreparedStatement consulta = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            escribir(consulta, movimientoCaja);
            if (consulta.executeUpdate() == 0) {
                return false;
            }
            try (ResultSet claves = consulta.getGeneratedKeys()) {
                if (!claves.next()) {
                    throw new SQLException("No se obtuvo el ID de movimiento_caja");
                }
                movimientoCaja.setId(claves.getInt(1));
            }
            return true;
        }
    }

    public boolean actualizar(MovimientoCaja movimientoCaja) throws SQLException {
        String sql = "UPDATE movimiento_caja SET caja_id = ?, tipo = ?, concepto = ?, monto = ?, fecha_hora = ? WHERE id = ?";
        try (Connection conexion = ConexionDB.getConnection();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {
            escribir(consulta, movimientoCaja);
            consulta.setInt(6, movimientoCaja.getId());
            return consulta.executeUpdate() > 0;
        }
    }

    // Las claves foráneas impiden borrar registros que todavía se utilizan.
    public boolean eliminar(int id) throws SQLException {
        try (Connection conexion = ConexionDB.getConnection();
             PreparedStatement consulta = conexion.prepareStatement("DELETE FROM movimiento_caja WHERE id = ?")) {
            consulta.setInt(1, id);
            return consulta.executeUpdate() > 0;
        }
    }

    private void escribir(PreparedStatement consulta, MovimientoCaja movimientoCaja) throws SQLException {
        consulta.setObject(1, movimientoCaja.getCaja() == null ? null : movimientoCaja.getCaja().getId());
        consulta.setString(2, movimientoCaja.getTipo() == null ? null : movimientoCaja.getTipo().name());
        consulta.setString(3, movimientoCaja.getConcepto());
        consulta.setBigDecimal(4, movimientoCaja.getMonto());
        consulta.setObject(5, movimientoCaja.getFechaHora());
    }

    private MovimientoCaja leer(Connection conexion, ResultSet resultado) throws SQLException {
        MovimientoCaja movimientoCaja = new MovimientoCaja();
        movimientoCaja.setId(resultado.getInt("id"));
        movimientoCaja.setCaja(resultado.getObject("caja_id") == null ? null
                : new CajaDAO().buscarPorId(conexion, resultado.getInt("caja_id")));
        movimientoCaja.setTipo(TipoMovimientoCaja.valueOf(resultado.getString("tipo")));
        movimientoCaja.setConcepto(resultado.getString("concepto"));
        movimientoCaja.setMonto(resultado.getBigDecimal("monto"));
        movimientoCaja.setFechaHora(resultado.getObject("fecha_hora", LocalDateTime.class));
        return movimientoCaja;
    }
}
