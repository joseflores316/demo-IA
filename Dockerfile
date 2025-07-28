FROM eclipse-temurin:11-jdk-alpine

# Crear directorio para uploads
RUN mkdir -p /app/uploads

# Cambiar al directorio de trabajo
WORKDIR /app

# Copiar el JAR compilado desde la carpeta backend
COPY backend/target/*.jar app.jar

# Usar perfil de producción
ENTRYPOINT ["java","-Dspring.profiles.active=prod","-jar","/app.jar"]
