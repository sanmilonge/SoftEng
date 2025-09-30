FROM openjdk:latest

# Assumes the local path is ./target/classes/Coursework and it contains .class files


COPY ./target/classes/Coursework /tmp/Coursework

# Set the working directory inside the container to /tmp
# All subsequent commands, including ENTRYPOINT, will run from here

WORKDIR /tmp

# This runs the 'Coursework.Test' Java class (must have a valid main method)

ENTRYPOINT ["java", "Coursework.Test"]