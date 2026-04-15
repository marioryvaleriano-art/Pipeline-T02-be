FROM maven:3.9-amazoncorretto-25-alpine AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:25-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]





    # Si se quiere subir una imagen a docker hub para luego utilizarla

   # Stage 1: Build with Maven
      #FROM maven:3.9-amazoncorretto-25-alpine AS builder
      #WORKDIR /app
      #COPY pom.xml .
      #COPY src ./src
      #RUN mvn clean package -DskipTests
      #
      ## Stage 2: Run with Java
      #FROM eclipse-temurin:25-jre-alpine
      #WORKDIR /app
      #COPY --from=builder /app/target/*.jar app.jar
      #ENTRYPOINT ["java", "-jar", "app.jar"]
      #
      #
      ## docker run -e "ACCEPT_EULA=Y" -e "SA_PASSWORD=Admin12345" -p 1433:1433 --name sqlserver -d juan321/sql-server:2022 Esto levanta la base de datos
      #
      #
      ## docker build -t juan321/springboot-sqlserver:1.0 . (construye la imagen (a partir del Dockerfile))
      #
      ## docker run -d --name springboot-sqlserver -p 8085:8085 juan321/springboot-sqlserver:1.0 crea y ejecuta un contenedor (a partir de la imagen)
      #
      ## docker push juan321/springboot-sqlserver:1.0   sube la imagen (no el contenedor) (subimos la imagen a dockerhub)
