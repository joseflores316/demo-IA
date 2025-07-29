# Etapa 1: Build
FROM eclipse-temurin:11-jdk-alpine AS build

WORKDIR /app

# Copiar archivos de Maven
COPY mvnw .
COPY mvnw.cmd .
COPY pom.xml .
COPY .mvn .mvn

# Copiar código fuente
COPY src src

# Hacer build de la aplicación
RUN chmod +x ./mvnw
RUN ./mvnw clean package -DskipTests

# Etapa 2: Runtime
FROM eclipse-temurin:11-jre-alpine

WORKDIR /app

# Crear directorio para uploads
RUN mkdir -p /tmp/uploads

# Copiar el JAR desde la etapa de build
COPY --from=build /app/target/*.jar app.jar

# Exponer puerto 8080
EXPOSE 8080

# Comando para ejecutar la aplicación
CMD ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]
