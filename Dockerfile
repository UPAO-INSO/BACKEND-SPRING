# Etapa 1: Construcción
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

# zip necesario para parchear culqi-java (fat jar con SLF4J embebido)
RUN apt-get update -qq && apt-get install -y -qq zip --no-install-recommends \
    && rm -rf /var/lib/apt/lists/*

COPY pom.xml ./
# Descarga dependencias y parchea culqi-java en el cache antes de compilar
RUN --mount=type=cache,id=maven-cache,target=/root/.m2 mvn -B dependency:go-offline \
    && CULQI_JAR=$(find /root/.m2/repository -name "culqi-java-v2.0.4.jar" 2>/dev/null | head -1) \
    && if [ -n "$CULQI_JAR" ]; then \
        jar tf "$CULQI_JAR" 2>/dev/null \
        | grep -E '^org/slf4j|^META-INF/services/org\.slf4j' \
        | grep -v '/$' \
        | tr '\n' '\0' \
        | xargs -0 -r zip -d "$CULQI_JAR" 2>/dev/null || true ; \
    fi

COPY src ./src
RUN --mount=type=cache,id=maven-cache,target=/root/.m2 mvn -B clean package -DskipTests

# Etapa 2: Ejecución
FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY --from=build /app/target/*.jar /app/punto_de_sal.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/punto_de_sal.jar"]
