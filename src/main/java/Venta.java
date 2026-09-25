public class Venta {

    private int numero;
    private Cliente cliente;
    private double total;

    public Venta(int numero, Cliente cliente) {
        this.numero = numero;
        this.cliente = cliente;
        this.total = 0;
    }

    public void agregarProducto(Producto producto, int cantidad) {
        if (cantidad <= producto.getStock()) {
            total += producto.getPrecio() * cantidad;
            producto.disminuirStock(cantidad);
        }
    }

    public double getTotal() {
        return total;
    }

    public void mostrarVenta() {
        System.out.println("Venta #" + numero);
        System.out.println("Cliente: " + cliente.getNombre());
        System.out.println("Total: Q" + total);
    }
}