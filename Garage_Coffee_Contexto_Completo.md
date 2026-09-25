# Garage Coffee — Especificación Técnica Base

## 1. Objetivo del proyecto

Garage Coffee será un sistema integral de gestión para restaurante/cafetería.

La prioridad actual del proyecto es desarrollar la aplicación de escritorio en **Java Swing** utilizando **POO**, **JDBC** y **MySQL**.

Arquitectura inicial:

```text
JAVA SWING
    ↓
Controller
    ↓
Service
    ↓
DAO
    ↓
JDBC
    ↓
MySQL
    ↓
Docker
```

Arquitectura futura:

```text
                  Swing
                    │
                    ▼
Android/Kotlin → Spring Boot ← Web
                    │
                    ▼
                  MySQL
```

Spring Boot será posteriormente el backend central. Por ahora, Swing puede trabajar directamente con MySQL mediante JDBC.

---

## 2. Estructura base del proyecto

```text
garage-coffee-swing/
│
├── pom.xml
│
└── src/main/java/com/garagecoffee/
    │
    ├── model/
    ├── dao/
    ├── service/
    ├── controller/
    ├── view/
    ├── database/
    │   └── ConexionDB.java
    │
    └── Main.java
```

### Responsabilidad de cada paquete

```text
model       → clases y objetos del dominio
view        → JFrame, JPanel, JTable y demás componentes Swing
controller  → comunicación entre View y Service
service     → reglas de negocio
dao         → SQL y acceso a datos
database    → conexión JDBC
```

Regla principal:

```text
View
 ↓
Controller
 ↓
Service
 ↓
DAO
 ↓
MySQL
```

Nunca debe existir SQL directamente dentro de una vista Swing.

---

## 3. Módulos funcionales

- Usuarios y empleados
- Clientes
- Menú
- Mesas
- Reservas
- Pedidos
- Cocina
- Delivery
- Inventario
- Compras
- Proveedores
- Pérdidas
- Caja
- Cobros
- Facturación
- Gastos
- Reportes

---

# 4. Modelo POO

## 4.1 Empleado

```text
Empleado
-------------------------
id : int
nombre : String
apellido : String
telefono : String
email : String
nombreUsuario : String
password : String
rol : RolEmpleado
activo : boolean
-------------------------
iniciarSesion()
atenderPedido()
```

### RolEmpleado

```text
ADMINISTRADOR
MESERO
COCINA
CAJERO
REPARTIDOR
```

---

## 4.2 Cliente

```text
Cliente
-------------------------
id : int
nombre : String
telefono : String
email : String
-------------------------
registrarse()
actualizarDatos()
```

### DireccionCliente

```text
DireccionCliente
-------------------------
id : int
direccion : String
referencia : String
```

Relación:

```text
Cliente 1 ───── 0..* DireccionCliente
```

---

# 5. Mesas y reservas

## Mesa

```text
Mesa
-------------------------
id : int
numero : int
capacidad : int
estado : EstadoMesa
-------------------------
ocupar()
liberar()
cambiarEstado()
```

### EstadoMesa

```text
LIBRE
OCUPADA
RESERVADA
FUERA_SERVICIO
```

## Reserva

```text
Reserva
-------------------------
id : int
fechaHora : LocalDateTime
numeroPersonas : int
estado : EstadoReserva
-------------------------
confirmar()
cancelar()
```

### EstadoReserva

```text
PENDIENTE
CONFIRMADA
CANCELADA
FINALIZADA
```

Relaciones:

```text
Cliente 1 ─── 0..* Reserva
Mesa    1 ─── 0..* Reserva
```

---

# 6. Pedidos

## Pedido

```text
<<abstract>>
Pedido
-------------------------
id : int
fechaCreacion : LocalDateTime
fechaProgramada : LocalDateTime
estado : EstadoPedido
tipoEntrega : TipoEntrega
total : BigDecimal
observaciones : String
-------------------------
calcularTotal()
cambiarEstado()
cancelar()
```

Herencia:

```text
                Pedido
             <<abstract>>
             /    |     \
            /     |      \
 PedidoLocal  PedidoRecoger  PedidoDomicilio
```

## PedidoLocal

```text
ordenLlegada : int

asignarMesa()
```

## PedidoRecoger

```text
codigoRetiro : String
horaProgramada : LocalDateTime

confirmarRetiro()
```

## PedidoDomicilio

```text
costoEnvio : BigDecimal

asignarRepartidor()
```

### EstadoPedido

```text
PENDIENTE
EN_PREPARACION
LISTO
ENTREGADO
CANCELADO
```

### TipoEntrega

```text
CONSUMIR_LOCAL
RECOGER
DOMICILIO
```

---

# 7. Detalle del pedido

```text
DetallePedido
-------------------------
cantidad : int
precioUnitario : BigDecimal
subtotal : BigDecimal
-------------------------
calcularSubtotal()
```

Relaciones:

```text
Pedido 1 ◆──── 1..* DetallePedido
DetallePedido * ──── 1 Producto
```

---

# 8. Repartidor

```text
Repartidor
-------------------------
id : int
telefono : String
vehiculo : String
activo : boolean
-------------------------
cambiarEstado()
```

Relación:

```text
PedidoDomicilio 0..* ─── 0..1 Repartidor
```

Solo aplica cuando:

```text
tipoEntrega = DOMICILIO
```

---

# 9. Menú y productos

## Categoria

```text
Categoria
-------------------------
id : int
nombre : String
tipo : TipoCategoria
activo : boolean
```

### TipoCategoria

```text
COMIDA
BEBIDA
EXTRA
```

## Producto

```text
Producto
-------------------------
id : int
nombre : String
descripcion : String
imagenUrl : String
precio : BigDecimal
disponible : boolean
-------------------------
actualizarPrecio()
cambiarDisponibilidad()
```

Relación:

```text
Categoria 1 ───── 0..* Producto
```

---

# 10. Menús especiales

## Menu

```text
Menu
-------------------------
id : int
nombre : String
activo : boolean
fechaInicio : LocalDate
fechaFin : LocalDate
-------------------------
activar()
desactivar()
```

## DetalleMenu

```text
DetalleMenu
-------------------------
id : int
precioEspecial : BigDecimal
```

Relaciones:

```text
Menu 1 ─── 0..* DetalleMenu
DetalleMenu * ─── 1 Producto
```

## PlatoDelDia

```text
PlatoDelDia
-------------------------
id : int
fecha : LocalDate
notaEspecial : String
precioEspecial : BigDecimal
```

---

# 11. Recetas

## DetalleReceta

```text
DetalleReceta
-------------------------
id : int
cantidadRequerida : BigDecimal
```

Relaciones:

```text
Producto 1 ─── 0..* DetalleReceta
DetalleReceta * ─── 1 Ingrediente
```

Ejemplo:

```text
Capuccino
│
├── 18 g Café
├── 200 ml Leche
└── 10 g Azúcar
```

---

# 12. Inventario

## Ingrediente

```text
Ingrediente
-------------------------
id : int
nombre : String
unidadMedida : String
stockActual : BigDecimal
stockMinimo : BigDecimal
costoUnitario : BigDecimal
-------------------------
descontarStock()
aumentarStock()
verificarAlertaStock()
```

## MovimientoStock

```text
MovimientoStock
-------------------------
id : int
tipo : TipoMovimientoStock
cantidad : BigDecimal
costoTotal : BigDecimal
fechaHora : LocalDateTime
motivo : String
```

### TipoMovimientoStock

```text
ENTRADA
SALIDA
AJUSTE
MERMA
```

## Perdida

```text
Perdida
-------------------------
id : int
cantidad : BigDecimal
motivo : String
fechaHora : LocalDateTime
```

Relaciones:

```text
Ingrediente 1 ─── 0..* MovimientoStock
Ingrediente 1 ─── 0..* Perdida
```

---

# 13. Proveedores y compras

## Proveedor

```text
Proveedor
-------------------------
id : int
nombre : String
telefono : String
email : String
direccion : String
activo : boolean
```

## Compra

```text
Compra
-------------------------
id : int
fecha : LocalDateTime
total : BigDecimal
estado : EstadoCompra
-------------------------
calcularTotal()
registrarCompra()
```

### EstadoCompra

```text
PENDIENTE
RECIBIDA
CANCELADA
```

## DetalleCompra

```text
DetalleCompra
-------------------------
cantidad : BigDecimal
costoUnitario : BigDecimal
subtotal : BigDecimal
-------------------------
calcularSubtotal()
```

Relaciones:

```text
Proveedor 1 ─── 0..* Compra
Compra 1 ◆─── 1..* DetalleCompra
DetalleCompra * ─── 1 Ingrediente
```

Flujo:

```text
Compra
 ↓
DetalleCompra
 ↓
MovimientoStock ENTRADA
 ↓
Ingrediente.stockActual aumenta
```

---

# 14. Pagos

## Pago

```text
Pago
-------------------------
id : int
metodo : MetodoPago
monto : BigDecimal
cambio : BigDecimal
fechaHora : LocalDateTime
-------------------------
calcularCambio()
procesarPago()
```

### MetodoPago

```text
EFECTIVO
TARJETA
TRANSFERENCIA
DIGITAL
```

Relación:

```text
Pedido 1 ───── 1..* Pago
```

---

# 15. Factura

```text
Factura
-------------------------
id : int
numero : String
fecha : LocalDateTime
subtotal : BigDecimal
impuesto : BigDecimal
total : BigDecimal
-------------------------
generarTicket()
```

Relación:

```text
Pedido 1 ───── 0..1 Factura
```

---

# 16. Caja

## Caja

```text
Caja
-------------------------
id : int
fechaApertura : LocalDateTime
fechaCierre : LocalDateTime
montoInicial : BigDecimal
montoFinal : BigDecimal
estado : EstadoCaja
-------------------------
abrir()
cerrar()
calcularSaldo()
```

### EstadoCaja

```text
ABIERTA
CERRADA
```

## MovimientoCaja

```text
MovimientoCaja
-------------------------
id : int
tipo : TipoMovimientoCaja
concepto : String
monto : BigDecimal
fechaHora : LocalDateTime
```

### TipoMovimientoCaja

```text
INGRESO
EGRESO
```

Relación:

```text
Caja 1 ◆──── 0..* MovimientoCaja
```

---

# 17. Gastos

```text
Gasto
-------------------------
id : int
categoria : String
descripcion : String
monto : BigDecimal
fechaHora : LocalDateTime
```

Ejemplos:

```text
Electricidad
Gas
Internet
Alquiler
Mantenimiento
Insumos
```

---

# 18. Reportes

## Reporte

```text
Reporte
-------------------------
tipo : TipoReporte
fechaInicio : LocalDate
fechaFin : LocalDate
-------------------------
generar()
exportarPDF()
```

### TipoReporte

```text
VENTAS
PRODUCTOS_MAS_VENDIDOS
INVENTARIO
GASTOS
UTILIDAD
MOVIMIENTOS_CAJA
```

`Reporte` no debe ser una tabla principal.

Debe funcionar como clase de consulta:

```text
Reporte - - -> Pedido
Reporte - - -> Pago
Reporte - - -> Producto
Reporte - - -> MovimientoStock
Reporte - - -> MovimientoCaja
Reporte - - -> Gasto
```

---

# 19. Flujo principal de un pedido

```text
Empleado / Cliente
        ↓
selecciona servicio
        ↓
LOCAL / RECOGER / DOMICILIO
        ↓
selecciona productos
        ↓
Pedido
        ↓
DetallePedido
        ↓
PENDIENTE
        ↓
Cocina
        ↓
EN_PREPARACION
        ↓
LISTO
        ↓
ENTREGADO
        ↓
Pago
        ↓
Factura
        ↓
MovimientoCaja
```

Descuento de inventario:

```text
DetallePedido
      ↓
Producto
      ↓
DetalleReceta
      ↓
Ingrediente
      ↓
MovimientoStock SALIDA
```

---

# 20. Base de datos

Nombre recomendado:

```text
garage_coffee
```

Entorno:

```text
Java Swing
    ↓
JDBC
    ↓
localhost:3306
    ↓
Docker
    ↓
MySQL
```

Tablas principales:

```text
empleado
cliente
direccion_cliente
mesa
reserva

pedido
detalle_pedido
repartidor

categoria
producto
menu
detalle_menu

ingrediente
detalle_receta
movimiento_stock
perdida

proveedor
compra
detalle_compra

pago
factura

caja
movimiento_caja
gasto
```

---

# 21. DAO

Todos los accesos a MySQL deben pasar por DAO.

Ejemplo:

```java
public interface ProductoDAO {

    List<Producto> listar();

    Producto buscarPorId(int id);

    boolean guardar(Producto producto);

    boolean actualizar(Producto producto);

    boolean eliminar(int id);
}
```

Implementación:

```text
ProductoDAOImpl
```

Flujo:

```text
ProductoPanel
       ↓
ProductoController
       ↓
ProductoService
       ↓
ProductoDAO
       ↓
ProductoDAOImpl
       ↓
ConexionDB
       ↓
MySQL
```

---

# 22. DAO prioritarios

Primera fase:

```text
EmpleadoDAO
CategoriaDAO
ProductoDAO
ClienteDAO
MesaDAO
PedidoDAO
IngredienteDAO
```

Segunda fase:

```text
ReservaDAO
ProveedorDAO
CompraDAO
PagoDAO
FacturaDAO
CajaDAO
GastoDAO
```

---

# 23. Pantallas Swing

```text
LoginFrame
        ↓
DashboardFrame
        │
        ├── ProductoPanel
        ├── PedidoPanel
        ├── CocinaPanel
        ├── MesaPanel
        ├── ReservaPanel
        ├── InventarioPanel
        ├── CompraPanel
        ├── CajaPanel
        └── ReportePanel
```

Se recomienda tener un único `DashboardFrame` principal y cambiar `JPanel` internos.

---

# 24. Distribución del equipo

## Persona 1 — Base de datos y persistencia

Responsabilidades:

```text
MySQL
Docker
JDBC
ConexionDB
scripts SQL
DAO
```

## Persona 2 — Pedidos y operación

Responsabilidades:

```text
Cliente
Mesa
Reserva
Pedido
PedidoLocal
PedidoRecoger
PedidoDomicilio
DetallePedido
Repartidor
```

Además:

```text
Service
Controller
View
```

de esos módulos.

## Persona 3 — Menú e inventario

Responsabilidades:

```text
Categoria
Producto
Menu
DetalleMenu
Ingrediente
DetalleReceta
MovimientoStock
Perdida
Proveedor
Compra
DetalleCompra
```

Además:

```text
Service
Controller
View
```

de esos módulos.

## Persona 4 — Administración y finanzas

Responsabilidades:

```text
Empleado
Login
Pago
Factura
Caja
MovimientoCaja
Gasto
Reporte
```

Además:

```text
Service
Controller
View
```

de esos módulos.

---

# 25. Ramas Git recomendadas

```text
main
develop

feature/database-dao
feature/pedidos
feature/menu-inventario
feature/admin-finanzas
```

Reglas:

- No trabajar directamente en `main`.
- Cada integrante trabaja principalmente en su módulo.
- Integrar mediante pull request o merge hacia `develop`.
- Probar antes de unir cambios.

---

# 26. Criterio para considerar un módulo terminado

```text
Modelo POO                 ✓
Tabla MySQL                ✓
DAO                        ✓
Service                    ✓
Controller                 ✓
Vista Swing                ✓
Validaciones               ✓
Prueba CRUD                ✓
Integración con módulos    ✓
```

Ejemplo para Producto:

```text
Producto.java
Categoria.java

tabla producto
tabla categoria

ProductoDAO
CategoriaDAO

ProductoService

ProductoController

ProductoPanel

Crear producto        ✓
Listar productos      ✓
Editar producto       ✓
Eliminar/desactivar   ✓
```

---

# 27. Reglas arquitectónicas

Correcto:

```text
ProductoPanel
      ↓
ProductoController
      ↓
ProductoService
      ↓
ProductoDAO
      ↓
MySQL
```

Incorrecto:

```text
ProductoPanel
      ↓
SELECT * FROM producto
```

Correcto:

```text
PedidoService
      ↓
InventarioService
```

Incorrecto:

```text
JButton
 ↓
UPDATE ingrediente ...
```

Principios:

- Las vistas no contienen SQL.
- Los DAO no contienen lógica visual.
- Los modelos no conocen Swing.
- Los Service contienen reglas del negocio.
- Los Controller coordinan vista y lógica.
- La conexión a datos está centralizada.

---

# 28. Primer hito funcional

Antes de desarrollar todos los módulos, el equipo debería conseguir:

```text
MySQL Docker           ✓
ConexionDB             ✓
Login                  ✓
Producto CRUD          ✓
Mesa                   ✓
Pedido local           ✓
DetallePedido          ✓
Cocina cambia estado   ✓
Pago                   ✓
Factura                ✓
```

Flujo mínimo:

```text
Login
 ↓
Seleccionar mesa
 ↓
Crear pedido
 ↓
Agregar productos
 ↓
Guardar pedido
 ↓
Cocina lo ve
 ↓
Marcar LISTO
 ↓
Cobrar
 ↓
Generar ticket
```

Después agregar:

```text
Inventario automático
Compras
Reservas
Domicilio
Caja
Gastos
Reportes
Android
Spring Boot
```

---

# 29. Criterio general para evaluar avances

Un avance va bien si:

- Respeta la estructura `View → Controller → Service → DAO → MySQL`.
- Las clases coinciden con el modelo POO acordado.
- No existe código SQL dentro de las vistas.
- No se duplica lógica de negocio.
- Las relaciones entre clases coinciden con el UML.
- Las tablas coinciden con el modelo ER.
- Cada módulo puede probarse de forma aislada.
- El código compila y ejecuta sin romper módulos existentes.
- Las ramas Git están separadas por funcionalidad.
- Cada integrante trabaja sobre su responsabilidad asignada.

Si un avance contradice significativamente esta estructura, debe revisarse antes de continuar.
