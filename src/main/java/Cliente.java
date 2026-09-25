public class Cliente {

    private int id;
    private String nombre;
    private String telefono;

    public Cliente(int id, String nombre, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void mostrarCliente() {
        System.out.println("Cliente: " + nombre);
        System.out.println("Telefono: " + telefono);
    }
}