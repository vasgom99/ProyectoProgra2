package garage_coffee.app

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import garage_coffee.app.ui.GarageCoffeeTheme
import garage_coffee.app.ui.MesaUi
import garage_coffee.app.ui.PantallaInicio
import garage_coffee.app.ui.ProductoUi
import garage_coffee.app.ui.Seccion
import org.json.JSONArray

// Muestra la información permitida para el empleado conectado.
class InicioActivity : ComponentActivity() {
    private lateinit var sesion: SesionApp
    private var seccion by mutableStateOf(Seccion.MESAS)
    private var mesas by mutableStateOf<List<MesaUi>>(emptyList())
    private var productos by mutableStateOf<List<ProductoUi>>(emptyList())
    private var mensaje by mutableStateOf("")
    private var cargando by mutableStateOf(false)

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
                    alActualizar = { mostrar(seccion) },
                    alSalir = {
                        sesion.api().salir()
                        volverAlAcceso()
                    }
                )
            }
        }

        when {
            puedeVerMesas -> mostrar(Seccion.MESAS)
            puedeVerCatalogo -> mostrar(Seccion.CATALOGO)
            else -> mensaje = getString(R.string.sin_consultas_rol)
        }
    }

    private fun mostrar(nueva: Seccion) {
        seccion = nueva
        mensaje = ""
        cargando = true
        if (nueva == Seccion.MESAS) {
            sesion.api().mesas(respuesta(nueva) { mesas = convertirMesas(it) })
        } else {
            sesion.api().catalogo(respuesta(nueva) { productos = convertirProductos(it) })
        }
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
            add(MesaUi(mesa.optInt("numero"), mesa.optInt("capacidad"), mesa.optString("estado")))
        }
    }

    private fun convertirProductos(datos: JSONArray): List<ProductoUi> = buildList {
        for (indice in 0 until datos.length()) {
            val producto = datos.optJSONObject(indice) ?: continue
            add(ProductoUi(
                nombre = producto.optString("nombre"),
                categoria = producto.optString("categoria"),
                descripcion = producto.optString("descripcion"),
                precio = producto.optString("precioVigente")
            ))
        }
    }

    private fun volverAlAcceso() {
        startActivity(Intent(this, AccesoActivity::class.java))
        finish()
    }
}
