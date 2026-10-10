package com.garagecoffee.service;

import com.garagecoffee.dao.*;
import com.garagecoffee.database.ConexionDB;
import com.garagecoffee.model.*;
import java.sql.SQLException;
import java.util.List;
import java.util.function.*;

// Usa las DAO existentes; agrupa cada operación del servicio en una sola transacción.
public class DatosMySQL extends DatosRestaurante {
    private final ControlCambiosDAO cambios = new ControlCambiosDAO();
    private long revision;
    private boolean escribiendo;
    private boolean disponible;
    public DatosMySQL() { recargar(); }
    @Override public boolean esDemo() { return false; }
    // MySQL asigna los IDs; cero indica un registro nuevo.
    @Override int siguienteId(Class<?> tipo) { return 0; }
    @Override public void recargar() {
        disponible = false;
        try {
            ConexionDB.transaccion(() -> { cargar(); return null; });
            disponible = true;
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudo cargar MySQL. Revisa la conexión y ejecuta garage_coffee.sql si falta el esquema.", e);
        }
    }
    @Override <T> T transaccion(Supplier<T> accion) {
        if (ConexionDB.enTransaccion()) return accion.get();
        if (!disponible) throw new IllegalStateException("Actualiza los datos antes de continuar.");
        escribiendo = false;
        try {
            T resultado = ConexionDB.transaccion(() -> {
                long actual = cambios.revision(true);
                if (actual != revision) throw new SQLException("Otra sesión modificó los datos. Pulsa Actualizar y vuelve a abrir el formulario.");
                T valor = accion.get();
                cambios.avanzar();
                return valor;
            });
            revision++;
            return resultado;
        } catch (SQLException e) {
            recuperar();
            throw new IllegalStateException("No se guardó la operación. Actualiza y vuelve a abrir el formulario. " + e.getMessage(), e);
        } catch (RuntimeException e) {
            if (escribiendo) {
                recuperar();
                throw new IllegalStateException("No se guardó la operación. Cierra y vuelve a abrir el formulario. " + e.getMessage(), e);
            }
            throw e;
        }
    }
    private void recuperar() {
        try { recargar(); } catch (RuntimeException e) { disponible = false; }
    }
    @Override void guardar(Object registro) {
        escribiendo = true;
        try {
            boolean guardado = switch (registro) {
                case Empleado r -> r.getId() == 0 ? new EmpleadoDAO().guardar(r) : new EmpleadoDAO().actualizar(r);
                case Categoria r -> r.getId() == 0 ? new CategoriaDAO().guardar(r) : new CategoriaDAO().actualizar(r);
                case Producto r -> r.getId() == 0 ? new ProductoDAO().guardar(r) : new ProductoDAO().actualizar(r);
                case Mesa r -> r.getId() == 0 ? new MesaDAO().guardar(r) : new MesaDAO().actualizar(r);
                case Pedido r -> r.getId() == 0 ? new PedidoDAO().guardar(r) : new PedidoDAO().actualizar(r);
                case Pago r -> r.getId() == 0 && new PagoDAO().guardar(r);
                case Caja r -> r.getId() == 0 ? new CajaDAO().guardar(r) : new CajaDAO().actualizar(r);
                default -> throw new IllegalArgumentException("Registro no soportado.");
            };
            if (!guardado) throw new SQLException("El registro ya no existe.");
        } catch (SQLException e) { throw new IllegalStateException("No se pudo guardar el registro en MySQL.", e); }
    }
    @Override void eliminar(Object registro) {
        escribiendo = true;
        try {
            boolean borrado = switch (registro) {
                case Mesa r -> new MesaDAO().eliminar(r.getId());
                case Categoria r -> new CategoriaDAO().eliminar(r.getId());
                case Producto r -> new ProductoDAO().eliminar(r.getId());
                default -> throw new IllegalArgumentException("Este registro no se puede eliminar.");
            };
            if (!borrado) throw new SQLException("El registro ya no existe.");
        } catch (SQLException e) { throw new IllegalStateException("No se puede eliminar: revisa si tiene registros relacionados.", e); }
    }
    private <T> void llenar(List<T> destino, List<T> origen) { destino.clear(); destino.addAll(origen); }
    private <T> T buscar(List<T> lista, ToIntFunction<T> id, int buscado) {
        return lista.stream().filter(x -> id.applyAsInt(x) == buscado).findFirst()
                .orElseThrow(() -> new IllegalStateException("Relación de datos inválida: " + buscado));
    }
    // Reutiliza una sola instancia por entidad para conservar sus relaciones POO.
    private void cargar() throws SQLException {
        revision = cambios.revision(false);
        llenar(empleados, new EmpleadoDAO().listar());
        llenar(categorias, new CategoriaDAO().listar());
        llenar(productos, new ProductoDAO().listar());
        llenar(mesas, new MesaDAO().listar());
        llenar(pedidos, new PedidoDAO().listar());
        llenar(pagos, new PagoDAO().listar());
        llenar(cajas, new CajaDAO().listar());
        for (Producto p : productos) p.setCategoria(buscar(categorias, Categoria::getId, p.getcategoria().getId()));
        for (Pedido p : pedidos) {
            if (p instanceof PedidoLocal l && l.getMesa() != null) l.setMesa(buscar(mesas, Mesa::getId, l.getMesa().getId()));
            for (DetallePedido d : p.getDetalles()) d.setProducto(buscar(productos, Producto::getId, d.getProducto().getId()));
        }
        for (Pago p : pagos) p.setPedido(buscar(pedidos, Pedido::getId, p.getPedido().getId()));
        for (Pago p : pagos) p.setCaja(buscar(cajas, Caja::getId, p.getCaja().getId()));
    }
}
