package garage_coffee.app;

import android.app.Application;

// mantiene la sesión en memoria.
public class SesionApp extends Application {
    private final ApiCliente api = new ApiCliente();
    public ApiCliente api() { return api; }
}
