package com.garagecoffee.dao;

import com.garagecoffee.model.DetallePedido;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/** Guarda y consulta los productos de cada pedido. */
public class DetallePedidoDAO {
    List<DetallePedido> listarPorPedido(Connection conexion, int pedidoId) throws SQLException {
        List<DetallePedido> detalles = new ArrayList<>();
        String sql = "SELECT id, producto_id, cantidad, precio_unitario, subtotal "
                + "FROM detalle_pedido WHERE pedido_id = ? ORDER BY id";
        try (var consulta = conexion.prepareStatement(sql)) {
            consulta.setInt(1, pedidoId);
            try (var filas = consulta.executeQuery()) {
                while (filas.next()) {
                    DetallePedido detalle = new DetallePedido();
                    detalle.setId(filas.getInt("id"));
                    detalle.setPedidoId(pedidoId);
                    detalle.setProducto(new ProductoDAO().buscarPorId(conexion, filas.getInt("producto_id")));
                    detalle.setCantidad(filas.getInt("cantidad"));
                    detalle.setPrecioUnitario(filas.getBigDecimal("precio_unitario"));
                    detalle.setSubtotal(filas.getBigDecimal("subtotal"));
                    detalles.add(detalle);
                }
            }
        }
        return detalles;
    }

    // PedidoDAO proporciona la transacción y asigna los IDs al terminar.
    int[] guardarDetalles(Connection conexion, int pedidoId, List<DetallePedido> detalles) throws SQLException {
        int[] ids = new int[detalles.size()];
        String sql = "INSERT INTO detalle_pedido "
                + "(pedido_id, producto_id, cantidad, precio_unitario, subtotal) VALUES (?, ?, ?, ?, ?)";
        try (var consulta = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            for (int i = 0; i < detalles.size(); i++) {
                DetallePedido detalle = detalles.get(i);
                consulta.setInt(1, pedidoId);
                consulta.setInt(2, detalle.getProducto().getId());
                consulta.setInt(3, detalle.getCantidad());
                consulta.setBigDecimal(4, detalle.getPrecioUnitario());
                consulta.setBigDecimal(5, detalle.getSubtotal());
                if (consulta.executeUpdate() != 1) throw new SQLException("No se guardó el detalle del pedido.");
                try (var claves = consulta.getGeneratedKeys()) {
                    if (!claves.next()) throw new SQLException("No se obtuvo el ID del detalle.");
                    ids[i] = claves.getInt(1);
                }
            }
        }
        return ids;
    }
}
