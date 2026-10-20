package com.garagecoffee.api;

import java.sql.SQLException;
import java.util.Map;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

// devuelve errores seguros al cliente.
@RestControllerAdvice
public class ErroresApi {
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> datosInvalidos(IllegalArgumentException error) {
        return ResponseEntity.badRequest().body(Map.of("error", error.getMessage()));
    }
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> conflicto(IllegalStateException error) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", error.getMessage()));
    }
    @ExceptionHandler(SQLException.class)
    public ResponseEntity<Map<String, String>> baseNoDisponible(SQLException error) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of("error", "No se pudo consultar la base de datos. Intenta de nuevo."));
    }
}
