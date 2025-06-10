package es.gaspardev.database.daos

import es.gaspardev.core.domain.entities.Note
import es.gaspardev.core.domain.entities.workouts.*
import es.gaspardev.database.Workouts
import es.gaspardev.database.entities.*
import es.gaspardev.enums.Difficulty
import es.gaspardev.enums.WeekDay
import es.gaspardev.enums.WorkoutType
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.time.Duration


object WorkoutDao {
    fun createWorkout(
        name: String,
        description: String,
        difficulty: Difficulty = Difficulty.EASY,
        duration: Duration,
        workoutType: WorkoutType,
        createdBy: Int
    ): Workout = transaction {
        val result = WorkoutEntity.new {
            this.name = name
            this.description = description
            this.difficulty = difficulty
            this.duration = duration
            this.workoutType = workoutType
            this.createdBy = TrainerEntity.all().first { it.user.id.value == createdBy }
            this.startAt = Clock.System.now()
        }

        result.toModel()
    }

    fun findWorkoutById(id: Int): WorkoutEntity? = transaction {
        WorkoutEntity.findById(id)
    }

    fun getWorkouts(trainerID: Int): List<Workout> = transaction {
        WorkoutEntity.all().filter { it ->
            it.createdBy.user.id.value == trainerID
        }
    }.map { it.toModel() }

    fun getPlans(trainerID: Int): List<WorkoutPlan> = transaction {
        WorkoutPlanEntity.all().filter { it ->
            it.createdBy.user.id.value == trainerID
        }.map { it.toModel() }
    }

    fun getTemplates(trainerID: Int): List<WorkoutTemplate> = transaction {
        WorkoutTemplateEntity.all().filter { it ->
            it.createdBy.user.id.value == trainerID
        }.map { it.toModel() }
    }

    fun addExerciseToWorkout(
        workoutId: Int,
        exerciseId: Int,
        weekDay: WeekDay,
        reps: Int,
        sets: Int,
        isOptional: Boolean = false
    ): WorkoutExerciseEntity = transaction {
        WorkoutExerciseEntity.new {
            this.workout = WorkoutEntity[workoutId]
            this.exercise = ExerciseEntity[exerciseId]
            this.weekDay = weekDay
            this.reps = reps
            this.sets = sets
            this.isOptional = isOptional
        }
    }

    fun completeWorkout(workoutId: Int, athleteId: Int): CompletionWorkoutStatisticEntity = transaction {
        CompletionWorkoutStatisticEntity.new {
            this.workout = WorkoutEntity[workoutId]
            this.athlete = AthleteEntity[athleteId]
            this.completeAt = Instant.fromEpochMilliseconds(System.currentTimeMillis())
        }
    }

    fun getWorkoutsByType(workoutType: WorkoutType): List<WorkoutEntity> = transaction {
        WorkoutEntity.find { Workouts.workoutType eq workoutType }.toList()
    }

    fun getCompletedWorkoutsInTimeRange(
        baseRange: Instant,
        upRange: Instant,
        trainerId: Int
    ): List<CompletionWorkoutStatistic> = transaction {
        CompletionWorkoutStatisticEntity.all()
            .filter {
                it.completeAt in baseRange..upRange
                        &&
                        it.athlete.trainer?.user?.id?.value == trainerId
            }
            .map { it.toModel() }
    }


    fun deleteWorkout(workoutId: String) {
        transaction {
            val workout = WorkoutEntity.findById(workoutId.toInt())
            if (workout != null) {
                workout.delete()
            } else {
                throw NoSuchElementException("Workout with ID $workoutId not found")
            }
        }
    }

    fun createWorkoutTemplate(
        name: String,
        description: String,
        difficulty: Difficulty = Difficulty.EASY,
        workoutType: WorkoutType,
        createdBy: Int
    ): WorkoutTemplate = transaction {
        val result = WorkoutTemplateEntity.new {
            this.name = name
            this.description = description
            this.difficulty = difficulty
            this.workoutType = workoutType
            this.createdBy = TrainerEntity.all().first { it.user.id.value == createdBy }
        }

        result.toModel()
    }

    fun addWorkoutTemplateExercises(
        templateId: Int,
        exerciseId: Int,
        weekDay: WeekDay,
        reps: Int,
        sets: Int,
        isOptional: Boolean = false,
    ) = transaction {
        WorkoutTemplateExerciseEntity.new {
            this.templateId = WorkoutTemplateEntity[templateId].id
            this.weekDay = weekDay
            this.reps = reps
            this.sets = sets
            this.isOption = isOption
            this.exercise = ExerciseEntity.findById(exerciseId)!!
        }
    }

    fun deleteWorkoutTemplate(templateId: Int): Boolean = transaction {
        try {
            val template = WorkoutTemplateEntity.findById(templateId)
            if (template != null) {
                // Eliminar ejercicios relacionados del template
                template.exercises.forEach { it.delete() }
                // Eliminar el template
                template.delete()
                true
            } else {
                false
            }
        } catch (e: Exception) {
            throw e
        }
    }

    fun findWorkoutTemplateById(id: Int): WorkoutTemplateEntity? = transaction {
        WorkoutTemplateEntity.findById(id)
    }

}
