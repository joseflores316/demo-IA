# Stage 1: Build the application
FROM eclipse-temurin:11-jdk-alpine AS builder

# Install Maven
RUN apk add --no-cache maven

# Set working directory
WORKDIR /app

# Copy only the backend source code
COPY . ./

# Build the application
RUN mvn clean package -DskipTests

# Stage 2: Create the final image
FROM eclipse-temurin:11-jre-alpine

# Crear directorio para uploads
RUN mkdir -p /app/uploads

# Cambiar al directorio de trabajo
WORKDIR /app

# Copy the specific JAR from the builder stage
COPY --from=builder /app/target/demo-IA-0.0.1-SNAPSHOT.jar app.jar

# Configuración optimizada para Railway con memoria limitada
ENTRYPOINT ["java", \
    "-Dspring.profiles.active=prod", \
    "-Xmx400m", \
    "-Xms200m", \
    "-XX:MaxMetaspaceSize=128m", \
    "-XX:+UseG1GC", \
    "-XX:+UseStringDeduplication", \
    "-Djava.security.egd=file:/dev/./urandom", \
    "-jar", "/app/app.jar"]
