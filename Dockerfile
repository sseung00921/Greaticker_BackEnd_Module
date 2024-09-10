# Step 1: Use Gradle image to build the project
FROM gradle:7.6.0-jdk17 AS build
WORKDIR /app

# Copy Gradle wrapper and configuration files
COPY build.gradle settings.gradle gradlew /app/
COPY gradle /app/gradle

# Copy the project source files
COPY src /app/src

# Build the project (excluding tests)
RUN ./gradlew clean build -x test

# Step 2: Use lightweight OpenJDK image to run the application
FROM openjdk:17-jdk-alpine
WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /app/build/libs/*.jar /app/app.jar

# Expose the application port
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
