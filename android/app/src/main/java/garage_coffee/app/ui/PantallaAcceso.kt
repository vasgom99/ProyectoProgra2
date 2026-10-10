package garage_coffee.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import garage_coffee.app.R

@Composable
fun PantallaAcceso(
    usuario: String,
    clave: String,
    mensaje: String,
    cargando: Boolean,
    alCambiarUsuario: (String) -> Unit,
    alCambiarClave: (String) -> Unit,
    alIngresar: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GarageCream)
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .background(GarageGreen)
                .padding(28.dp),
            contentAlignment = Alignment.BottomStart
        ) {
            Column {
                Text(
                    text = stringResource(R.string.marca_abreviada),
                    color = ColorArena,
                    fontSize = 42.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(R.string.marca_nombre),
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(R.string.equipo_servicio_mayusculas),
                    color = GarageLight,
                    fontSize = 12.sp
                )
            }
        }

        Column(
            modifier = Modifier.padding(28.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = stringResource(R.string.bienvenido),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = stringResource(R.string.instruccion_acceso),
                color = GarageMuted
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = usuario,
                onValueChange = alCambiarUsuario,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.usuario)) },
                singleLine = true,
                enabled = !cargando,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )
            OutlinedTextField(
                value = clave,
                onValueChange = alCambiarClave,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.contrasena)) },
                singleLine = true,
                enabled = !cargando,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { alIngresar() })
            )
            Button(
                onClick = alIngresar,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                enabled = !cargando
            ) {
                Text(stringResource(R.string.entrar))
            }
            if (cargando) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(32.dp)
                        .align(Alignment.CenterHorizontally),
                    strokeWidth = 3.dp
                )
            }
            if (mensaje.isNotBlank()) {
                Text(text = mensaje, color = MaterialTheme.colorScheme.error)
            }
            Spacer(Modifier.height(12.dp))
            Text(
                text = stringResource(R.string.uso_interno),
                color = GarageMuted,
                fontSize = 12.sp
            )
        }
    }
}

private val ColorArena = androidx.compose.ui.graphics.Color(0xFFD5B894)

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun VistaPreviaAcceso() {
    GarageCoffeeTheme {
        PantallaAcceso("", "", "", false, {}, {}, {})
    }
}
