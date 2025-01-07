# Diagramas UML

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

    enum BodyPart {
        Leg
        Arm
        Shoulder
        Abs
        Back
        Chest
    }

    class Exercise {
        + name: string
        + bodyPart: bodyPart
        + description: String
        - author: Trainer?
        + video: Resource?
        + notes: List{Note}
        + optionalExercises: List{Exercise}
    }

    Exercise::bodyPart .. BodyPart
    Workout::exercises -- Exercise

@enduml
</code-block>
