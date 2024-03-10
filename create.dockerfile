# Use the official Maven image to create a build artifact.
# https://hub.docker.com/_/maven
FROM maven:3.8-openjdk-17 AS build

# Copy local code to the container image.
WORKDIR /app
COPY pom.xml .
COPY src ./src

# Build a release artifact.
RUN mvn package -DskipTests

# Use OpenJDK for runtime.
FROM openjdk:17-slim

# Copy the jar to the production image from the builder stage.
# The JAR file is located in the /app/target directory of the build container.
COPY --from=build /app/target/CloudActivityAppCST323-1.0-TESTAPP.jar /app/application.jar

# Run the web service on container startup.
CMD ["java", "-jar", "/app/application.jar"]



