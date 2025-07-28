FROM eclipse-temurin:11-jdk-alpine
VOLUME /tmp

# Crear directorio para uploads
RUN mkdir -p /app/uploads

COPY target/*.jar app.jar

# Usar perfil de producción
ENTRYPOINT ["java","-Dspring.profiles.active=prod","-jar","/app.jar"]
