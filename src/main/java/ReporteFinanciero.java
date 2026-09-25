public class ReporteFinanciero {

    private double ingresos;
    private double egresos;

    public void registrarIngreso(double cantidad) {
        ingresos += cantidad;
    }

    public void registrarEgreso(double cantidad) {
        egresos += cantidad;
    }

    public double calcularBalance() {
        return ingresos - egresos;
    }

    public void mostrarReporte() {
        System.out.println("===== REPORTE FINANCIERO =====");
        System.out.println("Ingresos: Q" + ingresos);
        System.out.println("Egresos: Q" + egresos);
        System.out.println("Balance: Q" + calcularBalance());
    }
}