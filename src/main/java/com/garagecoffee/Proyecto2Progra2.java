package com.garagecoffee;

import com.garagecoffee.controller.RestauranteController;
import com.garagecoffee.service.DatosDemo;
import com.garagecoffee.view.LoginFrame;
import com.garagecoffee.view.Ui;
import java.util.Arrays;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

// Inicia con MySQL o, si se elige, con datos temporales de demostración.
public class Proyecto2Progra2 {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Ui.prepararApariencia();
            RestauranteController control;
            if (Arrays.asList(args).contains("--demo")) {
                control = new RestauranteController(new DatosDemo());
            } else {
                try {
                    control = new RestauranteController();
                } catch (IllegalStateException e) {
                    String detalle = e.getCause() == null ? "" : "\nDetalle: " + e.getCause().getMessage();
                    int respuesta = JOptionPane.showConfirmDialog(null,
                            e.getMessage() + detalle
                                    + "\n\n¿Abrir una demostración con datos temporales?"
                                    + "\nLos cambios de la demostración se pierden al cerrar.",
                            "MySQL no disponible",
                            JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                    if (respuesta != JOptionPane.YES_OPTION) return;
                    control = new RestauranteController(new DatosDemo());
                }
            }
            new LoginFrame(control).setVisible(true);
            System.out.println("Garage Coffee: pantalla de acceso lista ("
                    + (control.esDemo() ? "demostración" : "MySQL") + ").");
        });
    }
}
