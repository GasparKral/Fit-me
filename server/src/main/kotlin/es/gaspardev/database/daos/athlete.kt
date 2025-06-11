package es.gaspardev.database.daos

import es.gaspardev.core.domain.entities.diets.CompletionDietStatistics
import es.gaspardev.core.domain.entities.workouts.CompletionWorkoutStatistic
import es.gaspardev.database.Measurements
import es.gaspardev.database.entities.CompletionDietStatisticEntity
import es.gaspardev.database.entities.DietEntity
import es.gaspardev.database.entities.*
import es.gaspardev.database.entities.CompletionWorkoutStatisticEntity
import es.gaspardev.enums.Sex
import kotlinx.datetime.Instant
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction

object AthleteDao {
    fun createAthlete(
        userId: Int,
        age: Int,
        sex: Sex,
        trainingSince: Instant
    ): AthleteEntity = transaction {
        AthleteEntity.new(userId) {
            this.userEntity = UserEntity[userId]
            this.age = age
            this.sex = sex
            this.trainingSince = trainingSince
        }
    }

    /*
     *
     *  DATA
     *
     */
    fun getWorkoutHistory(userId: Int): List<CompletionWorkoutStatistic> = transaction {
        CompletionWorkoutStatisticEntity.all().filter { it.athlete.userEntity.id.value == userId }.map { it.toModel() }
    }

    fun getDietHistory(userId: Int): List<CompletionDietStatistics> = transaction {
        CompletionDietStatisticEntity.all().filter { it.athlete.userEntity.id.value == userId }.map { it.toModel() }
    }

    
    fun findAthleteByUserId(userId: Int): AthleteEntity? = transaction {
        AthleteEntity.findById(userId)
    }

    fun assignWorkout(athleteId: Int, workoutId: Int): AthleteEntity? = transaction {
        AthleteEntity.findById(athleteId)?.apply {
            this.workout = WorkoutEntity[workoutId]
        }
    }

    fun assignDiet(athleteId: Int, dietId: Int): AthleteEntity? = transaction {
        AthleteEntity.findById(athleteId)?.apply {
            this.diet = DietEntity[dietId]
        }
    }

    fun assignTrainer(athleteId: Int, trainerId: Int): AthleteEntity? = transaction {
        AthleteEntity.findById(athleteId)?.apply {
            this.trainer = TrainerEntity[trainerId]
        }
    }

    fun addMeasurement(
        athleteId: Int,
        weight: Double = 0.0,
        height: Double = 0.0,
        bodyFat: Double = 0.0,
        armSize: Double = 0.0,
        chestBackSize: Double = 0.0,
        hipSize: Double = 0.0,
        legSize: Double = 0.0,
        calvesSize: Double = 0.0
    ): MeasurementEntity = transaction {
        // Marcar mediciones anteriores como no actuales
        MeasurementEntity.find { Measurements.athleteId eq athleteId }
            .forEach { it.isCurrent = false }

        MeasurementEntity.new {
            this.athlete = AthleteEntity[athleteId]
            this.weight = weight
            this.height = height
            this.bodyFat = bodyFat
            this.armSize = armSize
            this.chestBackSize = chestBackSize
            this.hipSize = hipSize
            this.legSize = legSize
            this.calvesSize = calvesSize
            this.measureAt = Instant.fromEpochMilliseconds(System.currentTimeMillis())
            this.isCurrent = true
        }
    }

    fun getCurrentMeasurement(athleteId: Int): MeasurementEntity? = transaction {
        MeasurementEntity.find {
            (Measurements.athleteId eq athleteId) and (Measurements.isCurrent eq true)
        }.singleOrNull()
    }

    fun needAssistant(trainerID: Int): Int = transaction {
        AthleteEntity.all().count {
            it.trainer?.id?.value == trainerID && it.needAssistant
        }
    }


}