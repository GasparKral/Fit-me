package es.gaspardev.database.daos

import es.gaspardev.core.domain.entities.Note
import es.gaspardev.core.domain.entities.workouts.*
import es.gaspardev.database.WorkoutExercises
import es.gaspardev.database.Workouts
import es.gaspardev.database.entities.*
import es.gaspardev.enums.Difficulty
import es.gaspardev.enums.WeekDay
import es.gaspardev.enums.WorkoutType
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.jetbrains.exposed.sql.SizedCollection
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
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

    fun getWorkouts(trainerID: String): List<Workout> = transaction {
        WorkoutEntity.all().filter { it ->
            it.createdBy.user.id.value == trainerID.toInt()
        }
    }.map { it.toModel() }

    fun getPlans(trainerID: String): List<WorkoutPlan> = transaction {
        WorkoutPlanEntity.all().filter { it ->
            it.createdBy.user.id.value == trainerID.toInt()
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

    fun updateWorkout(newValues: WorkoutPlan) = transaction {
        val workoutEntity = WorkoutEntity.findById(newValues.workoutId)
            ?: throw IllegalArgumentException("Workout not found")

        // Actualizar campos básicos
        workoutEntity.apply {
            name = newValues.name
            description = newValues.description
            workoutType = newValues.type
            duration = Duration.parse(newValues.duration)
            difficulty = newValues.difficulty
        }

        // Gestionar ejercicios
        updateWorkoutExercises(workoutEntity, newValues.exercises)
    }

    private fun updateWorkoutExercises(
        workoutEntity: WorkoutEntity,
        newExercises: Map<WeekDay, List<WorkoutExecise>>
    ) {
        // Obtener ejercicios actuales
        val currentExercises = workoutEntity.exercises.toList()

        // Crear un mapa de los nuevos ejercicios para comparación
        val newExercisesFlat = newExercises.flatMap { (weekDay, exercises) ->
            exercises.map { exercise -> Triple(weekDay, exercise.exercise.id, exercise) }
        }

        // Eliminar ejercicios que ya no están en la nueva lista
        currentExercises.forEach { current ->
            val exists = newExercisesFlat.any { (weekDay, exerciseId, _) ->
                current.weekDay == weekDay && current.exercise.id.value == exerciseId
            }
            if (!exists) {
                current.delete()
            }
        }

        // Actualizar o crear ejercicios
        newExercisesFlat.forEach { (weekDay, exerciseId, exercise) ->
            val existing = currentExercises.find {
                it.weekDay == weekDay && it.exercise.id.value == exerciseId
            }

            if (existing != null) {
                // Actualizar existente
                existing.apply {
                    reps = exercise.reps
                    sets = exercise.sets
                    isOptional = exercise.isOptional()
                }
                updateExerciseNotes(existing, exercise.notes)
            } else {
                // Crear nuevo
                val newEntity = WorkoutExerciseEntity.new {
                    workout = workoutEntity
                    this.weekDay = weekDay
                    reps = exercise.reps
                    sets = exercise.sets
                    isOptional = exercise.isOptional()
                    this.exercise = ExerciseEntity.findById(exerciseId)
                        ?: throw IllegalArgumentException("Exercise not found: $exerciseId")
                }
                updateExerciseNotes(newEntity, exercise.notes)
            }
        }
    }

    private fun updateExerciseNotes(exerciseEntity: WorkoutExerciseEntity, newNotes: List<Note>) {
        // Limpiar notas actuales
        exerciseEntity.notes = SizedCollection(emptyList())

        // Agregar nuevas notas
        newNotes.forEach { note ->
            val noteEntity = NoteEntity.findById(note.id)
                ?: NoteEntity.new {
                    content = note.content
                }
            exerciseEntity.notes = SizedCollection(exerciseEntity.notes + noteEntity)
        }
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

}
