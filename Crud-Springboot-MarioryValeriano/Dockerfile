# Cambiamos la imagen base a una que sí está disponible
FROM eclipse-temurin:17-jdk-alpine
# Copiamos el archivo .jar que genera Maven a la imagen
COPY target/*.jar app.jar
# Ejecutamos la aplicación
ENTRYPOINT ["java", "-jar", "/app.jar"]