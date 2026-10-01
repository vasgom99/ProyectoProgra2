package com.garagecoffee;

import com.garagecoffee.controller.RestauranteController;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

// Solo lectura. Se activa expresamente con -Dgarage.test.mysql=true.
class AuditoriaMySQLTest {
    @Test void cargaTodasLasDaoYRelacionesSinModificarDatos() {
        assumeTrue(Boolean.getBoolean("garage.test.mysql"));
        assertDoesNotThrow(() -> {
            new RestauranteController();
        });
    }
}
