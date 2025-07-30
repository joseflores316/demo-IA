# 📚 Documentación del Proyecto Demo-IA

## 🎯 Descripción General

**Demo-IA** es una aplicación web full-stack para la gestión de actrices y escenas, desarrollada con arquitectura hexagonal. El proyecto incluye un backend en Spring Boot, un frontend en Angular, base de datos PostgreSQL, y almacenamiento de imágenes en la nube con Cloudinary.

## 🏗️ Arquitectura del Sistema

### Arquitectura Hexagonal (Clean Architecture)
El proyecto sigue los principios de arquitectura hexagonal para mantener la separación de responsabilidades:

```
backend/
├── domain/          # Lógica de negocio pura
│   ├── model/       # Entidades del dominio
│   ├── ports/       # Interfaces (in/out)
│   └── services/    # Casos de uso
├── infrastructure/ # Implementaciones técnicas
│   ├── web/        # Controladores REST
│   ├── persistence/ # Repositorios JPA
│   └── storage/    # Almacenamiento de archivos
└── config/         # Configuración de Spring
```

### Stack Tecnológico

#### Backend
- **Java 11** - Lenguaje de programación
- **Spring Boot 2.5.14** - Framework principal
- **Spring Data JPA** - Persistencia de datos
- **PostgreSQL** - Base de datos principal
- **H2** - Base de datos para desarrollo
- **MapStruct** - Mapeo entre DTOs y entidades
- **SpringDoc OpenAPI** - Documentación de API
- **Cloudinary** - Almacenamiento de imágenes en la nube

#### Frontend
- **Angular 18** - Framework frontend
- **TypeScript** - Lenguaje de programación
- **SCSS** - Estilos
- **Nginx** - Servidor web para producción

#### Infraestructura
- **Railway** - Plataforma de despliegue
- **Docker** - Containerización
- **Git/GitHub** - Control de versiones

## 🗂️ Modelo de Datos

### Diagrama ERD
El proyecto está basado en el siguiente modelo de datos:

```
Actrices (1) ←→ (N) Pais
Actrices (N) ←→ (N) Caracteristicas [ActrizCaracteristica]
Actrices (N) ←→ (N) Escenas [ActrizEscena]
Escenas (N) ←→ (1) TipoEscena
```

### Entidades Principales

#### Actriz
- `id`: Identificador único
- `nombre`: Nombre de la actriz (obligatorio)
- `fechaNacimiento`: Fecha de nacimiento
- `calificacion`: Calificación (1-5)
- `imagenUrl`: URL de la imagen (Cloudinary)
- `paisId`: Referencia al país
- `fechaRegistro`: Timestamp de creación

#### Escena
- `id`: Identificador único
- `descripcion`: Descripción de la escena
- `fechaGrabacion`: Fecha de grabación
- `duracion`: Duración en formato ISO (PT30M)
- `ubicacion`: Ubicación de grabación
- `imagenUrl`: URL de la imagen (Cloudinary)
- `tipoEscenaId`: Referencia al tipo de escena

#### País
- `id`: Identificador único
- `nombre`: Nombre del país (único)
- `codigoIso`: Código ISO del país
- `banderaUrl`: URL de la bandera

## 🔧 Configuración del Proyecto

### Variables de Entorno

#### Desarrollo (application.properties)
```properties
# Base de datos H2
spring.datasource.url=jdbc:h2:mem:testdb
spring.jpa.hibernate.ddl-auto=create-drop

# Cloudinary
cloudinary.cloud-name=${CLOUDINARY_CLOUD_NAME:your-cloud-name}
cloudinary.api-key=${CLOUDINARY_API_KEY:your-api-key}
cloudinary.api-secret=${CLOUDINARY_API_SECRET:your-api-secret}
```

#### Producción (application-prod.properties)
```properties
# Base de datos PostgreSQL
spring.datasource.url=${JDBC_DATABASE_URL}
spring.datasource.username=${PGUSER}
spring.datasource.password=${PGPASSWORD}

# Cloudinary
cloudinary.cloud-name=${CLOUDINARY_CLOUD_NAME}
cloudinary.api-key=${CLOUDINARY_API_KEY}
cloudinary.api-secret=${CLOUDINARY_API_SECRET}
```

### Variables de Railway
```
# Base de datos
JDBC_DATABASE_URL=postgresql://...
PGUSER=postgres
PGPASSWORD=...

# Cloudinary
CLOUDINARY_CLOUD_NAME=tu-cloud-name
CLOUDINARY_API_KEY=tu-api-key
CLOUDINARY_API_SECRET=tu-api-secret

# CORS
CORS_ORIGINS=https://tu-frontend-url.railway.app
```

## 🚀 Despliegue

### Desarrollo Local

#### Backend
```bash
cd backend
mvn spring-boot:run
# Servidor: http://localhost:8080
# Swagger: http://localhost:8080/swagger-ui.html
```

#### Frontend
```bash
cd frontend/actriz-frontend
npm install
npm start
# Aplicación: http://localhost:4200
```

#### Base de datos local con Docker
```bash
docker-compose up -d postgres
```

### Producción en Railway

#### Estructura de servicios
1. **Backend Service**
   - Dockerfile: `Dockerfile.backend`
   - Puerto: 8080
   - Variables de entorno configuradas

2. **Frontend Service**
   - Dockerfile: `frontend/actriz-frontend/Dockerfile`
   - Puerto: 80 (Nginx)
   - Proxy configurado hacia backend

3. **PostgreSQL Database**
   - Servicio gestionado de Railway
   - Variables automáticas conectadas al backend

#### Proceso de Deploy
1. Push a GitHub → Railway detecta cambios automáticamente
2. Build automático usando Dockerfiles
3. Deploy automático con zero-downtime

## 🖼️ Gestión de Imágenes con Cloudinary

### Integración
- **Servicio**: `CloudinaryService` - Maneja subida/eliminación
- **Adaptador**: `CloudinaryFileStorageAdapter` - Implementa `FileStoragePort`
- **Organización**: Imágenes organizadas en carpetas por tipo
  - `demo-ia/actrices/` - Imágenes de actrices
  - `demo-ia/escenas/` - Imágenes de escenas

### Capacidad del Plan Gratuito
- **Almacenamiento**: 25 GB
- **Transferencia**: 25 GB/mes
- **Transformaciones**: 25,000/mes
- **Tamaño máximo por archivo**: 100 MB

### Ventajas
- ✅ Persistencia entre deploys
- ✅ CDN global automático
- ✅ Transformaciones en tiempo real
- ✅ Optimización automática
- ✅ Backup automático

### URLs de ejemplo
```
# Imagen original
https://res.cloudinary.com/dfayxggsg/image/upload/v1753906457/demo-ia/actrices/u8f53v9ugmk0bcvvgaa4.jpg

# Redimensionada
https://res.cloudinary.com/dfayxggsg/image/upload/w_400,h_400,c_fill/v1753906457/demo-ia/actrices/u8f53v9ugmk0bcvvgaa4.jpg

# Optimizada automáticamente
https://res.cloudinary.com/dfayxggsg/image/upload/f_auto,q_auto/v1753906457/demo-ia/actrices/u8f53v9ugmk0bcvvgaa4.jpg
```

## 📡 API REST

### Endpoints Principales

#### Actrices
```
GET    /api/actrices              # Listar todas
GET    /api/actrices/{id}         # Obtener por ID
POST   /api/actrices/con-imagen   # Crear con imagen
PUT    /api/actrices/{id}         # Actualizar
DELETE /api/actrices/{id}         # Eliminar
```

#### Escenas
```
GET    /api/escenas               # Listar todas
GET    /api/escenas/{id}          # Obtener por ID
POST   /api/escenas/con-imagen    # Crear con imagen
PUT    /api/escenas/{id}          # Actualizar
DELETE /api/escenas/{id}          # Eliminar
```

#### Países
```
GET    /api/paises                # Listar todos
POST   /api/paises                # Crear nuevo
```

#### Tipos de Escena
```
GET    /api/tipo-escenas          # Listar todos
POST   /api/tipo-escenas          # Crear nuevo
```

### Formato de peticiones con imagen

#### Crear Actriz con Imagen
```bash
curl -X POST http://localhost:8080/api/actrices/con-imagen \
  -F 'actriz={"nombre": "Riley", "fechaNacimiento": "1988-11-06", "calificacion": 4.8, "paisId": 1}' \
  -F 'imagen=@imagen.jpg'
```

#### Crear Escena con Imagen
```bash
curl -X POST http://localhost:8080/api/escenas/con-imagen \
  -F 'escena={"descripcion": "Escena de prueba", "fechaGrabacion": "2025-01-15", "duracion": "PT30M", "ubicacion": "Madrid", "tipoEscenaId": 1, "actrices": [{"actrizId": 1, "papel": "Protagonista"}]}' \
  -F 'imagen=@escena.jpg'
```

## 🔒 Configuración CORS

### Desarrollo
```java
@CrossOrigin(origins = "http://localhost:4200")
```

### Producción
```properties
app.cors.allowed-origins=https://tu-frontend-url.railway.app
```

## 📋 Validaciones

### Backend
- **Imágenes**: Tipo, tamaño máximo (5MB)
- **Datos**: Validaciones con `@Valid` y Bean Validation
- **Calificación**: Rango 1-5

### Frontend
- **Formularios**: Validación en tiempo real
- **Archivos**: Validación de tipo y tamaño
- **Campos obligatorios**: Marcados claramente

## 🎨 Frontend - Componentes Principales

### Estructura de Componentes
```
components/
├── actriz-list/     # Lista de actrices
├── actriz-form/     # Formulario de actriz
├── actriz-detail/   # Detalle de actriz
├── escena-list/     # Lista de escenas
├── escena-form/     # Formulario de escena
└── escena-detail/   # Detalle de escena
```

### Servicios
- **ActrizService**: Gestión de actrices
- **EscenaService**: Gestión de escenas
- **TipoEscenaService**: Gestión de tipos de escena

### Navegación
- Menú principal con dropdowns
- Rutas protegidas y navegación fluida
- Responsive design

## 🔍 Documentación API (Swagger)

### Acceso
- **Desarrollo**: http://localhost:8080/swagger-ui.html
- **Producción**: https://demo-ia-production.up.railway.app/swagger-ui.html

### Características
- Documentación automática de todos los endpoints
- Prueba de APIs directamente desde la interfaz
- Esquemas de datos detallados
- Ejemplos de peticiones y respuestas

## 🛠️ Herramientas de Desarrollo

### MapStruct
Mapeo automático entre DTOs y entidades:
```java
@Mapper(componentModel = "spring")
public interface ActrizMapper {
    ActrizResponseDto toResponseDto(Actriz actriz);
    Actriz toEntity(ActrizRequestDto dto);
}
```

### Docker
- **Desarrollo**: `docker-compose.yml` para PostgreSQL
- **Producción**: Dockerfiles individuales para cada servicio

### Profiles de Spring
- **dev**: H2, logs detallados
- **prod**: PostgreSQL, logs optimizados

## 🚨 Solución de Problemas Comunes

### CORS en Producción
```java
@CrossOrigin(originPatterns = "${app.cors.allowed-origins}")
```

### Variables de Entorno no Reconocidas
Verificar que estén definidas tanto en `application.properties` como en `application-prod.properties`

### Imágenes no Persisten
Usar Cloudinary en lugar de almacenamiento local para persistencia entre deploys

### Error 502 en Frontend
Verificar configuración de proxy en `nginx.conf`

## 📈 Mejoras Futuras

### Funcionalidades
- [ ] Autenticación y autorización
- [ ] Búsqueda avanzada y filtros
- [ ] Paginación en listados
- [ ] Cache de imágenes
- [ ] Notificaciones en tiempo real

### Técnicas
- [ ] Tests unitarios y de integración
- [ ] CI/CD pipeline completo
- [ ] Monitoreo y logging avanzado
- [ ] Optimización de performance
- [ ] PWA (Progressive Web App)

## 📞 URLs del Proyecto

### Producción
- **Frontend**: https://loving-exploration-production.up.railway.app/
- **Backend API**: https://demo-ia-production.up.railway.app/
- **Swagger**: https://demo-ia-production.up.railway.app/swagger-ui.html

### Desarrollo
- **Frontend**: http://localhost:4200
- **Backend API**: http://localhost:8080
- **Swagger**: http://localhost:8080/swagger-ui.html

## 👥 Contribución

### Estructura de commits
```
feat: nueva funcionalidad
fix: corrección de errores
docs: documentación
style: formato de código
refactor: refactorización
test: tests
```

### Flujo de desarrollo
1. Desarrollo local con H2
2. Pruebas con PostgreSQL local
3. Push a GitHub
4. Deploy automático en Railway

---

**Proyecto desarrollado con arquitectura hexagonal, buenas prácticas y tecnologías modernas.**
