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

# Pull the latest patched Alpine package for the remaining Trivy finding.
RUN apk upgrade --no-cache libpng

RUN addgroup -S app && adduser -S -G app app

# Copy built jar from builder
COPY --from=builder /app/target/*.jar app.jar
RUN chown app:app /app/app.jar

# Expose port 8080
EXPOSE 8080

USER app

# Default command
CMD ["java", "-jar", "app.jar"]
