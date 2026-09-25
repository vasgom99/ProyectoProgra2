public class Compra {

    private int numero;
    private Proveedor proveedor;
    private double total;

    public Compra(int numero, Proveedor proveedor, double total) {
        this.numero = numero;
        this.proveedor = proveedor;
        this.total = total;
    }

    public double getTotal() {
        return total;
    }

    public void mostrarCompra() {
        System.out.println("Compra #" + numero);
        System.out.println("Proveedor: " + proveedor);
        System.out.println("Total: Q" + total);
    }
}