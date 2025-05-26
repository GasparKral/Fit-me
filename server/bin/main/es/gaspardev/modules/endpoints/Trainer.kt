package es.gaspardev.modules.endpoints

import es.gaspardev.core.domain.dtos.DashboardChartInfo
import es.gaspardev.core.domain.entities.Trainer
import es.gaspardev.db.*
import es.gaspardev.db.mappings.diets.CompletedDietDao
import es.gaspardev.db.mappings.diets.CompletedDietEntity
import es.gaspardev.db.mappings.users.TrainerDao
import es.gaspardev.db.mappings.users.TrainerEntity
import es.gaspardev.db.mappings.workouts.CompletedWorkoutDao
import es.gaspardev.db.mappings.workouts.CompletedWorkoutEntity
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.selectAll

fun Application.trainer() {

    routing {

        // LOGIN
        get(Trainer.URLPATH) {
            val userIdentification = call.request.queryParameters["userIdentification"]
            val userPasswordHash = call.request.queryParameters["userPassword"]

            if (userIdentification != null && userPasswordHash != null) {
                val trainer = suspendTransaction {
                    TrainerEntity.all().firstOrNull {
                        (it.user.email == userIdentification || it.user.name == userIdentification) &&
                                it.user.password == userPasswordHash
                    }?.let {
                        TrainerDao().toDomain(
                            it
                        )
                    }
                }

                if (trainer != null) {
                    call.respond(trainer)
                } else {
                    call.respondText("Entrenador no encontrado", status = HttpStatusCode.NotFound)
                }
            } else {
                call.respondText("Parámetros requeridos faltantes", status = HttpStatusCode.BadRequest)
            }
        }

        // NECESITAN ATENCION
        get(Trainer.URLPATH + "/pending") {
            val trainerID = call.request.queryParameters["trainer_id"]

            if (trainerID != null) {
                val value = suspendTransaction {
                    UsersTable.join(SportsmenTable, JoinType.INNER, UsersTable.id, SportsmenTable.userId).selectAll()
                        .where(
                            SportsmenTable.trainerId eq trainerID.toInt() and
                                    UsersTable.needsAttention
                        ).count()
                }
                call.respond(value)
            } else {
                call.respondText("Parámetros requeridos faltantes", status = HttpStatusCode.BadRequest)
            }
        }

        // DATOS DASHBOARD ENTRENADOR
        get(Trainer.URLPATH + "/data/dashboardChartInfo") {
            val trainerID = call.request.queryParameters["trainer_id"]

            if (trainerID != null) {
                val now = Clock.System.now()
                val oneMonthAgo = now.minus(DateTimePeriod(months = 1), TimeZone.currentSystemDefault())
                try {

                    val completedWorkouts = suspendTransaction {
                        CompletedWorkoutEntity
                            .all()
                            .filter {
                                it.completedAt in oneMonthAgo..now &&
                                        it.sportsman.trainer?.user?.id?.value == trainerID.toInt()
                            }
                            .map {
                                CompletedWorkoutDao().toDomain(it)
                            }
                    }

                    val completedDiets = suspendTransaction {
                        CompletedDietEntity.all()
                            .filter {
                                it.completeAt in oneMonthAgo..now &&
                                        it.sportsman.trainer?.user?.id?.value == trainerID.toInt()
                            }.map {
                                CompletedDietDao().toDomain(it)
                            }
                    }

                    call.respond(DashboardChartInfo(completedWorkouts, completedDiets))

                } catch (e: Exception) {
                    call.respondText(e.message!!, status = HttpStatusCode.ExpectationFailed)
                }
            } else {
                call.respondText("Parámetros requeridos faltantes", status = HttpStatusCode.BadRequest)
            }
        }

    }

}
