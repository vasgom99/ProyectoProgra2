package com.garagecoffee.api;

import java.sql.SQLException;
import java.util.Map;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

// No envía consultas SQL ni detalles internos de la conexión al cliente.
@RestControllerAdvice
public class ErroresApi {
    @ExceptionHandler(SQLException.class)
    public ResponseEntity<Map<String, String>> baseNoDisponible(SQLException error) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of("error", "No se pudo consultar la base de datos. Intenta de nuevo."));
    }
}
