package com.garagecoffee.view;

import com.garagecoffee.controller.RestauranteController;
import com.garagecoffee.service.DatosDemo;
import java.awt.*;
import java.lang.reflect.*;
import java.math.BigDecimal;
import java.nio.file.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicOptionPaneUI;
import javax.xml.parsers.DocumentBuilderFactory;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

// Comprueba componentes reales de Swing sin abrir ventanas durante Maven Test.
class AuditoriaSwingTest {
    private RestauranteController control;
    private static Formulario formularioMostrado;

    @BeforeEach void preparar() {
        control = new RestauranteController(new DatosDemo());
        control.sesion().iniciarSesion("admin", "demo".toCharArray());
    }

    public static class CapturaOptionPaneUI extends BasicOptionPaneUI {
        public static ComponentUI createUI(JComponent componente) { return new CapturaOptionPaneUI(); }
        @Override public void installUI(JComponent componente) {
            Object mensaje = ((JOptionPane) componente).getMessage();
            if (mensaje instanceof JScrollPane scroll && scroll.getViewport().getView() instanceof Formulario formulario)
                formularioMostrado = formulario;
            super.installUI(componente);
        }
    }

    private static <T> List<T> buscar(Component componente, Class<T> tipo) {
        List<T> encontrados = new ArrayList<>();
        if (tipo.isInstance(componente)) encontrados.add(tipo.cast(componente));
        if (componente instanceof Container contenedor)
            for (Component hijo : contenedor.getComponents()) encontrados.addAll(buscar(hijo, tipo));
        return encontrados;
    }

    private static void enEdt(ThrowingRunnable tarea) throws Exception {
        try { SwingUtilities.invokeAndWait(() -> { try { tarea.run(); } catch (Exception e) { throw new RuntimeException(e); } }); }
        catch (InvocationTargetException e) {
            if (e.getCause() instanceof RuntimeException r && r.getCause() instanceof Exception causa) throw causa;
            throw e;
        }
    }
    @FunctionalInterface private interface ThrowingRunnable { void run() throws Exception; }

    @Test void panelesPrincipalesSeConstruyenYRecargan() throws Exception {
        enEdt(() -> {
            List<PanelModulo> paneles = List.of(
                    new InicioPanel(control), new PedidosPanel(control, false), new AtencionPanel(control),
                    new CocinaPanel(control), new PedidosPanel(control, true), new CatalogoPanel(control),
                    new InventarioPanel(control), new ComprasPanel(control), new CajaPanel(control),
                    new EmpleadosPanel(control), new ReportesPanel(control));
            assertEquals(11, paneles.size());
            paneles.forEach(panel -> assertDoesNotThrow(panel::recargar));
            assertTrue(paneles.stream().allMatch(panel -> panel.getComponentCount() > 0));
        });
    }

    @Test void formularioRealDeCompraMuestraColumnasBotonesYOpciones() throws Exception {
        enEdt(() -> {
            UIManager.put("OptionPaneUI", CapturaOptionPaneUI.class.getName());
            ComprasPanel panel = new ComprasPanel(control);
            Method nueva = ComprasPanel.class.getDeclaredMethod("nueva");
            nueva.setAccessible(true);
            for (int intento = 0; intento < 3; intento++) {
                formularioMostrado = null;
                InvocationTargetException error = assertThrows(InvocationTargetException.class, () -> nueva.invoke(panel));
                assertInstanceOf(HeadlessException.class, error.getCause());
                assertInstanceOf(CompraForm.class, formularioMostrado);
                JTable tabla = buscar(formularioMostrado, JTable.class).getFirst();
                assertEquals(List.of("Ingrediente", "Cantidad", "Costo unitario Q", "Subtotal Q"),
                        java.util.Collections.list(tabla.getColumnModel().getColumns()).stream()
                                .map(columna -> String.valueOf(columna.getHeaderValue())).toList());
                assertTrue(buscar(formularioMostrado, JButton.class).stream()
                        .anyMatch(boton -> boton.isVisible() && boton.getText().equals("Agregar ingrediente")));
                assertTrue(buscar(formularioMostrado, JLabel.class).stream()
                        .anyMatch(etiqueta -> etiqueta.getText() != null && etiqueta.getText().startsWith("Agrega ingredientes")));
                assertEquals(2, buscar(formularioMostrado, JCheckBox.class).size());
            }
        });
    }

    @Test void formatosNumericosComunesSonAceptados() throws Exception {
        enEdt(() -> {
            Formulario formulario = new LineaCompraForm();
            Map<String, String> casos = new LinkedHashMap<>();
            casos.put("10.50", "10.50"); casos.put("10,50", "10.50"); casos.put("Q 10.50", "10.50");
            casos.put("1,250.50", "1250.50"); casos.put("1.250,50", "1250.50"); casos.put("1 250,50", "1250.50");
            casos.put(".50", "0.50"); casos.put("10.000", "10");
            casos.forEach((entrada, esperado) -> assertEquals(0, formulario.decimal(
                    formulario.texto("Costo por unidad Q", entrada)).compareTo(new BigDecimal(esperado)), entrada));
            for (String entrada : List.of("", "hola", "12,34.56", "1.23.456", "1e3", "$10"))
                assertThrows(IllegalArgumentException.class,
                        () -> formulario.decimal(formulario.texto("Costo por unidad Q", entrada)), entrada);
        });
    }

    @Test void todosLosFormulariosSeConstruyenConSusComponentes() throws Exception {
        enEdt(() -> {
            Path carpeta = Path.of("src/main/java/com/garagecoffee/view");
            try (var archivos = Files.list(carpeta)) {
                var formularios = archivos
                        .filter(ruta -> ruta.toString().endsWith("Form.java"))
                        .map(ruta -> ruta.getFileName().toString().replace(".java", ""))
                        .map(nombre -> {
                            try { return Class.forName("com.garagecoffee.view." + nombre); }
                            catch (ClassNotFoundException e) { throw new RuntimeException(e); }
                        })
                        .filter(Formulario.class::isAssignableFrom).toList();
                assertEquals(26, formularios.size());
                for (Class<?> tipo : formularios) {
                    Formulario formulario = (Formulario) tipo.getConstructor().newInstance();
                    assertTrue(formulario.getComponentCount() > 0, tipo.getSimpleName());
                }
            }
        });
    }

    @Test void archivosFormCoincidenConLosCamposJava() throws Exception {
        Path carpeta = Path.of("src/main/java/com/garagecoffee/view");
        var fabrica = DocumentBuilderFactory.newInstance();
        fabrica.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        try (var archivos = Files.list(carpeta)) {
            for (Path archivo : archivos.filter(ruta -> ruta.toString().endsWith(".form")).toList()) {
                String claseNombre = archivo.getFileName().toString().replace(".form", "");
                Class<?> clase = Class.forName("com.garagecoffee.view." + claseNombre);
                var xml = fabrica.newDocumentBuilder().parse(archivo.toFile());
                var nodos = xml.getElementsByTagName("*");
                for (int i = 0; i < nodos.getLength(); i++) {
                    var elemento = (org.w3c.dom.Element) nodos.item(i);
                    if ((elemento.getTagName().equals("Component") || elemento.getTagName().equals("Container"))
                            && elemento.hasAttribute("name")) {
                        String campo = elemento.getAttribute("name");
                        assertDoesNotThrow(() -> clase.getDeclaredField(campo), claseNombre + "." + campo);
                    }
                }
            }
        }
    }
}
