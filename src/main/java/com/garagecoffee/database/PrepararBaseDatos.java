package com.garagecoffee.database;

import java.sql.*;

// Ejecutar una vez sobre una base creada con el script del proyecto. No borra datos.
public class PrepararBaseDatos {
    public static void main(String[] args) throws SQLException {
        try (Connection c = ConexionDB.getConnection()) {
            columna(c, "cliente", "fecha_registro", "DATETIME NULL");
            columna(c, "repartidor", "nombre", "VARCHAR(150) NULL");
            columna(c, "pedido", "direccion_entrega", "VARCHAR(255) NULL");
            columna(c, "pedido", "costo_preparacion", "DECIMAL(12,2) NULL");
            ampliar(c, "repartidor", "vehiculo", 100);
            ampliar(c, "movimiento_caja", "concepto", 255);
            try (Statement s = c.createStatement()) {
                s.executeUpdate("CREATE TABLE IF NOT EXISTS control_cambios (id INT PRIMARY KEY, revision BIGINT NOT NULL) ENGINE=InnoDB");
                s.executeUpdate("INSERT IGNORE INTO control_cambios (id, revision) VALUES (1, 0)");
            }
        }
        System.out.println("Esquema actualizado sin eliminar tablas ni registros.");
    }
    private static void columna(Connection c, String tabla, String campo, String tipo) throws SQLException {
        try (ResultSet r = c.getMetaData().getColumns(c.getCatalog(), null, tabla, campo)) {
            if (r.next()) return;
        }
        try (Statement s = c.createStatement()) { s.executeUpdate("ALTER TABLE " + tabla + " ADD COLUMN " + campo + " " + tipo); }
    }
    private static void ampliar(Connection c, String tabla, String campo, int largo) throws SQLException {
        try (ResultSet r = c.getMetaData().getColumns(c.getCatalog(), null, tabla, campo)) {
            if (!r.next()) throw new SQLException("Falta " + tabla + "." + campo);
            if (r.getInt("COLUMN_SIZE") >= largo) return;
            String nullable = r.getInt("NULLABLE") == DatabaseMetaData.columnNoNulls ? " NOT NULL" : " NULL";
            try (Statement s = c.createStatement()) {
                s.executeUpdate("ALTER TABLE " + tabla + " MODIFY COLUMN " + campo + " VARCHAR(" + largo + ")" + nullable);
            }
        }
    }
}
