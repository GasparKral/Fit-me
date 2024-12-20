# Propuesta de Aplicación

## Introducción a Fit-me

### Justificación
Fit-me es un software especializado en el workflow entre entrenador y deportista permitiendo una mayor coordinación, fácil ajuste de rutinas deportivas y dieta.

Parte de la necesidad de una forma de contacto unificado entre deportistas y entrenadores, donde tener toda la información necesaria a mano sin tener que estar navegando entre distintos medios externos.

El sistema se secciona entre cuatro medios:

- **Cliente Mobile**: Punto crítico de acceso a la información creada por los entrenadores y consumida por los deportistas, permitiendo un flujo bidireccional.
- **Cliente Desktop**: Exclusivo de los entrenadores, contendrá la información estadística de los datos generados por los deportistas asociados a cada entrenador.
- **Servidor**: Actuará como punto medio entre los distintos clientes y servirá ciertas API’s para el cliente web.
- **Cliente Web** *(aún por decidir)*: Dividido en dos tipos:
    - Del lado del entrenador: Similar al cliente desktop, permitirá trabajar en caso de no contar con su dispositivo habitual.
    - Del lado del deportista: Acceso a la información de su cliente móvil y a estadísticas compartidas por el entrenador.
> **Aclaración**
>
> Se decidirá en función del tiempo, pero la estructura de la aplicación tanto como el servidor se organizará para permitir su futuro desarrollo
>
{style="note"}

---
## Arquitectura de la Aplicación

Puesto que todo el sistema se subdivide en varios clientes, implica el uso de varias tecnologías especializadas de cada medio, pero para evitar distintos *code-bases* se usará **Kotlin Multiplatform**, una tecnología desarrollada por **JetBrains** que permite el desarrollo por capas, reutilizando la lógica e interfaz gráfica entre plataformas.

---

### Tecnologías Usadas

- **IDE**:
    - JetBrains Fleet: Soporte oficial para Kotlin Multiplatform, necesario para el desarrollo con React usando Vite y TypeScript.

- **Lenguajes**:
    - Kotlin (principal)
    - Java
    - TypeScript
    - JavaScript
    - HTML y CSS

- **Frameworks**:
    - **Compose**: Desarrollo de interfaz gráfica y lógica para cliente Android.
    - **Compose Multiplatform**: Wrapper para extender Compose a desktop.
    - **Vite**: Framework web pasivo para compilar, optimizar y desplegar código HTML, CSS y JS. Compatible con React.
    - **Ktor**: Framework asincrónico para desarrollo de servidores.

- **Bases de Datos**: PostgreSQL

- **Librerías web usadas**:
  - ReactJs
  - Motion
  - TailwindCss

- **Otras Tecnologías**:
    - **Figma**: Editor de gráficos vectoriales y prototipos.
    - **WriterSide**: Editor de documentación de JetBrains.
    - **Docker**: Definición y ejecución de aplicaciones multi-contenedor con Docker Compose.

---

## Descripción de las Funcionalidades Principales

La aplicación diferenciará entre dos tipos de usuarios: **Entrenador** y **Deportista**.

---

### Diferenciación de Tipos de Usuarios

#### Entrenador
- Asociar varios deportistas con acceso a sus datos personales (nombre, edad) y específicos (peso, mediciones, carga de trabajo).
- Adaptar rutinas, dietas y suplementación desde una interfaz especializada.
- Crear nuevos sets de ejercicios con descripción y videos opcionales.
- Crear notas, recordatorios y usar el chat bidireccional con deportistas.
- Generar estadísticas y gráficos automatizados.

#### Deportista
- Conectarse a un entrenador mediante nombre, QR auto-generado o enlace de correo automático.
- Acceso a entrenamientos, dietas y suplementación asignados.
- Herramientas de entrenamiento integradas:
    - Cronómetro
    - Contador de repeticiones
    - Generación de notas rápidas vinculadas a ejercicios.
- Acceso al chat con su entrenador.

---
