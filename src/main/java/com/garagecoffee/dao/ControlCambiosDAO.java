package com.garagecoffee.dao;

import com.garagecoffee.database.ConexionDB;
import java.sql.*;

// Evita sobrescribir cambios realizados por otra sesión.
public class ControlCambiosDAO {
    public long revision(boolean bloquear) throws SQLException {
        try (var recurso = ConexionDB.abrir();
             var s = recurso.conexion.prepareStatement("SELECT revision FROM control_cambios WHERE id = 1" + (bloquear ? " FOR UPDATE" : ""));
             var r = s.executeQuery()) {
            if (!r.next()) throw new SQLException("Ejecuta garage_coffee.sql antes de iniciar la aplicación.");
            return r.getLong(1);
        }
    }
    public void avanzar() throws SQLException {
        try (var recurso = ConexionDB.abrir(); var s = recurso.conexion.prepareStatement("UPDATE control_cambios SET revision = revision + 1 WHERE id = 1")) {
            if (s.executeUpdate() != 1) throw new SQLException("No se pudo actualizar el control de cambios.");
        }
    }
}
