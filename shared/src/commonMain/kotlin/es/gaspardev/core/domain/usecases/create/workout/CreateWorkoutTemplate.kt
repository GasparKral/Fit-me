package es.gaspardev.core.domain.usecases.create.workout

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.entities.users.Trainer
import es.gaspardev.core.domain.entities.workouts.WorkoutTemplate
import es.gaspardev.core.domain.usecases.UseCase
import es.gaspardev.core.infrastructure.repositories.WorkoutRepositoryImp
import es.gaspardev.interfaces.repositories.WorkoutRepository

/**
 * Use case para crear una nueva plantilla de entrenamiento
 * 
 * @param repo Repositorio de entrenamientos
 */
class CreateWorkoutTemplate(
    private val repo: WorkoutRepository = WorkoutRepositoryImp()
) : UseCase<Int, Pair<WorkoutTemplate, Trainer>>() {
    
    /**
     * Ejecuta la creación de la plantilla de entrenamiento
     * 
     * @param params Par que contiene la plantilla de entrenamiento y el entrenador
     * @return Either con el ID de la plantilla creada o un error
     */
    override suspend fun run(params: Pair<WorkoutTemplate, Trainer>): Either<Exception, Int> {
        val (template, trainer) = params
        
        // Validar datos antes de crear la plantilla
        val validationResult = validateWorkoutTemplate(template)
        if (validationResult != null) {
            return Either.Failure(validationResult)
        }
        
        return repo.createWorkoutTemplate(template, trainer)
    }
    
    /**
     * Valida los datos de la plantilla de entrenamiento antes de crear
     * 
     * @param template Plantilla a validar
     * @return Exception si hay errores de validación, null si todo es válido
     */
    private fun validateWorkoutTemplate(template: WorkoutTemplate): Exception? {
        // Validar nombre (obligatorio)
        if (template.name.isBlank()) {
            return IllegalArgumentException("El nombre de la plantilla es obligatorio")
        }
        
        if (template.name.length < 3) {
            return IllegalArgumentException("El nombre de la plantilla debe tener al menos 3 caracteres")
        }
        
        if (template.name.length > 255) {
            return IllegalArgumentException("El nombre de la plantilla no puede exceder 255 caracteres")
        }
        
        // Validar descripción (obligatoria)
        if (template.description.isBlank()) {
            return IllegalArgumentException("La descripción de la plantilla es obligatoria")
        }
        
        if (template.description.length < 10) {
            return IllegalArgumentException("La descripción debe tener al menos 10 caracteres")
        }
        
        if (template.description.length > 1000) {
            return IllegalArgumentException("La descripción no puede exceder 1000 caracteres")
        }
        
        // Validar que tenga al menos un ejercicio asignado
        if (template.exercises.isEmpty()) {
            return IllegalArgumentException("La plantilla debe tener al menos un ejercicio asignado")
        }
        
        // Validar que cada día tenga al menos un ejercicio
        val emptyDays = template.exercises.filter { it.value.isEmpty() }
        if (emptyDays.isNotEmpty()) {
            val dayNames = emptyDays.keys.joinToString(", ")
            return IllegalArgumentException("Los siguientes días no tienen ejercicios asignados: $dayNames")
        }
        
        // Validar ejercicios
        template.exercises.values.flatten().forEach { workoutExercise ->
            if (workoutExercise.exercise.name.isBlank()) {
                return IllegalArgumentException("Todos los ejercicios deben tener un nombre válido")
            }
            
            if (workoutExercise.reps <= 0) {
                return IllegalArgumentException("El número de repeticiones debe ser mayor a 0")
            }
            
            if (workoutExercise.sets <= 0) {
                return IllegalArgumentException("El número de series debe ser mayor a 0")
            }
            
            if (workoutExercise.reps > 1000) { // Límite razonable
                return IllegalArgumentException("El número de repeticiones de ${workoutExercise.exercise.name} parece excesivo")
            }
            
            if (workoutExercise.sets > 50) { // Límite razonable
                return IllegalArgumentException("El número de series de ${workoutExercise.exercise.name} parece excesivo")
            }
        }
        
        return null
    }
    
    companion object {
        // Límites para validación
        const val MIN_NAME_LENGTH = 3
        const val MAX_NAME_LENGTH = 255
        const val MIN_DESCRIPTION_LENGTH = 10
        const val MAX_DESCRIPTION_LENGTH = 1000
        const val MAX_REPS = 1000
        const val MAX_SETS = 50
        
        /**
         * Valida si un nombre de plantilla es válido
         */
        fun isValidTemplateName(name: String): Boolean {
            return name.isNotBlank() && 
                   name.length >= MIN_NAME_LENGTH && 
                   name.length <= MAX_NAME_LENGTH
        }
        
        /**
         * Valida si una descripción de plantilla es válida
         */
        fun isValidTemplateDescription(description: String): Boolean {
            return description.isNotBlank() && 
                   description.length >= MIN_DESCRIPTION_LENGTH && 
                   description.length <= MAX_DESCRIPTION_LENGTH
        }
        
        /**
         * Valida si el número de repeticiones es válido
         */
        fun isValidReps(reps: Int): Boolean {
            return reps > 0 && reps <= MAX_REPS
        }
        
        /**
         * Valida si el número de series es válido
         */
        fun isValidSets(sets: Int): Boolean {
            return sets > 0 && sets <= MAX_SETS
        }
    }
}
