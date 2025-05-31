package es.gaspardev.modules.endpoints

import es.gaspardev.database.DatabaseConfig
import es.gaspardev.utils.DATA_BASE_NAME
import es.gaspardev.utils.DATA_BASE_PASSWORD
import es.gaspardev.utils.DATA_BASE_PORT
import es.gaspardev.utils.DATA_BASE_USERNAME
import io.ktor.server.application.*
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.time.Duration.Companion.days
import es.gaspardev.database.*
import es.gaspardev.enums.*
import kotlinx.datetime.Instant
import org.jetbrains.exposed.sql.*

fun Application.configureDatabases() {
    Database.connect(
        "jdbc:postgresql://localhost:${DATA_BASE_PORT}/${DATA_BASE_NAME}",
        driver = "org.postgresql.Driver",
        user = DATA_BASE_USERNAME,
        password = DATA_BASE_PASSWORD
    )

    transaction {
        SchemaUtils.create(*DatabaseConfig.allTables)

        // Crear dataset de prueba
        createTestDataset()
    }
}

private fun createTestDataset() {

    // ============================================================================
    // USUARIOS BASE
    // ============================================================================

    // Entrenadores
    val trainer1Id = Users.insertAndGetId {
        it[fullname] = "Alex"
        it[password] = "8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918"
        it[email] = "carlos.martinez@fitnesstrainer.com"
        it[phone] = "+34 666 123 456"
        it[creationDate] = Instant.fromEpochMilliseconds(System.currentTimeMillis() - 2.years.inWholeMilliseconds)
        it[userImageUrl] = "https://example.com/images/carlos.jpg"
    }

    val trainer2Id = Users.insertAndGetId {
        it[fullname] = "María García"
        it[password] = "hashed_password_456"
        it[email] = "maria.garcia@fitnesstrainer.com"
        it[phone] = "+34 666 234 567"
        it[creationDate] = Instant.fromEpochMilliseconds(System.currentTimeMillis() - 3.years.inWholeMilliseconds)
        it[userImageUrl] = "https://example.com/images/maria.jpg"
    }

    val trainer3Id = Users.insertAndGetId {
        it[fullname] = "David López"
        it[password] = "hashed_password_789"
        it[email] = "david.lopez@fitnesstrainer.com"
        it[phone] = "+34 666 345 678"
        it[creationDate] = Instant.fromEpochMilliseconds(System.currentTimeMillis() - 1.years.inWholeMilliseconds)
        it[userImageUrl] = "https://example.com/images/david.jpg"
    }

    // Atletas
    val athlete1UserId = Users.insertAndGetId {
        it[fullname] = "Ana Rodríguez"
        it[password] = "hashed_password_abc"
        it[email] = "ana.rodriguez@email.com"
        it[phone] = "+34 666 456 789"
        it[creationDate] = Instant.fromEpochMilliseconds(System.currentTimeMillis() - 6.months.inWholeMilliseconds)
        it[userImageUrl] = "https://example.com/images/ana.jpg"
    }

    val athlete2UserId = Users.insertAndGetId {
        it[fullname] = "Pedro Sánchez"
        it[password] = "hashed_password_def"
        it[email] = "pedro.sanchez@email.com"
        it[phone] = "+34 666 567 890"
        it[creationDate] = Instant.fromEpochMilliseconds(System.currentTimeMillis() - 4.months.inWholeMilliseconds)
        it[userImageUrl] = null
    }

    val athlete3UserId = Users.insertAndGetId {
        it[fullname] = "Laura Fernández"
        it[password] = "hashed_password_ghi"
        it[email] = "laura.fernandez@email.com"
        it[phone] = "+34 666 678 901"
        it[creationDate] = Instant.fromEpochMilliseconds(System.currentTimeMillis() - 8.months.inWholeMilliseconds)
        it[userImageUrl] = "https://example.com/images/laura.jpg"
    }

    val athlete4UserId = Users.insertAndGetId {
        it[fullname] = "Miguel Torres"
        it[password] = "hashed_password_jkl"
        it[email] = "miguel.torres@email.com"
        it[phone] = "+34 666 789 012"
        it[creationDate] = Instant.fromEpochMilliseconds(System.currentTimeMillis() - 2.months.inWholeMilliseconds)
        it[userImageUrl] = null
    }

    // ============================================================================
    // STATUS DE USUARIOS
    // ============================================================================

    UserStatus.insert {
        it[state] = StatusState.ACTIVE
        it[lastTimeActive] = Instant.fromEpochMilliseconds(System.currentTimeMillis() - 5 * 60 * 1000) // 5 min ago
        it[userId] = trainer1Id
    }

    UserStatus.insert {
        it[state] = StatusState.INACTIVE
        it[lastTimeActive] =
            Instant.fromEpochMilliseconds(System.currentTimeMillis() - 2 * 60 * 60 * 1000) // 2 hours ago
        it[userId] = trainer2Id
    }

    UserStatus.insert {
        it[state] = StatusState.INACTIVE
        it[lastTimeActive] =
            Instant.fromEpochMilliseconds(System.currentTimeMillis() - 30 * 60 * 1000) // 30 min ago
        it[userId] = trainer3Id
    }

    UserStatus.insert {
        it[state] = StatusState.ACTIVE
        it[lastTimeActive] = Instant.fromEpochMilliseconds(System.currentTimeMillis() - 2 * 60 * 1000) // 2 min ago
        it[userId] = athlete1UserId
    }

    UserStatus.insert {
        it[state] = StatusState.INACTIVE
        it[lastTimeActive] =
            Instant.fromEpochMilliseconds(System.currentTimeMillis() - 12 * 60 * 60 * 1000) // 12 hours ago
        it[userId] = athlete2UserId
    }

    UserStatus.insert {
        it[state] = StatusState.ACTIVE
        it[lastTimeActive] = Instant.fromEpochMilliseconds(System.currentTimeMillis() - 1 * 60 * 1000) // 1 min ago
        it[userId] = athlete3UserId
    }

    UserStatus.insert {
        it[state] = StatusState.INACTIVE
        it[lastTimeActive] =
            Instant.fromEpochMilliseconds(System.currentTimeMillis() - 15 * 60 * 1000) // 15 min ago
        it[userId] = athlete4UserId
    }

    // ============================================================================
    // ENTRENADORES
    // ============================================================================

    val trainerRecord1Id = Trainers.insertAndGetId {
        it[specialization] = "Entrenamiento de fuerza y musculación"
        it[yearsOfExperience] = 8
        it[userId] = trainer1Id
    }

    val trainerRecord2Id = Trainers.insertAndGetId {
        it[specialization] = "Fitness funcional y pérdida de peso"
        it[yearsOfExperience] = 12
        it[userId] = trainer2Id
    }

    val trainerRecord3Id = Trainers.insertAndGetId {
        it[specialization] = "Entrenamiento deportivo y rehabilitación"
        it[yearsOfExperience] = 5
        it[userId] = trainer3Id
    }

    // ============================================================================
    // ATLETAS
    // ============================================================================

    val athlete1Id = Athletes.insertAndGetId {
        it[age] = 28
        it[sex] = Sex.FEMALE
        it[trainingSince] = Instant.fromEpochMilliseconds(System.currentTimeMillis() - 2.years.inWholeMilliseconds)
        it[workoutId] = null
        it[dietId] = null
        it[trainer] = trainerRecord1Id
        it[needAssistant] = false
        it[userId] = athlete1UserId
    }

    val athlete2Id = Athletes.insertAndGetId {
        it[age] = 35
        it[sex] = Sex.MALE
        it[trainingSince] = Instant.fromEpochMilliseconds(System.currentTimeMillis() - 1.years.inWholeMilliseconds)
        it[workoutId] = null
        it[dietId] = null
        it[trainer] = trainerRecord2Id
        it[needAssistant] = true
        it[userId] = athlete2UserId
    }

    val athlete3Id = Athletes.insertAndGetId {
        it[age] = 24
        it[sex] = Sex.FEMALE
        it[trainingSince] = Instant.fromEpochMilliseconds(System.currentTimeMillis() - 3.years.inWholeMilliseconds)
        it[workoutId] = null
        it[dietId] = null
        it[trainer] = trainerRecord1Id
        it[needAssistant] = false
        it[userId] = athlete3UserId
    }

    val athlete4Id = Athletes.insertAndGetId {
        it[age] = 42
        it[sex] = Sex.MALE
        it[trainingSince] = Instant.fromEpochMilliseconds(System.currentTimeMillis() - 6.months.inWholeMilliseconds)
        it[workoutId] = null
        it[dietId] = null
        it[trainer] = trainerRecord3Id
        it[needAssistant] = true
        it[userId] = athlete4UserId
    }

    // ============================================================================
    // ALERGIAS
    // ============================================================================

    val allergyGlutenId = Allergies.insertAndGetId {
        it[name] = "Gluten"
    }

    val allergyLactoseId = Allergies.insertAndGetId {
        it[name] = "Lactosa"
    }

    val allergyNutsId = Allergies.insertAndGetId {
        it[name] = "Frutos secos"
    }

    val allergySeafoodId = Allergies.insertAndGetId {
        it[name] = "Mariscos"
    }

    // Asignar alergias a atletas
    AthleteAllergies.insert {
        it[athleteId] = Athletes.selectAll().where { Athletes.id eq athlete2Id }.single()[Athletes.id]
        it[allergyId] = allergyGlutenId
    }

    AthleteAllergies.insert {
        it[athleteId] = Athletes.selectAll().where { Athletes.id eq athlete3Id }.single()[Athletes.id]
        it[allergyId] = allergyLactoseId
    }

    AthleteAllergies.insert {
        it[athleteId] = Athletes.selectAll().where { Athletes.id eq athlete3Id }.single()[Athletes.id]
        it[allergyId] = allergyNutsId
    }

    // ============================================================================
    // MEDIDAS
    // ============================================================================

    Measurements.insert {
        it[athleteId] = athlete1Id
        it[weight] = 65.5
        it[height] = 168.0
        it[bodyFat] = 18.5
        it[armSize] = 28.0
        it[chestBackSize] = 88.0
        it[hipSize] = 92.0
        it[legSize] = 58.0
        it[calvesSize] = 36.0
        it[measureAt] = Instant.fromEpochMilliseconds(System.currentTimeMillis() - 1.weeks.inWholeMilliseconds)
        it[isCurrent] = true
    }

    Measurements.insert {
        it[athleteId] = athlete2Id
        it[weight] = 82.3
        it[height] = 175.0
        it[bodyFat] = 22.0
        it[armSize] = 35.0
        it[chestBackSize] = 98.0
        it[hipSize] = 95.0
        it[legSize] = 62.0
        it[calvesSize] = 40.0
        it[measureAt] = Instant.fromEpochMilliseconds(System.currentTimeMillis() - 2.weeks.inWholeMilliseconds)
        it[isCurrent] = true
    }

    Measurements.insert {
        it[athleteId] = athlete3Id
        it[weight] = 58.7
        it[height] = 162.0
        it[bodyFat] = 16.2
        it[armSize] = 26.5
        it[chestBackSize] = 84.0
        it[hipSize] = 89.0
        it[legSize] = 55.0
        it[calvesSize] = 34.0
        it[measureAt] = Instant.fromEpochMilliseconds(System.currentTimeMillis() - 3.days.inWholeMilliseconds)
        it[isCurrent] = true
    }

    // ============================================================================
    // CERTIFICACIONES
    // ============================================================================

    Certifications.insert {
        it[trainerId] = trainerRecord1Id
        it[name] = "ACSM Certified Personal Trainer"
        it[issuingOrganization] = "American College of Sports Medicine"
        it[completeAt] = Instant.fromEpochMilliseconds(System.currentTimeMillis() - 3.years.inWholeMilliseconds)
    }

    Certifications.insert {
        it[trainerId] = trainerRecord1Id
        it[name] = "NSCA Certified Strength and Conditioning Specialist"
        it[issuingOrganization] = "National Strength and Conditioning Association"
        it[completeAt] = Instant.fromEpochMilliseconds(System.currentTimeMillis() - 2.years.inWholeMilliseconds)
    }

    Certifications.insert {
        it[trainerId] = trainerRecord2Id
        it[name] = "NASM Certified Personal Trainer"
        it[issuingOrganization] = "National Academy of Sports Medicine"
        it[completeAt] = Instant.fromEpochMilliseconds(System.currentTimeMillis() - 5.years.inWholeMilliseconds)
    }

    Certifications.insert {
        it[trainerId] = trainerRecord3Id
        it[name] = "Certified Functional Movement Screen"
        it[issuingOrganization] = "Functional Movement Systems"
        it[completeAt] = Instant.fromEpochMilliseconds(System.currentTimeMillis() - 1.years.inWholeMilliseconds)
    }

    // ============================================================================
    // REDES SOCIALES DE ENTRENADORES
    // ============================================================================

    TrainerSocials.insert {
        it[trainerId] = trainerRecord1Id
        it[platform] = "Instagram"
        it[url] = "https://instagram.com/carlosfitness_trainer"
    }

    TrainerSocials.insert {
        it[trainerId] = trainerRecord1Id
        it[platform] = "YouTube"
        it[url] = "https://youtube.com/c/CarlosFitnessTrainer"
    }

    TrainerSocials.insert {
        it[trainerId] = trainerRecord2Id
        it[platform] = "Instagram"
        it[url] = "https://instagram.com/maria_functional_fitness"
    }

    TrainerSocials.insert {
        it[trainerId] = trainerRecord3Id
        it[platform] = "LinkedIn"
        it[url] = "https://linkedin.com/in/david-lopez-trainer"
    }

    // ============================================================================
    // DISPONIBILIDAD DE ENTRENADORES
    // ============================================================================

    val baseTime = System.currentTimeMillis()

    // Carlos - Lunes a Viernes mañanas
    listOf(WeekDay.MONDAY, WeekDay.TUESDAY, WeekDay.WEDNESDAY, WeekDay.THURSDAY, WeekDay.FRIDAY).forEach { day ->
        TrainerAvailability.insert {
            it[trainerId] = trainerRecord1Id
            it[weekDay] = day
            it[startTime] = Instant.fromEpochMilliseconds(baseTime + 8 * 60 * 60 * 1000) // 8:00 AM
            it[endTime] = Instant.fromEpochMilliseconds(baseTime + 12 * 60 * 60 * 1000) // 12:00 PM
        }
    }

    // María - Tardes todos los días
    WeekDay.entries.forEach { day ->
        TrainerAvailability.insert {
            it[trainerId] = trainerRecord2Id
            it[weekDay] = day
            it[startTime] = Instant.fromEpochMilliseconds(baseTime + 16 * 60 * 60 * 1000) // 4:00 PM
            it[endTime] = Instant.fromEpochMilliseconds(baseTime + 20 * 60 * 60 * 1000) // 8:00 PM
        }
    }

    // David - Mañanas y tardes, lunes a viernes
    listOf(WeekDay.MONDAY, WeekDay.TUESDAY, WeekDay.WEDNESDAY, WeekDay.THURSDAY, WeekDay.FRIDAY).forEach { day ->
        TrainerAvailability.insert {
            it[trainerId] = trainerRecord3Id
            it[weekDay] = day
            it[startTime] = Instant.fromEpochMilliseconds(baseTime + 9 * 60 * 60 * 1000) // 9:00 AM
            it[endTime] = Instant.fromEpochMilliseconds(baseTime + 13 * 60 * 60 * 1000) // 1:00 PM
        }

        TrainerAvailability.insert {
            it[trainerId] = trainerRecord3Id
            it[weekDay] = day
            it[startTime] = Instant.fromEpochMilliseconds(baseTime + 17 * 60 * 60 * 1000) // 5:00 PM
            it[endTime] = Instant.fromEpochMilliseconds(baseTime + 19 * 60 * 60 * 1000) // 7:00 PM
        }
    }

    // ============================================================================
    // EJERCICIOS
    // ============================================================================

    val pushUpId = Exercises.insertAndGetId {
        it[name] = "Flexiones"
        it[description] = "Ejercicio de fuerza que trabaja pecho, hombros y tríceps"
        it[bodyPart] = BodyPart.CHEST
    }

    val squatId = Exercises.insertAndGetId {
        it[name] = "Sentadillas"
        it[description] = "Ejercicio fundamental para trabajar piernas y glúteos"
        it[bodyPart] = BodyPart.LEG
    }

    val plankId = Exercises.insertAndGetId {
        it[name] = "Plancha"
        it[description] = "Ejercicio isométrico para fortalecer el core"
        it[bodyPart] = BodyPart.CORE
    }

    val pullUpId = Exercises.insertAndGetId {
        it[name] = "Dominadas"
        it[description] = "Ejercicio de tracción para trabajar espalda y bíceps"
        it[bodyPart] = BodyPart.BACK
    }

    val lungeId = Exercises.insertAndGetId {
        it[name] = "Zancadas"
        it[description] = "Ejercicio unilateral para piernas y glúteos"
        it[bodyPart] = BodyPart.LEG
    }

    val shoulderPressId = Exercises.insertAndGetId {
        it[name] = "Press de hombros"
        it[description] = "Ejercicio de empuje vertical para hombros"
        it[bodyPart] = BodyPart.SHOULDER
    }

    val bicepCurlId = Exercises.insertAndGetId {
        it[name] = "Curl de bíceps"
        it[description] = "Ejercicio de aislamiento para bíceps"
        it[bodyPart] = BodyPart.ARM
    }

    val tricepDipId = Exercises.insertAndGetId {
        it[name] = "Fondos de tríceps"
        it[description] = "Ejercicio para trabajar la parte posterior del brazo"
        it[bodyPart] = BodyPart.ARM
    }

    // ============================================================================
    // PLATOS/COMIDAS
    // ============================================================================

    val chickenBreastId = Dishes.insertAndGetId {
        it[name] = "Pechuga de pollo a la plancha"
    }

    val brownRiceId = Dishes.insertAndGetId {
        it[name] = "Arroz integral"
    }

    val mixedSaladId = Dishes.insertAndGetId {
        it[name] = "Ensalada mixta"
    }

    val oatmealId = Dishes.insertAndGetId {
        it[name] = "Avena con frutas"
    }

    val salmonId = Dishes.insertAndGetId {
        it[name] = "Salmón al horno"
    }

    val sweetPotatoId = Dishes.insertAndGetId {
        it[name] = "Batata asada"
    }

    val greekYogurtId = Dishes.insertAndGetId {
        it[name] = "Yogur griego con nueces"
    }

    val tunaId = Dishes.insertAndGetId {
        it[name] = "Atún con verduras"
    }

    // ============================================================================
    // PLANTILLAS DE ENTRENAMIENTOS
    // ============================================================================

    val beginnerTemplateId = WorkoutTemplates.insertAndGetId {
        it[name] = "Rutina Principiante Full Body"
        it[description] = "Rutina básica para comenzar a entrenar, trabajando todo el cuerpo"
        it[difficulty] = Difficulty.EASY
        it[workoutType] = WorkoutType.STRENGTH
        it[createdBy] = trainerRecord1Id
    }

    val strengthTemplateId = WorkoutTemplates.insertAndGetId {
        it[name] = "Rutina de Fuerza Avanzada"
        it[description] = "Rutina intensiva para desarrollo de fuerza muscular"
        it[difficulty] = Difficulty.HARD
        it[workoutType] = WorkoutType.STRENGTH
        it[createdBy] = trainerRecord1Id
    }

    val cardioTemplateId = WorkoutTemplates.insertAndGetId {
        it[name] = "Cardio HIIT"
        it[description] = "Entrenamiento cardiovascular de alta intensidad"
        it[difficulty] = Difficulty.ADVANCE
        it[workoutType] = WorkoutType.CARDIO
        it[createdBy] = trainerRecord2Id
    }

    // Ejercicios para plantilla principiante
    WorkoutTemplateExercises.insert {
        it[templateId] = beginnerTemplateId
        it[exerciseId] = pushUpId
        it[weekDay] = WeekDay.MONDAY
        it[reps] = 10
        it[sets] = 3
        it[isOptional] = false
        it[parentExerciseId] = null
    }

    WorkoutTemplateExercises.insert {
        it[templateId] = beginnerTemplateId
        it[exerciseId] = squatId
        it[weekDay] = WeekDay.MONDAY
        it[reps] = 15
        it[sets] = 3
        it[isOptional] = false
        it[parentExerciseId] = null
    }

    WorkoutTemplateExercises.insert {
        it[templateId] = beginnerTemplateId
        it[exerciseId] = plankId
        it[weekDay] = WeekDay.MONDAY
        it[reps] = 30 // segundos
        it[sets] = 3
        it[isOptional] = false
        it[parentExerciseId] = null
    }

    // ============================================================================
    // PLANTILLAS DE DIETAS
    // ============================================================================

    val weightLossTemplateId = DietTemplates.insertAndGetId {
        it[name] = "Dieta de Pérdida de Peso"
        it[description] = "Plan nutricional diseñado para pérdida de grasa corporal"
        it[dietType] = DietType.WEIGHT_LOSS
        it[createdBy] = trainerRecord2Id
    }

    val muscleGainTemplateId = DietTemplates.insertAndGetId {
        it[name] = "Dieta de Ganancia Muscular"
        it[description] = "Plan nutricional alto en proteínas para desarrollo muscular"
        it[dietType] = DietType.MUSCLE_GAIN
        it[createdBy] = trainerRecord1Id
    }

    // Platos para plantilla de pérdida de peso
    DietTemplateDishes.insert {
        it[templateId] = weightLossTemplateId
        it[dishId] = oatmealId
        it[weekDay] = WeekDay.MONDAY
        it[amount] = 1.0
        it[mealType] = MealType.BREAKFAST
    }

    DietTemplateDishes.insert {
        it[templateId] = weightLossTemplateId
        it[dishId] = chickenBreastId
        it[weekDay] = WeekDay.MONDAY
        it[amount] = 150.0 // gramos
        it[mealType] = MealType.LUNCH
    }

    DietTemplateDishes.insert {
        it[templateId] = weightLossTemplateId
        it[dishId] = mixedSaladId
        it[weekDay] = WeekDay.MONDAY
        it[amount] = 1.0
        it[mealType] = MealType.LUNCH
    }

    DietTemplateDishes.insert {
        it[templateId] = weightLossTemplateId
        it[dishId] = salmonId
        it[weekDay] = WeekDay.MONDAY
        it[amount] = 120.0 // gramos
        it[mealType] = MealType.DINNER
    }

    // ============================================================================
    // ENTRENAMIENTOS ACTIVOS
    // ============================================================================

    val workout1Id = Workouts.insertAndGetId {
        it[name] = "Rutina Ana - Semana 1"
        it[description] = "Rutina personalizada basada en plantilla principiante"
        it[difficulty] = Difficulty.EASY
        it[duration] = kotlin.time.Duration.parse("45m")
        it[workoutType] = WorkoutType.STRENGTH
        it[createdBy] = trainer1Id
        it[startUp] = Instant.fromEpochMilliseconds(System.currentTimeMillis() - 1.weeks.inWholeMilliseconds)
    }

    val workout2Id = Workouts.insertAndGetId {
        it[name] = "Rutina Pedro - Pérdida de Peso"
        it[description] = "Rutina cardiovascular con algo de fuerza"
        it[difficulty] = Difficulty.ADVANCE
        it[duration] = kotlin.time.Duration.parse("60m")
        it[workoutType] = WorkoutType.CARDIO
        it[createdBy] = trainer2Id
        it[startUp] = Instant.fromEpochMilliseconds(System.currentTimeMillis() - 2.weeks.inWholeMilliseconds)
    }

    // Ejercicios para rutina de Ana
    WorkoutExercises.insert {
        it[workoutId] = workout1Id
        it[exerciseId] = pushUpId
        it[weekDay] = WeekDay.MONDAY
        it[reps] = 8
        it[sets] = 3
        it[isOptional] = false
        it[parentExerciseId] = null
    }

    WorkoutExercises.insert {
        it[workoutId] = workout1Id
        it[exerciseId] = squatId
        it[weekDay] = WeekDay.MONDAY
        it[reps] = 12
        it[sets] = 3
        it[isOptional] = false
        it[parentExerciseId] = null
    }

    // ============================================================================
    // DIETAS ACTIVAS
    // ============================================================================

    val diet1Id = Diets.insertAndGetId {
        it[name] = "Dieta Ana - Mantenimiento"
        it[description] = "Dieta balanceada para mantenimiento del peso actual"
        it[dietType] = DietType.BALANCED
        it[duration] = kotlin.time.Duration.parse("30d")
        it[createdBy] = trainer1Id
        it[startAt] = Instant.fromEpochMilliseconds(System.currentTimeMillis() - 1.weeks.inWholeMilliseconds)
    }

    val diet2Id = Diets.insertAndGetId {
        it[name] = "Dieta Pedro - Pérdida de Peso"
        it[description] = "Plan nutricional hipocalórico para pérdida de peso"
        it[dietType] = DietType.WEIGHT_LOSS
        it[duration] = kotlin.time.Duration.parse("60d")
        it[createdBy] = trainer2Id
        it[startAt] = Instant.fromEpochMilliseconds(System.currentTimeMillis() - 2.weeks.inWholeMilliseconds)
    }

    // Actualizar atletas con sus rutinas y dietas
    Athletes.update({ Athletes.userId eq athlete1Id }) {
        it[workoutId] = workout1Id
        it[dietId] = diet1Id
    }

    Athletes.update({ Athletes.userId eq athlete2Id }) {
        it[workoutId] = workout2Id
        it[dietId] = diet2Id
    }

    // Platos para dieta de Ana
    DietDishes.insert {
        it[dietId] = diet1Id
        it[dishId] = oatmealId
        it[weekDay] = WeekDay.MONDAY
        it[amount] = 1.0
        it[mealType] = MealType.BREAKFAST
    }

    DietDishes.insert {
        it[dietId] = diet1Id
        it[dishId] = chickenBreastId
        it[weekDay] = WeekDay.MONDAY
        it[amount] = 120.0
        it[mealType] = MealType.LUNCH
    }

    // ============================================================================
    // CONVERSACIONES Y MENSAJES
    // ============================================================================

    val conversation1Id = Conversations.insertAndGetId {
        it[trainerId] = trainer1Id
        it[athleteId] = athlete1Id
        it[createdAt] = Instant.fromEpochMilliseconds(System.currentTimeMillis() - 5.days.inWholeMilliseconds)
    }

    val conversation2Id = Conversations.insertAndGetId {
        it[trainerId] = trainer2Id
        it[athleteId] = athlete2Id
        it[createdAt] = Instant.fromEpochMilliseconds(System.currentTimeMillis() - 10.days.inWholeMilliseconds)
    }

    val conversation3Id = Conversations.insertAndGetId {
        it[trainerId] = trainer1Id
        it[athleteId] = athlete3Id
        it[createdAt] = Instant.fromEpochMilliseconds(System.currentTimeMillis() - 3.days.inWholeMilliseconds)
    }

    val conversation4Id = Conversations.insertAndGetId {
        it[trainerId] = trainer3Id
        it[athleteId] = athlete4Id
        it[createdAt] = Instant.fromEpochMilliseconds(System.currentTimeMillis() - 1.days.inWholeMilliseconds)
    }

    // Mensajes para conversación Carlos-Ana
    Messages.insert {
        it[conversationId] = conversation1Id
        it[userName] = "Carlos Martínez"
        it[sentAt] = Instant.fromEpochMilliseconds(System.currentTimeMillis() - 2.days.inWholeMilliseconds)
        it[content] = "¡Hola Ana! ¿Cómo vas con la rutina de esta semana?"
        it[status] = MessageStatus.READ
    }

    Messages.insert {
        it[conversationId] = conversation1Id
        it[userName] = "Ana Rodríguez"
        it[sentAt] =
            Instant.fromEpochMilliseconds(System.currentTimeMillis() - 2.days.inWholeMilliseconds + 30 * 60 * 1000)
        it[content] = "¡Hola Carlos! Todo bien, aunque las flexiones me cuestan un poco más de lo esperado."
        it[status] = MessageStatus.READ
    }

    Messages.insert {
        it[conversationId] = conversation1Id
        it[userName] = "Carlos Martínez"
        it[sentAt] = Instant.fromEpochMilliseconds(System.currentTimeMillis() - 1.days.inWholeMilliseconds)
        it[content] = "Es normal al principio. Podemos reducir las repeticiones esta semana y aumentar gradualmente."
        it[status] = MessageStatus.READ
    }

    Messages.insert {
        it[conversationId] = conversation1Id
        it[userName] = "Ana Rodríguez"
        it[sentAt] = Instant.fromEpochMilliseconds(System.currentTimeMillis() - 3 * 60 * 60 * 1000)
        it[content] = "Perfecto, muchas gracias por el apoyo 💪"
        it[status] = MessageStatus.SENT
    }

    // Mensajes para conversación María-Pedro
    Messages.insert {
        it[conversationId] = conversation2Id
        it[userName] = "María García"
        it[sentAt] = Instant.fromEpochMilliseconds(System.currentTimeMillis() - 8.days.inWholeMilliseconds)
        it[content] = "Hola Pedro, ¿has podido revisar la dieta que te envié?"
        it[status] = MessageStatus.READ
    }

    Messages.insert {
        it[conversationId] = conversation2Id
        it[userName] = "Pedro Sánchez"
        it[sentAt] = Instant.fromEpochMilliseconds(System.currentTimeMillis() - 7.days.inWholeMilliseconds)
        it[content] = "Sí, la he visto. Tengo una duda sobre las cantidades del almuerzo."
        it[status] = MessageStatus.READ
    }

    Messages.insert {
        it[conversationId] = conversation2Id
        it[userName] = "María García"
        it[sentAt] = Instant.fromEpochMilliseconds(System.currentTimeMillis() - 6.days.inWholeMilliseconds)
        it[content] = "Claro, dime qué necesitas saber exactamente."
        it[status] = MessageStatus.READ
    }

    // ============================================================================
    // SESIONES DE ENTRENAMIENTO
    // ============================================================================

    val session1Id = Sessions.insertAndGetId {
        it[title] = "Sesión de Evaluación Inicial - Ana"
        it[dateTime] = Instant.fromEpochMilliseconds(System.currentTimeMillis() - 10.days.inWholeMilliseconds)
        it[sessionType] = SessionType.ASSESSMENT
        it[trainerId] = trainer1Id
        it[athleteId] = athlete1Id
        it[expectedDuration] = kotlin.time.Duration.parse("60m")
        it[actualDuration] = kotlin.time.Duration.parse("65m")
        it[completed] = true
    }

    val session2Id = Sessions.insertAndGetId {
        it[title] = "Entrenamiento Personal - Ana"
        it[dateTime] = Instant.fromEpochMilliseconds(System.currentTimeMillis() - 3.days.inWholeMilliseconds)
        it[sessionType] = SessionType.WORKOUT
        it[trainerId] = trainer1Id
        it[athleteId] = athlete1Id
        it[expectedDuration] = kotlin.time.Duration.parse("45m")
        it[actualDuration] = kotlin.time.Duration.parse("47m")
        it[completed] = true
    }

    val session3Id = Sessions.insertAndGetId {
        it[title] = "Consulta Nutricional - Pedro"
        it[dateTime] = Instant.fromEpochMilliseconds(System.currentTimeMillis() - 5.days.inWholeMilliseconds)
        it[sessionType] = SessionType.NUTRITION
        it[trainerId] = trainer2Id
        it[athleteId] = athlete2Id
        it[expectedDuration] = kotlin.time.Duration.parse("30m")
        it[actualDuration] = kotlin.time.Duration.parse("35m")
        it[completed] = true
    }

    val session4Id = Sessions.insertAndGetId {
        it[title] = "Entrenamiento Grupal - Funcional"
        it[dateTime] = Instant.fromEpochMilliseconds(System.currentTimeMillis() + 2.days.inWholeMilliseconds)
        it[sessionType] = SessionType.GROUP
        it[trainerId] = trainer2Id
        it[athleteId] = athlete2Id
        it[expectedDuration] = kotlin.time.Duration.parse("60m")
        it[actualDuration] = null
        it[completed] = false
    }

    val session5Id = Sessions.insertAndGetId {
        it[title] = "Sesión de Rehabilitación - Miguel"
        it[dateTime] = Instant.fromEpochMilliseconds(System.currentTimeMillis() + 1.days.inWholeMilliseconds)
        it[sessionType] = SessionType.WORKOUT
        it[trainerId] = trainer3Id
        it[athleteId] = athlete4Id
        it[expectedDuration] = kotlin.time.Duration.parse("45m")
        it[actualDuration] = null
        it[completed] = false
    }

    // ============================================================================
    // NOTAS
    // ============================================================================

    val note1Id = Notes.insertAndGetId {
        it[userId] = trainer1Id
        it[content] = "Ana muestra buen progreso en fuerza básica. Necesita trabajar más la flexibilidad en hombros."
        it[createdAt] = Instant.fromEpochMilliseconds(System.currentTimeMillis() - 3.days.inWholeMilliseconds)
    }

    val note2Id = Notes.insertAndGetId {
        it[userId] = trainer1Id
        it[content] = "Recordar aumentar progresión de flexiones para la próxima semana."
        it[createdAt] = Instant.fromEpochMilliseconds(System.currentTimeMillis() - 2.days.inWholeMilliseconds)
    }

    val note3Id = Notes.insertAndGetId {
        it[userId] = trainer2Id
        it[content] = "Pedro necesita más motivación. Considerar cambiar enfoque de entrenamiento a algo más divertido."
        it[createdAt] = Instant.fromEpochMilliseconds(System.currentTimeMillis() - 5.days.inWholeMilliseconds)
    }

    val note4Id = Notes.insertAndGetId {
        it[userId] = trainer2Id
        it[content] = "Revisar intolerancia al gluten de Pedro antes de modificar dieta."
        it[createdAt] = Instant.fromEpochMilliseconds(System.currentTimeMillis() - 7.days.inWholeMilliseconds)
    }

    val note5Id = Notes.insertAndGetId {
        it[userId] = trainer3Id
        it[content] = "Miguel tiene molestias en la rodilla izquierda. Evitar ejercicios de alto impacto."
        it[createdAt] = Instant.fromEpochMilliseconds(System.currentTimeMillis() - 1.days.inWholeMilliseconds)
    }

    val note6Id = Notes.insertAndGetId {
        it[userId] = athlete1Id
        it[content] = "Me siento más fuerte cada semana. Las sentadillas ya no me cuestan tanto."
        it[createdAt] = Instant.fromEpochMilliseconds(System.currentTimeMillis() - 1.days.inWholeMilliseconds)
    }

    // ============================================================================
    // ASOCIACIONES DE NOTAS
    // ============================================================================

    // Notas asociadas a entrenamientos
    WorkoutNotes.insert {
        it[workoutId] = workout1Id
        it[noteId] = note1Id
    }

    WorkoutNotes.insert {
        it[workoutId] = workout1Id
        it[noteId] = note2Id
    }

    WorkoutNotes.insert {
        it[workoutId] = workout2Id
        it[noteId] = note3Id
    }

    // Notas asociadas a ejercicios específicos en entrenamientos
    val workoutExercise1Id = WorkoutExercises.selectAll()
        .where { (WorkoutExercises.workoutId eq workout1Id) and (WorkoutExercises.exerciseId eq pushUpId) }
        .single()[WorkoutExercises.id]

    WorkoutExerciseNotes.insert {
        it[workoutExerciseId] = workoutExercise1Id
        it[noteId] = note2Id
    }

    // Notas asociadas a dietas
    DietNotes.insert {
        it[dietId] = diet2Id
        it[noteId] = note4Id
    }

    // Notas asociadas a sesiones
    SessionNotes.insert {
        it[sessionId] = session1Id
        it[noteId] = note1Id
    }

    SessionNotes.insert {
        it[sessionId] = session5Id
        it[noteId] = note5Id
    }

    // ============================================================================
    // ESTADÍSTICAS DE COMPLETADO DE ENTRENAMIENTOS
    // ============================================================================

    CompletionWorkoutStatistics.insert {
        it[workoutId] = workout1Id
        it[athleteId] = athlete1Id
        it[completeAt] = Instant.fromEpochMilliseconds(System.currentTimeMillis() - 6.days.inWholeMilliseconds)
    }

    CompletionWorkoutStatistics.insert {
        it[workoutId] = workout1Id
        it[athleteId] = athlete1Id
        it[completeAt] = Instant.fromEpochMilliseconds(System.currentTimeMillis() - 4.days.inWholeMilliseconds)
    }

    CompletionWorkoutStatistics.insert {
        it[workoutId] = workout1Id
        it[athleteId] = athlete1Id
        it[completeAt] = Instant.fromEpochMilliseconds(System.currentTimeMillis() - 2.days.inWholeMilliseconds)
    }

    CompletionWorkoutStatistics.insert {
        it[workoutId] = workout2Id
        it[athleteId] = athlete2Id
        it[completeAt] = Instant.fromEpochMilliseconds(System.currentTimeMillis() - 8.days.inWholeMilliseconds)
    }

    CompletionWorkoutStatistics.insert {
        it[workoutId] = workout2Id
        it[athleteId] = athlete2Id
        it[completeAt] = Instant.fromEpochMilliseconds(System.currentTimeMillis() - 5.days.inWholeMilliseconds)
    }

    // ============================================================================
    // ESTADÍSTICAS DE COMPLETADO DE DIETAS
    // ============================================================================

    CompletionDietStatistics.insert {
        it[dietId] = diet1Id
        it[athleteId] = athlete1Id
        it[completeAt] = Instant.fromEpochMilliseconds(System.currentTimeMillis() - 6.days.inWholeMilliseconds)
    }

    CompletionDietStatistics.insert {
        it[dietId] = diet1Id
        it[athleteId] = athlete1Id
        it[completeAt] = Instant.fromEpochMilliseconds(System.currentTimeMillis() - 5.days.inWholeMilliseconds)
    }

    CompletionDietStatistics.insert {
        it[dietId] = diet1Id
        it[athleteId] = athlete1Id
        it[completeAt] = Instant.fromEpochMilliseconds(System.currentTimeMillis() - 4.days.inWholeMilliseconds)
    }

    CompletionDietStatistics.insert {
        it[dietId] = diet2Id
        it[athleteId] = athlete2Id
        it[completeAt] = Instant.fromEpochMilliseconds(System.currentTimeMillis() - 10.days.inWholeMilliseconds)
    }

    CompletionDietStatistics.insert {
        it[dietId] = diet2Id
        it[athleteId] = athlete2Id
        it[completeAt] = Instant.fromEpochMilliseconds(System.currentTimeMillis() - 7.days.inWholeMilliseconds)
    }

    // ============================================================================
    // MÁS EJERCICIOS PARA LA BASE DE DATOS
    // ============================================================================

    val deadliftId = Exercises.insertAndGetId {
        it[name] = "Peso muerto"
        it[description] = "Ejercicio compuesto que trabaja toda la cadena posterior"
        it[bodyPart] = BodyPart.BACK
    }

    val benchPressId = Exercises.insertAndGetId {
        it[name] = "Press de banca"
        it[description] = "Ejercicio básico de empuje horizontal para pecho"
        it[bodyPart] = BodyPart.CHEST
    }

    val rowId = Exercises.insertAndGetId {
        it[name] = "Remo con barra"
        it[description] = "Ejercicio de tracción horizontal para espalda"
        it[bodyPart] = BodyPart.BACK
    }

    val calfRaiseId = Exercises.insertAndGetId {
        it[name] = "Elevación de gemelos"
        it[description] = "Ejercicio de aislamiento para pantorrillas"
        it[bodyPart] = BodyPart.LEG
    }

    val crunchId = Exercises.insertAndGetId {
        it[name] = "Abdominales"
        it[description] = "Ejercicio básico para el recto abdominal"
        it[bodyPart] = BodyPart.CORE
    }

    // ============================================================================
    // MÁS PLATOS PARA LAS DIETAS
    // ============================================================================

    val quinoaId = Dishes.insertAndGetId {
        it[name] = "Quinoa con verduras"
    }

    val proteinShakeId = Dishes.insertAndGetId {
        it[name] = "Batido de proteínas"
    }

    val eggWhiteOmeletId = Dishes.insertAndGetId {
        it[name] = "Tortilla de claras"
    }

    val avocadoToastId = Dishes.insertAndGetId {
        it[name] = "Tostada de aguacate"
    }

    val grilledVegetablesId = Dishes.insertAndGetId {
        it[name] = "Verduras a la parrilla"
    }

    // Completar la dieta de Pedro con más comidas
    DietDishes.insert {
        it[dietId] = diet2Id
        it[dishId] = eggWhiteOmeletId
        it[weekDay] = WeekDay.MONDAY
        it[amount] = 1.0
        it[mealType] = MealType.BREAKFAST
    }

    DietDishes.insert {
        it[dietId] = diet2Id
        it[dishId] = tunaId
        it[weekDay] = WeekDay.MONDAY
        it[amount] = 150.0
        it[mealType] = MealType.LUNCH
    }

    DietDishes.insert {
        it[dietId] = diet2Id
        it[dishId] = quinoaId
        it[weekDay] = WeekDay.MONDAY
        it[amount] = 100.0
        it[mealType] = MealType.LUNCH
    }

    DietDishes.insert {
        it[dietId] = diet2Id
        it[dishId] = proteinShakeId
        it[weekDay] = WeekDay.MONDAY
        it[amount] = 1.0
        it[mealType] = MealType.SNACK
    }

    DietDishes.insert {
        it[dietId] = diet2Id
        it[dishId] = chickenBreastId
        it[weekDay] = WeekDay.MONDAY
        it[amount] = 120.0
        it[mealType] = MealType.DINNER
    }

    DietDishes.insert {
        it[dietId] = diet2Id
        it[dishId] = grilledVegetablesId
        it[weekDay] = WeekDay.MONDAY
        it[amount] = 200.0
        it[mealType] = MealType.DINNER
    }

    // ============================================================================
    // CLAVES DE REGISTRO PARA ENTRENADORES
    // ============================================================================

    RegistKeyTable.insert {
        it[key] = "TRAINER_KEY_2024_CARLOS_001"
        it[trainer] = trainerRecord1Id
    }

    RegistKeyTable.insert {
        it[key] = "TRAINER_KEY_2024_MARIA_002"
        it[trainer] = trainerRecord2Id
    }

    RegistKeyTable.insert {
        it[key] = "TRAINER_KEY_2024_DAVID_003"
        it[trainer] = trainerRecord3Id
    }

    // Claves adicionales para cada entrenador
    RegistKeyTable.insert {
        it[key] = "INVITE_CARLOS_STRENGTH_2024"
        it[trainer] = trainerRecord1Id
    }

    RegistKeyTable.insert {
        it[key] = "INVITE_MARIA_FUNCTIONAL_2024"
        it[trainer] = trainerRecord2Id
    }

    // ============================================================================
    // MEDIDAS HISTÓRICAS ADICIONALES
    // ============================================================================

    // Medidas anteriores para Ana (para ver progreso)
    Measurements.insert {
        it[athleteId] = athlete1Id
        it[weight] = 67.2
        it[height] = 168.0
        it[bodyFat] = 20.1
        it[armSize] = 27.5
        it[chestBackSize] = 89.5
        it[hipSize] = 94.0
        it[legSize] = 57.0
        it[calvesSize] = 35.5
        it[measureAt] = Instant.fromEpochMilliseconds(System.currentTimeMillis() - 4.weeks.inWholeMilliseconds)
        it[isCurrent] = false
    }

    // Medidas anteriores para Pedro
    Measurements.insert {
        it[athleteId] = athlete2Id
        it[weight] = 85.1
        it[height] = 175.0
        it[bodyFat] = 24.5
        it[armSize] = 36.0
        it[chestBackSize] = 101.0
        it[hipSize] = 98.0
        it[legSize] = 63.5
        it[calvesSize] = 41.0
        it[measureAt] = Instant.fromEpochMilliseconds(System.currentTimeMillis() - 6.weeks.inWholeMilliseconds)
        it[isCurrent] = false
    }

}


// Extensión para facilitar el cálculo de fechas
private val Int.weeks get() = (this * 7).days
private val Int.months get() = (this * 30).days
private val Int.years get() = (this * 365).days

suspend fun <T> suspendTransaction(block: Transaction.() -> T): T =
    newSuspendedTransaction(Dispatchers.IO, statement = block)
