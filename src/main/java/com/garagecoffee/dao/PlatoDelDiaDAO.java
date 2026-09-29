package com.garagecoffee.dao;

import com.garagecoffee.database.ConexionDB;
import com.garagecoffee.model.PlatoDelDia;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/** Guarda los productos ofrecidos como plato del día, su fecha y precio especial. */
public class PlatoDelDiaDAO {

    private static final String CONSULTA_BASE =
            "SELECT id, producto_id, fecha, nota_especial, precio_especial FROM plato_del_dia";

    public List<PlatoDelDia> listar() throws SQLException {
        try (ConexionDB.Recurso recurso = ConexionDB.abrir()) {
            return listar(recurso.conexion);
        }
    }

    List<PlatoDelDia> listar(Connection conexion) throws SQLException {
        List<PlatoDelDia> registros = new ArrayList<>();
        try (PreparedStatement consulta = conexion.prepareStatement(CONSULTA_BASE + " ORDER BY id");
             ResultSet resultado = consulta.executeQuery()) {
            while (resultado.next()) {
                registros.add(leer(conexion, resultado));
            }
        }
        return registros;
    }

    // Devuelve null si no existe el registro.
    public PlatoDelDia buscarPorId(int id) throws SQLException {
        try (ConexionDB.Recurso recurso = ConexionDB.abrir()) {
            return buscarPorId(recurso.conexion, id);
        }
    }

    // Reutiliza la conexión cuando otra DAO necesita recuperar una relación.
    PlatoDelDia buscarPorId(Connection conexion, int id) throws SQLException {
        try (PreparedStatement consulta = conexion.prepareStatement(CONSULTA_BASE + " WHERE id = ?")) {
            consulta.setInt(1, id);
            try (ResultSet resultado = consulta.executeQuery()) {
                return resultado.next() ? leer(conexion, resultado) : null;
            }
        }
    }

    // Asigna al objeto el ID generado por MySQL.
    public boolean guardar(PlatoDelDia platoDelDia) throws SQLException {
        String sql = "INSERT INTO plato_del_dia (producto_id, fecha, nota_especial, precio_especial) "
                + "VALUES (?, ?, ?, ?)";
        try (ConexionDB.Recurso recurso = ConexionDB.abrir();
             PreparedStatement consulta = recurso.conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            escribir(consulta, platoDelDia);
            if (consulta.executeUpdate() == 0) {
                return false;
            }
            try (ResultSet claves = consulta.getGeneratedKeys()) {
                if (!claves.next()) {
                    throw new SQLException("No se obtuvo el ID de plato_del_dia");
                }
                platoDelDia.setId(claves.getInt(1));
            }
            return true;
        }
    }

    public boolean actualizar(PlatoDelDia platoDelDia) throws SQLException {
        String sql = "UPDATE plato_del_dia SET producto_id = ?, fecha = ?, nota_especial = ?, precio_especial = ? WHERE id = ?";
        try (ConexionDB.Recurso recurso = ConexionDB.abrir();
             PreparedStatement consulta = recurso.conexion.prepareStatement(sql)) {
            escribir(consulta, platoDelDia);
            consulta.setInt(5, platoDelDia.getId());
            return consulta.executeUpdate() > 0;
        }
    }

    // Las claves foráneas impiden borrar registros que todavía se utilizan.
    public boolean eliminar(int id) throws SQLException {
        try (ConexionDB.Recurso recurso = ConexionDB.abrir();
             PreparedStatement consulta = recurso.conexion.prepareStatement("DELETE FROM plato_del_dia WHERE id = ?")) {
            consulta.setInt(1, id);
            return consulta.executeUpdate() > 0;
        }
    }

    private void escribir(PreparedStatement consulta, PlatoDelDia platoDelDia) throws SQLException {
        consulta.setObject(1, platoDelDia.getProducto() == null ? null : platoDelDia.getProducto().getId());
        consulta.setObject(2, platoDelDia.getFecha());
        consulta.setString(3, platoDelDia.getNotaEspecial());
        consulta.setBigDecimal(4, platoDelDia.getPrecioEspecial());
    }

    private PlatoDelDia leer(Connection conexion, ResultSet resultado) throws SQLException {
        PlatoDelDia platoDelDia = new PlatoDelDia();
        platoDelDia.setId(resultado.getInt("id"));
        platoDelDia.setProducto(resultado.getObject("producto_id") == null ? null
                : new ProductoDAO().buscarPorId(conexion, resultado.getInt("producto_id")));
        platoDelDia.setFecha(resultado.getObject("fecha", LocalDate.class));
        platoDelDia.setNotaEspecial(resultado.getString("nota_especial"));
        platoDelDia.setPrecioEspecial(resultado.getBigDecimal("precio_especial"));
        return platoDelDia;
    }
}
