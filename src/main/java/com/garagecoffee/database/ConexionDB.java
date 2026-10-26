package com.garagecoffee.database;

import java.sql.*;
import java.nio.file.*;
import java.io.*;
import java.util.Properties;

// administra la conexión con mysql.
public class ConexionDB {
    private static final ThreadLocal<Connection> actual = new ThreadLocal<>();
    private static volatile javax.sql.DataSource origen;
    // permite usar la conexión de spring.
    public static void usarDataSource(javax.sql.DataSource dataSource) { origen = dataSource; }
    private static String configuracion(String clave, String defecto) throws SQLException {
        Properties p = new Properties();
        Path archivo = Path.of("garage-db.properties");
        if (Files.exists(archivo)) {
            try (Reader lector = Files.newBufferedReader(archivo)) { p.load(lector); }
            catch (IOException e) { throw new SQLException("No se pudo leer garage-db.properties.", e); }
        }
        return System.getProperty(clave, p.getProperty(clave, defecto));
    }
    public static Connection getConnection() throws SQLException {
        javax.sql.DataSource fuente = origen;
        if (fuente != null) return fuente.getConnection();
        return DriverManager.getConnection(configuracion("garage.db.url", "jdbc:mysql://localhost:3306/garage_coffee"),
                configuracion("garage.db.user", "root"), configuracion("garage.db.password", "David_2006"));
    }
    // cierra las conexiones propias.
    public static Recurso abrir() throws SQLException {
        Connection compartida = actual.get();
        return new Recurso(compartida == null ? getConnection() : compartida, compartida == null);
    }
    public static final class Recurso implements AutoCloseable {
        public final Connection conexion;
        private final boolean propia;
        private Recurso(Connection conexion, boolean propia) { this.conexion = conexion; this.propia = propia; }
        @Override public void close() throws SQLException { if (propia) conexion.close(); }
    }
    public static void confirmar(Connection conexion) throws SQLException { if (actual.get() != conexion) conexion.commit(); }
    public static void revertir(Connection conexion) throws SQLException { if (actual.get() != conexion) conexion.rollback(); }
    public static boolean enTransaccion() { return actual.get() != null; }
    @FunctionalInterface public interface Operacion<T> { T ejecutar() throws SQLException; }
    public static <T> T transaccion(Operacion<T> operacion) throws SQLException {
        if (enTransaccion()) return operacion.ejecutar();
        try (Connection conexion = getConnection()) {
            conexion.setAutoCommit(false);
            actual.set(conexion);
            try {
                T resultado = operacion.ejecutar();
                conexion.commit();
                return resultado;
            } catch (SQLException | RuntimeException | Error e) {
                try { conexion.rollback(); } catch (SQLException rollback) { e.addSuppressed(rollback); }
                throw e;
            } finally { actual.remove(); }
        }
    }
    public static void closeConnection(Connection conexion) {
        if (conexion != null && conexion != actual.get()) {
            try { conexion.close(); } catch (SQLException e) { System.err.println("No se pudo cerrar la conexión."); }
        }
    }
}
