package com.garagecoffee.view;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

// Base de los JPanel Form: comparte actualización, no construye el diseño.
public abstract class PanelModulo extends JPanel {
    private final List<PanelListado<?>> listados = new ArrayList<>();
    protected void registrar(PanelListado<?> listado) { listados.add(listado); }
    public void recargar() { listados.forEach(PanelListado::recargar); }

    // Conserva las pestañas del diseñador, pero oculta las tareas poco frecuentes.
    protected void opcionesSecundarias(javax.swing.JTabbedPane pestanas, PanelListado<?> principal,
                                      java.awt.Component... secundarios) {
        var titulos = new java.util.LinkedHashMap<java.awt.Component, String>();
        for (var componente : secundarios) {
            int indice = pestanas.indexOfComponent(componente);
            if (indice >= 0) {
                titulos.put(componente, pestanas.getTitleAt(indice));
                pestanas.remove(componente);
            }
        }
        if (!titulos.isEmpty()) principal.alternar("Más opciones", "Menos opciones", mostrar -> {
            if (mostrar) titulos.forEach((componente, titulo) -> pestanas.addTab(titulo, componente));
            else titulos.keySet().forEach(pestanas::remove);
        });
    }
}
