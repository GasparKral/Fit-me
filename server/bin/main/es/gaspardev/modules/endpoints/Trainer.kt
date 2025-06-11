package es.gaspardev.modules.endpoints

import es.gaspardev.core.domain.dtos.DashboardChartInfo
import es.gaspardev.core.domain.dtos.RegisterTrainerData
import es.gaspardev.core.domain.entities.users.Athlete
import es.gaspardev.core.domain.entities.users.Trainer
import es.gaspardev.database.RegistKeyTable
import es.gaspardev.database.Trainers
import es.gaspardev.database.daos.*
import es.gaspardev.database.entities.MessageEntity
import es.gaspardev.database.entities.TrainerEntity
import es.gaspardev.enums.MessageStatus
import es.gaspardev.modules.endpoints.suspendTransaction
import es.gaspardev.utils.encrypt
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.time.DurationUnit
import kotlin.time.toDuration

fun Route.trainer() {

    route(Trainer.URLPATH) {

        // Endpoint para registro de nuevos entrenadores
        post("/register") {
            try {
                val registerData = call.receive<RegisterTrainerData>()

                // Validar datos básicos
                if (registerData.userName.isBlank()) {
                    return@post call.respondText(
                        "El nombre de usuario es requerido",
                        status = HttpStatusCode.BadRequest
                    )
                }

                if (registerData.email.isBlank()) {
                    return@post call.respondText(
                        "El email es requerido",
                        status = HttpStatusCode.BadRequest
                    )
                }

                if (registerData.password.isBlank()) {
                    return@post call.respondText(
                        "La contraseña es requerida",
                        status = HttpStatusCode.BadRequest
                    )
                }

                if (registerData.specialization.isNullOrBlank()) {
                    return@post call.respondText(
                        "La especialización es requerida",
                        status = HttpStatusCode.BadRequest
                    )
                }

                val result = suspendTransaction {
                    // Verificar si el email ya existe
                    val existingUser = UserDao().findUserByEmail(registerData.email)
                    if (existingUser != null) {
                        return@suspendTransaction null
                    }

                    // Crear usuario
                    val user = UserDao().createUser(
                        fullname = registerData.userName,
                        password = encrypt(registerData.password),
                        email = registerData.email,
                        phone = "", // Por ahora vacío, se puede añadir al formulario después
                    )

                    // Crear entrenador
                    val trainer = TrainerDao.createTrainer(
                        userId = user.id.value,
                        specialization = registerData.specialization!!, // Ya validamos que no sea null
                        yearsOfExperience = registerData.yearsOfExperience
                    )

                    trainer.toModel()
                }

                if (result != null) {
                    call.respond(HttpStatusCode.Created, result)
                } else {
                    call.respondText(
                        "El email ya está registrado",
                        status = HttpStatusCode.Conflict
                    )
                }

            } catch (e: Exception) {
                call.respondText(
                    "Error interno del servidor: ${e.message}",
                    status = HttpStatusCode.InternalServerError
                )
            }
        }

        delete("/acount") {
            val userId = call.queryParameters["user_id"]?.toInt() ?: return@delete call.respondText(
                "Parámetros requeridos faltantes",
                status = HttpStatusCode.BadRequest
            )

            try {
                TrainerDao.deleteTrainer(userId)
            } catch (e: Exception) {
                call.respondText("Error al eliminar: ${e.message!!}", status = HttpStatusCode.ExpectationFailed)
            }
        }

        get("/{userIdentification}/{userPassword}") {
            val userIdentification = call.parameters["userIdentification"]
            val userPasswordHash = call.parameters["userPassword"]

            if (userIdentification == null || userPasswordHash == null) return@get call.respondText(
                "Parámetros requeridos faltantes",
                status = HttpStatusCode.BadRequest
            )

            val trainer: Trainer = TrainerDao.findTrainerByCredencials(userIdentification, userPasswordHash)
                ?: return@get call.respondText("Entrenador no encontrado", status = HttpStatusCode.NotFound)
            val athletes: List<Athlete> = TrainerDao.getAthletes(trainer.user.id)
            val conversations = CommunicationDao.getConversations(trainer.user.id)
            call.respond(Triple(trainer, athletes, conversations))
        }

        get("{trainer_id}/comunication") {
            val trainerID = call.parameters["trainer_id"]?.toInt()
                ?: return@get call.respondText("Parámetro trainer_id requerido", status = HttpStatusCode.BadRequest)

            call.respond(CommunicationDao.getConversations(trainerID))
        }

        get("/key_gen") {
            val trainerID = call.request.queryParameters["trainer_id"]
            if (trainerID != null) {
                val keyValue = encrypt(trainerID + Clock.System.now().toString())
                suspendTransaction {
                    RegistKeyTable.insert {
                        it[key] = keyValue
                        it[trainer] =
                            Trainers.select(Trainers.id).where(Trainers.id eq trainerID.toInt())
                    }
                }
                call.respond(keyValue)
            } else {
                call.respondText("Parámetros requeridos faltantes", status = HttpStatusCode.BadRequest)
            }
        }

        route("/data/{trainer_id}") {

            get("pending") {
                val trainerID = call.parameters["trainer_id"]?.toInt()
                    ?: return@get call.respondText("Parámetro trainer_id requerido", status = HttpStatusCode.BadRequest)
                call.respond(AthleteDao.needAssistant(trainerID))
            }

            // DATOS DASHBOARD ENTRENADOR
            get("dashboardChartInfo") {
                val trainerID = call.parameters["trainer_id"]?.toInt()
                    ?: return@get call.respondText("Parámetro trainer_id requerido", status = HttpStatusCode.BadRequest)

                try {
                    val now = Clock.System.now()
                    val oneMonthAgo = now.minus(DateTimePeriod(months = 1), TimeZone.currentSystemDefault())

                    val result = transaction {
                        val workouts =
                            WorkoutDao.getCompletedWorkoutsInTimeRange(oneMonthAgo, now, trainerID)
                        val diets = DietDao.getCompletedDietsInTimeRange(oneMonthAgo, now, trainerID)
                        DashboardChartInfo(workouts, diets)
                    }
                    call.respond(result)

                } catch (e: Exception) {
                    call.respondText(e.message!!, status = HttpStatusCode.ExpectationFailed)
                }

            }

            get("/plans") {
                val trainerID = call.parameters["trainer_id"]?.toInt()
                    ?: return@get call.respondText("Parámetro trainer_id requerido", status = HttpStatusCode.BadRequest)

                try {
                    val result = transaction {
                        val workouts = TrainerDao.getAthletes(trainerID).mapNotNull { it.workout }.count()
                        val diet = TrainerDao.getAthletes(trainerID).mapNotNull { it.diet }.count()
                        workouts + diet
                    }
                    call.respond(result)
                } catch (e: Exception) {
                    call.respondText(e.message!!, status = HttpStatusCode.ExpectationFailed)
                }
            }

            get("/messages") {
                val trainerID = call.parameters["trainer_id"]?.toInt()
                    ?: return@get call.respondText("Parámetro trainer_id requerido", status = HttpStatusCode.BadRequest)

                try {
                    val result = transaction {
                        MessageEntity.all()
                            .count { it.status == MessageStatus.DELIVERED && it.conversation.trainer.id.value == trainerID }
                    }
                    call.respond(result)
                } catch (e: Exception) {
                    call.respondText(e.message!!, status = HttpStatusCode.ExpectationFailed)
                }
            }
            get("/new_athletes") {
                val trainerID = call.parameters["trainer_id"]?.toInt()
                    ?: return@get call.respondText("Parámetro trainer_id requerido", status = HttpStatusCode.BadRequest)

                val result = transaction {
                    TrainerDao.getAthletes(trainerID).count {
                        it.trainingSince in Clock.System.now()
                            .minus(1.toDuration(DurationUnit.DAYS))..Clock.System.now()
                    }
                }
                call.respond(result)
                try {
                } catch (e: Exception) {
                    call.respondText(e.message!!, status = HttpStatusCode.ExpectationFailed)
                }
            }
            get("/new_active_plans") {
                val trainerID = call.parameters["trainer_id"]?.toInt()
                    ?: return@get call.respondText("Parámetro trainer_id requerido", status = HttpStatusCode.BadRequest)

                try {
                    val result = transaction {
                        val workouts = TrainerDao.getAthletes(trainerID)
                            .map {
                                it.workout != null && it.trainingSince in Clock.System.now()
                                    .minus(30.toDuration(DurationUnit.DAYS))..Clock.System.now()
                            }.count()
                        val diet = TrainerDao.getAthletes(trainerID).map {
                            it.diet != null && it.trainingSince in Clock.System.now()
                                .minus(30.toDuration(DurationUnit.DAYS))..Clock.System.now()
                        }.count()

                        workouts + diet
                    }
                    call.respond(result)
                } catch (e: Exception) {
                    call.respondText(e.message!!, status = HttpStatusCode.ExpectationFailed)
                }
            }
            get("/new_messages") {
                val trainerID = call.parameters["trainer_id"]?.toInt()
                    ?: return@get call.respondText("Parámetro trainer_id requerido", status = HttpStatusCode.BadRequest)

                try {
                    val result = transaction {
                        MessageEntity.all()
                            .count {
                                it.status == MessageStatus.DELIVERED && it.conversation.trainer.id.value == trainerID && it.sentAt in Clock.System.now()
                                    .minus(1.toDuration(DurationUnit.DAYS))..Clock.System.now()
                            }
                    }
                    call.respond(result)
                } catch (e: Exception) {
                    call.respondText(e.message!!, status = HttpStatusCode.ExpectationFailed)
                }
            }
        }
    }
}


