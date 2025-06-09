# API Endpoints para Dietas

## Endpoints Disponibles

### 1. GET `/diets/plans/{trainer_id}`
Obtiene todos los planes de dieta de un entrenador específico.

**Parámetros:**
- `trainer_id` (path): ID del entrenador

**Respuesta:** Lista de `DietPlan`

### 2. GET `/diets/templates/{trainer_id}`
Obtiene todas las plantillas de dieta de un entrenador específico.

**Parámetros:**
- `trainer_id` (path): ID del entrenador

**Respuesta:** Lista de `DietTemplate`

### 3. POST `/diets/create/{trainer_id}`
Crea una nueva dieta.

**Parámetros:**
- `trainer_id` (path): ID del entrenador que crea la dieta

**Body:** Objeto `Diet`
```json
{
    "name": "Plan de pérdida de peso",
    "description": "Dieta baja en carbohidratos",
    "dietType": "WEIGHT_LOSS",
    "duration": "30D",
    "dishes": {
        "MONDAY": [
            {
                "amout": 150.0,
                "mealType": "BREAKFAST",
                "dish": {
                    "id": 1,
                    "name": "Avena con frutas"
                }
            }
        ]
    }
}
```

**Respuesta:** Objeto `Diet` creado con código 201

### 4. PATCH `/diets` (NUEVO)
Actualiza un plan de dieta existente.

**Body:** Objeto `DietPlan`
```json
{
    "dietId": 1,
    "name": "Plan actualizado",
    "description": "Nueva descripción",
    "type": "MUSCLE_GAIN",
    "duration": "45D",
    "frequency": "7",
    "asignedCount": 2,
    "dishes": {
        "MONDAY": [
            {
                "amout": 200.0,
                "mealType": "LUNCH",
                "dish": {
                    "id": 2,
                    "name": "Pollo con arroz"
                }
            }
        ]
    }
}
```

**Respuesta:** 
- **200 OK:** Objeto `DietPlan` actualizado
- **404 Not Found:** Si la dieta no existe
- **400 Bad Request:** Si el nombre está vacío
- **500 Internal Server Error:** Error del servidor

### 5. DELETE `/diets?diet_id={id}` (NUEVO)
Elimina una dieta específica.

**Parámetros:**
- `diet_id` (query): ID de la dieta a eliminar

**Respuesta:**
- **200 OK:** Dieta eliminada correctamente
- **404 Not Found:** Si la dieta no existe
- **400 Bad Request:** Si el parámetro diet_id no se proporciona
- **500 Internal Server Error:** Error del servidor (ej: atletas asignados)

## Funciones del DAO Agregadas

### `updateDiet(dietPlan: DietPlan): DietPlanEntity?`
- Actualiza los campos básicos de la dieta
- Elimina todas las relaciones diet-dish existentes
- Crea nuevas relaciones basadas en el DietPlan proporcionado
- Busca platos existentes o crea nuevos según sea necesario

### `deleteDiet(dietId: Int): Boolean`
- Verifica que no haya atletas asignados a la dieta
- Elimina todas las relaciones diet-dish
- Elimina la dieta
- Retorna true si fue exitoso, false si no se encontró la dieta
- Lanza excepción si hay atletas asignados

## Validaciones Implementadas

### Para actualización (PATCH):
- Nombre no puede estar vacío
- La dieta debe existir

### Para eliminación (DELETE):
- La dieta debe existir
- No debe tener atletas asignados

## Manejo de Errores

Todos los endpoints manejan errores con códigos HTTP apropiados y mensajes descriptivos en español. Los errores de validación y restricciones de integridad se capturan y reportan adecuadamente.
