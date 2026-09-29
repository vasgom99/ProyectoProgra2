package com.garagecoffee.service;

import com.garagecoffee.dao.*;
import com.garagecoffee.database.ConexionDB;
import com.garagecoffee.model.*;
import java.sql.SQLException;
import java.math.BigDecimal;
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
            throw new IllegalStateException("No se pudo cargar MySQL. Revisa la conexión y ejecuta PrepararBaseDatos si falta el esquema.", e);
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
                case Cliente r -> r.getId() == 0 ? new ClienteDAO().guardar(r) : new ClienteDAO().actualizar(r);
                case DireccionCliente r -> r.getId() == 0 ? new DireccionClienteDAO().guardar(r) : new DireccionClienteDAO().actualizar(r);
                case Mesa r -> r.getId() == 0 ? new MesaDAO().guardar(r) : new MesaDAO().actualizar(r);
                case Reserva r -> r.getId() == 0 ? new ReservaDAO().guardar(r) : new ReservaDAO().actualizar(r);
                case Repartidor r -> r.getId() == 0 ? new RepartidorDAO().guardar(r) : new RepartidorDAO().actualizar(r);
                case Pedido r -> r.getId() == 0 ? new PedidoDAO().guardar(r) : new PedidoDAO().actualizar(r);
                case Menu r -> r.getId() == 0 ? new MenuDAO().guardar(r) : new MenuDAO().actualizar(r);
                case DetalleMenu r -> r.getId() == 0 ? new DetalleMenuDAO().guardar(r) : new DetalleMenuDAO().actualizar(r);
                case PlatoDelDia r -> r.getId() == 0 ? new PlatoDelDiaDAO().guardar(r) : new PlatoDelDiaDAO().actualizar(r);
                case Ingrediente r -> r.getId() == 0 ? new IngredienteDAO().guardar(r) : new IngredienteDAO().actualizar(r);
                case DetalleReceta r -> r.getId() == 0 ? new DetalleRecetaDAO().guardar(r) : new DetalleRecetaDAO().actualizar(r);
                case MovimientoStock r -> r.getId() == 0 ? new MovimientoStockDAO().guardar(r) : new MovimientoStockDAO().actualizar(r);
                case Perdida r -> r.getId() == 0 ? new PerdidaDAO().guardar(r) : new PerdidaDAO().actualizar(r);
                case Proveedor r -> r.getId() == 0 ? new ProveedorDAO().guardar(r) : new ProveedorDAO().actualizar(r);
                case Compra r -> r.getId() == 0 ? new CompraDAO().guardar(r) : new CompraDAO().actualizar(r);
                case Pago r -> r.getId() == 0 ? new PagoDAO().guardar(r) : new PagoDAO().actualizar(r);
                case Factura r -> r.getId() == 0 ? new FacturaDAO().guardar(r) : new FacturaDAO().actualizar(r);
                case Caja r -> r.getId() == 0 ? new CajaDAO().guardar(r) : new CajaDAO().actualizar(r);
                case MovimientoCaja r -> r.getId() == 0 ? new MovimientoCajaDAO().guardar(r) : new MovimientoCajaDAO().actualizar(r);
                case Gasto r -> r.getId() == 0 ? new GastoDAO().guardar(r) : new GastoDAO().actualizar(r);
                default -> throw new IllegalArgumentException("Registro no soportado.");
            };
            if (!guardado) throw new SQLException("El registro ya no existe.");
        } catch (SQLException e) { throw new IllegalStateException("No se pudo guardar el registro en MySQL.", e); }
    }
    @Override void eliminar(Object registro) {
        escribiendo = true;
        try {
            boolean borrado = switch (registro) {
                case Cliente r -> new ClienteDAO().eliminar(r.getId());
                case DireccionCliente r -> new DireccionClienteDAO().eliminar(r.getId());
                case Mesa r -> new MesaDAO().eliminar(r.getId());
                case Categoria r -> new CategoriaDAO().eliminar(r.getId());
                case Producto r -> new ProductoDAO().eliminar(r.getId());
                case Menu r -> new MenuDAO().eliminar(r.getId());
                case DetalleMenu r -> new DetalleMenuDAO().eliminar(r.getId());
                case PlatoDelDia r -> new PlatoDelDiaDAO().eliminar(r.getId());
                case DetalleReceta r -> new DetalleRecetaDAO().eliminar(r.getId());
                case Ingrediente r -> new IngredienteDAO().eliminar(r.getId());
                case Proveedor r -> new ProveedorDAO().eliminar(r.getId());
                default -> throw new IllegalArgumentException("Este registro no se puede eliminar.");
            };
            if (!borrado) throw new SQLException("El registro ya no existe.");
        } catch (SQLException e) { throw new IllegalStateException("No se puede eliminar: revisa si tiene registros relacionados.", e); }
    }
    @Override void guardarCosto(int pedido, BigDecimal costo) {
        escribiendo = true;
        try { cambios.guardarCosto(pedido, costo); super.guardarCosto(pedido, costo); }
        catch (SQLException e) { throw new IllegalStateException("No se pudo guardar el costo del pedido.", e); }
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
        llenar(clientes, new ClienteDAO().listar());
        llenar(direcciones, new DireccionClienteDAO().listar());
        llenar(mesas, new MesaDAO().listar());
        llenar(reservas, new ReservaDAO().listar());
        llenar(repartidores, new RepartidorDAO().listar());
        llenar(pedidos, new PedidoDAO().listar());
        llenar(menus, new MenuDAO().listar());
        llenar(detallesMenu, new DetalleMenuDAO().listar());
        llenar(platos, new PlatoDelDiaDAO().listar());
        llenar(ingredientes, new IngredienteDAO().listar());
        llenar(recetas, new DetalleRecetaDAO().listar());
        llenar(movimientosStock, new MovimientoStockDAO().listar());
        llenar(perdidas, new PerdidaDAO().listar());
        llenar(proveedores, new ProveedorDAO().listar());
        llenar(compras, new CompraDAO().listar());
        llenar(pagos, new PagoDAO().listar());
        llenar(facturas, new FacturaDAO().listar());
        llenar(cajas, new CajaDAO().listar());
        llenar(movimientosCaja, new MovimientoCajaDAO().listar());
        llenar(gastos, new GastoDAO().listar());
        for (Producto p : productos) p.setCategoria(buscar(categorias, Categoria::getId, p.getcategoria().getId()));
        direcciones.clear();
        for (Cliente c : clientes) direcciones.addAll(c.getDirecciones());
        for (Reserva r : reservas) {
            r.setCliente(buscar(clientes, Cliente::getId, r.getCliente().getId()));
            r.setMesa(buscar(mesas, Mesa::getId, r.getMesa().getId()));
        }
        for (DetalleMenu d : detallesMenu) {
            d.setMenu(buscar(menus, Menu::getId, d.getMenu().getId()));
            d.setProducto(buscar(productos, Producto::getId, d.getProducto().getId()));
        }
        for (PlatoDelDia p : platos) p.setProducto(buscar(productos, Producto::getId, p.getProducto().getId()));
        for (DetalleReceta r : recetas) {
            r.setProducto(buscar(productos, Producto::getId, r.getProducto().getId()));
            r.setIngrediente(buscar(ingredientes, Ingrediente::getId, r.getIngrediente().getId()));
        }
        for (MovimientoStock m : movimientosStock) m.setIngrediente(buscar(ingredientes, Ingrediente::getId, m.getIngrediente().getId()));
        for (Perdida p : perdidas) p.setIngrediente(buscar(ingredientes, Ingrediente::getId, p.getIngrediente().getId()));
        for (Compra c : compras) {
            c.setProveedor(buscar(proveedores, Proveedor::getId, c.getProveedor().getId()));
            for (DetalleCompra d : c.getDetalles()) d.setIngrediente(buscar(ingredientes, Ingrediente::getId, d.getIngrediente().getId()));
        }
        for (Pedido p : pedidos) {
            if (p.getCliente() != null) p.setCliente(buscar(clientes, Cliente::getId, p.getCliente().getId()));
            if (p instanceof PedidoLocal l && l.getMesa() != null) l.setMesa(buscar(mesas, Mesa::getId, l.getMesa().getId()));
            if (p instanceof PedidoDomicilio d && d.getRepartidor() != null) d.setRepartidor(buscar(repartidores, Repartidor::getId, d.getRepartidor().getId()));
            for (DetallePedido d : p.getDetalles()) d.setProducto(buscar(productos, Producto::getId, d.getProducto().getId()));
        }
        for (Pago p : pagos) p.setPedido(buscar(pedidos, Pedido::getId, p.getPedido().getId()));
        for (Factura f : facturas) f.setPedido(buscar(pedidos, Pedido::getId, f.getPedido().getId()));
        for (MovimientoCaja m : movimientosCaja) m.setCaja(buscar(cajas, Caja::getId, m.getCaja().getId()));
        costosPedidos.clear(); costosPedidos.putAll(cambios.costos());
    }
}
