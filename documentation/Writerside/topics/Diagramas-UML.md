# Diagramas UML

## Usuarios

<code-block lang="plantuml">
@startuml
    
    class User {
       - id: Int,
       + fullName: String,
       + password: String,
       + email: String,
       - creationDate: Date,
       - lastModification: Date,
       + userImage: Resource,
    }

    class Trainer {
       + userData: User,
       + specialization: String,
       + experienceYears: Int,
       + bio: String,
       + socialLinks: Map,
       + availability: String,
       + raiting: Double,
    }

    class Sportsman {
       + userData: User,
       + trainer: Trainer,
       + workouts: Workouts,
       + age: Int,
       + weight: Doble,
       + height: Double,
       + sex: Boolean,
       + allergies: Array,

        +calcularIMC():Double
    }

    

    User --* Trainer::userData
    User --* Sportsman::userData
    Trainer -- Sportsman::trainer
  

@enduml

</code-block>