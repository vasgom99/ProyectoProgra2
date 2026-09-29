package com.garagecoffee.view;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

// Base de los JPanel Form: comparte actualización, no construye el diseño.
public abstract class PanelModulo extends JPanel {
    private final List<PanelListado<?>> listados = new ArrayList<>();
    protected void registrar(PanelListado<?> listado) { listados.add(listado); }
    public void recargar() { listados.forEach(PanelListado::recargar); }
}
