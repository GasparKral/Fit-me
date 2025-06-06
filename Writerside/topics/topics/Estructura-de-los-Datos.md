# Estructura de los Datos

## Clases principales

La aplicación se organiza bajo **Clean Architecture** y se compone de las siguientes clases principales en el módulo compartido:

### Entidades de Dominio

#### Usuarios
- **User**: Información común entre usuarios (entrenadores y deportistas)
- **Trainer**: Datos específicos de entrenadores, certificaciones, experiencia, disponibilidad
- **Athlete**: Datos específicos de deportistas, medidas, alergias, dietas y rutinas asignadas
- **UserStatus**: Estado de conexión y última actividad del usuario

#### Entrenamientos
- **Workout**: Rutina de entrenamiento con ejercicios organizados por días
- **WorkoutPlan**: Plan de entrenamiento con información de asignación
- **WorkoutTemplate**: Plantilla reutilizable para crear entrenamientos
- **Exercise**: Ejercicio individual con descripción y parte del cuerpo
- **WorkoutExercise**: Ejercicio específico dentro de una rutina (sets, reps, etc.)
- **CompletionWorkoutStatistic**: Estadística de entrenamiento completado

#### Nutrición
- **Diet**: Plan nutricional con platos organizados por días
- **DietPlan**: Plan de dieta con información de asignación  
- **DietTemplate**: Plantilla reutilizable para crear dietas
- **Dish**: Plato individual con nombre
- **DietDish**: Plato específico dentro de una dieta (cantidad, tipo de comida)
- **CompletionDietStatistics**: Estadística de dieta completada

#### Comunicación
- **Conversation**: Conversación entre entrenador y deportista
- **Message**: Mensaje individual en una conversación
- **Session**: Sesión de entrenamiento programada

#### Información Adicional
- **Note**: Notas que pueden asociarse a diferentes entidades
- **Measurements**: Medidas corporales del deportista
- **Allergy**: Alergia específica
- **Certification**: Certificación del entrenador
- **TimeSlot**: Franja horaria de disponibilidad

### DTOs (Data Transfer Objects)

- **LoginCredentials**: Credenciales de acceso al sistema
- **RegisterTrainerData**: Datos para registro de nuevo entrenador
- **RegisterAthleteData**: Datos para registro de nuevo deportista
- **TrainerDashBoardInfo**: Información del dashboard del entrenador
- **DashboardChartInfo**: Datos para gráficos del dashboard
- **QrData**: Datos para generación de códigos QR

### Enumeraciones

```kotlin
enum class BodyPart { LEG, ARM, SHOULDER, CORE, BACK, CHEST, FULL_BODY }
enum class WeekDay { MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY }
enum class Difficulty { EASY, ADVANCE, HARD }
enum class WorkoutType { STRENGTH, CARDIO, FULL_BODY, UPPER_BODY, LOWER_BODY, CORE, FLEXIBILITY, ALL }
enum class DietType { WEIGHT_LOSS, MUSCLE_GAIN, BALANCED, VEGETARIAN, VEGAN, LOW_CARB, PERFORMANCE, ALL }
enum class MealType { BREAKFAST, LUNCH, DINNER, SNACK, PRE_WORKOUT, POST_WORKOUT }
enum class Sex { MALE, FEMALE }
enum class StatusState { ACTIVE, INACTIVE }
enum class MessageStatus { SENT, DELIVERED, READ, ALL }
enum class SessionType { WORKOUT, NUTRITION, ASSESSMENT, GROUP, OTHER }
```

## Casos de Uso (Use Cases)

### Casos de Uso de Lectura
- **LogInUser**: Autenticación de usuarios
- **LoadDashboardInfo**: Cargar información del dashboard
- **LoadDashboardChartInfo**: Cargar datos de gráficos
- **GetTrainerWorkoutsPlans**: Obtener planes de entrenamiento del entrenador
- **GetTrainerWorkoutsTemplates**: Obtener plantillas de entrenamiento
- **GetTrainerDietsPlans**: Obtener planes de dieta del entrenador
- **GetTrainerDietsTemplates**: Obtener plantillas de dieta
- **GetAthleteWorkoutHistory**: Obtener historial de entrenamientos del deportista
- **GetAthleteDietHistory**: Obtener historial de dietas del deportista
- **GetAthleteCommingSessions**: Obtener sesiones próximas del deportista

### Casos de Uso de Creación
- **CreateNewWorkout**: Crear nuevo entrenamiento
- **CreateNewDiet**: Crear nueva dieta
- **RegisterNewTrainer**: Registrar nuevo entrenador
- **RegisterNewSportsman**: Registrar nuevo deportista

### Casos de Uso de Eliminación
- **DeleteTrainerAccount**: Eliminar cuenta de entrenador

## Arquitectura de Repositorios

### Interfaces de Repositorio
```kotlin
interface UserRepository<T : Any, T2 : Any> {
    suspend fun logIn(expectedUser: LoginCredentials): Either<Exception, Pair<T, List<T2>>>
}

interface TrainerRepository : UserRepository<Trainer, Athlete> {
    suspend fun registerTrainer(newTrainerData: RegisterTrainerData): Either<Exception, Trainer>
    suspend fun getPendingWorkouts(trainer: Trainer): Int
    suspend fun getActivePlans(trainer: Trainer): Int
    suspend fun getDashboardChartData(trainer: Trainer): DashboardChartInfo
    suspend fun generateRegistrationKey(trainer: Trainer): Either<Exception, String>
    // ... más métodos
}

interface AthleteRepository : UserRepository<Athlete, Trainer> {
    suspend fun registerAthlete(newAthleteData: RegisterAthleteData): Either<Exception, Athlete>
    suspend fun getWorkoutsHistory(athlete: Athlete): Either<Exception, List<CompletionWorkoutStatistic>>
    suspend fun getCommingSessions(athlete: Athlete): Either<Exception, List<Session>>
}

interface WorkoutRepository {
    suspend fun getWorkouts(trainer: Trainer): Either<Exception, List<Workout>>
    suspend fun getWorkoutsPlans(trainer: Trainer): Either<Exception, List<WorkoutPlan>>
    suspend fun getWorkoutsPlanTemplates(trainer: Trainer): Either<Exception, List<WorkoutTemplate>>
    suspend fun createWorkout(workout: Workout, trainer: Trainer): Either<Exception, Boolean>
}

interface DietRepository {
    suspend fun getDiets(trainer: Trainer): Either<Exception, List<Diet>>
    suspend fun getDietsPlans(trainer: Trainer): Either<Exception, List<DietPlan>>
    suspend fun getDietsTemplates(trainer: Trainer): Either<Exception, List<DietTemplate>>
    suspend fun createDiet(diet: Diet, trainer: Trainer): Either<Exception, Boolean>
}
```

### APIs de Comunicación
```kotlin
abstract class API<ApiType> {
    protected abstract val apiPath: String
    
    abstract suspend fun post(segments: List<String>, body: Any): Either<Exception, ApiType>
    abstract suspend fun get(segments: List<String>, vararg params: Pair<String, String>): Either<Exception, ApiType>
    abstract suspend fun getList(segments: List<String>, vararg params: Pair<String, String>): Either<Exception, List<ApiType>>
    abstract suspend fun delete(segments: List<String>, vararg params: Pair<String, String>): Either.Failure<Exception>?
    abstract suspend fun patch(segments: List<String>, body: Any): Either.Failure<Exception>?
}
```

## Estructura de Datos en Kotlin

### Ejemplo de Entidad Principal - User
```kotlin
@Serializable
class User(
    val id: Int,
    var fullname: String,
    private var password: String,
    var email: String,
    var phone: String?,
    val creationDate: Instant,
    val userImageURL: String?,
    var status: UserStatus
) {
    companion object {
        val URLPATH = "/users"
    }

    fun getInitials(): String {
        return fullname
            .split(" ")
            .filter { it.isNotEmpty() }
            .take(2)
            .joinToString(separator = "") { it.first().uppercase() }
    }
}
```

### Ejemplo de Entidad de Entrenamiento - Workout
```kotlin
@Serializable
data class Workout(
    var name: String = "",
    var description: String = "",
    var difficulty: Difficulty = Difficulty.EASY,
    var duration: Duration = 0.toDuration(DurationUnit.HOURS),
    val startAt: Instant = Clock.System.now(),
    var workoutType: WorkoutType = WorkoutType.ALL,
    var exercises: Map<WeekDay, List<WorkoutExercise>> = mapOf(),
    val notes: List<Note> = listOf()
) {
    companion object {
        const val URLPATH = "/workouts"
    }

    fun getWorkoutProgression(): Double {
        val now = Clock.System.now()
        val endTime = startAt + duration
        
        if (now < startAt) return 0.0
        if (now >= endTime) return 1.0
        
        val totalDuration = endTime - startAt
        val elapsed = now - startAt
        return (elapsed.inWholeMilliseconds.toDouble() / totalDuration.inWholeMilliseconds.toDouble()) * 100
    }
}
```

## Comunicación en Tiempo Real

### WebSocket para Chat
```kotlin
class ChatWebShocket(
    private val userId: String,
    private val conversationId: Int,
    private val onMessageReceived: (Message) -> Unit,
    private val onStatusUpdate: (MessageStatus) -> Unit,
    private val onConnectionEvent: (Boolean) -> Unit,
    private val onError: (String) -> Unit = { }
) {
    suspend fun sendMessage(content: String): Boolean
    fun connect()
    fun closeConnection()
    fun isConnected(): Boolean
}
```

## Manejo de Errores con Either

```kotlin
sealed class Either<out E, out T> where E : Any, T : Any {
    data class Failure<out E : Any>(val error: E) : Either<E, Nothing>()
    data class Success<out T : Any>(val value: T) : Either<Nothing, T>()
    
    inline fun fold(onSuccess: (T) -> Unit, onFailure: (E) -> Unit = { _ -> }) {
        when (this) {
            is Failure -> onFailure(error)
            is Success -> onSuccess(value)
        }
    }
}
```

## Estructura de Carpetas Actualizada

La aplicación sigue **Clean Architecture** organizada de la siguiente manera:

```
📁 shared/src/commonMain/kotlin/es/gaspardev/
├── 📁 auxliars/                    # Utilidades auxiliares (Either, etc.)
├── 📁 core/
│   ├── 📁 domain/
│   │   ├── 📁 entities/            # Entidades de dominio
│   │   │   ├── 📁 users/           # User, Trainer, Athlete
│   │   │   ├── 📁 workouts/        # Workout, Exercise, etc.
│   │   │   ├── 📁 diets/           # Diet, Dish, etc.
│   │   │   ├── 📁 comunication/    # Message, Conversation, Session
│   │   │   └── Note.kt
│   │   ├── 📁 dtos/                # Data Transfer Objects
│   │   └── 📁 usecases/            # Casos de uso
│   │       ├── 📁 create/          # Casos de uso de creación
│   │       ├── 📁 read/            # Casos de uso de lectura
│   │       ├── 📁 update/          # Casos de uso de actualización
│   │       └── 📁 delete/          # Casos de uso de eliminación
│   └── 📁 infrastructure/
│       ├── 📁 apis/                # APIs de comunicación con servidor
│       ├── 📁 repositories/        # Implementaciones de repositorios
│       ├── 📁 shockets/            # WebSocket implementations
│       └── 📁 memo/                # Cache y memoria
├── 📁 enums/                       # Enumeraciones del dominio
├── 📁 interfaces/
│   ├── 📁 repositories/            # Interfaces de repositorios
│   ├── 📁 apis/                    # Interfaces de APIs
│   └── 📁 debug/                   # Interfaces de debug
└── 📁 utils/                       # Utilidades (constantes, validaciones, etc.)
```

### Ventajas de esta Estructura

1. **Separación de Responsabilidades**: Cada capa tiene una responsabilidad específica
2. **Inversión de Dependencias**: Las capas internas no dependen de las externas
3. **Testabilidad**: Fácil creación de tests unitarios por capa
4. **Flexibilidad**: Fácil intercambio de implementaciones
5. **Reutilización**: El módulo compartido se usa en todos los clientes
6. **Mantenibilidad**: Código organizado y fácil de mantener

### Flujo de Datos

1. **UI/Presentación** → Llama a casos de uso
2. **Casos de Uso** → Coordinan lógica de negocio usando entidades
3. **Entidades** → Encapsulan reglas de negocio
4. **Repositorios** → Abstraen acceso a datos
5. **APIs** → Implementan comunicación con servicios externos

<note title="Actualización">
    Esta estructura está actualizada según el código actual del repositorio y seguirá evolucionando.
</note>
