# ---- Build stage ----
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# copy pom.xml first for caching
COPY pom.xml .
RUN mvn -B -q -DskipTests dependency:go-offline

# copy source and build
COPY src ./src
RUN mvn -B -DskipTests package && \
    JAR=$(ls target/*-jar-with-dependencies.jar 2>/dev/null || ls target/*SNAPSHOT.jar) && \
    cp "$JAR" /app/app.jar && \
    ls -lh /app/app.jar

# ---- Runtime stage ----
FROM amazoncorretto:17
WORKDIR /app
COPY --from=build /app/app.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
