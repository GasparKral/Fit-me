# Documentación API servidor (OpenAPI)

Esta sección contiene la documentación completa de todas las APIs del servidor FitMe, organizadas por funcionalidad.

## APIs Principales

### API de Usuarios y Utilidades
<api-doc openapi-path="../../openApi/UtilsApi.yaml"/>

### API de Entrenadores
<api-doc openapi-path="../../openApi/TrainerApi.yaml"/>

### API de Atletas
<api-doc openapi-path="../../openApi/AthleteApi.yaml"/>

### API de Entrenamientos
<api-doc openapi-path="../../openApi/WorkoutApi.yaml"/>

### API de Dietas
<api-doc openapi-path="../../openApi/DietApi.yaml"/>

### API de Comunicación
<api-doc openapi-path="../../openApi/CommunicationApi.yaml"/>

### API de WebSockets
<api-doc openapi-path="../../openApi/WebSocketApi.yaml"/>

### API de Recursos (Upload)
<api-doc openapi-path="../../openApi/ResourceApi.yaml"/>

---

## Resumen de Endpoints

### Autenticación y Usuarios
- `GET /ping` - Verificación de conectividad
- `GET /users/key_gen` - Generar clave de registro
- `POST /upload` - Subir archivos
- `GET /resources` - Obtener recursos del sistema

### Entrenadores
- `GET /user/trainer` - Autenticación de entrenador
- `GET /user/trainer/data/*` - Datos del dashboard y estadísticas

### Atletas  
- `GET /users/athlete/data/workouthistory` - Historial de entrenamientos
- `GET /users/athlete/data/diethitory` - Historial de dietas
- `GET /users/athlete/data/sessions` - Sesiones próximas

### Entrenamientos
- `GET /workouts` - Listar entrenamientos
- `GET /workouts/plans` - Planes de entrenamiento
- `GET /workouts/templates` - Plantillas de entrenamiento
- `POST /workouts/create` - Crear nuevo entrenamiento

### Dietas
- `GET /diets/plans` - Planes de dieta
- `GET /diets/templates` - Plantillas de dieta
- `POST /diets/create` - Crear nueva dieta

### Comunicación
- `GET /conversations/{conversationId}` - Obtener conversación
- `POST /messages/{messageId}/read` - Marcar mensaje como leído

### WebSockets 1
- `WS /chat/{userId}/{conversationId}` - Chat en tiempo real
- `WS /status` - WebSocket de prueba

---

## Notas de Implementación

### Autenticación
El sistema utiliza autenticación basada en credenciales (usuario/contraseña) para entrenadores y tokens JWT para comunicación WebSocket.

### Base de Datos
- **PostgreSQL** como base de datos principal
- **Exposed ORM** para manejo de datos
- Estructura normalizada con tablas relacionales

### WebSockets
- Comunicación bidireccional en tiempo real
- Estados de mensaje: SENT, DELIVERED, READ
- Gestión automática de sesiones activas

### Upload de Archivos
- Carpeta de destino: `./bucket`
- Tipos soportados: Imágenes, videos
- Nombres únicos basados en timestamp

### Códigos de Estado HTTP
- `200` - Operación exitosa
- `400` - Parámetros faltantes o inválidos
- `404` - Recurso no encontrado
- `417` - Error de expectativa fallida
- `500` - Error interno del servidor
