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

## Información sobre el desarrollo

Esta sección describe la información esencial para el desarrollo de la aplicación, incluyendo su ciclo de vida, los requisitos funcionales y no funcionales, así como detalles específicos para cada uno de los componentes.

### Ciclo de vida
El ciclo de vida del desarrollo de la aplicación seguirá un modelo **Evolutivo Incremental**, lo que permitirá la entrega continua de nuevas funcionalidades y la mejora progresiva del sistema con base en retroalimentación y pruebas. Junto con el uso de **Clean Architecture** para la organización del código podré ampliar o reformular la estructura de la aplicación en caso de ser necesario.

### Requerimientos

<tabs>
  <tab title="Requisitos funcionales">
    La aplicación estará compuesta por tres principales componentes: <b>Cliente Desktop</b> <b>Cliente Mobile</b> y <b>Servidor</b>.
    <list>
      <li>Desarrollo del servidor que gestionará la API REST para los distintos clientes.</li>
      <li>Desarrollo de la base de datos utilizando <b>PostgreSQL</b> para el almacenamiento de la información.</li>
    </list>
    <deflist collapsible="true">
      <def title="Cliente Desktop" default-state="expanded">
        <list>
          <li>Procesado del alta de entrenadores y autenticación de usuarios mediante inicio de sesión seguro.</li>     
          <li>Capacidad de agregar y gestionar nuevos deportistas bajo la tutela de un entrenador.</li>
          <li>Creación, modificación, eliminación y asignación de rutinas, dietas y suplementos personalizados para cada deportista.</li>        
          <li>Gestión integral de la información de los deportistas, incluyendo métricas de evaluación del rendimiento y planificación de entrenamientos.</li>        
          <li>Configuración del área personal, permitiendo la personalización de horarios, preferencias y ajustes de la cuenta de usuario, incluyendo el correo electrónico.</li>
        </list>
      </def>
      <def title="Cliente Mobile">
        <list>
          <li>Implementación de un sistema de <b>WebSockets</b> para comunicación bidireccional con el servidor, utilizado para funciones de chat y notificaciones en tiempo real.</li>
          <li>Desarrollo de herramientas de entrenamiento integradas, como <b>cronómetro</b>, <b>contador de repeticiones</b> y <b>notas rápidas</b> vinculadas a ejercicios.</li>
          <li>Acceso rápido a rutinas personalizadas y registro en tiempo real del progreso del usuario.</li>
        </list>
      </def>
      <def title="Servidor">
        <list>
          <li>Implementación de una API REST con autenticación basada en <b>JWT (JSON Web Token)</b></li>
          <li>Manejo de concurrencia y escalabilidad para soportar múltiples clientes simultáneamente.</li>
          <li>Gestión eficiente de la persistencia de datos mediante <b>ORMs adecuados (ej. Exposed y Ktor-serialization)</b>.</li>
          <li>Soporte para almacenamiento y procesamiento de métricas de entrenamiento y evolución de los deportistas.</li>
        </list>
      </def>
    </deflist>
  </tab>
  <tab title="Requisitos no funcionales">
    La aplicación debe garantizar una <b>experiencia de usuario fluida y eficiente</b>, optimizando el rendimiento en cada una de sus plataformas.
    <list>
      <li>Diseño de una interfaz intuitiva y accesible para el usuario final.</li>
      <li>Garantizar la seguridad en el acceso y la manipulación de datos sensibles.</li>
      <li>Optimización de la carga y procesamiento de datos para reducir tiempos de respuesta.</li>
    </list>
    <deflist collapsible="true">
      <def title="Cliente Desktop" default-state="expanded">
        <list>
          <li>Desarrollo basado en el modelo <b>SPA (Single Page Application)</b> con un enrutador personalizado para la navegación interna.</li>
          <li>Implementación de actualización optimista de la interfaz: los cambios se reflejan en la UI antes de confirmarse en el servidor. Si la petición falla, se notifica al usuario y se revierte el cambio.</li>
          <li>Uso de <b>Jetpack Compose Desktop</b> para la construcción de la interfaz con un diseño adaptable.</li>
        </list>
      </def>
      <def title="Cliente Mobile">
        <list>
          <li>Optimización para dispositivos móviles con <b>Jetpack Compose Multiplatform</b>.</li>
          <li>Sincronización eficiente entre clientes y servidor para minimizar latencia en la obtención de datos.</li>
          <li>Integración de notificaciones push para alertas importantes.</li>
        </list>
      </def>
    </deflist>
  </tab>
</tabs>
