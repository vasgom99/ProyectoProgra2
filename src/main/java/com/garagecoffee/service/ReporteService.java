package com.garagecoffee.service;

import com.garagecoffee.model.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// Calcula reportes a partir de operaciones registradas; no necesita una tabla propia.
public class ReporteService {
    public enum Tipo { VENTAS, PRODUCTOS_VENDIDOS, INVENTARIO, GASTOS, RESULTADO_ESTIMADO, MOVIMIENTOS_CAJA }
    private final DatosRestaurante datos;
    private final SesionService sesion;
    public ReporteService(DatosRestaurante datos, SesionService sesion) { this.datos = datos; this.sesion = sesion; }

    public String generar(Tipo tipo, LocalDate inicio, LocalDate fin) {
        sesion.exigir();
        Validacion.exigir(tipo != null && inicio != null && fin != null && !fin.isBefore(inicio), "Indica un intervalo de fechas válido.");
        List<Factura> ventas = datos.facturas.stream().filter(f -> dentro(f.getFecha(), inicio, fin)).toList();
        StringBuilder texto = new StringBuilder("GARAGE COFFEE" + (datos.esDemo() ? " — DEMOSTRACIÓN" : " — MYSQL") + "\n");
        texto.append(tipo.toString().replace('_', ' ')).append("\nDel ").append(inicio).append(" al ").append(fin).append("\n\n");
        switch (tipo) {
            case VENTAS -> {
                texto.append("Ventas completamente cobradas (fecha del último pago).\n\n");
                for (Factura f : ventas) texto.append(f.getNumero()).append(" · Pedido #").append(f.getPedido().getId())
                        .append(" · ").append(f.getFecha().toLocalDate()).append(" · Q").append(f.getTotal()).append("\n");
                texto.append("\nTotal: Q").append(totalVentas(ventas));
                BigDecimal abonos = datos.pagos.stream().filter(p -> dentro(p.getFechaHora(), inicio, fin))
                        .map(Pago::getMonto).reduce(BigDecimal.ZERO, BigDecimal::add);
                texto.append("\nCobros del período, incluidos abonos: Q").append(abonos);
                texto.append("\nLos cobros pueden pertenecer a ventas de otro período.");
            }
            case PRODUCTOS_VENDIDOS -> {
                Map<Producto, Integer> cantidades = new LinkedHashMap<>();
                for (Factura f : ventas) for (DetallePedido d : f.getPedido().getDetalles())
                    cantidades.merge(d.getProducto(), d.getCantidad(), Integer::sum);
                cantidades.entrySet().stream().sorted(Map.Entry.<Producto, Integer>comparingByValue().reversed())
                        .forEach(e -> texto.append(e.getKey().getNombre()).append(": ").append(e.getValue()).append(" unidades\n"));
            }
            case INVENTARIO -> {
                texto.append("Existencias actuales; no reconstruye inventario histórico.\n\n");
                for (Ingrediente i : datos.ingredientes) texto.append(i.getNombre()).append(": ").append(i.getStockActual())
                        .append(" ").append(i.getUnidadMedida()).append(" · mínimo ").append(i.getStockMinimo())
                        .append(i.verificarAlertaStock() ? " · STOCK BAJO" : "").append("\n");
            }
            case GASTOS -> {
                for (Gasto g : datos.gastos) if (dentro(g.getFechaHora(), inicio, fin))
                    texto.append(g.getFechaHora().toLocalDate()).append(" · ").append(g.getCategoria()).append(" · ")
                            .append(g.getDescripcion()).append(" · Q").append(g.getMonto()).append("\n");
                texto.append("\nTotal: Q").append(totalGastos(inicio, fin));
            }
            case RESULTADO_ESTIMADO -> {
                BigDecimal ingresos = totalVentas(ventas);
                BigDecimal costos = ventas.stream().map(f -> datos.costosPedidos.getOrDefault(f.getPedido().getId(), BigDecimal.ZERO))
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                long sinCosto = ventas.stream().filter(f -> !datos.costosPedidos.containsKey(f.getPedido().getId())).count();
                BigDecimal gastos = totalGastos(inicio, fin);
                BigDecimal mermas = datos.movimientosStock.stream().filter(m -> m.getTipo() == TipoMovimientoStock.MERMA
                        && dentro(m.getFechaHora(), inicio, fin)).map(MovimientoStock::getCostoTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
                texto.append("Ventas cobradas: Q").append(ingresos).append("\nIngredientes consumidos de esas ventas: Q").append(costos)
                        .append("\nGastos registrados: Q").append(gastos).append("\nMermas registradas: Q").append(mermas)
                        .append("\nResultado estimado: Q").append(ingresos.subtract(costos).subtract(gastos).subtract(mermas))
                        .append("\n\nNo representa utilidad contable ni incluye impuestos o costos no registrados.")
                        .append("\nLas compras no se restan otra vez: se considera el costo de ingredientes consumidos.");
                if (sinCosto > 0) texto.append("\nADVERTENCIA: ").append(sinCosto).append(" ventas aún no preparadas; el costo y el resultado están incompletos.");
            }
            case MOVIMIENTOS_CAJA -> {
                for (MovimientoCaja m : datos.movimientosCaja) if (dentro(m.getFechaHora(), inicio, fin))
                    texto.append("Caja #").append(m.getCaja().getId()).append(" · ").append(m.getTipo()).append(" · Q")
                            .append(m.getMonto()).append(" · ").append(m.getConcepto()).append("\n");
                texto.append("\nSolo movimientos de efectivo. Los pagos electrónicos no aumentan la caja.");
            }
        }
        return texto.append(datos.esDemo() ? "\n\nDatos temporales. Sin validez fiscal.\n" : "\n\nDatos registrados en MySQL. Sin validez fiscal.\n").toString();
    }
    private boolean dentro(LocalDateTime fecha, LocalDate inicio, LocalDate fin) {
        return !fecha.toLocalDate().isBefore(inicio) && !fecha.toLocalDate().isAfter(fin);
    }
    private BigDecimal totalVentas(List<Factura> ventas) {
        return ventas.stream().map(Factura::getTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    private BigDecimal totalGastos(LocalDate inicio, LocalDate fin) {
        return datos.gastos.stream().filter(g -> dentro(g.getFechaHora(), inicio, fin)).map(Gasto::getMonto).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
