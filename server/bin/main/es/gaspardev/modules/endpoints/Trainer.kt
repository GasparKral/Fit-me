package es.gaspardev.modules.endpoints

import es.gaspardev.core.domain.dtos.DashboardChartInfo
import es.gaspardev.core.domain.entities.users.Athlete
import es.gaspardev.core.domain.entities.users.Trainer
import es.gaspardev.database.daos.*
import es.gaspardev.database.entities.MessageEntity
import es.gaspardev.database.entities.TrainerEntity
import es.gaspardev.enums.MessageStatus
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.time.DurationUnit
import kotlin.time.toDuration

fun Route.trainer() {

    route(Trainer.URLPATH) {

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

            get("/session") {
                val trainerID = call.parameters["trainer_id"]?.toInt()
                    ?: return@get call.respondText("Parámetro trainer_id requerido", status = HttpStatusCode.BadRequest)

                try {
                    val result = transaction {
                        TrainerEntity.all()
                            .first { it.user.id.value == trainerID }.sessions.count {
                                !it.completed && it.dateTime in Clock.System.now()..Clock.System.now()
                                    .plus(2.toDuration(DurationUnit.DAYS))
                            }
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


