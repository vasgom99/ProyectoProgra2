package com.garagecoffee.api;

import java.sql.SQLException;
import java.util.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

// publica las rutas de la app.
@RestController
@RequestMapping("/api")
public class RestauranteApi {
    private final ConsultaRestauranteService consultas;
    private final OperacionRestauranteService operaciones;
    public RestauranteApi(ConsultaRestauranteService consultas, OperacionRestauranteService operaciones) {
        this.consultas = consultas;
        this.operaciones = operaciones;
    }
    @GetMapping("/estado") public Map<String, String> estado() { return Map.of("servidor", "disponible"); }
    @GetMapping("/sesion") public Map<String, Object> sesion(Authentication usuario) {
        return Map.of("usuario", usuario.getName(), "roles",
                usuario.getAuthorities().stream().map(a -> a.getAuthority()).filter(a -> a.startsWith("ROLE_"))
                        .map(a -> a.substring(5)).toList());
    }
    @GetMapping("/mesas") public List<ConsultaRestauranteService.MesaRespuesta> mesas() throws SQLException { return consultas.mesas(); }
    @GetMapping("/catalogo") public List<ConsultaRestauranteService.ProductoRespuesta> catalogo() throws SQLException { return consultas.catalogo(); }
    @GetMapping("/mesas/{id}/pedido-activo")
    public OperacionRestauranteService.PedidoMesaRespuesta pedidoActivo(@PathVariable int id) throws SQLException {
        return operaciones.pedidoActivo(id);
    }
    @PostMapping("/pedidos")
    @ResponseStatus(org.springframework.http.HttpStatus.CREATED)
    public OperacionRestauranteService.PedidoRespuesta crearPedido(
            @RequestBody OperacionRestauranteService.PedidoEntrada pedido) throws SQLException {
        return operaciones.crearPedido(pedido);
    }
    @PutMapping("/mesas/{id}/estado")
    public ConsultaRestauranteService.MesaRespuesta cambiarEstado(
            @PathVariable int id, @RequestBody OperacionRestauranteService.EstadoMesaEntrada estado) throws SQLException {
        return operaciones.cambiarEstadoMesa(id, estado);
    }
    @PutMapping("/pedidos/{id}/entregar")
    public OperacionRestauranteService.PedidoMesaRespuesta entregar(@PathVariable int id) throws SQLException {
        return operaciones.entregarPedido(id);
    }
    @PutMapping("/pedidos/{id}/productos")
    public OperacionRestauranteService.PedidoMesaRespuesta agregarProductos(
            @PathVariable int id, @RequestBody OperacionRestauranteService.AgregarPedidoEntrada entrada)
            throws SQLException {
        return operaciones.agregarAlPedido(id, entrada);
    }
    @PostMapping("/pedidos/{id}/cobrar")
    public OperacionRestauranteService.CobroRespuesta cobrar(
            @PathVariable int id, @RequestBody OperacionRestauranteService.CobroEntrada cobro) throws SQLException {
        return operaciones.cobrarPedido(id, cobro);
    }
}
