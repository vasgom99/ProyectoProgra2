package com.garagecoffee.dao;

import com.garagecoffee.database.ConexionDB;
import com.garagecoffee.model.Pago;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/** Guarda y consulta los cobros en efectivo. */
public class PagoDAO {
    public List<Pago> listar() throws SQLException {
        try (var recurso = ConexionDB.abrir();
             var consulta = recurso.conexion.prepareStatement(
                     "SELECT id, pedido_id, caja_id, monto, cambio, fecha_hora FROM pago ORDER BY id");
             var filas = consulta.executeQuery()) {
            List<Pago> pagos = new ArrayList<>();
            while (filas.next()) {
                Pago pago = new Pago();
                pago.setId(filas.getInt("id"));
                pago.setPedido(new PedidoDAO().buscarPorId(recurso.conexion, filas.getInt("pedido_id")));
                pago.setCaja(new CajaDAO().buscarPorId(recurso.conexion, filas.getInt("caja_id")));
                pago.setMonto(filas.getBigDecimal("monto"));
                pago.setCambio(filas.getBigDecimal("cambio"));
                pago.setFechaHora(filas.getObject("fecha_hora", LocalDateTime.class));
                pagos.add(pago);
            }
            return pagos;
        }
    }

    public boolean guardar(Pago pago) throws SQLException {
        try (var recurso = ConexionDB.abrir();
             var consulta = recurso.conexion.prepareStatement(
                     "INSERT INTO pago (pedido_id, caja_id, monto, cambio, fecha_hora) VALUES (?, ?, ?, ?, ?)",
                     Statement.RETURN_GENERATED_KEYS)) {
            consulta.setInt(1, pago.getPedido().getId());
            consulta.setInt(2, pago.getCaja().getId());
            consulta.setBigDecimal(3, pago.getMonto());
            consulta.setBigDecimal(4, pago.getCambio());
            consulta.setObject(5, pago.getFechaHora());
            if (consulta.executeUpdate() != 1) return false;
            try (var claves = consulta.getGeneratedKeys()) {
                if (!claves.next()) throw new SQLException("No se obtuvo el ID del pago.");
                pago.setId(claves.getInt(1));
            }
            return true;
        }
    }
}
