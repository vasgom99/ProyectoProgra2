public class Producto {

    private int id;
    private String nombre;
    private double precio;
    private int stock;

    public Producto(int id, String nombre, double precio, int stock) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public int getStock() {
        return stock;
    }

    public void aumentarStock(int cantidad) {
        stock += cantidad;
    }

    public void disminuirStock(int cantidad) {
        if (cantidad <= stock) {
            stock -= cantidad;
        }
    }

    public void mostrarProducto() {
        System.out.println(nombre + " - Q" + precio + " - Stock: " + stock);
    }
}