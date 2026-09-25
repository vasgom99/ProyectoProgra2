package com.garagecoffee.dao;

import com.garagecoffee.database.ConexionDB;
import com.garagecoffee.model.Cliente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/** Guarda y recupera objetos Cliente en MySQL. */
public class ClienteDAO {

    // La tabla cliente debe incluir fecha_registro DATETIME NULL.

    private static final String CONSULTA_BASE =
            "SELECT id, nombre, telefono, email, fecha_registro FROM cliente";

    public List<Cliente> listar() throws SQLException {
        try (Connection conexion = ConexionDB.getConnection()) {
            return listar(conexion);
        }
    }

    List<Cliente> listar(Connection conexion) throws SQLException {
        List<Cliente> registros = new ArrayList<>();
        try (PreparedStatement consulta = conexion.prepareStatement(CONSULTA_BASE + " ORDER BY id");
             ResultSet resultado = consulta.executeQuery()) {
            while (resultado.next()) {
                registros.add(leer(conexion, resultado));
            }
        }
        return registros;
    }

    // Devuelve null si no existe el registro.
    public Cliente buscarPorId(int id) throws SQLException {
        try (Connection conexion = ConexionDB.getConnection()) {
            return buscarPorId(conexion, id);
        }
    }

    // Reutiliza la conexión cuando otra DAO necesita recuperar una relación.
    Cliente buscarPorId(Connection conexion, int id) throws SQLException {
        try (PreparedStatement consulta = conexion.prepareStatement(CONSULTA_BASE + " WHERE id = ?")) {
            consulta.setInt(1, id);
            try (ResultSet resultado = consulta.executeQuery()) {
                return resultado.next() ? leer(conexion, resultado) : null;
            }
        }
    }

    // Asigna al objeto el ID generado por MySQL.
    // Las direcciones se guardan por separado con DireccionClienteDAO.
    public boolean guardar(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO cliente (nombre, telefono, email, fecha_registro) "
                + "VALUES (?, ?, ?, ?)";
        try (Connection conexion = ConexionDB.getConnection();
             PreparedStatement consulta = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            escribir(consulta, cliente);
            if (consulta.executeUpdate() == 0) {
                return false;
            }
            try (ResultSet claves = consulta.getGeneratedKeys()) {
                if (!claves.next()) {
                    throw new SQLException("No se obtuvo el ID de cliente");
                }
                cliente.setId(claves.getInt(1));
            }
            return true;
        }
    }

    public boolean actualizar(Cliente cliente) throws SQLException {
        String sql = "UPDATE cliente SET nombre = ?, telefono = ?, email = ?, fecha_registro = ? WHERE id = ?";
        try (Connection conexion = ConexionDB.getConnection();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {
            escribir(consulta, cliente);
            consulta.setInt(5, cliente.getId());
            return consulta.executeUpdate() > 0;
        }
    }

    // Las claves foráneas impiden borrar registros que todavía se utilizan.
    public boolean eliminar(int id) throws SQLException {
        try (Connection conexion = ConexionDB.getConnection();
             PreparedStatement consulta = conexion.prepareStatement("DELETE FROM cliente WHERE id = ?")) {
            consulta.setInt(1, id);
            return consulta.executeUpdate() > 0;
        }
    }

    private void escribir(PreparedStatement consulta, Cliente cliente) throws SQLException {
        consulta.setString(1, cliente.getNombre());
        consulta.setString(2, cliente.getTelefono());
        consulta.setString(3, cliente.getEmail());
        consulta.setObject(4, cliente.getFechaRegistro());
    }

    private Cliente leer(Connection conexion, ResultSet resultado) throws SQLException {
        Cliente cliente = new Cliente();
        cliente.setId(resultado.getInt("id"));
        cliente.setNombre(resultado.getString("nombre"));
        cliente.setTelefono(resultado.getString("telefono"));
        cliente.setEmail(resultado.getString("email"));
        cliente.setFechaRegistro(resultado.getObject("fecha_registro", LocalDateTime.class));
        for (var direccion : new DireccionClienteDAO().listarPorCliente(conexion, cliente.getId())) {
            cliente.agregarDireccion(direccion);
        }
        return cliente;
    }
}
