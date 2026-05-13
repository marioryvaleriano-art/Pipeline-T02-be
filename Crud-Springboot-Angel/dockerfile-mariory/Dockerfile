# USANDO TUS IMÁGENES DE DOCKERHUB CON JAVA 25
FROM mariory/maven:3.9-amazoncorretto-25-alpine AS build
WORKDIR /app
# Copiamos los archivos desde la carpeta donde está el pom.xml
COPY . .
RUN mvn clean package -DskipTests

FROM mariory/eclipse-temurin:25-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]