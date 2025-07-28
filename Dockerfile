# Stage 1: Build the application
FROM eclipse-temurin:11-jdk-alpine AS builder

# Install Maven
RUN apk add --no-cache maven

# Set working directory
WORKDIR /app

# Copy the backend source code
COPY backend/ ./

# Build the application
RUN mvn clean package -DskipTests

# Stage 2: Create the final image
FROM eclipse-temurin:11-jdk-alpine

# Crear directorio para uploads
RUN mkdir -p /app/uploads

# Cambiar al directorio de trabajo
WORKDIR /app

# Copy the JAR from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Usar perfil de producción
ENTRYPOINT ["java","-Dspring.profiles.active=prod","-jar","/app.jar"]
