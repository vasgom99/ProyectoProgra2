package com.garagecoffee.dao;

import com.garagecoffee.database.ConexionDB;
import com.garagecoffee.model.Menu;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/** Guarda y recupera objetos Menu en MySQL. */
public class MenuDAO {

    private static final String CONSULTA_BASE =
            "SELECT id, nombre, activo, fecha_inicio, fecha_fin FROM menu";

    public List<Menu> listar() throws SQLException {
        try (Connection conexion = ConexionDB.getConnection()) {
            return listar(conexion);
        }
    }

    List<Menu> listar(Connection conexion) throws SQLException {
        List<Menu> registros = new ArrayList<>();
        try (PreparedStatement consulta = conexion.prepareStatement(CONSULTA_BASE + " ORDER BY id");
             ResultSet resultado = consulta.executeQuery()) {
            while (resultado.next()) {
                registros.add(leer(conexion, resultado));
            }
        }
        return registros;
    }

    // Devuelve null si no existe el registro.
    public Menu buscarPorId(int id) throws SQLException {
        try (Connection conexion = ConexionDB.getConnection()) {
            return buscarPorId(conexion, id);
        }
    }

    // Reutiliza la conexión cuando otra DAO necesita recuperar una relación.
    Menu buscarPorId(Connection conexion, int id) throws SQLException {
        try (PreparedStatement consulta = conexion.prepareStatement(CONSULTA_BASE + " WHERE id = ?")) {
            consulta.setInt(1, id);
            try (ResultSet resultado = consulta.executeQuery()) {
                return resultado.next() ? leer(conexion, resultado) : null;
            }
        }
    }

    // Asigna al objeto el ID generado por MySQL.
    public boolean guardar(Menu menu) throws SQLException {
        String sql = "INSERT INTO menu (nombre, activo, fecha_inicio, fecha_fin) "
                + "VALUES (?, ?, ?, ?)";
        try (Connection conexion = ConexionDB.getConnection();
             PreparedStatement consulta = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            escribir(consulta, menu);
            if (consulta.executeUpdate() == 0) {
                return false;
            }
            try (ResultSet claves = consulta.getGeneratedKeys()) {
                if (!claves.next()) {
                    throw new SQLException("No se obtuvo el ID de menu");
                }
                menu.setId(claves.getInt(1));
            }
            return true;
        }
    }

    public boolean actualizar(Menu menu) throws SQLException {
        String sql = "UPDATE menu SET nombre = ?, activo = ?, fecha_inicio = ?, fecha_fin = ? WHERE id = ?";
        try (Connection conexion = ConexionDB.getConnection();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {
            escribir(consulta, menu);
            consulta.setInt(5, menu.getId());
            return consulta.executeUpdate() > 0;
        }
    }

    // Las claves foráneas impiden borrar registros que todavía se utilizan.
    public boolean eliminar(int id) throws SQLException {
        try (Connection conexion = ConexionDB.getConnection();
             PreparedStatement consulta = conexion.prepareStatement("DELETE FROM menu WHERE id = ?")) {
            consulta.setInt(1, id);
            return consulta.executeUpdate() > 0;
        }
    }

    private void escribir(PreparedStatement consulta, Menu menu) throws SQLException {
        consulta.setString(1, menu.getNombre());
        consulta.setBoolean(2, menu.isActivo());
        consulta.setObject(3, menu.getFechaInicio());
        consulta.setObject(4, menu.getFechaFin());
    }

    private Menu leer(Connection conexion, ResultSet resultado) throws SQLException {
        Menu menu = new Menu();
        menu.setId(resultado.getInt("id"));
        menu.setNombre(resultado.getString("nombre"));
        menu.setActivo(resultado.getBoolean("activo"));
        menu.setFechaInicio(resultado.getObject("fecha_inicio", LocalDate.class));
        menu.setFechaFin(resultado.getObject("fecha_fin", LocalDate.class));
        return menu;
    }
}
