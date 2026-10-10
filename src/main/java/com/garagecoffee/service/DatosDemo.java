package com.garagecoffee.service;

import com.garagecoffee.model.*;
import java.math.BigDecimal;

// Datos temporales para revisar la aplicación sin conectar MySQL.
public class DatosDemo extends DatosRestaurante {
    public DatosDemo() {
        for (RolEmpleado rol : RolEmpleado.values()) {
            String usuario = rol == RolEmpleado.ADMINISTRADOR ? "admin" : rol.name().toLowerCase();
            empleados.add(new Empleado(siguienteId(Empleado.class), usuario, "Demo", "", "",
                    usuario, "demo", rol, true));
        }
        Categoria bebidas = new Categoria(siguienteId(Categoria.class), "Bebidas", TipoCategoria.BEBIDA, true);
        Categoria comidas = new Categoria(siguienteId(Categoria.class), "Comidas", TipoCategoria.COMIDA, true);
        categorias.add(bebidas);
        categorias.add(comidas);
        Producto cafe = new Producto(siguienteId(Producto.class), "Café americano", "Café de la casa", "",
                new BigDecimal("15.00"), true, bebidas);
        Producto capuchino = new Producto(siguienteId(Producto.class), "Capuchino", "Café con leche", "",
                new BigDecimal("22.00"), true, bebidas);
        Producto pastel = new Producto(siguienteId(Producto.class), "Pastel de chocolate", "Porción individual", "",
                new BigDecimal("25.00"), true, comidas);
        productos.add(cafe); productos.add(capuchino); productos.add(pastel);
        for (int i = 1; i <= 6; i++) mesas.add(new Mesa(siguienteId(Mesa.class), i, 4, EstadoMesa.LIBRE));
    }
}
