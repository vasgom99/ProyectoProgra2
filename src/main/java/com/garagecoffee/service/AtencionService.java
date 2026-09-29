package com.garagecoffee.service;

import com.garagecoffee.model.*;
import java.time.LocalDateTime;
import java.util.List;

// Organiza clientes, mesas, reservas y repartidores.
public class AtencionService {
    private final DatosRestaurante datos;
    private final SesionService sesion;
    public AtencionService(DatosRestaurante datos, SesionService sesion) { this.datos = datos; this.sesion = sesion; }
    public List<Cliente> clientes() { sesion.exigir(RolEmpleado.MESERO, RolEmpleado.CAJERO); return List.copyOf(datos.clientes); }
    public List<DireccionCliente> direcciones() { sesion.exigir(RolEmpleado.MESERO, RolEmpleado.CAJERO); return List.copyOf(datos.direcciones); }
    public List<Mesa> mesas() { sesion.actual(); return List.copyOf(datos.mesas); }
    public List<Reserva> reservas() { sesion.exigir(RolEmpleado.MESERO); return List.copyOf(datos.reservas); }
    public List<Repartidor> repartidores() { sesion.actual(); return List.copyOf(datos.repartidores); }

    public void guardarCliente(int id, String nombre, String telefono, String email) {
        datos.transaccion(() -> { guardarClienteInterno(id, nombre, telefono, email); return null; });
    }
    private void guardarClienteInterno(int id, String nombre, String telefono, String email) {
        sesion.exigir(RolEmpleado.MESERO, RolEmpleado.CAJERO);
        nombre = Validacion.texto(nombre, "Nombre", 150);
        telefono = Validacion.opcional(telefono, "Teléfono", 30);
        email = Validacion.opcional(email, "Correo", 150);
        Cliente cliente = id == 0 ? new Cliente() : datos.clientes.stream().filter(c -> c.getId() == id).findFirst().orElseThrow();
        if (id == 0) { cliente.setId(datos.siguienteId(Cliente.class)); cliente.setFechaRegistro(LocalDateTime.now()); }
        cliente.setNombre(nombre); cliente.setTelefono(telefono); cliente.setEmail(email);
        datos.guardar(cliente);
        if (id == 0) datos.clientes.add(cliente);
    }

    public void eliminarCliente(Cliente cliente) {
        datos.transaccion(() -> { eliminarClienteInterno(cliente); return null; });
    }
    private void eliminarClienteInterno(Cliente cliente) {
        sesion.exigir(RolEmpleado.MESERO, RolEmpleado.CAJERO);
        Validacion.exigir(datos.pedidos.stream().noneMatch(p -> p.getCliente() == cliente)
                && datos.reservas.stream().noneMatch(r -> r.getCliente() == cliente), "El cliente tiene pedidos o reservas.");
        for (DireccionCliente d : datos.direcciones.stream().filter(d -> d.getClienteId() == cliente.getId()).toList()) datos.eliminar(d);
        datos.direcciones.removeIf(d -> d.getClienteId() == cliente.getId());
        datos.eliminar(cliente); datos.clientes.remove(cliente);
    }

    public void guardarDireccion(int id, Cliente cliente, String direccion, String referencia) {
        datos.transaccion(() -> { guardarDireccionInterno(id, cliente, direccion, referencia); return null; });
    }
    private void guardarDireccionInterno(int id, Cliente cliente, String direccion, String referencia) {
        sesion.exigir(RolEmpleado.MESERO, RolEmpleado.CAJERO);
        Validacion.exigir(datos.clientes.contains(cliente), "Selecciona un cliente.");
        direccion = Validacion.texto(direccion, "Dirección", 255);
        referencia = Validacion.opcional(referencia, "Referencia", 255);
        DireccionCliente detalle = id == 0 ? new DireccionCliente() : datos.direcciones.stream().filter(d -> d.getId() == id).findFirst().orElseThrow();
        if (id == 0) detalle.setId(datos.siguienteId(DireccionCliente.class));
        else datos.clientes.stream().filter(c -> c.getId() == detalle.getClienteId()).forEach(c -> c.eliminarDireccion(detalle));
        detalle.setClienteId(cliente.getId()); detalle.setDireccion(direccion); detalle.setReferencia(referencia);
        cliente.agregarDireccion(detalle);
        datos.guardar(detalle);
        if (id == 0) datos.direcciones.add(detalle);
    }

    public void eliminarDireccion(DireccionCliente direccion) {
        datos.transaccion(() -> { eliminarDireccionInterno(direccion); return null; });
    }
    private void eliminarDireccionInterno(DireccionCliente direccion) {
        sesion.exigir(RolEmpleado.MESERO, RolEmpleado.CAJERO);
        datos.clientes.stream().filter(c -> c.getId() == direccion.getClienteId()).forEach(c -> c.eliminarDireccion(direccion));
        datos.eliminar(direccion); datos.direcciones.remove(direccion);
    }

    public void guardarMesa(int id, int numero, int capacidad, boolean disponible) {
        datos.transaccion(() -> { guardarMesaInterno(id, numero, capacidad, disponible); return null; });
    }
    private void guardarMesaInterno(int id, int numero, int capacidad, boolean disponible) {
        sesion.exigir();
        Validacion.exigir(numero > 0 && capacidad > 0, "Número y capacidad deben ser mayores que cero.");
        Validacion.exigir(datos.mesas.stream().noneMatch(m -> m.getId() != id && m.getNumero() == numero), "Ese número de mesa ya existe.");
        Mesa mesa = id == 0 ? new Mesa() : datos.mesas.stream().filter(m -> m.getId() == id).findFirst().orElseThrow();
        Validacion.exigir(id == 0 || mesa.getEstado() != EstadoMesa.OCUPADA, "No se puede modificar una mesa ocupada.");
        Validacion.exigir(datos.reservas.stream().noneMatch(r -> r.getMesa() == mesa && vigente(r)
                && (!disponible || r.getNumeroPersonas() > capacidad)), "La mesa tiene reservas que requieren su disponibilidad o capacidad actual.");
        if (id == 0) mesa.setId(datos.siguienteId(Mesa.class));
        mesa.setNumero(numero); mesa.setCapacidad(capacidad);
        mesa.setEstado(disponible ? EstadoMesa.LIBRE : EstadoMesa.FUERA_SERVICIO);
        datos.guardar(mesa);
        if (id == 0) datos.mesas.add(mesa);
    }

    public void eliminarMesa(Mesa mesa) {
        datos.transaccion(() -> { eliminarMesaInterno(mesa); return null; });
    }
    private void eliminarMesaInterno(Mesa mesa) {
        sesion.exigir();
        Validacion.exigir(datos.pedidos.stream().noneMatch(p -> p instanceof PedidoLocal local && local.getMesa() == mesa)
                && datos.reservas.stream().noneMatch(r -> r.getMesa() == mesa), "La mesa tiene historial; márcala fuera de servicio.");
        datos.eliminar(mesa); datos.mesas.remove(mesa);
    }

    public void guardarReserva(int id, Cliente cliente, Mesa mesa, LocalDateTime fecha, int personas) {
        datos.transaccion(() -> { guardarReservaInterno(id, cliente, mesa, fecha, personas); return null; });
    }
    private void guardarReservaInterno(int id, Cliente cliente, Mesa mesa, LocalDateTime fecha, int personas) {
        sesion.exigir(RolEmpleado.MESERO);
        Validacion.exigir(datos.clientes.contains(cliente) && datos.mesas.contains(mesa), "Selecciona cliente y mesa.");
        Validacion.exigir(mesa.getEstado() != EstadoMesa.FUERA_SERVICIO, "La mesa está fuera de servicio.");
        Validacion.exigir(fecha != null && fecha.isAfter(LocalDateTime.now()), "La reserva debe ser para una fecha futura.");
        Validacion.exigir(personas > 0 && personas <= mesa.getCapacidad(), "La cantidad de personas supera la capacidad o no es válida.");
        // Cada reserva ocupa un intervalo de 90 minutos.
        Validacion.exigir(datos.reservas.stream().noneMatch(r -> r.getId() != id && r.getMesa() == mesa && vigente(r)
                && fecha.isBefore(r.getFechaHora().plusMinutes(90)) && fecha.plusMinutes(90).isAfter(r.getFechaHora())),
                "La mesa ya tiene una reserva en ese intervalo de 90 minutos.");
        Reserva reserva = id == 0 ? new Reserva() : datos.reservas.stream().filter(r -> r.getId() == id).findFirst().orElseThrow();
        Validacion.exigir(id == 0 || vigente(reserva), "Una reserva finalizada o cancelada no se puede editar.");
        if (id == 0) { reserva.setId(datos.siguienteId(Reserva.class)); reserva.setEstado(EstadoReserva.PENDIENTE); }
        reserva.setCliente(cliente); reserva.setMesa(mesa); reserva.setFechaHora(fecha); reserva.setNumeroPersonas(personas);
        datos.guardar(reserva);
        if (id == 0) datos.reservas.add(reserva);
    }

    public void cambiarReserva(Reserva reserva, EstadoReserva estado) {
        datos.transaccion(() -> { cambiarReservaInterno(reserva, estado); return null; });
    }
    private void cambiarReservaInterno(Reserva reserva, EstadoReserva estado) {
        sesion.exigir(RolEmpleado.MESERO);
        Validacion.exigir(datos.reservas.contains(reserva) && vigente(reserva), "La reserva ya terminó.");
        Validacion.exigir(estado == EstadoReserva.CONFIRMADA || estado == EstadoReserva.CANCELADA || estado == EstadoReserva.FINALIZADA,
                "Cambio de estado no permitido.");
        Validacion.exigir(estado != EstadoReserva.FINALIZADA || !reserva.getFechaHora().isAfter(LocalDateTime.now()),
                "Todavía no llega la hora de la reserva.");
        reserva.setEstado(estado); datos.guardar(reserva);
        // Una reserva futura no cambia el estado físico actual de la mesa.
    }

    static boolean vigente(Reserva reserva) {
        return reserva.getEstado() == EstadoReserva.PENDIENTE || reserva.getEstado() == EstadoReserva.CONFIRMADA;
    }

    public void guardarRepartidor(int id, String nombre, String telefono, String vehiculo, boolean activo) {
        datos.transaccion(() -> { guardarRepartidorInterno(id, nombre, telefono, vehiculo, activo); return null; });
    }
    private void guardarRepartidorInterno(int id, String nombre, String telefono, String vehiculo, boolean activo) {
        sesion.exigir();
        nombre = Validacion.texto(nombre, "Nombre", 150);
        telefono = Validacion.opcional(telefono, "Teléfono", 30);
        vehiculo = Validacion.opcional(vehiculo, "Vehículo", 100);
        Repartidor repartidor = id == 0 ? new Repartidor() : datos.repartidores.stream().filter(r -> r.getId() == id).findFirst().orElseThrow();
        Validacion.exigir(activo || datos.pedidos.stream().noneMatch(p -> p instanceof PedidoDomicilio d && d.getRepartidor() == repartidor
                && p.getEstado() != EstadoPedido.ENTREGADO && p.getEstado() != EstadoPedido.CANCELADO), "El repartidor todavía tiene pedidos activos.");
        if (id == 0) repartidor.setId(datos.siguienteId(Repartidor.class));
        repartidor.setNombre(nombre); repartidor.setTelefono(telefono); repartidor.setVehiculo(vehiculo); repartidor.setActivo(activo);
        datos.guardar(repartidor);
        if (id == 0) datos.repartidores.add(repartidor);
    }
}
