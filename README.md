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

## Uso diario simplificado

- Cada rol entra a su tarea principal: mesas, cocina o caja. El administrador
  entra al resumen. El menú muestra las tareas diarias; **Más opciones** conserva
  inventario, compras, catálogo, entregas, empleados y reportes según los permisos.
- Desde **Mesas → Tomar pedido**, la mesa queda seleccionada. Agrega productos
  y cantidades en la misma ventana y pulsa **Enviar a cocina**. Para consumo local
  no hace falta registrar un cliente. Dirección y envío aparecen solo a domicilio;
  la programación y los datos adicionales están en **Opciones del pedido**.
- Pedidos muestra los que siguen en curso (incluidos los entregados sin pagar).
  **Más acciones** permite editar, cancelar, asignar reparto y consultar el historial.
- Caja muestra cuentas pendientes y apertura/cierre. **Ver también pagadas**
  recupera las cuentas cobradas para consultar o imprimir su comprobante.
  El cobro propone el total, calcula el cambio y permite un abono solo si se marca
  esa opción. Cobrar no marca el pedido como entregado.
- En una nueva compra, confirma **Mercadería recibida** para guardar y aumentar
  existencias en una sola operación; desmárcala si aún no llegó. **Pagué esta compra
  con efectivo de caja** está desmarcada por defecto: al marcarla, registra también
  el egreso de caja, sin repetirlo como gasto. Requiere caja abierta y saldo suficiente.
  Las tres operaciones se guardan juntas en MySQL o se revierten juntas si fallan.
  Para compras pendientes recibidas posteriormente, el pago sigue registrándose
  por separado en Caja → Más opciones → Movimientos.
- Reservas, direcciones y repartidores quedan en **Mesas → Más opciones**;
  promociones en **Catálogo y recetas → Más opciones**. No se eliminaron funciones.

Los diseños siguen siendo formularios Swing `.java` / `.form` editables en NetBeans.
