import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {

    public VentanaPrincipal() {

        setTitle("Sistema de Administración - Cafetería");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel titulo = new JLabel("BIENVENIDO A LA CAFETERÍA", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 22));

        JLabel mensaje = new JLabel(
                "Seleccione una opción para comenzar",
                SwingConstants.CENTER
        );

        JButton btnCliente = new JButton("Registrar Cliente");
        JButton btnPedido = new JButton("Realizar Pedido");
        JButton btnCaja = new JButton("Caja");
        JButton btnInventario = new JButton("Inventario");
        JButton btnReporte = new JButton("Reporte Financiero");
        
        
btnCliente.addActionListener(e -> {

    String nombre = JOptionPane.showInputDialog(
            this,
            "Ingrese el nombre del cliente:"
    );

    if (nombre == null || nombre.trim().isEmpty()) {
        return;
    }

    String telefono = JOptionPane.showInputDialog(
            this,
            "Ingrese el teléfono:"
    );

    if (telefono == null || telefono.trim().isEmpty()) {
        return;
    }

    Cliente cliente = new Cliente(1, nombre, telefono);

    JOptionPane.showMessageDialog(
            this,
            "Cliente registrado correctamente.\n\n"
            + "Nombre: " + cliente.getNombre()
            + "\nTeléfono: " + cliente.getTelefono()
    );
});

btnPedido.addActionListener(e -> {

    String nombre = JOptionPane.showInputDialog(
            this,
            "Ingrese el nombre del cliente:"
    );

    if (nombre == null || nombre.trim().isEmpty()) {
        return;
    }

    Cliente cliente = new Cliente(1, nombre, "");

    Producto cafe = new Producto(1, "Cafe Americano", 15, 20);
    Producto capuchino = new Producto(2, "Capuchino", 20, 15);
    Producto pastel = new Producto(3, "Pastel", 25, 10);

    String[] productos = {
        "Cafe Americano - Q15",
        "Capuchino - Q20",
        "Pastel - Q25"
    };

    int opcion = JOptionPane.showOptionDialog(
            this,
            "Seleccione el producto:",
            "Nuevo Pedido",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null,
            productos,
            productos[0]
    );

    if (opcion == -1) {
        return;
    }

    String textoCantidad = JOptionPane.showInputDialog(
            this,
            "Ingrese la cantidad:"
    );

    if (textoCantidad == null) {
        return;
    }

    int cantidad;

    try {
        cantidad = Integer.parseInt(textoCantidad);
    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(
                this,
                "Ingrese una cantidad válida."
        );
        return;
    }

    Producto seleccionado;

    if (opcion == 0) {
        seleccionado = cafe;
    } else if (opcion == 1) {
        seleccionado = capuchino;
    } else {
        seleccionado = pastel;
    }

    if (cantidad <= 0 || cantidad > seleccionado.getStock()) {

        JOptionPane.showMessageDialog(
                this,
                "Cantidad no disponible."
        );

        return;
    }

    Venta venta = new Venta(1, cliente);

    venta.agregarProducto(seleccionado, cantidad);

    JOptionPane.showMessageDialog(
            this,
            "PEDIDO REALIZADO\n\n"
            + "Cliente: " + cliente.getNombre()
            + "\nProducto: " + seleccionado.getNombre()
            + "\nCantidad: " + cantidad
            + "\nTotal: Q" + venta.getTotal()
            + "\n\nStock restante: "
            + seleccionado.getStock()
    );
});
        
        btnCliente.addActionListener(e -> {

    String nombre = JOptionPane.showInputDialog(
            this,
            "Ingrese el nombre del cliente:"
    );

    if (nombre == null || nombre.trim().isEmpty()) {
        return;
    }

    String telefono = JOptionPane.showInputDialog(
            this,
            "Ingrese el teléfono:"
    );

    if (telefono == null || telefono.trim().isEmpty()) {
        return;
    }

    Cliente cliente = new Cliente(1, nombre, telefono);

    JOptionPane.showMessageDialog(
            this,
            "Cliente registrado correctamente.\n\n"
            + "Nombre: " + cliente.getNombre()
            + "\nTeléfono: " + cliente.getTelefono()
    );
});
        

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 80, 20, 80));

        panel.add(btnCliente);
        panel.add(btnPedido);
        panel.add(btnCaja);
        panel.add(btnInventario);
        panel.add(btnReporte);

        setLayout(new BorderLayout());

        add(titulo, BorderLayout.NORTH);
        add(mensaje, BorderLayout.CENTER);
        add(panel, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        // El prototipo se conserva como referencia; la ejecución usa la aplicación integrada.
        com.garagecoffee.Proyecto2Progra2.main(args);
    }
}
