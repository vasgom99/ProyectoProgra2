package com.garagecoffee.api;

import com.garagecoffee.dao.*;
import com.garagecoffee.database.ConexionDB;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// conecta las dao con spring.
@Configuration
public class ConfiguracionDatos {
    public ConfiguracionDatos(DataSource fuente) { ConexionDB.usarDataSource(fuente); }
    @Bean EmpleadoDAO empleadoDAO() { return new EmpleadoDAO(); }
    @Bean MesaDAO mesaDAO() { return new MesaDAO(); }
    @Bean ProductoDAO productoDAO() { return new ProductoDAO(); }
    @Bean PedidoDAO pedidoDAO() { return new PedidoDAO(); }
    @Bean PagoDAO pagoDAO() { return new PagoDAO(); }
    @Bean CajaDAO cajaDAO() { return new CajaDAO(); }
    @Bean ControlCambiosDAO controlCambiosDAO() { return new ControlCambiosDAO(); }
}
