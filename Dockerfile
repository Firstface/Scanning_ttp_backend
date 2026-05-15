# Builder stage
FROM maven:3.9-eclipse-temurin-17-alpine AS builder

WORKDIR /app

# Copy pom.xml first for better cache
COPY pom.xml .

# Download dependencies
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the app
RUN mvn clean package -DskipTests -B

# Production stage
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy built jar from builder
COPY --from=builder /app/target/*.jar app.jar

# Expose port 8080
EXPOSE 8080

# Default command
CMD ["java", "-jar", "app.jar"]
