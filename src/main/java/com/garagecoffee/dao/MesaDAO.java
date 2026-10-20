package com.garagecoffee.dao;

import com.garagecoffee.database.ConexionDB;
import com.garagecoffee.model.Mesa;
import com.garagecoffee.model.EstadoMesa;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/** Guarda las mesas, su capacidad y su estado. */
public class MesaDAO {

    private static final String CONSULTA_BASE =
            "SELECT id, numero, capacidad, estado FROM mesa";

    public List<Mesa> listar() throws SQLException {
        try (ConexionDB.Recurso recurso = ConexionDB.abrir()) {
            return listar(recurso.conexion);
        }
    }

    List<Mesa> listar(Connection conexion) throws SQLException {
        List<Mesa> registros = new ArrayList<>();
        try (PreparedStatement consulta = conexion.prepareStatement(CONSULTA_BASE + " ORDER BY id");
             ResultSet resultado = consulta.executeQuery()) {
            while (resultado.next()) {
                registros.add(leer(conexion, resultado));
            }
        }
        return registros;
    }

    // Devuelve null si no existe el registro.
    public Mesa buscarPorId(int id) throws SQLException {
        try (ConexionDB.Recurso recurso = ConexionDB.abrir()) {
            return buscarPorId(recurso.conexion, id);
        }
    }

    // Reutiliza la conexión cuando otra DAO necesita recuperar una relación.
    Mesa buscarPorId(Connection conexion, int id) throws SQLException {
        try (PreparedStatement consulta = conexion.prepareStatement(CONSULTA_BASE + " WHERE id = ?")) {
            consulta.setInt(1, id);
            try (ResultSet resultado = consulta.executeQuery()) {
                return resultado.next() ? leer(conexion, resultado) : null;
            }
        }
    }

    // Asigna al objeto el ID generado por MySQL.
    public boolean guardar(Mesa mesa) throws SQLException {
        String sql = "INSERT INTO mesa (numero, capacidad, estado) "
                + "VALUES (?, ?, ?)";
        try (ConexionDB.Recurso recurso = ConexionDB.abrir();
             PreparedStatement consulta = recurso.conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            escribir(consulta, mesa);
            if (consulta.executeUpdate() == 0) {
                return false;
            }
            try (ResultSet claves = consulta.getGeneratedKeys()) {
                if (!claves.next()) {
                    throw new SQLException("No se obtuvo el ID de mesa");
                }
                mesa.setId(claves.getInt(1));
            }
            return true;
        }
    }

    public boolean actualizar(Mesa mesa) throws SQLException {
        String sql = "UPDATE mesa SET numero = ?, capacidad = ?, estado = ? WHERE id = ?";
        try (ConexionDB.Recurso recurso = ConexionDB.abrir();
             PreparedStatement consulta = recurso.conexion.prepareStatement(sql)) {
            escribir(consulta, mesa);
            consulta.setInt(4, mesa.getId());
            return consulta.executeUpdate() > 0;
        }
    }

    // Las claves foráneas impiden borrar registros que todavía se utilizan.
    public boolean eliminar(int id) throws SQLException {
        try (ConexionDB.Recurso recurso = ConexionDB.abrir();
             PreparedStatement consulta = recurso.conexion.prepareStatement("DELETE FROM mesa WHERE id = ?")) {
            consulta.setInt(1, id);
            return consulta.executeUpdate() > 0;
        }
    }

    private void escribir(PreparedStatement consulta, Mesa mesa) throws SQLException {
        consulta.setInt(1, mesa.getNumero());
        consulta.setInt(2, mesa.getCapacidad());
        consulta.setString(3, mesa.getEstado() == null ? null : mesa.getEstado().name());
    }

    private Mesa leer(Connection conexion, ResultSet resultado) throws SQLException {
        Mesa mesa = new Mesa();
        mesa.setId(resultado.getInt("id"));
        mesa.setNumero(resultado.getInt("numero"));
        mesa.setCapacidad(resultado.getInt("capacidad"));
        mesa.setEstado(EstadoMesa.valueOf(resultado.getString("estado")));
        return mesa;
    }
}
