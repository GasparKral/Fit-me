package es.gaspardev.modules.endpoints

import es.gaspardev.core.domain.entities.stadistics.*
import es.gaspardev.database.daos.StatisticsDao
import es.gaspardev.database.entities.AthleteEntity
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

fun Route.statistics() {

    route("/statistics") {

        // Obtener estadísticas generales del atleta
        get("/{athlete_id}") {
            val athleteId = call.parameters["athlete_id"]?.toInt() ?: return@get call.respondText(
                "Parámetro requerido",
                status = HttpStatusCode.BadRequest
            )
            val timeRange = try {
                call.request.queryParameters["timeRange"]?.let { Duration.parse(it) }
                    ?: 30.toDuration(DurationUnit.DAYS)
            } catch (_: Exception) {
                30.toDuration(DurationUnit.DAYS)
            }

            try {
                val strengthStats = StatisticsDao.getStrengthStatistics(athleteId, timeRange)
                val enduranceStats = StatisticsDao.getEnduranceStatistics(athleteId, timeRange)
                val measurementHistory = StatisticsDao.getMeasurementHistory(athleteId, timeRange)

                // Generar datos para gráficos
                val strengthChartData = generateStrengthChartData(strengthStats)
                val enduranceChartData = generateEnduranceChartData(enduranceStats)
                val measurementChartData = generateMeasurementChartData(measurementHistory)

                val athlete = transaction {
                    AthleteEntity.getByUserId(athleteId).toModel()
                }

                val comprehensiveStats = ComprehensiveStatistics(
                    athlete = athlete,
                    timeRange = timeRange,
                    strengthStats = strengthStats,
                    enduranceStats = enduranceStats,
                    measurementHistory = measurementHistory,
                    strengthChartData = strengthChartData,
                    enduranceChartData = enduranceChartData,
                    measurementChartData = measurementChartData
                )

                call.respond(HttpStatusCode.OK, comprehensiveStats)

            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "Error retrieving statistics: ${e.message}")
            }
        }

        // Obtener solo estadísticas de fuerza
        get("/strength/{athleteId}") {
            val athleteId = call.parameters["athleteId"]?.toIntOrNull()
            val timeRange = call.request.queryParameters["timeRange"]?.let { Duration.parse(it) } ?: 30.toDuration(
                DurationUnit.DAYS
            )
            if (athleteId == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid athlete ID")
                return@get
            }

            try {
                val strengthStats = StatisticsDao.getStrengthStatistics(athleteId, timeRange)
                val chartData = generateStrengthChartData(strengthStats)

                val response = mapOf(
                    "statistics" to strengthStats,
                    "chartData" to chartData
                )

                call.respond(HttpStatusCode.OK, response)

            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "Error retrieving strength statistics: ${e.message}")
            }
        }

        // Obtener solo estadísticas de resistencia
        get("/endurance/{athleteId}") {
            val athleteId = call.parameters["athleteId"]?.toIntOrNull()
            val timeRange = call.request.queryParameters["timeRange"]?.let { Duration.parse(it) } ?: 30.toDuration(
                DurationUnit.DAYS
            )
            if (athleteId == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid athlete ID")
                return@get
            }

            try {
                val enduranceStats = StatisticsDao.getEnduranceStatistics(athleteId, timeRange)
                val chartData = generateEnduranceChartData(enduranceStats)

                val response = mapOf(
                    "statistics" to enduranceStats,
                    "chartData" to chartData
                )

                call.respond(HttpStatusCode.OK, response)

            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "Error retrieving endurance statistics: ${e.message}")
            }
        }

        // Obtener historial de medidas corporales
        get("/measurements/{athleteId}") {
            val athleteId = call.parameters["athleteId"]?.toIntOrNull()
            val timeRange = call.request.queryParameters["timeRange"]?.let { Duration.parse(it) } ?: 30.toDuration(
                DurationUnit.DAYS
            )
            if (athleteId == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid athlete ID")
                return@get
            }

            try {
                val measurementHistory = StatisticsDao.getMeasurementHistory(athleteId, timeRange)
                val chartData = generateMeasurementChartData(measurementHistory)

                val response = mapOf(
                    "history" to measurementHistory,
                    "chartData" to chartData
                )

                call.respond(HttpStatusCode.OK, response)

            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "Error retrieving measurement history: ${e.message}")
            }
        }

        // Obtener resumen de estadísticas (datos básicos para dashboard)
        get("/summary/{athleteId}") {
            val athleteId = call.parameters["athleteId"]?.toIntOrNull()

            if (athleteId == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid athlete ID")
                return@get
            }

            try {
                /* val latestAthleteStats = AthleteStatisticsDAO.getLatestAthleteStatistic(athleteId)
                 val latestStrengthStats = StrengthStatisticsDAO.getLatestStrengthStatistic(athleteId)
                 val latestEnduranceStats = EnduranceStatisticsDAO.getLatestEnduranceStatistic(athleteId)
                 val latestMeasurement = BodyMeasurementDAO.getLatestMeasurement(athleteId)
                 val latestNutritionStats = NutritionStatisticsDAO.getLatestNutritionStatistic(athleteId)

                 val summary = mapOf(
                     "athlete" to latestAthleteStats,
                     "strength" to latestStrengthStats,
                     "endurance" to latestEnduranceStats,
                     "measurements" to latestMeasurement,
                     "nutrition" to latestNutritionStats
                 )*/

                call.respond(HttpStatusCode.OK)

            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "Error retrieving statistics summary: ${e.message}")
            }
        }
    }
}


private fun generateStrengthChartData(strengthStats: List<StrengthStatistic>): StrengthChartData {
    return StrengthChartData(
        benchPressData = strengthStats.mapNotNull { stat ->
            stat.benchPressMax?.let { ChartDataPoint(stat.recordedAt, it, "Bench Press") }
        },
        squatData = strengthStats.mapNotNull { stat ->
            stat.squatMax?.let { ChartDataPoint(stat.recordedAt, it, "Squat") }
        },
        deadliftData = strengthStats.mapNotNull { stat ->
            stat.deadliftMax?.let { ChartDataPoint(stat.recordedAt, it, "Deadlift") }
        },
        pullUpsData = strengthStats.mapNotNull { stat ->
            stat.pullUpsMax?.let { ChartDataPoint(stat.recordedAt, it.toDouble(), "Pull Ups") }
        },
        strengthIndexData = strengthStats.map {
            ChartDataPoint(it.recordedAt, it.strengthIndex, "Strength Index")
        }
    )
}

private fun generateEnduranceChartData(enduranceStats: List<EnduranceStatistic>): EnduranceChartData {
    return EnduranceChartData(
        vo2MaxData = enduranceStats.mapNotNull { stat ->
            stat.vo2Max?.let { ChartDataPoint(stat.recordedAt, it, "VO2 Max") }
        },
        cardioEnduranceData = enduranceStats.map {
            ChartDataPoint(it.recordedAt, it.cardioEndurance, "Cardio Endurance")
        },
        distanceCoveredData = enduranceStats.map {
            ChartDataPoint(it.recordedAt, it.distanceCovered, "Distance")
        },
        pacingData = enduranceStats.mapNotNull { stat ->
            stat.runningPace?.let { ChartDataPoint(stat.recordedAt, it, "Pace") }
        }
    )
}

private fun generateMeasurementChartData(measurementHistory: List<BodyMeasurementHistoric>): MeasurementChartData {
    return MeasurementChartData(
        weightData = measurementHistory.map {
            ChartDataPoint(it.recordedAt, it.measurements.weight, "Weight")
        },
        bodyFatData = measurementHistory.map {
            ChartDataPoint(it.recordedAt, it.measurements.bodyFat, "Body Fat %")
        },
        bmiData = measurementHistory.map {
            ChartDataPoint(it.recordedAt, it.bmi, "BMI")
        }
    )
}