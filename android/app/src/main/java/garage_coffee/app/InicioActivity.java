package garage_coffee.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import org.json.*;

// Muestra las mesas y los productos disponibles para el equipo.
public class InicioActivity extends AppCompatActivity {
    private SesionApp sesion;
    private LinearLayout lista;
    private TextView mensaje;
    private ProgressBar progreso;
    private Button tabMesas, tabCatalogo;
    private String seccion = "mesas";
    @Override protected void onCreate(Bundle estado) {
        super.onCreate(estado);
        sesion = (SesionApp) getApplication();
        if (!sesion.api().iniciada()) { volverAlAcceso(); return; }
        setContentView(R.layout.activity_inicio);
        lista = findViewById(R.id.lista);
        mensaje = findViewById(R.id.mensaje);
        progreso = findViewById(R.id.progreso);
        tabMesas = findViewById(R.id.tab_mesas);
        tabCatalogo = findViewById(R.id.tab_catalogo);
        ((TextView) findViewById(R.id.subtitulo)).setText(sesion.api().usuario() + " · " + sesion.api().rol());
        boolean puedeVerMesas = !sesion.api().rol().equals("COCINA") && !sesion.api().rol().equals("REPARTIDOR");
        boolean puedeVerCatalogo = !sesion.api().rol().equals("REPARTIDOR");
        tabMesas.setEnabled(puedeVerMesas);
        tabCatalogo.setEnabled(puedeVerCatalogo);
        tabMesas.setOnClickListener(v -> mostrar("mesas"));
        tabCatalogo.setOnClickListener(v -> mostrar("catalogo"));
        findViewById(R.id.actualizar).setOnClickListener(v -> mostrar(seccion));
        findViewById(R.id.salir).setOnClickListener(v -> { sesion.api().salir(); volverAlAcceso(); });
        if (puedeVerMesas) mostrar("mesas");
        else if (puedeVerCatalogo) mostrar("catalogo");
        else mensaje.setText("Tu rol aún no tiene consultas disponibles en la app.");
    }
    private void mostrar(String nueva) {
        seccion = nueva;
        lista.removeAllViews();
        mensaje.setText("");
        progreso.setVisibility(View.VISIBLE);
        if (nueva.equals("mesas")) sesion.api().mesas(respuesta(nueva, this::dibujarMesas));
        else sesion.api().catalogo(respuesta(nueva, this::dibujarCatalogo));
    }
    private ApiCliente.Respuesta<JSONArray> respuesta(String esperada, java.util.function.Consumer<JSONArray> pintar) {
        return new ApiCliente.Respuesta<>() {
            @Override public void correcto(JSONArray datos) {
                if (isFinishing() || isDestroyed()) return;
                if (!seccion.equals(esperada)) return;
                progreso.setVisibility(View.GONE);
                pintar.accept(datos);
            }
            @Override public void error(String texto) {
                if (isFinishing() || isDestroyed()) return;
                if (!sesion.api().iniciada()) { volverAlAcceso(); return; }
                if (!seccion.equals(esperada)) return;
                progreso.setVisibility(View.GONE);
                mensaje.setText(texto);
            }
        };
    }
    private void dibujarMesas(JSONArray datos) {
        if (datos.length() == 0) { mensaje.setText("Todavía no hay mesas registradas."); return; }
        for (int i = 0; i < datos.length(); i++) {
            JSONObject mesa = datos.optJSONObject(i);
            if (mesa == null) continue;
            View fila = LayoutInflater.from(this).inflate(R.layout.item_mesa, lista, false);
            ((TextView) fila.findViewById(R.id.nombre)).setText("Mesa " + mesa.optInt("numero"));
            ((TextView) fila.findViewById(R.id.detalle)).setText(mesa.optInt("capacidad") + " personas · " + mesa.optString("estado").replace('_', ' '));
            lista.addView(fila);
        }
    }
    private void dibujarCatalogo(JSONArray datos) {
        if (datos.length() == 0) { mensaje.setText("Todavía no hay productos disponibles."); return; }
        for (int i = 0; i < datos.length(); i++) {
            JSONObject producto = datos.optJSONObject(i);
            if (producto == null) continue;
            View fila = LayoutInflater.from(this).inflate(R.layout.item_producto, lista, false);
            ((TextView) fila.findViewById(R.id.nombre)).setText(producto.optString("nombre"));
            ((TextView) fila.findViewById(R.id.detalle)).setText(producto.optString("categoria") + " · " + producto.optString("descripcion"));
            ((TextView) fila.findViewById(R.id.precio)).setText("Q " + producto.optString("precioVigente"));
            lista.addView(fila);
        }
    }
    private void volverAlAcceso() {
        startActivity(new Intent(this, AccesoActivity.class));
        finish();
    }
}
