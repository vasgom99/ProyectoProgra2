package garage_coffee.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;

// Entrada del empleado a la app interna.
public class AccesoActivity extends AppCompatActivity {
    private TextInputEditText usuario, clave;
    private Button entrar;
    private TextView mensaje;
    private ProgressBar progreso;
    @Override protected void onCreate(Bundle estado) {
        super.onCreate(estado);
        setContentView(R.layout.activity_acceso);
        usuario = findViewById(R.id.usuario);
        clave = findViewById(R.id.clave);
        entrar = findViewById(R.id.entrar);
        mensaje = findViewById(R.id.mensaje);
        progreso = findViewById(R.id.progreso);
        entrar.setOnClickListener(v -> ingresar());
    }
    private void ingresar() {
        String nombre = usuario.getText() == null ? "" : usuario.getText().toString().trim();
        String password = clave.getText() == null ? "" : clave.getText().toString();
        if (nombre.isEmpty() || password.isEmpty()) {
            mensaje.setText("Escribe tu usuario y contraseña.");
            return;
        }
        entrar.setEnabled(false);
        progreso.setVisibility(View.VISIBLE);
        mensaje.setText("");
        ((SesionApp) getApplication()).api().ingresar(nombre, password, new ApiCliente.Respuesta<>() {
            @Override public void correcto(String nombreValido) {
                if (isFinishing() || isDestroyed()) return;
                clave.setText("");
                progreso.setVisibility(View.GONE);
                startActivity(new Intent(AccesoActivity.this, InicioActivity.class));
                finish();
            }
            @Override public void error(String texto) {
                if (isFinishing() || isDestroyed()) return;
                clave.setText("");
                progreso.setVisibility(View.GONE);
                entrar.setEnabled(true);
                mensaje.setText(texto);
            }
        });
    }
}
