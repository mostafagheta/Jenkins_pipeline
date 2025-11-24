# Multi-stage Dockerfile
# Builder: use Maven with JDK 11 to compile and package the application
# Use a Maven image that includes OpenJDK 11 (slim variant is commonly available on Docker Hub)
FROM maven:3.8.8-openjdk-11-slim AS builder
WORKDIR /build

# Copy only pom first to leverage Docker layer caching for dependencies
COPY pom.xml ./
COPY src ./src

# Build the application and copy the produced jar to a fixed name
RUN mvn -B -DskipTests package \
    && cp target/*.jar app.jar

# Runtime: small JRE image
FROM eclipse-temurin:11-jre-jammy
WORKDIR /app

# Copy the jar built in the previous stage
COPY --from=builder /build/app.jar ./app.jar

# If your app listens on a port, expose it (optional)
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
