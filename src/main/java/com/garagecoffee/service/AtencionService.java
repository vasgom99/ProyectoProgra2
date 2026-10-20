package com.garagecoffee.service;

import com.garagecoffee.model.EstadoMesa;
import com.garagecoffee.model.Mesa;
import com.garagecoffee.model.PedidoLocal;
import java.util.List;

// Mantiene las mesas disponibles para tomar pedidos locales.
public class AtencionService {
    private final DatosRestaurante datos;
    private final SesionService sesion;

    public AtencionService(DatosRestaurante datos, SesionService sesion) {
        this.datos = datos;
        this.sesion = sesion;
    }

    public List<Mesa> mesas() {
        sesion.actual();
        return List.copyOf(datos.mesas);
    }

    public void guardarMesa(int id, int numero, int capacidad, boolean disponible) {
        datos.transaccion(() -> {
            sesion.exigir();
            Validacion.exigir(numero > 0 && capacidad > 0,
                    "Número y capacidad deben ser mayores que cero.");
            Validacion.exigir(datos.mesas.stream().noneMatch(m -> m.getId() != id && m.getNumero() == numero),
                    "Ese número de mesa ya existe.");
            Mesa mesa = id == 0 ? new Mesa() : datos.mesas.stream()
                    .filter(m -> m.getId() == id).findFirst().orElseThrow();
            Validacion.exigir(id == 0 || mesa.getEstado() != EstadoMesa.OCUPADA,
                    "No se puede modificar una mesa ocupada.");
            if (id == 0) mesa.setId(datos.siguienteId(Mesa.class));
            mesa.setNumero(numero);
            mesa.setCapacidad(capacidad);
            mesa.setEstado(disponible ? EstadoMesa.LIBRE : EstadoMesa.FUERA_SERVICIO);
            datos.guardar(mesa);
            if (id == 0) datos.mesas.add(mesa);
            return null;
        });
    }

    public void eliminarMesa(Mesa mesa) {
        datos.transaccion(() -> {
            sesion.exigir();
            Validacion.exigir(datos.pedidos.stream().noneMatch(p -> p instanceof PedidoLocal local
                    && local.getMesa() == mesa), "La mesa tiene historial; márcala fuera de servicio.");
            datos.eliminar(mesa);
            datos.mesas.remove(mesa);
            return null;
        });
    }
}
