package garage_coffee.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import garage_coffee.app.R
import java.math.BigDecimal
import java.math.RoundingMode

enum class Seccion { MESAS, CATALOGO }

data class MesaUi(val id: Int, val numero: Int, val capacidad: Int, val estado: String)

data class ProductoUi(
    val id: Int,
    val nombre: String,
    val categoria: String,
    val descripcion: String,
    val precio: String
)

data class DetallePedidoUi(val producto: String, val cantidad: Int, val subtotal: String)

data class PedidoMesaUi(
    val activo: Boolean,
    val id: Int,
    val estado: String,
    val total: String,
    val pendiente: String,
    val detalles: List<DetallePedidoUi>
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
    alAbrirMesa: (MesaUi) -> Unit,
    alActualizar: () -> Unit,
    alSalir: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().background(GarageCream)
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        Cabecera(usuario, rol)
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            BotonSeccion(stringResource(R.string.mesas), seccion == Seccion.MESAS,
                puedeVerMesas, Modifier.weight(1f)) { alSeleccionar(Seccion.MESAS) }
            BotonSeccion(stringResource(R.string.catalogo), seccion == Seccion.CATALOGO,
                puedeVerCatalogo, Modifier.weight(1f)) { alSeleccionar(Seccion.CATALOGO) }
        }
        MensajeEstado(mensaje, cargando)
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (seccion == Seccion.MESAS) {
                items(mesas, key = { it.id }) { MesaTarjeta(it) { alAbrirMesa(it) } }
            } else {
                items(productos, key = { it.id }) { ProductoTarjeta(it) }
            }
        }
        HorizontalDivider()
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = alActualizar,
                modifier = Modifier.weight(1f),
                enabled = !cargando && (puedeVerMesas || puedeVerCatalogo)
            ) { Text(stringResource(R.string.actualizar)) }
            OutlinedButton(onClick = alSalir, modifier = Modifier.weight(1f)) {
                Text(stringResource(R.string.salir))
            }
        }
    }
}
@Composable
fun PantallaPedidoMesa(
    mesa: MesaUi,
    productos: List<ProductoUi>,
    pedido: PedidoMesaUi?,
    mensaje: String,
    cargando: Boolean,
    puedeCambiarEstado: Boolean,
    alVolver: () -> Unit,
    alActualizarPedido: () -> Unit,
    alCambiarEstado: (String) -> Unit,
    alEntregar: (Int) -> Unit,
    alCobrar: (Int, String) -> Unit,
    alEnviar: (String, Map<Int, Int>) -> Unit
) {
    val edicion = "${mesa.id}:${pedido?.id ?: 0}:${pedido?.total.orEmpty()}"
    val cantidades = remember(edicion) { mutableStateMapOf<Int, Int>() }
    var observaciones by remember(edicion) { mutableStateOf("") }
    val total = productos.fold(BigDecimal.ZERO) { suma, producto ->
        suma + (producto.precio.toBigDecimalOrNull() ?: BigDecimal.ZERO)
            .multiply(BigDecimal.valueOf((cantidades[producto.id] ?: 0).toLong()))
    }.setScale(2, RoundingMode.HALF_UP)
    val permitePedido = mesa.estado == "LIBRE" || pedido?.activo == true

    Column(
        modifier = Modifier.fillMaxSize().background(GarageCream)
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        Surface(color = GarageGreen, contentColor = MaterialTheme.colorScheme.onPrimary) {
            Column(Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 16.dp)) {
                TextButton(onClick = alVolver, colors = ButtonDefaults.textButtonColors(contentColor = GarageLight)) {
                    Text(stringResource(R.string.volver_mesas))
                }
                Text(stringResource(R.string.mesa_nombre, mesa.numero), fontSize = 25.sp, fontWeight = FontWeight.Bold)
                Text(
                    pluralStringResource(R.plurals.mesa_detalle, mesa.capacidad, mesa.capacidad,
                        mesa.estado.replace('_', ' ')),
                    color = GarageLight
                )
            }
        }
        if (puedeCambiarEstado && mesa.estado != "OCUPADA") {
            OutlinedButton(
                onClick = { alCambiarEstado(if (mesa.estado == "LIBRE") "FUERA_SERVICIO" else "LIBRE") },
                enabled = !cargando,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(stringResource(if (mesa.estado == "LIBRE") R.string.poner_fuera_servicio else R.string.poner_en_servicio))
            }
        }
        if (!permitePedido) {
            Text(
                text = stringResource(if (mesa.estado == "OCUPADA") R.string.mesa_ocupada_aviso else R.string.mesa_fuera_aviso),
                color = GarageAccent,
                modifier = Modifier.padding(horizontal = 18.dp, vertical = 8.dp)
            )
        }
        if (pedido?.activo == true) {
            PedidoActivoTarjeta(pedido, cargando, alActualizarPedido, alEntregar, alCobrar)
        }
        MensajeEstado(mensaje, cargando)
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Text(stringResource(R.string.menu), fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(4.dp))
                Text(stringResource(R.string.instruccion_pedido), color = GarageMuted)
            }
            items(productos, key = { it.id }) { producto ->
                ProductoPedidoTarjeta(
                    producto = producto,
                    cantidad = cantidades[producto.id] ?: 0,
                    habilitado = permitePedido && !cargando,
                    alCambiar = { cantidad ->
                        if (cantidad <= 0) cantidades.remove(producto.id)
                        else cantidades[producto.id] = cantidad.coerceAtMost(9999)
                    }
                )
            }
            item {
                OutlinedTextField(
                    value = observaciones,
                    onValueChange = { if (it.length <= 255) observaciones = it },
                    label = { Text(stringResource(R.string.observaciones)) },
                    enabled = permitePedido && !cargando,
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2
                )
                Spacer(Modifier.height(14.dp))
                Text(
                    stringResource(R.string.total_pedido, total.toPlainString()),
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold,
                    color = GarageGreen
                )
                Spacer(Modifier.height(10.dp))
                Button(
                    onClick = { alEnviar(observaciones, cantidades.toMap()) },
                    enabled = permitePedido && cantidades.isNotEmpty() && !cargando,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(if (pedido?.activo == true) R.string.agregar_al_pedido else R.string.enviar_cocina))
                }
            }
        }
    }
}

@Composable
private fun PedidoActivoTarjeta(
    pedido: PedidoMesaUi,
    cargando: Boolean,
    alActualizar: () -> Unit,
    alEntregar: (Int) -> Unit,
    alCobrar: (Int, String) -> Unit
) {
    var efectivo by remember(pedido.id, pedido.pendiente) { mutableStateOf(pedido.pendiente) }
    val recibido = efectivo.toBigDecimalOrNull()
    val pendiente = pedido.pendiente.toBigDecimalOrNull() ?: BigDecimal.ZERO
    val cambio = if (recibido != null && recibido >= pendiente) recibido - pendiente else BigDecimal.ZERO
    val estadoVisible = when (pedido.estado) {
        "PENDIENTE" -> stringResource(R.string.estado_pendiente)
        "EN_PREPARACION" -> stringResource(R.string.estado_preparacion)
        "LISTO" -> stringResource(R.string.estado_listo)
        "ENTREGADO" -> stringResource(R.string.estado_entregado)
        else -> pedido.estado.replace('_', ' ')
    }
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (pedido.estado == "LISTO") Color(0xFFDDF2E3) else MaterialTheme.colorScheme.surface
        )
    ) {
        Column(Modifier.fillMaxWidth().padding(16.dp)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text(stringResource(R.string.pedido_numero, pedido.id), fontWeight = FontWeight.Bold, fontSize = 19.sp)
                    Text(estadoVisible, color = if (pedido.estado == "LISTO") Color(0xFF19723B) else GarageAccent,
                        fontWeight = FontWeight.Bold)
                }
                TextButton(onClick = alActualizar, enabled = !cargando) {
                    Text(stringResource(R.string.actualizar))
                }
            }
            pedido.detalles.forEach { detalle ->
                Text(stringResource(R.string.detalle_pedido, detalle.cantidad, detalle.producto, detalle.subtotal),
                    color = GarageMuted)
            }
            Spacer(Modifier.height(8.dp))
            Text(stringResource(R.string.total_pedido, pedido.total), fontWeight = FontWeight.Bold)
            if (pedido.estado == "LISTO") {
                Spacer(Modifier.height(10.dp))
                Button(onClick = { alEntregar(pedido.id) }, enabled = !cargando, modifier = Modifier.fillMaxWidth()) {
                    Text(stringResource(R.string.marcar_entregado))
                }
            }
            if (pedido.estado == "ENTREGADO") {
                Spacer(Modifier.height(10.dp))
                Text(stringResource(R.string.pendiente_cobro, pedido.pendiente), fontWeight = FontWeight.Bold)
                OutlinedTextField(
                    value = efectivo,
                    onValueChange = { efectivo = it.replace(',', '.') },
                    label = { Text(stringResource(R.string.efectivo_recibido)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    enabled = !cargando,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(stringResource(R.string.cambio_estimado, cambio.setScale(2, RoundingMode.HALF_UP).toPlainString()),
                    color = GarageMuted)
                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = { alCobrar(pedido.id, efectivo) },
                    enabled = !cargando && recibido != null && recibido >= pendiente,
                    modifier = Modifier.fillMaxWidth()
                ) { Text(stringResource(R.string.cobrar_cerrar_mesa)) }
            }
        }
    }
}

@Composable
private fun Cabecera(usuario: String, rol: String) {
    Surface(color = GarageGreen, contentColor = MaterialTheme.colorScheme.onPrimary) {
        Column(Modifier.fillMaxWidth().padding(horizontal = 22.dp, vertical = 18.dp)) {
            Text(stringResource(R.string.garage_coffee), fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text(stringResource(R.string.sesion_resumen, usuario, rol), color = GarageLight)
        }
    }
}

@Composable
private fun MensajeEstado(mensaje: String, cargando: Boolean) {
    if (mensaje.isNotBlank()) {
        Text(mensaje, color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp))
    }
    if (cargando) {
        Box(Modifier.fillMaxWidth().padding(10.dp), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}

@Composable
private fun BotonSeccion(texto: String, seleccionado: Boolean, habilitado: Boolean,
    modifier: Modifier, alPulsar: () -> Unit) {
    if (seleccionado) Button(onClick = alPulsar, enabled = habilitado, modifier = modifier) { Text(texto) }
    else OutlinedButton(onClick = alPulsar, enabled = habilitado, modifier = modifier) { Text(texto) }
}

@Composable
private fun MesaTarjeta(mesa: MesaUi, alPulsar: () -> Unit) {
    val color = when (mesa.estado) {
        "LIBRE" -> Color(0xFFE2F1E7)
        "OCUPADA" -> Color(0xFFFFE2D6)
        else -> Color(0xFFE7E7E7)
    }
    Card(onClick = alPulsar, modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = color)) {
        Row(Modifier.fillMaxWidth().padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(stringResource(R.string.mesa_nombre, mesa.numero), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(Modifier.height(4.dp))
                Text(pluralStringResource(R.plurals.mesa_detalle, mesa.capacidad,
                    mesa.capacidad, mesa.estado.replace('_', ' ')), color = GarageMuted)
            }
            Text(stringResource(R.string.abrir), color = GarageGreen, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun ProductoTarjeta(producto: ProductoUi) {
    Card(Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Row(Modifier.fillMaxWidth().padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
            ProductoDescripcion(producto, Modifier.weight(1f))
            Text(stringResource(R.string.precio_quetzales, producto.precio),
                color = GarageAccent, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}

@Composable
private fun ProductoPedidoTarjeta(producto: ProductoUi, cantidad: Int, habilitado: Boolean,
    alCambiar: (Int) -> Unit) {
    Card(Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Column(Modifier.fillMaxWidth().padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                ProductoDescripcion(producto, Modifier.weight(1f))
                Text(stringResource(R.string.precio_quetzales, producto.precio),
                    color = GarageAccent, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(10.dp))
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(onClick = { alCambiar(cantidad - 1) }, enabled = habilitado && cantidad > 0) { Text("−") }
                Text(cantidad.toString(), fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Button(onClick = { alCambiar(cantidad + 1) }, enabled = habilitado && cantidad < 9999) { Text("+") }
            }
        }
    }
}

@Composable
private fun ProductoDescripcion(producto: ProductoUi, modifier: Modifier = Modifier) {
    Column(modifier) {
        Text(producto.nombre, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Spacer(Modifier.height(4.dp))
        Text(stringResource(R.string.producto_detalle, producto.categoria, producto.descripcion), color = GarageMuted)
    }
}
