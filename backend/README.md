# Servidor Garage Coffee

Spring comunica la app Android con MySQL. No abre pantallas Swing ni crea tablas.

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
| GET /api/mesas/{id}/pedido-activo | Consulta el servicio abierto de una mesa |
| POST /api/pedidos | Crea un pedido y ocupa la mesa |
| PUT /api/mesas/{id}/estado | Habilita o deshabilita una mesa libre |
| PUT /api/pedidos/{id}/productos | Agrega productos al pedido activo |
| PUT /api/pedidos/{id}/entregar | Marca el pedido listo como entregado |
| POST /api/pedidos/{id}/cobrar | Cobra en efectivo y libera la mesa |

El catálogo muestra solo productos disponibles de categorías activas. Incluye
precio base y vigente con el mismo valor, porque se retiraron las ofertas.
Una consulta sin acceso devuelve 401, un rol no autorizado 403 y un fallo de consulta
503, sin exponer SQL. No se envían contraseñas ni objetos de empleado completos.

Se usa HTTP Basic, con el usuario y contraseña del empleado guardados en MySQL,
no las credenciales de conexión a MySQL. Las solicitudes se autentican por separado;
no hay un usuario global compartido ni se emiten tokens de sesión.

Por seguridad escucha solamente en 127.0.0.1:8080. HTTP Basic no cifra las claves:
no cambiar a una dirección de red ni usar desde el celular sin configurar HTTPS.
Las rutas se autorizan según el rol del empleado.

## Comprobación manual

Inicia Spring, abre `http://127.0.0.1:8080/api/estado` y confirma que responda
`servidor: disponible`. Luego consulta `/api/sesion`, `/api/mesas` y
`/api/catalogo` desde un cliente HTTP con autenticación Basic y la cuenta
de un empleado activo. Sin credenciales deben responder 401. Si no hay mesas
o productos registrados, las rutas respectivas devuelven listas vacías.

En Swing también puede recorrerse manualmente el flujo de pedido, preparación y cobro.

Mientras Swing y Spring coexistan, no implementar escrituras nuevas ignorando
control_cambios y las reglas del servicio. El servidor no debe compartir un
RestauranteController de escritorio como singleton entre varios empleados.
