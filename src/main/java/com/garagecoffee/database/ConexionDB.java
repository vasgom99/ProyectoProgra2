package com.garagecoffee.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {

    // Los valores actuales siguen funcionando; las propiedades permiten probar otra base.
    private static final String URL = System.getProperty("garage.db.url", "jdbc:mysql://localhost:3306/garage_coffee");
    private static final String USER = System.getProperty("garage.db.user", "root");
    private static final String PASSWORD = System.getProperty("garage.db.password", "root");

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }
}
