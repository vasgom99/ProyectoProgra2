package com.garagecoffee.dao;

import com.garagecoffee.database.ConexionDB;
import com.garagecoffee.model.Factura;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/** Guarda y consulta las facturas asociadas a los pedidos. */
public class FacturaDAO {

    private static final String CONSULTA_BASE =
            "SELECT id, pedido_id, numero, fecha, subtotal, impuesto, total FROM factura";

    public List<Factura> listar() throws SQLException {
        try (ConexionDB.Recurso recurso = ConexionDB.abrir()) {
            return listar(recurso.conexion);
        }
    }

    List<Factura> listar(Connection conexion) throws SQLException {
        List<Factura> registros = new ArrayList<>();
        try (PreparedStatement consulta = conexion.prepareStatement(CONSULTA_BASE + " ORDER BY id");
             ResultSet resultado = consulta.executeQuery()) {
            while (resultado.next()) {
                registros.add(leer(conexion, resultado));
            }
        }
        return registros;
    }

    // Devuelve null si no existe el registro.
    public Factura buscarPorId(int id) throws SQLException {
        try (ConexionDB.Recurso recurso = ConexionDB.abrir()) {
            return buscarPorId(recurso.conexion, id);
        }
    }

    // Reutiliza la conexión cuando otra DAO necesita recuperar una relación.
    Factura buscarPorId(Connection conexion, int id) throws SQLException {
        try (PreparedStatement consulta = conexion.prepareStatement(CONSULTA_BASE + " WHERE id = ?")) {
            consulta.setInt(1, id);
            try (ResultSet resultado = consulta.executeQuery()) {
                return resultado.next() ? leer(conexion, resultado) : null;
            }
        }
    }

    public List<Factura> listarPorPedido(int id) throws SQLException {
        try (ConexionDB.Recurso recurso = ConexionDB.abrir()) {
            return listarPorPedido(recurso.conexion, id);
        }
    }

    List<Factura> listarPorPedido(Connection conexion, int id) throws SQLException {
        List<Factura> registros = new ArrayList<>();
        String sql = CONSULTA_BASE + " WHERE pedido_id = ? ORDER BY id";
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
    public boolean guardar(Factura factura) throws SQLException {
        String sql = "INSERT INTO factura (pedido_id, numero, fecha, subtotal, impuesto, total) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (ConexionDB.Recurso recurso = ConexionDB.abrir();
             PreparedStatement consulta = recurso.conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            escribir(consulta, factura);
            if (consulta.executeUpdate() == 0) {
                return false;
            }
            try (ResultSet claves = consulta.getGeneratedKeys()) {
                if (!claves.next()) {
                    throw new SQLException("No se obtuvo el ID de factura");
                }
                factura.setId(claves.getInt(1));
            }
            return true;
        }
    }

    public boolean actualizar(Factura factura) throws SQLException {
        String sql = "UPDATE factura SET pedido_id = ?, numero = ?, fecha = ?, subtotal = ?, impuesto = ?, total = ? WHERE id = ?";
        try (ConexionDB.Recurso recurso = ConexionDB.abrir();
             PreparedStatement consulta = recurso.conexion.prepareStatement(sql)) {
            escribir(consulta, factura);
            consulta.setInt(7, factura.getId());
            return consulta.executeUpdate() > 0;
        }
    }

    // Las claves foráneas impiden borrar registros que todavía se utilizan.
    public boolean eliminar(int id) throws SQLException {
        try (ConexionDB.Recurso recurso = ConexionDB.abrir();
             PreparedStatement consulta = recurso.conexion.prepareStatement("DELETE FROM factura WHERE id = ?")) {
            consulta.setInt(1, id);
            return consulta.executeUpdate() > 0;
        }
    }

    private void escribir(PreparedStatement consulta, Factura factura) throws SQLException {
        consulta.setObject(1, factura.getPedido() == null ? null : factura.getPedido().getId());
        consulta.setString(2, factura.getNumero());
        consulta.setObject(3, factura.getFecha());
        consulta.setBigDecimal(4, factura.getSubtotal());
        consulta.setBigDecimal(5, factura.getImpuesto());
        consulta.setBigDecimal(6, factura.getTotal());
    }

    private Factura leer(Connection conexion, ResultSet resultado) throws SQLException {
        Factura factura = new Factura();
        factura.setId(resultado.getInt("id"));
        factura.setPedido(resultado.getObject("pedido_id") == null ? null
                : new PedidoDAO().buscarPorId(conexion, resultado.getInt("pedido_id")));
        factura.setNumero(resultado.getString("numero"));
        factura.setFecha(resultado.getObject("fecha", LocalDateTime.class));
        factura.setSubtotal(resultado.getBigDecimal("subtotal"));
        factura.setImpuesto(resultado.getBigDecimal("impuesto"));
        factura.setTotal(resultado.getBigDecimal("total"));
        return factura;
    }
}
