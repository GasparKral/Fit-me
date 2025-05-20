package es.gaspardev.db

import es.gaspardev.enums.BodyPart
import es.gaspardev.enums.MediaType
import es.gaspardev.enums.WeekDay
import kotlinx.datetime.Instant
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestamp
import org.postgresql.util.PGobject

class PGEnum<T : Enum<T>>(enumTypeName: String, enumValue: T?) : PGobject() {
    init {
        value = enumValue?.name
        type = enumTypeName
    }
}

object UserTable : IntIdTable("user") {
    val name = varchar("name", 50)
    val password = varchar("password", 250)
    val email = varchar("email", 250)
    val creationTime = timestamp("creationTime").transform(
        wrap = { Instant.fromEpochMilliseconds(it.toEpochMilli()) },
        unwrap = { java.time.Instant.ofEpochMilli(it.toEpochMilliseconds()) })
    val userImage = optReference("userImage", ResourceTable.id, ReferenceOption.SET_NULL, ReferenceOption.CASCADE)
}

object SocialLinksTable : IntIdTable("socials") {
    val trainerId = reference(
        "trainer_id",
        TrainerTable.id,
        ReferenceOption.CASCADE,
        ReferenceOption.CASCADE
    )
    val socialMedia = varchar("media", 25)
    val link = varchar("links", 150)
}

object CertificationTable : IntIdTable("certifications") {
    val name = varchar("name", 50)
    val issuinOrganization = varchar("issuin_organization", 75)
    val optainedDate = timestamp("optainedDate").transform(
        wrap = { Instant.fromEpochMilliseconds(it.toEpochMilli()) },
        unwrap = { java.time.Instant.ofEpochMilli(it.toEpochMilliseconds()) })
}

object TrainerTable : IntIdTable("trainer") {
    val userId = reference(
        "user_id",
        UserTable.id,
        ReferenceOption.CASCADE,
        ReferenceOption.CASCADE
    )
    val specialization = varchar("specialization", 50)
    val years_of_experience = integer("years_of_experience")
    val bio = text("bio")
    val certification =
        array<Int>("certifications", Int.MAX_VALUE) // guarda la referencia al indice de la tabla Certifications
}

object SportsmanTable : IntIdTable("sportsmans") {
    val userId = reference(
        "user_id",
        UserTable.id,
        ReferenceOption.CASCADE,
        ReferenceOption.CASCADE
    )
    val trainerId =
        optReference(
            "trainer_id",
            TrainerTable.id,
            onDelete = ReferenceOption.SET_NULL,
            onUpdate = ReferenceOption.CASCADE
        )
    val age = integer("age")
    val weight = double("weight").nullable()
    val height = double("height").nullable()
    val sex = bool("sex") // 1 Hombre 0 Mujer
}

object ResourceTable : IntIdTable("resources") {
    val type = customEnumeration(
        "type", "MediaType",
        { value -> MediaType.valueOf(value as String) },
        { PGEnum<MediaType>("MediaType", it) }
    )
    val path = varchar("path", 255)
}

object NoteTable : IntIdTable("notes") {
    val user = reference("user_id", UserTable.id, ReferenceOption.CASCADE, ReferenceOption.CASCADE)
    val message = mediumText("message")
}

object ExercisesTable : IntIdTable("exercises") {
    val name = varchar("name", 200)
    val bodyPart = customEnumeration(
        "body_part", "BodyPart",
        { value -> BodyPart.valueOf(value as String) },
        { PGEnum<BodyPart>("BodyPart", it) }
    )
    val reps = integer("reps")
    val sets = integer("sets")
    val description = mediumText("description")
    val author = optReference("author", TrainerTable.id, ReferenceOption.SET_NULL, ReferenceOption.CASCADE)
    val video = optReference("video", ResourceTable.id, ReferenceOption.SET_NULL, ReferenceOption.CASCADE)
}

object ExerciseOptionalLinks : Table("exercise_optional_links") {
    val exercise = reference("exercise_id", ExercisesTable, onDelete = ReferenceOption.CASCADE)
    val optional = reference("optional_id", ExercisesTable, onDelete = ReferenceOption.CASCADE)

    override val primaryKey = PrimaryKey(exercise, optional, name = "PK_Exercise_Optional")
}

object ChatTable : IntIdTable("chat") {
    val traine = reference("trainer_id", TrainerTable.id, ReferenceOption.SET_NULL, ReferenceOption.CASCADE)
    val sportsman = reference("sportsman_id", SportsmanTable.id, ReferenceOption.SET_NULL, ReferenceOption.CASCADE)
}

object MessagesTable : IntIdTable("messages") {
    val chat = reference("chat_id", ChatTable.id, ReferenceOption.CASCADE, ReferenceOption.CASCADE)
    val user = reference("user_id", UserTable.id, ReferenceOption.CASCADE, ReferenceOption.CASCADE)
    val message = varchar("message_text", 250)
    val messageType = customEnumeration(
        "message_type",
        "MessageType",
        { value -> MediaType.valueOf(value as String) },
        { PGEnum<MediaType>("MediaType", it) })
}

// Completar la tabla de Workouts
object WorkoutsTable : IntIdTable("workouts") {
    val name = varchar("name", 100)
    val description = text("description").nullable()
    val sportsman = reference("sportsman_id", SportsmanTable.id, ReferenceOption.CASCADE, ReferenceOption.CASCADE)
    val author = reference("trainer_id", TrainerTable.id, ReferenceOption.CASCADE, ReferenceOption.CASCADE)
    val requiresAssistance = bool("requires_assistance").default(false)
    val creationDate = timestamp("creation_date").transform(
        wrap = { Instant.fromEpochMilliseconds(it.toEpochMilli()) },
        unwrap = { java.time.Instant.ofEpochMilli(it.toEpochMilliseconds()) })
    val durationWeeks = integer("duration_weeks")
}

// Tabla para los ejercicios de una rutina, organizados por día
object WorkoutExercises : IntIdTable("workout_exercises") {
    val workout = reference("workout_id", WorkoutsTable.id, ReferenceOption.CASCADE, ReferenceOption.CASCADE)
    val exercise = reference("exercise_id", ExercisesTable.id, ReferenceOption.CASCADE, ReferenceOption.CASCADE)
    val weekDay = customEnumeration(
        "week_day", "WeekDay",
        { value -> WeekDay.valueOf(value as String) },
        { PGEnum<WeekDay>("WeekDay", it) }
    )
    val order = integer("exercise_order") // orden de ejecución dentro del día
    val reps = integer("reps")
    val sets = integer("sets")
    val note = optReference("note_id", NoteTable.id, ReferenceOption.SET_NULL, ReferenceOption.CASCADE)
    val isOption = bool("is_option").default(false) // Indica si es un ejercicio opcional
    val parentExercise = optReference(
        "parent_exercise",
        id,
        ReferenceOption.CASCADE,
        ReferenceOption.CASCADE
    )

    // Índice para buscar ejercicios por rutina y día
    init {
        index(true, workout, weekDay, order)
    }
}

// Tabla para registrar el progreso del deportista con sus rutinas
object WorkoutProgress : IntIdTable("workout_progress") {
    val workout = reference("workout_id", WorkoutsTable.id, ReferenceOption.CASCADE, ReferenceOption.CASCADE)
    val exercise =
        reference("exercise_id", WorkoutExercises.id, ReferenceOption.CASCADE, ReferenceOption.CASCADE)
    val weekDay = customEnumeration(
        "week_day", "WeekDay",
        { value -> WeekDay.valueOf(value as String) },
        { PGEnum<WeekDay>("WeekDay", it) }
    )
    val completionDate = timestamp("completion_date").transform(
        wrap = { Instant.fromEpochMilliseconds(it.toEpochMilli()) },
        unwrap = { java.time.Instant.ofEpochMilli(it.toEpochMilliseconds()) })
    val completed = bool("completed").default(false)
    val feedback = text("feedback").nullable() // comentarios del deportista sobre el ejercicio
    val actualReps = integer("actual_reps").nullable() // repeticiones realmente realizadas
    val actualSets = integer("actual_sets").nullable() // series realmente realizadas
    val difficulty = integer("perceived_difficulty").nullable() // percepción de dificultad (1-5)
}

// Tabla para gestionar las notas específicas de una rutina
object WorkoutNotes : Table("workout_notes") {
    val workout = reference("workout_id", WorkoutsTable.id, ReferenceOption.CASCADE, ReferenceOption.CASCADE)
    val note = reference("note_id", NoteTable.id, ReferenceOption.CASCADE, ReferenceOption.CASCADE)

    override val primaryKey = PrimaryKey(workout, note, name = "PK_Workout_Note")
}


object SportsmanAllergiesTable : Table("sportsman_allergies") {
    val sportsman = reference("sportsman_id", SportsmanTable)
    val allergy = reference("allergy_id", AllergyTable)
    override val primaryKey = PrimaryKey(sportsman, allergy)
}

object AllergyTable : IntIdTable("allergies") {
    val name = varchar("name", 100)
}


object DietTable : IntIdTable("diets") {
    val name = varchar("name", 100)
    val sportsmanId = reference("sportsman_id", SportsmanTable)
    val initialDate = timestamp("initial_date").transform(
        wrap = { Instant.fromEpochMilliseconds(it.toEpochMilli()) },
        unwrap = { java.time.Instant.ofEpochMilli(it.toEpochMilliseconds()) })
    val duration = long("duration_days")
    val description = text("description").nullable()
}

object UserStatus : IntIdTable("sportsman_status") {
    val sportsmanId = reference("user_id", UserTable.id)
    val status = bool("status")
    val lastActive = timestamp("last_active").transform(
        wrap = { Instant.fromEpochMilliseconds(it.toEpochMilli()) },
        unwrap = { java.time.Instant.ofEpochMilli(it.toEpochMilliseconds()) })
    val needsAttention = bool("needs_attention").default(false)
}