package com.garagecoffee.dao;

import com.garagecoffee.database.ConexionDB;
import com.garagecoffee.model.Pedido;
import com.garagecoffee.model.DetallePedido;
import com.garagecoffee.model.EstadoPedido;
import com.garagecoffee.model.TipoEntrega;
import com.garagecoffee.model.PedidoLocal;
import com.garagecoffee.model.PedidoRecoger;
import com.garagecoffee.model.PedidoDomicilio;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/** Guarda y consulta los pedidos y sus detalles, respetando cada tipo de entrega. */
public class PedidoDAO {

    // La tabla pedido debe incluir direccion_entrega VARCHAR(255) NULL.

    private static final String CONSULTA_BASE =
            "SELECT id, cliente_id, mesa_id, repartidor_id, fecha_creacion, fecha_programada, estado, tipo_entrega, total, observaciones, orden_llegada, codigo_retiro, costo_envio, direccion_entrega FROM pedido";
    private final DetallePedidoDAO detalleDAO = new DetallePedidoDAO();

    public List<Pedido> listar() throws SQLException {
        try (ConexionDB.Recurso recurso = ConexionDB.abrir()) {
            recurso.conexion.setAutoCommit(false);
            try {
                List<Pedido> registros = new ArrayList<>();
                try (PreparedStatement consulta = recurso.conexion.prepareStatement(CONSULTA_BASE + " ORDER BY id");
                     ResultSet resultado = consulta.executeQuery()) {
                    while (resultado.next()) {
                        registros.add(leer(recurso.conexion, resultado));
                    }
                }
                ConexionDB.confirmar(recurso.conexion);
                return registros;
            } catch (SQLException | RuntimeException error) {
                revertir(recurso.conexion, error);
                throw error;
            }
        }
    }

    public Pedido buscarPorId(int id) throws SQLException {
        try (ConexionDB.Recurso recurso = ConexionDB.abrir()) {
            recurso.conexion.setAutoCommit(false);
            try {
                Pedido pedido = buscarPorId(recurso.conexion, id);
                ConexionDB.confirmar(recurso.conexion);
                return pedido;
            } catch (SQLException | RuntimeException error) {
                revertir(recurso.conexion, error);
                throw error;
            }
        }
    }

    Pedido buscarPorId(Connection conexion, int id) throws SQLException {
        try (PreparedStatement consulta = conexion.prepareStatement(CONSULTA_BASE + " WHERE id = ?")) {
            consulta.setInt(1, id);
            try (ResultSet resultado = consulta.executeQuery()) {
                return resultado.next() ? leer(conexion, resultado) : null;
            }
        }
    }

    // Guarda la cabecera y todos los detalles en una sola transacción.
    public boolean guardar(Pedido pedido) throws SQLException {
        if (pedido.getId() != 0) {
            throw new IllegalArgumentException("Para guardar un nuevo pedido, su ID debe ser cero");
        }
        for (DetallePedido detalle : pedido.getDetalles()) {
            if (detalle == null || detalle.getId() != 0 || detalle.getPedidoId() != 0) {
                throw new IllegalArgumentException("Los detalles nuevos deben tener sus IDs en cero");
            }
        }
        pedido.calcularTotal();
        String sql = "INSERT INTO pedido (cliente_id, mesa_id, repartidor_id, fecha_creacion, fecha_programada, estado, tipo_entrega, total, observaciones, orden_llegada, codigo_retiro, costo_envio, direccion_entrega) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (ConexionDB.Recurso recurso = ConexionDB.abrir()) {
            recurso.conexion.setAutoCommit(false);
            try {
                int nuevoId;
                try (PreparedStatement consulta = recurso.conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    escribir(consulta, pedido);
                    consulta.executeUpdate();
                    try (ResultSet claves = consulta.getGeneratedKeys()) {
                        if (!claves.next()) {
                            throw new SQLException("No se obtuvo el ID de pedido");
                        }
                        nuevoId = claves.getInt(1);
                    }
                }
                int[] ids = detalleDAO.guardarDetalles(recurso.conexion, nuevoId, pedido.getDetalles());
                ConexionDB.confirmar(recurso.conexion);
                pedido.setId(nuevoId);
                asignarIds(pedido, ids);
                return true;
            } catch (SQLException | RuntimeException error) {
                revertir(recurso.conexion, error);
                throw error;
            }
        }
    }

    // Sincroniza la lista completa: inserta nuevos, actualiza existentes y elimina retirados.
    public boolean actualizar(Pedido pedido) throws SQLException {
        pedido.calcularTotal();
        String sql = "UPDATE pedido SET cliente_id = ?, mesa_id = ?, repartidor_id = ?, fecha_creacion = ?, fecha_programada = ?, estado = ?, tipo_entrega = ?, total = ?, observaciones = ?, orden_llegada = ?, codigo_retiro = ?, costo_envio = ?, direccion_entrega = ? WHERE id = ?";
        try (ConexionDB.Recurso recurso = ConexionDB.abrir()) {
            recurso.conexion.setAutoCommit(false);
            try {
                if (!bloquear(recurso.conexion, pedido.getId())) {
                    ConexionDB.revertir(recurso.conexion);
                    return false;
                }
                try (PreparedStatement consulta = recurso.conexion.prepareStatement(sql)) {
                    escribir(consulta, pedido);
                    consulta.setInt(14, pedido.getId());
                    consulta.executeUpdate();
                }
                int[] ids = detalleDAO.guardarDetalles(recurso.conexion, pedido.getId(), pedido.getDetalles());
                ConexionDB.confirmar(recurso.conexion);
                asignarIds(pedido, ids);
                return true;
            } catch (SQLException | RuntimeException error) {
                revertir(recurso.conexion, error);
                throw error;
            }
        }
    }

    // Borra también sus detalles; las demás claves foráneas siguen protegiendo el registro.
    public boolean eliminar(int id) throws SQLException {
        try (ConexionDB.Recurso recurso = ConexionDB.abrir()) {
            recurso.conexion.setAutoCommit(false);
            try {
                if (!bloquear(recurso.conexion, id)) {
                    ConexionDB.revertir(recurso.conexion);
                    return false;
                }
                detalleDAO.eliminarPorPedido(recurso.conexion, id);
                try (PreparedStatement consulta = recurso.conexion.prepareStatement("DELETE FROM pedido WHERE id = ?")) {
                    consulta.setInt(1, id);
                    consulta.executeUpdate();
                }
                ConexionDB.confirmar(recurso.conexion);
                return true;
            } catch (SQLException | RuntimeException error) {
                revertir(recurso.conexion, error);
                throw error;
            }
        }
    }

    private boolean bloquear(Connection conexion, int id) throws SQLException {
        try (PreparedStatement consulta = conexion.prepareStatement("SELECT id FROM pedido WHERE id = ? FOR UPDATE")) {
            consulta.setInt(1, id);
            try (ResultSet resultado = consulta.executeQuery()) {
                return resultado.next();
            }
        }
    }

    private void asignarIds(Pedido pedido, int[] ids) {
        for (int i = 0; i < ids.length; i++) {
            DetallePedido detalle = pedido.getDetalles().get(i);
            detalle.setId(ids[i]);
            detalle.setPedidoId(pedido.getId());
        }
    }

    private void revertir(Connection conexion, Exception error) {
        try {
            ConexionDB.revertir(conexion);
        } catch (SQLException errorRollback) {
            error.addSuppressed(errorRollback);
        }
    }

    private void escribir(PreparedStatement consulta, Pedido pedido) throws SQLException {
        if (pedido instanceof PedidoDomicilio domicilio) {
            domicilio.getCostoEnvio().setScale(2, java.math.RoundingMode.UNNECESSARY);
        }
        consulta.setObject(1, pedido.getCliente() == null ? null : pedido.getCliente().getId());
        consulta.setObject(2, pedido instanceof PedidoLocal local && local.getMesa() != null
                ? local.getMesa().getId() : null);
        consulta.setObject(3, pedido instanceof PedidoDomicilio domicilio && domicilio.getRepartidor() != null
                ? domicilio.getRepartidor().getId() : null);
        consulta.setObject(4, pedido.getFechaCreacion());
        consulta.setObject(5, pedido.getFechaProgramada());
        consulta.setString(6, pedido.getEstado() == null ? null : pedido.getEstado().name());
        consulta.setString(7, pedido.getTipoEntrega().name());
        consulta.setBigDecimal(8, pedido.getTotal());
        consulta.setString(9, pedido.getObservaciones());
        consulta.setObject(10, pedido instanceof PedidoLocal local ? local.getOrdenLlegada() : null);
        consulta.setString(11, pedido instanceof PedidoRecoger recoger ? recoger.getCodigoRetiro() : null);
        consulta.setBigDecimal(12, pedido instanceof PedidoDomicilio domicilio ? domicilio.getCostoEnvio() : null);
        consulta.setString(13, pedido instanceof PedidoDomicilio domicilio ? domicilio.getDireccionEntrega() : null);
    }

    private Pedido leer(Connection conexion, ResultSet resultado) throws SQLException {
        int id = resultado.getInt("id");
        String observaciones = resultado.getString("observaciones");
        TipoEntrega tipo = TipoEntrega.valueOf(resultado.getString("tipo_entrega"));
        Pedido pedido;
        switch (tipo) {
            case CONSUMIR_LOCAL -> {
                PedidoLocal local = new PedidoLocal(id, observaciones, resultado.getInt("orden_llegada"));
                if (resultado.getObject("mesa_id") != null) {
                    local.setMesa(new MesaDAO().buscarPorId(conexion, resultado.getInt("mesa_id")));
                }
                pedido = local;
            }
            case RECOGER -> pedido = new PedidoRecoger(id, observaciones,
                    resultado.getString("codigo_retiro"), resultado.getObject("fecha_programada", LocalDateTime.class));
            case DOMICILIO -> {
                BigDecimal envio = resultado.getBigDecimal("costo_envio");
                PedidoDomicilio domicilio = new PedidoDomicilio(id, observaciones,
                        resultado.getString("direccion_entrega"), envio == null ? BigDecimal.ZERO : envio);
                if (resultado.getObject("repartidor_id") != null) {
                    domicilio.setRepartidor(new RepartidorDAO().buscarPorId(conexion, resultado.getInt("repartidor_id")));
                }
                pedido = domicilio;
            }
            default -> throw new SQLException("Tipo de entrega desconocido: " + tipo);
        }
        pedido.setFechaCreacion(resultado.getObject("fecha_creacion", LocalDateTime.class));
        pedido.setFechaProgramada(resultado.getObject("fecha_programada", LocalDateTime.class));
        pedido.setEstado(EstadoPedido.valueOf(resultado.getString("estado")));
        if (resultado.getObject("cliente_id") != null) {
            pedido.setCliente(new ClienteDAO().buscarPorId(conexion, resultado.getInt("cliente_id")));
        }
        for (DetallePedido detalle : detalleDAO.listarPorPedido(conexion, pedido.getId())) {
            pedido.agregarDetalle(detalle);
        }
        pedido.setTotal(resultado.getBigDecimal("total"));
        return pedido;
    }
}
