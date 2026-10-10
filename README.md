# Garage Coffee

Proyecto académico de restaurante con POO. El programa principal es **Java Swing + JDBC + MySQL**. `backend/` (Spring) y `android/` se conservan como ampliaciones de consulta; **Swing no necesita Spring ni Android para iniciar**.

## Lo que hace el sistema

1. Un empleado entra con su usuario y rol.
2. El administrador registra categorías, productos y mesas.
3. El mesero toma un pedido para una mesa libre.
4. Cocina lo prepara y lo marca listo; el mesero lo entrega.
5. El cajero abre caja y cobra el pedido completo en efectivo. La mesa queda libre cuando el pedido está entregado y pagado.

No hay clientes, reservas, reparto, recetas, inventario, compras, promociones, abonos, gastos ni reportes. Un pedido conserva sus productos y precios para poder revisar después lo cobrado. El comprobante es académico y **no es una factura fiscal**. El modelo POO vigente está en `Garage_Coffee_UML_Academico.drawio`.

## Preparar MySQL en cada computadora

1. Instala e inicia MySQL. En DBeaver ejecuta [garage_coffee.sql](garage_coffee.sql) para crear las nueve tablas del flujo y los datos de ejemplo. El script no elimina tablas ni datos existentes.
2. Copia `garage-db.properties.example` a `garage-db.properties` en la raíz y escribe el usuario y la contraseña MySQL **de esa computadora**. El archivo local está excluido de Git.
3. Abre **esta carpeta raíz** como proyecto Maven en NetBeans con JDK 25 o superior. Ejecuta `com.garagecoffee.Proyecto2Progra2` o **Run Project**.

DBeaver es solo el cliente de administración: debe apuntar al mismo MySQL que Swing. En esta Mac también existe un MySQL instalado fuera de Docker; por eso el archivo local `garage-db.properties` apunta a `jdbc:mysql://[::1]:3306/garage_coffee`, que llega al contenedor. `localhost:3306` llega al MySQL de macOS. En otras computadoras usa la dirección y contraseña que correspondan a su servidor; no copies el archivo local con credenciales.

Usuarios de ejemplo: `admin.demo`, `mesero.demo`, `cocina.demo`, `cajero.demo`. Contraseña de todos: `demo`. Cámbialas antes de usar datos reales. Si ya existe una base `garage_coffee` de la versión anterior, haz una copia de seguridad antes de migrarla: `CREATE TABLE IF NOT EXISTS` no convierte automáticamente sus tablas antiguas al nuevo esquema.

Si MySQL no está disponible, Swing puede abrirse en **demostración temporal**: acepta la opción al inicio o usa `--demo`. En ese modo se entra con `admin` / `demo` y se pierden los cambios al cerrar.

## Compilar y probar

Desde la raíz del repositorio:

```sh
./backend/mvnw -f pom.xml test
./backend/mvnw -f pom.xml process-classes exec:exec
```

En Windows cambia `./backend/mvnw` por `backend\mvnw.cmd`. El proyecto de Android se abre **desde `android/`** en Android Studio, no desde la raíz. Spring se abre desde `backend/`; consulta [backend/README.md](backend/README.md). Los archivos `.form` de Swing siguen siendo editables con el diseñador de NetBeans.

Con MySQL iniciado y el esquema importado, `./backend/mvnw -f pom.xml test -Dgarage.test.mysql=true` añade una prueba JDBC de lectura y otra de pedido/cobro que se revierte al finalizar. El comando normal de pruebas no necesita MySQL.

Los campos monetarios aceptan coma o punto decimal y formatos habituales de miles. Una cantidad como `10.000` se interpreta como diez con tres decimales; para diez mil escribe `10000` o `10,000.00`.

El archivo `Garage_Coffee_Contexto_Completo.md` conserva la historia y el diseño original, pero está excluido de Git y describe funciones retiradas. **Este README y el UML académico representan el alcance actual.**
