package com.garagecoffee.api;

import java.sql.SQLException;
import java.util.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

// Rutas iniciales del servidor; no exponen modelos con contraseñas.
@RestController
@RequestMapping("/api")
public class RestauranteApi {
    private final ConsultaRestauranteService consultas;
    public RestauranteApi(ConsultaRestauranteService consultas) { this.consultas = consultas; }
    @GetMapping("/estado") public Map<String, String> estado() { return Map.of("servidor", "disponible"); }
    @GetMapping("/sesion") public Map<String, Object> sesion(Authentication usuario) {
        return Map.of("usuario", usuario.getName(), "roles",
                usuario.getAuthorities().stream().map(a -> a.getAuthority()).filter(a -> a.startsWith("ROLE_"))
                        .map(a -> a.substring(5)).toList());
    }
    @GetMapping("/mesas") public List<ConsultaRestauranteService.MesaRespuesta> mesas() throws SQLException { return consultas.mesas(); }
    @GetMapping("/catalogo") public List<ConsultaRestauranteService.ProductoRespuesta> catalogo() throws SQLException { return consultas.catalogo(); }
}
