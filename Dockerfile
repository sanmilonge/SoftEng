#FROM openjdk:latest
#COPY ./target/classes/Coursework /tmp/Coursework
#WORKDIR /tmp
#ENTRYPOINT ["java", "Coursework.App"]

FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy the packaged JAR (includes dependencies)
COPY target/*-jar-with-dependencies.jar app.jar

# Run the JAR
ENTRYPOINT ["java", "-jar", "app.jar"]
