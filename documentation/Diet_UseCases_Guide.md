# Use Cases para Gestión de Dietas

## Use Cases Disponibles

### 1. **CreateNewDiet** - Crear Nueva Dieta
```kotlin
val createDiet = CreateNewDiet()
val diet = Diet(
    name = "Plan de pérdida de peso",
    description = "Dieta baja en carbohidratos",
    dietType = DietType.WEIGHT_LOSS,
    duration = 30.toDuration(DurationUnit.DAYS),
    dishes = mutableMapOf(
        WeekDay.MONDAY to mutableListOf(
            DietDish(
                amout = 150.0,
                mealType = MealType.BREAKFAST,
                dish = Dish(1, "Avena con frutas")
            )
        )
    )
)

createDiet.run(Pair(diet, trainer)).fold(
    { dietId -> println("Dieta creada con ID: $dietId") },
    { error -> println("Error: ${error.message}") }
)
```

### 2. **UpdateDiet** - Actualizar Dieta Existente
```kotlin
val updateDiet = UpdateDiet()
val dietPlan = DietPlan(
    dietId = 1,
    name = "Plan actualizado",
    description = "Nueva descripción",
    type = DietType.MUSCLE_GAIN,
    duration = 45.toDuration(DurationUnit.DAYS),
    frequency = "7",
    asignedCount = 0,
    dishes = updatedDishes
)

updateDiet.run(dietPlan).fold(
    { updatedPlan -> println("Dieta actualizada: ${updatedPlan.name}") },
    { error -> println("Error: ${error.message}") }
)
```

### 3. **DeleteDiet** - Eliminar Dieta
```kotlin
val deleteDiet = DeleteDiet()
val dietId = 1

deleteDiet.run(dietId).fold(
    { _ -> println("Dieta eliminada correctamente") },
    { error -> println("Error: ${error.message}") }
)
```

### 4. **GetDietById** - Obtener Dieta por ID
```kotlin
val getDiet = GetDietById()
val dietId = 1

getDiet.run(dietId).fold(
    { dietPlan -> println("Dieta encontrada: ${dietPlan.name}") },
    { error -> println("Error: ${error.message}") }
)
```

### 5. **DuplicateDiet** - Duplicar Dieta Existente
```kotlin
val duplicateDiet = DuplicateDiet()
val params = Triple(originalDietPlan, "Copia de ${originalDietPlan.name}", trainer)

duplicateDiet.run(params).fold(
    { newDietPlan -> println("Dieta duplicada: ${newDietPlan.name}") },
    { error -> println("Error: ${error.message}") }
)
```

### 6. **AssignDietToAthlete** - Asignar Dieta a Atleta
```kotlin
val assignDiet = AssignDietToAthlete()
val params = Pair(dietPlan, athlete)

assignDiet.run(params).fold(
    { updatedPlan -> println("Dieta asignada. Contador: ${updatedPlan.asignedCount}") },
    { error -> println("Error: ${error.message}") }
)
```

### 7. **GetTrainerDietsPlans** - Obtener Planes de Dieta del Entrenador
```kotlin
val getPlans = GetTrainerDietsPlans()

getPlans.run(trainer).fold(
    { plans -> println("Encontrados ${plans.size} planes de dieta") },
    { error -> println("Error: ${error.message}") }
)
```

### 8. **GetTrainerDietsTemplates** - Obtener Plantillas de Dieta del Entrenador
```kotlin
val getTemplates = GetTrainerDietsTemplates()

getTemplates.run(trainer).fold(
    { templates -> println("Encontradas ${templates.size} plantillas") },
    { error -> println("Error: ${error.message}") }
)
```

### 9. **GetAthleteDietHistory** - Obtener Historial de Dietas del Atleta
```kotlin
val getHistory = GetAthleteDietHystory()

getHistory.run(athlete).fold(
    { history -> println("Historial: ${history.size} registros") },
    { error -> println("Error: ${error.message}") }
)
```

## Ejemplos de Uso Completos

### Flujo Completo: Crear, Actualizar y Asignar Dieta

```kotlin
class DietManagementService {
    private val createDiet = CreateNewDiet()
    private val updateDiet = UpdateDiet()
    private val assignDiet = AssignDietToAthlete()
    private val deleteDiet = DeleteDiet()
    
    suspend fun createAndAssignDiet(
        trainer: Trainer,
        athlete: Athlete,
        dietData: Diet
    ): Either<Exception, DietPlan> {
        
        // 1. Crear la dieta
        return createDiet.run(Pair(dietData, trainer)).flatMap { dietId ->
            
            // 2. Convertir a DietPlan para asignación
            val dietPlan = DietPlan(
                dietId = dietId,
                name = dietData.name,
                description = dietData.description,
                type = dietData.dietType,
                duration = dietData.duration,
                frequency = dietData.dishes.keys.size.toString(),
                asignedCount = 0,
                dishes = dietData.dishes
            )
            
            // 3. Asignar al atleta
            assignDiet.run(Pair(dietPlan, athlete))
        }
    }
    
    suspend fun updateDietSafely(
        dietPlan: DietPlan,
        newName: String,
        newDescription: String
    ): Either<Exception, DietPlan> {
        
        val updatedPlan = dietPlan.copy(
            name = newName,
            description = newDescription
        )
        
        return updateDiet.run(updatedPlan)
    }
    
    suspend fun deleteDietSafely(dietId: Int): Either<Exception, UseCase.None> {
        return deleteDiet.run(dietId)
    }
}
```

### Uso en ViewModel/Presenter

```kotlin
class DietViewModel {
    private val dietService = DietManagementService()
    
    fun createDiet(dietData: Diet, trainer: Trainer) {
        viewModelScope.launch {
            dietService.createAndAssignDiet(trainer, currentAthlete, dietData).fold(
                onSuccess = { dietPlan ->
                    _uiState.update { it.copy(
                        diets = it.diets + dietPlan,
                        isLoading = false
                    )}
                    showSuccessMessage("Dieta creada y asignada correctamente")
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isLoading = false) }
                    showErrorMessage("Error al crear dieta: ${error.message}")
                }
            )
        }
    }
    
    fun updateDiet(dietPlan: DietPlan, newName: String, newDescription: String) {
        viewModelScope.launch {
            val updateUseCase = UpdateDiet()
            updateUseCase.run(dietPlan.copy(
                name = newName,
                description = newDescription
            )).fold(
                onSuccess = { updatedPlan ->
                    _uiState.update { state ->
                        state.copy(
                            diets = state.diets.map { 
                                if (it.dietId == updatedPlan.dietId) updatedPlan else it 
                            }
                        )
                    }
                },
                onFailure = { error ->
                    showErrorMessage("Error al actualizar: ${error.message}")
                }
            )
        }
    }
}
```

## Consideraciones de Uso

### ✅ **Buenas Prácticas:**
- Siempre manejar ambos casos (success/failure) con `.fold()`
- Usar corrutinas para operaciones asíncronas
- Validar datos antes de enviar a los use cases
- Mantener los use cases independientes y reutilizables

### ⚠️ **Cosas a Tener en Cuenta:**
- Los use cases no manejan validaciones de UI (hazlo en el ViewModel)
- Siempre verifica que los objetos existan antes de operaciones
- El contador de `asignedCount` se actualiza automáticamente
- Las validaciones de negocio se manejan en el servidor (ej: no eliminar dietas con atletas asignados)

### 🔄 **Patrón de Uso Recomendado:**
1. Validar datos de entrada en la UI
2. Llamar al use case apropiado
3. Manejar el resultado con `.fold()`
4. Actualizar el estado de la UI
5. Mostrar feedback al usuario
