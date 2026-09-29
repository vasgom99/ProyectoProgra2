package garage_coffee.app;

import android.app.Application;

// Mantiene una sesión en memoria; al cerrar la app o salir no guarda contraseñas.
public class SesionApp extends Application {
    private final ApiCliente api = new ApiCliente();
    public ApiCliente api() { return api; }
}
