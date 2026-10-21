package com.garagecoffee;

import com.garagecoffee.controller.RestauranteController;
import com.garagecoffee.view.LoginFrame;
import com.garagecoffee.view.Ui;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

// inicia la aplicación con mysql.
public class Proyecto2Progra2 {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Ui.prepararApariencia();
            try {
                RestauranteController control = new RestauranteController();
                new LoginFrame(control).setVisible(true);
            } catch (IllegalStateException e) {
                JOptionPane.showMessageDialog(null,
                        "No se pudo conectar a MySQL.",
                        "No se pudo abrir Garage Coffee",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
