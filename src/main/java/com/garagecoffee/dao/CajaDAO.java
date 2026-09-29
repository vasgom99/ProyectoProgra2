package com.garagecoffee.dao;

import com.garagecoffee.database.ConexionDB;
import com.garagecoffee.model.Caja;
import com.garagecoffee.model.EstadoCaja;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/** Guarda y consulta las aperturas, cierres y montos de caja. */
public class CajaDAO {

    private static final String CONSULTA_BASE =
            "SELECT id, fecha_apertura, fecha_cierre, monto_inicial, monto_final, estado FROM caja";

    public List<Caja> listar() throws SQLException {
        try (ConexionDB.Recurso recurso = ConexionDB.abrir()) {
            return listar(recurso.conexion);
        }
    }

    List<Caja> listar(Connection conexion) throws SQLException {
        List<Caja> registros = new ArrayList<>();
        try (PreparedStatement consulta = conexion.prepareStatement(CONSULTA_BASE + " ORDER BY id");
             ResultSet resultado = consulta.executeQuery()) {
            while (resultado.next()) {
                registros.add(leer(conexion, resultado));
            }
        }
        return registros;
    }

    // Devuelve null si no existe el registro.
    public Caja buscarPorId(int id) throws SQLException {
        try (ConexionDB.Recurso recurso = ConexionDB.abrir()) {
            return buscarPorId(recurso.conexion, id);
        }
    }

    // Reutiliza la conexión cuando otra DAO necesita recuperar una relación.
    Caja buscarPorId(Connection conexion, int id) throws SQLException {
        try (PreparedStatement consulta = conexion.prepareStatement(CONSULTA_BASE + " WHERE id = ?")) {
            consulta.setInt(1, id);
            try (ResultSet resultado = consulta.executeQuery()) {
                return resultado.next() ? leer(conexion, resultado) : null;
            }
        }
    }

    // Asigna al objeto el ID generado por MySQL.
    public boolean guardar(Caja caja) throws SQLException {
        String sql = "INSERT INTO caja (fecha_apertura, fecha_cierre, monto_inicial, monto_final, estado) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (ConexionDB.Recurso recurso = ConexionDB.abrir();
             PreparedStatement consulta = recurso.conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            escribir(consulta, caja);
            if (consulta.executeUpdate() == 0) {
                return false;
            }
            try (ResultSet claves = consulta.getGeneratedKeys()) {
                if (!claves.next()) {
                    throw new SQLException("No se obtuvo el ID de caja");
                }
                caja.setId(claves.getInt(1));
            }
            return true;
        }
    }

    public boolean actualizar(Caja caja) throws SQLException {
        String sql = "UPDATE caja SET fecha_apertura = ?, fecha_cierre = ?, monto_inicial = ?, monto_final = ?, estado = ? WHERE id = ?";
        try (ConexionDB.Recurso recurso = ConexionDB.abrir();
             PreparedStatement consulta = recurso.conexion.prepareStatement(sql)) {
            escribir(consulta, caja);
            consulta.setInt(6, caja.getId());
            return consulta.executeUpdate() > 0;
        }
    }

    // Las claves foráneas impiden borrar registros que todavía se utilizan.
    public boolean eliminar(int id) throws SQLException {
        try (ConexionDB.Recurso recurso = ConexionDB.abrir();
             PreparedStatement consulta = recurso.conexion.prepareStatement("DELETE FROM caja WHERE id = ?")) {
            consulta.setInt(1, id);
            return consulta.executeUpdate() > 0;
        }
    }

    private void escribir(PreparedStatement consulta, Caja caja) throws SQLException {
        consulta.setObject(1, caja.getFechaApertura());
        consulta.setObject(2, caja.getFechaCierre());
        consulta.setBigDecimal(3, caja.getMontoInicial());
        consulta.setBigDecimal(4, caja.getMontoFinal());
        consulta.setString(5, caja.getEstado() == null ? null : caja.getEstado().name());
    }

    private Caja leer(Connection conexion, ResultSet resultado) throws SQLException {
        Caja caja = new Caja();
        caja.setId(resultado.getInt("id"));
        caja.setFechaApertura(resultado.getObject("fecha_apertura", LocalDateTime.class));
        caja.setFechaCierre(resultado.getObject("fecha_cierre", LocalDateTime.class));
        caja.setMontoInicial(resultado.getBigDecimal("monto_inicial"));
        caja.setMontoFinal(resultado.getBigDecimal("monto_final"));
        caja.setEstado(EstadoCaja.valueOf(resultado.getString("estado")));
        return caja;
    }
}
