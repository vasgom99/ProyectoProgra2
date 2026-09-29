package com.garagecoffee.dao;

import com.garagecoffee.database.ConexionDB;
import java.sql.*;
import java.math.BigDecimal;
import java.util.*;

// Evita guardar sobre cambios de otra sesión y conserva el costo histórico del pedido.
public class ControlCambiosDAO {
    public long revision(boolean bloquear) throws SQLException {
        try (var recurso = ConexionDB.abrir();
             var s = recurso.conexion.prepareStatement("SELECT revision FROM control_cambios WHERE id = 1" + (bloquear ? " FOR UPDATE" : ""));
             var r = s.executeQuery()) {
            if (!r.next()) throw new SQLException("Ejecuta PrepararBaseDatos antes de iniciar la aplicación.");
            return r.getLong(1);
        }
    }
    public void avanzar() throws SQLException {
        try (var recurso = ConexionDB.abrir(); var s = recurso.conexion.prepareStatement("UPDATE control_cambios SET revision = revision + 1 WHERE id = 1")) {
            if (s.executeUpdate() != 1) throw new SQLException("No se pudo actualizar el control de cambios.");
        }
    }
    public Map<Integer, BigDecimal> costos() throws SQLException {
        Map<Integer, BigDecimal> costos = new HashMap<>();
        try (var recurso = ConexionDB.abrir(); var s = recurso.conexion.prepareStatement("SELECT id, costo_preparacion FROM pedido WHERE costo_preparacion IS NOT NULL"); var r = s.executeQuery()) {
            while (r.next()) costos.put(r.getInt(1), r.getBigDecimal(2));
        }
        return costos;
    }
    public void guardarCosto(int pedido, BigDecimal costo) throws SQLException {
        try (var recurso = ConexionDB.abrir(); var s = recurso.conexion.prepareStatement("UPDATE pedido SET costo_preparacion = ? WHERE id = ?")) {
            s.setBigDecimal(1, costo); s.setInt(2, pedido);
            if (s.executeUpdate() != 1) throw new SQLException("El pedido ya no existe.");
        }
    }
}
