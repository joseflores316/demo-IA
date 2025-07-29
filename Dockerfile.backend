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

# Debug: List the target directory to see what files are generated
RUN ls -la target/

# Stage 2: Create the final image
FROM eclipse-temurin:11-jdk-alpine

# Crear directorio para uploads
RUN mkdir -p /app/uploads

# Cambiar al directorio de trabajo
WORKDIR /app

# Copy the specific JAR from the builder stage
COPY --from=builder /app/target/demo-IA-0.0.1-SNAPSHOT.jar app.jar

# Debug: List the app directory to verify the JAR was copied
RUN ls -la /app/

# Usar perfil de producción
ENTRYPOINT ["java","-Dspring.profiles.active=prod","-jar","/app/app.jar"]
