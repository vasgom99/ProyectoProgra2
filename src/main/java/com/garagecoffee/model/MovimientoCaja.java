package com.garagecoffee.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** @author Miguel */
public class MovimientoCaja {

    private int id;
    private Caja caja;
    private TipoMovimientoCaja tipo;
    private String concepto;
    private BigDecimal monto;
    private LocalDateTime fechaHora;

    public MovimientoCaja() {
    }

    public MovimientoCaja(
            int id,
            Caja caja,
            TipoMovimientoCaja tipo,
            String concepto,
            BigDecimal monto,
            LocalDateTime fechaHora) {
        this.id = id;
        this.caja = caja;
        this.tipo = tipo;
        this.concepto = concepto;
        this.monto = monto;
        this.fechaHora = fechaHora;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Caja getCaja() {
        return caja;
    }

    public void setCaja(Caja caja) {
        this.caja = caja;
    }

    public TipoMovimientoCaja getTipo() {
        return tipo;
    }

    public void setTipo(TipoMovimientoCaja tipo) {
        this.tipo = tipo;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }
}
