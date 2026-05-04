# Stage 1: Build con tu propia imagen de Maven
FROM sebastianalvareztito/maven:3.9-amazoncorretto-21-alpine AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run con tu propia imagen de Java
FROM sebastianalvareztito/eclipse-temurin:21-jre-alpine
WORKDIR /app
# Copiamos el .jar generado en la etapa anterior
COPY --from=builder /app/target/*.jar app.jar

# Exponemos el puerto (ajústalo según tu aplicación, ej: 8080)
EXPOSE 8085

ENTRYPOINT ["java", "-jar", "app.jar"]