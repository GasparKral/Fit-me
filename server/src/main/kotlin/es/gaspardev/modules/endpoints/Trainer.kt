package es.gaspardev.modules.endpoints

import es.gaspardev.core.domain.dtos.DashboardChartInfo
import es.gaspardev.core.domain.entities.users.Athlete
import es.gaspardev.core.domain.entities.users.Trainer
import es.gaspardev.database.daos.*
import es.gaspardev.database.entities.MessageEntity
import es.gaspardev.database.entities.TrainerEntity
import es.gaspardev.enums.MessageStatus
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.time.DurationUnit
import kotlin.time.toDuration

fun Application.trainer() {

    routing {

        // LOGIN
        get(Trainer.URLPATH) {
            val userIdentification = call.request.queryParameters["userIdentification"]
            val userPasswordHash = call.request.queryParameters["userPassword"]

            if (userIdentification != null && userPasswordHash != null) {
                var trainer: Trainer? = null
                var athletes: List<Athlete> = listOf()
                suspendTransaction {
                    trainer = TrainerDao().findTrainerByCredencials(userIdentification, userPasswordHash)?.toModel()
                    athletes = TrainerDao().getAthletes(trainer?.user?.id.toString()).map { it.toModel() }
                }

                if (trainer != null) {
                    call.respond(Pair(trainer!!, athletes))
                } else {
                    call.respondText("Entrenador no encontrado", status = HttpStatusCode.NotFound)
                }
            } else {
                call.respondText("Parámetros requeridos faltantes", status = HttpStatusCode.BadRequest)
            }
        }

        // NECESITAN ATENCION
        get("${Trainer.URLPATH}/data/pending") {
            val trainerID = call.request.queryParameters["trainer_id"]
                ?: return@get call.respondText("Parámetro trainer_id requerido", status = HttpStatusCode.BadRequest)

            val value = AthleteDao().needAssistant(trainerID)
            call.respond(value)
        }

        // DATOS DASHBOARD ENTRENADOR
        get("${Trainer.URLPATH}/data/dashboardChartInfo") {
            val trainerID = call.request.queryParameters["trainer_id"]
                ?: return@get call.respondText("Parámetro trainer_id requerido", status = HttpStatusCode.BadRequest)

            try {
                val now = Clock.System.now()
                val oneMonthAgo = now.minus(DateTimePeriod(months = 1), TimeZone.currentSystemDefault())

                val result = transaction {
                    val workouts =
                        WorkoutDao().getCompletedWorkoutsInTimeRange(oneMonthAgo, now, trainerID)
                    val diets = DietDao().getCompletedDietsInTimeRange(oneMonthAgo, now, trainerID)
                    DashboardChartInfo(workouts, listOf())
                }
                call.respond(result)

            } catch (e: Exception) {
                call.respondText(e.message!!, status = HttpStatusCode.ExpectationFailed)
            }

        }

        get("${Trainer.URLPATH}/data/plans") {
            val trainerID = call.request.queryParameters["trainer_id"]
                ?: return@get call.respondText("Parámetro trainer_id requerido", status = HttpStatusCode.BadRequest)

            try {
                val result = transaction {
                    val workouts = TrainerDao().getAthletes(trainerID).mapNotNull { it.workout }.count()
                    val diet = TrainerDao().getAthletes(trainerID).mapNotNull { it.diet }.count()
                    workouts + diet
                }
                call.respond(result)
            } catch (e: Exception) {
                call.respondText(e.message!!, status = HttpStatusCode.ExpectationFailed)
            }
        }

        get("${Trainer.URLPATH}/data/session") {
            val trainerID = call.request.queryParameters["trainer_id"]
                ?: return@get call.respondText("Parámetro trainer_id requerido", status = HttpStatusCode.BadRequest)

            try {
                val result = transaction {
                    TrainerEntity.all()
                        .first { it.user.id.value == trainerID.toInt() }.sessions.count {
                            !it.completed && it.dateTime in Clock.System.now()..Clock.System.now()
                                .plus(2.toDuration(DurationUnit.DAYS))
                        }
                }
                call.respond(result)
            } catch (e: Exception) {
                call.respondText(e.message!!, status = HttpStatusCode.ExpectationFailed)
            }
        }
        get("${Trainer.URLPATH}/data/messages") {
            val trainerID = call.request.queryParameters["trainer_id"]
                ?: return@get call.respondText("Parámetro trainer_id requerido", status = HttpStatusCode.BadRequest)

            try {
                val result = transaction {
                    MessageEntity.all()
                        .count { it.status == MessageStatus.DELIVERED && it.conversation.trainer.id.value == trainerID.toInt() }
                }
                call.respond(result)
            } catch (e: Exception) {
                call.respondText(e.message!!, status = HttpStatusCode.ExpectationFailed)
            }
        }
        get("${Trainer.URLPATH}/data/new_athletes") {
            val trainerID = call.request.queryParameters["trainer_id"]
                ?: return@get call.respondText("Parámetro trainer_id requerido", status = HttpStatusCode.BadRequest)

            val result = transaction {
                TrainerDao().getAthletes(trainerID).count {
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
        get("${Trainer.URLPATH}/data/new_active_plans") {
            val trainerID = call.request.queryParameters["trainer_id"]
                ?: return@get call.respondText("Parámetro trainer_id requerido", status = HttpStatusCode.BadRequest)

            try {
                val result = transaction {
                    val workouts = TrainerDao().getAthletes(trainerID)
                        .map {
                            it.workout != null && it.trainingSince in Clock.System.now()
                                .minus(30.toDuration(DurationUnit.DAYS))..Clock.System.now()
                        }.count()
                    val diet = TrainerDao().getAthletes(trainerID).map {
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
        get("${Trainer.URLPATH}/data/new_messages") {
            val trainerID = call.request.queryParameters["trainer_id"]
                ?: return@get call.respondText("Parámetro trainer_id requerido", status = HttpStatusCode.BadRequest)

            try {
                val result = transaction {
                    MessageEntity.all()
                        .count {
                            it.status == MessageStatus.DELIVERED && it.conversation.trainer.id.value == trainerID.toInt() && it.sentAt in Clock.System.now()
                                .minus(1.toDuration(DurationUnit.DAYS))..Clock.System.now()
                        }
                }
                call.respond(result)
            } catch (e: Exception) {
                call.respondText(e.message!!, status = HttpStatusCode.ExpectationFailed)
            }
        }

        get("${Trainer.URLPATH}/comunication") {
            val userID = call.request.queryParameters["user_id"]
                ?: return@get call.respondText("Parámetro user_id requerido", status = HttpStatusCode.BadRequest)

            try {
                // Validate user ID format
                val userIdInt = userID.toIntOrNull()
                    ?: return@get call.respondText(
                        "user_id debe ser un número válido",
                        status = HttpStatusCode.BadRequest
                    )

                val result = transaction {
                    CommunicationDao().getConversations(userIdInt).map { it.toModel() }
                }
                call.respond(result)
            } catch (e: NumberFormatException) {
                call.respondText("user_id debe ser un número válido", status = HttpStatusCode.BadRequest)
            } catch (e: Exception) {
                println("Error in communication endpoint: ${e.message}")
                e.printStackTrace()
                call.respondText("Error interno del servidor", status = HttpStatusCode.InternalServerError)
            }
        }

    }

}
