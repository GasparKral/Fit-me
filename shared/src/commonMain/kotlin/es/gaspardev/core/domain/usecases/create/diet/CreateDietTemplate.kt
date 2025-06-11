package es.gaspardev.core.domain.usecases.create.diet

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.entities.diets.DietTemplate
import es.gaspardev.core.domain.entities.users.Trainer
import es.gaspardev.core.domain.usecases.UseCase
import es.gaspardev.core.infrastructure.repositories.DietRepositoryImp
import es.gaspardev.interfaces.repositories.DietRepository

/**
 * Use case para crear una nueva plantilla de dieta
 * 
 * @param repo Repositorio de dietas
 */
class CreateDietTemplate(
    private val repo: DietRepository = DietRepositoryImp()
) : UseCase<Int, Pair<DietTemplate, Trainer>>() {
    
    /**
     * Ejecuta la creación de la plantilla de dieta
     * 
     * @param params Par que contiene la plantilla de dieta y el entrenador
     * @return Either con el ID de la plantilla creada o un error
     */
    override suspend fun run(params: Pair<DietTemplate, Trainer>): Either<Exception, Int> {
        val (template, trainer) = params
        
        // Validar datos antes de crear la plantilla
        val validationResult = validateDietTemplate(template)
        if (validationResult != null) {
            return Either.Failure(validationResult)
        }
        
        return repo.createDietTemplate(template, trainer)
    }
    
    /**
     * Valida los datos de la plantilla de dieta antes de crear
     * 
     * @param template Plantilla a validar
     * @return Exception si hay errores de validación, null si todo es válido
     */
    private fun validateDietTemplate(template: DietTemplate): Exception? {
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
        
        // Validar que tenga al menos un plato asignado
        if (template.dishes.isEmpty()) {
            return IllegalArgumentException("La plantilla debe tener al menos un plato asignado")
        }
        
        // Validar que cada día tenga al menos un plato
        val emptyDays = template.dishes.filter { it.value.isEmpty() }
        if (emptyDays.isNotEmpty()) {
            val dayNames = emptyDays.keys.joinToString(", ")
            return IllegalArgumentException("Los siguientes días no tienen platos asignados: $dayNames")
        }
        
        // Validar platos
        template.dishes.values.flatten().forEach { dietDish ->
            if (dietDish.dish.name.isBlank()) {
                return IllegalArgumentException("Todos los platos deben tener un nombre válido")
            }
            
            if (dietDish.amout <= 0.0) {
                return IllegalArgumentException("La cantidad de cada plato debe ser mayor a 0")
            }
            
            if (dietDish.amout > 10000.0) { // Límite razonable
                return IllegalArgumentException("La cantidad de ${dietDish.dish.name} parece excesiva")
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
        const val MAX_DISH_AMOUNT = 10000.0
        
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
    }
}
