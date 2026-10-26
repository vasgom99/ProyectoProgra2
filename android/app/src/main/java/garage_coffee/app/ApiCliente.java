package garage_coffee.app;

import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

// comunica la app con el servidor.
public class ApiCliente {
    public interface Respuesta<T> {
        void correcto(T valor);
        void error(String mensaje);
    }
    private final ExecutorService trabajo = Executors.newSingleThreadExecutor();
    private final Handler pantalla = new Handler(Looper.getMainLooper());
    private final AtomicLong versionSesion = new AtomicLong();
    private String autorizacion;
    private String usuario;
    private String rol;

    public boolean iniciada() { return autorizacion != null; }
    public String usuario() { return usuario; }
    public String rol() { return rol; }
    public void salir() {
        versionSesion.incrementAndGet();
        autorizacion = null;
        usuario = null;
        rol = null;
    }

    public void ingresar(String nombre, String clave, Respuesta<String> respuesta) {
        if (BuildConfig.API_BASE_URL.isEmpty()) {
            respuesta.error("Configura un servidor HTTPS antes de instalar esta versión.");
            return;
        }
        String candidato = "Basic " + Base64.encodeToString(
                (nombre + ":" + clave).getBytes(StandardCharsets.UTF_8), Base64.NO_WRAP);
        long intento = versionSesion.incrementAndGet();
        tarea(() -> {
            JSONObject datos = new JSONObject(pedir("/api/sesion", candidato));
            JSONArray roles = datos.getJSONArray("roles");
            if (roles.length() == 0) throw new IOException("Tu cuenta no tiene un rol asignado.");
            String usuarioValido = datos.getString("usuario");
            String rolValido = roles.getString(0);
            if (intento != versionSesion.get()) throw new IOException("El acceso fue cancelado.");
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
    public void crearPedido(int mesaId, String observaciones, Map<Integer, Integer> cantidades,
            Respuesta<JSONObject> respuesta) {
        tarea(() -> {
            JSONObject pedido = new JSONObject();
            pedido.put("mesaId", mesaId);
            pedido.put("observaciones", observaciones == null ? "" : observaciones);
            JSONArray detalles = new JSONArray();
            for (var entrada : cantidades.entrySet()) {
                JSONObject detalle = new JSONObject();
                detalle.put("productoId", entrada.getKey());
                detalle.put("cantidad", entrada.getValue());
                detalles.put(detalle);
            }
            pedido.put("detalles", detalles);
            return new JSONObject(solicitar("/api/pedidos", "POST", pedido.toString(), autorizacion));
        }, respuesta);
    }
    public void cambiarEstadoMesa(int mesaId, String estado, Respuesta<JSONObject> respuesta) {
        tarea(() -> {
            JSONObject cambio = new JSONObject();
            cambio.put("estado", estado);
            return new JSONObject(solicitar(
                    "/api/mesas/" + mesaId + "/estado", "PUT", cambio.toString(), autorizacion));
        }, respuesta);
    }
    public void agregarAlPedido(int pedidoId, String observaciones, Map<Integer, Integer> cantidades,
            Respuesta<JSONObject> respuesta) {
        tarea(() -> {
            JSONObject entrada = new JSONObject();
            entrada.put("observaciones", observaciones == null ? "" : observaciones);
            JSONArray detalles = new JSONArray();
            for (var cantidad : cantidades.entrySet()) {
                JSONObject detalle = new JSONObject();
                detalle.put("productoId", cantidad.getKey());
                detalle.put("cantidad", cantidad.getValue());
                detalles.put(detalle);
            }
            entrada.put("detalles", detalles);
            return new JSONObject(solicitar(
                    "/api/pedidos/" + pedidoId + "/productos", "PUT", entrada.toString(), autorizacion));
        }, respuesta);
    }
    public void pedidoActivo(int mesaId, Respuesta<JSONObject> respuesta) {
        tarea(() -> new JSONObject(pedir(
                "/api/mesas/" + mesaId + "/pedido-activo", autorizacion)), respuesta);
    }
    public void entregarPedido(int pedidoId, Respuesta<JSONObject> respuesta) {
        tarea(() -> new JSONObject(solicitar(
                "/api/pedidos/" + pedidoId + "/entregar", "PUT", "{}", autorizacion)), respuesta);
    }
    public void cobrarPedido(int pedidoId, String efectivo, Respuesta<JSONObject> respuesta) {
        tarea(() -> {
            JSONObject cobro = new JSONObject();
            cobro.put("efectivo", efectivo);
            return new JSONObject(solicitar(
                    "/api/pedidos/" + pedidoId + "/cobrar", "POST", cobro.toString(), autorizacion));
        }, respuesta);
    }
    private interface Operacion<T> { T ejecutar() throws Exception; }
    private <T> void tarea(Operacion<T> operacion, Respuesta<T> respuesta) {
        trabajo.execute(() -> {
            try {
                T valor = operacion.ejecutar();
                pantalla.post(() -> respuesta.correcto(valor));
            } catch (Exception e) {
                String mensaje = e instanceof SocketTimeoutException
                        ? "El servidor tardó demasiado. Verifica que Spring y MySQL estén iniciados."
                        : e instanceof ConnectException ? "No se pudo conectar. Abre Spring en este equipo."
                        : e.getMessage() == null ? "No se pudo completar la consulta." : e.getMessage();
                pantalla.post(() -> respuesta.error(mensaje));
            }
        });
    }
    private String pedir(String ruta, String credenciales) throws IOException {
        return solicitar(ruta, "GET", null, credenciales);
    }
    private String solicitar(String ruta, String metodo, String cuerpo, String credenciales) throws IOException {
        if (credenciales == null) throw new IOException("Inicia sesión para continuar.");
        HttpURLConnection conexion = (HttpURLConnection) new URL(BuildConfig.API_BASE_URL + ruta).openConnection();
        conexion.setRequestMethod(metodo);
        // permite el primer acceso del servidor.
        conexion.setConnectTimeout(10000);
        conexion.setReadTimeout(15000);
        conexion.setRequestProperty("Authorization", credenciales);
        conexion.setRequestProperty("Accept", "application/json");
        conexion.setUseCaches(false);
        if (cuerpo != null) {
            conexion.setDoOutput(true);
            conexion.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            try (OutputStream salida = conexion.getOutputStream()) {
                salida.write(cuerpo.getBytes(StandardCharsets.UTF_8));
            }
        }
        try {
            int estado = conexion.getResponseCode();
            if (estado == 401) { salir(); throw new IOException("Usuario o contraseña incorrectos."); }
            if (estado == 403) throw new IOException("Tu cuenta no tiene permiso para consultar esta sección.");
            if (estado < 200 || estado >= 300) {
                String detalle = leer(conexion.getErrorStream());
                try { detalle = new JSONObject(detalle).optString("error", detalle); }
                catch (Exception ignorado) { }
                throw new IOException(detalle.isBlank()
                        ? "El servidor respondió con el código " + estado + "." : detalle);
            }
            try (InputStream entrada = conexion.getInputStream()) {
                return new String(entrada.readAllBytes(), StandardCharsets.UTF_8);
            }
        } finally { conexion.disconnect(); }
    }
    private String leer(InputStream entrada) throws IOException {
        if (entrada == null) return "";
        try (entrada) { return new String(entrada.readAllBytes(), StandardCharsets.UTF_8); }
    }
}
