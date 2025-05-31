package es.gaspardev.database.daos

import es.gaspardev.core.domain.entities.workouts.WorkoutPlan
import es.gaspardev.core.domain.entities.workouts.CompletionWorkoutStatistic
import es.gaspardev.core.domain.entities.workouts.Workout
import es.gaspardev.core.domain.entities.workouts.WorkoutTemplate
import es.gaspardev.database.Workouts
import es.gaspardev.database.entities.*
import es.gaspardev.enums.Difficulty
import es.gaspardev.enums.WeekDay
import es.gaspardev.enums.WorkoutType
import kotlinx.datetime.Instant
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.time.Duration


class WorkoutDao {
    fun createWorkout(
        name: String,
        description: String,
        difficulty: Difficulty = Difficulty.EASY,
        duration: Duration,
        workoutType: WorkoutType,
        createdBy: Int
    ): WorkoutEntity = transaction {
        WorkoutEntity.new {
            this.name = name
            this.description = description
            this.difficulty = difficulty
            this.duration = duration
            this.workoutType = workoutType
            this.createdBy = TrainerEntity.all().first { it.user.id.value == createdBy }
        }
    }

    fun findWorkoutById(id: Int): WorkoutEntity? = transaction {
        WorkoutEntity.findById(id)
    }

    fun getWorkouts(trainerID: String): List<Workout> = transaction {
        WorkoutEntity.all().filter { it ->
            it.athletes.any { it.trainer?.id?.value == trainerID.toInt() }
        }
    }.map { it.toModel() }

    fun getPlans(trainerID: String): List<WorkoutPlan> = transaction {
        WorkoutPlanEntity.all().filter { it ->
            it.athletes.any { it.trainer?.id?.value == trainerID.toInt() }
        }.map { it.toModel() }
    }

    fun getTemplates(trainerID: String): List<WorkoutTemplate> = transaction {
        WorkoutTemplateEntity.all().filter { it ->
            it.createdBy.user.id.value == trainerID.toInt()
        }.map { it.toModel() }
    }

    fun addExerciseToWorkout(
        workoutId: Int,
        exerciseId: Int,
        weekDay: WeekDay,
        reps: Int,
        sets: Int,
        isOptional: Boolean = false,
        parentExerciseId: Int? = null
    ): WorkoutExerciseEntity = transaction {
        WorkoutExerciseEntity.new {
            this.workout = WorkoutEntity[workoutId]
            this.exercise = ExerciseEntity[exerciseId]
            this.weekDay = weekDay
            this.reps = reps
            this.sets = sets
            this.isOptional = isOptional
            this.parentExercise = parentExerciseId?.let { WorkoutExerciseEntity[it] }
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
        trainerId: String
    ): List<CompletionWorkoutStatistic> = transaction {
        CompletionWorkoutStatisticEntity.all()
            .filter {
                it.completeAt in baseRange..upRange
                    &&
                    it.athlete.trainer?.user?.id?.value == trainerId.toInt()
            }
            .map { it.toModel() }
    }

}
