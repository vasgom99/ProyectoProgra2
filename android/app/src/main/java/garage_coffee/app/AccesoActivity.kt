package garage_coffee.app

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import garage_coffee.app.ui.GarageCoffeeTheme
import garage_coffee.app.ui.PantallaAcceso

// permite el acceso del empleado.
class AccesoActivity : ComponentActivity() {
    private var usuario by mutableStateOf("")
    private var clave by mutableStateOf("")
    private var mensaje by mutableStateOf("")
    private var cargando by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GarageCoffeeTheme {
                PantallaAcceso(
                    usuario = usuario,
                    clave = clave,
                    mensaje = mensaje,
                    cargando = cargando,
                    alCambiarUsuario = { usuario = it },
                    alCambiarClave = { clave = it },
                    alIngresar = ::ingresar
                )
            }
        }
    }

    private fun ingresar() {
        val nombre = usuario.trim()
        if (nombre.isEmpty() || clave.isEmpty()) {
            mensaje = getString(R.string.datos_acceso_requeridos)
            return
        }
        cargando = true
        mensaje = ""
        (application as SesionApp).api().ingresar(nombre, clave,
            object : ApiCliente.Respuesta<String> {
                override fun correcto(valor: String) {
                    if (isFinishing || isDestroyed) return
                    clave = ""
                    cargando = false
                    startActivity(Intent(this@AccesoActivity, InicioActivity::class.java))
                    finish()
                }

                override fun error(texto: String) {
                    if (isFinishing || isDestroyed) return
                    clave = ""
                    cargando = false
                    mensaje = texto
                }
            })
    }
}
