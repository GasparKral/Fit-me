package es.gaspardev.db.mappings.diets

import es.gaspardev.core.domain.entities.*
import es.gaspardev.db.*
import es.gaspardev.db.mappings.notes.NoteDao
import es.gaspardev.db.mappings.notes.NoteEntity
import es.gaspardev.db.mappings.users.SportsmanEntity
import es.gaspardev.modules.endpoints.BaseDao
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import kotlin.time.Duration.Companion.milliseconds

// Dish DAO
class DishDao : BaseDao<DishEntity, Int>(DishEntity) {
    fun toDomain(dish: DishEntity): Dish {
        return Dish(
            id = dish.id.value,
            name = dish.name,
            optionalDishes = dish.alternatives.map { toDomain(it.alternativeDishId) }
        )
    }
}

class DishEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<DishEntity>(DishesTable)

    var name by DishesTable.name

    val alternatives by DishAlternativeEntity referrersOn DishAlternativesTable.dishId
}

class DishAlternativeEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<DishAlternativeEntity>(DishAlternativesTable)

    var dishId by DishEntity referencedOn DishAlternativesTable.dishId
    var alternativeDishId by DishEntity referencedOn DishAlternativesTable.alternativeDishId
}


// Diet DAO
class DietDao(
) : BaseDao<DietEntity, Int>(DietEntity) {
    fun toDomain(diet: DietEntity): Diet {
        return Diet(
            name = diet.name,
            description = diet.description,
            notes = diet.notes.map { NoteDao().toDomain(it) },
            meals = diet.meals.groupBy { it.day }
                .mapValues { (_, meals) -> meals.map { Pair(DietMealDao().toDomain(it), it.quantity) } },
            duration = diet.durationMillis.milliseconds,
            initialDate = diet.initialDate
        )
    }
}

class DietEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<DietEntity>(DietsTable)

    var sportsmanId by SportsmanEntity referencedOn DietsTable.sportsmanId
    var name by DietsTable.name
    var description by DietsTable.description
    var durationMillis by DietsTable.duration
    var initialDate by DietsTable.initialDate

    val notes by NoteEntity via DietNotesIntermediateTable
    val meals by DietMealEntity referrersOn DietMealsTable.dietId
}

class DietMealDao() : BaseDao<DietMealEntity, Int>(DietMealEntity) {
    fun toDomain(dietMeal: DietMealEntity): Dish {
        return DishDao().toDomain(dietMeal.dishId)
    }
}

class DietMealEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<DietMealEntity>(DietMealsTable)

    var dietId by DietEntity referencedOn DietMealsTable.dietId
    var day by DietMealsTable.day
    var dishId by DishEntity referencedOn DietMealsTable.dishId
    var quantity by DietMealsTable.quantity
}

class CompletedDietDao : BaseDao<CompletedDietEntity, Int>(CompletedDietEntity) {
    fun toDomain(entity: CompletedDietEntity): CompletedDiet {
        return CompletedDiet(
            diet = DietDao().toDomain(entity.diet),
            completeAt = entity.completeAt,
            notes = entity.notes.map { NoteDao().toDomain(it) }
        )
    }
}

class CompletedDietEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<CompletedDietEntity>(CompletedDietsTable)

    var diet by DietEntity referencedOn CompletedDietsTable.diet
    var sportsman by SportsmanEntity referencedOn CompletedDietsTable.sportsman
    var completeAt by CompletedDietsTable.completedAt
    var notes by NoteEntity via CompletedDietNotesTable
}