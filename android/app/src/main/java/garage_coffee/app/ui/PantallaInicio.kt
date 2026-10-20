package garage_coffee.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import garage_coffee.app.R

enum class Seccion { MESAS, CATALOGO }

data class MesaUi(val numero: Int, val capacidad: Int, val estado: String)

data class ProductoUi(
    val nombre: String,
    val categoria: String,
    val descripcion: String,
    val precio: String
)

@Composable
fun PantallaInicio(
    usuario: String,
    rol: String,
    seccion: Seccion,
    puedeVerMesas: Boolean,
    puedeVerCatalogo: Boolean,
    mesas: List<MesaUi>,
    productos: List<ProductoUi>,
    mensaje: String,
    cargando: Boolean,
    alSeleccionar: (Seccion) -> Unit,
    alActualizar: () -> Unit,
    alSalir: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GarageCream)
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        Surface(color = GarageGreen, contentColor = MaterialTheme.colorScheme.onPrimary) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 22.dp, vertical = 18.dp)
            ) {
                Text(
                    text = stringResource(R.string.garage_coffee),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(R.string.sesion_resumen, usuario, rol),
                    color = GarageLight
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            BotonSeccion(
                texto = stringResource(R.string.mesas),
                seleccionado = seccion == Seccion.MESAS,
                habilitado = puedeVerMesas,
                modifier = Modifier.weight(1f),
                alPulsar = { alSeleccionar(Seccion.MESAS) }
            )
            BotonSeccion(
                texto = stringResource(R.string.catalogo),
                seleccionado = seccion == Seccion.CATALOGO,
                habilitado = puedeVerCatalogo,
                modifier = Modifier.weight(1f),
                alPulsar = { alSeleccionar(Seccion.CATALOGO) }
            )
        }

        if (mensaje.isNotBlank()) {
            Text(
                text = mensaje,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)
            )
        }

        if (cargando) {
            CircularProgressIndicator(
                modifier = Modifier
                    .padding(18.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (seccion == Seccion.MESAS) {
                items(mesas) { MesaTarjeta(it) }
            } else {
                items(productos) { ProductoTarjeta(it) }
            }
        }

        HorizontalDivider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = alActualizar,
                modifier = Modifier.weight(1f),
                enabled = !cargando && (puedeVerMesas || puedeVerCatalogo)
            ) {
                Text(stringResource(R.string.actualizar))
            }
            OutlinedButton(onClick = alSalir, modifier = Modifier.weight(1f)) {
                Text(stringResource(R.string.salir))
            }
        }
    }
}

@Composable
private fun BotonSeccion(
    texto: String,
    seleccionado: Boolean,
    habilitado: Boolean,
    modifier: Modifier,
    alPulsar: () -> Unit
) {
    if (seleccionado) {
        Button(onClick = alPulsar, enabled = habilitado, modifier = modifier) { Text(texto) }
    } else {
        OutlinedButton(onClick = alPulsar, enabled = habilitado, modifier = modifier) { Text(texto) }
    }
}

@Composable
private fun MesaTarjeta(mesa: MesaUi) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                text = stringResource(R.string.mesa_nombre, mesa.numero),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = pluralStringResource(
                    R.plurals.mesa_detalle,
                    mesa.capacidad,
                    mesa.capacidad,
                    mesa.estado.replace('_', ' ')
                ),
                color = GarageMuted
            )
        }
    }
}

@Composable
private fun ProductoTarjeta(producto: ProductoUi) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = producto.nombre, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(Modifier.height(4.dp))
                Text(
                    text = stringResource(
                        R.string.producto_detalle,
                        producto.categoria,
                        producto.descripcion
                    ),
                    color = GarageMuted
                )
            }
            Text(
                text = stringResource(R.string.precio_quetzales, producto.precio),
                color = GarageAccent,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun VistaPreviaInicio() {
    GarageCoffeeTheme {
        PantallaInicio(
            usuario = "mesero.demo",
            rol = "MESERO",
            seccion = Seccion.MESAS,
            puedeVerMesas = true,
            puedeVerCatalogo = true,
            mesas = listOf(MesaUi(1, 4, "LIBRE"), MesaUi(2, 4, "OCUPADA")),
            productos = emptyList(),
            mensaje = "",
            cargando = false,
            alSeleccionar = {},
            alActualizar = {},
            alSalir = {}
        )
    }
}
