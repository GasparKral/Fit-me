package es.gaspardev.db


import es.gaspardev.core.domain.entities.Diet
import es.gaspardev.core.domain.entities.Sportsman
import es.gaspardev.core.domain.entities.UserStatus
import es.gaspardev.core.domain.entities.Workout
import kotlinx.datetime.Instant
import kotlinx.datetime.toKotlinInstant
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.time.Duration

class SportsmanDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<SportsmanDAO>(SportsmanTable)

    var userId by SportsmanTable.userId
    var trainerId by SportsmanTable.trainerId
    var age by SportsmanTable.age
    var weight by SportsmanTable.weight
    var height by SportsmanTable.height
    var sex by SportsmanTable.sex

    // Related entities
    val user by UserDAO referencedOn SportsmanTable.userId
    val trainer by TrainerDAO optionalReferencedOn SportsmanTable.trainerId

    // Note: These are not directly mapped in the database structure provided
    // You may need to add these tables or retrieve this data differently
    val workouts by WorkoutDAO referrersOn WorkoutsTable.sportsman
    val allergies by AllergyDAO.via(SportsmanAllergiesTable.sportsman, SportsmanAllergiesTable.allergy)
    val diet by DietDAO referencedOn DietTable.sportsmanId

    fun toModel(dao: SportsmanDAO = this): Sportsman = transaction {
        // Convert user
        val user = UserDAO(dao.userId).toModel()

        // Convert trainer if present
        val trainer = dao.trainer?.toModel()

        // Convert allergies
        val allergies = dao.allergies.map { it.name }

        // Convert workout if present
        val workout = dao.workouts.firstOrNull()?.let {
            Workout(
                name = it.name,
                description = it.description,
                initalDate = it.creationDate,
                duration = Duration.parse("${it.durationWeeks * 7}d"),
                exercises = mapOf(), // Would need to be populated from WorkoutExercises table
                notes = emptyList()
            )
        }

        // Convert diet if present
        val diet = dao.diet.let {
            Diet(
                name = it.name,
                initialDate = it.initialDate,
                duration = Duration.parse("${it.duration}d"),
                description = it.description ?: "",
                meals = mapOf(),
                notes = listOf(),
            )
        }

        // Convert supplements
        // This is more complex because we need to group by WeekDay
        /*val supplements = dao.supplementation.groupBy {
            WeekDay.valueOf(it.weekDay.name)
        }.mapValues { (_, sups) ->
            sups.map { supDAO ->
                Suplemment(
                    name = supDAO.name,
                    description = supDAO.description ?: "",
                    ammount: supDao. ammount,
                notes:listOf()
                )
            }
        }*/


        // Create Sportsman entity
        Sportsman(
            user = user,
            trainer = trainer,
            age = dao.age,
            weight = dao.weight,
            height = dao.height,
            sex = dao.sex,
            allergies = allergies,
            workouts = workout,
            diet = diet,
        )
    }
}

