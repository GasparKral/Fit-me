# Mapa de Rutas

Este documento detalla las rutas disponibles para las APIs y sus respectivas funcionalidades.

---

## **Trainner API**
_Ruta base: `/trainner/*`_

La API de entrenadores gestiona los datos relacionados con usuarios de tipo entrenador.

### **GET Requests**

---

#### **Obtener listado de deportistas asociados a un entrenador**

Obtiene la lista de deportistas asociados a un entrenador especificado mediante su correo electrónico. Un middleware verifica que la solicitud provenga del entrenador asociado.

**Endpoint:**
```
/trainner/get_sportmants/:email
```

**Respuestas:**
- **401 Unauthorized:** Si la autenticación no es válida.
- **202 Accepted:** Si la solicitud es exitosa. Se devuelve un JSON con los datos de los deportistas.

**Ejemplo de Respuesta:**
<code-block lang="JSON">
{
    "sportsmans": [
        {"name": "Paco Encinas", "email": "paquito@gmail.com"},
        {"name": "Emilio Encinas", "email": "eencinas@hotmail.com"},
        {"name": "María Casado", "email": "mcasado04@gmail.com"},
        {"name": "Cristina Riberas", "email": "rib01crist@gmail.com"}
    ]
}
</code-block>

---

#### **Obtener información específica de un deportista**

Recupera información detallada de un deportista asociado a un entrenador. Los datos se obtienen mediante el correo electrónico del deportista. También se utiliza un middleware para validar el correo del entrenador.

**Endpoint:**
```
/trainner/get_sportmant/:email/:sportman_email
```

**Respuestas:**
- **401 Unauthorized:** Si la autenticación no es válida.
- **202 Accepted:** Si la solicitud es exitosa. Se devuelve un JSON con los datos del deportista.

### **POST Requests**

---

#### **Crear un nuevo entrenador**

Crea un nuevo registro de entrenador. Se debe enviar un JSON con los datos necesarios.

**Endpoint:**
```
/trainner/new
```

**Datos requeridos:**
- **user_name:** Nombre completo del entrenador.
- **password:** Contraseña del entrenador (debe cumplir con un mínimo de 8 caracteres, incluir una letra mayúscula y un número).
- **email:** Correo electrónico del entrenador.

**Ejemplo de Request Body:**
<code-block lang="JSON">
{
    "user_name": "Juan García",
    "password": "465sdf46s5d4f6sd54fsd", // Contraseña cifrada
    "email": "example@gmail.com"
}
</code-block>

**Respuestas:**
- **201 Created:** Usuario creado exitosamente.
- **409 Conflict:** El correo electrónico ya está registrado.
- **400 Bad Request:** Error indeterminado durante la creación.

---

#### **Obtener información de un entrenador existente**

Devuelve la información de un entrenador con base en los datos proporcionados.

**Endpoint:**
```
/trainner/get
```

**Datos requeridos:**
- **email:** Correo electrónico del entrenador.
- **password:** Contraseña cifrada del entrenador.

**Ejemplo de Request Body:**
<code-block lang="JSON">
{
    "email": "example@gmail.com",
    "password": "56sd4fsd66sdfdsf8sd" // Contraseña cifrada
}
</code-block>

**Respuestas:**
- **200 OK:** Si la autenticación es válida y el usuario existe.
- **404 Not Found:** Si no se encuentra el usuario.

