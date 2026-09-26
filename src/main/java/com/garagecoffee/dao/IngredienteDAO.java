package com.garagecoffee.dao;

import com.garagecoffee.database.ConexionDB;
import com.garagecoffee.model.Ingrediente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/** Guarda y recupera objetos Ingrediente en MySQL. */
public class IngredienteDAO {

    private static final String CONSULTA_BASE =
            "SELECT id, nombre, unidad_medida, stock_actual, stock_minimo, costo_unitario FROM ingrediente";

    public List<Ingrediente> listar() throws SQLException {
        try (Connection conexion = ConexionDB.getConnection()) {
            return listar(conexion);
        }
    }

    List<Ingrediente> listar(Connection conexion) throws SQLException {
        List<Ingrediente> registros = new ArrayList<>();
        try (PreparedStatement consulta = conexion.prepareStatement(CONSULTA_BASE + " ORDER BY id");
             ResultSet resultado = consulta.executeQuery()) {
            while (resultado.next()) {
                registros.add(leer(conexion, resultado));
            }
        }
        return registros;
    }

    // Devuelve null si no existe el registro.
    public Ingrediente buscarPorId(int id) throws SQLException {
        try (Connection conexion = ConexionDB.getConnection()) {
            return buscarPorId(conexion, id);
        }
    }

    // Reutiliza la conexión cuando otra DAO necesita recuperar una relación.
    Ingrediente buscarPorId(Connection conexion, int id) throws SQLException {
        try (PreparedStatement consulta = conexion.prepareStatement(CONSULTA_BASE + " WHERE id = ?")) {
            consulta.setInt(1, id);
            try (ResultSet resultado = consulta.executeQuery()) {
                return resultado.next() ? leer(conexion, resultado) : null;
            }
        }
    }

    // Asigna al objeto el ID generado por MySQL.
    public boolean guardar(Ingrediente ingrediente) throws SQLException {
        String sql = "INSERT INTO ingrediente (nombre, unidad_medida, stock_actual, stock_minimo, costo_unitario) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (Connection conexion = ConexionDB.getConnection();
             PreparedStatement consulta = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            escribir(consulta, ingrediente);
            if (consulta.executeUpdate() == 0) {
                return false;
            }
            try (ResultSet claves = consulta.getGeneratedKeys()) {
                if (!claves.next()) {
                    throw new SQLException("No se obtuvo el ID de ingrediente");
                }
                ingrediente.setId(claves.getInt(1));
            }
            return true;
        }
    }

    public boolean actualizar(Ingrediente ingrediente) throws SQLException {
        String sql = "UPDATE ingrediente SET nombre = ?, unidad_medida = ?, stock_actual = ?, stock_minimo = ?, costo_unitario = ? WHERE id = ?";
        try (Connection conexion = ConexionDB.getConnection();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {
            escribir(consulta, ingrediente);
            consulta.setInt(6, ingrediente.getId());
            return consulta.executeUpdate() > 0;
        }
    }

    // Las claves foráneas impiden borrar registros que todavía se utilizan.
    public boolean eliminar(int id) throws SQLException {
        try (Connection conexion = ConexionDB.getConnection();
             PreparedStatement consulta = conexion.prepareStatement("DELETE FROM ingrediente WHERE id = ?")) {
            consulta.setInt(1, id);
            return consulta.executeUpdate() > 0;
        }
    }

    private void escribir(PreparedStatement consulta, Ingrediente ingrediente) throws SQLException {
        consulta.setString(1, ingrediente.getNombre());
        consulta.setString(2, ingrediente.getUnidadMedida());
        consulta.setBigDecimal(3, ingrediente.getStockActual());
        consulta.setBigDecimal(4, ingrediente.getStockMinimo());
        consulta.setBigDecimal(5, ingrediente.getCostoUnitario());
    }

    private Ingrediente leer(Connection conexion, ResultSet resultado) throws SQLException {
        Ingrediente ingrediente = new Ingrediente();
        ingrediente.setId(resultado.getInt("id"));
        ingrediente.setNombre(resultado.getString("nombre"));
        ingrediente.setUnidadMedida(resultado.getString("unidad_medida"));
        ingrediente.setStockActual(resultado.getBigDecimal("stock_actual"));
        ingrediente.setStockMinimo(resultado.getBigDecimal("stock_minimo"));
        ingrediente.setCostoUnitario(resultado.getBigDecimal("costo_unitario"));
        return ingrediente;
    }
}
