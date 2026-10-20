package garage_coffee.app

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import garage_coffee.app.ui.GarageCoffeeTheme
import garage_coffee.app.ui.MesaUi
import garage_coffee.app.ui.DetallePedidoUi
import garage_coffee.app.ui.PantallaInicio
import garage_coffee.app.ui.PantallaPedidoMesa
import garage_coffee.app.ui.PedidoMesaUi
import garage_coffee.app.ui.ProductoUi
import garage_coffee.app.ui.Seccion
import org.json.JSONArray
import org.json.JSONObject

// muestra las opciones del empleado.
class InicioActivity : ComponentActivity() {
    private lateinit var sesion: SesionApp
    private var seccion by mutableStateOf(Seccion.MESAS)
    private var mesas by mutableStateOf<List<MesaUi>>(emptyList())
    private var productos by mutableStateOf<List<ProductoUi>>(emptyList())
    private var mesaSeleccionada by mutableStateOf<MesaUi?>(null)
    private var pedidoActivo by mutableStateOf<PedidoMesaUi?>(null)
    private var mensaje by mutableStateOf("")
    private var cargando by mutableStateOf(false)
    private var consultaPedidoEnCurso = false
    private val actualizador = Handler(Looper.getMainLooper())
    private val refrescarPedido = object : Runnable {
        override fun run() {
            if (mesaSeleccionada == null || isFinishing || isDestroyed) return
            cargarPedidoActivo(true)
            actualizador.postDelayed(this, 4000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sesion = application as SesionApp
        if (!sesion.api().iniciada()) {
            volverAlAcceso()
            return
        }

        val rol = sesion.api().rol().orEmpty()
        val puedeVerMesas = rol != "COCINA" && rol != "REPARTIDOR"
        val puedeVerCatalogo = rol != "REPARTIDOR"

        setContent {
            GarageCoffeeTheme {
                val mesa = mesaSeleccionada
                if (mesa == null) {
                    PantallaInicio(
                        usuario = sesion.api().usuario().orEmpty(),
                        rol = rol,
                        seccion = seccion,
                        puedeVerMesas = puedeVerMesas,
                        puedeVerCatalogo = puedeVerCatalogo,
                        mesas = mesas,
                        productos = productos,
                        mensaje = mensaje,
                        cargando = cargando,
                        alSeleccionar = ::mostrar,
                        alAbrirMesa = ::abrirMesa,
                        alActualizar = { mostrar(seccion) },
                        alSalir = {
                            sesion.api().salir()
                            volverAlAcceso()
                        }
                    )
                } else {
                    PantallaPedidoMesa(
                        mesa = mesa,
                        productos = productos,
                        pedido = pedidoActivo,
                        mensaje = mensaje,
                        cargando = cargando,
                        puedeCambiarEstado = rol == "ADMINISTRADOR" || rol == "MESERO",
                        alVolver = {
                            detenerActualizacion()
                            mesaSeleccionada = null
                            pedidoActivo = null
                            mensaje = ""
                        },
                        alActualizarPedido = { cargarPedidoActivo(false) },
                        alCambiarEstado = ::cambiarEstado,
                        alEntregar = ::entregarPedido,
                        alCobrar = ::cobrarPedido,
                        alEnviar = ::enviarPedido
                    )
                }
            }
        }

        when {
            puedeVerMesas -> mostrar(Seccion.MESAS)
            puedeVerCatalogo -> mostrar(Seccion.CATALOGO)
            else -> mensaje = getString(R.string.sin_consultas_rol)
        }
    }

    private fun mostrar(nueva: Seccion) {
        detenerActualizacion()
        mesaSeleccionada = null
        pedidoActivo = null
        seccion = nueva
        mensaje = ""
        cargando = true
        if (nueva == Seccion.MESAS) {
            sesion.api().mesas(respuesta(nueva) { mesas = convertirMesas(it) })
        } else {
            sesion.api().catalogo(respuesta(nueva) { productos = convertirProductos(it) })
        }
    }

    private fun abrirMesa(mesa: MesaUi) {
        mesaSeleccionada = mesa
        pedidoActivo = null
        mensaje = ""
        if (productos.isEmpty()) {
            cargando = true
            sesion.api().catalogo(object : ApiCliente.Respuesta<JSONArray> {
                override fun correcto(datos: JSONArray) {
                    if (isFinishing || isDestroyed || mesaSeleccionada == null) return
                    cargando = false
                    productos = convertirProductos(datos)
                    if (productos.isEmpty()) mensaje = getString(R.string.sin_productos)
                }
                override fun error(texto: String) {
                    if (isFinishing || isDestroyed) return
                    cargando = false
                    mensaje = texto
                }
            })
        }
        cargarPedidoActivo(false)
        detenerActualizacion()
        actualizador.postDelayed(refrescarPedido, 4000)
    }

    private fun cargarPedidoActivo(silencioso: Boolean) {
        val mesa = mesaSeleccionada ?: return
        if (consultaPedidoEnCurso) return
        consultaPedidoEnCurso = true
        if (!silencioso) cargando = true
        sesion.api().pedidoActivo(mesa.id, object : ApiCliente.Respuesta<JSONObject> {
            override fun correcto(datos: JSONObject) {
                consultaPedidoEnCurso = false
                if (isFinishing || isDestroyed || mesaSeleccionada?.id != mesa.id) return
                pedidoActivo = convertirPedido(datos)
                if (!silencioso) cargando = false
                actualizarMesaAbierta()
            }
            override fun error(texto: String) {
                consultaPedidoEnCurso = false
                if (isFinishing || isDestroyed || mesaSeleccionada?.id != mesa.id) return
                if (!silencioso) {
                    cargando = false
                    mensaje = texto
                }
            }
        })
    }

    private fun actualizarMesaAbierta() {
        val id = mesaSeleccionada?.id ?: return
        sesion.api().mesas(object : ApiCliente.Respuesta<JSONArray> {
            override fun correcto(datos: JSONArray) {
                if (isFinishing || isDestroyed) return
                val nuevas = convertirMesas(datos)
                mesas = nuevas
                mesaSeleccionada = nuevas.firstOrNull { it.id == id } ?: mesaSeleccionada
            }
            override fun error(texto: String) { }
        })
    }

    private fun enviarPedido(observaciones: String, cantidades: Map<Int, Int>) {
        val mesa = mesaSeleccionada ?: return
        val pedido = pedidoActivo
        mensaje = ""
        cargando = true
        if (pedido?.activo == true) {
            sesion.api().agregarAlPedido(pedido.id, observaciones, cantidades,
                object : ApiCliente.Respuesta<JSONObject> {
                    override fun correcto(datos: JSONObject) {
                        if (isFinishing || isDestroyed || mesaSeleccionada?.id != mesa.id) return
                        pedidoActivo = convertirPedido(datos)
                        cargando = false
                        mensaje = getString(R.string.productos_agregados)
                    }
                    override fun error(texto: String) {
                        if (isFinishing || isDestroyed) return
                        cargando = false
                        mensaje = texto
                    }
                })
            return
        }
        sesion.api().crearPedido(mesa.id, observaciones, cantidades,
            object : ApiCliente.Respuesta<JSONObject> {
                override fun correcto(datos: JSONObject) {
                    if (isFinishing || isDestroyed) return
                    mesaSeleccionada = null
                    mostrar(Seccion.MESAS)
                    mensaje = getString(R.string.pedido_enviado)
                }
                override fun error(texto: String) {
                    if (isFinishing || isDestroyed) return
                    cargando = false
                    mensaje = texto
                }
            })
    }

    private fun cambiarEstado(nuevo: String) {
        val mesa = mesaSeleccionada ?: return
        mensaje = ""
        cargando = true
        sesion.api().cambiarEstadoMesa(mesa.id, nuevo, object : ApiCliente.Respuesta<JSONObject> {
            override fun correcto(datos: JSONObject) {
                if (isFinishing || isDestroyed) return
                val actualizada = convertirMesa(datos)
                mesas = mesas.map { if (it.id == actualizada.id) actualizada else it }
                mesaSeleccionada = actualizada
                cargando = false
            }
            override fun error(texto: String) {
                if (isFinishing || isDestroyed) return
                cargando = false
                mensaje = texto
            }
        })
    }

    private fun entregarPedido(id: Int) {
        mensaje = ""
        cargando = true
        sesion.api().entregarPedido(id, object : ApiCliente.Respuesta<JSONObject> {
            override fun correcto(datos: JSONObject) {
                if (isFinishing || isDestroyed) return
                pedidoActivo = convertirPedido(datos)
                cargando = false
            }
            override fun error(texto: String) {
                if (isFinishing || isDestroyed) return
                cargando = false
                mensaje = texto
            }
        })
    }

    private fun cobrarPedido(id: Int, efectivo: String) {
        if (efectivo.toBigDecimalOrNull() == null) {
            mensaje = getString(R.string.efectivo_invalido)
            return
        }
        mensaje = ""
        cargando = true
        sesion.api().cobrarPedido(id, efectivo, object : ApiCliente.Respuesta<JSONObject> {
            override fun correcto(datos: JSONObject) {
                if (isFinishing || isDestroyed) return
                detenerActualizacion()
                val cambio = datos.optString("cambio", "0.00")
                mesaSeleccionada = null
                pedidoActivo = null
                mostrar(Seccion.MESAS)
                mensaje = getString(R.string.cobro_realizado, cambio)
            }
            override fun error(texto: String) {
                if (isFinishing || isDestroyed) return
                cargando = false
                mensaje = texto
            }
        })
    }

    private fun respuesta(
        esperada: Seccion,
        recibir: (JSONArray) -> Unit
    ) = object : ApiCliente.Respuesta<JSONArray> {
        override fun correcto(datos: JSONArray) {
            if (isFinishing || isDestroyed || seccion != esperada) return
            cargando = false
            recibir(datos)
            if (datos.length() == 0) {
                mensaje = getString(if (esperada == Seccion.MESAS) R.string.sin_mesas else R.string.sin_productos)
            }
        }

        override fun error(texto: String) {
            if (isFinishing || isDestroyed) return
            if (!sesion.api().iniciada()) {
                volverAlAcceso()
                return
            }
            if (seccion != esperada) return
            cargando = false
            mensaje = texto
        }
    }

    private fun convertirMesas(datos: JSONArray): List<MesaUi> = buildList {
        for (indice in 0 until datos.length()) {
            val mesa = datos.optJSONObject(indice) ?: continue
            add(convertirMesa(mesa))
        }
    }

    private fun convertirMesa(mesa: JSONObject) = MesaUi(
        id = mesa.optInt("id"),
        numero = mesa.optInt("numero"),
        capacidad = mesa.optInt("capacidad"),
        estado = mesa.optString("estado")
    )

    private fun convertirProductos(datos: JSONArray): List<ProductoUi> = buildList {
        for (indice in 0 until datos.length()) {
            val producto = datos.optJSONObject(indice) ?: continue
            add(ProductoUi(
                id = producto.optInt("id"),
                nombre = corregirTexto(producto.optString("nombre")),
                categoria = corregirTexto(producto.optString("categoria")),
                descripcion = corregirTexto(producto.optString("descripcion")),
                precio = producto.optString("precioVigente")
            ))
        }
    }

    private fun convertirPedido(datos: JSONObject): PedidoMesaUi {
        val detalles = datos.optJSONArray("detalles") ?: JSONArray()
        return PedidoMesaUi(
            activo = datos.optBoolean("activo"),
            id = datos.optInt("id"),
            estado = datos.optString("estado"),
            total = datos.optString("total", "0.00"),
            pendiente = datos.optString("pendiente", "0.00"),
            detalles = buildList {
                for (indice in 0 until detalles.length()) {
                    val detalle = detalles.optJSONObject(indice) ?: continue
                    add(DetallePedidoUi(
                        producto = corregirTexto(detalle.optString("producto")),
                        cantidad = detalle.optInt("cantidad"),
                        subtotal = detalle.optString("subtotal")
                    ))
                }
            }
        )
    }

    private fun corregirTexto(valor: String): String {
        if (!valor.contains('Ã') && !valor.contains('Â')) return valor
        return String(valor.toByteArray(Charsets.ISO_8859_1), Charsets.UTF_8)
    }

    private fun detenerActualizacion() {
        actualizador.removeCallbacks(refrescarPedido)
    }

    override fun onDestroy() {
        detenerActualizacion()
        super.onDestroy()
    }

    private fun volverAlAcceso() {
        startActivity(Intent(this, AccesoActivity::class.java))
        finish()
    }
}
