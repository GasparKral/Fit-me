package es.gaspardev.database

import es.gaspardev.enums.*
import kotlinx.datetime.Instant
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestamp
import kotlin.time.DurationUnit
import kotlin.time.toDuration


// ============================================================================
// TABLAS DE USUARIOS
// ============================================================================

object Users : IntIdTable("users") {
    val fullname = varchar("fullname", 255)
    val password = varchar("password", 255)
    val email = varchar("email", 255).uniqueIndex()
    val phone = varchar("phone", 50)
    val creationDate = timestamp("creation_date").transform(
        wrap = { Instant.fromEpochMilliseconds(it.toEpochMilli()) },
        unwrap = { java.time.Instant.ofEpochMilli(it.toEpochMilliseconds()) })
    val userImageUrl = varchar("user_image_url", 500).nullable()
}

object UserStatus : IntIdTable("user_status") {
    val state = enumeration("state", StatusState::class)
    val lastTimeActive = timestamp("last_time_active").transform(
        wrap = { Instant.fromEpochMilliseconds(it.toEpochMilli()) },
        unwrap = { java.time.Instant.ofEpochMilli(it.toEpochMilliseconds()) })
    val userId = reference("user_id", Users.id)
}

object Trainers : IntIdTable("trainers") {
    val specialization = varchar("specialization", 255)
    val yearsOfExperience = integer("years_of_experience")
    val userId = reference("user_id", Users.id)
}

object Athletes : IntIdTable("athletes") {
    val age = integer("age")
    val sex = enumeration("sex", Sex::class)
    val trainingSince = timestamp("training_since").transform(
        wrap = { Instant.fromEpochMilliseconds(it.toEpochMilli()) },
        unwrap = { java.time.Instant.ofEpochMilli(it.toEpochMilliseconds()) })
    val workoutId = optReference("workout_id", Workouts)
    val dietId = optReference("diet_id", Diets)
    val trainer = optReference("trainer_id", Trainers.id)
    val needAssistant = bool("need_assistant")
    val userId = reference("user_id", Users.id)
}

// ============================================================================
// TABLAS DE INFORMACIÓN DE USUARIOS
// ============================================================================

object Allergies : IntIdTable("allergies") {
    val name = varchar("name", 255)
}

object AthleteAllergies : Table("athlete_allergies") {
    val athleteId = reference("athlete_id", Athletes.id)
    val allergyId = reference("allergy_id", Allergies)

    override val primaryKey = PrimaryKey(athleteId, allergyId)
}

object Measurements : IntIdTable("measurements") {
    val athleteId = reference("athlete_id", Athletes)
    val weight = double("weight").default(0.0)
    val height = double("height").default(0.0)
    val bodyFat = double("body_fat").default(0.0)
    val armSize = double("arm_size").default(0.0)
    val chestBackSize = double("chest_back_size").default(0.0)
    val hipSize = double("hip_size").default(0.0)
    val legSize = double("leg_size").default(0.0)
    val calvesSize = double("calves_size").default(0.0)
    val measureAt = timestamp("measure_at").transform(
        wrap = { Instant.fromEpochMilliseconds(it.toEpochMilli()) },
        unwrap = { java.time.Instant.ofEpochMilli(it.toEpochMilliseconds()) })
    val isCurrent = bool("is_current").default(true)
}

object Certifications : IntIdTable("certifications") {
    val trainerId = reference("trainer_id", Trainers.id)
    val name = varchar("name", 255)
    val issuingOrganization = varchar("issuing_organization", 255)
    val completeAt = timestamp("complete_at").transform(
        wrap = { Instant.fromEpochMilliseconds(it.toEpochMilli()) },
        unwrap = { java.time.Instant.ofEpochMilli(it.toEpochMilliseconds()) })
}

object TrainerSocials : IntIdTable("trainer_socials") {
    val trainerId = reference("trainer_id", Trainers.id)
    val platform = varchar("platform", 50)
    val url = varchar("url", 500)

    // Índice único para evitar duplicados
    init {
        uniqueIndex(trainerId, platform)
    }
}


object TrainerAvailability : IntIdTable("trainer_availability") {
    val trainerId = reference("trainer_id", Trainers.id)
    val weekDay = enumeration("week_day", WeekDay::class)
    val startTime = timestamp("start_time").transform(
        wrap = { Instant.fromEpochMilliseconds(it.toEpochMilli()) },
        unwrap = { java.time.Instant.ofEpochMilli(it.toEpochMilliseconds()) })
    val endTime = timestamp("end_time").transform(
        wrap = { Instant.fromEpochMilliseconds(it.toEpochMilli()) },
        unwrap = { java.time.Instant.ofEpochMilli(it.toEpochMilliseconds()) })
}

// ============================================================================
// TABLAS DE EJERCICIOS Y ENTRENAMIENTOS
// ============================================================================

object Exercises : IntIdTable("exercises") {
    val name = varchar("name", 255)
    val description = text("description")
    val bodyPart = enumeration("body_part", BodyPart::class)
}

object Workouts : IntIdTable("workouts") {
    val name = varchar("name", 255)
    val description = text("description")
    val difficulty = enumeration("difficulty", Difficulty::class).default(Difficulty.EASY)
    val duration = long("duration_minutes").transform(
        wrap = { it.toDuration(DurationUnit.MINUTES) },
        unwrap = { it.toLong(DurationUnit.MINUTES) }
    )
    val workoutType = enumeration("workout_type", WorkoutType::class)
    val createdBy = reference("created_by", Trainers)
    val startUp = timestamp("start_up").transform(
        wrap = { Instant.fromEpochMilliseconds(it.toEpochMilli()) },
        unwrap = { java.time.Instant.ofEpochMilli(it.toEpochMilliseconds()) }
    )
}

object WorkoutExercises : IntIdTable("workout_exercises") {
    val workoutId = reference("workout_id", Workouts)
    val exerciseId = reference("exercise_id", Exercises)
    val weekDay = enumeration("week_day", WeekDay::class)
    val reps = integer("reps")
    val sets = integer("sets")
    val isOptional = bool("is_optional").default(false)
    val parentExerciseId = reference("parent_exercise_id", WorkoutExercises).nullable()
}

object WorkoutTemplates : IntIdTable("workout_templates") {
    val name = varchar("name", 255)
    val description = text("description")
    val difficulty = enumeration("difficulty", Difficulty::class).default(Difficulty.EASY)
    val workoutType = enumeration("workout_type", WorkoutType::class)
    val createdBy = reference("created_by", Trainers)
}

object WorkoutTemplateExercises : IntIdTable("workout_template_exercises") {
    val templateId = reference("template_id", WorkoutTemplates)
    val exerciseId = reference("exercise_id", Exercises)
    val weekDay = enumeration("week_day", WeekDay::class)

    val reps = integer("reps")
    val sets = integer("sets")
    val isOptional = bool("is_optional").default(false)
    val parentExerciseId = reference("parent_exercise_id", WorkoutExercises).nullable()
}

object CompletionWorkoutStatistics : IntIdTable("completion_workout_statistics") {
    val workoutId = reference("workout_id", Workouts)
    val athleteId = reference("athlete_id", Athletes)
    val completeAt = timestamp("complete_at").transform(
        wrap = { Instant.fromEpochMilliseconds(it.toEpochMilli()) },
        unwrap = { java.time.Instant.ofEpochMilli(it.toEpochMilliseconds()) })
}

// ============================================================================
// TABLAS DE DIETAS Y COMIDAS
// ============================================================================

object Dishes : IntIdTable("dishes") {
    val name = varchar("name", 255)
}

object Diets : IntIdTable("diets") {
    val name = varchar("name", 255)
    val description = text("description")
    val dietType = enumeration("diet_type", DietType::class)
    val duration = long("duration_days").transform(
        wrap = { it.toDuration(DurationUnit.DAYS) },
        unwrap = { it.toLong(DurationUnit.DAYS) }
    )
    val createdBy = reference("created_by", Trainers)
    val startAt = timestamp("start_at").transform(
        wrap = { Instant.fromEpochMilliseconds(it.toEpochMilli()) },
        unwrap = { java.time.Instant.ofEpochMilli(it.toEpochMilliseconds()) }
    )
}

object DietDishes : IntIdTable("diet_dishes") {
    val dietId = reference("diet_id", Diets)
    val dishId = reference("dish_id", Dishes)
    val weekDay = enumeration("week_day", WeekDay::class)
    val amount = double("amount")
    val mealType = enumeration("meal_type", MealType::class)
}

object DietTemplates : IntIdTable("diet_templates") {
    val name = varchar("name", 255)
    val description = text("description")
    val dietType = enumeration("diet_type", DietType::class)
    val createdBy = reference("created_by", Trainers.id)
}

object DietTemplateDishes : IntIdTable("diet_template_dishes") {
    val templateId = reference("template_id", DietTemplates)
    val dishId = reference("dish_id", Dishes)
    val weekDay = enumeration("week_day", WeekDay::class)
    val amount = double("amount")
    val mealType = enumeration("meal_type", MealType::class)
}

object CompletionDietStatistics : IntIdTable("completion_diet_statistics") {
    val dietId = reference("diet_id", Diets)
    val athleteId = reference("athlete_id", Athletes)
    val completeAt = timestamp("complete_at").transform(
        wrap = { Instant.fromEpochMilliseconds(it.toEpochMilli()) },
        unwrap = { java.time.Instant.ofEpochMilli(it.toEpochMilliseconds()) })
}

// ============================================================================
// TABLAS DE COMUNICACIÓN
// ============================================================================

object Conversations : IntIdTable("conversations") {
    val trainerId = reference("trainer_id", Users)
    val athleteId = reference("athlete_id", Users)
    val createdAt = timestamp("created_at").transform(
        wrap = { Instant.fromEpochMilliseconds(it.toEpochMilli()) },
        unwrap = { java.time.Instant.ofEpochMilli(it.toEpochMilliseconds()) })

    init {
        uniqueIndex(trainerId, athleteId)
    }
}

object Messages : IntIdTable("messages") {
    val conversationId = reference("conversation_id", Conversations)
    val userName = varchar("user_name", 255)
    val sentAt = timestamp("sent_at").transform(
        wrap = { Instant.fromEpochMilliseconds(it.toEpochMilli()) },
        unwrap = { java.time.Instant.ofEpochMilli(it.toEpochMilliseconds()) })
    val content = text("content")
    val status = enumeration("status", MessageStatus::class).default(MessageStatus.SENT)
}

object Sessions : IntIdTable("sessions") {
    val title = varchar("title", 255)
    val dateTime = timestamp("date_time").transform(
        wrap = { Instant.fromEpochMilliseconds(it.toEpochMilli()) },
        unwrap = { java.time.Instant.ofEpochMilli(it.toEpochMilliseconds()) })
    val sessionType = enumeration("session_type", SessionType::class)
    val trainerId = reference("trainer_id", Trainers)
    val athleteId = reference("athlete_id", Athletes)
    val expectedDuration = long("expected_duration_minutes").transform(
        wrap = { it.toDuration(DurationUnit.MINUTES) },
        unwrap = { it.toLong(DurationUnit.MINUTES) }
    )
    val actualDuration = long("actual_duration_minutes").nullable().transform(
        wrap = { it?.toDuration(DurationUnit.MINUTES) },
        unwrap = { it?.toLong(DurationUnit.MINUTES) }
    )
    val completed = bool("completed").default(false)
}

// ============================================================================
// TABLA DE NOTAS
// ============================================================================

object Notes : IntIdTable("notes") {
    val userId = reference("user_id", Users)
    val content = text("content")
    val createdAt = timestamp("created_at").transform(
        wrap = { Instant.fromEpochMilliseconds(it.toEpochMilli()) },
        unwrap = { java.time.Instant.ofEpochMilli(it.toEpochMilliseconds()) })
}

// Tablas intermedias para asociar notas con diferentes entidades

object WorkoutNotes : Table("workout_notes") {
    val workoutId = reference("workout_id", Workouts)
    val noteId = reference("note_id", Notes)
    override val primaryKey = PrimaryKey(workoutId, noteId)
}

object WorkoutExerciseNotes : Table("workout_exercise_notes") {
    val workoutExerciseId = reference("workout_exercise_id", WorkoutExercises)
    val noteId = reference("note_id", Notes)
    override val primaryKey = PrimaryKey(workoutExerciseId, noteId)
}

object DietNotes : Table("diet_notes") {
    val dietId = reference("diet_id", Diets)
    val noteId = reference("note_id", Notes)
    override val primaryKey = PrimaryKey(dietId, noteId)
}

object DietDishNotes : Table("diet_dish_notes") {
    val dietDishId = reference("diet_dish_id", DietDishes)
    val noteId = reference("note_id", Notes)
    override val primaryKey = PrimaryKey(dietDishId, noteId)
}

object SessionNotes : Table("session_notes") {
    val sessionId = reference("session_id", Sessions)
    val noteId = reference("note_id", Notes)
    override val primaryKey = PrimaryKey(sessionId, noteId)
}

// ============================================================================
// TABLA CLAVES DE ACCESO
// ============================================================================

object RegistKeyTable : IntIdTable("key_gen") {
    val key = varchar("key", 250)
    val trainer = reference("trainer_id", Trainers)
}

object DatabaseConfig {
    val allTables = arrayOf(
        Users,
        UserStatus,
        Trainers,
        Athletes,
        Allergies,
        AthleteAllergies,
        Measurements,
        Certifications,
        TrainerSocials,
        TrainerAvailability,
        Exercises,
        Workouts,
        WorkoutExercises,
        WorkoutTemplates,
        WorkoutTemplateExercises,
        CompletionWorkoutStatistics,
        Dishes,
        Diets,
        DietDishes,
        DietTemplates,
        DietTemplateDishes,
        CompletionDietStatistics,
        Conversations,
        Messages,
        Sessions,
        Notes,
        WorkoutNotes,
        WorkoutExerciseNotes,
        DietNotes,
        DietDishNotes,
        SessionNotes,
        RegistKeyTable
    )
}