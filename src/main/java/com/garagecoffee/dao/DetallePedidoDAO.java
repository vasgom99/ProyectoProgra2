package com.garagecoffee.dao;

import com.garagecoffee.database.ConexionDB;
import com.garagecoffee.model.DetallePedido;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** Consulta los detalles; las escrituras se realizan desde PedidoDAO. */
public class DetallePedidoDAO {

    private static final String CONSULTA_BASE =
            "SELECT id, pedido_id, producto_id, cantidad, precio_unitario, subtotal FROM detalle_pedido";

    public List<DetallePedido> listar() throws SQLException {
        List<DetallePedido> detalles = new ArrayList<>();
        try (Connection conexion = ConexionDB.getConnection();
             PreparedStatement consulta = conexion.prepareStatement(CONSULTA_BASE + " ORDER BY id");
             ResultSet resultado = consulta.executeQuery()) {
            while (resultado.next()) {
                detalles.add(leer(conexion, resultado));
            }
        }
        return detalles;
    }

    public DetallePedido buscarPorId(int id) throws SQLException {
        try (Connection conexion = ConexionDB.getConnection();
             PreparedStatement consulta = conexion.prepareStatement(CONSULTA_BASE + " WHERE id = ?")) {
            consulta.setInt(1, id);
            try (ResultSet resultado = consulta.executeQuery()) {
                return resultado.next() ? leer(conexion, resultado) : null;
            }
        }
    }

    public List<DetallePedido> listarPorPedido(int id) throws SQLException {
        try (Connection conexion = ConexionDB.getConnection()) {
            return listarPorPedido(conexion, id);
        }
    }

    List<DetallePedido> listarPorPedido(Connection conexion, int id) throws SQLException {
        List<DetallePedido> detalles = new ArrayList<>();
        try (PreparedStatement consulta = conexion.prepareStatement(
                CONSULTA_BASE + " WHERE pedido_id = ? ORDER BY id")) {
            consulta.setInt(1, id);
            try (ResultSet resultado = consulta.executeQuery()) {
                while (resultado.next()) {
                    detalles.add(leer(conexion, resultado));
                }
            }
        }
        return detalles;
    }

    // Usa la transacción de PedidoDAO. No modifica IDs de los objetos antes del commit.
    int[] guardarDetalles(Connection conexion, int id, List<DetallePedido> detalles) throws SQLException {
        Set<Integer> pendientes = new HashSet<>();
        try (PreparedStatement consulta = conexion.prepareStatement(
                "SELECT id FROM detalle_pedido WHERE pedido_id = ?")) {
            consulta.setInt(1, id);
            try (ResultSet resultado = consulta.executeQuery()) {
                while (resultado.next()) {
                    pendientes.add(resultado.getInt("id"));
                }
            }
        }
        int[] ids = new int[detalles.size()];
        Set<DetallePedido> objetos = new HashSet<>();
        for (int i = 0; i < detalles.size(); i++) {
            DetallePedido detalle = detalles.get(i);
            if (detalle == null || !objetos.add(detalle)) {
                throw new IllegalArgumentException("Hay un detalle nulo o repetido");
            }
            if (detalle.getPedidoId() != 0 && detalle.getPedidoId() != id) {
                throw new IllegalArgumentException("El detalle pertenece a otro pedido");
            }
            if (detalle.getId() == 0) {
                ids[i] = insertar(conexion, id, detalle);
            } else {
                if (!pendientes.remove(detalle.getId())) {
                    throw new IllegalArgumentException("El detalle no pertenece a este pedido o está repetido");
                }
                actualizar(conexion, id, detalle);
                ids[i] = detalle.getId();
            }
        }
        try (PreparedStatement consulta = conexion.prepareStatement(
                "DELETE FROM detalle_pedido WHERE id = ? AND pedido_id = ?")) {
            for (int pendiente : pendientes) {
                consulta.setInt(1, pendiente);
                consulta.setInt(2, id);
                consulta.executeUpdate();
            }
        }
        return ids;
    }

    void eliminarPorPedido(Connection conexion, int id) throws SQLException {
        try (PreparedStatement consulta = conexion.prepareStatement(
                "DELETE FROM detalle_pedido WHERE pedido_id = ?")) {
            consulta.setInt(1, id);
            consulta.executeUpdate();
        }
    }

    private int insertar(Connection conexion, int id, DetallePedido detalle) throws SQLException {
        String sql = "INSERT INTO detalle_pedido (producto_id, cantidad, precio_unitario, subtotal, pedido_id) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement consulta = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            escribir(consulta, detalle);
            consulta.setInt(5, id);
            consulta.executeUpdate();
            try (ResultSet claves = consulta.getGeneratedKeys()) {
                if (!claves.next()) {
                    throw new SQLException("No se obtuvo el ID de detalle_pedido");
                }
                return claves.getInt(1);
            }
        }
    }

    private void actualizar(Connection conexion, int id, DetallePedido detalle) throws SQLException {
        String sql = "UPDATE detalle_pedido SET producto_id = ?, cantidad = ?, precio_unitario = ?, subtotal = ? "
                + "WHERE pedido_id = ? AND id = ?";
        try (PreparedStatement consulta = conexion.prepareStatement(sql)) {
            escribir(consulta, detalle);
            consulta.setInt(5, id);
            consulta.setInt(6, detalle.getId());
            consulta.executeUpdate();
        }
    }

    private void escribir(PreparedStatement consulta, DetallePedido detalle) throws SQLException {
        if (detalle.getCantidad() <= 0 || detalle.getPrecioUnitario() == null
                || detalle.getPrecioUnitario().signum() < 0) {
            throw new IllegalArgumentException("La cantidad debe ser positiva y el precio no puede ser negativo");
        }
        detalle.getPrecioUnitario().setScale(2, java.math.RoundingMode.UNNECESSARY);
        consulta.setObject(1, detalle.getProducto() == null ? null : detalle.getProducto().getId());
        consulta.setInt(2, detalle.getCantidad());
        consulta.setBigDecimal(3, detalle.getPrecioUnitario());
        consulta.setBigDecimal(4, detalle.calcularSubtotal());
    }

    private DetallePedido leer(Connection conexion, ResultSet resultado) throws SQLException {
        DetallePedido detalle = new DetallePedido();
        detalle.setId(resultado.getInt("id"));
        detalle.setPedidoId(resultado.getInt("pedido_id"));
        detalle.setProducto(new ProductoDAO().buscarPorId(conexion, resultado.getInt("producto_id")));
        detalle.setCantidad(resultado.getInt("cantidad"));
        detalle.setPrecioUnitario(resultado.getBigDecimal("precio_unitario"));
        detalle.setSubtotal(resultado.getBigDecimal("subtotal"));
        return detalle;
    }
}
