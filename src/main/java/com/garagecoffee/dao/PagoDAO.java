package com.garagecoffee.dao;

import com.garagecoffee.database.ConexionDB;
import com.garagecoffee.model.Pago;
import com.garagecoffee.model.MetodoPago;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/** Guarda y consulta los pagos realizados para cada pedido. */
public class PagoDAO {

    private static final String CONSULTA_BASE =
            "SELECT id, pedido_id, metodo, monto, cambio, fecha_hora FROM pago";

    public List<Pago> listar() throws SQLException {
        try (ConexionDB.Recurso recurso = ConexionDB.abrir()) {
            return listar(recurso.conexion);
        }
    }

    List<Pago> listar(Connection conexion) throws SQLException {
        List<Pago> registros = new ArrayList<>();
        try (PreparedStatement consulta = conexion.prepareStatement(CONSULTA_BASE + " ORDER BY id");
             ResultSet resultado = consulta.executeQuery()) {
            while (resultado.next()) {
                registros.add(leer(conexion, resultado));
            }
        }
        return registros;
    }

    // Devuelve null si no existe el registro.
    public Pago buscarPorId(int id) throws SQLException {
        try (ConexionDB.Recurso recurso = ConexionDB.abrir()) {
            return buscarPorId(recurso.conexion, id);
        }
    }

    // Reutiliza la conexión cuando otra DAO necesita recuperar una relación.
    Pago buscarPorId(Connection conexion, int id) throws SQLException {
        try (PreparedStatement consulta = conexion.prepareStatement(CONSULTA_BASE + " WHERE id = ?")) {
            consulta.setInt(1, id);
            try (ResultSet resultado = consulta.executeQuery()) {
                return resultado.next() ? leer(conexion, resultado) : null;
            }
        }
    }

    public List<Pago> listarPorPedido(int id) throws SQLException {
        try (ConexionDB.Recurso recurso = ConexionDB.abrir()) {
            return listarPorPedido(recurso.conexion, id);
        }
    }

    List<Pago> listarPorPedido(Connection conexion, int id) throws SQLException {
        List<Pago> registros = new ArrayList<>();
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
    public boolean guardar(Pago pago) throws SQLException {
        String sql = "INSERT INTO pago (pedido_id, metodo, monto, cambio, fecha_hora) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (ConexionDB.Recurso recurso = ConexionDB.abrir();
             PreparedStatement consulta = recurso.conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            escribir(consulta, pago);
            if (consulta.executeUpdate() == 0) {
                return false;
            }
            try (ResultSet claves = consulta.getGeneratedKeys()) {
                if (!claves.next()) {
                    throw new SQLException("No se obtuvo el ID de pago");
                }
                pago.setId(claves.getInt(1));
            }
            return true;
        }
    }

    public boolean actualizar(Pago pago) throws SQLException {
        String sql = "UPDATE pago SET pedido_id = ?, metodo = ?, monto = ?, cambio = ?, fecha_hora = ? WHERE id = ?";
        try (ConexionDB.Recurso recurso = ConexionDB.abrir();
             PreparedStatement consulta = recurso.conexion.prepareStatement(sql)) {
            escribir(consulta, pago);
            consulta.setInt(6, pago.getId());
            return consulta.executeUpdate() > 0;
        }
    }

    // Las claves foráneas impiden borrar registros que todavía se utilizan.
    public boolean eliminar(int id) throws SQLException {
        try (ConexionDB.Recurso recurso = ConexionDB.abrir();
             PreparedStatement consulta = recurso.conexion.prepareStatement("DELETE FROM pago WHERE id = ?")) {
            consulta.setInt(1, id);
            return consulta.executeUpdate() > 0;
        }
    }

    private void escribir(PreparedStatement consulta, Pago pago) throws SQLException {
        consulta.setObject(1, pago.getPedido() == null ? null : pago.getPedido().getId());
        consulta.setString(2, pago.getMetodo() == null ? null : pago.getMetodo().name());
        consulta.setBigDecimal(3, pago.getMonto());
        consulta.setBigDecimal(4, pago.getCambio());
        consulta.setObject(5, pago.getFechaHora());
    }

    private Pago leer(Connection conexion, ResultSet resultado) throws SQLException {
        Pago pago = new Pago();
        pago.setId(resultado.getInt("id"));
        pago.setPedido(resultado.getObject("pedido_id") == null ? null
                : new PedidoDAO().buscarPorId(conexion, resultado.getInt("pedido_id")));
        pago.setMetodo(MetodoPago.valueOf(resultado.getString("metodo")));
        pago.setMonto(resultado.getBigDecimal("monto"));
        pago.setCambio(resultado.getBigDecimal("cambio"));
        pago.setFechaHora(resultado.getObject("fecha_hora", LocalDateTime.class));
        return pago;
    }
}
