package com.garagecoffee.view;

import com.garagecoffee.controller.RestauranteController;
import com.garagecoffee.model.*;
import com.garagecoffee.service.InventarioService;
import java.util.List;
// Diseño editable en NetBeans: abrir este archivo y elegir Diseño.
public class InventarioPanel extends PanelModulo {

    public InventarioPanel() { initComponents(); }

    private InventarioService servicio;
    public InventarioPanel(RestauranteController control) {
        this();
        servicio = control.inventario();
        boolean admin = control.sesion().permite();
        ingredientes.configurar(new String[]{"ID", "Ingrediente", "Unidad", "Stock", "Mínimo", "Costo unitario Q", "Alerta"},
                servicio::ingredientes, i -> new Object[]{i.getId(), i.getNombre(), i.getUnidadMedida(), i.getStockActual(), i.getStockMinimo(), i.getCostoUnitario(), i.verificarAlertaStock() ? "STOCK BAJO" : ""});
        if (admin) {
            ingredientes.boton("Nuevo", () -> ingrediente(null)); ingredientes.accion("Editar", this::ingrediente);
            ingredientes.accion("Movimiento / merma", this::movimiento);
            ingredientes.accion("Eliminar", i -> { if (Ui.confirmar(this, "¿Eliminar el ingrediente?")) servicio.eliminarIngrediente(i); });
        }
        registrar(ingredientes);
        movimientos.configurar(new String[]{"Fecha", "Ingrediente", "Tipo", "Cantidad", "Costo Q", "Motivo"},
                servicio::movimientos, m -> new Object[]{Ui.fecha(m.getFechaHora()), Ui.nombre(m.getIngrediente()), Ui.nombre(m.getTipo()), m.getCantidad(), m.getCostoTotal(), m.getMotivo()});
        registrar(movimientos);
        perdidas.configurar(new String[]{"Fecha", "Ingrediente", "Cantidad", "Motivo"},
                servicio::perdidas, p -> new Object[]{Ui.fecha(p.getFechaHora()), Ui.nombre(p.getIngrediente()), p.getCantidad(), p.getMotivo()});
        registrar(perdidas);
        if (admin) {
            proveedores.configurar(new String[]{"ID", "Proveedor", "Teléfono", "Correo", "Dirección", "Activo"},
                    servicio::proveedores, p -> new Object[]{p.getId(), p.getNombre(), p.getTelefono(), p.getEmail(), p.getDireccion(), p.isActivo()});
            proveedores.boton("Nuevo", () -> proveedor(null)); proveedores.accion("Editar", this::proveedor);
            proveedores.accion("Eliminar", p -> { if (Ui.confirmar(this, "¿Eliminar el proveedor?")) servicio.eliminarProveedor(p); });
            registrar(proveedores);
        } else { pestanas.remove(proveedores); }
        
    }
    private void ingrediente(Ingrediente i) {
        Formulario f = new IngredienteForm();
        var nombre = f.texto("Nombre", i == null ? "" : i.getNombre());
        var unidad = f.texto("Unidad (g, ml, unidad…)", i == null ? "" : i.getUnidadMedida());
        var minimo = f.texto("Stock mínimo", i == null ? "0" : i.getStockMinimo());
        var costo = f.texto("Costo por unidad Q", i == null ? "0" : i.getCostoUnitario());
        f.guardar(this, "Ingrediente (stock inicial 0)", () -> servicio.guardarIngrediente(i == null ? 0 : i.getId(), f.valor(nombre), f.valor(unidad), f.decimal(minimo), f.decimal(costo)));
    }
    private void movimiento(Ingrediente i) {
        Formulario f = new MovimientoStockForm();
        var tipo = f.combo("Tipo", List.of(TipoMovimientoStock.values()), TipoMovimientoStock.ENTRADA);
        var cantidad = f.texto("Cantidad", "");
        var motivo = f.texto("Motivo", "");
        f.campo("Importante", Ui.aviso("<html>AJUSTE: escribe el stock final contado.<br>MERMA: descuenta y registra una pérdida.</html>"));
        f.guardar(this, Ui.nombre(i), () -> { servicio.registrarMovimiento(i, f.elegido(tipo), f.decimal(cantidad), f.valor(motivo)); recargar(); });
    }
    private void proveedor(Proveedor p) {
        Formulario f = new ProveedorForm();
        var nombre = f.texto("Nombre", p == null ? "" : p.getNombre());
        var telefono = f.texto("Teléfono", p == null ? "" : p.getTelefono());
        var email = f.texto("Correo", p == null ? "" : p.getEmail());
        var direccion = f.texto("Dirección", p == null ? "" : p.getDireccion());
        var activo = f.check("Activo", p == null || p.isActivo());
        f.guardar(this, "Proveedor", () -> servicio.guardarProveedor(p == null ? 0 : p.getId(), f.valor(nombre), f.valor(telefono), f.valor(email), f.valor(direccion), activo.isSelected()));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        pestanas = new javax.swing.JTabbedPane();
        ingredientes = new com.garagecoffee.view.PanelListado<>();
        movimientos = new com.garagecoffee.view.PanelListado<>();
        perdidas = new com.garagecoffee.view.PanelListado<>();
        proveedores = new com.garagecoffee.view.PanelListado<>();
        lblAyuda = new javax.swing.JLabel();
        this.setBackground(new java.awt.Color(246, 244, 238));
        this.setLayout(new java.awt.BorderLayout(12, 12));
        pestanas.setFont(new java.awt.Font("SansSerif", 0, 14));
        pestanas.setBackground(new java.awt.Color(246, 244, 238));
        pestanas.addTab("Existencias", ingredientes);
        pestanas.addTab("Movimientos", movimientos);
        pestanas.addTab("Pérdidas", perdidas);
        pestanas.addTab("Proveedores", proveedores);
        this.add(pestanas, java.awt.BorderLayout.CENTER);
        lblAyuda.setText("Las recetas descuentan stock al iniciar preparación; las compras lo aumentan al recibirse.");
        lblAyuda.setFont(new java.awt.Font("SansSerif", 0, 12));
        lblAyuda.setForeground(new java.awt.Color(109, 121, 114));
        this.add(lblAyuda, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane pestanas;
    private com.garagecoffee.view.PanelListado<Ingrediente> ingredientes;
    private com.garagecoffee.view.PanelListado<MovimientoStock> movimientos;
    private com.garagecoffee.view.PanelListado<Perdida> perdidas;
    private com.garagecoffee.view.PanelListado<Proveedor> proveedores;
    private javax.swing.JLabel lblAyuda;
    // End of variables declaration//GEN-END:variables
}
