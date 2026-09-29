package com.garagecoffee.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

// Validaciones pequeñas que utilizan los servicios antes de modificar datos.
final class Validacion {
    private Validacion() { }

    static void exigir(boolean condicion, String mensaje) {
        if (!condicion) throw new IllegalArgumentException(mensaje);
    }

    static String texto(String valor, String campo, int maximo) {
        String limpio = valor == null ? "" : valor.trim();
        exigir(!limpio.isEmpty(), campo + " es obligatorio.");
        exigir(limpio.length() <= maximo, campo + " admite hasta " + maximo + " caracteres.");
        return limpio;
    }

    static String opcional(String valor, String campo, int maximo) {
        String limpio = valor == null ? "" : valor.trim();
        exigir(limpio.length() <= maximo, campo + " admite hasta " + maximo + " caracteres.");
        return limpio;
    }

    static BigDecimal numero(BigDecimal valor, String campo, int escala, boolean positivo) {
        exigir(valor != null && (positivo ? valor.signum() > 0 : valor.signum() >= 0),
                campo + (positivo ? " debe ser mayor que cero." : " no puede ser negativo."));
        exigir(valor.compareTo(new BigDecimal("99999999.9999")) <= 0, campo + " es demasiado grande.");
        exigir(valor.scale() <= 100, campo + " contiene demasiados decimales.");
        try {
            return valor.setScale(escala, RoundingMode.UNNECESSARY);
        } catch (ArithmeticException error) {
            throw new IllegalArgumentException(campo + " admite hasta " + escala + " decimales.");
        }
    }

    static BigDecimal dinero(BigDecimal valor, String campo, boolean positivo) {
        BigDecimal resultado = numero(valor, campo, 2, positivo);
        exigir(resultado.compareTo(new BigDecimal("99999999.99")) <= 0, campo + " es demasiado grande.");
        return resultado;
    }
}
