# Garage Coffee · app interna

Esta primera etapa para Android permite entrar con una cuenta de empleado y
consultar las mesas y los productos disponibles, con precios vigentes. La app
obtiene esos datos del servidor Spring; no tiene acceso directo a MySQL.

La interfaz está construida completamente con Jetpack Compose y Material 3.
`AccesoActivity.kt` e `InicioActivity.kt` coordinan la sesión, mientras que las
funciones `@Composable` están en `app/src/main/java/garage_coffee/app/ui/`.
Las pantallas ya no dependen de archivos de diseño XML.

## Probar en el emulador

1. Iniciar MySQL y luego `BackendApplication` en IntelliJ. Spring escucha en
   `127.0.0.1:8080`.
2. Con el emulador encendido, ejecutar una vez en la terminal de Android Studio:

   ```sh
   adb reverse tcp:8080 tcp:8080
   ```

3. Abrir la carpeta `android/` del repositorio en Android Studio como proyecto
   Gradle y usar el emulador.
4. Ejecutar `app` en variante **debug** e ingresar con una cuenta de empleado
   existente en la base. La contraseña de MySQL no sirve como contraseña de
   empleado.

El puente ADB permite que la app llegue a Spring mediante `127.0.0.1:8080`. Se
debe repetir el comando si se reinicia el emulador. La app no guarda
la contraseña en disco: conserva las credenciales de esta sesión en memoria y
las borra al pulsar Salir o al cerrar el proceso. La lista se actualiza mediante
el botón **Actualizar**. Si la base todavía no contiene mesas o productos, las
listas aparecerán vacías.

La variante debug permite HTTP únicamente para `127.0.0.1`, para probar en
este equipo. La variante release deja la dirección del servidor vacía y no
permite iniciar sesión hasta configurar HTTPS. No usar un celular físico con
esta dirección: es exclusiva del emulador.

## Alcance pendiente

El servidor actual solo ofrece consultas GET. Para tomar pedidos y registrar
cobros habrá que agregar primero esas rutas en Spring, con el mesero
responsable, caja abierta, validación de precios y protección contra cobros
duplicados. Entonces se incorporarán los formularios correspondientes a esta
app. El efectivo recibido por el mesero se entrega inmediatamente a caja.

El proyecto Android está en `android/app/` y sus archivos Gradle están en
`android/`. Desde esa carpeta se compila con `./gradlew :app:assembleDebug`.
Para compilar y revisar errores de configuración usa
`./gradlew :app:assembleDebug :app:lintDebug`.
Sigue dentro del mismo repositorio que Swing y Spring; no requiere otro Git.
