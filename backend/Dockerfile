# Usar imagen base más simple y confiable
FROM ubuntu:20.04

# Instalar Java 11 y Maven
RUN apt-get update && \
    apt-get install -y openjdk-11-jdk maven && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Copiar archivos de Maven
COPY pom.xml .
COPY mvnw .
COPY mvnw.cmd .
COPY .mvn .mvn

# Copiar código fuente
COPY src src

# Crear directorio para uploads
RUN mkdir -p /tmp/uploads

# Hacer build de la aplicación
RUN chmod +x ./mvnw
RUN ./mvnw clean package -DskipTests

# Exponer puerto 8080
EXPOSE 8080

# Comando para ejecutar la aplicación
CMD ["java", "-jar", "target/demo-IA-0.0.1-SNAPSHOT.jar", "--spring.profiles.active=prod"]
