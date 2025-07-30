# 🔧 Guía Técnica para Desarrolladores

## 🚀 Setup Rápido del Proyecto

### Prerrequisitos
- Java 11+
- Node.js 18+
- Docker Desktop
- Git
- IDE (IntelliJ IDEA recomendado para backend, VS Code para frontend)

### Instalación Paso a Paso

#### 1. Clonar el repositorio
```bash
git clone https://github.com/joseflores316/demo-IA.git
cd demo-IA
```

#### 2. Configurar Backend
```bash
cd backend
# Compilar proyecto
mvn clean package -DskipTests

# Ejecutar con H2 (desarrollo)
mvn spring-boot:run

# O ejecutar con PostgreSQL local
docker-compose up -d postgres
mvn spring-boot:run -Dspring.profiles.active=prod
```

#### 3. Configurar Frontend
```bash
cd frontend/actriz-frontend
npm install
npm start
```

#### 4. Acceder a la aplicación
- **Frontend**: http://localhost:4200
- **Backend API**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **H2 Console**: http://localhost:8080/h2-console

## 🏗️ Arquitectura Hexagonal Detallada

### Capas y Responsabilidades

#### Domain Layer (Núcleo)
```
domain/
├── model/           # Entidades de negocio
│   ├── Actriz.java
│   ├── Escena.java
│   └── Pais.java
├── ports/
│   ├── in/          # Casos de uso (interfaces)
│   │   ├── ActrizUseCase.java
│   │   └── EscenaUseCase.java
│   └── out/         # Puertos de salida (interfaces)
│       ├── ActrizRepositoryPort.java
│       ├── FileStoragePort.java
│       └── EscenaRepositoryPort.java
└── services/        # Implementación casos de uso
    ├── ActrizService.java
    └── EscenaService.java
```

#### Infrastructure Layer (Adaptadores)
```
infrastructure/
├── web/             # Adaptadores de entrada
│   ├── controllers/
│   └── dto/
├── persistence/     # Adaptadores de salida BD
│   ├── entities/
│   ├── repositories/
│   └── adapters/
└── storage/         # Adaptadores almacenamiento
    ├── LocalFileStorageAdapter.java
    └── CloudinaryFileStorageAdapter.java
```

### Flujo de Datos
```
HTTP Request → Controller → UseCase → Domain Service → Repository Port → JPA Repository → Database
                    ↓
HTTP Response ← DTO ← Mapper ← Domain Entity ← Repository Adapter ← JPA Entity ← Database
```

## 📊 Base de Datos

### Configuración H2 (Desarrollo)
```properties
# application.properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.username=sa
spring.datasource.password=password
spring.h2.console.enabled=true
```

### Configuración PostgreSQL (Producción)
```properties
# application-prod.properties
spring.datasource.url=${JDBC_DATABASE_URL}
spring.datasource.username=${PGUSER}
spring.datasource.password=${PGPASSWORD}
spring.jpa.hibernate.ddl-auto=update
```

### Scripts SQL Iniciales
```sql
-- Datos de prueba insertados automáticamente al arrancar
INSERT INTO pais (nombre, codigo_iso) VALUES ('España', 'ES');
INSERT INTO pais (nombre, codigo_iso) VALUES ('Estados Unidos', 'US');
INSERT INTO tipo_escena (nombre) VALUES ('Acción');
INSERT INTO tipo_escena (nombre) VALUES ('Drama');
```

## 🔌 Integración con Cloudinary

### Configuración del Servicio
```java
@Service
public class CloudinaryService {
    
    @Value("${cloudinary.cloud-name}")
    private String cloudName;
    
    @PostConstruct
    public void init() {
        cloudinary = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", cloudName,
            "api_key", apiKey,
            "api_secret", apiSecret
        ));
    }
    
    public String uploadImage(MultipartFile file, String folder) throws IOException {
        Map<String, Object> uploadResult = cloudinary.uploader().upload(
            file.getBytes(),
            ObjectUtils.asMap("folder", folder, "resource_type", "image")
        );
        return (String) uploadResult.get("secure_url");
    }
}
```

### Implementación del Adaptador
```java
@Service
@Primary // Usa Cloudinary en lugar del almacenamiento local
public class CloudinaryFileStorageAdapter implements FileStoragePort {
    
    @Override
    public String guardarImagen(MultipartFile file, String categoria) {
        // Validar archivo
        validarArchivo(file);
        
        // Subir a Cloudinary en carpeta específica
        String folder = "demo-ia/" + categoria;
        return cloudinaryService.uploadImage(file, folder);
    }
}
```

## 🌐 Frontend Angular

### Estructura de Servicios
```typescript
// actriz.service.ts
@Injectable({
  providedIn: 'root'
})
export class ActrizService {
  private apiUrl = 'http://localhost:8080/api/actrices';

  crearActrizConImagen(actriz: any, imagen: File): Observable<Actriz> {
    const formData = new FormData();
    formData.append('actriz', JSON.stringify(actriz));
    formData.append('imagen', imagen);
    
    return this.http.post<Actriz>(`${this.apiUrl}/con-imagen`, formData);
  }
}
```

### Configuración de Proxy
```json
// proxy.conf.json
{
  "/api/*": {
    "target": "http://localhost:8080",
    "secure": true,
    "changeOrigin": true,
    "logLevel": "debug"
  }
}
```

### Manejo de Imágenes
```typescript
// Método para mostrar imágenes (funciona con URLs locales y Cloudinary)
getImageUrl(imagenUrl: string | undefined): string {
  if (!imagenUrl) {
    return 'assets/images/no-image.svg';
  }
  // La URL ya viene completa desde el backend
  return imagenUrl;
}
```

## 🚢 Deploy en Railway

### Configuración de Servicios

#### Backend Service
```dockerfile
# Dockerfile.backend
FROM openjdk:11-jre-alpine
WORKDIR /app
COPY backend/target/*.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
```

```json
// backend/railway.json
{
  "build": {
    "builder": "DOCKERFILE",
    "dockerfilePath": "Dockerfile.backend"
  },
  "deploy": {
    "restartPolicyType": "ON_FAILURE",
    "restartPolicyMaxRetries": 10
  }
}
```

#### Frontend Service
```dockerfile
# frontend/actriz-frontend/Dockerfile
FROM node:18-alpine as build
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
RUN npm run build

FROM nginx:alpine
COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /app/dist/actriz-frontend /usr/share/nginx/html
EXPOSE 80
```

```nginx
# nginx.conf
server {
    listen 80;
    server_name localhost;
    root /usr/share/nginx/html;
    index index.html;

    # Proxy para API
    location /api/ {
        proxy_pass https://demo-ia-production.up.railway.app;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }

    # SPA routing
    location / {
        try_files $uri $uri/ /index.html;
    }
}
```

## 🧪 Testing

### Backend - Tests Unitarios
```java
@ExtendWith(MockitoExtension.class)
class ActrizServiceTest {
    
    @Mock
    private ActrizRepositoryPort actrizRepository;
    
    @Mock
    private FileStoragePort fileStorage;
    
    @InjectMocks
    private ActrizService actrizService;
    
    @Test
    void crearActriz_DeberiaGuardarActrizConImagen() {
        // Given
        ActrizRequestDto request = new ActrizRequestDto();
        request.setNombre("Test Actriz");
        
        // When & Then
        assertThat(actrizService.crearActriz(request)).isNotNull();
    }
}
```

### Frontend - Tests de Componentes
```typescript
describe('ActrizListComponent', () => {
  let component: ActrizListComponent;
  let fixture: ComponentFixture<ActrizListComponent>;
  let actrizService: jasmine.SpyObj<ActrizService>;

  beforeEach(() => {
    const spy = jasmine.createSpyObj('ActrizService', ['getActrices']);
    
    TestBed.configureTestingModule({
      imports: [ActrizListComponent],
      providers: [
        { provide: ActrizService, useValue: spy }
      ]
    });
    
    actrizService = TestBed.inject(ActrizService) as jasmine.SpyObj<ActrizService>;
  });

  it('should load actrices on init', () => {
    actrizService.getActrices.and.returnValue(of([]));
    component.ngOnInit();
    expect(actrizService.getActrices).toHaveBeenCalled();
  });
});
```

## 🔍 Debugging y Logs

### Backend - Configuración de Logs
```properties
# application.properties (desarrollo)
logging.level.com.jose.demoia=DEBUG
logging.level.org.springframework.web=DEBUG
spring.jpa.show-sql=true

# application-prod.properties (producción)
logging.level.com.jose.demoia=INFO
logging.level.org.springframework.web=WARN
spring.jpa.show-sql=false
```

### Frontend - Console Logs
```typescript
// En desarrollo, habilitar logs detallados
console.log('Datos de actriz a enviar:', actrizData);
console.log('Archivo seleccionado:', selectedFile);
```

## ⚡ Optimizaciones de Performance

### Backend
```java
// Lazy loading para relaciones
@OneToMany(fetch = FetchType.LAZY, mappedBy = "actriz")
private List<ActrizEscena> escenas;

// Cache de consultas frecuentes
@Cacheable("paises")
public List<Pais> findAll() {
    return paisRepository.findAll();
}
```

### Frontend
```typescript
// Lazy loading de módulos
const routes: Routes = [
  {
    path: 'actrices',
    loadComponent: () => import('./components/actriz-list/actriz-list').then(m => m.ActrizListComponent)
  }
];

// OnPush change detection para mejor performance
@Component({
  changeDetection: ChangeDetectionStrategy.OnPush
})
```

### Cloudinary - Optimización de Imágenes
```typescript
// URLs optimizadas automáticamente
const optimizedUrl = `${baseUrl}/f_auto,q_auto,w_400,h_400,c_fill/${publicId}`;
```

## 🛡️ Seguridad

### CORS Configuración
```java
@Configuration
public class CorsConfig {
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList(allowedOrigins.split(",")));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
```

### Validación de Archivos
```java
private void validarArchivo(MultipartFile file) {
    if (file.getSize() > TAMAÑO_MAXIMO) {
        throw new IllegalArgumentException("Archivo demasiado grande");
    }
    
    if (!TIPOS_PERMITIDOS.contains(file.getContentType())) {
        throw new IllegalArgumentException("Tipo de archivo no permitido");
    }
}
```

## 📈 Monitoreo

### Actuator Endpoints
```properties
# application-prod.properties
management.endpoints.web.exposure.include=health,info,metrics
management.endpoint.health.show-details=when-authorized
```

### Health Checks
```java
@Component
public class CloudinaryHealthIndicator implements HealthIndicator {
    
    @Override
    public Health health() {
        try {
            // Verificar conectividad con Cloudinary
            cloudinary.api().ping();
            return Health.up().withDetail("cloudinary", "Connected").build();
        } catch (Exception e) {
            return Health.down().withDetail("cloudinary", "Disconnected").build();
        }
    }
}
```

## 🔧 Scripts Útiles

### Desarrollo
```bash
# Limpiar y compilar backend
cd backend && mvn clean package -DskipTests

# Ejecutar con perfil específico
mvn spring-boot:run -Dspring.profiles.active=prod

# Ejecutar frontend con proxy
cd frontend/actriz-frontend && npm start

# Reiniciar base de datos local
docker-compose down && docker-compose up -d postgres
```

### Producción
```bash
# Deploy manual en Railway
railway deploy

# Ver logs en tiempo real
railway logs --follow

# Conectar a base de datos
railway connect postgres
```

---

**Esta guía técnica complementa la documentación principal y está dirigida específicamente a desarrolladores que trabajen en el proyecto.**
