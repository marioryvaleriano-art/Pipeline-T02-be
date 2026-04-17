-- Script de inicialización para Bodega Peirano con Auditoría
-- Base de datos: BodegaPeiranoDB

IF NOT EXISTS (SELECT * FROM sys.databases WHERE name = 'BodegaPeiranoDB')
BEGIN
    CREATE DATABASE BodegaPeiranoDB;
END
GO

USE BodegaPeiranoDB;
GO

-- Tabla de productos con los 4 campos de auditoría requeridos
CREATE TABLE productos (
    id INT IDENTITY(1,1) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    precio DECIMAL(10,2) NOT NULL,
    estado BIT DEFAULT 1,
    -- Campos de Auditoría (Fulfillment de requerimiento)
    fecha_registro DATETIME,
    fecha_edicion DATETIME,
    fecha_eliminacion DATETIME,
    fecha_restauracion DATETIME
);
GO

-- Insert opcional para pruebas iniciales
INSERT INTO productos (nombre, precio, estado, fecha_registro) 
VALUES ('Vino Malbec Reservado', 45.00, 1, GETDATE());