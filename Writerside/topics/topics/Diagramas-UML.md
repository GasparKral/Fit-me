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
        }

        package "Acciones comunes" {
            usecase "Acceso a entrenamientos"
        }

        package "Acciones del entrenador" {
            usecase "Adaptar rutinas y dietas" 
            usecase "Creación de estadísticas" 
            usecase "Crear sets de ejercicios" 
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
       e --> (Crear dieta, sumplmentación y guardarlas)
       e --> (Asignar rutina y dieta a los deportistas)
       e --> (Ver estadísticas relacionadas con los deportistas)
       e --> (Ver estadísticas del usuario)
       e --> (Control del perfil personal)
       e --> (Agendar entrenamientos)
       e --> (Usar el chat con los deportistas)

    @enduml
</code-block>

## Diagrama de Caso de uso Aplicación Móvil

<code-block lang="plantuml">
    @startuml
        
        :Deportista: as d
        
        d --> (Ver sus estadisticas)
        d --> (Revisar sus entrenamientos y agregar notas)
        d --> (Revisar sus dietas y sumplmentos)
        d --> (Agendar entrenamientos con el entrenador)
        d --> (Usar el chat con el entrenador)
        d --> (Hacer uso de las herramientas de entrenamiento)

    @enduml
</code-block>

## Modelo Entidad-Relación

<code-block lang="plantuml">
    @startuml

        entity User {}
        
        entity Trainer {}
        
        entity Sportsman {}
        
        entity Diet {}
        
        entity Workouts {}
        
        entity Suplements {}
        
        diamond Assignment
        

        diamond Assignet
        

        User --> Trainer : "is a"
        User --> Sportsman : "is a"
        
        
        Trainer "1" -- "n" Assignment
        Assignment -- Diet
        Assignment -- Workouts
        Assignment -- Suplements

        Sportsman "1" -- "1" Assignet : "Assigned to"
        note right: Tiene asignado
        Assignet "1" -- "n" Diet : "Sigue"
        Assignet "1" -- "n" Workouts : "Executes"
        Assignet "1" -- "n" Suplements : "Uses"

@enduml
</code-block>

## Usuarios

El usuario será uno de los dos tipos como ya se ha explicado, podrá ser o entrenador o deportista, para construir sus respectivas clases
se hará por composición, cada uno guardará una referencia con los la clase <code>User</code> que guarda los datos comunes
y luego los datos específicos de cada uno.

<code-block lang="plantuml">
@startuml
    
    class User {
       - id: Int
       + fullName: String
       + password: String
       + email: String
       - creationDate: Date
       - lastModification: Date
       + userImage: Resource
    }

    class Trainer {
       + userData: User
       + specialization: String
       + experienceYears: Int
       + bio: String
       + socialLinks: Map{String,String}
       + availability: String
       + raiting: Double
    }

    class Sportsman {
       + userData: User
       + trainer: Trainer
       + workouts: Map{WeekDay, Workout}
       + age: Int
       + weight: Doble
       + height: Double
       + sex: Boolean
       + allergies: List{String}

        +calcularIMC():Double
    }

    User --* Trainer::userData
    User --* Sportsman::userData
    Trainer "1" o-- Sportsman::trainer : " is binded"
    

@enduml
</code-block>

## Información asociada a los deportistas

<code-block lang="PlantUML">
@startuml

    class Sportsman {
       + userData: User
       + trainer: Trainer
       + workouts: Map{Weekday,Workout}
       + age: Int
       + weight: Doble
       + height: Double
       + sex: Boolean
       + allergies: List{String}

        +calcularIMC():Double
    }

    enum Weekday{
        MONDAY
        THURSDAY
        WEDNESDAY
        TUESDAY
        FRIDAY
        SATURDAY
        SUNDAY
    }

    Sportsman::workouts .. Weekday : " Map key"

    class Workout{
        + notes: List{Note}
        + exercises: List{Exercise}
    }
    
    Sportsman::workouts -- Workout : " Map values"

    class Exercise {
        + name: string
        + bodyPart: bodyPart
        + description: String
        - author: Trainer?
        + video: Resource?
        + notes: List{Note}
        + optionalExercises: List{Exercise}
    }

    Workout::exercises -- Exercise

@enduml
</code-block>
