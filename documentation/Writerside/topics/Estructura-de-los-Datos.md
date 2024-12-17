# Estructura de los Datos

## Clases principales

La aplicación se compondrá de las siguientes principales clases:
- **User**: Almacenará la información común entre usuarios, esto incluye los entrenadores y los deportistas.
- **Trainner**: Datos específicos de los entrenadores, incluyendo certificaciones, experiencia, disponibilidad.
- **Sportman**: Datos específicos de los deportistas, medidas, pesos, sexo, alergias, dietas, rutinas y suplementación.
- **Exercise**: Clase que representa los ejercicios a realizar.
- **Dish**: Clase que representa los platos(pollo, pescado blanco, verduras, frutas, ...) y sus cantidades en las dietas.
- **Supplementation**: Clase que representa los distintos tipos de suplementación y cantidades a usar por los deportistas.
- **Note**: Clase que representa las notas que se puede usar en los distintos sitios de la aplicación.

## Validación de JSONS

Hay dos JSON específicos que requieren de un formato rígido; estos son las rutinas/entrenamientos (Workout::class) y 
las dietas (Diet::class) que son explícitamente un Map dentro de la aplicación

### Esquema para los entrenamientos (Workout::class) 

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

### Esquema para los platos (Dish::class)

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

## Modelo pata de gallo de la base de datos
<img src="Data-base.png" alt="Estructura de la base de datos"/>

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

> Esta estructura será actualizada periodicamente
> 
{style="note"}

📦 Fit-me <br/>
├── 📁 .fleet <br/>
├── 📁 .git <br/>
├── 📄 .gitignore <br/>
├── 📁 .gradle <br/>
├── 📁 .idea <br/>
├── 📁 .kotlin <br/>
├── 📄 build.gradle.kts <br/>
├── 📁 composeApp <br/>
│├── 📁 build <br/>
│   ├── 📄 build.gradle.kts <br/>
│   ├── 📁 frontend <br/>
│   │   ├── 📁 .idea <br/>
│   │   ├── 📄 bun.lockb <br/>
│   │   ├── 📄 eslint.config.js <br/>
│   │   ├── 📄 index.html <br/>
│   │   ├── 📁 node_modules <br/>
│   │   ├── 📄 package.json <br/>
│   │   ├── 📄 postcss.config.js <br/>
│   │   ├── 📁 public <br/>
│   │   ├── 📁 src <br/>
│   │   │   ├── 📄 App.tsx <br/>
│   │   │   ├── 📁 assets <br/>
│   │   │   ├── 📄 index.css <br/>
│   │   │   ├── 📄 main.tsx <br/>
│   │   │   └── 📄 vite-env.d.ts <br/>
│   │   ├── 📄 tailwind.config.js <br/>
│   │   ├── 📄 tsconfig.app.json <br/>
│   │   ├── 📄 tsconfig.json <br/>
│   │   ├── 📄 tsconfig.node.json <br/>
│   │   └── 📄 vite.config.ts <br/>
│   └── 📁 src <br/>
│       ├── 📁 androidMain <br/>
│       │   ├── 📄 AndroidManifest.xml <br/>
│       │   └── 📁 kotlin <br/>
│       │       └── 📁 es <br/>
│       │           └── 📁 gaspardev <br/>
│       │               └── 📄 MainActivity.kt <br/>
│       ├── 📁 commonMain <br/>
│       │   ├── 📁 composeResources <br/>
│       │   │   └── 📁 drawable <br/>
│       │   │       └── 📄 compose-multiplatform.xml <br/>
│       │   └── 📁 kotlin <br/>
│       │       └── 📁 es <br/>
│       │           └── 📁 gaspardev <br/>
│       │               └── 📄 App.kt <br/>
│       └── 📁 desktopMain <br/>
│           └── 📁 kotlin <br/>
│               └── 📁 es <br/>
│                   └── 📁 gaspardev <br/>
│                       └── 📄 main.kt <br/>
├── 📁 documentation <br/>
│   ├── 📁 .idea <br/>
│   └── 📁 Writerside <br/>
│       ├── 📄 c.list <br/>
│       ├── 📁 cfg <br/>
│       │   └── 📄 buildprofiles.xml <br/>
│       ├── 📄 f.tree <br/>
│       ├── 📁 images <br/>
│       │   ├── 📄 completion_procedure.png <br/>
│       │   ├── 📄 completion_procedure_dark.png <br/>
│       │   ├── 📄 convert_table_to_xml.png <br/>
│       │   ├── 📄 convert_table_to_xml_dark.png <br/>
│       │   ├── 📄 Data-base.png <br/>
│       │   ├── 📄 new_topic_options.png <br/>
│       │   └── 📄 new_topic_options_dark.png <br/>
│       ├── 📁 topics <br/>
│       │   ├── 📄 Estructura-de-los-Datos.md <br/>
│       │   └── 📄 starter-topic.md <br/>
│       ├── 📄 v.list <br/>
│       └── 📄 writerside.cfg <br/>
├── 📁 gradle <br/>
│   ├── 📄 libs.versions.toml <br/>
│   └── 📁 wrapper <br/>
│       ├── 📄 gradle-wrapper.jar <br/>
│       └── 📄 gradle-wrapper.properties <br/>
├── 📄 gradle.properties <br/>
├── 📄 gradlew <br/>
├── 📄 gradlew.bat <br/>
├── 📁 kotlin-js-store <br/>
│   └── 📄 package-lock.json <br/>
├── 📄 LICENSE.md <br/>
├── 📄 local.properties <br/>
├── 📄 README.md <br/>
├── 📁 server <br/>
│   ├── 📄 build.gradle.kts <br/>
│   └── 📁 src <br/>
│       ├── 📁 main <br/>
│       │   ├── 📁 kotlin <br/>
│       │   │   └── 📁 es <br/>
│       │   │       └── 📁 gaspardev <br/>
│       │   │           ├── 📄 Application.kt <br/>
│       │   │           ├── 📁 db <br/>
│       │   │           └── 📁 modules <br/>
│       │   │               ├── 📄 CommonModule.kt <br/>
│       │   │               ├── 📄 DektopAndMobileModule.kt <br/>
│       │   │               ├── 📄 DesktopModule.kt <br/>
│       │   │               ├── 📄 MobileModule.kt <br/>
│       │   │               └── 📄 WebModule.kt <br/>
│       │   └── 📁 resources <br/>
└── 📁 shared <br/>
├── 📄 build.gradle.kts <br/>
└── 📁 src <br/>
├── 📁 androidMain <br/>
│   └── 📁 kotlin <br/>
│       └── 📁 es <br/>
│           └── 📁 gaspardev <br/>
│               ├── 📁 core <br/>
│               │   └── 📁 debug <br/>
│               │       └── 📄 FilePrintter.android.kt <br/>
│               └── 📁 utils <br/>
│                   └── 📄 Logger.android.kt <br/>
├── 📁 commonMain <br/>
│   └── 📁 kotlin <br/>
│       └── 📁 es <br/>
│           └── 📁 gaspardev <br/>
│               ├── 📁 auxliars <br/>
│               │   └── 📄 Either.kt <br/>
│               ├── 📁 core <br/>
│               │   ├── 📁 adapters <br/>
│               │   ├── 📁 debug <br/>
│               │   │   ├── 📄 BasicPrintter.kt <br/>
│               │   │   ├── 📄 DebugPrintter.kt <br/>
│               │   │   └── 📄 FilePrintter.kt <br/>
│               │   ├── 📁 domain <br/>
│               │   │   ├── 📁 entities <br/>
│               │   │   │   ├── 📄 Certification.kt <br/>
│               │   │   │   ├── 📄 Chat.kt <br/>
│               │   │   │   ├── 📄 Diet.kt <br/>
│               │   │   │   ├── 📄 Dish.kt <br/>
│               │   │   │   ├── 📄 Exercise.kt <br/>
│               │   │   │   ├── 📄 Message.kt <br/>
│               │   │   │   ├── 📄 Note.kt <br/>
│               │   │   │   ├── 📄 Resource.kt <br/>
│               │   │   │   ├── 📄 Sportman.kt <br/>
│               │   │   │   ├── 📄 Suplemment.kt <br/>
│               │   │   │   ├── 📄 Trainner.kt <br/>
│               │   │   │   ├── 📄 User.kt <br/>
│               │   │   │   └── 📄 Workout.kt <br/>
│               │   │   └── 📁 usecases <br/>
│               │   │       ├── 📁 create <br/>
│               │   │       │   ├── 📄 CreateNewSportManUseCase.kt <br/>
│               │   │       │   ├── 📄 CreateNewTrainnerUseCase.kt <br/>
│               │   │       │   └── 📄 CreateNewUserUseCase.kt <br/>
│               │   │       ├── 📁 delete <br/>
│               │   │       ├── 📁 read <br/>
│               │   │       └── 📁 update <br/>
│               │   └── 📁 infrastructure <br/>
│               ├── 📁 enums <br/>
│               ├── 📁 interfaces <br/>
│               └── 📁 utils <br/>
├── 📁 commonTest <br/>
│   └── 📁 kotlin <br/>
├── 📁 jsMain <br/>
│   └── 📁 kotlin <br/>
└── 📁 jvmMain <br/>
└── 📁 kotlin <br/>
