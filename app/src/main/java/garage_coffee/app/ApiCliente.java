package garage_coffee.app;

import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// Consulta el servidor sin bloquear la pantalla y sin guardar datos de acceso en disco.
public class ApiCliente {
    public interface Respuesta<T> {
        void correcto(T valor);
        void error(String mensaje);
    }
    private final ExecutorService trabajo = Executors.newSingleThreadExecutor();
    private final Handler pantalla = new Handler(Looper.getMainLooper());
    private String autorizacion;
    private String usuario;
    private String rol;

    public boolean iniciada() { return autorizacion != null; }
    public String usuario() { return usuario; }
    public String rol() { return rol; }
    public void salir() { autorizacion = null; usuario = null; rol = null; }

    public void ingresar(String nombre, String clave, Respuesta<String> respuesta) {
        if (BuildConfig.API_BASE_URL.isEmpty()) {
            respuesta.error("Configura un servidor HTTPS antes de instalar esta versión.");
            return;
        }
        String candidato = "Basic " + Base64.encodeToString(
                (nombre + ":" + clave).getBytes(StandardCharsets.UTF_8), Base64.NO_WRAP);
        tarea(() -> {
            JSONObject datos = new JSONObject(pedir("/api/sesion", candidato));
            JSONArray roles = datos.getJSONArray("roles");
            if (roles.length() == 0) throw new IOException("Tu cuenta no tiene un rol asignado.");
            String usuarioValido = datos.getString("usuario");
            String rolValido = roles.getString(0);
            autorizacion = candidato;
            usuario = usuarioValido;
            rol = rolValido;
            return usuarioValido;
        }, respuesta);
    }
    public void mesas(Respuesta<JSONArray> respuesta) {
        tarea(() -> new JSONArray(pedir("/api/mesas", autorizacion)), respuesta);
    }
    public void catalogo(Respuesta<JSONArray> respuesta) {
        tarea(() -> new JSONArray(pedir("/api/catalogo", autorizacion)), respuesta);
    }
    private interface Operacion<T> { T ejecutar() throws Exception; }
    private <T> void tarea(Operacion<T> operacion, Respuesta<T> respuesta) {
        trabajo.execute(() -> {
            try {
                T valor = operacion.ejecutar();
                pantalla.post(() -> respuesta.correcto(valor));
            } catch (Exception e) {
                String mensaje = e instanceof SocketTimeoutException ? "El servidor tardó demasiado. Intenta de nuevo."
                        : e instanceof ConnectException ? "No se pudo conectar. Abre Spring en este equipo."
                        : e.getMessage() == null ? "No se pudo completar la consulta." : e.getMessage();
                pantalla.post(() -> respuesta.error(mensaje));
            }
        });
    }
    private String pedir(String ruta, String credenciales) throws IOException {
        if (credenciales == null) throw new IOException("Inicia sesión para continuar.");
        HttpURLConnection conexion = (HttpURLConnection) new URL(BuildConfig.API_BASE_URL + ruta).openConnection();
        conexion.setRequestMethod("GET");
        conexion.setConnectTimeout(5000);
        conexion.setReadTimeout(5000);
        conexion.setRequestProperty("Authorization", credenciales);
        conexion.setRequestProperty("Accept", "application/json");
        conexion.setUseCaches(false);
        try {
            int estado = conexion.getResponseCode();
            if (estado == 401) { salir(); throw new IOException("Usuario o contraseña incorrectos."); }
            if (estado == 403) throw new IOException("Tu cuenta no tiene permiso para consultar esta sección.");
            if (estado != 200) throw new IOException("El servidor respondió con el código " + estado + ".");
            try (InputStream entrada = conexion.getInputStream()) {
                return new String(entrada.readAllBytes(), StandardCharsets.UTF_8);
            }
        } finally { conexion.disconnect(); }
    }
}
