# Garage Coffee

Este repositorio contiene tres partes: Swing (`pom.xml` de la raíz), el servidor
Spring (`backend/`) y Android (`android/`). Para abrir **Swing no hay que iniciar
Spring**. La app Android sí consulta Spring.

## Revisar Swing sin MySQL

Abre la carpeta raíz como proyecto Maven en NetBeans y ejecuta
`com.garagecoffee.Proyecto2Progra2` con JDK 25 o superior. Si MySQL no está
disponible, elige **Sí** cuando se ofrezca la demostración. Ingresa con usuario
`admin` y contraseña `demo`. Puedes recorrer las pantallas, pero los datos se
pierden al cerrar. Para entrar directamente en este modo, usa el argumento
`--demo`.

La configuración Gradle de Android está únicamente en `android/`. Si NetBeans
todavía muestra las importaciones antiguas `garage_coffee` o
`Proyecto2Progra2:app`, reinicia NetBeans para que vuelva a reconocer la raíz.
Abre `Proyecto2Progra2` desde esa carpeta si no se abre automáticamente.
El proyecto debe mostrar el icono de Maven y su nombre `Proyecto2Progra2`.
La acción **Run Project / Ejecutar proyecto** está fijada en `nbactions.xml`
para abrir la clase principal Swing desde la raíz, donde está la configuración
local de MySQL.

También se puede abrir desde una terminal situada en la raíz:

```sh
./backend/mvnw -f pom.xml process-classes exec:exec
```

En Windows, el mismo comando comienza con `backend\mvnw.cmd`.

## Usar Swing con datos permanentes

En cada equipo hay que instalar e iniciar MySQL e importar el esquema de
Garage Coffee. El script original para crear las tablas no está incluido
actualmente en Git; el equipo debe compartirlo por separado para usar este
modo. Copia `garage-db.properties.example` como `garage-db.properties` en la
raíz y pon la dirección, usuario y contraseña de **ese equipo**. El archivo
con la contraseña no se sube a Git. Ejecuta una vez
`com.garagecoffee.database.PrepararBaseDatos` y después inicia Swing
normalmente, sin `--demo`.

Spring no sustituye esta conexión: por ahora Swing accede a MySQL directamente.
Para la parte móvil, consulta las instrucciones de `backend/README.md` y
`android/app/README.md`. En Android Studio se abre `android/` como proyecto
Gradle, sin moverlo a otro repositorio.
