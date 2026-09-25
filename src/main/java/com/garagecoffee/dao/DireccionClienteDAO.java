package com.garagecoffee.dao;

import com.garagecoffee.database.ConexionDB;
import com.garagecoffee.model.DireccionCliente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/** Guarda y recupera objetos DireccionCliente en MySQL. */
public class DireccionClienteDAO {

    private static final String CONSULTA_BASE =
            "SELECT id, cliente_id, direccion, referencia FROM direccion_cliente";

    public List<DireccionCliente> listar() throws SQLException {
        try (Connection conexion = ConexionDB.getConnection()) {
            return listar(conexion);
        }
    }

    List<DireccionCliente> listar(Connection conexion) throws SQLException {
        List<DireccionCliente> registros = new ArrayList<>();
        try (PreparedStatement consulta = conexion.prepareStatement(CONSULTA_BASE + " ORDER BY id");
             ResultSet resultado = consulta.executeQuery()) {
            while (resultado.next()) {
                registros.add(leer(conexion, resultado));
            }
        }
        return registros;
    }

    // Devuelve null si no existe el registro.
    public DireccionCliente buscarPorId(int id) throws SQLException {
        try (Connection conexion = ConexionDB.getConnection()) {
            return buscarPorId(conexion, id);
        }
    }

    // Reutiliza la conexión cuando otra DAO necesita recuperar una relación.
    DireccionCliente buscarPorId(Connection conexion, int id) throws SQLException {
        try (PreparedStatement consulta = conexion.prepareStatement(CONSULTA_BASE + " WHERE id = ?")) {
            consulta.setInt(1, id);
            try (ResultSet resultado = consulta.executeQuery()) {
                return resultado.next() ? leer(conexion, resultado) : null;
            }
        }
    }

    public List<DireccionCliente> listarPorCliente(int id) throws SQLException {
        try (Connection conexion = ConexionDB.getConnection()) {
            return listarPorCliente(conexion, id);
        }
    }

    List<DireccionCliente> listarPorCliente(Connection conexion, int id) throws SQLException {
        List<DireccionCliente> registros = new ArrayList<>();
        String sql = CONSULTA_BASE + " WHERE cliente_id = ? ORDER BY id";
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
    public boolean guardar(DireccionCliente direccionCliente) throws SQLException {
        String sql = "INSERT INTO direccion_cliente (cliente_id, direccion, referencia) "
                + "VALUES (?, ?, ?)";
        try (Connection conexion = ConexionDB.getConnection();
             PreparedStatement consulta = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            escribir(consulta, direccionCliente);
            if (consulta.executeUpdate() == 0) {
                return false;
            }
            try (ResultSet claves = consulta.getGeneratedKeys()) {
                if (!claves.next()) {
                    throw new SQLException("No se obtuvo el ID de direccion_cliente");
                }
                direccionCliente.setId(claves.getInt(1));
            }
            return true;
        }
    }

    public boolean actualizar(DireccionCliente direccionCliente) throws SQLException {
        String sql = "UPDATE direccion_cliente SET cliente_id = ?, direccion = ?, referencia = ? WHERE id = ?";
        try (Connection conexion = ConexionDB.getConnection();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {
            escribir(consulta, direccionCliente);
            consulta.setInt(4, direccionCliente.getId());
            return consulta.executeUpdate() > 0;
        }
    }

    // Las claves foráneas impiden borrar registros que todavía se utilizan.
    public boolean eliminar(int id) throws SQLException {
        try (Connection conexion = ConexionDB.getConnection();
             PreparedStatement consulta = conexion.prepareStatement("DELETE FROM direccion_cliente WHERE id = ?")) {
            consulta.setInt(1, id);
            return consulta.executeUpdate() > 0;
        }
    }

    private void escribir(PreparedStatement consulta, DireccionCliente direccionCliente) throws SQLException {
        consulta.setInt(1, direccionCliente.getClienteId());
        consulta.setString(2, direccionCliente.getDireccion());
        consulta.setString(3, direccionCliente.getReferencia());
    }

    private DireccionCliente leer(Connection conexion, ResultSet resultado) throws SQLException {
        DireccionCliente direccionCliente = new DireccionCliente();
        direccionCliente.setId(resultado.getInt("id"));
        direccionCliente.setClienteId(resultado.getInt("cliente_id"));
        direccionCliente.setDireccion(resultado.getString("direccion"));
        direccionCliente.setReferencia(resultado.getString("referencia"));
        return direccionCliente;
    }
}
