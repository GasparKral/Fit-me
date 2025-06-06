# Diagramas UML

## Casos de uso de la Aplicación

<code-block lang="plantuml">
    @startuml

        package Usuarios {
            :Entrenador: as e
            :Deportista: as d

            (Comunicación bidireccional) as cb
                d --> cb
                e --> cb
                cb --> d
                cb --> e

            d --> (Herramientas de entrenamiento)
        }

        (Servidor) as se
            Usuarios -- se

        package "Acciones del deportista" {
            usecase "Acceso a información de deportista" 
            usecase "Acceso a estadísticas"  
            usecase "Conectar con un entrenador" 
            usecase "Obtener historial de entrenamientos"
            usecase "Obtener sesiones próximas"
            usecase "Obtener historial de dietas"
        }

        package "Acciones comunes" {
            usecase "Acceso a entrenamientos"
            usecase "Gestión de notas"
            usecase "Chat en tiempo real"
        }

        package "Acciones del entrenador" {
            usecase "Adaptar rutinas y dietas" 
            usecase "Creación de estadísticas" 
            usecase "Crear sets de ejercicios"
            usecase "Gestionar deportistas"
            usecase "Crear plantillas de entrenamientos"
            usecase "Crear plantillas de dietas"
            usecase "Generar claves de registro"
        }

        se -->  "Acciones del deportista"
        se -->  "Acciones comunes"
        se --> "Acciones del entrenador"

    @enduml 

</code-block>

## Diagrama de Caso de uso Aplicación Desktop

<code-block lang="plantuml">
    @startuml
        
        :Entrenador: as e
        
       e --> (Crear rutina y guardar)
       e --> (Crear dieta, suplementación y guardarlas)
       e --> (Asignar rutina y dieta a los deportistas)
       e --> (Ver estadísticas relacionadas con los deportistas)
       e --> (Ver estadísticas del usuario)
       e --> (Control del perfil personal)
       e --> (Agendar entrenamientos)
       e --> (Usar el chat con los deportistas)
       e --> (Crear plantillas de ejercicios)
       e --> (Gestionar planes de entrenamiento)
       e --> (Gestionar planes nutricionales)
       e --> (Generar informes de progreso)

    @enduml
</code-block>

## Diagrama de Caso de uso Aplicación Móvil

<code-block lang="plantuml">
    @startuml
        
        :Deportista: as d
        
        d --> (Ver sus estadísticas)
        d --> (Revisar sus entrenamientos y agregar notas)
        d --> (Revisar sus dietas y suplementos)
        d --> (Agendar entrenamientos con el entrenador)
        d --> (Usar el chat con el entrenador)
        d --> (Hacer uso de las herramientas de entrenamiento)
        d --> (Registrar progreso de ejercicios)
        d --> (Ver historial de entrenamientos)
        d --> (Conectarse con entrenador vía QR)

    @enduml
</code-block>

## Modelo Entidad-Relación Actualizado

<code-block lang="plantuml">
    @startuml

        entity User {
            id: Int
            fullname: String
            password: String
            email: String
            phone: String
            creationDate: Instant
            userImageURL: String
        }
        
        entity UserStatus {
            state: StatusState
            lastTimeActive: Instant
        }
        
        entity Trainer {
            specialization: String
            yearsOfExperience: Int
            socials: Map<String,String>
            certifications: List<Certification>
            availability: Map<WeekDay,List<TimeSlot>>
        }
        
        entity Athlete {
            age: Int
            sex: Sex
            trainingSince: Instant
            allergies: List<Allergy>
            measurements: Measurements
            needAssistant: Boolean
        }
        
        entity Workout {
            name: String
            description: String
            difficulty: Difficulty
            duration: Duration
            workoutType: WorkoutType
            exercises: Map<WeekDay,List<WorkoutExercise>>
            notes: List<Note>
        }
        
        entity Diet {
            name: String
            description: String
            dietType: DietType
            duration: Duration
            dishes: Map<WeekDay,List<DietDish>>
            notes: List<Note>
        }
        
        entity Exercise {
            id: Int
            name: String
            description: String
            bodyPart: BodyPart
        }
        
        entity Note {
            id: Int
            content: String
            createdAt: Instant
        }
        
        entity Conversation {
            id: Int
            messages: List<Message>
            lastMessage: Instant
        }
        
        entity Session {
            title: String
            dateTime: Instant
            sessionType: SessionType
            expectedDuration: Duration
            notes: List<Note>
        }

        User ||--|| UserStatus
        User ||--o{ Trainer : "can be"
        User ||--o{ Athlete : "can be"
        Trainer ||--o{ Athlete : "trains"
        Trainer ||--o{ Workout : "creates"
        Trainer ||--o{ Diet : "creates"
        Athlete ||--o| Workout : "follows"
        Athlete ||--o| Diet : "follows"
        Trainer ||--o{ Conversation : "participates"
        Athlete ||--o{ Conversation : "participates"
        Trainer ||--o{ Session : "schedules"
        Athlete ||--o{ Session : "attends"
        User ||--o{ Note : "creates"

@enduml
</code-block>

## Arquitectura de Datos - Clean Architecture

<code-block lang="plantuml">
@startuml
    package "Domain Layer" {
        package "Entities" {
            class User
            class Trainer
            class Athlete
            class Workout
            class Diet
            class Exercise
            class Note
        }
        
        package "Use Cases" {
            class LogInUser
            class CreateNewWorkout
            class CreateNewDiet
            class GetTrainerWorkoutsPlans
            class GetAthleteWorkoutHistory
            class LoadDashboardInfo
        }
    }
    
    package "Infrastructure Layer" {
        package "APIs" {
            class TrainerAPI
            class AthleteAPI
            class WorkoutAPI
            class DietAPI
            class UserAPI
        }
        
        package "Repositories" {
            class TrainerRepositoryImp
            class AthleteRepositoryImp
            class WorkoutRepositoryImp
            class DietRepositoryImp
        }
    }
    
    package "Interfaces Layer" {
        package "Repositories" {
            interface TrainerRepository
            interface AthleteRepository
            interface WorkoutRepository
            interface DietRepository
        }
    }
    
    "Use Cases" --> Entities
    "Repositories" --> "APIs"
    "Repositories" ..|> "Interfaces Layer"
    "Use Cases" --> "Interfaces Layer"

@enduml
</code-block>

## Diagrama de Estados - Usuario

<code-block lang="plantuml">
@startuml
    [*] --> Desconectado
    
    Desconectado --> Autenticando : login()
    Autenticando --> Activo : credentials_valid
    Autenticando --> Desconectado : credentials_invalid
    
    Activo --> Inactivo : timeout / no_activity
    Inactivo --> Activo : user_activity
    
    Activo --> Desconectado : logout()
    Inactivo --> Desconectado : logout()
    
    note right of Activo : Usuario interactuando\ncon la aplicación
    note right of Inactivo : Usuario conectado pero\nsin actividad reciente
@enduml
</code-block>

## Diagrama de Secuencia - Chat en Tiempo Real

<code-block lang="plantuml">
@startuml
    participant "Cliente" as C
    participant "WebSocket" as WS
    participant "Servidor" as S
    participant "Base de Datos" as DB
    participant "Otro Cliente" as OC
    
    C -> WS: Conectar(/chat/{userId}/{conversationId})
    WS -> S: Establecer conexión
    S -> WS: Conexión establecida
    WS -> C: Conectado
    
    C -> WS: Enviar mensaje
    WS -> S: Procesar mensaje
    S -> DB: Guardar mensaje
    DB -> S: Mensaje guardado
    S -> WS: Reenviar a destinatario
    WS -> OC: Mensaje recibido
    OC -> WS: Confirmar lectura
    WS -> S: Actualizar estado
    S -> DB: Actualizar estado mensaje
@enduml
</code-block>

## Diagrama de Componentes - Estructura del Proyecto

<code-block lang="plantuml">
@startuml
    package "Shared Module" {
        package "Domain" {
            [Entities]
            [Use Cases]
            [DTOs]
        }
        
        package "Infrastructure" {
            [APIs]
            [Repositories]
            [WebSockets]
        }
        
        package "Utils" {
            [Constants]
            [Validations]
            [Encrypt]
        }
        
        package "Enums" {
            [BodyPart]
            [WeekDay]
            [DietType]
            [WorkoutType]
            [MessageStatus]
        }
    }
    
    package "Desktop Client" {
        [UI Components]
        [Screens]
        [Navigation]
        [Dialogs]
    }
    
    package "Mobile Client" {
        [Android App]
        [Training Tools]
        [Real-time Chat]
    }
    
    package "Server" {
        [Ktor Application]
        [Database Layer]
        [API Endpoints]
        [WebSocket Handler]
    }
    
    [Desktop Client] --> [Shared Module] : uses
    [Mobile Client] --> [Shared Module] : uses
    [Server] --> [Shared Module] : uses
    [Server] --> [PostgreSQL] : connects to
@enduml
</code-block>
