package com.garagecoffee.view;

import com.garagecoffee.controller.RestauranteController;
import com.garagecoffee.model.*;
import java.util.Arrays;
import java.util.List;
import javax.swing.JPasswordField;
public class EmpleadosPanel extends PanelModulo {

    public EmpleadosPanel() { initComponents(); }

    private RestauranteController control;
    public EmpleadosPanel(RestauranteController control) {
        this();
        this.control = control;
        empleados.configurar(new String[]{"ID", "Nombre", "Apellido", "Usuario", "Rol", "Teléfono", "Activo"},
                control.sesion()::listarEmpleados, e -> new Object[]{e.getId(), e.getNombre(), e.getApellido(), e.getNombreUsuario(), Ui.nombre(e.getRol()), e.getTelefono(), e.isActivo()});
        empleados.boton("Nuevo", () -> editar(null)); empleados.accion("Editar / desactivar", this::editar);
        registrar(empleados);
        
    }
    private void editar(Empleado e) {
        Formulario f = new EmpleadoForm();
        var nombre = f.texto("Nombre", e == null ? "" : e.getNombre());
        var apellido = f.texto("Apellido", e == null ? "" : e.getApellido());
        var usuario = f.texto("Usuario", e == null ? "" : e.getNombreUsuario());
        var clave = f.clave("Clave (vacío conserva la anterior)");
        var telefono = f.texto("Teléfono", e == null ? "" : e.getTelefono());
        var email = f.texto("Correo", e == null ? "" : e.getEmail());
        var rol = f.combo("Rol", List.of(RolEmpleado.values()), e == null ? RolEmpleado.MESERO : e.getRol());
        var activo = f.check("Activo", e == null || e.isActivo());
        f.guardar(this, "Empleado", () -> {
            char[] password = clave.getPassword();
            try { control.sesion().guardarEmpleado(e == null ? 0 : e.getId(), f.valor(nombre), f.valor(apellido), f.valor(usuario),
                    new String(password), f.valor(telefono), f.valor(email), f.elegido(rol), activo.isSelected()); }
            finally { Arrays.fill(password, '\0'); }
        });
        clave.setText("");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        pestanas = new javax.swing.JTabbedPane();
        empleados = new com.garagecoffee.view.PanelListado<>();
        this.setBackground(new java.awt.Color(246, 244, 238));
        this.setLayout(new java.awt.BorderLayout(12, 12));
        pestanas.setFont(new java.awt.Font("SansSerif", 0, 14));
        pestanas.setBackground(new java.awt.Color(246, 244, 238));
        pestanas.addTab("Empleados", empleados);
        this.add(pestanas, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane pestanas;
    private com.garagecoffee.view.PanelListado<Empleado> empleados;
    // End of variables declaration//GEN-END:variables
}
