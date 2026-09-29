package com.garagecoffee.dao;

import com.garagecoffee.database.ConexionDB;
import com.garagecoffee.model.Perdida;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/** Guarda y consulta las cantidades de ingredientes perdidos y sus motivos. */
public class PerdidaDAO {

    private static final String CONSULTA_BASE =
            "SELECT id, ingrediente_id, cantidad, motivo, fecha_hora FROM perdida";

    public List<Perdida> listar() throws SQLException {
        try (ConexionDB.Recurso recurso = ConexionDB.abrir()) {
            return listar(recurso.conexion);
        }
    }

    List<Perdida> listar(Connection conexion) throws SQLException {
        List<Perdida> registros = new ArrayList<>();
        try (PreparedStatement consulta = conexion.prepareStatement(CONSULTA_BASE + " ORDER BY id");
             ResultSet resultado = consulta.executeQuery()) {
            while (resultado.next()) {
                registros.add(leer(conexion, resultado));
            }
        }
        return registros;
    }

    // Devuelve null si no existe el registro.
    public Perdida buscarPorId(int id) throws SQLException {
        try (ConexionDB.Recurso recurso = ConexionDB.abrir()) {
            return buscarPorId(recurso.conexion, id);
        }
    }

    // Reutiliza la conexión cuando otra DAO necesita recuperar una relación.
    Perdida buscarPorId(Connection conexion, int id) throws SQLException {
        try (PreparedStatement consulta = conexion.prepareStatement(CONSULTA_BASE + " WHERE id = ?")) {
            consulta.setInt(1, id);
            try (ResultSet resultado = consulta.executeQuery()) {
                return resultado.next() ? leer(conexion, resultado) : null;
            }
        }
    }

    public List<Perdida> listarPorIngrediente(int id) throws SQLException {
        try (ConexionDB.Recurso recurso = ConexionDB.abrir()) {
            return listarPorIngrediente(recurso.conexion, id);
        }
    }

    List<Perdida> listarPorIngrediente(Connection conexion, int id) throws SQLException {
        List<Perdida> registros = new ArrayList<>();
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
    public boolean guardar(Perdida perdida) throws SQLException {
        String sql = "INSERT INTO perdida (ingrediente_id, cantidad, motivo, fecha_hora) "
                + "VALUES (?, ?, ?, ?)";
        try (ConexionDB.Recurso recurso = ConexionDB.abrir();
             PreparedStatement consulta = recurso.conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            escribir(consulta, perdida);
            if (consulta.executeUpdate() == 0) {
                return false;
            }
            try (ResultSet claves = consulta.getGeneratedKeys()) {
                if (!claves.next()) {
                    throw new SQLException("No se obtuvo el ID de perdida");
                }
                perdida.setId(claves.getInt(1));
            }
            return true;
        }
    }

    public boolean actualizar(Perdida perdida) throws SQLException {
        String sql = "UPDATE perdida SET ingrediente_id = ?, cantidad = ?, motivo = ?, fecha_hora = ? WHERE id = ?";
        try (ConexionDB.Recurso recurso = ConexionDB.abrir();
             PreparedStatement consulta = recurso.conexion.prepareStatement(sql)) {
            escribir(consulta, perdida);
            consulta.setInt(5, perdida.getId());
            return consulta.executeUpdate() > 0;
        }
    }

    // Las claves foráneas impiden borrar registros que todavía se utilizan.
    public boolean eliminar(int id) throws SQLException {
        try (ConexionDB.Recurso recurso = ConexionDB.abrir();
             PreparedStatement consulta = recurso.conexion.prepareStatement("DELETE FROM perdida WHERE id = ?")) {
            consulta.setInt(1, id);
            return consulta.executeUpdate() > 0;
        }
    }

    private void escribir(PreparedStatement consulta, Perdida perdida) throws SQLException {
        consulta.setObject(1, perdida.getIngrediente() == null ? null : perdida.getIngrediente().getId());
        consulta.setBigDecimal(2, perdida.getCantidad());
        consulta.setString(3, perdida.getMotivo());
        consulta.setObject(4, perdida.getFechaHora());
    }

    private Perdida leer(Connection conexion, ResultSet resultado) throws SQLException {
        Perdida perdida = new Perdida();
        perdida.setId(resultado.getInt("id"));
        perdida.setIngrediente(resultado.getObject("ingrediente_id") == null ? null
                : new IngredienteDAO().buscarPorId(conexion, resultado.getInt("ingrediente_id")));
        perdida.setCantidad(resultado.getBigDecimal("cantidad"));
        perdida.setMotivo(resultado.getString("motivo"));
        perdida.setFechaHora(resultado.getObject("fecha_hora", LocalDateTime.class));
        return perdida;
    }
}
