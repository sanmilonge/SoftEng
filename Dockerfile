FROM openjdk:latest
COPY ./target/classes/Coursework /tmp/Coursework
WORKDIR /tmp
ENTRYPOINT ["java", "Coursework.Test"]