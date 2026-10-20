package com.garagecoffee.service;

import com.garagecoffee.model.Empleado;
import com.garagecoffee.model.RolEmpleado;
import java.util.Arrays;
import java.util.List;

// controla el acceso y los permisos.
public class SesionService {
    private final DatosRestaurante datos;
    private Integer empleadoId;

    public SesionService(DatosRestaurante datos) { this.datos = datos; }

    public Empleado iniciarSesion(String usuario, char[] clave) {
        empleadoId = null;
        try {
            datos.recargar();
            Validacion.exigir(usuario != null && clave != null, "Indica usuario y contraseña.");
            Empleado empleado = datos.empleados.stream()
                    .filter(e -> e.isActivo() && e.getNombreUsuario().equalsIgnoreCase(usuario.trim()))
                    .findFirst().orElse(null);
            Validacion.exigir(empleado != null && Claves.verificar(clave, empleado.getPassword()),
                    "Usuario o contraseña incorrectos.");
            if (!Claves.protegida(empleado.getPassword())) {
                datos.transaccion(() -> { empleado.setPassword(Claves.proteger(clave)); datos.guardar(empleado); return null; });
            }
            empleadoId = empleado.getId();
            return empleado;
        } finally {
            if (clave != null) Arrays.fill(clave, '\0');
        }
    }

    public void cerrarSesion() { empleadoId = null; }

    public Empleado actual() {
        Empleado empleado = datos.empleados.stream()
                .filter(e -> empleadoId != null && e.getId() == empleadoId && e.isActivo()).findFirst().orElse(null);
        if (empleado == null) throw new IllegalStateException("Inicia sesión para continuar.");
        return empleado;
    }

    public boolean permite(RolEmpleado... roles) {
        RolEmpleado rol = actual().getRol();
        return rol == RolEmpleado.ADMINISTRADOR || Arrays.asList(roles).contains(rol);
    }

    public void exigir(RolEmpleado... roles) {
        if (!permite(roles)) throw new IllegalArgumentException("Tu usuario no tiene permiso para esta operación.");
    }

    public List<Empleado> listarEmpleados() { exigir(); return List.copyOf(datos.empleados); }

    public Empleado guardarEmpleado(int id, String nombre, String apellido, String usuario,
                                    String clave, String telefono, String email, RolEmpleado rol, boolean activo) {
        return datos.transaccion(() -> guardarEmpleadoInterno(id, nombre, apellido, usuario, clave, telefono, email, rol, activo));
    }
    private Empleado guardarEmpleadoInterno(int id, String nombre, String apellido, String usuario,
                                    String clave, String telefono, String email, RolEmpleado rol, boolean activo) {
        exigir();
        nombre = Validacion.texto(nombre, "Nombre", 100);
        apellido = Validacion.texto(apellido, "Apellido", 100);
        usuario = Validacion.texto(usuario, "Usuario", 80);
        telefono = Validacion.opcional(telefono, "Teléfono", 30);
        email = Validacion.opcional(email, "Correo", 150);
        Validacion.exigir(rol != null, "Selecciona un rol.");
        for (Empleado otro : datos.empleados) {
            Validacion.exigir(otro.getId() == id || !otro.getNombreUsuario().equalsIgnoreCase(usuario),
                    "Ese usuario ya existe.");
        }
        Empleado existente = id == 0 ? null : datos.empleados.stream().filter(e -> e.getId() == id).findFirst().orElse(null);
        Validacion.exigir(id == 0 || existente != null, "El empleado ya no existe.");
        if (id == actual().getId()) {
            Validacion.exigir(activo && rol == RolEmpleado.ADMINISTRADOR,
                    "No puedes desactivar tu propia cuenta ni quitarte el rol de administrador.");
        }
        String password;
        if ((clave == null || clave.isBlank()) && existente != null) password = existente.getPassword();
        else {
            Validacion.exigir(clave != null && clave.length() >= 4 && clave.length() <= 255, "La clave debe tener entre 4 y 255 caracteres.");
            password = Claves.proteger(clave.toCharArray());
        }
        Empleado empleado = existente == null ? new Empleado() : existente;
        if (existente == null) empleado.setId(datos.siguienteId(Empleado.class));
        empleado.setNombre(nombre); empleado.setApellido(apellido); empleado.setNombreUsuario(usuario);
        empleado.setPassword(password); empleado.setTelefono(telefono); empleado.setEmail(email);
        empleado.setRol(rol); empleado.setActivo(activo);
        datos.guardar(empleado);
        if (existente == null) datos.empleados.add(empleado);
        return empleado;
    }
}
