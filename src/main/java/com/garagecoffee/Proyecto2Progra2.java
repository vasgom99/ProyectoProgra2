package com.garagecoffee;

import com.garagecoffee.controller.RestauranteController;
import com.garagecoffee.view.LoginFrame;
import javax.swing.SwingUtilities;
import com.garagecoffee.view.Ui;

// Punto de entrada de la aplicación Swing con persistencia MySQL.
public class Proyecto2Progra2 {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Ui.prepararApariencia();
            try { new LoginFrame(new RestauranteController()).setVisible(true); }
            catch (IllegalStateException e) {
                javax.swing.JOptionPane.showMessageDialog(null,
                        e.getMessage() + "\nNo se iniciará en modo temporal.",
                        "No se pudo abrir Garage Coffee", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
