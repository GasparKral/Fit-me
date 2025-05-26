package es.gaspardev.db.mappings.workouts

import es.gaspardev.core.domain.entities.*
import es.gaspardev.db.*
import es.gaspardev.db.mappings.notes.NoteDao
import es.gaspardev.db.mappings.notes.NoteEntity
import es.gaspardev.db.mappings.users.*
import es.gaspardev.modules.endpoints.BaseDao
import kotlinx.datetime.Clock
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.ExperimentalTime


// ExerciseBase DAO
class ExerciseBaseDao : BaseDao<ExerciseBaseEntity, Int>(ExerciseBaseEntity) {
    fun toDomain(exercise: ExerciseBaseEntity, includeAuthor: Boolean = false): ExerciseBase {
        return ExerciseBase(
            id = exercise.id.value,
            name = exercise.name,
            bodyPart = exercise.bodyPart,
            description = exercise.description,
            author = if (includeAuthor) exercise.author?.let {
                TrainerDao().toDomain(
                    it,
                    includeSportsmen = false
                )
            } else null,
            video = exercise.video?.let { ResourceDao().toDomain(it) }
        )
    }
}


class ExerciseBaseEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ExerciseBaseEntity>(ExerciseBasesTable)

    var name by ExerciseBasesTable.name
    var bodyPart by ExerciseBasesTable.bodyPart
    var description by ExerciseBasesTable.description
    var author by TrainerEntity optionalReferencedOn ExerciseBasesTable.authorId
    var video by ResourceEntity optionalReferencedOn ExerciseBasesTable.videoResourceId
}


// Workout DAO
class WorkoutDao : BaseDao<WorkoutEntity, Int>(WorkoutEntity) {
    fun toDomain(workout: WorkoutEntity, includeAuthor: Boolean = false): Workout {
        return Workout(
            name = workout.name,
            description = workout.description,
            notes = workout.notes.map { NoteDao().toDomain(it) },
            exercises =
                workout.exercises.groupBy { it.day }
                    .mapValues { (_, exercises) ->
                        exercises.map {
                            WorkoutExerciseDao().toDomain(it, includeAuthor = includeAuthor)
                        }
                    },
            duration = workout.durationMillis?.milliseconds,
            initalDate = workout.initialDate
        )
    }
}


class WorkoutEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<WorkoutEntity>(WorkoutsTable)

    var sportsmanId by SportsmanEntity referencedOn WorkoutsTable.sportsmanId
    var name by WorkoutsTable.name
    var description by WorkoutsTable.description
    var durationMillis by WorkoutsTable.duration
    var initialDate by WorkoutsTable.initialDate

    val notes by NoteEntity via WorkoutsNotesTable
    val exercises by WorkoutExerciseEntity referrersOn WorkoutExercisesTable.workoutId
}

// WorkoutExercise DAO
class WorkoutExerciseDao : BaseDao<WorkoutExerciseEntity, Int>(WorkoutExerciseEntity) {
    fun toDomain(exercise: WorkoutExerciseEntity, includeAuthor: Boolean = false): WorkoutExercise {
        return WorkoutExercise(
            base = ExerciseBaseDao().toDomain(exercise.exerciseBaseId, includeAuthor = includeAuthor),
            reps = exercise.reps,
            sets = exercise.sets,
            note = exercise.notes.map { NoteDao().toDomain(it) },
            optionalExercises = exercise.alternatives.map {
                toDomain(it.alternativeExerciseId)
            }.toMutableList(),
            isOption = exercise.isOption
        )
    }
}


class WorkoutExerciseEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<WorkoutExerciseEntity>(WorkoutExercisesTable)

    var workoutId by WorkoutEntity referencedOn WorkoutExercisesTable.workoutId
    var exerciseBaseId by ExerciseBaseEntity referencedOn WorkoutExercisesTable.exerciseBaseId
    var day by WorkoutExercisesTable.day
    var reps by WorkoutExercisesTable.reps
    var sets by WorkoutExercisesTable.sets
    var notes by NoteEntity via WorkoutExerciseNotesTable
    var isOption by WorkoutExercisesTable.isOption

    val alternatives by ExerciseAlternativeEntity referrersOn ExerciseAlternativesTable.mainExerciseId
}

class ExerciseAlternativeEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ExerciseAlternativeEntity>(ExerciseAlternativesTable)

    var mainExerciseId by WorkoutExerciseEntity referencedOn ExerciseAlternativesTable.mainExerciseId
    var alternativeExerciseId by WorkoutExerciseEntity referencedOn ExerciseAlternativesTable.alternativeExerciseId
}

class CompletedWorkoutDao : BaseDao<CompletedWorkoutEntity, Int>(CompletedWorkoutEntity) {
    fun toDomain(entity: CompletedWorkoutEntity): CompletedWorkout {
        return CompletedWorkout(
            workout = WorkoutDao().toDomain(entity.workout),
            completedAt = entity.completedAt,
            notes = entity.notes.map { NoteDao().toDomain(it) }
        )
    }

    fun fromDomain(workout: WorkoutEntity, sportsman: SportsmanEntity): CompletedWorkoutEntity {
        return CompletedWorkoutEntity.new {
            this.workout = workout
            this.sportsman = sportsman
            completedAt = Clock.System.now()
            this.notes = notes
        }
    }
}

class CompletedWorkoutEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<CompletedWorkoutEntity>(CompletedWorkoutsTable)

    var workout by WorkoutEntity referencedOn CompletedWorkoutsTable.workout
    var sportsman by SportsmanEntity referencedOn CompletedWorkoutsTable.sportsman
    var completedAt by CompletedWorkoutsTable.completedAt
    var notes by NoteEntity via CompletedWorkoutNotesTable
}