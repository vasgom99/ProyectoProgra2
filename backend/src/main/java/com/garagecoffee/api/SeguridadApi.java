package com.garagecoffee.api;

import com.garagecoffee.dao.EmpleadoDAO;
import com.garagecoffee.service.Claves;
import java.sql.SQLException;
import java.util.Arrays;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.http.HttpMethod;

// Primera etapa: consultas protegidas por usuario y rol; sin sesión global compartida.
@Configuration
public class SeguridadApi {
    @Bean UserDetailsService empleados(EmpleadoDAO dao) {
        return usuario -> {
            try {
                var empleado = dao.buscarPorNombreUsuario(usuario);
                if (empleado == null) throw new UsernameNotFoundException("Credenciales incorrectas.");
                return User.withUsername(empleado.getNombreUsuario()).password(empleado.getPassword())
                        .roles(empleado.getRol().name()).disabled(!empleado.isActivo()).build();
            } catch (SQLException e) {
                throw new InternalAuthenticationServiceException("No se pudo consultar el acceso.", e);
            }
        };
    }
    @Bean PasswordEncoder claves() {
        return new PasswordEncoder() {
            public String encode(CharSequence clave) {
                char[] valor = clave.toString().toCharArray();
                try { return Claves.proteger(valor); } finally { Arrays.fill(valor, '\0'); }
            }
            public boolean matches(CharSequence clave, String guardada) {
                char[] valor = clave.toString().toCharArray();
                try { return Claves.verificar(valor, guardada); } finally { Arrays.fill(valor, '\0'); }
            }
        };
    }
    @Bean SecurityFilterChain permisos(HttpSecurity http) throws Exception {
        return http
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .requestCache(c -> c.disable())
                // La app usa autenticación Basic sin cookies ni sesión web.
                .csrf(c -> c.disable())
                .authorizeHttpRequests(a -> a
                        .requestMatchers(HttpMethod.GET, "/api/estado").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/sesion").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/mesas").hasAnyRole("ADMINISTRADOR", "MESERO", "CAJERO")
                        .requestMatchers(HttpMethod.GET, "/api/mesas/*/pedido-activo").hasAnyRole("ADMINISTRADOR", "MESERO", "CAJERO")
                        .requestMatchers(HttpMethod.GET, "/api/catalogo").hasAnyRole("ADMINISTRADOR", "MESERO", "CAJERO", "COCINA")
                        .requestMatchers(HttpMethod.POST, "/api/pedidos").hasAnyRole("ADMINISTRADOR", "MESERO", "CAJERO")
                        .requestMatchers(HttpMethod.POST, "/api/pedidos/*/cobrar").hasAnyRole("ADMINISTRADOR", "MESERO", "CAJERO")
                        .requestMatchers(HttpMethod.PUT, "/api/mesas/*/estado").hasAnyRole("ADMINISTRADOR", "MESERO")
                        .requestMatchers(HttpMethod.PUT, "/api/pedidos/*/entregar").hasAnyRole("ADMINISTRADOR", "MESERO", "CAJERO")
                        .requestMatchers(HttpMethod.PUT, "/api/pedidos/*/productos").hasAnyRole("ADMINISTRADOR", "MESERO", "CAJERO")
                        .anyRequest().denyAll())
                .httpBasic(Customizer.withDefaults())
                .exceptionHandling(e -> e
                        .authenticationEntryPoint((req, res, ex) -> {
                            res.setStatus(401); res.setContentType("application/json");
                            res.getWriter().write("{\"error\":\"Credenciales requeridas o incorrectas\"}");
                        })
                        .accessDeniedHandler((req, res, ex) -> {
                            res.setStatus(403); res.setContentType("application/json");
                            res.getWriter().write("{\"error\":\"Acceso no permitido\"}");
                        }))
                .build();
    }
}
