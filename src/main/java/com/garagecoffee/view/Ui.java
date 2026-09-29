package com.garagecoffee.view;

import com.garagecoffee.model.*;
import java.awt.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.*;

// Utilidades pequeñas para mensajes, nombres legibles e impresión.
public final class Ui {
    public static final Color FONDO = new Color(246, 244, 238);
    public static final Color CAFE = new Color(36, 59, 50);
    public static final Color ACENTO = new Color(174, 96, 64);
    public static final DateTimeFormatter FECHA_HORA = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm");
    private Ui() { }
    // Nimbus está incluido en Java; no requiere instalar otra librería gráfica.
    public static void prepararApariencia() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if (info.getName().equals("Nimbus")) { UIManager.setLookAndFeel(info.getClassName()); break; }
            }
        } catch (ReflectiveOperationException | UnsupportedLookAndFeelException ex) {
            System.err.println("Se usará la apariencia Swing disponible.");
        }
        UIManager.put("defaultFont", new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        UIManager.put("control", FONDO);
        UIManager.put("nimbusBase", CAFE);
        UIManager.put("nimbusFocus", new Color(128, 157, 137));
        UIManager.put("nimbusSelectionBackground", new Color(219, 232, 222));
        UIManager.put("text", CAFE);
        UIManager.put("Table.alternateRowColor", new Color(249, 250, 247));
        UIManager.put("OptionPane.background", FONDO);
        UIManager.put("Panel.background", FONDO);
    }
    private static void estiloBoton(JButton boton, Color fondo, Color texto) {
        boton.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        boton.setOpaque(true); boton.setContentAreaFilled(true); boton.setBorderPainted(true);
        boton.setBackground(fondo); boton.setForeground(texto);
        boton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
        boton.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(fondo.darker()),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)));
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        boton.setFocusPainted(true);
    }
    public static void primario(JButton boton) { estiloBoton(boton, CAFE, Color.WHITE); }
    public static void secundario(JButton boton) { estiloBoton(boton, new Color(235, 240, 235), CAFE); }
    public static void navegacion(JButton boton, boolean seleccionado) {
        estiloBoton(boton, seleccionado ? new Color(219, 232, 219) : CAFE,
                seleccionado ? CAFE : new Color(226, 234, 227));
        boton.setHorizontalAlignment(SwingConstants.LEFT);
        boton.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
        boton.getAccessibleContext().setAccessibleDescription(seleccionado ? "Sección actual" : "Abrir sección");
    }
    public static String nombre(Object valor) {
        return switch (valor) {
            case null -> "—";
            case Producto p -> p.getNombre();
            case Categoria c -> c.getNombre();
            case Cliente c -> c.getNombre() + " (#" + c.getId() + ")";
            case Ingrediente i -> i.getNombre() + " (" + i.getUnidadMedida() + ")";
            case Proveedor p -> p.getNombre();
            case Mesa m -> "Mesa " + m.getNumero() + " · " + m.getEstado();
            case Repartidor r -> r.getNombre();
            case com.garagecoffee.model.Menu m -> m.getNombre();
            case DireccionCliente d -> d.getDireccion();
            case Pedido p -> "#" + p.getId() + " · " + p.getTipoEntrega() + " · Q" + p.getTotal();
            case Enum<?> e -> e.name().replace('_', ' ');
            default -> valor.toString();
        };
    }
    public static String fecha(LocalDateTime fecha) { return fecha == null ? "" : FECHA_HORA.format(fecha); }
    public static String dinero(BigDecimal monto) { return monto == null ? "—" : "Q " + monto.setScale(2, java.math.RoundingMode.HALF_UP); }
    public static void ejecutar(Component padre, Runnable accion) {
        try { accion.run(); }
        catch (IllegalArgumentException | IllegalStateException ex) {
            JOptionPane.showMessageDialog(padre, ex.getMessage(), "Revisa los datos", JOptionPane.WARNING_MESSAGE);
        } catch (RuntimeException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(padre, "No se pudo completar la operación. Revisa el detalle en la salida de la aplicación.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    public static boolean confirmar(Component padre, String texto) {
        return JOptionPane.showConfirmDialog(padre, texto, "Confirmar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION;
    }
    public static JLabel aviso(String texto) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        return etiqueta;
    }
    public static JButton boton(String texto, Runnable accion) {
        JButton boton = new JButton(texto);
        secundario(boton);
        boton.addActionListener(e -> ejecutar(boton, accion));
        return boton;
    }
    public static void mostrarTexto(Component padre, String titulo, String texto) {
        new TextoDialog(SwingUtilities.getWindowAncestor(padre), titulo, texto).setVisible(true);
    }
    public static JTextArea areaTexto(String texto) {
        JTextArea area = new JTextArea(texto);
        area.setEditable(false); area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        area.setMargin(new Insets(12, 12, 12, 12)); area.setLineWrap(true); area.setWrapStyleWord(true);
        area.setCaretPosition(0); return area;
    }
    public static void guardarTexto(Component padre, String texto) {
        JFileChooser selector = new JFileChooser();
        selector.setSelectedFile(new java.io.File("garage-coffee.txt"));
        if (selector.showSaveDialog(padre) != JFileChooser.APPROVE_OPTION) return;
        java.io.File archivo = selector.getSelectedFile();
        if (archivo.exists() && !confirmar(padre, "¿Reemplazar el archivo seleccionado?")) return;
        try { Files.writeString(archivo.toPath(), texto, StandardCharsets.UTF_8); }
        catch (IOException ex) { throw new IllegalStateException("No se pudo escribir el archivo: " + ex.getMessage()); }
    }
    public static void imprimir(Component padre, JTextArea area, String titulo) {
        try {
            // El diálogo del sistema permite PDF si hay una impresora/opción PDF disponible.
            area.print(new MessageFormat(titulo.replace("'", "''")), new MessageFormat("Página {0}"));
        } catch (java.awt.print.PrinterException | SecurityException ex) {
            throw new IllegalStateException("No se pudo imprimir: " + ex.getMessage());
        }
    }
}
