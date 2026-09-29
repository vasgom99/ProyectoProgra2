package com.garagecoffee.view;

import com.garagecoffee.controller.RestauranteController;
import com.garagecoffee.model.*;
import com.garagecoffee.service.AtencionService;
// Diseño editable en NetBeans: abrir este archivo y elegir Diseño.
public class AtencionPanel extends PanelModulo {

    public AtencionPanel() { initComponents(); }

    private AtencionService servicio;
    public AtencionPanel(RestauranteController control) {
        this();
        servicio = control.atencion();
        boolean admin = control.sesion().permite();
        clientes.configurar(new String[]{"ID", "Cliente", "Teléfono", "Correo"},
                servicio::clientes, c -> new Object[]{c.getId(), c.getNombre(), c.getTelefono(), c.getEmail()});
        clientes.boton("Nuevo", () -> cliente(null)); clientes.accion("Editar", this::cliente);
        clientes.accion("Eliminar", c -> { if (Ui.confirmar(this, "¿Eliminar el cliente y sus direcciones?")) { servicio.eliminarCliente(c); recargar(); } });
        registrar(clientes);
        direcciones.configurar(new String[]{"Cliente", "Dirección", "Referencia"},
                servicio::direcciones, d -> new Object[]{servicio.clientes().stream().filter(c -> c.getId() == d.getClienteId()).map(Cliente::getNombre).findFirst().orElse("—"), d.getDireccion(), d.getReferencia()});
        direcciones.boton("Nueva", () -> direccion(null)); direcciones.accion("Editar", this::direccion);
        direcciones.accion("Eliminar", d -> { if (Ui.confirmar(this, "¿Eliminar la dirección?")) servicio.eliminarDireccion(d); });
        registrar(direcciones);
        mesas.configurar(new String[]{"ID", "Número", "Capacidad", "Estado"},
                servicio::mesas, m -> new Object[]{m.getId(), m.getNumero(), m.getCapacidad(), Ui.nombre(m.getEstado())});
        if (admin) {
            mesas.boton("Nueva", () -> mesa(null)); mesas.accion("Editar", this::mesa);
            mesas.accion("Eliminar", m -> { if (Ui.confirmar(this, "¿Eliminar la mesa?")) servicio.eliminarMesa(m); });
        }
        registrar(mesas);
        if (control.sesion().permite(RolEmpleado.MESERO)) {
            reservas.configurar(new String[]{"ID", "Cliente", "Mesa", "Fecha / hora", "Personas", "Estado"},
                    servicio::reservas, r -> new Object[]{r.getId(), Ui.nombre(r.getCliente()), r.getMesa().getNumero(), Ui.fecha(r.getFechaHora()), r.getNumeroPersonas(), Ui.nombre(r.getEstado())});
            reservas.boton("Nueva", () -> reserva(null)); reservas.accion("Editar", this::reserva);
            reservas.accion("Confirmar", r -> servicio.cambiarReserva(r, EstadoReserva.CONFIRMADA));
            reservas.accion("Finalizar", r -> { if (Ui.confirmar(this, "¿Finalizar la reserva?")) servicio.cambiarReserva(r, EstadoReserva.FINALIZADA); });
            reservas.accion("Cancelar", r -> { if (Ui.confirmar(this, "¿Cancelar la reserva?")) servicio.cambiarReserva(r, EstadoReserva.CANCELADA); });
            registrar(reservas);
        } else { pestanas.remove(reservas); }
        repartidores.configurar(new String[]{"ID", "Repartidor", "Teléfono", "Vehículo", "Activo"},
                servicio::repartidores, r -> new Object[]{r.getId(), r.getNombre(), r.getTelefono(), r.getVehiculo(), r.isActivo()});
        if (admin) { repartidores.boton("Nuevo", () -> repartidor(null)); repartidores.accion("Editar", this::repartidor); }
        registrar(repartidores);
        
    }
    private void cliente(Cliente c) {
        Formulario f = new ClienteForm();
        var nombre = f.texto("Nombre", c == null ? "" : c.getNombre());
        var telefono = f.texto("Teléfono", c == null ? "" : c.getTelefono());
        var email = f.texto("Correo", c == null ? "" : c.getEmail());
        f.guardar(this, "Cliente", () -> { servicio.guardarCliente(c == null ? 0 : c.getId(), f.valor(nombre), f.valor(telefono), f.valor(email)); recargar(); });
    }
    private void direccion(DireccionCliente d) {
        Formulario f = new DireccionForm();
        Cliente actual = d == null ? null : servicio.clientes().stream().filter(c -> c.getId() == d.getClienteId()).findFirst().orElse(null);
        var cliente = f.combo("Cliente", servicio.clientes(), actual);
        var direccion = f.texto("Dirección", d == null ? "" : d.getDireccion());
        var referencia = f.texto("Referencia", d == null ? "" : d.getReferencia());
        f.guardar(this, "Dirección", () -> servicio.guardarDireccion(d == null ? 0 : d.getId(), f.elegido(cliente), f.valor(direccion), f.valor(referencia)));
    }
    private void mesa(Mesa m) {
        Formulario f = new MesaForm();
        var numero = f.texto("Número", m == null ? "" : m.getNumero());
        var capacidad = f.texto("Capacidad", m == null ? "4" : m.getCapacidad());
        var disponible = f.check("En servicio", m == null || m.getEstado() != EstadoMesa.FUERA_SERVICIO);
        f.guardar(this, "Mesa", () -> servicio.guardarMesa(m == null ? 0 : m.getId(), f.entero(numero), f.entero(capacidad), disponible.isSelected()));
    }
    private void reserva(Reserva r) {
        Formulario f = new ReservaForm();
        var cliente = f.combo("Cliente", servicio.clientes(), r == null ? null : r.getCliente());
        var mesa = f.combo("Mesa", servicio.mesas(), r == null ? null : r.getMesa());
        var fecha = f.texto("Fecha / hora AAAA-MM-DD HH:MM", r == null ? Ui.fecha(java.time.LocalDateTime.now().plusDays(1)) : Ui.fecha(r.getFechaHora()));
        var personas = f.texto("Personas", r == null ? "2" : r.getNumeroPersonas());
        f.guardar(this, "Reserva (90 minutos)", () -> servicio.guardarReserva(r == null ? 0 : r.getId(), f.elegido(cliente), f.elegido(mesa), f.fechaHora(fecha), f.entero(personas)));
    }
    private void repartidor(Repartidor r) {
        Formulario f = new RepartidorForm();
        var nombre = f.texto("Nombre", r == null ? "" : r.getNombre());
        var telefono = f.texto("Teléfono", r == null ? "" : r.getTelefono());
        var vehiculo = f.texto("Vehículo", r == null ? "" : r.getVehiculo());
        var activo = f.check("Activo", r == null || r.isActivo());
        f.guardar(this, "Repartidor", () -> servicio.guardarRepartidor(r == null ? 0 : r.getId(), f.valor(nombre), f.valor(telefono), f.valor(vehiculo), activo.isSelected()));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        pestanas = new javax.swing.JTabbedPane();
        clientes = new com.garagecoffee.view.PanelListado<>();
        direcciones = new com.garagecoffee.view.PanelListado<>();
        mesas = new com.garagecoffee.view.PanelListado<>();
        reservas = new com.garagecoffee.view.PanelListado<>();
        repartidores = new com.garagecoffee.view.PanelListado<>();
        lblAyuda = new javax.swing.JLabel();
        this.setBackground(new java.awt.Color(246, 244, 238));
        this.setLayout(new java.awt.BorderLayout(12, 12));
        pestanas.setFont(new java.awt.Font("SansSerif", 0, 14));
        pestanas.setBackground(new java.awt.Color(246, 244, 238));
        pestanas.addTab("Clientes", clientes);
        pestanas.addTab("Direcciones", direcciones);
        pestanas.addTab("Mesas", mesas);
        pestanas.addTab("Reservas", reservas);
        pestanas.addTab("Repartidores", repartidores);
        this.add(pestanas, java.awt.BorderLayout.CENTER);
        lblAyuda.setText("Reservas: intervalos de 90 minutos. Una mesa se libera cuando su pedido está entregado y pagado.");
        lblAyuda.setFont(new java.awt.Font("SansSerif", 0, 12));
        lblAyuda.setForeground(new java.awt.Color(109, 121, 114));
        this.add(lblAyuda, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane pestanas;
    private com.garagecoffee.view.PanelListado<Cliente> clientes;
    private com.garagecoffee.view.PanelListado<DireccionCliente> direcciones;
    private com.garagecoffee.view.PanelListado<Mesa> mesas;
    private com.garagecoffee.view.PanelListado<Reserva> reservas;
    private com.garagecoffee.view.PanelListado<Repartidor> repartidores;
    private javax.swing.JLabel lblAyuda;
    // End of variables declaration//GEN-END:variables
}
