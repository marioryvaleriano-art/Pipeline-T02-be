#!/bin/bash

echo "=== CREANDO LA BASE DE DATOS usuarios_db ==="

# Esperar a que SQL Server esté listo
sleep 10

# Crear la base de datos
docker exec sqlserver-db /opt/mssql-tools18/bin/sqlcmd \
  -S localhost -U sa -P "${SA_PASSWORD}" \
  -C \
  -Q "IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = 'usuarios_db') CREATE DATABASE usuarios_db; GO"

echo "=== BASE DE DATOS CREADA EXITOSAMENTE ==="
echo "=== AHORA PUEDES INICIAR EL BACKEND ==="
