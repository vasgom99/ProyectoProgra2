package com.garagecoffee.dao;

import com.garagecoffee.database.ConexionDB;
import com.garagecoffee.model.Gasto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/** Guarda y consulta los gastos del restaurante. */
public class GastoDAO {

    private static final String CONSULTA_BASE =
            "SELECT id, categoria, descripcion, monto, fecha_hora FROM gasto";

    public List<Gasto> listar() throws SQLException {
        try (ConexionDB.Recurso recurso = ConexionDB.abrir()) {
            return listar(recurso.conexion);
        }
    }

    List<Gasto> listar(Connection conexion) throws SQLException {
        List<Gasto> registros = new ArrayList<>();
        try (PreparedStatement consulta = conexion.prepareStatement(CONSULTA_BASE + " ORDER BY id");
             ResultSet resultado = consulta.executeQuery()) {
            while (resultado.next()) {
                registros.add(leer(conexion, resultado));
            }
        }
        return registros;
    }

    // Devuelve null si no existe el registro.
    public Gasto buscarPorId(int id) throws SQLException {
        try (ConexionDB.Recurso recurso = ConexionDB.abrir()) {
            return buscarPorId(recurso.conexion, id);
        }
    }

    // Reutiliza la conexión cuando otra DAO necesita recuperar una relación.
    Gasto buscarPorId(Connection conexion, int id) throws SQLException {
        try (PreparedStatement consulta = conexion.prepareStatement(CONSULTA_BASE + " WHERE id = ?")) {
            consulta.setInt(1, id);
            try (ResultSet resultado = consulta.executeQuery()) {
                return resultado.next() ? leer(conexion, resultado) : null;
            }
        }
    }

    // Asigna al objeto el ID generado por MySQL.
    public boolean guardar(Gasto gasto) throws SQLException {
        String sql = "INSERT INTO gasto (categoria, descripcion, monto, fecha_hora) "
                + "VALUES (?, ?, ?, ?)";
        try (ConexionDB.Recurso recurso = ConexionDB.abrir();
             PreparedStatement consulta = recurso.conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            escribir(consulta, gasto);
            if (consulta.executeUpdate() == 0) {
                return false;
            }
            try (ResultSet claves = consulta.getGeneratedKeys()) {
                if (!claves.next()) {
                    throw new SQLException("No se obtuvo el ID de gasto");
                }
                gasto.setId(claves.getInt(1));
            }
            return true;
        }
    }

    public boolean actualizar(Gasto gasto) throws SQLException {
        String sql = "UPDATE gasto SET categoria = ?, descripcion = ?, monto = ?, fecha_hora = ? WHERE id = ?";
        try (ConexionDB.Recurso recurso = ConexionDB.abrir();
             PreparedStatement consulta = recurso.conexion.prepareStatement(sql)) {
            escribir(consulta, gasto);
            consulta.setInt(5, gasto.getId());
            return consulta.executeUpdate() > 0;
        }
    }

    // Las claves foráneas impiden borrar registros que todavía se utilizan.
    public boolean eliminar(int id) throws SQLException {
        try (ConexionDB.Recurso recurso = ConexionDB.abrir();
             PreparedStatement consulta = recurso.conexion.prepareStatement("DELETE FROM gasto WHERE id = ?")) {
            consulta.setInt(1, id);
            return consulta.executeUpdate() > 0;
        }
    }

    private void escribir(PreparedStatement consulta, Gasto gasto) throws SQLException {
        consulta.setString(1, gasto.getCategoria());
        consulta.setString(2, gasto.getDescripcion());
        consulta.setBigDecimal(3, gasto.getMonto());
        consulta.setObject(4, gasto.getFechaHora());
    }

    private Gasto leer(Connection conexion, ResultSet resultado) throws SQLException {
        Gasto gasto = new Gasto();
        gasto.setId(resultado.getInt("id"));
        gasto.setCategoria(resultado.getString("categoria"));
        gasto.setDescripcion(resultado.getString("descripcion"));
        gasto.setMonto(resultado.getBigDecimal("monto"));
        gasto.setFechaHora(resultado.getObject("fecha_hora", LocalDateTime.class));
        return gasto;
    }
}
