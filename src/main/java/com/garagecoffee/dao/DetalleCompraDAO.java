package com.garagecoffee.dao;

import com.garagecoffee.database.ConexionDB;
import com.garagecoffee.model.DetalleCompra;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** Consulta los detalles de las compras; CompraDAO coordina su guardado. */
public class DetalleCompraDAO {

    private static final String CONSULTA_BASE =
            "SELECT id, compra_id, ingrediente_id, cantidad, costo_unitario, subtotal FROM detalle_compra";

    public List<DetalleCompra> listar() throws SQLException {
        List<DetalleCompra> detalles = new ArrayList<>();
        try (ConexionDB.Recurso recurso = ConexionDB.abrir();
             PreparedStatement consulta = recurso.conexion.prepareStatement(CONSULTA_BASE + " ORDER BY id");
             ResultSet resultado = consulta.executeQuery()) {
            while (resultado.next()) {
                detalles.add(leer(recurso.conexion, resultado));
            }
        }
        return detalles;
    }

    public DetalleCompra buscarPorId(int id) throws SQLException {
        try (ConexionDB.Recurso recurso = ConexionDB.abrir();
             PreparedStatement consulta = recurso.conexion.prepareStatement(CONSULTA_BASE + " WHERE id = ?")) {
            consulta.setInt(1, id);
            try (ResultSet resultado = consulta.executeQuery()) {
                return resultado.next() ? leer(recurso.conexion, resultado) : null;
            }
        }
    }

    public List<DetalleCompra> listarPorCompra(int id) throws SQLException {
        try (ConexionDB.Recurso recurso = ConexionDB.abrir()) {
            return listarPorCompra(recurso.conexion, id);
        }
    }

    List<DetalleCompra> listarPorCompra(Connection conexion, int id) throws SQLException {
        List<DetalleCompra> detalles = new ArrayList<>();
        try (PreparedStatement consulta = conexion.prepareStatement(
                CONSULTA_BASE + " WHERE compra_id = ? ORDER BY id")) {
            consulta.setInt(1, id);
            try (ResultSet resultado = consulta.executeQuery()) {
                while (resultado.next()) {
                    detalles.add(leer(conexion, resultado));
                }
            }
        }
        return detalles;
    }

    // Usa la transacción de CompraDAO. No modifica IDs de los objetos antes del commit.
    int[] guardarDetalles(Connection conexion, int id, List<DetalleCompra> detalles) throws SQLException {
        Set<Integer> pendientes = new HashSet<>();
        try (PreparedStatement consulta = conexion.prepareStatement(
                "SELECT id FROM detalle_compra WHERE compra_id = ?")) {
            consulta.setInt(1, id);
            try (ResultSet resultado = consulta.executeQuery()) {
                while (resultado.next()) {
                    pendientes.add(resultado.getInt("id"));
                }
            }
        }
        int[] ids = new int[detalles.size()];
        Set<DetalleCompra> objetos = new HashSet<>();
        for (int i = 0; i < detalles.size(); i++) {
            DetalleCompra detalle = detalles.get(i);
            if (detalle == null || !objetos.add(detalle)) {
                throw new IllegalArgumentException("Hay un detalle nulo o repetido");
            }
            if (detalle.getCompraId() != 0 && detalle.getCompraId() != id) {
                throw new IllegalArgumentException("El detalle pertenece a otro compra");
            }
            if (detalle.getId() == 0) {
                ids[i] = insertar(conexion, id, detalle);
            } else {
                if (!pendientes.remove(detalle.getId())) {
                    throw new IllegalArgumentException("El detalle no pertenece a este compra o está repetido");
                }
                actualizar(conexion, id, detalle);
                ids[i] = detalle.getId();
            }
        }
        try (PreparedStatement consulta = conexion.prepareStatement(
                "DELETE FROM detalle_compra WHERE id = ? AND compra_id = ?")) {
            for (int pendiente : pendientes) {
                consulta.setInt(1, pendiente);
                consulta.setInt(2, id);
                consulta.executeUpdate();
            }
        }
        return ids;
    }

    void eliminarPorCompra(Connection conexion, int id) throws SQLException {
        try (PreparedStatement consulta = conexion.prepareStatement(
                "DELETE FROM detalle_compra WHERE compra_id = ?")) {
            consulta.setInt(1, id);
            consulta.executeUpdate();
        }
    }

    private int insertar(Connection conexion, int id, DetalleCompra detalle) throws SQLException {
        String sql = "INSERT INTO detalle_compra (ingrediente_id, cantidad, costo_unitario, subtotal, compra_id) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement consulta = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            escribir(consulta, detalle);
            consulta.setInt(5, id);
            consulta.executeUpdate();
            try (ResultSet claves = consulta.getGeneratedKeys()) {
                if (!claves.next()) {
                    throw new SQLException("No se obtuvo el ID de detalle_compra");
                }
                return claves.getInt(1);
            }
        }
    }

    private void actualizar(Connection conexion, int id, DetalleCompra detalle) throws SQLException {
        String sql = "UPDATE detalle_compra SET ingrediente_id = ?, cantidad = ?, costo_unitario = ?, subtotal = ? "
                + "WHERE compra_id = ? AND id = ?";
        try (PreparedStatement consulta = conexion.prepareStatement(sql)) {
            escribir(consulta, detalle);
            consulta.setInt(5, id);
            consulta.setInt(6, detalle.getId());
            consulta.executeUpdate();
        }
    }

    private void escribir(PreparedStatement consulta, DetalleCompra detalle) throws SQLException {
        if (detalle.getCantidad() == null || detalle.getCantidad().signum() <= 0
                || detalle.getCostoUnitario() == null || detalle.getCostoUnitario().signum() < 0) {
            throw new IllegalArgumentException("La cantidad debe ser positiva y el costo no puede ser negativo");
        }
        // No permite que MySQL redondee cantidades o costos sin reflejarlo en el total.
        detalle.getCantidad().setScale(3, java.math.RoundingMode.UNNECESSARY);
        detalle.getCostoUnitario().setScale(4, java.math.RoundingMode.UNNECESSARY);
        consulta.setObject(1, detalle.getIngrediente() == null ? null : detalle.getIngrediente().getId());
        consulta.setBigDecimal(2, detalle.getCantidad());
        consulta.setBigDecimal(3, detalle.getCostoUnitario());
        consulta.setBigDecimal(4, detalle.calcularSubtotal());
    }

    private DetalleCompra leer(Connection conexion, ResultSet resultado) throws SQLException {
        DetalleCompra detalle = new DetalleCompra();
        detalle.setId(resultado.getInt("id"));
        detalle.setCompraId(resultado.getInt("compra_id"));
        detalle.setIngrediente(new IngredienteDAO().buscarPorId(conexion, resultado.getInt("ingrediente_id")));
        detalle.setCantidad(resultado.getBigDecimal("cantidad"));
        detalle.setCostoUnitario(resultado.getBigDecimal("costo_unitario"));
        detalle.setSubtotal(resultado.getBigDecimal("subtotal"));
        return detalle;
    }
}
