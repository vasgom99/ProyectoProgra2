# Servidor Garage Coffee

Primera etapa: acceso de empleados y consultas de mesas y catálogo. Spring no abre
pantallas Swing, no crea tablas y no publica todavía creación de pedidos ni cobros.

## Ejecutar

Usar un JDK 25 o superior tanto en el proyecto como en Maven de IntelliJ.
Desde la carpeta backend:

```sh
./mvnw -f ../pom.xml install
./mvnw spring-boot:run
```

El primer comando compila e instala localmente el proyecto existente como biblioteca.
Repetirlo cuando cambien sus modelos, DAO o servicios. No copia código al backend.
Después se puede ejecutar BackendApplication desde IntelliJ, con backend como
directorio de trabajo. Si IntelliJ no encuentra Proyecto2Progra2, recargar Maven
después de ejecutar el primer comando.

La configuración local es garage-db.properties, situada en la raíz del repositorio
y excluida de Git. Spring la lee al ejecutar desde la raíz o desde backend. Para
otra ubicación, indicar spring.config.additional-location con la ruta del archivo.
No colocar la contraseña en application.properties.

## Rutas disponibles

| Método y ruta | Acceso |
| --- | --- |
| GET /api/estado | Público; indica que el servidor responde, no garantiza acceso a MySQL |
| GET /api/sesion | Cualquier empleado activo; devuelve usuario y rol |
| GET /api/mesas | Administración, mesero y cajero |
| GET /api/catalogo | Administración, mesero, cajero y cocina |

El catálogo muestra solo productos disponibles de categorías activas e incluye
precio base y precio vigente. Reutiliza la regla de ofertas del proyecto Swing.
Una consulta sin acceso devuelve 401, un rol no autorizado 403 y un fallo de consulta
503, sin exponer SQL. No se envían contraseñas ni objetos de empleado completos.

En esta etapa se usa HTTP Basic, con el usuario y contraseña del empleado de MySQL,
no las credenciales de conexión a MySQL. Las solicitudes se autentican por separado;
no hay un usuario global compartido ni se emiten tokens de sesión.

Por seguridad escucha solamente en 127.0.0.1:8080. HTTP Basic no cifra las claves:
no cambiar a una dirección de red ni usar desde el celular sin configurar HTTPS.
La sesión móvil definitiva queda para la siguiente etapa.
Se conserva la protección CSRF y todas las rutas de escritura están bloqueadas.

## Comprobación manual

Inicia Spring, abre `http://127.0.0.1:8080/api/estado` y confirma que responda
`servidor: disponible`. Luego consulta `/api/sesion`, `/api/mesas` y
`/api/catalogo` desde un cliente HTTP con autenticación Basic y la cuenta
de un empleado activo. Sin credenciales deben responder 401. Si no hay mesas
o productos registrados, las rutas respectivas devuelven listas vacías.

En Swing, entra con un empleado y recorre el flujo de pedido, preparación y
cobro. Comprueba en DBeaver que los registros permanezcan al cerrar y abrir
de nuevo. Las clases de prueba se retiraron para que el equipo haga estas
comprobaciones directamente.

## Continuación

1. Implementar pedidos con identidad del mesero, validación y transacciones.
2. Registrar cobros con caja abierta y protección contra duplicados; el efectivo
   se entrega directamente a caja. No habilitar al mesero gastos ni cierre de caja.
3. Preparar HTTPS y acceso móvil; conectar Android.
4. Mantener Swing con JDBC directo a MySQL; Spring sirve a la app móvil y no es necesario para abrir Swing.

Mientras Swing y Spring coexistan, no implementar escrituras nuevas ignorando
control_cambios y las reglas del servicio. El servidor no debe compartir un
RestauranteController de escritorio como singleton entre varios empleados.
