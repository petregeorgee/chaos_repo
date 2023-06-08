# Use a base image with Java 17
FROM eclipse-temurin:17-jdk-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the Spring Boot application JAR file into the container
COPY target/image.encrypt.decrypt-0.0.1-SNAPSHOT.jar /app

# Expose the port on which the application will listen
EXPOSE 8080

# Set the command to run the application when the container starts
CMD ["java", "-jar", "image.encrypt.decrypt-0.0.1-SNAPSHOT.jar"]
