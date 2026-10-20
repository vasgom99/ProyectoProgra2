package com.garagecoffee.view;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

// actualiza las pantallas del sistema.
public abstract class PanelModulo extends JPanel {
    private final List<PanelListado<?>> listados = new ArrayList<>();
    protected void registrar(PanelListado<?> listado) { listados.add(listado); }
    public void recargar() { listados.forEach(PanelListado::recargar); }

}
