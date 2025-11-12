FROM amazoncorretto:17

# Working directory
WORKDIR /app

# Copy packaged JAR (the one built by Maven)
COPY ./target/devops.jar /app/devops.jar

# Run the app (connect to db container on port 3306, delay 30s)
ENTRYPOINT ["java", "-jar", "devops.jar", "db:3306", "30000"]
