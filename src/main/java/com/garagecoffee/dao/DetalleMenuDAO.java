package com.garagecoffee.dao;

import com.garagecoffee.database.ConexionDB;
import com.garagecoffee.model.DetalleMenu;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/** Relaciona los menús con sus productos y guarda sus precios especiales. */
public class DetalleMenuDAO {

    private static final String CONSULTA_BASE =
            "SELECT id, menu_id, producto_id, precio_especial FROM detalle_menu";

    public List<DetalleMenu> listar() throws SQLException {
        try (ConexionDB.Recurso recurso = ConexionDB.abrir()) {
            return listar(recurso.conexion);
        }
    }

    List<DetalleMenu> listar(Connection conexion) throws SQLException {
        List<DetalleMenu> registros = new ArrayList<>();
        try (PreparedStatement consulta = conexion.prepareStatement(CONSULTA_BASE + " ORDER BY id");
             ResultSet resultado = consulta.executeQuery()) {
            while (resultado.next()) {
                registros.add(leer(conexion, resultado));
            }
        }
        return registros;
    }

    // Devuelve null si no existe el registro.
    public DetalleMenu buscarPorId(int id) throws SQLException {
        try (ConexionDB.Recurso recurso = ConexionDB.abrir()) {
            return buscarPorId(recurso.conexion, id);
        }
    }

    // Reutiliza la conexión cuando otra DAO necesita recuperar una relación.
    DetalleMenu buscarPorId(Connection conexion, int id) throws SQLException {
        try (PreparedStatement consulta = conexion.prepareStatement(CONSULTA_BASE + " WHERE id = ?")) {
            consulta.setInt(1, id);
            try (ResultSet resultado = consulta.executeQuery()) {
                return resultado.next() ? leer(conexion, resultado) : null;
            }
        }
    }

    public List<DetalleMenu> listarPorMenu(int id) throws SQLException {
        try (ConexionDB.Recurso recurso = ConexionDB.abrir()) {
            return listarPorMenu(recurso.conexion, id);
        }
    }

    List<DetalleMenu> listarPorMenu(Connection conexion, int id) throws SQLException {
        List<DetalleMenu> registros = new ArrayList<>();
        String sql = CONSULTA_BASE + " WHERE menu_id = ? ORDER BY id";
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
    public boolean guardar(DetalleMenu detalleMenu) throws SQLException {
        String sql = "INSERT INTO detalle_menu (menu_id, producto_id, precio_especial) "
                + "VALUES (?, ?, ?)";
        try (ConexionDB.Recurso recurso = ConexionDB.abrir();
             PreparedStatement consulta = recurso.conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            escribir(consulta, detalleMenu);
            if (consulta.executeUpdate() == 0) {
                return false;
            }
            try (ResultSet claves = consulta.getGeneratedKeys()) {
                if (!claves.next()) {
                    throw new SQLException("No se obtuvo el ID de detalle_menu");
                }
                detalleMenu.setId(claves.getInt(1));
            }
            return true;
        }
    }

    public boolean actualizar(DetalleMenu detalleMenu) throws SQLException {
        String sql = "UPDATE detalle_menu SET menu_id = ?, producto_id = ?, precio_especial = ? WHERE id = ?";
        try (ConexionDB.Recurso recurso = ConexionDB.abrir();
             PreparedStatement consulta = recurso.conexion.prepareStatement(sql)) {
            escribir(consulta, detalleMenu);
            consulta.setInt(4, detalleMenu.getId());
            return consulta.executeUpdate() > 0;
        }
    }

    // Las claves foráneas impiden borrar registros que todavía se utilizan.
    public boolean eliminar(int id) throws SQLException {
        try (ConexionDB.Recurso recurso = ConexionDB.abrir();
             PreparedStatement consulta = recurso.conexion.prepareStatement("DELETE FROM detalle_menu WHERE id = ?")) {
            consulta.setInt(1, id);
            return consulta.executeUpdate() > 0;
        }
    }

    private void escribir(PreparedStatement consulta, DetalleMenu detalleMenu) throws SQLException {
        consulta.setObject(1, detalleMenu.getMenu() == null ? null : detalleMenu.getMenu().getId());
        consulta.setObject(2, detalleMenu.getProducto() == null ? null : detalleMenu.getProducto().getId());
        consulta.setBigDecimal(3, detalleMenu.getPrecioEspecial());
    }

    private DetalleMenu leer(Connection conexion, ResultSet resultado) throws SQLException {
        DetalleMenu detalleMenu = new DetalleMenu();
        detalleMenu.setId(resultado.getInt("id"));
        detalleMenu.setMenu(resultado.getObject("menu_id") == null ? null
                : new MenuDAO().buscarPorId(conexion, resultado.getInt("menu_id")));
        detalleMenu.setProducto(resultado.getObject("producto_id") == null ? null
                : new ProductoDAO().buscarPorId(conexion, resultado.getInt("producto_id")));
        detalleMenu.setPrecioEspecial(resultado.getBigDecimal("precio_especial"));
        return detalleMenu;
    }
}
