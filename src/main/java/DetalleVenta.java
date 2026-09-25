public class DetalleVenta {

    private Producto producto;
    private int cantidad;

    public DetalleVenta(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
    }

    public double calcularSubtotal() {
        return producto.getPrecio() * cantidad;
    }

    public void mostrarDetalle() {
        System.out.println(
            producto.getNombre() + 
            " x " + cantidad + 
            " = Q" + calcularSubtotal()
        );
    }
}