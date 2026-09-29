package com.garagecoffee.api;

import com.garagecoffee.dao.*;
import com.garagecoffee.database.ConexionDB;
import com.garagecoffee.service.CatalogoService;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;

// Consulta MySQL en cada solicitud y devuelve solo los datos que necesita la app.
@Service
public class ConsultaRestauranteService {
    private final MesaDAO mesas;
    private final ProductoDAO productos;
    private final DetalleMenuDAO menus;
    private final PlatoDelDiaDAO platos;
    public ConsultaRestauranteService(MesaDAO mesas, ProductoDAO productos, DetalleMenuDAO menus, PlatoDelDiaDAO platos) {
        this.mesas = mesas; this.productos = productos; this.menus = menus; this.platos = platos;
    }
    public record MesaRespuesta(int id, int numero, int capacidad, String estado) { }
    public record ProductoRespuesta(int id, String nombre, String descripcion, String imagenUrl,
                                    int categoriaId, String categoria, BigDecimal precioBase, BigDecimal precioVigente) { }
    public List<MesaRespuesta> mesas() throws SQLException {
        return mesas.listar().stream().map(m -> new MesaRespuesta(m.getId(), m.getNumero(), m.getCapacidad(), m.getEstado().name())).toList();
    }
    public List<ProductoRespuesta> catalogo() throws SQLException {
        return ConexionDB.transaccion(() -> {
            var ofertas = menus.listar();
            var especiales = platos.listar();
            LocalDate hoy = LocalDate.now();
            return productos.listar().stream().filter(p -> p.isDisponible() && p.getcategoria().isActivo())
                    .map(p -> new ProductoRespuesta(p.getId(), p.getNombre(), p.getDescripcion(), p.getImagenUrl(),
                            p.getcategoria().getId(), p.getcategoria().getNombre(), p.getPrecio(),
                            CatalogoService.calcularPrecio(p, ofertas, especiales, hoy))).toList();
        });
    }
}
