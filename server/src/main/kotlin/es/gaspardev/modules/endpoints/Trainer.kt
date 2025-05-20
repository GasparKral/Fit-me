package es.gaspardev.modules.endpoints

import es.gaspardev.core.domain.entities.Trainer
import es.gaspardev.db.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.datetime.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.between
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq


fun Application.trainer() {

    routing {

        get(Trainer.URLPATH) {
            val userIdentification = call.request.queryParameters["userIdentification"]
            val userPassword = call.request.queryParameters["userPassword"]
            if (userIdentification != null && userPassword != null) {
                val trainer = suspendTransaction {
                    // Primero encontrar el usuario que coincide con las credenciales
                    val user = UserDAO.find {
                        (UserTable.name eq userIdentification or (UserTable.email eq userIdentification)) and
                                (UserTable.password eq userPassword)
                    }.firstOrNull()

                    // Luego encontrar el entrenador asociado a ese usuario
                    user?.let {
                        TrainerDAO.find { TrainerTable.userId eq it.id }.firstOrNull()?.toModel()
                    }
                }
                if (trainer != null) {
                    call.respond(HttpStatusCode.OK, trainer)
                } else {
                    call.respondText("Entrenadir no encontrado", status = HttpStatusCode.NotFound)
                }
            } else {
                call.respondText("Parámetros requeridos faltantes", status = HttpStatusCode.BadRequest)
            }
        }

        get(Trainer.URLPATH + "/pending") {
            val trainerID = call.request.queryParameters["trainer_id"]

            if (trainerID != null) {
                val value =
                    WorkoutsTable.select(WorkoutsTable.requiresAssistance)
                        .where(WorkoutsTable.author eq trainerID.toInt()).count()
            } else {
                call.respondText("Parámetros requeridos faltantes", status = HttpStatusCode.BadRequest)
            }
        }

        get(Trainer.URLPATH + "/data/dashboardChartInfo") {
            val trainerID = call.request.queryParameters["trainer_id"]

            if (trainerID != null) {
                val now = Clock.System.now()
                val oneMonthAgo = now.minus(DateTimePeriod(months = 1), TimeZone.currentSystemDefault())

                val workoutsResult = WorkoutProgress
                    .join(WorkoutsTable, JoinType.INNER, WorkoutProgress.workout, WorkoutsTable.id)
                    .select(WorkoutProgress.completionDate, WorkoutProgress.completionDate.countDistinct())
                    .where(
                        (WorkoutsTable.author eq trainerID.toInt()) and
                                (WorkoutProgress.completed.eq(true)) and
                                (WorkoutProgress.completionDate.between(oneMonthAgo, now))
                    )
                    .groupBy(WorkoutProgress.completionDate)
                    .map {
                        Pair(
                            it[WorkoutProgress.completionDate],
                            it[WorkoutProgress.completionDate.countDistinct()]
                        )
                    }


                val dietsResult =
                    DietTable.join(SportsmanTable, JoinType.INNER, DietTable.sportsmanId, SportsmanTable.userId)
                        .selectAll()
                        .where(SportsmanTable.trainerId eq trainerID.toInt())
                        .map {
                            val initialDate = it[DietTable.initialDate]
                            val durationDays = it[DietTable.duration]
                            val endDate =
                                Instant.fromEpochMilliseconds(initialDate.toEpochMilliseconds() + durationDays)
                            it to endDate
                        }
                        .filter { (_, endDate) ->
                            endDate in oneMonthAgo..now
                        }
                        .groupBy { it.second }
                        .map { Pair(it.key, it.value.size.toLong()) }
                call.respond(arrayOf(workoutsResult, dietsResult))

            } else {
                call.respondText("Parámetros requeridos faltantes", status = HttpStatusCode.BadRequest)
            }
        }

    }

}
