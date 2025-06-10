package es.gaspardev.database.entities

import es.gaspardev.core.domain.entities.workouts.*
import es.gaspardev.database.*
import es.gaspardev.enums.WeekDay
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class ExerciseEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ExerciseEntity>(Exercises)

    var name by Exercises.name
    var description by Exercises.description
    var bodyPart by Exercises.bodyPart

    fun toModel(): Exercise {
        return Exercise(
            id = this.id.value,
            name = this.name,
            description = this.description,
            bodyPart = this.bodyPart
        )
    }
}

class WorkoutEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<WorkoutEntity>(Workouts)

    var name by Workouts.name
    var description by Workouts.description
    var difficulty by Workouts.difficulty
    var duration by Workouts.duration
    var workoutType by Workouts.workoutType
    var createdBy by TrainerEntity referencedOn Workouts.createdBy
    var startAt by Workouts.startUp

    // Relaciones
    val exercises by WorkoutExerciseEntity referrersOn WorkoutExercises.workoutId

    fun toModel(): Workout {
        return Workout(
            id = this.id.value,
            name = this.name,
            description = this.description,
            difficulty = this.difficulty,
            duration = this.duration,
            workoutType = this.workoutType,
            exercises = this.exercises.map { it.toModel() }.groupBy({ it.first }, { it.second })
                .mapValues { it.value.toMutableList() }.toMutableMap(),
            startAt = this.startAt
        )
    }
}

class WorkoutExerciseEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<WorkoutExerciseEntity>(WorkoutExercises)

    var workout by WorkoutEntity referencedOn WorkoutExercises.workoutId
    var exercise by ExerciseEntity referencedOn WorkoutExercises.exerciseId
    var weekDay by WorkoutExercises.weekDay
    var reps by WorkoutExercises.reps
    var sets by WorkoutExercises.sets
    var isOptional by WorkoutExercises.isOptional
    var parentExercise by WorkoutExerciseEntity optionalReferencedOn WorkoutExercises.parentExerciseId

    fun toModel(): Pair<WeekDay, WorkoutExecise> {
        return Pair(
            this.weekDay,
            WorkoutExecise(
                reps = this.reps,
                sets = this.sets,
                isOption = this.isOptional,
                exercise = this.exercise.toModel()
            )
        )
    }
}

class WorkoutTemplateEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<WorkoutTemplateEntity>(WorkoutTemplates)

    var name by WorkoutTemplates.name
    var description by WorkoutTemplates.description
    var difficulty by WorkoutTemplates.difficulty
    var workoutType by WorkoutTemplates.workoutType
    var createdBy by TrainerEntity referencedOn WorkoutTemplates.createdBy

    // Relaciones
    val exercises by WorkoutTemplateExerciseEntity referrersOn WorkoutTemplateExercises.templateId

    fun toModel(): WorkoutTemplate {
        return WorkoutTemplate(
            templateId = this.id.value,
            name = this.name,
            description = this.description,
            difficulty = this.difficulty,
            workoutType = this.workoutType,
            exercises = this.exercises.map { it.toModel() }.groupBy({ it.first }, { it.second })
                .mapValues { it.value.toMutableList() }.toMutableMap()
        )
    }
}

class WorkoutPlanEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<WorkoutPlanEntity>(Workouts)

    var name by Workouts.name
    var description by Workouts.description
    var difficulty by Workouts.difficulty
    var duration by Workouts.duration
    var workoutType by Workouts.workoutType
    var createdBy by TrainerEntity referencedOn Workouts.createdBy

    // Relaciones
    val exercises by WorkoutExerciseEntity referrersOn WorkoutExercises.workoutId
    val athletes by AthleteEntity optionalReferrersOn Athletes.workoutId

    fun toModel(): WorkoutPlan {
        return WorkoutPlan(
            workoutId = this.id.value,
            name = this.name,
            description = this.description,
            difficulty = this.difficulty,
            duration = this.duration,
            type = this.workoutType,
            exercises = this.exercises.map { it.toModel() }.groupBy({ it.first }, { it.second })
                .mapValues { it.value.toMutableList() }.toMutableMap(),
            frequency = this.exercises.map { it.toModel() }.groupBy({ it.first }, { it.second }).keys.count()
                .toString(),
            asignedCount = athletes.count().toInt(),
        )
    }
}

class WorkoutTemplateExerciseEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<WorkoutTemplateExerciseEntity>(WorkoutTemplateExercises)

    var templateId by WorkoutTemplateExercises.templateId
    var exercise by ExerciseEntity referencedOn WorkoutTemplateExercises.exerciseId
    var weekDay by WorkoutTemplateExercises.weekDay
    var reps by WorkoutTemplateExercises.reps
    var sets by WorkoutTemplateExercises.sets
    var isOption by WorkoutTemplateExercises.isOptional

    fun toModel(): Pair<WeekDay, WorkoutExecise> {
        return Pair(
            weekDay,
            WorkoutExecise(
                reps = this.reps,
                sets = this.sets,
                isOption = this.isOption,
                exercise = this.exercise.toModel()
            )
        )
    }
}

class CompletionWorkoutStatisticEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<CompletionWorkoutStatisticEntity>(CompletionWorkoutStatistics)

    var workout by WorkoutEntity referencedOn CompletionWorkoutStatistics.workoutId
    var athlete by AthleteEntity referencedOn CompletionWorkoutStatistics.athleteId
    var completeAt by CompletionWorkoutStatistics.completeAt

    fun toModel(): CompletionWorkoutStatistic {
        return CompletionWorkoutStatistic(
            workout = this.workout.toModel(),
            completeAt = this.completeAt,
            assignedAthlete = this.athlete.toModel()
        )
    }

}