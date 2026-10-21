package com.garagecoffee.dao;

import com.garagecoffee.database.ConexionDB;
import com.garagecoffee.model.DetallePedido;
import com.garagecoffee.model.EstadoPedido;
import com.garagecoffee.model.Mesa;
import com.garagecoffee.model.Pedido;
import com.garagecoffee.model.PedidoLocal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/** guarda y consulta pedidos. */
public class PedidoDAO {
    private static final String COLUMNAS =
            "SELECT id, mesa_id, fecha_creacion, estado, total, observaciones FROM pedido";
    private final DetallePedidoDAO detalles = new DetallePedidoDAO();

    public List<Pedido> listar() throws SQLException {
        try (var recurso = ConexionDB.abrir()) {
            List<Pedido> pedidos = new ArrayList<>();
            try (var consulta = recurso.conexion.prepareStatement(COLUMNAS + " ORDER BY id");
                 var filas = consulta.executeQuery()) {
                while (filas.next()) pedidos.add(leer(recurso.conexion, filas));
            }
            return pedidos;
        }
    }

    public Pedido buscarPorId(int id) throws SQLException {
        try (var recurso = ConexionDB.abrir()) {
            return buscarPorId(recurso.conexion, id);
        }
    }

    Pedido buscarPorId(Connection conexion, int id) throws SQLException {
        try (var consulta = conexion.prepareStatement(COLUMNAS + " WHERE id = ?")) {
            consulta.setInt(1, id);
            try (var filas = consulta.executeQuery()) {
                return filas.next() ? leer(conexion, filas) : null;
            }
        }
    }

    public boolean guardar(Pedido pedido) throws SQLException {
        if (!(pedido instanceof PedidoLocal local) || pedido.getId() != 0)
            throw new IllegalArgumentException("Se esperaba un pedido local nuevo.");
        pedido.calcularTotal();
        return ConexionDB.transaccion(() -> {
            int nuevoId;
            try (var recurso = ConexionDB.abrir();
                 var consulta = recurso.conexion.prepareStatement(
                         "INSERT INTO pedido (mesa_id, fecha_creacion, estado, total, observaciones) VALUES (?, ?, ?, ?, ?)",
                         Statement.RETURN_GENERATED_KEYS)) {
                consulta.setInt(1, local.getMesa().getId());
                consulta.setObject(2, pedido.getFechaCreacion());
                consulta.setString(3, pedido.getEstado().name());
                consulta.setBigDecimal(4, pedido.getTotal());
                consulta.setString(5, pedido.getObservaciones());
                consulta.executeUpdate();
                try (var claves = consulta.getGeneratedKeys()) {
                    if (!claves.next()) throw new SQLException("No se obtuvo el ID del pedido.");
                    nuevoId = claves.getInt(1);
                }
                int[] ids = detalles.guardarDetalles(recurso.conexion, nuevoId, pedido.getDetalles());
                pedido.setId(nuevoId);
                for (int i = 0; i < ids.length; i++) {
                    DetallePedido detalle = pedido.getDetalles().get(i);
                    detalle.setId(ids[i]);
                    detalle.setPedidoId(nuevoId);
                }
            }
            return true;
        });
    }

    public boolean actualizar(Pedido pedido) throws SQLException {
        if (!(pedido instanceof PedidoLocal))
            throw new IllegalArgumentException("Se esperaba un pedido local.");
        try (var recurso = ConexionDB.abrir();
             var consulta = recurso.conexion.prepareStatement(
                     "UPDATE pedido SET estado = ?, total = ?, observaciones = ? WHERE id = ?")) {
            consulta.setString(1, pedido.getEstado().name());
            consulta.setBigDecimal(2, pedido.getTotal());
            consulta.setString(3, pedido.getObservaciones());
            consulta.setInt(4, pedido.getId());
            return consulta.executeUpdate() == 1;
        }
    }

    public boolean agregarDetalles(Pedido pedido, List<DetallePedido> nuevos) throws SQLException {
        if (!(pedido instanceof PedidoLocal) || nuevos == null || nuevos.isEmpty())
            throw new IllegalArgumentException("Se esperaban productos para un pedido local.");
        try (var recurso = ConexionDB.abrir()) {
            int[] ids = detalles.guardarDetalles(recurso.conexion, pedido.getId(), nuevos);
            for (int i = 0; i < ids.length; i++) {
                nuevos.get(i).setId(ids[i]);
                nuevos.get(i).setPedidoId(pedido.getId());
            }
            try (var consulta = recurso.conexion.prepareStatement(
                    "UPDATE pedido SET estado = ?, total = ?, observaciones = ? WHERE id = ?")) {
                consulta.setString(1, pedido.getEstado().name());
                consulta.setBigDecimal(2, pedido.getTotal());
                consulta.setString(3, pedido.getObservaciones());
                consulta.setInt(4, pedido.getId());
                return consulta.executeUpdate() == 1;
            }
        }
    }

    private Pedido leer(Connection conexion, ResultSet fila) throws SQLException {
        Mesa mesa = new MesaDAO().buscarPorId(conexion, fila.getInt("mesa_id"));
        if (mesa == null) throw new SQLException("El pedido referencia una mesa inexistente.");
        PedidoLocal pedido = new PedidoLocal(fila.getInt("id"), fila.getString("observaciones"), mesa);
        pedido.setFechaCreacion(fila.getObject("fecha_creacion", LocalDateTime.class));
        pedido.setEstado(EstadoPedido.valueOf(fila.getString("estado")));
        for (DetallePedido detalle : detalles.listarPorPedido(conexion, pedido.getId()))
            pedido.agregarDetalle(detalle);
        pedido.setTotal(fila.getBigDecimal("total"));
        return pedido;
    }
}
