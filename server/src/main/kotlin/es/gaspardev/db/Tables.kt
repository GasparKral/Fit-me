package es.gaspardev.db

import es.gaspardev.enums.BodyPart
import es.gaspardev.enums.MediaType
import es.gaspardev.enums.MessageType
import es.gaspardev.enums.WeekDay
import kotlinx.datetime.Instant
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestamp

object UsersTable : IntIdTable("users") {
    val name = varchar("name", 100)
    val email = varchar("email", 100).uniqueIndex()
    val phone = varchar("phone", 20).nullable()
    val creationTime = timestamp("creation_time").transform(
        wrap = { Instant.fromEpochMilliseconds(it.toEpochMilli()) },
        unwrap = { java.time.Instant.ofEpochMilli(it.toEpochMilliseconds()) })
    val status = bool("status").default(false)
    val lastActive = timestamp("last_active").transform(
        wrap = { Instant.fromEpochMilliseconds(it.toEpochMilli()) },
        unwrap = { java.time.Instant.ofEpochMilli(it.toEpochMilliseconds()) })
    val needsAttention = bool("needs_attention").default(false)
    val password = varchar("password", 200)
}

object ResourcesTable : IntIdTable("resources") {
    val resourceType = enumerationByName<MediaType>("media_type", 20)
    val src = varchar("src", 255)
    val userId = reference("user_id", UsersTable).nullable()
}

object UserImagesTable : IntIdTable("user_images") {
    val userId = optReference("user_id", UsersTable).uniqueIndex()
    val resourceId = reference("resource_id", ResourcesTable)
}

object TrainersTable : IntIdTable("trainers") {
    val userId = reference("user_id", UsersTable).uniqueIndex()
    val specialization = varchar("specialization", 100)
    val yearsOfExperience = integer("years_of_experience")
    val bio = text("bio")
}

object SocialLinksTable : IntIdTable("trainer_social_links") {
    val trainerId = reference("trainer_id", TrainersTable)
    val platform = varchar("platform", 20)
    val url = varchar("url", 255)
}

object SportsmenTable : IntIdTable("sportsmen") {
    val userId = reference("user_id", UsersTable).uniqueIndex()
    val trainerId = reference("trainer_id", TrainersTable).nullable()
    val age = integer("age")
    val sex = bool("sex") // true for male, false for female
    val trainingSince = timestamp("training_since").nullable().transform(
        wrap = { it?.let { it1 -> Instant.fromEpochMilliseconds(it1.toEpochMilli()) } },
        unwrap = { it?.let { it1 -> java.time.Instant.ofEpochMilli(it1.toEpochMilliseconds()) } })
}

object AllergiesTable : IntIdTable("sportsman_allergies") {
    val sportsmanId = reference("sportsman_id", SportsmenTable.userId)
    val allergy = varchar("allergy", 100)
}

object CertificationsTable : IntIdTable("certifications") {
    val trainerId = reference("trainer_id", TrainersTable)
    val name = varchar("name", 100)
    val issuingOrganization = varchar("issuing_organization", 100)
    val dateObtained = timestamp("date_obtained").transform(
        wrap = { Instant.fromEpochMilliseconds(it.toEpochMilli()) },
        unwrap = { java.time.Instant.ofEpochMilli(it.toEpochMilliseconds()) })
}

object MeasurementsTable : IntIdTable("measurements") {
    val sportsmanId = reference("sportsman_id", SportsmenTable.userId)
    val weight = double("weight").nullable()
    val height = double("height").nullable()
    val bodyFat = integer("body_fat").nullable()
    val lastUpdated = timestamp("last_updated").nullable().transform(
        wrap = { it?.let { it1 -> Instant.fromEpochMilliseconds(it1.toEpochMilli()) } },
        unwrap = { it?.let { it1 -> java.time.Instant.ofEpochMilli(it1.toEpochMilliseconds()) } })
}

object MeasurementHistoryTable : IntIdTable("measurement_history") {
    val sportsmanId = reference("sportsman_id", SportsmenTable)
    val weight = double("weight").nullable()
    val height = double("height").nullable()
    val bodyFat = integer("body_fat").nullable()
    val timestamp = timestamp("measurement_date").transform(
        wrap = { Instant.fromEpochMilliseconds(it.toEpochMilli()) },
        unwrap = { java.time.Instant.ofEpochMilli(it.toEpochMilliseconds()) }
    )
}

object DishesTable : IntIdTable("dishes") {
    val name = varchar("name", 100)
}

object DishAlternativesTable : IntIdTable("dish_alternatives") {
    val dishId = reference("dish_id", DishesTable)
    val alternativeDishId = reference("alternative_dish_id", DishesTable)
}

object ExerciseBasesTable : IntIdTable("exercise_bases") {
    val name = varchar("name", 100)
    val bodyPart = enumerationByName<BodyPart>("body_part", 20)
    val description = text("description")
    val authorId = reference("author_id", TrainersTable.userId).nullable()
    val videoResourceId = reference("video_resource_id", ResourcesTable).nullable()
}

object NotesTable : IntIdTable("notes") {
    val userId = reference("user_id", UsersTable)
    val message = text("message")
}

object WorkoutExerciseNotesTable : IntIdTable("workout_exercise_notes") {
    val workoutExerciseId = reference("workout_exercise_id", WorkoutExercisesTable)
    val notes = reference("note_id", NotesTable)
}

object WorkoutsNotesTable : IntIdTable("workouts_notes") {
    val workoutId = reference("workout_id", WorkoutsTable)
    val notes = reference("note_id", NotesTable)
}

object CompletedWorkoutNotesTable : IntIdTable("completed_workout_notes") {
    val completedWorkoutId = reference("completed_workout_id", CompletedWorkoutsTable)
    val notes = reference("note_id", NotesTable)
}

object NutritionNotesTable : IntIdTable("nutrition_notes") {
    val nutritionPlanId = reference("nutrition_plan_id", NutritionPlansTable)
    val notes = reference("note_id", NotesTable)
}

object CompletedNutritionNotesTable : IntIdTable("completed_nutrition_notes") {
    val completedNutritionId = reference("completed_nutrition_id", CompletedNutritionPlansTable)
    val notes = reference("note_id", NotesTable)
}

// WORKOUT PLANS - Actualizada para coincidir con los modelos de UI
object WorkoutsTable : IntIdTable("workouts") {
    val name = varchar("name", 100)
    val description = text("description").nullable()

    // Campos básicos para planes
    val type = varchar("type", 50) // "strength", "cardio", "full body", etc.
    val difficulty = varchar("difficulty", 20) // "beginner", "intermediate", "advanced"
    val duration = varchar("duration", 30) // "4 weeks", "8 weeks", etc.
    val frequency = varchar("frequency", 30) // "3 days/week", "5 days/week", etc.

    // Metadatos
    val author = reference("trainer_id", TrainersTable.id, ReferenceOption.CASCADE, ReferenceOption.CASCADE)
    val creationDate = timestamp("creation_date").transform(
        wrap = { Instant.fromEpochMilliseconds(it.toEpochMilli()) },
        unwrap = { java.time.Instant.ofEpochMilli(it.toEpochMilliseconds()) })
    val lastUpdated = timestamp("last_updated").transform(
        wrap = { Instant.fromEpochMilliseconds(it.toEpochMilli()) },
        unwrap = { java.time.Instant.ofEpochMilli(it.toEpochMilliseconds()) })

    // Template vs Plan asignado
    val isTemplate = bool("is_template").default(false)
    val baseTemplate = optReference("base_template_id", id, ReferenceOption.SET_NULL, ReferenceOption.CASCADE)
    val isPublic = bool("is_public").default(false)

    // Campos para cálculos (se actualizan automáticamente)
    val exerciseCount = integer("exercise_count").default(0)
    val assignedCount = integer("assigned_count").default(0)

    // Índices para búsquedas eficientes
    init {
        index(false, author, isTemplate)
        index(false, isTemplate, isPublic)
        index(false, type, difficulty)
        index(false, lastUpdated)
    }
}

// ASIGNACIONES DE WORKOUT PLANS A DEPORTISTAS
object WorkoutAssignmentsTable : IntIdTable("workout_assignments") {
    val workoutId = reference("workout_id", WorkoutsTable)
    val sportsmanId = reference("sportsman_id", SportsmenTable.id)
    val assignedAt = timestamp("assigned_at").transform(
        wrap = { Instant.fromEpochMilliseconds(it.toEpochMilli()) },
        unwrap = { java.time.Instant.ofEpochMilli(it.toEpochMilliseconds()) })
    val startDate = timestamp("start_date").nullable().transform(
        wrap = { it?.let { Instant.fromEpochMilliseconds(it.toEpochMilli()) } },
        unwrap = { it?.let { java.time.Instant.ofEpochMilli(it.toEpochMilliseconds()) } })
    val endDate = timestamp("end_date").nullable().transform(
        wrap = { it?.let { Instant.fromEpochMilliseconds(it.toEpochMilli()) } },
        unwrap = { it?.let { java.time.Instant.ofEpochMilli(it.toEpochMilliseconds()) } })
    val isActive = bool("is_active").default(true)

    init {
        uniqueIndex(workoutId, sportsmanId)
    }
}

object CompletedWorkoutsTable : IntIdTable("completed_workouts") {
    val workout = reference("workout_id", WorkoutsTable)
    val sportsman = reference("sportsman_id", SportsmenTable)
    val completedAt = timestamp("completed_at").transform(
        wrap = { Instant.fromEpochMilliseconds(it.toEpochMilli()) },
        unwrap = { java.time.Instant.ofEpochMilli(it.toEpochMilliseconds()) }
    )
}

object WorkoutExercisesTable : IntIdTable("workout_exercises") {
    val workoutId = reference("workout_id", WorkoutsTable)
    val exerciseBaseId = reference("exercise_base_id", ExerciseBasesTable)
    val day = enumerationByName<WeekDay>("day", 10)
    val reps = integer("reps")
    val sets = integer("sets")
    val isOption = bool("is_option").default(false)
    val orderIndex = integer("order_index").default(0) // Para ordenar ejercicios
}

object ExerciseAlternativesTable : IntIdTable("exercise_alternatives") {
    val mainExerciseId = reference("main_exercise_id", WorkoutExercisesTable)
    val alternativeExerciseId = reference("alternative_exercise_id", WorkoutExercisesTable)
}

// NUTRITION PLANS - Nueva tabla diseñada para coincidir con los modelos de UI
object NutritionPlansTable : IntIdTable("nutrition_plans") {
    val name = varchar("name", 100)
    val description = text("description").nullable()

    // Campos básicos para planes nutricionales
    val type = varchar("type", 50) // "weight loss", "muscle gain", "maintenance", etc.
    val difficulty = varchar("difficulty", 20) // "beginner", "intermediate", "advanced"
    val duration = varchar("duration", 30) // "2 weeks", "4 weeks", "8 weeks", etc.
    val mealsPerDay = integer("meals_per_day").default(3)
    val caloriesPerDay = integer("calories_per_day").nullable()

    // Metadatos
    val author = reference("trainer_id", TrainersTable.id, ReferenceOption.CASCADE, ReferenceOption.CASCADE)
    val creationDate = timestamp("creation_date").transform(
        wrap = { Instant.fromEpochMilliseconds(it.toEpochMilli()) },
        unwrap = { java.time.Instant.ofEpochMilli(it.toEpochMilliseconds()) })
    val lastUpdated = timestamp("last_updated").transform(
        wrap = { Instant.fromEpochMilliseconds(it.toEpochMilli()) },
        unwrap = { java.time.Instant.ofEpochMilli(it.toEpochMilliseconds()) })

    // Template vs Plan asignado
    val isTemplate = bool("is_template").default(false)
    val baseTemplate = optReference("base_template_id", id, ReferenceOption.SET_NULL, ReferenceOption.CASCADE)
    val isPublic = bool("is_public").default(false)

    // Campos para cálculos (se actualizan automáticamente)
    val recipeCount = integer("recipe_count").default(0)
    val assignedCount = integer("assigned_count").default(0)

    // Índices para búsquedas eficientes
    init {
        index(false, author, isTemplate)
        index(false, isTemplate, isPublic)
        index(false, type, difficulty)
        index(false, lastUpdated)
    }
}

// ASIGNACIONES DE NUTRITION PLANS A DEPORTISTAS
object NutritionAssignmentsTable : IntIdTable("nutrition_assignments") {
    val nutritionPlanId = reference("nutrition_plan_id", NutritionPlansTable)
    val sportsmanId = reference("sportsman_id", SportsmenTable.id)
    val assignedAt = timestamp("assigned_at").transform(
        wrap = { Instant.fromEpochMilliseconds(it.toEpochMilli()) },
        unwrap = { java.time.Instant.ofEpochMilli(it.toEpochMilliseconds()) })
    val startDate = timestamp("start_date").nullable().transform(
        wrap = { it?.let { Instant.fromEpochMilliseconds(it.toEpochMilli()) } },
        unwrap = { it?.let { java.time.Instant.ofEpochMilli(it.toEpochMilliseconds()) } })
    val endDate = timestamp("end_date").nullable().transform(
        wrap = { it?.let { Instant.fromEpochMilliseconds(it.toEpochMilli()) } },
        unwrap = { it?.let { java.time.Instant.ofEpochMilli(it.toEpochMilliseconds()) } })
    val isActive = bool("is_active").default(true)

    init {
        uniqueIndex(nutritionPlanId, sportsmanId)
    }
}

object CompletedNutritionPlansTable : IntIdTable("completed_nutrition_plans") {
    val nutritionPlan = reference("nutrition_plan_id", NutritionPlansTable)
    val sportsman = reference("sportsman_id", SportsmenTable)
    val completedAt = timestamp("completed_at").transform(
        wrap = { Instant.fromEpochMilliseconds(it.toEpochMilli()) },
        unwrap = { java.time.Instant.ofEpochMilli(it.toEpochMilliseconds()) }
    )
}

// RECETAS/COMIDAS EN PLANES NUTRICIONALES
object NutritionRecipesTable : IntIdTable("nutrition_recipes") {
    val nutritionPlanId = reference("nutrition_plan_id", NutritionPlansTable)
    val day = enumerationByName<WeekDay>("day", 10)
    val mealType = varchar("meal_type", 20) // "breakfast", "lunch", "dinner", "snack"
    val dishId = reference("dish_id", DishesTable)
    val quantity = double("quantity") // Porción/cantidad
    val orderIndex = integer("order_index").default(0) // Para ordenar comidas del día
    val calories = integer("calories").nullable() // Calorías estimadas para esta porción
}

// TABLA PARA BOOKMARKS/FAVORITOS DE TEMPLATES
object TemplateBookmarks : Table("template_bookmarks") {
    val trainerId = reference("trainer_id", TrainersTable.id, ReferenceOption.CASCADE, ReferenceOption.CASCADE)
    val workoutTemplateId =
        optReference("workout_template_id", WorkoutsTable.id, ReferenceOption.CASCADE, ReferenceOption.CASCADE)
    val nutritionTemplateId =
        optReference("nutrition_template_id", NutritionPlansTable.id, ReferenceOption.CASCADE, ReferenceOption.CASCADE)
    val bookmarkedAt = timestamp("bookmarked_at").transform(
        wrap = { Instant.fromEpochMilliseconds(it.toEpochMilli()) },
        unwrap = { java.time.Instant.ofEpochMilli(it.toEpochMilliseconds()) })

    override val primaryKey =
        PrimaryKey(trainerId, workoutTemplateId, nutritionTemplateId, name = "PK_Template_Bookmark")
}

// ESTADÍSTICAS DE USO DE TEMPLATES
object TemplateUsageStats : IntIdTable("template_usage_stats") {
    val workoutTemplateId =
        optReference("workout_template_id", WorkoutsTable.id, ReferenceOption.CASCADE, ReferenceOption.CASCADE)
    val nutritionTemplateId =
        optReference("nutrition_template_id", NutritionPlansTable.id, ReferenceOption.CASCADE, ReferenceOption.CASCADE)
    val usedByTrainerId =
        reference("used_by_trainer_id", TrainersTable.id, ReferenceOption.CASCADE, ReferenceOption.CASCADE)
    val usageDate = timestamp("usage_date").transform(
        wrap = { Instant.fromEpochMilliseconds(it.toEpochMilli()) },
        unwrap = { java.time.Instant.ofEpochMilli(it.toEpochMilliseconds()) })
    val assignedToSportsmanId =
        optReference("assigned_to_sportsman_id", SportsmenTable.id, ReferenceOption.CASCADE, ReferenceOption.CASCADE)
}

// TABLAS RESTANTES (sin cambios significativos)
object TimeSlotsTable : IntIdTable("time_slots") {
    val trainerId = reference("trainer_id", TrainersTable)
    val day = enumerationByName<WeekDay>("day", 10)
    val startTime = varchar("start_time", 8) // HH:MM:SS format
    val endTime = varchar("end_time", 8) // HH:MM:SS format
}

object SessionsTable : IntIdTable("sessions") {
    val trainerId = reference("trainer_id", TrainersTable)
    val sportsmanId = reference("sportsman_id", SportsmenTable.userId)
    val dateTime = timestamp("date_time").transform(
        wrap = { Instant.fromEpochMilliseconds(it.toEpochMilli()) },
        unwrap = { java.time.Instant.ofEpochMilli(it.toEpochMilliseconds()) })
    val duration = long("duration_millis") // Stored in milliseconds
}

object ChatsTable : IntIdTable("chats") {
    val trainerId = reference("trainer_id", TrainersTable)
    val sportsmanId = reference("sportsman_id", SportsmenTable.userId)
}

object MessagesTable : IntIdTable("messages") {
    val chatId = reference("chat_id", ChatsTable)
    val authorId = reference("author_id", UsersTable)
    val type = enumerationByName<MessageType>("message_type", 20)
    val message = text("message")
    val mediaResourceId = reference("media_resource_id", ResourcesTable).nullable()
    val postTime = timestamp("post_time").transform(
        wrap = { Instant.fromEpochMilliseconds(it.toEpochMilli()) },
        unwrap = { java.time.Instant.ofEpochMilli(it.toEpochMilliseconds()) })
}

object RegistKeyTable : IntIdTable("key_gen") {
    val key = varchar("key", 250)
    val trainer = reference("trainer_id", TrainersTable.userId, ReferenceOption.RESTRICT, ReferenceOption.CASCADE)
}