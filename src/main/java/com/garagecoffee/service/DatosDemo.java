package com.garagecoffee.service;

import com.garagecoffee.model.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Ingrediente grano = ingrediente("Café", "g", "2000.000", "200.000", "0.1000");
        Ingrediente leche = ingrediente("Leche", "ml", "5000.000", "500.000", "0.0100");
        Ingrediente porcion = ingrediente("Porción de pastel", "unidad", "20.000", "3.000", "10.0000");
        recetas.add(new DetalleReceta(siguienteId(DetalleReceta.class), new BigDecimal("18.000"), cafe, grano));
        recetas.add(new DetalleReceta(siguienteId(DetalleReceta.class), new BigDecimal("18.000"), capuchino, grano));
        recetas.add(new DetalleReceta(siguienteId(DetalleReceta.class), new BigDecimal("200.000"), capuchino, leche));
        recetas.add(new DetalleReceta(siguienteId(DetalleReceta.class), BigDecimal.ONE, pastel, porcion));
        for (int i = 1; i <= 6; i++) mesas.add(new Mesa(siguienteId(Mesa.class), i, 4, EstadoMesa.LIBRE));
        clientes.add(new Cliente(siguienteId(Cliente.class), "Cliente de ejemplo", "5555-0101", ""));
        repartidores.add(new Repartidor(siguienteId(Repartidor.class), "Repartidor de ejemplo", "5555-0202", "Moto"));
        proveedores.add(new Proveedor(siguienteId(Proveedor.class), "Proveedor de ejemplo", "5555-0303", "", "", true));
    }



    private Ingrediente ingrediente(String nombre, String unidad, String stock, String minimo, String costo) {
        Ingrediente ingrediente = new Ingrediente(siguienteId(Ingrediente.class), nombre, unidad,
                new BigDecimal(stock), new BigDecimal(minimo), new BigDecimal(costo));
        ingredientes.add(ingrediente);
        return ingrediente;
    }
}
