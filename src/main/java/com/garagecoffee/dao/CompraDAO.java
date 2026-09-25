package com.garagecoffee.dao;

import com.garagecoffee.database.ConexionDB;
import com.garagecoffee.model.Compra;
import com.garagecoffee.model.DetalleCompra;
import com.garagecoffee.model.EstadoCompra;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/** Guarda y recupera objetos Compra en MySQL. */
public class CompraDAO {

    private static final String CONSULTA_BASE =
            "SELECT id, proveedor_id, fecha, total, estado FROM compra";
    private final DetalleCompraDAO detalleDAO = new DetalleCompraDAO();

    public List<Compra> listar() throws SQLException {
        try (Connection conexion = ConexionDB.getConnection()) {
            conexion.setAutoCommit(false);
            try {
                List<Compra> registros = new ArrayList<>();
                try (PreparedStatement consulta = conexion.prepareStatement(CONSULTA_BASE + " ORDER BY id");
                     ResultSet resultado = consulta.executeQuery()) {
                    while (resultado.next()) {
                        registros.add(leer(conexion, resultado));
                    }
                }
                conexion.commit();
                return registros;
            } catch (SQLException | RuntimeException error) {
                revertir(conexion, error);
                throw error;
            }
        }
    }

    public Compra buscarPorId(int id) throws SQLException {
        try (Connection conexion = ConexionDB.getConnection()) {
            conexion.setAutoCommit(false);
            try {
                Compra compra = buscarPorId(conexion, id);
                conexion.commit();
                return compra;
            } catch (SQLException | RuntimeException error) {
                revertir(conexion, error);
                throw error;
            }
        }
    }

    Compra buscarPorId(Connection conexion, int id) throws SQLException {
        try (PreparedStatement consulta = conexion.prepareStatement(CONSULTA_BASE + " WHERE id = ?")) {
            consulta.setInt(1, id);
            try (ResultSet resultado = consulta.executeQuery()) {
                return resultado.next() ? leer(conexion, resultado) : null;
            }
        }
    }

    // Guarda la cabecera y todos los detalles en una sola transacción.
    public boolean guardar(Compra compra) throws SQLException {
        if (compra.getId() != 0) {
            throw new IllegalArgumentException("Para guardar un nuevo compra, su ID debe ser cero");
        }
        for (DetalleCompra detalle : compra.getDetalles()) {
            if (detalle == null || detalle.getId() != 0 || detalle.getCompraId() != 0) {
                throw new IllegalArgumentException("Los detalles nuevos deben tener sus IDs en cero");
            }
        }
        compra.calcularTotal();
        String sql = "INSERT INTO compra (proveedor_id, fecha, total, estado) "
                + "VALUES (?, ?, ?, ?)";
        try (Connection conexion = ConexionDB.getConnection()) {
            conexion.setAutoCommit(false);
            try {
                int nuevoId;
                try (PreparedStatement consulta = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    escribir(consulta, compra);
                    consulta.executeUpdate();
                    try (ResultSet claves = consulta.getGeneratedKeys()) {
                        if (!claves.next()) {
                            throw new SQLException("No se obtuvo el ID de compra");
                        }
                        nuevoId = claves.getInt(1);
                    }
                }
                int[] ids = detalleDAO.guardarDetalles(conexion, nuevoId, compra.getDetalles());
                conexion.commit();
                compra.setId(nuevoId);
                asignarIds(compra, ids);
                return true;
            } catch (SQLException | RuntimeException error) {
                revertir(conexion, error);
                throw error;
            }
        }
    }

    // Sincroniza la lista completa: inserta nuevos, actualiza existentes y elimina retirados.
    public boolean actualizar(Compra compra) throws SQLException {
        compra.calcularTotal();
        String sql = "UPDATE compra SET proveedor_id = ?, fecha = ?, total = ?, estado = ? WHERE id = ?";
        try (Connection conexion = ConexionDB.getConnection()) {
            conexion.setAutoCommit(false);
            try {
                if (!bloquear(conexion, compra.getId())) {
                    conexion.rollback();
                    return false;
                }
                try (PreparedStatement consulta = conexion.prepareStatement(sql)) {
                    escribir(consulta, compra);
                    consulta.setInt(5, compra.getId());
                    consulta.executeUpdate();
                }
                int[] ids = detalleDAO.guardarDetalles(conexion, compra.getId(), compra.getDetalles());
                conexion.commit();
                asignarIds(compra, ids);
                return true;
            } catch (SQLException | RuntimeException error) {
                revertir(conexion, error);
                throw error;
            }
        }
    }

    // Borra también sus detalles; las demás claves foráneas siguen protegiendo el registro.
    public boolean eliminar(int id) throws SQLException {
        try (Connection conexion = ConexionDB.getConnection()) {
            conexion.setAutoCommit(false);
            try {
                if (!bloquear(conexion, id)) {
                    conexion.rollback();
                    return false;
                }
                detalleDAO.eliminarPorCompra(conexion, id);
                try (PreparedStatement consulta = conexion.prepareStatement("DELETE FROM compra WHERE id = ?")) {
                    consulta.setInt(1, id);
                    consulta.executeUpdate();
                }
                conexion.commit();
                return true;
            } catch (SQLException | RuntimeException error) {
                revertir(conexion, error);
                throw error;
            }
        }
    }

    private boolean bloquear(Connection conexion, int id) throws SQLException {
        try (PreparedStatement consulta = conexion.prepareStatement("SELECT id FROM compra WHERE id = ? FOR UPDATE")) {
            consulta.setInt(1, id);
            try (ResultSet resultado = consulta.executeQuery()) {
                return resultado.next();
            }
        }
    }

    private void asignarIds(Compra compra, int[] ids) {
        for (int i = 0; i < ids.length; i++) {
            DetalleCompra detalle = compra.getDetalles().get(i);
            detalle.setId(ids[i]);
            detalle.setCompraId(compra.getId());
        }
    }

    private void revertir(Connection conexion, Exception error) {
        try {
            conexion.rollback();
        } catch (SQLException errorRollback) {
            error.addSuppressed(errorRollback);
        }
    }

    private void escribir(PreparedStatement consulta, Compra compra) throws SQLException {
        consulta.setObject(1, compra.getProveedor() == null ? null : compra.getProveedor().getId());
        consulta.setObject(2, compra.getFecha());
        consulta.setBigDecimal(3, compra.getTotal());
        consulta.setString(4, compra.getEstado() == null ? null : compra.getEstado().name());
    }

    private Compra leer(Connection conexion, ResultSet resultado) throws SQLException {
        Compra compra = new Compra();
        compra.setId(resultado.getInt("id"));
        compra.setProveedor(new ProveedorDAO().buscarPorId(conexion, resultado.getInt("proveedor_id")));
        compra.setFecha(resultado.getObject("fecha", LocalDateTime.class));
        compra.setEstado(EstadoCompra.valueOf(resultado.getString("estado")));
        for (DetalleCompra detalle : detalleDAO.listarPorCompra(conexion, compra.getId())) {
            compra.agregarDetalle(detalle);
        }
        compra.setTotal(resultado.getBigDecimal("total"));
        return compra;
    }
}
