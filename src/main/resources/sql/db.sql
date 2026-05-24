-- =====================================================
-- SCRIPT DE INICIALIZACIÓN PARA BODEGA PEIRANO
-- Base de datos: BodegaPeiranoDBF
-- Tablas en español con restricciones mejoradas
-- =====================================================
-- Crear base de datos si no existe
IF NOT EXISTS (SELECT * FROM sys.databases WHERE name = 'BodegaPeiranoDBF')
BEGIN
    CREATE DATABASE BodegaPeiranoDBF;
END
GO

USE BodegaPeiranoDBF;
GO

-- =====================================================
-- TABLA: UBIGEO (APOYO)
-- =====================================================
CREATE TABLE ubigeo (
    ubigeo_id CHAR(6) NOT NULL,
    distrito VARCHAR(120) NOT NULL,
    provincia VARCHAR(20) NOT NULL,
    departamento VARCHAR(20) NOT NULL,
    CONSTRAINT ubigeo_pk PRIMARY KEY (ubigeo_id)
);
GO

-- =====================================================
-- TABLA: CATEGORIA (MAESTRA)
-- Restricciones: UNIQUE en nombre y código
-- =====================================================
CREATE TABLE categoria (
    id_categoria INT NOT NULL,
    nombre VARCHAR(50) NOT NULL,
    descripcion VARCHAR(150) NULL,
    estado BIT NOT NULL DEFAULT 1,
    fecha_creacion DATETIME NOT NULL DEFAULT GETDATE(),
    codigo_categoria CHAR(3) NOT NULL,
    -- Nuevas restricciones
    CONSTRAINT categoria_pk PRIMARY KEY (id_categoria),
    CONSTRAINT categoria_nombre_unico UNIQUE (nombre),
    CONSTRAINT categoria_codigo_unico UNIQUE (codigo_categoria),
    CONSTRAINT categoria_codigo_check CHECK (codigo_categoria LIKE '[A-Z][A-Z][A-Z]')
);
GO

-- =====================================================
-- TABLA: PRODUCTO (MAESTRA CON AUDITORÍA)
-- Restricciones: UNIQUE (código_barras), CHECK (precio > 0)
-- =====================================================
CREATE TABLE producto (
    id_producto INT IDENTITY(1,1) NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255) NULL,
    codigo_barras_sku VARCHAR(30) NOT NULL,
    unidad_medida VARCHAR(20) NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL,
    estado BIT NOT NULL DEFAULT 1,
    id_categoria INT NOT NULL,
    -- Campos de auditoría
    fecha_creacion DATETIME NOT NULL DEFAULT GETDATE(),
    fecha_actualizacion DATETIME NULL,
    fecha_eliminacion DATETIME NULL,
    fecha_restauracion DATETIME NULL,
    -- Restricciones
    CONSTRAINT producto_pk PRIMARY KEY (id_producto),
    CONSTRAINT producto_barcode_unico UNIQUE (codigo_barras_sku),
    CONSTRAINT producto_precio_positivo CHECK (precio_unitario > 0),
);
GO

-- =====================================================
-- TABLA: CLIENTE (MAESTRA CON AUDITORÍA)
-- Restricciones: UNIQUE (documento, email), CHECK (mayor de edad)
-- =====================================================
CREATE TABLE cliente (
    id_cliente INT IDENTITY(1,1) NOT NULL,
    tipo_documento CHAR(3) NOT NULL,
    numero_documento VARCHAR(12) NOT NULL,
    nombre VARCHAR(50) NOT NULL,
    apellido VARCHAR(50) NOT NULL,
    email VARCHAR(150) NOT NULL,
    telefono VARCHAR(15) NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    estado BIT NOT NULL DEFAULT 1,
    ubigeo_id CHAR(6) NOT NULL,
    -- Auditoría
    fecha_creacion DATETIME NOT NULL DEFAULT GETDATE(),
    fecha_actualizacion DATETIME NULL,
    fecha_eliminacion DATETIME NULL,
    fecha_restauracion DATETIME NULL,
    -- Restricciones
    CONSTRAINT cliente_pk PRIMARY KEY (id_cliente),
    CONSTRAINT cliente_documento_unico UNIQUE (numero_documento),
    CONSTRAINT cliente_email_unico UNIQUE (email),
    CONSTRAINT cliente_tipo_documento_check CHECK (tipo_documento IN ('DNI', 'CE', 'RUC', 'PAS')),
    CONSTRAINT cliente_mayor_edad CHECK (DATEADD(YEAR, 18, fecha_nacimiento) <= GETDATE()),
    CONSTRAINT cliente_telefono_check CHECK (telefono LIKE '[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]%')
);
GO

-- =====================================================
-- TABLA: ALMACEN (MAESTRA CON AUDITORÍA)
-- Restricciones: UNIQUE (código_almacen), CHECK (cantidad_botellas >= 0)
-- =====================================================
CREATE TABLE almacen (
    id_almacen INT IDENTITY(1,1) NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    ubicacion VARCHAR(150) NULL,
    telefono VARCHAR(20) NULL,
    responsable VARCHAR(100) NULL,
    codigo_almacen VARCHAR(20) NOT NULL,
    tipo_producto VARCHAR(50) NULL,
    cantidad_botellas INT NULL DEFAULT 0,
    estado BIT NOT NULL DEFAULT 1,
    ubigeo_id CHAR(6) NOT NULL,
    -- Auditoría
    fecha_creacion DATETIME2 NOT NULL DEFAULT GETDATE(),
    fecha_actualizacion DATETIME2 NULL,
    fecha_eliminacion DATETIME2 NULL,
    fecha_restauracion DATETIME2 NULL,
    -- Restricciones
    CONSTRAINT almacen_pk PRIMARY KEY (id_almacen),
    CONSTRAINT almacen_codigo_unico UNIQUE (codigo_almacen),
    CONSTRAINT almacen_cantidad_check CHECK (cantidad_botellas >= 0),
);
GO

-- =====================================================
-- TABLA: INVENTARIO (DETALLE)
-- Restricciones: CHECK (stock >= stock_minimo)
-- =====================================================
CREATE TABLE inventario (
    id_inventario INT IDENTITY(1,1) NOT NULL,
    stock_actual DECIMAL(10,2) NOT NULL,
    stock_minimo DECIMAL(10,2) NOT NULL,
    ultima_actualizacion DATETIME NOT NULL DEFAULT GETDATE(),
    id_producto INT NOT NULL,
    id_almacen INT NOT NULL,
    -- Restricciones
    CONSTRAINT inventario_pk PRIMARY KEY (id_inventario),
    CONSTRAINT inventario_stock_check CHECK (stock_actual >= 0 AND stock_minimo >= 0),
    CONSTRAINT inventario_stock_suficiente CHECK (stock_actual >= stock_minimo)
);
GO

-- =====================================================
-- TABLA: METODO_PAGO (APOYO)
-- =====================================================
CREATE TABLE metodo_pago (
    id_metodo_pago INT IDENTITY(1,1) NOT NULL,
    nombre VARCHAR(50) NOT NULL,
    es_electronico BIT NOT NULL DEFAULT 0,
    estado BIT NOT NULL DEFAULT 1,
    CONSTRAINT metodo_pago_pk PRIMARY KEY (id_metodo_pago),
    CONSTRAINT metodo_pago_nombre_unico UNIQUE (nombre)
);
GO

-- =====================================================
-- TABLA: VENTA (TRANSACCIÓN PRINCIPAL)
-- Restricciones: CHECK (total > 0, estado válido)
-- =====================================================
CREATE TABLE venta (
    id_venta INT IDENTITY(1,1) NOT NULL,
    fecha_venta DATETIME2 NOT NULL DEFAULT GETDATE(),
    tipo_documento VARCHAR(20) NOT NULL,
    numero_documento VARCHAR(20) NOT NULL,
    estado CHAR(1) NOT NULL DEFAULT 'C',
    total DECIMAL(10,2) NOT NULL,
    referencia_transaccion VARCHAR(100) NULL,
    id_cliente INT NOT NULL,
    id_almacen INT NOT NULL,
    id_metodo_pago INT NOT NULL,
    -- Restricciones
    CONSTRAINT venta_pk PRIMARY KEY (id_venta),
    CONSTRAINT venta_total_positivo CHECK (total > 0),
    CONSTRAINT venta_estado_check CHECK (estado IN ('C', 'P', 'A')), -- C=Completada, P=Pendiente, A=Anulada
    CONSTRAINT venta_tipo_doc_check CHECK (tipo_documento IN ('Boleta', 'Factura', 'Ticket')),
    CONSTRAINT venta_documento_unico UNIQUE (tipo_documento, numero_documento)
);
GO

-- =====================================================
-- TABLA: DETALLE_VENTA (DETALLE)
-- Restricciones: CHECK (cantidad > 0, subtotal calculado automático)
-- =====================================================
CREATE TABLE detalle_venta (
    id_detalle_venta INT IDENTITY(1,1) NOT NULL,
    cantidad DECIMAL(10,2) NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    descuento DECIMAL(10,2) NULL DEFAULT 0,
    id_venta INT NOT NULL,
    id_producto INT NOT NULL,
    -- Restricciones
    CONSTRAINT detalle_venta_pk PRIMARY KEY (id_detalle_venta),
    CONSTRAINT detalle_cantidad_positiva CHECK (cantidad > 0),
    CONSTRAINT detalle_precio_positivo CHECK (precio_unitario > 0),
    CONSTRAINT detalle_subtotal_check CHECK (subtotal = (cantidad * precio_unitario) - descuento),
    CONSTRAINT detalle_descuento_check CHECK (descuento >= 0 AND descuento <= (cantidad * precio_unitario))
);
GO

-- =====================================================
-- RESTRICCIONES DE LLAVE FORÁNEA
-- =====================================================
ALTER TABLE producto ADD CONSTRAINT producto_categoria_fk
    FOREIGN KEY (id_categoria) REFERENCES categoria (id_categoria);

ALTER TABLE cliente ADD CONSTRAINT cliente_ubigeo_fk
    FOREIGN KEY (ubigeo_id) REFERENCES ubigeo (ubigeo_id);

ALTER TABLE almacen ADD CONSTRAINT almacen_ubigeo_fk
    FOREIGN KEY (ubigeo_id) REFERENCES ubigeo (ubigeo_id);

ALTER TABLE inventario ADD CONSTRAINT inventario_producto_fk
    FOREIGN KEY (id_producto) REFERENCES producto (id_producto);

ALTER TABLE inventario ADD CONSTRAINT inventario_almacen_fk
    FOREIGN KEY (id_almacen) REFERENCES almacen (id_almacen);

ALTER TABLE venta ADD CONSTRAINT venta_cliente_fk
    FOREIGN KEY (id_cliente) REFERENCES cliente (id_cliente);

ALTER TABLE venta ADD CONSTRAINT venta_almacen_fk
    FOREIGN KEY (id_almacen) REFERENCES almacen (id_almacen);

ALTER TABLE venta ADD CONSTRAINT venta_metodo_pago_fk
    FOREIGN KEY (id_metodo_pago) REFERENCES metodo_pago (id_metodo_pago);

ALTER TABLE detalle_venta ADD CONSTRAINT detalle_venta_fk
    FOREIGN KEY (id_venta) REFERENCES venta (id_venta);

ALTER TABLE detalle_venta ADD CONSTRAINT detalle_producto_fk
    FOREIGN KEY (id_producto) REFERENCES producto (id_producto);
GO

-- =====================================================
-- INSERCIÓN DE DATOS (SOLO 15 REGISTROS POR TABLA)
-- =====================================================

-- 1. UBIGEO (ahora con TODOS los códigos necesarios)
INSERT INTO ubigeo (ubigeo_id, distrito, provincia, departamento) VALUES
('150101', 'Lima', 'Lima', 'Lima'),
('150102', 'Asia', 'Cañete', 'Lima'),
('150103', 'Chilca', 'Cañete', 'Lima'),
('150104', 'Mala', 'Cañete', 'Lima'),
('150105', 'San Vicente de Cañete', 'Cañete', 'Lima'),
('150106', 'Imperial', 'Cañete', 'Lima'),
('150108', 'Lunahuana', 'Cañete', 'Lima'),   
('150201', 'Arequipa', 'Arequipa', 'Arequipa'),
('150202', 'Cayma', 'Arequipa', 'Arequipa'),
('150203', 'Yanahuara', 'Arequipa', 'Arequipa'), 
('150301', 'Cusco', 'Cusco', 'Cusco'),
('150302', 'San Jeronimo', 'Cusco', 'Cusco'),
('150401', 'Trujillo', 'Trujillo', 'La Libertad'),
('150402', 'Victor Larco', 'Trujillo', 'La Libertad'),
('150501', 'Piura', 'Piura', 'Piura'),
('150502', 'Castilla', 'Piura', 'Piura');   
GO

-- 2. CATEGORIA (15 registros)
INSERT INTO categoria (id_categoria, nombre, descripcion, estado, codigo_categoria) VALUES
(1, 'Vinos Tintos', 'Vinos de uva tinta de alta calidad', 1, 'VTN'),
(2, 'Vinos Blancos', 'Vinos jóvenes y refrescantes', 1, 'VBL'),
(3, 'Piscos Puros', 'Destilados de uva quebranta y mollar', 1, 'PIS'),
(4, 'Cremas de Pisco', 'Licores dulces a base de pisco', 1, 'CRE'),
(5, 'Espumantes', 'Vinos con burbujas', 1, 'ESP'),
(6, 'Whiskies', 'Whisky importado y nacional', 1, 'WHI'),
(7, 'Vodkas', 'Vodka importado y nacional', 1, 'VOD'),
(8, 'Rones', 'Ron añejo y blanco', 1, 'RON'),
(9, 'Cervezas Artesanales', 'Cerveza de producción local', 1, 'CER'),
(10, 'Licores', 'Bebidas alcohólicas dulces', 1, 'LIC'),
(11, 'Packs Promocionales', 'Combos de productos', 1, 'PAQ'),
(12, 'Accesorios', 'Copas, decantadores, etc.', 1, 'ACC'),
(13, 'Gaseosas', 'Bebidas sin alcohol', 1, 'GAS'),
(14, 'Snacks', 'Productos para picar', 1, 'SNK'),
(15, 'Vinos de Reserva', 'Vinos añejos', 1, 'RES');
GO

-- 3. PRODUCTO (15 registros)
INSERT INTO producto (nombre, descripcion, codigo_barras_sku, unidad_medida, precio_unitario, id_categoria) VALUES
('Vino Malbec Reservado', 'Vino tinto roble', '7750123456789', '750ml', 45.00, 1),
('Pisco Quebranta Premium', 'Pisco puro artesanal', '7750987654321', '1 Litro', 65.00, 3),
('Crema de Pisco de Lúcuma', 'Licor dulce cremoso', 'SKU-CRE-LUC', '500ml', 35.00, 4),
('Vino Sauvignon Blanc', 'Vino blanco joven', '7750223344556', '750ml', 38.00, 2),
('Pisco Acholado Especial', 'Mezcla de cepas', 'SKU-PIS-ACH', 'Galon 4 Lt', 120.00, 3),
('Vino Cabernet Sauvignon', 'Vino tinto reserva', '7750112233445', '750ml', 55.00, 1),
('Champagne Brut', 'Champagne importado', '7788990011223', '750ml', 120.00, 5),
('Whisky Escocés 12 Años', 'Whisky single malt', '7766554433221', '750ml', 150.00, 6),
('Vodka Premium', 'Vodka importado', '7744112233445', '1 Litro', 80.00, 7),
('Ron Añejo 8 Años', 'Ron añejo premium', '7733221144556', '750ml', 95.00, 8),
('Pack Promo Vinos', '2 Vinos tinto roble + 1 Copa', 'PK-VIN-001', 'Pack', 85.00, 11),
('Cerveza Artesanal IPA', 'Cerveza IPA 750ml', '7700123456789', '750ml', 22.00, 9),
('Vino Orgánico Malbec', 'Vino orgánico certificado', '7766778899001', '750ml', 65.00, 1),
('Crema de Pisco de Cacao', 'Licor sabor cacao', 'SKU-CRE-CAC', '750ml', 38.00, 4),
('Ginebra Premium', 'Ginebra inglesa', '7766554433223', '750ml', 130.00, 10);
GO

-- 4. CLIENTE (15 registros)
INSERT INTO cliente (tipo_documento, numero_documento, nombre, apellido, email, telefono, fecha_nacimiento, ubigeo_id) VALUES
('DNI', '72145678', 'Sebastian', 'Alvarez', 'sebastian@mail.com', '987654321', '1990-05-15', '150101'),
('DNI', '45678912', 'Angel', 'Perez', 'angel.p@mail.com', '999888777', '1985-08-20', '150102'),
('CE', '00123456', 'Valeriano', 'Gomez', 'valeriano@mail.com', '912345678', '1982-12-10', '150103'),
('DNI', '12345678', 'Maria', 'Sosa', 'maria.sosa@mail.com', '955444333', '1995-03-25', '150101'),
('RUC', '20123456789', 'Distribuidora Asia', 'SAC', 'ventas@asiacorp.pe', '944333222', '1990-01-01', '150102'),
('DNI', '76543210', 'Carlos', 'Mendoza', 'carlos.m@mail.com', '998877665', '1985-07-12', '150101'),
('DNI', '98325432', 'Laura', 'Fernandez', 'laura.f@mail.com', '977788899', '1992-11-03', '150201'),
('CE', '98765432', 'John', 'Doe', 'john.doe@mail.com', '966655544', '1988-02-28', '150202'),
('DNI', '11223344', 'Ana', 'Martinez', 'ana.m@mail.com', '955566677', '1998-06-15', '150301'),
('DNI', '55667788', 'Luis', 'Garcia', 'luis.g@mail.com', '944477788', '2000-09-22', '150401'),
('RUC', '20667788991', 'Importadora Vinos', 'SAC', 'import@vinos.com', '977788899', '1985-03-10', '150101'),
('DNI', '99887766', 'Patricia', 'Lopez', 'paty.l@mail.com', '966611122', '1993-12-05', '150501'),
('CE', 'A1234567', 'Robert', 'Smith', 'robert.s@mail.com', '955522233', '1979-04-18', '150102'),
('DNI', '44332211', 'Carmen', 'Rojas', 'carmen.r@mail.com', '944433344', '2001-07-30', '150103'),
('DNI', '77665544', 'Jorge', 'Castillo', 'jorge.c@mail.com', '977766655', '1975-01-25', '150104');
GO

-- 5. ALMACEN (15 registros)
INSERT INTO almacen (nombre, ubicacion, telefono, responsable, codigo_almacen, tipo_producto, cantidad_botellas, ubigeo_id) VALUES
('Bodega Principal - Asia', 'Av. Principal 123, Asia', '999111111', 'Carlos Mendoza', 'BOD-001', 'vino', 4500, '150102'),
('Bodega Costa Azul', 'Carretera Panamericana Sur', '999222222', 'María Torres', 'BOD-002', 'pisco', 3200, '150103'),
('Bodega Norteña', 'Industrial Mz A Lt 5', '999333333', 'José Ramírez', 'BOD-003', 'vino', 6000, '150401'),
('Bodega Sur Andina', 'Cerro Colorado 456', '999444444', 'Lucía Vargas', 'BOD-004', 'pisco', 2800, '150201'),
('Bodega Premium Perú', 'Ate - Carretera Central', '999555555', 'Fernando Ruiz', 'BOD-005', 'vino', 7500, '150101'),
('Bodega Tradición Peruana', 'Ica - Pisco 789', '999666666', 'Ana López', 'BOD-006', 'pisco', 4100, '150102'),
('Almacén Central Lima', 'San Isidro 321', '999777777', 'Roberto Sánchez', 'BOD-007', 'mixto', 12000, '150101'),
('Bodega Valle Sagrado', 'Urubamba - Cusco', '999888888', 'Mariana Quispe', 'BOD-008', 'vino', 3500, '150301'),
('Depósito Cañete', 'Imperial - Cañete', '999999999', 'Jorge Rivas', 'BOD-009', 'licores', 2200, '150106'),
('Bodega Lunahuana', 'Lunahuana - Cañete', '900111111', 'Patricia Ríos', 'BOD-010', 'vino artesanal', 1800, '150108'),
('Centro Distribución Norte', 'Trujillo Centro', '900222222', 'Luis Castillo', 'BOD-011', 'cervezas', 5000, '150401'),
('Bodega Arequipa', 'Yanahuara - Arequipa', '900333333', 'Elena García', 'BOD-012', 'pisco', 3100, '150203'),
('Almacén Piura', 'Castilla - Piura', '900444444', 'Pablo Zegarra', 'BOD-013', 'mixto', 2800, '150502'),
('Bodega San Isidro', 'Lima - San Isidro', '900555555', 'Andrea Paz', 'BOD-014', 'premium', 9500, '150101'),
('Depósito de Reserva', 'Asia - Km 97', '900666666', 'Fernando Vera', 'BOD-015', 'vino', 1500, '150102');
GO

-- 6. INVENTARIO (15 registros)
INSERT INTO inventario (stock_actual, stock_minimo, id_producto, id_almacen) VALUES
(50.00, 10.00, 1, 1),
(100.00, 20.00, 2, 1),
(30.00, 10.00, 3, 5),
(80.00, 20.00, 4, 1),
(15.00, 5.00, 5, 6),
(200.00, 50.00, 1, 7),
(60.00, 15.00, 2, 7),
(40.00, 10.00, 6, 1),
(25.00, 5.00, 7, 5),
(50.00, 10.00, 8, 7),
(30.00, 8.00, 9, 7),
(100.00, 25.00, 11, 5),
(60.00, 15.00, 12, 11),
(45.00, 10.00, 13, 1),
(70.00, 20.00, 14, 5);
GO

-- 7. METODO_PAGO (15 registros)
INSERT INTO metodo_pago (nombre, es_electronico) VALUES
('Efectivo', 0),
('Yape', 1),
('Plin', 1),
('Tarjeta Visa', 1),
('Transferencia BCP', 1),
('Tarjeta Mastercard', 1),
('Transferencia Interbank', 1),
('Transferencia BBVA', 1),
('Tarjeta American Express', 1),
('PagoEfectivo', 1),
('Mercado Pago', 1),
('Depósito Bancario', 1),
('Cheque', 0),
('Tunki', 1),
('Cuenta DNI', 1);
GO

-- 8. VENTA (15 registros)
INSERT INTO venta (tipo_documento, numero_documento, estado, total, referencia_transaccion, id_cliente, id_almacen, id_metodo_pago) VALUES
('Boleta', 'B001-0001', 'C', 45.00, 'CASH-001', 1, 1, 1),
('Factura', 'F001-0001', 'C', 130.00, 'OP-987654', 5, 5, 5),
('Boleta', 'B001-0002', 'C', 70.00, 'YAPE-5544', 2, 1, 2),
('Boleta', 'B001-0003', 'P', 35.00, 'PLIN-1122', 3, 5, 3),
('Ticket', 'T001-0001', 'C', 105.00, 'VISA-4433', 4, 1, 4),
('Boleta', 'B001-0004', 'C', 120.00, 'CASH-002', 6, 7, 1),
('Factura', 'F001-0002', 'C', 255.00, 'OP-123456', 11, 1, 7),
('Boleta', 'B001-0005', 'C', 89.00, 'YAPE-1122', 7, 7, 2),
('Ticket', 'T001-0002', 'C', 150.00, 'VISA-5566', 8, 14, 6),
('Boleta', 'B001-0006', 'C', 42.00, 'PLIN-3344', 9, 7, 3),
('Boleta', 'B001-0007', 'C', 120.00, 'CASH-003', 10, 4, 1),
('Factura', 'F001-0003', 'C', 260.00, 'OP-789012', 12, 7, 8),
('Boleta', 'B001-0008', 'C', 75.00, 'YAPE-6677', 13, 14, 2),
('Boleta', 'B001-0009', 'P', 80.00, 'PLIN-8899', 14, 7, 4),
('Ticket', 'T001-0003', 'C', 95.00, 'VISA-9900', 15, 2, 6);
GO

-- 9. DETALLE_VENTA (15 registros)
INSERT INTO detalle_venta (cantidad, precio_unitario, subtotal, descuento, id_venta, id_producto) VALUES
(1.00, 45.00, 45.00, 0.00, 1, 1),
(2.00, 65.00, 130.00, 0.00, 2, 2),
(1.00, 70.00, 70.00, 0.00, 3, 5),
(1.00, 35.00, 35.00, 0.00, 4, 3),
(3.00, 35.00, 105.00, 0.00, 5, 3),
(2.00, 60.00, 120.00, 0.00, 6, 6),
(3.00, 85.00, 255.00, 0.00, 7, 2),
(1.00, 89.00, 89.00, 0.00, 8, 11),
(1.00, 150.00, 150.00, 0.00, 9, 8),
(1.00, 42.00, 42.00, 0.00, 10, 3),
(2.00, 60.00, 120.00, 0.00, 11, 6),
(2.00, 130.00, 260.00, 0.00, 12, 10),
(1.00, 75.00, 75.00, 0.00, 13, 2),
(2.00, 40.00, 80.00, 0.00, 14, 4),
(1.00, 95.00, 95.00, 0.00, 15, 10);
GO









































-- Eliminar todas las Foreign Keys
DECLARE @sql NVARCHAR(MAX) = N'';
SELECT @sql += 'ALTER TABLE ' + QUOTENAME(s.name) + '.' + QUOTENAME(t.name) +
    ' DROP CONSTRAINT ' + QUOTENAME(f.name) + ';'
FROM sys.foreign_keys AS f
    INNER JOIN sys.tables AS t ON f.parent_object_id = t.object_id
    INNER JOIN sys.schemas AS s ON t.schema_id = s.schema_id;

EXEC sp_executesql @sql;

-- Eliminar todas las tablas
SET @sql = N'';
SELECT @sql += 'DROP TABLE ' + QUOTENAME(s.name) + '.' + QUOTENAME(t.name) + ';'
FROM sys.tables AS t
    INNER JOIN sys.schemas AS s ON t.schema_id = s.schema_id;

EXEC sp_executesql @sql;