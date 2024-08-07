# Use OpenJDK 17 base image
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the application JAR file to the container
ARG JAR_FILE=target/RoombaHoover-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

# Expose the application port
EXPOSE 8080

# Set the entry point to run the application
ENTRYPOINT ["java","-jar","app.jar"]