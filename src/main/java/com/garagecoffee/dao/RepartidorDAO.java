package com.garagecoffee.dao;

import com.garagecoffee.database.ConexionDB;
import com.garagecoffee.model.Repartidor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/** Guarda los datos de los repartidores, sus vehículos y si están activos. */
public class RepartidorDAO {

    // La tabla repartidor debe incluir nombre VARCHAR(150) NULL.

    private static final String CONSULTA_BASE =
            "SELECT id, nombre, telefono, vehiculo, activo FROM repartidor";

    public List<Repartidor> listar() throws SQLException {
        try (ConexionDB.Recurso recurso = ConexionDB.abrir()) {
            return listar(recurso.conexion);
        }
    }

    List<Repartidor> listar(Connection conexion) throws SQLException {
        List<Repartidor> registros = new ArrayList<>();
        try (PreparedStatement consulta = conexion.prepareStatement(CONSULTA_BASE + " ORDER BY id");
             ResultSet resultado = consulta.executeQuery()) {
            while (resultado.next()) {
                registros.add(leer(conexion, resultado));
            }
        }
        return registros;
    }

    // Devuelve null si no existe el registro.
    public Repartidor buscarPorId(int id) throws SQLException {
        try (ConexionDB.Recurso recurso = ConexionDB.abrir()) {
            return buscarPorId(recurso.conexion, id);
        }
    }

    // Reutiliza la conexión cuando otra DAO necesita recuperar una relación.
    Repartidor buscarPorId(Connection conexion, int id) throws SQLException {
        try (PreparedStatement consulta = conexion.prepareStatement(CONSULTA_BASE + " WHERE id = ?")) {
            consulta.setInt(1, id);
            try (ResultSet resultado = consulta.executeQuery()) {
                return resultado.next() ? leer(conexion, resultado) : null;
            }
        }
    }

    // Asigna al objeto el ID generado por MySQL.
    public boolean guardar(Repartidor repartidor) throws SQLException {
        String sql = "INSERT INTO repartidor (nombre, telefono, vehiculo, activo) "
                + "VALUES (?, ?, ?, ?)";
        try (ConexionDB.Recurso recurso = ConexionDB.abrir();
             PreparedStatement consulta = recurso.conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            escribir(consulta, repartidor);
            if (consulta.executeUpdate() == 0) {
                return false;
            }
            try (ResultSet claves = consulta.getGeneratedKeys()) {
                if (!claves.next()) {
                    throw new SQLException("No se obtuvo el ID de repartidor");
                }
                repartidor.setId(claves.getInt(1));
            }
            return true;
        }
    }

    public boolean actualizar(Repartidor repartidor) throws SQLException {
        String sql = "UPDATE repartidor SET nombre = ?, telefono = ?, vehiculo = ?, activo = ? WHERE id = ?";
        try (ConexionDB.Recurso recurso = ConexionDB.abrir();
             PreparedStatement consulta = recurso.conexion.prepareStatement(sql)) {
            escribir(consulta, repartidor);
            consulta.setInt(5, repartidor.getId());
            return consulta.executeUpdate() > 0;
        }
    }

    // Las claves foráneas impiden borrar registros que todavía se utilizan.
    public boolean eliminar(int id) throws SQLException {
        try (ConexionDB.Recurso recurso = ConexionDB.abrir();
             PreparedStatement consulta = recurso.conexion.prepareStatement("DELETE FROM repartidor WHERE id = ?")) {
            consulta.setInt(1, id);
            return consulta.executeUpdate() > 0;
        }
    }

    private void escribir(PreparedStatement consulta, Repartidor repartidor) throws SQLException {
        consulta.setString(1, repartidor.getNombre());
        consulta.setString(2, repartidor.getTelefono());
        consulta.setString(3, repartidor.getVehiculo());
        consulta.setBoolean(4, repartidor.isActivo());
    }

    private Repartidor leer(Connection conexion, ResultSet resultado) throws SQLException {
        Repartidor repartidor = new Repartidor();
        repartidor.setId(resultado.getInt("id"));
        repartidor.setNombre(resultado.getString("nombre"));
        repartidor.setTelefono(resultado.getString("telefono"));
        repartidor.setVehiculo(resultado.getString("vehiculo"));
        repartidor.setActivo(resultado.getBoolean("activo"));
        return repartidor;
    }
}
