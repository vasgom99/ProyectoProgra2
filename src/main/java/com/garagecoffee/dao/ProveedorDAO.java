package com.garagecoffee.dao;

import com.garagecoffee.database.ConexionDB;
import com.garagecoffee.model.Proveedor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/** Guarda y consulta los datos de los proveedores del restaurante. */
public class ProveedorDAO {

    private static final String CONSULTA_BASE =
            "SELECT id, nombre, telefono, email, direccion, activo FROM proveedor";

    public List<Proveedor> listar() throws SQLException {
        try (ConexionDB.Recurso recurso = ConexionDB.abrir()) {
            return listar(recurso.conexion);
        }
    }

    List<Proveedor> listar(Connection conexion) throws SQLException {
        List<Proveedor> registros = new ArrayList<>();
        try (PreparedStatement consulta = conexion.prepareStatement(CONSULTA_BASE + " ORDER BY id");
             ResultSet resultado = consulta.executeQuery()) {
            while (resultado.next()) {
                registros.add(leer(conexion, resultado));
            }
        }
        return registros;
    }

    // Devuelve null si no existe el registro.
    public Proveedor buscarPorId(int id) throws SQLException {
        try (ConexionDB.Recurso recurso = ConexionDB.abrir()) {
            return buscarPorId(recurso.conexion, id);
        }
    }

    // Reutiliza la conexión cuando otra DAO necesita recuperar una relación.
    Proveedor buscarPorId(Connection conexion, int id) throws SQLException {
        try (PreparedStatement consulta = conexion.prepareStatement(CONSULTA_BASE + " WHERE id = ?")) {
            consulta.setInt(1, id);
            try (ResultSet resultado = consulta.executeQuery()) {
                return resultado.next() ? leer(conexion, resultado) : null;
            }
        }
    }

    // Asigna al objeto el ID generado por MySQL.
    public boolean guardar(Proveedor proveedor) throws SQLException {
        String sql = "INSERT INTO proveedor (nombre, telefono, email, direccion, activo) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (ConexionDB.Recurso recurso = ConexionDB.abrir();
             PreparedStatement consulta = recurso.conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            escribir(consulta, proveedor);
            if (consulta.executeUpdate() == 0) {
                return false;
            }
            try (ResultSet claves = consulta.getGeneratedKeys()) {
                if (!claves.next()) {
                    throw new SQLException("No se obtuvo el ID de proveedor");
                }
                proveedor.setId(claves.getInt(1));
            }
            return true;
        }
    }

    public boolean actualizar(Proveedor proveedor) throws SQLException {
        String sql = "UPDATE proveedor SET nombre = ?, telefono = ?, email = ?, direccion = ?, activo = ? WHERE id = ?";
        try (ConexionDB.Recurso recurso = ConexionDB.abrir();
             PreparedStatement consulta = recurso.conexion.prepareStatement(sql)) {
            escribir(consulta, proveedor);
            consulta.setInt(6, proveedor.getId());
            return consulta.executeUpdate() > 0;
        }
    }

    // Las claves foráneas impiden borrar registros que todavía se utilizan.
    public boolean eliminar(int id) throws SQLException {
        try (ConexionDB.Recurso recurso = ConexionDB.abrir();
             PreparedStatement consulta = recurso.conexion.prepareStatement("DELETE FROM proveedor WHERE id = ?")) {
            consulta.setInt(1, id);
            return consulta.executeUpdate() > 0;
        }
    }

    private void escribir(PreparedStatement consulta, Proveedor proveedor) throws SQLException {
        consulta.setString(1, proveedor.getNombre());
        consulta.setString(2, proveedor.getTelefono());
        consulta.setString(3, proveedor.getEmail());
        consulta.setString(4, proveedor.getDireccion());
        consulta.setBoolean(5, proveedor.isActivo());
    }

    private Proveedor leer(Connection conexion, ResultSet resultado) throws SQLException {
        Proveedor proveedor = new Proveedor();
        proveedor.setId(resultado.getInt("id"));
        proveedor.setNombre(resultado.getString("nombre"));
        proveedor.setTelefono(resultado.getString("telefono"));
        proveedor.setEmail(resultado.getString("email"));
        proveedor.setDireccion(resultado.getString("direccion"));
        proveedor.setActivo(resultado.getBoolean("activo"));
        return proveedor;
    }
}
