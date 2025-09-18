# Etapa 1: Construcción de la aplicación .jar
FROM maven:3.8.5-openjdk-17 AS build

WORKDIR /app

COPY pom.xml ./
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

RUN ls -la /app/target

# Etapa 2: Ejecución de la aplicación
FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY --from=build /app/target/*.jar /app/punto_de_sal.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/punto_de_sal.jar"]
