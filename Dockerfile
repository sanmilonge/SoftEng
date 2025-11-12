# ---- Build stage ----
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copy pom.xml first (for dependency caching)
COPY pom.xml .
RUN mvn -B -q -DskipTests dependency:go-offline

# Copy project source
COPY src ./src

# Build and dynamically pick the built JAR (devops.jar, shaded, or snapshot)
RUN mvn -B -DskipTests package && \
    JAR="$(ls -1 target/*-jar-with-dependencies*.jar 2>/dev/null || true)"; \
    if [ -z "$JAR" ]; then \
      JAR="$(ls -1 target/*.jar | grep -v 'original-' | head -n1)"; \
    fi; \
    echo "✅ Using artifact: $JAR" && \
    cp "$JAR" /app/app.jar && \
    ls -lh /app/app.jar

# ---- Runtime stage ----
FROM amazoncorretto:17
WORKDIR /app

# Copy the built JAR from the build stage
COPY --from=build /app/app.jar /app/app.jar

# Expose the app port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
