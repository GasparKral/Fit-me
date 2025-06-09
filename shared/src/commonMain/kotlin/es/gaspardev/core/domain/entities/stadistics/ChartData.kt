package es.gaspardev.core.domain.entities.stadistics

import es.gaspardev.core.domain.entities.users.Athlete
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import kotlin.time.Duration

@Serializable
data class ChartDataPoint(
    val date: Instant,
    val value: Double,
    val label: String = ""
)

@Serializable
data class StatisticsChartData(
    val strengthData: List<ChartDataPoint> = listOf(),
    val enduranceData: List<ChartDataPoint> = listOf(),
    val measurementData: List<ChartDataPoint> = listOf(),
)

@Serializable
data class StrengthChartData(
    val benchPressData: List<ChartDataPoint> = listOf(),
    val squatData: List<ChartDataPoint> = listOf(),
    val deadliftData: List<ChartDataPoint> = listOf(),
    val pullUpsData: List<ChartDataPoint> = listOf(),
    val strengthIndexData: List<ChartDataPoint> = listOf()
)

@Serializable
data class EnduranceChartData(
    val vo2MaxData: List<ChartDataPoint> = listOf(),
    val cardioEnduranceData: List<ChartDataPoint> = listOf(),
    val distanceCoveredData: List<ChartDataPoint> = listOf(),
    val pacingData: List<ChartDataPoint> = listOf()
)

@Serializable
data class MeasurementChartData(
    val weightData: List<ChartDataPoint> = listOf(),
    val bodyFatData: List<ChartDataPoint> = listOf(),
    val muscleMassData: List<ChartDataPoint> = listOf(),
    val bmiData: List<ChartDataPoint> = listOf()
)

@Serializable
data class ComprehensiveStatistics(
    val athlete: Athlete,
    val timeRange: Duration, // "1month", "3months", "6months", "1year", "all"
    val strengthStats: List<StrengthStatistic>,
    val enduranceStats: List<EnduranceStatistic>,
    val measurementHistory: List<BodyMeasurementHistoric>,
    val strengthChartData: StrengthChartData,
    val enduranceChartData: EnduranceChartData,
    val measurementChartData: MeasurementChartData
)