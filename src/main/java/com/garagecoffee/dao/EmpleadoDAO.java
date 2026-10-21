package com.garagecoffee.dao;

import com.garagecoffee.database.ConexionDB;
import com.garagecoffee.model.Empleado;
import com.garagecoffee.model.RolEmpleado;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/** guarda y consulta empleados. */
public class EmpleadoDAO {

    private static final String CONSULTA_BASE =
            "SELECT id, nombre, apellido, telefono, email, nombre_usuario, password, rol, activo FROM empleado";

    public List<Empleado> listar() throws SQLException {
        try (ConexionDB.Recurso recurso = ConexionDB.abrir()) {
            return listar(recurso.conexion);
        }
    }

    List<Empleado> listar(Connection conexion) throws SQLException {
        List<Empleado> registros = new ArrayList<>();
        try (PreparedStatement consulta = conexion.prepareStatement(CONSULTA_BASE + " ORDER BY id");
             ResultSet resultado = consulta.executeQuery()) {
            while (resultado.next()) {
                registros.add(leer(conexion, resultado));
            }
        }
        return registros;
    }

    // devuelve null si no existe.
    public Empleado buscarPorId(int id) throws SQLException {
        try (ConexionDB.Recurso recurso = ConexionDB.abrir()) {
            return buscarPorId(recurso.conexion, id);
        }
    }

    // reutiliza la conexión activa.
    Empleado buscarPorId(Connection conexion, int id) throws SQLException {
        try (PreparedStatement consulta = conexion.prepareStatement(CONSULTA_BASE + " WHERE id = ?")) {
            consulta.setInt(1, id);
            try (ResultSet resultado = consulta.executeQuery()) {
                return resultado.next() ? leer(conexion, resultado) : null;
            }
        }
    }

    // asigna el id generado.
    public boolean guardar(Empleado empleado) throws SQLException {
        String sql = "INSERT INTO empleado (nombre, apellido, telefono, email, nombre_usuario, password, rol, activo) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (ConexionDB.Recurso recurso = ConexionDB.abrir();
             PreparedStatement consulta = recurso.conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            escribir(consulta, empleado);
            if (consulta.executeUpdate() == 0) {
                return false;
            }
            try (ResultSet claves = consulta.getGeneratedKeys()) {
                if (!claves.next()) {
                    throw new SQLException("No se obtuvo el ID de empleado");
                }
                empleado.setId(claves.getInt(1));
            }
            return true;
        }
    }

    public Empleado buscarPorNombreUsuario(String nombreUsuario) throws SQLException {
        try (ConexionDB.Recurso recurso = ConexionDB.abrir();
             PreparedStatement consulta = recurso.conexion.prepareStatement(CONSULTA_BASE + " WHERE nombre_usuario = ?")) {
            consulta.setString(1, nombreUsuario);
            try (ResultSet resultado = consulta.executeQuery()) {
                return resultado.next() ? leer(recurso.conexion, resultado) : null;
            }
        }
    }

    public boolean actualizar(Empleado empleado) throws SQLException {
        String sql = "UPDATE empleado SET nombre = ?, apellido = ?, telefono = ?, email = ?, nombre_usuario = ?, password = ?, rol = ?, activo = ? WHERE id = ?";
        try (ConexionDB.Recurso recurso = ConexionDB.abrir();
             PreparedStatement consulta = recurso.conexion.prepareStatement(sql)) {
            escribir(consulta, empleado);
            consulta.setInt(9, empleado.getId());
            return consulta.executeUpdate() > 0;
        }
    }

    // respeta las relaciones existentes.
    public boolean eliminar(int id) throws SQLException {
        try (ConexionDB.Recurso recurso = ConexionDB.abrir();
             PreparedStatement consulta = recurso.conexion.prepareStatement("DELETE FROM empleado WHERE id = ?")) {
            consulta.setInt(1, id);
            return consulta.executeUpdate() > 0;
        }
    }

    private void escribir(PreparedStatement consulta, Empleado empleado) throws SQLException {
        consulta.setString(1, empleado.getNombre());
        consulta.setString(2, empleado.getApellido());
        consulta.setString(3, empleado.getTelefono());
        consulta.setString(4, empleado.getEmail());
        consulta.setString(5, empleado.getNombreUsuario());
        consulta.setString(6, empleado.getPassword());
        consulta.setString(7, empleado.getRol() == null ? null : empleado.getRol().name());
        consulta.setBoolean(8, empleado.isActivo());
    }

    private Empleado leer(Connection conexion, ResultSet resultado) throws SQLException {
        Empleado empleado = new Empleado();
        empleado.setId(resultado.getInt("id"));
        empleado.setNombre(resultado.getString("nombre"));
        empleado.setApellido(resultado.getString("apellido"));
        empleado.setTelefono(resultado.getString("telefono"));
        empleado.setEmail(resultado.getString("email"));
        empleado.setNombreUsuario(resultado.getString("nombre_usuario"));
        empleado.setPassword(resultado.getString("password"));
        empleado.setRol(RolEmpleado.valueOf(resultado.getString("rol")));
        empleado.setActivo(resultado.getBoolean("activo"));
        return empleado;
    }
}
