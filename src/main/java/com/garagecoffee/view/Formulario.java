package com.garagecoffee.view;

import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.*;

// Base de los JPanel Form de captura. Solo enlaza campos y valida; no crea su diseño.
public class Formulario extends JPanel implements Scrollable {
    private final Map<String, JComponent> campos = new LinkedHashMap<>();
    protected void registrar(String etiqueta, JComponent componente) {
        campos.put(etiqueta, componente);
        componente.getAccessibleContext().setAccessibleName(etiqueta);
    }
    private JComponent campoRegistrado(String etiqueta) {
        JComponent campo = campos.get(etiqueta);
        if (campo == null) throw new IllegalArgumentException("Campo no definido en el formulario: " + etiqueta);
        return campo;
    }
    @SuppressWarnings("unchecked")
    public <T extends JComponent> T campo(String etiqueta, T valor) {
        JComponent existente = campoRegistrado(etiqueta);
        if (existente instanceof JLabel actual && valor instanceof JLabel nuevo) actual.setText(nuevo.getText());
        return (T) existente;
    }
    public JLabel etiqueta(String etiqueta, String texto) {
        JLabel campo = (JLabel) campoRegistrado(etiqueta); campo.setText(texto); return campo;
    }
    public JPasswordField clave(String etiqueta) { return (JPasswordField) campoRegistrado(etiqueta); }
    @SuppressWarnings("unchecked")
    public <T> PanelListado<T> listado(String etiqueta) { return (PanelListado<T>) campoRegistrado(etiqueta); }
    public JTextField texto(String etiqueta, Object valor) {
        JTextField campo = (JTextField) campoRegistrado(etiqueta);
        campo.setText(valor == null ? "" : valor.toString()); return campo;
    }
    public JCheckBox check(String etiqueta, boolean marcado) {
        JCheckBox campo = (JCheckBox) campoRegistrado(etiqueta); campo.setSelected(marcado); return campo;
    }
    @SuppressWarnings("unchecked")
    public <T> JComboBox<T> combo(String etiqueta, List<T> opciones, T actual) {
        JComboBox<T> combo = (JComboBox<T>) campoRegistrado(etiqueta);
        combo.removeAllItems();
        for (T opcion : opciones) combo.addItem(opcion);
        combo.setRenderer(new DefaultListCellRenderer() {
            @Override public Component getListCellRendererComponent(JList<?> lista, Object valor, int indice, boolean elegido, boolean foco) {
                return super.getListCellRendererComponent(lista, Ui.nombre(valor), indice, elegido, foco);
            }
        });
        if (actual != null) combo.setSelectedItem(actual);
        return combo;
    }
    public <T> T elegido(JComboBox<T> combo) { return combo.getSelectedIndex() < 0 ? null : combo.getItemAt(combo.getSelectedIndex()); }
    public String valor(JTextField campo) { return campo.getText().trim(); }
    public int entero(JTextField campo) {
        try { return Integer.parseInt(valor(campo)); }
        catch (NumberFormatException ex) { throw new IllegalArgumentException(campo.getAccessibleContext().getAccessibleName() + ": usa un número entero."); }
    }
    public BigDecimal decimal(JTextField campo) {
        if (valor(campo).length() > 30) throw new IllegalArgumentException("El número es demasiado largo.");
        if (!valor(campo).matches("[+-]?[0-9]+([.,][0-9]+)?"))
            throw new IllegalArgumentException(campo.getAccessibleContext().getAccessibleName() + ": escribe un número sin separadores de miles.");
        try { return new BigDecimal(valor(campo).replace(',', '.')); }
        catch (NumberFormatException ex) { throw new IllegalArgumentException(campo.getAccessibleContext().getAccessibleName() + ": usa un número válido."); }
    }
    public LocalDate fecha(JTextField campo) {
        if (valor(campo).isEmpty()) return null;
        try { return LocalDate.parse(valor(campo)); }
        catch (DateTimeParseException ex) { throw new IllegalArgumentException("Usa una fecha válida: AAAA-MM-DD."); }
    }
    public LocalDateTime fechaHora(JTextField campo) {
        if (valor(campo).isEmpty()) return null;
        try { return LocalDateTime.parse(valor(campo), Ui.FECHA_HORA.withResolverStyle(ResolverStyle.STRICT)); }
        catch (DateTimeParseException ex) { throw new IllegalArgumentException("Usa una fecha y hora válidas: AAAA-MM-DD HH:MM."); }
    }
    public void guardar(Component padre, String titulo, Runnable guardar) {
        JScrollPane contenido = new JScrollPane(this);
        contenido.setBorder(null);
        Dimension preferido = getPreferredSize();
        contenido.setPreferredSize(new Dimension(Math.min(1000, Math.max(580, preferido.width + 25)), Math.min(600, preferido.height + 15)));
        while (JOptionPane.showConfirmDialog(padre, contenido, titulo, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION) {
            try { guardar.run(); return; }
            catch (IllegalArgumentException | IllegalStateException ex) {
                JOptionPane.showMessageDialog(padre, ex.getMessage(), "Revisa los datos", JOptionPane.WARNING_MESSAGE);
            }
        }
    }
    @Override public Dimension getPreferredScrollableViewportSize() { return getPreferredSize(); }
    @Override public int getScrollableUnitIncrement(Rectangle visible, int orientacion, int direccion) { return 20; }
    @Override public int getScrollableBlockIncrement(Rectangle visible, int orientacion, int direccion) { return Math.max(20, visible.height - 20); }
    @Override public boolean getScrollableTracksViewportWidth() { return true; }
    @Override public boolean getScrollableTracksViewportHeight() { return false; }
}
