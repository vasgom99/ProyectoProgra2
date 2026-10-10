package com.garagecoffee.api;

import com.garagecoffee.dao.*;
import com.garagecoffee.database.ConexionDB;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Conecta las DAO existentes al DataSource administrado por Spring.
@Configuration
public class ConfiguracionDatos {
    public ConfiguracionDatos(DataSource fuente) { ConexionDB.usarDataSource(fuente); }
    @Bean EmpleadoDAO empleadoDAO() { return new EmpleadoDAO(); }
    @Bean MesaDAO mesaDAO() { return new MesaDAO(); }
    @Bean ProductoDAO productoDAO() { return new ProductoDAO(); }
}
