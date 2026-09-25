package com.garagecoffee.dao;

import com.garagecoffee.database.ConexionDB;
import com.garagecoffee.model.Reserva;
import com.garagecoffee.model.EstadoReserva;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/** Guarda y recupera objetos Reserva en MySQL. */
public class ReservaDAO {

    private static final String CONSULTA_BASE =
            "SELECT id, cliente_id, mesa_id, fecha_hora, numero_personas, estado FROM reserva";

    public List<Reserva> listar() throws SQLException {
        try (Connection conexion = ConexionDB.getConnection()) {
            return listar(conexion);
        }
    }

    List<Reserva> listar(Connection conexion) throws SQLException {
        List<Reserva> registros = new ArrayList<>();
        try (PreparedStatement consulta = conexion.prepareStatement(CONSULTA_BASE + " ORDER BY id");
             ResultSet resultado = consulta.executeQuery()) {
            while (resultado.next()) {
                registros.add(leer(conexion, resultado));
            }
        }
        return registros;
    }

    // Devuelve null si no existe el registro.
    public Reserva buscarPorId(int id) throws SQLException {
        try (Connection conexion = ConexionDB.getConnection()) {
            return buscarPorId(conexion, id);
        }
    }

    // Reutiliza la conexión cuando otra DAO necesita recuperar una relación.
    Reserva buscarPorId(Connection conexion, int id) throws SQLException {
        try (PreparedStatement consulta = conexion.prepareStatement(CONSULTA_BASE + " WHERE id = ?")) {
            consulta.setInt(1, id);
            try (ResultSet resultado = consulta.executeQuery()) {
                return resultado.next() ? leer(conexion, resultado) : null;
            }
        }
    }

    public List<Reserva> listarPorCliente(int id) throws SQLException {
        try (Connection conexion = ConexionDB.getConnection()) {
            return listarPorCliente(conexion, id);
        }
    }

    List<Reserva> listarPorCliente(Connection conexion, int id) throws SQLException {
        List<Reserva> registros = new ArrayList<>();
        String sql = CONSULTA_BASE + " WHERE cliente_id = ? ORDER BY id";
        try (PreparedStatement consulta = conexion.prepareStatement(sql)) {
            consulta.setInt(1, id);
            try (ResultSet resultado = consulta.executeQuery()) {
                while (resultado.next()) {
                    registros.add(leer(conexion, resultado));
                }
            }
        }
        return registros;
    }

    // Asigna al objeto el ID generado por MySQL.
    public boolean guardar(Reserva reserva) throws SQLException {
        String sql = "INSERT INTO reserva (cliente_id, mesa_id, fecha_hora, numero_personas, estado) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (Connection conexion = ConexionDB.getConnection();
             PreparedStatement consulta = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            escribir(consulta, reserva);
            if (consulta.executeUpdate() == 0) {
                return false;
            }
            try (ResultSet claves = consulta.getGeneratedKeys()) {
                if (!claves.next()) {
                    throw new SQLException("No se obtuvo el ID de reserva");
                }
                reserva.setId(claves.getInt(1));
            }
            return true;
        }
    }

    public boolean actualizar(Reserva reserva) throws SQLException {
        String sql = "UPDATE reserva SET cliente_id = ?, mesa_id = ?, fecha_hora = ?, numero_personas = ?, estado = ? WHERE id = ?";
        try (Connection conexion = ConexionDB.getConnection();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {
            escribir(consulta, reserva);
            consulta.setInt(6, reserva.getId());
            return consulta.executeUpdate() > 0;
        }
    }

    // Las claves foráneas impiden borrar registros que todavía se utilizan.
    public boolean eliminar(int id) throws SQLException {
        try (Connection conexion = ConexionDB.getConnection();
             PreparedStatement consulta = conexion.prepareStatement("DELETE FROM reserva WHERE id = ?")) {
            consulta.setInt(1, id);
            return consulta.executeUpdate() > 0;
        }
    }

    private void escribir(PreparedStatement consulta, Reserva reserva) throws SQLException {
        consulta.setObject(1, reserva.getCliente() == null ? null : reserva.getCliente().getId());
        consulta.setObject(2, reserva.getMesa() == null ? null : reserva.getMesa().getId());
        consulta.setObject(3, reserva.getFechaHora());
        consulta.setInt(4, reserva.getNumeroPersonas());
        consulta.setString(5, reserva.getEstado() == null ? null : reserva.getEstado().name());
    }

    private Reserva leer(Connection conexion, ResultSet resultado) throws SQLException {
        Reserva reserva = new Reserva();
        reserva.setId(resultado.getInt("id"));
        reserva.setCliente(resultado.getObject("cliente_id") == null ? null
                : new ClienteDAO().buscarPorId(conexion, resultado.getInt("cliente_id")));
        reserva.setMesa(resultado.getObject("mesa_id") == null ? null
                : new MesaDAO().buscarPorId(conexion, resultado.getInt("mesa_id")));
        reserva.setFechaHora(resultado.getObject("fecha_hora", LocalDateTime.class));
        reserva.setNumeroPersonas(resultado.getInt("numero_personas"));
        reserva.setEstado(EstadoReserva.valueOf(resultado.getString("estado")));
        return reserva;
    }
}
