package garage_coffee.app.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val GarageGreen = Color(0xFF243B32)
val GarageCream = Color(0xFFF6F4EE)
val GarageLight = Color(0xFFE7EEE8)
val GarageAccent = Color(0xFFAE6040)
val GarageMuted = Color(0xFF6D7972)

private val colores = lightColorScheme(
    primary = GarageGreen,
    onPrimary = Color.White,
    secondary = GarageAccent,
    onSecondary = Color.White,
    background = GarageCream,
    onBackground = GarageGreen,
    surface = Color.White,
    onSurface = GarageGreen,
    error = GarageAccent
)

@Composable
fun GarageCoffeeTheme(contenido: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = colores,
        typography = Typography(),
        content = contenido
    )
}
