# Fit-me API Documentation

Esta carpeta contiene la documentación completa de la API de Fit-me en formato OpenAPI 3.0.3.

## Estructura de Archivos

La documentación está organizada en archivos separados por módulo para facilitar el mantenimiento:

### Archivos Principales

- **`fit-me-api.yaml`** - Archivo principal que contiene la estructura general de la API y referencias a todos los módulos
- **`athlete.yaml`** - Endpoints relacionados con datos de atletas
- **`communication.yaml`** - Endpoints de comunicación y mensajería
- **`diet.yaml`** - Endpoints de gestión de dietas
- **`resources.yaml`** - Endpoints de recursos del sistema
- **`statistics.yaml`** - Endpoints de estadísticas y analíticas
- **`trainer.yaml`** - Endpoints de autenticación y dashboard de entrenadores
- **`upload.yaml`** - Endpoints de subida de archivos
- **`user.yaml`** - Endpoints de gestión de usuarios
- **`workout.yaml`** - Endpoints de gestión de entrenamientos

## Cómo Usar la Documentación

### 1. Visualizar con Swagger UI

Para visualizar la documentación de forma interactiva, puedes usar cualquiera de estas opciones:

#### Opción A: Swagger Editor Online
1. Ve a [editor.swagger.io](https://editor.swagger.io/)
2. Copia y pega el contenido de `fit-me-api.yaml`
3. La documentación se renderizará automáticamente

#### Opción B: Swagger UI Local
```bash
# Instalar swagger-ui-serve
npm install -g swagger-ui-serve

# Servir la documentación
swagger-ui-serve fit-me-api.yaml
```

#### Opción C: VS Code con extensión OpenAPI
1. Instala la extensión "OpenAPI (Swagger) Editor" en VS Code
2. Abre cualquier archivo .yaml
3. Usa Ctrl+Shift+P > "OpenAPI: Preview"

### 2. Generar Clientes

Puedes generar clientes para diferentes lenguajes usando OpenAPI Generator:

```bash
# Instalar OpenAPI Generator
npm install -g @openapitools/openapi-generator-cli

# Generar cliente JavaScript
openapi-generator-cli generate -i fit-me-api.yaml -g javascript -o ./clients/javascript

# Generar cliente TypeScript
openapi-generator-cli generate -i fit-me-api.yaml -g typescript-axios -o ./clients/typescript

# Generar cliente Python
openapi-generator-cli generate -i fit-me-api.yaml -g python -o ./clients/python
```

### 3. Validar la Documentación

Para validar que la documentación esté correctamente formateada:

```bash
# Usando swagger-codegen
swagger-codegen validate -i fit-me-api.yaml

# Usando spectral (recomendado)
npm install -g @stoplight/spectral-cli
spectral lint fit-me-api.yaml
```

## Módulos de la API

### 🏃 Athlete Data
Gestión de datos de atletas incluyendo historial de entrenamientos, dietas y sesiones programadas.

**Endpoints principales:**
- `GET /athlete/data/workouthistory/{athlete_id}` - Historial de entrenamientos
- `GET /athlete/data/diethitory/{athlete_id}` - Historial de dietas
- `GET /athlete/data/sessions/{athlete_id}` - Sesiones próximas

### 💬 Communication
Sistema de mensajería entre entrenadores y atletas.

**Endpoints principales:**
- `GET /comunication/conversations/{conversationId}` - Detalles de conversación
- `POST /comunication/messages/{messageId}/read` - Marcar mensaje como leído

### 🥗 Diet Management
Gestión completa de planes dietéticos y plantillas.

**Endpoints principales:**
- `GET /diet/plans/{trainer_id}` - Planes de dieta del entrenador
- `GET /diet/templates/{trainer_id}` - Plantillas de dieta
- `POST /diet/create/{trainer_id}` - Crear nuevo plan de dieta

### 📊 Statistics
Análisis y estadísticas detalladas de rendimiento.

**Endpoints principales:**
- `GET /statistics/athlete` - Estadísticas comprensivas del atleta
- `GET /statistics/strength/{athleteId}` - Estadísticas de fuerza
- `GET /statistics/endurance/{athleteId}` - Estadísticas de resistencia

### 👨‍💼 Trainer Management
Autenticación y gestión del dashboard de entrenadores.

**Endpoints principales:**
- `GET /trainer/{userIdentification}/{userPassword}` - Autenticación
- `GET /data/{trainer_id}/dashboardChartInfo` - Datos del dashboard
- `GET /{trainer_id}/comunication` - Conversaciones del entrenador

### 🏋️ Workout Management
Gestión completa de entrenamientos y planes de ejercicio.

**Endpoints principales:**
- `GET /workout/{trainer_id}` - Entrenamientos del entrenador
- `POST /workout/{trainer_id}` - Crear nuevo entrenamiento
- `GET /workout/plans/{trainer_id}` - Planes de entrenamiento

## Esquemas de Autenticación

La API soporta dos métodos de autenticación:

1. **Bearer Token (JWT)** - Para autenticación de usuarios
2. **API Key** - Para acceso de aplicaciones

```yaml
# Ejemplo de uso con Bearer Token
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...

# Ejemplo de uso con API Key
X-API-Key: your-api-key-here
```

## Códigos de Estado HTTP

La API utiliza los siguientes códigos de estado:

- **200** - Éxito
- **201** - Recurso creado exitosamente
- **400** - Solicitud incorrecta
- **401** - No autorizado
- **404** - Recurso no encontrado
- **417** - Expectativa fallida
- **500** - Error interno del servidor

## Formato de Respuestas

### Respuestas Exitosas
```json
{
  "message": "Operación exitosa",
  "data": { ... },
  "timestamp": "2025-06-07T12:00:00Z"
}
```

### Respuestas de Error
```json
{
  "error": "Descripción del error",
  "code": 400,
  "timestamp": "2025-06-07T12:00:00Z",
  "path": "/api/endpoint"
}
```

## Contribución

Para actualizar la documentación:

1. Modifica el archivo específico del módulo correspondiente
2. Asegúrate de que la sintaxis OpenAPI sea válida
3. Actualiza el archivo principal `fit-me-api.yaml` si es necesario
4. Valida los cambios usando las herramientas mencionadas arriba

## Soporte

Para preguntas sobre la API, contacta:
- **Email**: contact@gaspardev.es
- **Documentación**: https://docs.fit-me.com
