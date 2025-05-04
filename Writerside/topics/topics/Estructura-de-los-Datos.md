# Estructura de los Datos

## Clases principales

La aplicación se compondrá de las siguientes principales clases:
- **User**: Almacenará la información común entre usuarios, esto incluye los entrenadores y los deportistas.
- **Trainer**: Datos específicos de los entrenadores, incluyendo certificaciones, experiencia, disponibilidad.
- **Sportsman**: Datos específicos de los deportistas, medidas, pesos, sexo, alergias, dietas, rutinas y suplementación.
- **Exercise**: Clase que representa los ejercicios a realizar.
- **Dish**: Clase que representa los platos(pollo, pescado blanco, verduras, frutas, ...) y sus cantidades en las dietas.
- **Supplementation**: Clase que representa los distintos tipos de suplementación y cantidades a usar por los deportistas.
- **Note**: Clase que representa las notas que se puede usar en los distintos sitios de la aplicación.

## Validación de JSONS

Hay dos JSON específicos que requieren de un formato rígido; estos son las rutinas/entrenamientos (Workout::class) y 
las dietas (Diet::class) que son explícitamente un Map dentro de la aplicación

### Esquema para los platos (Dish::class)

<code-block lang="JSON">
{
    "$schema": "https://json-schema.org/draft/2020-12/schema",
    "$id": "https://example.com/fitme-exercises.schema.json",
    "title": "Fit-me Exercises Schema",
    "description": "Schema for daily dishes",
    "type": "object",
    "patternProperties": {
        "^(monday|tuesday|wednesday|thursday|friday|saturday|sunday)$": {
            "type": "array",
            "description": "Dishes for a specific day",
            "items": {
                "$ref": "#/$defs/dish"
            }
        }
    },
    "$defs": {
        "dish": {
            "type": "object",
            "description": "Dish",
            "properties": {
                "dishName": {
                    "type": "string",
                    "description": "Name of the dish"
                },
                "dishDescription": {
                    "type": "string",
                    "description": "Description of the dish"
                },
                "dishAmount": {
                    "type": "number",
                    "minimum": 0,
                    "description": "Amount of the dish in grams"
                },
                "notes": {
                    "type": "object",
                    "properties": {
                        "id": {
                            "type": "number"
                        },
                        "title": {
                            "description": "note title",
                            "type": "string"
                        },
                        "content": {
                            "description": "note content",
                            "type": "string"
                        },
                        "user": {
                            "type": "string"
                        },
                        "date": {
                            "type": "string"
                        }
                    }
                },
                "optionalDishes": {
                    "type": "array",
                    "items": {
                        "$ref": "#/$defs/dish"
                    },
                    "description": "Optional dishes related to this dish"
                }
            },
            "required": [
                "dishName",
                "dishDescription",
                "dishAmount"
            ]
        }
    }
}
</code-block>

### Esquema para los entrenamientos (Workout::class)

<code-block lang="JSON">
{
    "$schema": "https://json-schema.org/draft/2020-12/schema",
    "$id": "https://example.com/fitme-exercises.schema.json",
    "title": "Fit-me Exercises Schema",
    "description": "Schema for daily exercises in the Fit-me app",
    "type": "object",
    "patternProperties": {
        "^(monday|tuesday|wednesday|thursday|friday|saturday|sunday)$": {
            "type": "array",
            "description": "Exercises for a specific day",
            "items": {
                "$ref": "#/$defs/exercise"
            }
        }
    },
    "$defs": {
        "exercise": {
            "type": "object",
            "properties": {
                "exerciseName": {
                    "type": "string",
                    "description": "Name of the exercise"
                },
                "exerciseDescription": {
                    "type": "string",
                    "description": "Description of the exercise"
                },
                "repetitions": {
                    "type": "integer",
                    "minimum": 0,
                    "description": "Number of repetitions per set"
                },
                "sets": {
                    "type": "integer",
                    "minimum": 0,
                    "description": "Number of sets for the exercise"
                },
                "video": {
                    "type": "object",
                    "description": "metadata for a resources request to the server",
                    "properties": {
                        "id": {
                            "type": "number"
                        },
                        "title": {
                            "description": "video title",
                            "type": "string"
                        },
                        "url": {
                            "description": "url for the api to obtain the video",
                            "type": "string"
                        },
                        "thumbnail": {
                            "description": "url for the thumbnail api"
                        }
                    }
                },
                "optionalExercises": {
                    "type": "array",
                    "items": {
                        "$ref": "#/$defs/exercise"
                    },
                    "description": "Optional exercises related to this exercise"
                },
                "notes": {
                    "type": "object",
                    "properties": {
                        "id": {
                            "type": "number"
                        },
                        "title": {
                            "description": "note title",
                            "type": "string"
                        },
                        "content": {
                            "description": "note content",
                            "type": "string"
                        },
                        "user": {
                            "type": "string"
                        },
                        "date": {
                            "type": "string"
                        }
                    }
                }
            },
            "required": [
                "exerciseName",
                "repetitions",
                "sets"
            ]
        }
    }
}
</code-block>

## Modelo base de datos

<code-block lang="plantuml">
@startuml
skinparam classAttributeIconSize 0

' Enumeraciones
enum MediaType
enum BodyPart
enum WeekDay

' Tablas base
class UserTable {
+id: Int
+name: String
+password: String
+email: String
+creationTime: Timestamp
+userImage: String
}

class TrainerTable {
+id: Int
+userId: Int?
+specialization: String
+years_of_experience: Int
+bio: Text
+rating: Double
+certification: Int[] ' referencia a CertificationTable
}

class SportsmanTable {
+id: Int
+userId: Int?
+trainerId: Int?
+age: Int
+weight: Double
+height: Double
+sex: Boolean
}

class SocialLinksTable {
+id: Int
+trainerId: Int?
+socialMedia: String
+link: String
}

class CertificationTable {
+id: Int
+name: String
+issuinOrganization: String
+optainedDate: Timestamp
}

class ResourceTable {
+id: Int
+type: MediaType
+path: String
}

class NoteTable {
+id: Int
+user: Int?
+message: Text
+answer: Int?
}

' Ejercicios
class ExerciseBase {
+id: Int
+name: String
+bodyPart: BodyPart
+description: Text
+author: Int?
+video: Int?
}

class WorkoutTable {
+id: Int
+duration: String
}

class WorkoutExercises {
+id: Int
+workoutId: Int
+exerciseId: Int
+reps: Int
+sets: Int
+note: Text?
+day: WeekDay
}

class ExerciseOptionalLinks {
+workoutExerciseId: Int
+optionalExerciseId: Int
}

' Relaciones
UserTable --o{ TrainerTable : userId
UserTable --o{ SportsmanTable : userId
TrainerTable --o{ SportsmanTable : trainerId
TrainerTable --o{ SocialLinksTable : trainerId
TrainerTable --o{ ExerciseBase : author
ResourceTable --o{ ExerciseBase : video
UserTable --o{ NoteTable : user

WorkoutTable --o{ WorkoutExercises : id
ExerciseBase --o{ WorkoutExercises : exerciseId
WorkoutExercises --o{ ExerciseOptionalLinks : workoutExerciseId
ExerciseBase --o{ ExerciseOptionalLinks : optionalExerciseId

@enduml

</code-block>

## Estructura de carpetas

La aplicación sigue una estructura basada en **Clean Architecture**, una arquitectura propuesta por **Robert C. Martin (Uncle Bob)** que busca lograr un sistema altamente **mantenible, flexible y escalable** a través de un **desacoplamiento estricto** entre las distintas capas del software. Esta estructura tiene dos objetivos principales:

---

### 1. Desacoplamiento entre las capas del sistema
Las capas están organizadas de manera que dependen **únicamente de abstracciones** y no de implementaciones concretas.  
Esto significa que los cambios realizados en el **núcleo de la aplicación** (lógica de negocio) no generan una cascada de modificaciones en otras capas, como la interfaz de usuario, el acceso a datos o frameworks externos. Cada componente del sistema se manipula de forma **independiente y atómica**, facilitando la prueba unitaria y la evolución del software sin riesgo de afectar otras partes.

---

### 2. Organización por capas
Clean Architecture organiza el código en **cuatro capas principales** que forman un círculo concéntrico, donde las **dependencias fluyen siempre hacia adentro**, es decir, las capas externas dependen de las internas, pero nunca al revés. Estas capas son:

- **Entities (Entidades):**  
  Representan las **lógicas de negocio de nivel más alto**, independientes de cualquier capa externa. Son clases puras que encapsulan las reglas de negocio centrales y no dependen de detalles de implementación.

- **Use Cases (Casos de uso):**  
  Contienen la lógica específica de las operaciones que la aplicación puede realizar. Representan las **acciones** que el sistema permite ejecutar y dependen únicamente de las entidades.

- **Interface Adapters (Adaptadores de interfaz):**  
  Esta capa actúa como un **puente** entre los casos de uso y las capas externas, como la interfaz de usuario (UI) y la infraestructura de datos. Aquí se implementan transformaciones de datos, controladores, presentadores y vistas.

- **Frameworks and Drivers (Frameworks y controladores):**  
  Es la capa más externa, donde residen los **detalles de implementación**, como bases de datos, frameworks web, herramientas externas y bibliotecas específicas. Estas dependencias externas son reemplazables sin afectar las capas internas.

---

### 3. Ventajas y desventajas

- **Ventajas:**
    - Facilita la **mantenibilidad** del código y la **evolución** del sistema.
    - Permite realizar **pruebas unitarias** de forma más sencilla y aislada, al desacoplar los componentes.
    - Favorece la **escalabilidad**, facilitando la incorporación de nuevas funcionalidades sin alterar las existentes.
    - Promueve el uso de **abstracciones**, haciendo que el sistema sea independiente de tecnologías específicas o frameworks.

- **Desventajas:**
    - Su implementación inicial puede ser **más costosa en tiempo**, ya que requiere una planificación cuidadosa y la creación de un mayor número de clases.
    - Puede parecer **compleja** para proyectos pequeños o equipos no familiarizados con este tipo de arquitectura.
    - Genera **overhead de código** debido a la separación estricta entre capas y la necesidad de definir múltiples interfaces y clases.

---

### 4. Resumen práctico

La **Clean Architecture** proporciona una estructura modular, donde cada capa cumple una **responsabilidad específica** y depende solo de **abstracciones**, no de implementaciones concretas. Aunque requiere un esfuerzo inicial significativo, su adopción resulta beneficiosa a largo plazo, especialmente en proyectos grandes y con necesidades de **escalabilidad** y **mantenimiento continuo**.

<note title="Actualización">
    Este esquema será actualizado periódicamente.
</note>

```
📁 
│   ├── 📁 .fleet
│   ├── 📁 .git
│   ├── 📄 .gitignore
│   ├── 📁 .gradle
│   ├── 📁 .idea
│   ├── 📁 .kotlin
│   ├── 📄 build.gradle.kts
│   ├── 📁 composeApp
│   │   ├── 📁 build
│   │   ├── 📄 build.gradle.kts
│   │   ├── 📁 frontend
│   │   │   ├── 📁 .idea
│   │   │   ├── 📄 bun.lockb
│   │   │   ├── 📄 eslint.config.js
│   │   │   ├── 📄 index.html
│   │   │   ├── 📁 node_modules
│   │   │   ├── 📄 package-lock.json
│   │   │   ├── 📄 package.json
│   │   │   ├── 📄 postcss.config.js
│   │   │   ├── 📁 public
│   │   │   ├── 📁 src
│   │   │   │   ├── 📄 App.tsx
│   │   │   │   ├── 📁 assets
│   │   │   │   ├── 📄 index.css
│   │   │   │   ├── 📄 main.tsx
│   │   │   │   ├── 📄 vite-env.d.ts
│   │   │   ├── 📄 tailwind.config.js
│   │   │   ├── 📄 tsconfig.app.json
│   │   │   ├── 📄 tsconfig.json
│   │   │   ├── 📄 tsconfig.node.json
│   │   │   ├── 📄 vite.config.ts
│   │   ├── 📁 src
│   │   │   ├── 📁 androidMain
│   │   │   │   ├── 📄 AndroidManifest.xml
│   │   │   │   ├── 📁 kotlin
│   │   │   │   │   ├── 📁 es
│   │   │   │   │   │   ├── 📁 gaspardev
│   │   │   │   │   │   │   ├── 📄 MainActivity.kt
│   │   │   │   ├── 📁 res
│   │   │   ├── 📁 commonMain
│   │   │   │   ├── 📁 composeResources
│   │   │   │   │   ├── 📁 drawable
│   │   │   │   │   │   ├── 📄 Athlets.xml
│   │   │   │   │   │   ├── 📄 Calendar.xml
│   │   │   │   │   │   ├── 📄 compose-multiplatform.xml
│   │   │   │   │   │   ├── 📄 Home.xml
│   │   │   │   │   │   ├── 📄 Messages.xml
│   │   │   │   │   │   ├── 📄 Nutrition.xml
│   │   │   │   │   │   ├── 📄 Settings.xml
│   │   │   │   │   │   ├── 📄 Stadistics.xml
│   │   │   │   │   │   ├── 📄 Weights.xml
│   │   │   │   │   ├── 📁 values
│   │   │   │   │   │   ├── 📄 strings.xml
│   │   │   │   ├── 📁 kotlin
│   │   │   │   │   ├── 📁 es
│   │   │   │   │   │   ├── 📁 gaspardev
│   │   │   │   │   │   │   ├── 📄 App.kt
│   │   │   │   │   │   │   ├── 📁 components
│   │   │   │   │   │   │   │   ├── 📄 Badge.kt
│   │   │   ├── 📁 desktopMain
│   │   │   │   ├── 📁 kotlin
│   │   │   │   │   ├── 📁 es
│   │   │   │   │   │   ├── 📁 gaspardev
│   │   │   │   │   │   │   ├── 📁 components
│   │   │   │   │   │   │   ├── 📁 core
│   │   │   │   │   │   │   │   ├── 📁 actions
│   │   │   │   │   │   │   │   │   ├── 📄 PageAction.kt
│   │   │   │   │   │   │   │   ├── 📁 routing
│   │   │   │   │   │   │   │   │   ├── 📄 Anchor.kt
│   │   │   │   │   │   │   │   │   ├── 📄 Route.kt
│   │   │   │   │   │   │   │   │   ├── 📄 Router.kt
│   │   │   │   │   │   │   │   │   ├── 📄 TextAnchor.kt
│   │   │   │   │   │   │   ├── 📁 layout
│   │   │   │   │   │   │   │   ├── 📄 FloatingDialong.kt
│   │   │   │   │   │   │   │   ├── 📄 SideBarMenu.kt
│   │   │   │   │   │   │   │   ├── 📄 SideBarMenuItem.kt
│   │   │   │   │   │   │   ├── 📄 main.kt
│   │   │   │   │   │   │   ├── 📁 pages
│   │   │   │   │   │   │   │   ├── 📄 AthletesScreen.kt
│   │   │   │   │   │   │   │   ├── 📄 CalendarScreen.kt
│   │   │   │   │   │   │   │   ├── 📄 DashboardScreen.kt
│   │   │   │   │   │   │   │   ├── 📄 LoginScreen.kt
│   │   │   │   │   │   │   │   ├── 📄 MessagesScreen.kt
│   │   │   │   │   │   │   │   ├── 📄 NutritionScreen.kt
│   │   │   │   │   │   │   │   ├── 📄 SettingsScreen.kt
│   │   │   │   │   │   │   │   ├── 📄 StatisticsScreen.kt
│   │   │   │   │   │   │   │   ├── 📄 WorkoutsScreen.kt
│   ├── 📁 gradle
│   ├── 📄 gradle.properties
│   ├── 📄 gradlew
│   ├── 📄 gradlew.bat
│   ├── 📄 LICENSE.md
│   ├── 📄 local.properties
│   ├── 📄 README.md
│   ├── 📁 server
│   │   ├── 📁 bin
│   │   │   ├── 📁 main
│   │   │   │   ├── 📄 application.conf
│   │   │   │   ├── 📄 docker-compose.yml
│   │   │   │   ├── 📁 es
│   │   │   │   │   ├── 📁 gaspardev
│   │   │   │   │   │   ├── 📄 Application.kt
│   │   │   │   │   │   ├── 📁 db
│   │   │   │   │   │   ├── 📁 modules
│   │   │   │   │   │   │   ├── 📄 Sportsmankt.kt
│   │   │   │   │   │   │   ├── 📄 Trainer.kt
│   │   │   │   ├── 📄 logback.xml
│   │   ├── 📁 build
│   │   ├── 📄 build.gradle.kts
│   │   ├── 📁 src
│   │   │   ├── 📁 main
│   │   │   │   ├── 📁 kotlin
│   │   │   │   │   ├── 📁 es
│   │   │   │   │   │   ├── 📁 gaspardev
│   │   │   │   │   │   │   ├── 📄 Application.kt
│   │   │   │   │   │   │   ├── 📁 bucket
│   │   │   │   │   │   │   ├── 📁 db
│   │   │   │   │   │   │   │   ├── 📄 allergiesMapping.kt
│   │   │   │   │   │   │   │   ├── 📄 sportsmanMapping.kt
│   │   │   │   │   │   │   │   ├── 📄 Tables.kt
│   │   │   │   │   │   │   │   ├── 📄 trainerMappings.kt
│   │   │   │   │   │   │   │   ├── 📄 userMapping.kt
│   │   │   │   │   │   │   ├── 📁 modules
│   │   │   │   │   │   │   │   ├── 📁 endpoints
│   │   │   │   │   │   │   │   │   ├── 📄 DataBase.kt
│   │   │   │   │   │   │   │   │   ├── 📄 Resources.kt
│   │   │   │   │   │   │   │   │   ├── 📄 Sportsmankt.kt
│   │   │   │   │   │   │   │   │   ├── 📄 Trainer.kt
│   │   │   │   │   │   │   │   │   ├── 📄 Upload.kt
│   │   │   │   │   │   │   │   │   ├── 📄 User.kt
│   │   │   │   │   │   │   │   ├── 📁 shockets
│   │   │   │   │   │   │   │   │   ├── 📄 Chat.kt
│   │   │   │   ├── 📁 resources
│   ├── 📄 settings.gradle.kts
│   ├── 📁 shared
│   │   ├── 📁 build
│   │   ├── 📄 build.gradle.kts
│   │   ├── 📁 src
│   │   │   ├── 📁 androidMain
│   │   │   │   ├── 📁 kotlin
│   │   │   │   │   ├── 📁 es
│   │   │   │   │   │   ├── 📁 gaspardev
│   │   │   │   │   │   │   ├── 📁 core
│   │   │   │   │   │   │   │   ├── 📁 debug
│   │   │   │   │   │   │   │   │   ├── 📄 FilePrintter.android.kt
│   │   │   │   │   │   │   ├── 📁 utils
│   │   │   │   │   │   │   │   ├── 📄 Constants.android.kt
│   │   │   │   │   │   │   │   ├── 📄 Logger.android.kt
│   │   │   ├── 📁 commonMain
│   │   │   │   ├── 📁 kotlin
│   │   │   │   │   ├── 📁 es
│   │   │   │   │   │   ├── 📁 gaspardev
│   │   │   │   │   │   │   ├── 📁 auxliars
│   │   │   │   │   │   │   │   ├── 📄 Dupla.kt
│   │   │   │   │   │   │   │   ├── 📄 Either.kt
│   │   │   │   │   │   │   ├── 📁 controllers
│   │   │   │   │   │   │   │   ├── 📄 LoggedUser.kt
│   │   │   │   │   │   │   ├── 📁 core
│   │   │   │   │   │   │   │   ├── 📁 debug
│   │   │   │   │   │   │   │   │   ├── 📄 BasicPrintter.kt
│   │   │   │   │   │   │   │   │   ├── 📄 DebugPrintter.kt
│   │   │   │   │   │   │   │   │   ├── 📄 FilePrintter.kt
│   │   │   │   │   │   │   │   ├── 📁 domain
│   │   │   │   │   │   │   │   │   ├── 📁 entities
│   │   │   │   │   │   │   │   │   │   ├── 📄 Availability.kt
│   │   │   │   │   │   │   │   │   │   ├── 📄 Certification.kt
│   │   │   │   │   │   │   │   │   │   ├── 📄 Chat.kt
│   │   │   │   │   │   │   │   │   │   ├── 📄 Diet.kt
│   │   │   │   │   │   │   │   │   │   ├── 📄 Dish.kt
│   │   │   │   │   │   │   │   │   │   ├── 📄 Exercise.kt
│   │   │   │   │   │   │   │   │   │   ├── 📄 Message.kt
│   │   │   │   │   │   │   │   │   │   ├── 📄 Note.kt
│   │   │   │   │   │   │   │   │   │   ├── 📄 Resource.kt
│   │   │   │   │   │   │   │   │   │   ├── 📄 Social.kt
│   │   │   │   │   │   │   │   │   │   ├── 📄 Sportsman.kt
│   │   │   │   │   │   │   │   │   │   ├── 📄 Suplemment.kt
│   │   │   │   │   │   │   │   │   │   ├── 📄 Trainer.kt
│   │   │   │   │   │   │   │   │   │   ├── 📄 User.kt
│   │   │   │   │   │   │   │   │   │   ├── 📄 Workout.kt
│   │   │   │   │   │   │   │   │   ├── 📁 usecases
│   │   │   │   │   │   │   │   │   │   ├── 📁 create
│   │   │   │   │   │   │   │   │   │   │   ├── 📄 RegisterNewSportsman.kt
│   │   │   │   │   │   │   │   │   │   │   ├── 📄 RegisterNewTrainer.kt
│   │   │   │   │   │   │   │   │   │   ├── 📁 delete
│   │   │   │   │   │   │   │   │   │   ├── 📁 read
│   │   │   │   │   │   │   │   │   │   │   ├── 📄 LogInUser.kt
│   │   │   │   │   │   │   │   │   │   ├── 📁 update
│   │   │   │   │   │   │   │   │   │   ├── 📄 UseCase.kt
│   │   │   │   │   │   │   │   ├── 📁 infrastructure
│   │   │   │   │   │   │   │   │   ├── 📁 apis
│   │   │   │   │   │   │   │   │   │   ├── 📄 SportsmanAPI.kt
│   │   │   │   │   │   │   │   │   │   ├── 📄 TrainerAPI.kt
│   │   │   │   │   │   │   │   │   │   ├── 📄 UserAPI.kt
│   │   │   │   │   │   │   │   │   │   ├── 📄 WorkoutAPI.kt
│   │   │   │   │   │   │   │   │   ├── 📁 memo
│   │   │   │   │   │   │   │   │   │   ├── 📄 CacheManager.kt
│   │   │   │   │   │   │   │   │   │   ├── 📄 ContentManager.kt
│   │   │   │   │   │   │   │   │   ├── 📁 repositories
│   │   │   │   │   │   │   │   │   │   ├── 📄 NotesRepositoryImp.kt
│   │   │   │   │   │   │   │   │   │   ├── 📄 SportsmantRepositoryImp.kt
│   │   │   │   │   │   │   │   │   │   ├── 📄 TrainerRepositoryImp.kt
│   │   │   │   │   │   │   │   │   │   ├── 📄 UserRespositoryImp.kt
│   │   │   │   │   │   │   │   │   │   ├── 📄 WorkoutRespositoryImp.kt
│   │   │   │   │   │   │   ├── 📁 enums
│   │   │   │   │   │   │   │   ├── 📄 BodyPart.kt
│   │   │   │   │   │   │   │   ├── 📄 MediaType.kt
│   │   │   │   │   │   │   │   ├── 📄 MessageType.kt
│   │   │   │   │   │   │   │   ├── 📄 WeekDay.kt
│   │   │   │   │   │   │   ├── 📁 interfaces
│   │   │   │   │   │   │   │   ├── 📁 apis
│   │   │   │   │   │   │   │   │   ├── 📄 API.kt
│   │   │   │   │   │   │   │   ├── 📁 debug
│   │   │   │   │   │   │   │   │   ├── 📄 Printter.kt
│   │   │   │   │   │   │   │   ├── 📁 repositories
│   │   │   │   │   │   │   │   │   ├── 📄 EntitieRepository.kt
│   │   │   │   │   │   │   │   │   ├── 📄 NotesRepository.kt
│   │   │   │   │   │   │   │   │   ├── 📄 SportsmanRepository.kt
│   │   │   │   │   │   │   │   │   ├── 📄 TrainerRepository.kt
│   │   │   │   │   │   │   │   │   ├── 📄 UserRepository.kt
│   │   │   │   │   │   │   │   │   ├── 📄 WorkoutRespository.kt
│   │   │   │   │   │   │   ├── 📁 utils
│   │   │   │   │   │   │   │   ├── 📄 Constants.kt
│   │   │   │   │   │   │   │   ├── 📄 Encrypter.kt
│   │   │   │   │   │   │   │   ├── 📄 Logger.kt
│   │   │   ├── 📁 commonTest
│   │   │   │   ├── 📁 kotlin
│   │   │   ├── 📁 jvmMain
│   │   │   │   ├── 📁 kotlin
│   │   │   │   │   ├── 📁 es
│   │   │   │   │   │   ├── 📁 gaspardev
│   │   │   │   │   │   │   ├── 📁 core
│   │   │   │   │   │   │   │   ├── 📁 debug
│   │   │   │   │   │   │   │   │   ├── 📄 FilePrintter.jvm.kt
│   │   │   │   │   │   │   ├── 📁 utils
│   │   │   │   │   │   │   │   ├── 📄 Constants.jvm.kt
│   │   │   │   │   │   │   │   ├── 📄 Logger.jvm.kt
│   ├── 📁 Writerside
│   │   ├── 📁 .idea
│   │   ├── 📄 c.list
│   │   ├── 📁 cfg
│   │   │   ├── 📄 buildprofiles.xml
│   │   ├── 📄 d.tree
│   │   ├── 📁 images
│   │   │   ├── 📄 completion_procedure.png
│   │   │   ├── 📄 completion_procedure_dark.png
│   │   │   ├── 📄 convert_table_to_xml.png
│   │   │   ├── 📄 convert_table_to_xml_dark.png
│   │   │   ├── 📄 Data-base.png
│   │   │   ├── 📄 new_topic_options.png
│   │   │   ├── 📄 new_topic_options_dark.png
│   │   ├── 📁 openApi
│   │   │   ├── 📄 ResourceApi.yaml
│   │   │   ├── 📄 ResourceApi.yaml~
│   │   │   ├── 📄 UserApi.yaml
│   │   ├── 📁 topics
│   │   │   ├── 📁 topics
│   │   │   │   ├── 📄 Diagramas-UML.md
│   │   │   │   ├── 📄 Estructura-de-los-Datos.md
│   │   │   │   ├── 📄 Mapa-de-rutas.md
│   │   │   │   ├── 📄 Propuesta_de_Aplicación.md
│   │   ├── 📄 v.list
│   │   ├── 📄 writerside.cfg


```