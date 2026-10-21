# Garage Coffee · app interna

La app permite al mesero consultar mesas, tomar pedidos, agregar productos,
marcar entregas y cobrar en efectivo. Los datos pasan por el servidor Spring;
Android no se conecta directamente a MySQL.

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
las borra al pulsar Salir o al cerrar el proceso. Las pantallas se actualizan
mediante el botón **Actualizar**. Si la base no contiene mesas o productos, las
listas aparecen vacías.

La variante debug permite HTTP únicamente para `127.0.0.1`, para probar en
este equipo. La variante release deja la dirección del servidor vacía y no
permite iniciar sesión hasta configurar HTTPS. No usar un celular físico con
esta dirección: es exclusiva del emulador.

El proyecto Android está en `android/app/` y sus archivos Gradle están en
`android/`. Desde esa carpeta se compila con `./gradlew :app:assembleDebug`.
Para compilar y revisar errores de configuración usa
`./gradlew :app:assembleDebug :app:lintDebug`.
Sigue dentro del mismo repositorio que Swing y Spring; no requiere otro Git.
