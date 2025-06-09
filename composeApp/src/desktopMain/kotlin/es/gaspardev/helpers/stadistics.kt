package es.gaspardev.helpers

import es.gaspardev.core.domain.entities.stadistics.BodyMeasurementHistoric
import es.gaspardev.core.domain.entities.stadistics.ChartDataPoint
import es.gaspardev.core.domain.entities.stadistics.EnduranceStatistic
import es.gaspardev.core.domain.entities.stadistics.StrengthStatistic
import androidx.compose.ui.graphics.Color
import es.gaspardev.core.domain.entities.stadistics.*
import kotlinx.datetime.Instant
import java.text.SimpleDateFormat
import java.util.*

fun StrengthStatistic.toChartDataPoints(): List<ChartDataPoint> {
    return listOf(
        ChartDataPoint(recordedAt, benchPressMax ?: 0.0, "Press Banca"),
        ChartDataPoint(recordedAt, squatMax ?: 0.0, "Sentadillas"),
        ChartDataPoint(recordedAt, deadliftMax ?: 0.0, "Peso Muerto"),
        ChartDataPoint(recordedAt, strengthIndex, "Índice Fuerza")
    )
}

fun EnduranceStatistic.toChartDataPoints(): List<ChartDataPoint> {
    return listOf(
        ChartDataPoint(recordedAt, vo2Max ?: 0.0, "VO2 Max"),
        ChartDataPoint(recordedAt, cardioEndurance, "Resistencia Cardio"),
        ChartDataPoint(recordedAt, distanceCovered, "Distancia"),
        ChartDataPoint(recordedAt, aerobicCapacity, "Capacidad Aeróbica")
    )
}

fun BodyMeasurementHistoric.toChartDataPoints(): List<ChartDataPoint> {
    return listOf(
        ChartDataPoint(recordedAt, measurements.weight, "Peso"),
        ChartDataPoint(recordedAt, measurements.bodyFat, "Grasa Corporal"),
        ChartDataPoint(recordedAt, muscleMassGain, "Masa Muscular"),
        ChartDataPoint(recordedAt, bmi, "IMC")
    )
}

object ChartUtils {

    // Colores consistentes para diferentes métricas
    object ChartColors {
        val Strength = Color(0xFF2196F3)
        val SquatColor = Color(0xFF4CAF50)
        val DeadliftColor = Color(0xFFFF5722)
        val BenchPressColor = Color(0xFF2196F3)
        val PullUpsColor = Color(0xFF9C27B0)

        val EnduranceVO2 = Color(0xFF9C27B0)
        val EnduranceCardio = Color(0xFF00BCD4)
        val EnduranceDistance = Color(0xFFFF9800)
        val EndurancePacing = Color(0xFF607D8B)

        val WeightColor = Color(0xFF3F51B5)
        val BodyFatColor = Color(0xFFE91E63)
        val MuscleMassColor = Color(0xFF8BC34A)
        val BMIColor = Color(0xFFFF7043)

        val PrimaryChart = Color(0xFF6200EE)
        val SecondaryChart = Color(0xFF03DAC6)
        val TertiaryChart = Color(0xFFFF6B6B)
        val QuaternaryChart = Color(0xFF4ECDC4)
    }

    // Formateadores de fecha
    private val shortDateFormatter = SimpleDateFormat("dd/MM", Locale.getDefault())
    private val mediumDateFormatter = SimpleDateFormat("dd/MM/yy", Locale.getDefault())
    private val longDateFormatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    fun formatDateShort(instant: Instant): String {
        return shortDateFormatter.format(Date(instant.toEpochMilliseconds()))
    }

    fun formatDateMedium(instant: Instant): String {
        return mediumDateFormatter.format(Date(instant.toEpochMilliseconds()))
    }

    fun formatDateLong(instant: Instant): String {
        return longDateFormatter.format(Date(instant.toEpochMilliseconds()))
    }

    // Funciones para filtrar datos por rango de tiempo
    /*fun filterDataByTimeRange(
        data: List<ChartDataPoint>,
        timeRange: String,
        currentTime: Instant = kotlinx.datetime.Clock.System.now()
    ): List<ChartDataPoint> {
        val cutoffTime = when (timeRange) {
            "1month" -> currentTime.minus(kotlinx.datetime.DateTimeUnit.DAY, 30)
            "3months" -> currentTime.minus(kotlinx.datetime.DateTimeUnit.DAY, 90)
            "6months" -> currentTime.minus(kotlinx.datetime.DateTimeUnit.DAY, 180)
            "1year" -> currentTime.minus(kotlinx.datetime.DateTimeUnit.DAY, 365)
            "all" -> return data
            else -> currentTime.minus(kotlinx.datetime.DateTimeUnit.DAY, 180) // default 6 months
        }

        return data.filter { it.date >= cutoffTime }
    }*/

    // Función para calcular rangos dinámicos del eje Y
    fun calculateYAxisRange(values: List<Double>): Int {
        if (values.isEmpty()) return 10

        val max = values.maxOrNull() ?: 0.0
        val min = values.minOrNull() ?: 0.0
        val range = max - min

        return when {
            range <= 10 -> 2
            range <= 50 -> 5
            range <= 100 -> 10
            range <= 500 -> 25
            else -> 50
        }
    }

    // Funciones de conversión para diferentes tipos de estadísticas
    fun List<StrengthStatistic>.toStrengthChartData(): StrengthChartData {
        return StrengthChartData(
            benchPressData = this.mapNotNull { stat ->
                stat.benchPressMax?.let { max ->
                    ChartDataPoint(stat.recordedAt, max, "Press Banca")
                }
            },
            squatData = this.mapNotNull { stat ->
                stat.squatMax?.let { max ->
                    ChartDataPoint(stat.recordedAt, max, "Sentadillas")
                }
            },
            deadliftData = this.mapNotNull { stat ->
                stat.deadliftMax?.let { max ->
                    ChartDataPoint(stat.recordedAt, max, "Peso Muerto")
                }
            },
            pullUpsData = this.mapNotNull { stat ->
                stat.pullUpsMax?.let { max ->
                    ChartDataPoint(stat.recordedAt, max.toDouble(), "Dominadas")
                }
            },
            strengthIndexData = this.map { stat ->
                ChartDataPoint(stat.recordedAt, stat.strengthIndex, "Índice de Fuerza")
            }
        )
    }

    fun List<EnduranceStatistic>.toEnduranceChartData(): EnduranceChartData {
        return EnduranceChartData(
            vo2MaxData = this.mapNotNull { stat ->
                stat.vo2Max?.let { vo2 ->
                    ChartDataPoint(stat.recordedAt, vo2, "VO2 Max")
                }
            },
            cardioEnduranceData = this.map { stat ->
                ChartDataPoint(stat.recordedAt, stat.cardioEndurance, "Resistencia Cardio")
            },
            distanceCoveredData = this.map { stat ->
                ChartDataPoint(stat.recordedAt, stat.distanceCovered, "Distancia")
            },
            pacingData = this.mapNotNull { stat ->
                stat.runningPace?.let { pace ->
                    ChartDataPoint(stat.recordedAt, pace, "Ritmo de Carrera")
                }
            }
        )
    }

    fun List<BodyMeasurementHistoric>.toMeasurementChartData(): MeasurementChartData {
        return MeasurementChartData(
            weightData = this.map { measurement ->
                ChartDataPoint(measurement.recordedAt, measurement.measurements.weight, "Peso")
            },
            bodyFatData = this.mapNotNull { measurement ->
                measurement.measurements.bodyFat?.let { bodyFat ->
                    ChartDataPoint(measurement.recordedAt, bodyFat, "Grasa Corporal")
                }
            },
            muscleMassData = this.map { measurement ->
                ChartDataPoint(measurement.recordedAt, measurement.muscleMassGain, "Masa Muscular")
            },
            bmiData = this.map { measurement ->
                ChartDataPoint(measurement.recordedAt, measurement.bmi, "IMC")
            }
        )
    }

    // Función para calcular estadísticas resumidas
    fun calculateSummaryStats(data: List<ChartDataPoint>): SummaryStats {
        if (data.isEmpty()) return SummaryStats.empty()

        val values = data.map { it.value }
        val latest = data.last().value
        val previous = if (data.size > 1) data[data.size - 2].value else latest
        val first = data.first().value

        return SummaryStats(
            current = latest,
            previous = previous,
            change = latest - previous,
            changePercentage = if (previous != 0.0) ((latest - previous) / previous) * 100 else 0.0,
            totalChange = latest - first,
            totalChangePercentage = if (first != 0.0) ((latest - first) / first) * 100 else 0.0,
            average = values.average(),
            max = values.maxOrNull() ?: 0.0,
            min = values.minOrNull() ?: 0.0
        )
    }

    // Función para generar etiquetas del eje X basadas en el rango de tiempo
    fun generateXAxisLabels(
        data: List<ChartDataPoint>,
        timeRange: String,
        maxLabels: Int = 6
    ): List<String> {
        if (data.isEmpty()) return emptyList()

        val sortedData = data.sortedBy { it.date }
        val step = maxOf(1, sortedData.size / maxLabels)

        return sortedData.filterIndexed { index, _ ->
            index % step == 0
        }.map { point ->
            when (timeRange) {
                "1month", "3months" -> formatDateShort(point.date)
                "6months", "1year" -> formatDateMedium(point.date)
                else -> formatDateShort(point.date)
            }
        }
    }

    // Función para interpolar datos faltantes
    fun interpolateMissingData(data: List<ChartDataPoint>): List<ChartDataPoint> {
        if (data.size < 2) return data

        val result = mutableListOf<ChartDataPoint>()
        val sortedData = data.sortedBy { it.date }

        for (i in sortedData.indices) {
            result.add(sortedData[i])

            if (i < sortedData.size - 1) {
                val current = sortedData[i]
                val next = sortedData[i + 1]

                // Si hay más de 60 días entre puntos, interpolar
                val daysBetween =
                    (next.date.toEpochMilliseconds() - current.date.toEpochMilliseconds()) / (24 * 60 * 60 * 1000)

                if (daysBetween > 60) {
                    val midTime = Instant.fromEpochMilliseconds(
                        (current.date.toEpochMilliseconds() + next.date.toEpochMilliseconds()) / 2
                    )
                    val midValue = (current.value + next.value) / 2

                    result.add(ChartDataPoint(midTime, midValue, "${current.label} (interpolado)"))
                }
            }
        }

        return result
    }
}

data class SummaryStats(
    val current: Double,
    val previous: Double,
    val change: Double,
    val changePercentage: Double,
    val totalChange: Double,
    val totalChangePercentage: Double,
    val average: Double,
    val max: Double,
    val min: Double
) {
    companion object {
        fun empty() = SummaryStats(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
    }
}

// Extensiones útiles para formatear valores
fun Double.formatAsWeight(): String = "%.1f kg".format(this)
fun Double.formatAsPercentage(): String = "%.1f%%".format(this)
fun Double.formatAsDistance(): String = "%.2f km".format(this)
fun Double.formatAsTime(): String {
    val hours = (this / 3600).toInt()
    val minutes = ((this % 3600) / 60).toInt()
    return "${hours}h ${minutes}m"
}

// Función para generar colores de gradiente
fun generateGradientColors(baseColor: Color, count: Int): List<Color> {
    if (count <= 1) return listOf(baseColor)

    return (0 until count).map { index ->
        val factor = 0.3f + (0.7f * index / (count - 1))
        Color(
            red = (baseColor.red * factor),
            green = (baseColor.green * factor),
            blue = (baseColor.blue * factor),
            alpha = baseColor.alpha
        )
    }
}