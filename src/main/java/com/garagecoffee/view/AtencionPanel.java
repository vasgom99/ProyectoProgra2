package com.garagecoffee.view;

import com.garagecoffee.controller.RestauranteController;
import com.garagecoffee.model.EstadoMesa;
import com.garagecoffee.model.Mesa;
import com.garagecoffee.service.AtencionService;

public class AtencionPanel extends PanelModulo {
    private AtencionService servicio;

    public AtencionPanel() { initComponents(); }

    public AtencionPanel(RestauranteController control) {
        this();
        servicio = control.atencion();
        mesas.configurar(new String[]{"ID", "Número", "Capacidad", "Estado"},
                servicio::mesas, m -> new Object[]{m.getId(), m.getNumero(), m.getCapacidad(), Ui.nombre(m.getEstado())});
        if (control.sesion().permite()) {
            mesas.boton("Nueva", () -> editarMesa(null));
            mesas.accion("Editar", this::editarMesa);
            mesas.accion("Eliminar", servicio::eliminarMesa);
        }
        mesas.boton("Tomar pedido", () -> {
            Mesa elegida = mesas.seleccionado();
            if (elegida.getEstado() != EstadoMesa.LIBRE)
                throw new IllegalArgumentException("Selecciona una mesa libre.");
            new PedidoDialog(javax.swing.SwingUtilities.getWindowAncestor(this), control, elegida).setVisible(true);
        });
        registrar(mesas);
    }

    private void editarMesa(Mesa mesa) {
        Formulario formulario = new MesaForm();
        var numero = formulario.texto("Número", mesa == null ? "" : mesa.getNumero());
        var capacidad = formulario.texto("Capacidad", mesa == null ? "4" : mesa.getCapacidad());
        var disponible = formulario.check("En servicio", mesa == null || mesa.getEstado() != EstadoMesa.FUERA_SERVICIO);
        formulario.guardar(this, "Mesa", () -> servicio.guardarMesa(mesa == null ? 0 : mesa.getId(),
                formulario.entero(numero), formulario.entero(capacidad), disponible.isSelected()));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        pestanas = new javax.swing.JTabbedPane();
        mesas = new com.garagecoffee.view.PanelListado<>();
        this.setBackground(new java.awt.Color(246, 244, 238));
        this.setLayout(new java.awt.BorderLayout(12, 12));
        pestanas.setFont(new java.awt.Font("SansSerif", 0, 14));
        pestanas.setBackground(new java.awt.Color(246, 244, 238));
        pestanas.addTab("Mesas", mesas);
        this.add(pestanas, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane pestanas;
    private com.garagecoffee.view.PanelListado<Mesa> mesas;
    // End of variables declaration//GEN-END:variables
}
