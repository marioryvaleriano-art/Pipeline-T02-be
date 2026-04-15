#!/bin/bash
set -e

# Variables - MODIFICAR SEGÚN TU CONFIGURACIÓN
RESOURCE_GROUP="rg-angelarenas"
LOCATION="eastus"
ACI_SQLSERVER="aci-sqlserver"
ACI_BACKEND="aci-backend"
ACI_NETWORK="aci-network"

# Credenciales SQL Server
SA_PASSWORD="TuPasswordFuerte123!"
DATABASE_NAME="usuarios_db"

# ============================================
# 1. CREAR GRUPO DE RECURSOS
# ============================================
echo "Creando grupo de recursos..."
az group create \
  --name $RESOURCE_GROUP \
  --location $LOCATION

# ============================================
# 2. DESPLEGAR SQL SERVER
# ============================================
echo "Desplegando SQL Server en ACI..."
az container create \
  --resource-group $RESOURCE_GROUP \
  --name $ACI_SQLSERVER \
  --image mcr.microsoft.com/mssql/server:2022-latest \
  --ports 1433 \
  --environment-variables \
    ACCEPT_EULA=Y \
    MSSQL_SA_PASSWORD=$SA_PASSWORD \
    MSSQL_PID=Express \
  --dns-name-label sqlserver-$RANDOM \
  --ip-address public \
  --location $LOCATION

# Esperar a que SQL Server esté listo
echo "Esperando que SQL Server esté disponible..."
sleep 30

# ============================================
# 3. CREAR BASE DE DATOS
# ============================================
echo "Creando base de datos..."
SQL_IP=$(az container show \
  --resource-group $RESOURCE_GROUP \
  --name $ACI_SQLSERVER \
  --query ipAddress.ip --output tsv)

# Instalar sqlcmd si no existe (Linux/Mac)
if ! command -v sqlcmd &> /dev/null; then
    echo "Instalando sqlcmd..."
    curl https://packages.microsoft.com/keys/microsoft.asc | apt-key add -
    curl https://packages.microsoft.com/config/ubuntu/20.04/prod.list > /etc/apt/sources.list.d/mssql-release.list
    apt-get update
    ACCEPT_EULA=Y apt-get install -y mssql-tools unixodbc-dev
    export PATH="$PATH:/opt/mssql-tools/bin"
fi

/opt/mssql-tools/bin/sqlcmd \
  -S $SQL_IP,1433 \
  -U sa \
  -P $SA_PASSWORD \
  -Q "CREATE DATABASE $DATABASE_NAME; GO"

# ============================================
# 4. COMPILAR Y SUBIR IMAGEN DOCKER
# ============================================
echo "Compilando imagen Docker..."
docker build -t angelarenas-backend:$RANDOM .

echo "Subiendo imagen a Docker Hub (o ACR)..."
# Opción A: Docker Hub
docker tag angelarenas-backend:latest TU_DOCKERHUB_USER/angelarenas-backend:latest
docker push TU_DOCKERHUB_USER/angelarenas-backend:latest

# Opción B: Azure Container Registry (más rápido para ACI)
# Descomenta si usas ACR:
# az acr create --resource-group $RESOURCE_GROUP --name arangelarenas --sku Basic
# az acr login --name arangelarenas
# docker tag angelarenas-backend:latest arangelarenas.azurecr.io/angelarenas-backend:latest
# docker push arangelarenas.azurecr.io/angelarenas-backend:latest

# ============================================
# 5. DESPLEGAR BACKEND
# ============================================
echo "Desplegando backend en ACI..."
az container create \
  --resource-group $RESOURCE_GROUP \
  --name $ACI_BACKEND \
  --image TU_DOCKERHUB_USER/angelarenas-backend:latest \
  --ports 8080 \
  --environment-variables \
    SPRING_DATASOURCE_URL="jdbc:sqlserver://$SQL_IP;databaseName=$DATABASE_NAME;encrypt=true;trustServerCertificate=true" \
    SPRING_DATASOURCE_USERNAME=sa \
    SPRING_DATASOURCE_PASSWORD=$SA_PASSWORD \
  --dns-name-label angelarenas-api-$RANDOM \
  --ip-address public \
  --location $LOCATION

# ============================================
# 6. MOSTRAR RESULTADOS
# ============================================
echo ""
echo "=========================================="
echo "DESPLIEGUE COMPLETADO"
echo "=========================================="
echo "SQL Server: http://$SQL_IP:1433"
BACKEND_IP=$(az container show \
  --resource-group $RESOURCE_GROUP \
  --name $ACI_BACKEND \
  --query ipAddress.ip --output tsv)
echo "Backend API: http://$BACKEND_IP:8080"
echo ""
echo "Endpoints disponibles:"
echo "  GET  http://$BACKEND_IP:8080/api/usuarios"
echo "  POST http://$BACKEND_IP:8080/api/usuarios"
echo "=========================================="
