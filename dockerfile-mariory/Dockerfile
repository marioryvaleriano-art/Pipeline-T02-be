# ETAPA 1: BUILD
FROM mariory/maven:3.9-amazoncorretto-25-alpine AS build

WORKDIR /app

COPY . .

RUN mvn clean package -DskipTests


# ETAPA 2: RUN
FROM mariory/eclipse-temurin:25-jre-alpine

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8085

ENTRYPOINT ["java", "-jar", "app.jar"]