-- Datos de ejemplo para Garage Coffee.
-- Ejecutar después de crear el esquema y ejecutar PrepararBaseDatos.
-- No borra datos existentes. Los usuarios agregados usan la contraseña: demo

USE garage_coffee;
START TRANSACTION;

SET @clave_demo = 'pbkdf2$600000$EzcgbVXQQD5yaojGcn8rvg==$Sg1fYVB2ZWiEvgQIlRMw6ysYBoH8puA+i/0AfBeGGbI=';

-- 1. Empleados para probar todos los roles.
INSERT IGNORE INTO empleado
    (nombre, apellido, telefono, email, nombre_usuario, password, rol, activo)
VALUES
    ('Ana', 'Administradora', '5555-0100', 'admin.demo@garage.local', 'admin.demo', @clave_demo, 'ADMINISTRADOR', 1),
    ('Mario', 'Mesero',        '5555-0101', 'mesero.demo@garage.local', 'mesero.demo', @clave_demo, 'MESERO', 1),
    ('Carla', 'Cocina',        '5555-0102', 'cocina.demo@garage.local', 'cocina.demo', @clave_demo, 'COCINA', 1),
    ('Carlos', 'Caja',         '5555-0103', 'cajero.demo@garage.local', 'cajero.demo', @clave_demo, 'CAJERO', 1),
    ('Raúl', 'Repartidor',     '5555-0104', 'repartidor.demo@garage.local', 'repartidor.demo', @clave_demo, 'REPARTIDOR', 1);

-- 2. Categorías.
INSERT INTO categoria (nombre, tipo, activo)
SELECT 'Bebidas', 'BEBIDA', 1
WHERE NOT EXISTS (SELECT 1 FROM categoria WHERE nombre = 'Bebidas');
INSERT INTO categoria (nombre, tipo, activo)
SELECT 'Comidas', 'COMIDA', 1
WHERE NOT EXISTS (SELECT 1 FROM categoria WHERE nombre = 'Comidas');
INSERT INTO categoria (nombre, tipo, activo)
SELECT 'Extras', 'EXTRA', 1
WHERE NOT EXISTS (SELECT 1 FROM categoria WHERE nombre = 'Extras');

SET @cat_bebidas = (SELECT id FROM categoria WHERE nombre = 'Bebidas' ORDER BY id LIMIT 1);
SET @cat_comidas = (SELECT id FROM categoria WHERE nombre = 'Comidas' ORDER BY id LIMIT 1);
SET @cat_extras = (SELECT id FROM categoria WHERE nombre = 'Extras' ORDER BY id LIMIT 1);

-- 3. Productos.
INSERT INTO producto (categoria_id, nombre, descripcion, imagen_url, precio, disponible)
SELECT @cat_bebidas, 'Café americano', 'Café de la casa', '', 15.00, 1
WHERE NOT EXISTS (SELECT 1 FROM producto WHERE nombre = 'Café americano');
INSERT INTO producto (categoria_id, nombre, descripcion, imagen_url, precio, disponible)
SELECT @cat_bebidas, 'Capuchino', 'Café con leche espumada', '', 22.00, 1
WHERE NOT EXISTS (SELECT 1 FROM producto WHERE nombre = 'Capuchino');
INSERT INTO producto (categoria_id, nombre, descripcion, imagen_url, precio, disponible)
SELECT @cat_comidas, 'Sándwich de jamón y queso', 'Preparado al momento', '', 35.00, 1
WHERE NOT EXISTS (SELECT 1 FROM producto WHERE nombre = 'Sándwich de jamón y queso');
INSERT INTO producto (categoria_id, nombre, descripcion, imagen_url, precio, disponible)
SELECT @cat_comidas, 'Pastel de chocolate', 'Porción individual', '', 25.00, 1
WHERE NOT EXISTS (SELECT 1 FROM producto WHERE nombre = 'Pastel de chocolate');
INSERT INTO producto (categoria_id, nombre, descripcion, imagen_url, precio, disponible)
SELECT @cat_extras, 'Leche extra', 'Porción adicional de leche', '', 5.00, 1
WHERE NOT EXISTS (SELECT 1 FROM producto WHERE nombre = 'Leche extra');

SET @prod_cafe = (SELECT id FROM producto WHERE nombre = 'Café americano' ORDER BY id LIMIT 1);
SET @prod_capuchino = (SELECT id FROM producto WHERE nombre = 'Capuchino' ORDER BY id LIMIT 1);
SET @prod_sandwich = (SELECT id FROM producto WHERE nombre = 'Sándwich de jamón y queso' ORDER BY id LIMIT 1);
SET @prod_pastel = (SELECT id FROM producto WHERE nombre = 'Pastel de chocolate' ORDER BY id LIMIT 1);
SET @prod_leche_extra = (SELECT id FROM producto WHERE nombre = 'Leche extra' ORDER BY id LIMIT 1);

-- 4. Ingredientes y existencias actuales.
INSERT INTO ingrediente (nombre, unidad_medida, stock_actual, stock_minimo, costo_unitario)
SELECT 'Café en grano', 'g', 4964.000, 500.000, 0.1000
WHERE NOT EXISTS (SELECT 1 FROM ingrediente WHERE nombre = 'Café en grano');
INSERT INTO ingrediente (nombre, unidad_medida, stock_actual, stock_minimo, costo_unitario)
SELECT 'Leche', 'ml', 9990.000, 1000.000, 0.0150
WHERE NOT EXISTS (SELECT 1 FROM ingrediente WHERE nombre = 'Leche');
INSERT INTO ingrediente (nombre, unidad_medida, stock_actual, stock_minimo, costo_unitario)
SELECT 'Pan', 'unidad', 48.000, 10.000, 1.0000
WHERE NOT EXISTS (SELECT 1 FROM ingrediente WHERE nombre = 'Pan');
INSERT INTO ingrediente (nombre, unidad_medida, stock_actual, stock_minimo, costo_unitario)
SELECT 'Jamón', 'g', 4960.000, 500.000, 0.1200
WHERE NOT EXISTS (SELECT 1 FROM ingrediente WHERE nombre = 'Jamón');
INSERT INTO ingrediente (nombre, unidad_medida, stock_actual, stock_minimo, costo_unitario)
SELECT 'Queso', 'g', 2970.000, 400.000, 0.1000
WHERE NOT EXISTS (SELECT 1 FROM ingrediente WHERE nombre = 'Queso');
INSERT INTO ingrediente (nombre, unidad_medida, stock_actual, stock_minimo, costo_unitario)
SELECT 'Porción de pastel', 'unidad', 20.000, 4.000, 10.0000
WHERE NOT EXISTS (SELECT 1 FROM ingrediente WHERE nombre = 'Porción de pastel');
INSERT INTO ingrediente (nombre, unidad_medida, stock_actual, stock_minimo, costo_unitario)
SELECT 'Azúcar', 'g', 3000.000, 500.000, 0.0200
WHERE NOT EXISTS (SELECT 1 FROM ingrediente WHERE nombre = 'Azúcar');

SET @ing_cafe = (SELECT id FROM ingrediente WHERE nombre = 'Café en grano' ORDER BY id LIMIT 1);
SET @ing_leche = (SELECT id FROM ingrediente WHERE nombre = 'Leche' ORDER BY id LIMIT 1);
SET @ing_pan = (SELECT id FROM ingrediente WHERE nombre = 'Pan' ORDER BY id LIMIT 1);
SET @ing_jamon = (SELECT id FROM ingrediente WHERE nombre = 'Jamón' ORDER BY id LIMIT 1);
SET @ing_queso = (SELECT id FROM ingrediente WHERE nombre = 'Queso' ORDER BY id LIMIT 1);
SET @ing_pastel = (SELECT id FROM ingrediente WHERE nombre = 'Porción de pastel' ORDER BY id LIMIT 1);

-- 5. Recetas. Son necesarias para enviar pedidos a cocina.
INSERT INTO detalle_receta (producto_id, ingrediente_id, cantidad_requerida)
SELECT @prod_cafe, @ing_cafe, 18.000
WHERE NOT EXISTS (SELECT 1 FROM detalle_receta WHERE producto_id = @prod_cafe AND ingrediente_id = @ing_cafe);
INSERT INTO detalle_receta (producto_id, ingrediente_id, cantidad_requerida)
SELECT @prod_capuchino, @ing_cafe, 18.000
WHERE NOT EXISTS (SELECT 1 FROM detalle_receta WHERE producto_id = @prod_capuchino AND ingrediente_id = @ing_cafe);
INSERT INTO detalle_receta (producto_id, ingrediente_id, cantidad_requerida)
SELECT @prod_capuchino, @ing_leche, 200.000
WHERE NOT EXISTS (SELECT 1 FROM detalle_receta WHERE producto_id = @prod_capuchino AND ingrediente_id = @ing_leche);
INSERT INTO detalle_receta (producto_id, ingrediente_id, cantidad_requerida)
SELECT @prod_sandwich, @ing_pan, 2.000
WHERE NOT EXISTS (SELECT 1 FROM detalle_receta WHERE producto_id = @prod_sandwich AND ingrediente_id = @ing_pan);
INSERT INTO detalle_receta (producto_id, ingrediente_id, cantidad_requerida)
SELECT @prod_sandwich, @ing_jamon, 40.000
WHERE NOT EXISTS (SELECT 1 FROM detalle_receta WHERE producto_id = @prod_sandwich AND ingrediente_id = @ing_jamon);
INSERT INTO detalle_receta (producto_id, ingrediente_id, cantidad_requerida)
SELECT @prod_sandwich, @ing_queso, 30.000
WHERE NOT EXISTS (SELECT 1 FROM detalle_receta WHERE producto_id = @prod_sandwich AND ingrediente_id = @ing_queso);
INSERT INTO detalle_receta (producto_id, ingrediente_id, cantidad_requerida)
SELECT @prod_pastel, @ing_pastel, 1.000
WHERE NOT EXISTS (SELECT 1 FROM detalle_receta WHERE producto_id = @prod_pastel AND ingrediente_id = @ing_pastel);
INSERT INTO detalle_receta (producto_id, ingrediente_id, cantidad_requerida)
SELECT @prod_leche_extra, @ing_leche, 100.000
WHERE NOT EXISTS (SELECT 1 FROM detalle_receta WHERE producto_id = @prod_leche_extra AND ingrediente_id = @ing_leche);

-- 6. Menú promocional y plato del día.
INSERT INTO menu (nombre, activo, fecha_inicio, fecha_fin)
SELECT 'Promoción de la casa', 1, DATE_SUB(CURDATE(), INTERVAL 30 DAY), DATE_ADD(CURDATE(), INTERVAL 30 DAY)
WHERE NOT EXISTS (SELECT 1 FROM menu WHERE nombre = 'Promoción de la casa');
SET @menu_promo = (SELECT id FROM menu WHERE nombre = 'Promoción de la casa' ORDER BY id LIMIT 1);
UPDATE menu SET activo = 1, fecha_inicio = DATE_SUB(CURDATE(), INTERVAL 30 DAY),
    fecha_fin = DATE_ADD(CURDATE(), INTERVAL 30 DAY) WHERE id = @menu_promo;
INSERT INTO detalle_menu (menu_id, producto_id, precio_especial)
SELECT @menu_promo, @prod_capuchino, 19.00
WHERE NOT EXISTS (SELECT 1 FROM detalle_menu WHERE menu_id = @menu_promo AND producto_id = @prod_capuchino);
INSERT INTO plato_del_dia (producto_id, fecha, nota_especial, precio_especial)
SELECT @prod_sandwich, CURDATE(), 'Especial del día', 30.00
WHERE NOT EXISTS (SELECT 1 FROM plato_del_dia WHERE producto_id = @prod_sandwich AND fecha = CURDATE());

-- 7. Clientes y direcciones.
INSERT INTO cliente (nombre, telefono, email, fecha_registro)
SELECT 'María López', '5555-1001', 'maria.lopez@ejemplo.local', DATE_SUB(NOW(), INTERVAL 15 DAY)
WHERE NOT EXISTS (SELECT 1 FROM cliente WHERE telefono = '5555-1001');
INSERT INTO cliente (nombre, telefono, email, fecha_registro)
SELECT 'José García', '5555-1002', 'jose.garcia@ejemplo.local', DATE_SUB(NOW(), INTERVAL 10 DAY)
WHERE NOT EXISTS (SELECT 1 FROM cliente WHERE telefono = '5555-1002');
SET @cliente_maria = (SELECT id FROM cliente WHERE telefono = '5555-1001' ORDER BY id LIMIT 1);
SET @cliente_jose = (SELECT id FROM cliente WHERE telefono = '5555-1002' ORDER BY id LIMIT 1);
INSERT INTO direccion_cliente (cliente_id, direccion, referencia)
SELECT @cliente_maria, 'Zona 1, 1a avenida 10-20', 'Casa con portón café'
WHERE NOT EXISTS (SELECT 1 FROM direccion_cliente WHERE cliente_id = @cliente_maria AND direccion = 'Zona 1, 1a avenida 10-20');
INSERT INTO direccion_cliente (cliente_id, direccion, referencia)
SELECT @cliente_jose, 'Zona 10, 10a calle 5-40', 'Recepción del edificio'
WHERE NOT EXISTS (SELECT 1 FROM direccion_cliente WHERE cliente_id = @cliente_jose AND direccion = 'Zona 10, 10a calle 5-40');

-- 8. Mesas y una reserva futura.
INSERT IGNORE INTO mesa (numero, capacidad, estado) VALUES
    (1, 4, 'LIBRE'), (2, 4, 'LIBRE'), (3, 6, 'LIBRE'),
    (4, 2, 'LIBRE'), (5, 4, 'LIBRE'), (6, 8, 'LIBRE');
SET @mesa_1 = (SELECT id FROM mesa WHERE numero = 1 LIMIT 1);
SET @mesa_2 = (SELECT id FROM mesa WHERE numero = 2 LIMIT 1);
SET @mesa_3 = (SELECT id FROM mesa WHERE numero = 3 LIMIT 1);
INSERT INTO reserva (cliente_id, mesa_id, fecha_hora, numero_personas, estado)
SELECT @cliente_jose, @mesa_3, DATE_ADD(CURDATE(), INTERVAL 1 DAY) + INTERVAL 18 HOUR, 4, 'CONFIRMADA'
WHERE NOT EXISTS (
    SELECT 1 FROM reserva
    WHERE cliente_id = @cliente_jose AND mesa_id = @mesa_3
      AND estado IN ('PENDIENTE', 'CONFIRMADA') AND fecha_hora > NOW()
);

-- 9. Repartidor operativo.
INSERT INTO repartidor (nombre, telefono, vehiculo, activo)
SELECT 'Pedro Repartidor', '5555-3001', 'Moto roja', 1
WHERE NOT EXISTS (SELECT 1 FROM repartidor WHERE telefono = '5555-3001');
SET @repartidor = (SELECT id FROM repartidor WHERE telefono = '5555-3001' ORDER BY id LIMIT 1);

-- 10. Proveedor, una compra recibida y una pendiente.
INSERT INTO proveedor (nombre, telefono, email, direccion, activo)
SELECT 'Distribuidora Demo Garage', '5555-4001', 'ventas@distribuidora.local', 'Zona 12', 1
WHERE NOT EXISTS (SELECT 1 FROM proveedor WHERE nombre = 'Distribuidora Demo Garage');
SET @proveedor = (SELECT id FROM proveedor WHERE nombre = 'Distribuidora Demo Garage' ORDER BY id LIMIT 1);

INSERT INTO compra (proveedor_id, fecha, total, estado)
SELECT @proveedor, DATE_SUB(NOW(), INTERVAL 1 DAY), 200.00, 'RECIBIDA'
WHERE NOT EXISTS (SELECT 1 FROM compra WHERE proveedor_id = @proveedor AND total = 200.00 AND estado = 'RECIBIDA');
SET @compra_recibida = (SELECT id FROM compra WHERE proveedor_id = @proveedor AND total = 200.00 AND estado = 'RECIBIDA' ORDER BY id LIMIT 1);
INSERT INTO detalle_compra (compra_id, ingrediente_id, cantidad, costo_unitario, subtotal)
SELECT @compra_recibida, @ing_cafe, 2000.000, 0.1000, 200.00
WHERE NOT EXISTS (SELECT 1 FROM detalle_compra WHERE compra_id = @compra_recibida AND ingrediente_id = @ing_cafe);

INSERT INTO compra (proveedor_id, fecha, total, estado)
SELECT @proveedor, NOW(), 12.00, 'PENDIENTE'
WHERE NOT EXISTS (SELECT 1 FROM compra WHERE proveedor_id = @proveedor AND total = 12.00 AND estado = 'PENDIENTE');
SET @compra_pendiente = (SELECT id FROM compra WHERE proveedor_id = @proveedor AND total = 12.00 AND estado = 'PENDIENTE' ORDER BY id LIMIT 1);
INSERT INTO detalle_compra (compra_id, ingrediente_id, cantidad, costo_unitario, subtotal)
SELECT @compra_pendiente, @ing_leche, 1000.000, 0.0120, 12.00
WHERE NOT EXISTS (SELECT 1 FROM detalle_compra WHERE compra_id = @compra_pendiente AND ingrediente_id = @ing_leche);

-- 11. Pedidos de ejemplo: entregado, pendiente, domicilio listo y para recoger.
INSERT INTO pedido
    (cliente_id, mesa_id, repartidor_id, fecha_creacion, fecha_programada, estado,
     tipo_entrega, total, observaciones, orden_llegada, codigo_retiro,
     costo_envio, direccion_entrega, costo_preparacion)
SELECT NULL, @mesa_1, NULL, DATE_SUB(NOW(), INTERVAL 2 HOUR), NULL, 'ENTREGADO',
       'CONSUMIR_LOCAL', 30.00, '[SEMILLA] Pedido local entregado', 1, NULL,
       NULL, NULL, 3.60
WHERE NOT EXISTS (SELECT 1 FROM pedido WHERE observaciones = '[SEMILLA] Pedido local entregado');
SET @pedido_pagado = (SELECT id FROM pedido WHERE observaciones = '[SEMILLA] Pedido local entregado' ORDER BY id LIMIT 1);
INSERT INTO detalle_pedido (pedido_id, producto_id, cantidad, precio_unitario, subtotal)
SELECT @pedido_pagado, @prod_cafe, 2, 15.00, 30.00
WHERE NOT EXISTS (SELECT 1 FROM detalle_pedido WHERE pedido_id = @pedido_pagado AND producto_id = @prod_cafe);

INSERT INTO pedido
    (cliente_id, mesa_id, repartidor_id, fecha_creacion, fecha_programada, estado,
     tipo_entrega, total, observaciones, orden_llegada, codigo_retiro,
     costo_envio, direccion_entrega, costo_preparacion)
SELECT @cliente_maria, @mesa_2, NULL, NOW(), NULL, 'PENDIENTE',
       'CONSUMIR_LOCAL', 38.00, '[SEMILLA] Pedido local pendiente', 2, NULL,
       NULL, NULL, NULL
WHERE NOT EXISTS (SELECT 1 FROM pedido WHERE observaciones = '[SEMILLA] Pedido local pendiente');
SET @pedido_pendiente = (SELECT id FROM pedido WHERE observaciones = '[SEMILLA] Pedido local pendiente' ORDER BY id LIMIT 1);
INSERT INTO detalle_pedido (pedido_id, producto_id, cantidad, precio_unitario, subtotal)
SELECT @pedido_pendiente, @prod_capuchino, 2, 19.00, 38.00
WHERE NOT EXISTS (SELECT 1 FROM detalle_pedido WHERE pedido_id = @pedido_pendiente AND producto_id = @prod_capuchino);

INSERT INTO pedido
    (cliente_id, mesa_id, repartidor_id, fecha_creacion, fecha_programada, estado,
     tipo_entrega, total, observaciones, orden_llegada, codigo_retiro,
     costo_envio, direccion_entrega, costo_preparacion)
SELECT @cliente_jose, NULL, @repartidor, DATE_SUB(NOW(), INTERVAL 30 MINUTE), NULL, 'LISTO',
       'DOMICILIO', 35.00, '[SEMILLA] Domicilio listo', NULL, NULL,
       5.00, 'Zona 10, 10a calle 5-40', 9.80
WHERE NOT EXISTS (SELECT 1 FROM pedido WHERE observaciones = '[SEMILLA] Domicilio listo');
SET @pedido_domicilio = (SELECT id FROM pedido WHERE observaciones = '[SEMILLA] Domicilio listo' ORDER BY id LIMIT 1);
INSERT INTO detalle_pedido (pedido_id, producto_id, cantidad, precio_unitario, subtotal)
SELECT @pedido_domicilio, @prod_sandwich, 1, 30.00, 30.00
WHERE NOT EXISTS (SELECT 1 FROM detalle_pedido WHERE pedido_id = @pedido_domicilio AND producto_id = @prod_sandwich);

INSERT INTO pedido
    (cliente_id, mesa_id, repartidor_id, fecha_creacion, fecha_programada, estado,
     tipo_entrega, total, observaciones, orden_llegada, codigo_retiro,
     costo_envio, direccion_entrega, costo_preparacion)
SELECT @cliente_maria, NULL, NULL, NOW(), DATE_ADD(NOW(), INTERVAL 1 HOUR), 'PENDIENTE',
       'RECOGER', 25.00, '[SEMILLA] Pedido para recoger', NULL, 'R-DEMO',
       NULL, NULL, NULL
WHERE NOT EXISTS (SELECT 1 FROM pedido WHERE observaciones = '[SEMILLA] Pedido para recoger');
SET @pedido_recoger = (SELECT id FROM pedido WHERE observaciones = '[SEMILLA] Pedido para recoger' ORDER BY id LIMIT 1);
INSERT INTO detalle_pedido (pedido_id, producto_id, cantidad, precio_unitario, subtotal)
SELECT @pedido_recoger, @prod_pastel, 1, 25.00, 25.00
WHERE NOT EXISTS (SELECT 1 FROM detalle_pedido WHERE pedido_id = @pedido_recoger AND producto_id = @prod_pastel);

UPDATE mesa SET estado = 'LIBRE' WHERE id = @mesa_1;
UPDATE mesa SET estado = 'OCUPADA' WHERE id = @mesa_2;

-- 12. Caja histórica, movimientos, pago, comprobante y gasto.
SET @caja_demo = (
    SELECT mc.caja_id FROM movimiento_caja mc
    WHERE mc.concepto = '[SEMILLA] Fondo adicional' ORDER BY mc.id LIMIT 1
);
INSERT INTO caja (fecha_apertura, fecha_cierre, monto_inicial, monto_final, estado)
SELECT DATE_SUB(NOW(), INTERVAL 3 HOUR), NOW(), 200.00, 240.00, 'CERRADA'
WHERE @caja_demo IS NULL;
SET @caja_demo = COALESCE(@caja_demo, LAST_INSERT_ID());

INSERT INTO pago (pedido_id, metodo, monto, cambio, fecha_hora)
SELECT @pedido_pagado, 'EFECTIVO', 30.00, 20.00, DATE_SUB(NOW(), INTERVAL 1 HOUR)
WHERE NOT EXISTS (SELECT 1 FROM pago WHERE pedido_id = @pedido_pagado);
INSERT INTO factura (pedido_id, numero, fecha, subtotal, impuesto, total)
SELECT @pedido_pagado, 'GC-DEMO-001', DATE_SUB(NOW(), INTERVAL 1 HOUR), 30.00, 0.00, 30.00
WHERE NOT EXISTS (SELECT 1 FROM factura WHERE pedido_id = @pedido_pagado);

INSERT INTO movimiento_caja (caja_id, tipo, concepto, monto, fecha_hora)
SELECT @caja_demo, 'INGRESO', CONCAT('[SEMILLA] Cobro pedido #', @pedido_pagado), 30.00, DATE_SUB(NOW(), INTERVAL 1 HOUR)
WHERE NOT EXISTS (SELECT 1 FROM movimiento_caja WHERE caja_id = @caja_demo AND concepto = CONCAT('[SEMILLA] Cobro pedido #', @pedido_pagado));
INSERT INTO gasto (categoria, descripcion, monto, fecha_hora)
SELECT 'Limpieza', '[SEMILLA] Insumos de limpieza', 15.00, DATE_SUB(NOW(), INTERVAL 45 MINUTE)
WHERE NOT EXISTS (SELECT 1 FROM gasto WHERE descripcion = '[SEMILLA] Insumos de limpieza');
INSERT INTO movimiento_caja (caja_id, tipo, concepto, monto, fecha_hora)
SELECT @caja_demo, 'EGRESO', '[SEMILLA] Gasto de limpieza', 15.00, DATE_SUB(NOW(), INTERVAL 45 MINUTE)
WHERE NOT EXISTS (SELECT 1 FROM movimiento_caja WHERE caja_id = @caja_demo AND concepto = '[SEMILLA] Gasto de limpieza');
INSERT INTO movimiento_caja (caja_id, tipo, concepto, monto, fecha_hora)
SELECT @caja_demo, 'INGRESO', '[SEMILLA] Fondo adicional', 25.00, DATE_SUB(NOW(), INTERVAL 30 MINUTE)
WHERE NOT EXISTS (SELECT 1 FROM movimiento_caja WHERE caja_id = @caja_demo AND concepto = '[SEMILLA] Fondo adicional');

-- 13. Historial de inventario y pérdida.
INSERT INTO movimiento_stock (ingrediente_id, tipo, cantidad, costo_total, fecha_hora, motivo)
SELECT @ing_cafe, 'ENTRADA', 2000.000, 200.00, DATE_SUB(NOW(), INTERVAL 1 DAY), '[SEMILLA] Compra recibida'
WHERE NOT EXISTS (SELECT 1 FROM movimiento_stock WHERE ingrediente_id = @ing_cafe AND motivo = '[SEMILLA] Compra recibida');
INSERT INTO movimiento_stock (ingrediente_id, tipo, cantidad, costo_total, fecha_hora, motivo)
SELECT @ing_cafe, 'SALIDA', 36.000, 3.60, DATE_SUB(NOW(), INTERVAL 2 HOUR), '[SEMILLA] Preparación pedido local'
WHERE NOT EXISTS (SELECT 1 FROM movimiento_stock WHERE ingrediente_id = @ing_cafe AND motivo = '[SEMILLA] Preparación pedido local');
INSERT INTO movimiento_stock (ingrediente_id, tipo, cantidad, costo_total, fecha_hora, motivo)
SELECT @ing_pan, 'SALIDA', 2.000, 2.00, DATE_SUB(NOW(), INTERVAL 30 MINUTE), '[SEMILLA] Preparación domicilio'
WHERE NOT EXISTS (SELECT 1 FROM movimiento_stock WHERE ingrediente_id = @ing_pan AND motivo = '[SEMILLA] Preparación domicilio');
INSERT INTO movimiento_stock (ingrediente_id, tipo, cantidad, costo_total, fecha_hora, motivo)
SELECT @ing_jamon, 'SALIDA', 40.000, 4.80, DATE_SUB(NOW(), INTERVAL 30 MINUTE), '[SEMILLA] Preparación domicilio'
WHERE NOT EXISTS (SELECT 1 FROM movimiento_stock WHERE ingrediente_id = @ing_jamon AND motivo = '[SEMILLA] Preparación domicilio');
INSERT INTO movimiento_stock (ingrediente_id, tipo, cantidad, costo_total, fecha_hora, motivo)
SELECT @ing_queso, 'SALIDA', 30.000, 3.00, DATE_SUB(NOW(), INTERVAL 30 MINUTE), '[SEMILLA] Preparación domicilio'
WHERE NOT EXISTS (SELECT 1 FROM movimiento_stock WHERE ingrediente_id = @ing_queso AND motivo = '[SEMILLA] Preparación domicilio');
INSERT INTO movimiento_stock (ingrediente_id, tipo, cantidad, costo_total, fecha_hora, motivo)
SELECT @ing_leche, 'MERMA', 10.000, 0.15, DATE_SUB(NOW(), INTERVAL 20 MINUTE), '[SEMILLA] Derrame de leche'
WHERE NOT EXISTS (SELECT 1 FROM movimiento_stock WHERE ingrediente_id = @ing_leche AND motivo = '[SEMILLA] Derrame de leche');
INSERT INTO perdida (ingrediente_id, cantidad, motivo, fecha_hora)
SELECT @ing_leche, 10.000, '[SEMILLA] Derrame de leche', DATE_SUB(NOW(), INTERVAL 20 MINUTE)
WHERE NOT EXISTS (SELECT 1 FROM perdida WHERE ingrediente_id = @ing_leche AND motivo = '[SEMILLA] Derrame de leche');

-- Avisa a las sesiones Swing abiertas que la información cambió.
INSERT INTO control_cambios (id, revision) VALUES (1, 1)
ON DUPLICATE KEY UPDATE revision = revision + 1;

COMMIT;
