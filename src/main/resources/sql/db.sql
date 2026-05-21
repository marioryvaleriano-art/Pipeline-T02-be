-- Script de inicialización para Bodega Peirano con Auditoría
-- Base de datos: BodegaPeiranoDB

IF NOT EXISTS (SELECT * FROM sys.databases WHERE name = 'BodegaPeiranoDBF')
BEGIN
    CREATE DATABASE BodegaPeiranoDBF;
END
GO

USE BodegaPeiranoDBF;
GO




-- 2. TABLA DE APOYO: CATEGORIA
CREATE TABLE categoria (
                           id_categoria int NOT NULL,
                           nombre varchar(50) NOT NULL,
                           descripcion varchar(150) NULL,
                           estado bit NOT NULL,
                           fecha_creacion datetime NOT NULL,
                           codigo_categoria char(3) NOT NULL,
                           CONSTRAINT categoria_pk PRIMARY KEY (id_categoria)
);

-- 3. TABLA MAESTRA: PRODUCTO (Con 12 campos: 8 negocio + 4 auditoría)
CREATE TABLE producto (
                          id_producto INT IDENTITY(1,1) NOT NULL, -- IDENTITY es la clave aquí
                          nombre VARCHAR(100) NOT NULL,
                          descripcion VARCHAR(255) NULL,
                          codigo_barras_sku VARCHAR(30) NOT NULL,
                          unidad_medida VARCHAR(20) NOT NULL,
                          precio_unitario DECIMAL(10,2) NOT NULL,
                          estado BIT NOT NULL DEFAULT 1,         -- Valor por defecto activo
                          id_categoria INT NOT NULL,

    -- Campos de Auditoría
                          fecha_creacion DATETIME NOT NULL DEFAULT GETDATE(),
                          fecha_actualizacion DATETIME NULL,
                          fecha_eliminacion DATETIME NULL,
                          fecha_restauracion DATETIME NULL,

                          CONSTRAINT producto_pk PRIMARY KEY (id_producto)
);
CREATE TABLE Cliente (
                         id_cliente INT PRIMARY KEY IDENTITY(1,1), -- Equivalente a AUTO_INCREMENT en MySQL
                         tipo_documento CHAR(3) NOT NULL,
                         numero_docum VARCHAR(12) NOT NULL UNIQUE, -- Unique por validación en backend
                         nombre VARCHAR(50) NOT NULL,
                         apellido VARCHAR(50) NOT NULL,
                         correo VARCHAR(150) NOT NULL UNIQUE, -- Unique por @Column(unique = true)
                         telefono VARCHAR(15) NOT NULL,
                         fecha_nacimiento DATE NOT NULL,
                         estado BIT NOT NULL DEFAULT 1, -- BIT equivale a BOOLEAN, 1 = true



    -- Campos de Auditoría
                         fecha_creacion DATETIME DEFAULT GETDATE(), -- Equivalente a CURRENT_TIMESTAMP
                         fecha_actualizacion DATETIME NULL,
                         fecha_eliminacion DATETIME NULL,
                         fecha_restauracion DATETIME NULL,

);

CREATE TABLE almacen (
                         id_almacen INT IDENTITY PRIMARY KEY,
                         nombre VARCHAR(100),
                         ubicacion VARCHAR(150),
                         telefono VARCHAR(20),
                         responsable VARCHAR(100),
                         codigo_almacen VARCHAR(50),
                         tipo_producto VARCHAR(50),
                         cantidad_botellas INT,
                         estado BIT DEFAULT 1,

    -- Campos de Auditoría
                         fecha_creacion DATETIME2,
                         fecha_actualizacion DATETIME2,
                         fecha_eliminacion DATETIME2,
                         fecha_restauracion DATETIME2
);


-- 4. RELACIÓN
ALTER TABLE producto ADD CONSTRAINT producto_categoria_fk
    FOREIGN KEY (id_categoria)
        REFERENCES categoria (id_categoria);

-- 5. REGISTROS DE CATEGORIA
INSERT INTO categoria (id_categoria, nombre, descripcion, estado, fecha_creacion, codigo_categoria) VALUES
                                                                                                        (1, 'Vinos Tintos', 'Vinos de uva tinta de alta calidad', 1, GETDATE(), 'VIN'),
                                                                                                        (2, 'Piscos Puros', 'Destilados de uva quebranta y mollar', 1, GETDATE(), 'PIS'),
                                                                                                        (3, 'Cremas de Pisco', 'Licores dulces a base de pisco y fruta', 1, GETDATE(), 'CRE'),
                                                                                                        (4, 'Vinos Blancos', 'Vinos jóvenes y refrescantes', 1, GETDATE(), 'VIB'),
                                                                                                        (5, 'Promociones', 'Packs y descuentos especiales', 1, GETDATE(), 'PRO');

-- 6. REGISTROS DE PRODUCTO
-- Nota: Al no incluir 'id_producto', SQL Server asignará el 1, 2, 3... automáticamente.
INSERT INTO producto (nombre, descripcion, codigo_barras_sku, unidad_medida, precio_unitario, estado, id_categoria, fecha_creacion) VALUES
                                                                                                                                        ('Vino Malbec Reservado', 'Vino tinto roble 750ml', '7750123456789', '750ml', 45.00, 1, 1, GETDATE()),
                                                                                                                                        ('Pisco Quebranta Premium', 'Pisco puro artesanal', '7750987654321', '1 Litro', 65.00, 1, 2, GETDATE()),
                                                                                                                                        ('Crema de Pisco de Lúcuma', 'Licor dulce cremoso', 'SKU-CRE-LUC', '500ml', 35.00, 1, 3, GETDATE()),
                                                                                                                                        ('Vino Sauvignon Blanc', 'Vino blanco joven', '7750223344556', '750ml', 38.00, 1, 4, GETDATE()),
                                                                                                                                        ('Pisco Acholado Especial', 'Mezcla de cepas seleccionadas', 'SKU-PIS-ACH', '1 Litro', 70.00, 1, 2, GETDATE());
GO


-- 6. REGISTROS DE ALMACEN
INSERT INTO almacen
(nombre, ubicacion, telefono, responsable, codigo_almacen, tipo_producto, cantidad_botellas, estado, fecha_creacion)
VALUES
('Bodega Andina', 'Valle Sagrado - Cusco', '999111111', 'Carlos Mendoza', 'BOD-001', 'vino', 4500, 1, GETDATE()),
('Bodega Costa Azul', 'Ica - Nazca', '999222222', 'María Torres', 'BOD-002', 'pisco', 3200, 1, GETDATE()),
('Bodega Norteña', 'Trujillo - Industrial', '999333333', 'José Ramírez', 'BOD-003', 'vino', 6000, 1, GETDATE()),
('Bodega Sur Andina', 'Arequipa - Cerro Colorado', '999444444', 'Lucía Vargas', 'BOD-004', 'pisco', 2800, 1, GETDATE()),
('Bodega Premium Perú', 'Lima - Ate', '999555555', 'Fernando Ruiz', 'BOD-005', 'vino', 7500, 1, GETDATE()),
('Bodega Tradición Peruana', 'Ica - Pisco', '999666666', 'Ana López', 'BOD-006', 'pisco', 4100, 1, GETDATE());

