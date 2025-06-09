package es.gaspardev.core.domain.entities.stadistics

import es.gaspardev.core.domain.entities.users.Athlete
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import java.math.BigDecimal
import java.math.RoundingMode

@Serializable
data class AthletePerformanceReport(
    val reportHeader: ReportHeader,
    val athleteProfile: AthleteProfile,
    val performanceSummary: PerformanceSummary,
    val strengthAnalysis: StrengthAnalysis,
    val enduranceAnalysis: EnduranceAnalysis,
    val bodyCompositionAnalysis: BodyCompositionAnalysis,
    val progressTimeline: ProgressTimeline,
    val recommendations: RecommendationsSection,
    val reportFooter: ReportFooter
)

@Serializable
data class ReportHeader(
    val title: String = "REPORTE DE RENDIMIENTO DEPORTIVO",
    val subtitle: String = "Análisis Estadístico de Progreso",
    val generatedDate: String,
    val reportPeriod: String,
    val logoUrl: String? = null
)

@Serializable
data class AthleteProfile(
    val fullName: String,
    val age: Int,
    val sex: String,
    val trainingExperience: String,
    val memberSince: String,
    val currentPrograms: AthletePrograms
)

@Serializable
data class AthletePrograms(
    val workout: String,
    val diet: String,
    val assistanceRequired: Boolean
)

@Serializable
data class PerformanceSummary(
    val overallScore: String, // "Excelente", "Bueno", "Regular", "Necesita Mejora"
    val overallProgress: String, // "+15.3%"
    val totalWorkouts: Int,
    val trackingPeriod: String,
    val keyAchievements: List<String>,
    val areasForImprovement: List<String>
)

@Serializable
data class StrengthAnalysis(
    val overallStrengthProgress: String,
    val strengthRating: String,
    val keyMetrics: StrengthMetrics,
    val personalRecords: List<PersonalRecord>,
    val strengthTrends: List<TrendPoint>
)

@Serializable
data class StrengthMetrics(
    val strengthIndex: MetricChange,
    val muscularEndurance: MetricChange,
    val powerOutput: MetricChange?,
    val maxLifts: MaxLiftsProgress
)

@Serializable
data class MetricChange(
    val current: String,
    val previous: String,
    val change: String,
    val trend: String // "↗️", "↘️", "➡️"
)

@Serializable
data class MaxLiftsProgress(
    val benchPress: MetricChange?,
    val squat: MetricChange?,
    val deadlift: MetricChange?,
    val pullUps: MetricChange?,
    val pushUps: MetricChange?
)

@Serializable
data class PersonalRecord(
    val exercise: String,
    val value: String,
    val date: String,
    val improvement: String
)

@Serializable
data class EnduranceAnalysis(
    val overallEnduranceProgress: String,
    val enduranceRating: String,
    val keyMetrics: EnduranceMetrics,
    val cardioProgress: List<TrendPoint>,
    val enduranceHighlights: List<String>
)

@Serializable
data class EnduranceMetrics(
    val vo2Max: MetricChange?,
    val cardioEndurance: MetricChange,
    val distanceCovered: MetricChange,
    val restingHeartRate: MetricChange?,
    val recoveryTime: MetricChange?
)

@Serializable
data class BodyCompositionAnalysis(
    val overallBodyProgress: String,
    val bodyRating: String,
    val keyMetrics: BodyMetrics,
    val compositionTrends: List<TrendPoint>,
    val bodyHighlights: List<String>
)

@Serializable
data class BodyMetrics(
    val weight: MetricChange,
    val bodyFat: MetricChange,
    val muscleMass: MetricChange,
    val bmi: MetricChange,
    val bodyComposition: MetricChange
)

@Serializable
data class TrendPoint(
    val period: String, // "Ene 2025", "Feb 2025"
    val value: String,
    val change: String
)

@Serializable
data class ProgressTimeline(
    val milestones: List<Milestone>,
    val monthlyProgress: List<MonthlyProgress>
)

@Serializable
data class Milestone(
    val date: String,
    val achievement: String,
    val category: String, // "Fuerza", "Resistencia", "Composición"
    val impact: String // "Alto", "Medio", "Bajo"
)

@Serializable
data class MonthlyProgress(
    val month: String,
    val strengthScore: String,
    val enduranceScore: String,
    val bodyScore: String,
    val overallScore: String
)

@Serializable
data class RecommendationsSection(
    val strengthRecommendations: List<String>,
    val enduranceRecommendations: List<String>,
    val nutritionRecommendations: List<String>,
    val generalRecommendations: List<String>,
    val nextGoals: List<String>
)

@Serializable
data class ReportFooter(
    val trainerName: String?,
    val trainerEmail: String?,
    val generatedBy: String = "FitMe - Sistema de Gestión Deportiva",
    val disclaimer: String = "Este reporte se basa en datos registrados durante el período de entrenamiento. Consulte con su entrenador para interpretación específica."
)

// Extensión para generar el reporte
fun Athlete.generatePerformanceReport(
    strengthStats: List<StrengthStatistic> = emptyList(),
    enduranceStats: List<EnduranceStatistic> = emptyList(),
    measurementHistory: List<BodyMeasurementHistoric> = emptyList(),
    trainerName: String? = null,
    trainerEmail: String? = null
): AthletePerformanceReport {

    val strengthProgress = StrengthStatistic.calculatePerformance(strengthStats)
    val enduranceProgress = EnduranceStatistic.calculatePerformance(enduranceStats)
    val bodyProgress = BodyMeasurementHistoric.calculatePerformance(measurementHistory)

    val overallProgress = getOverallProgression()

    return AthletePerformanceReport(
        reportHeader = ReportHeader(
            generatedDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date.toString(),
            reportPeriod = calculateReportPeriod(strengthStats, enduranceStats, measurementHistory)
        ),

        athleteProfile = AthleteProfile(
            fullName = user.fullname,
            age = age,
            sex = when (sex.name) {
                "MALE" -> "Masculino"
                "FEMALE" -> "Femenino"
                else -> sex.name
            },
            trainingExperience = calculateTrainingExperience(),
            memberSince = user.creationDate.toLocalDateTime(TimeZone.currentSystemDefault()).date.toString(),
            currentPrograms = AthletePrograms(
                workout = workout?.toString() ?: "Sin programa asignado",
                diet = diet?.toString() ?: "Sin dieta asignada",
                assistanceRequired = needAssistant
            )
        ),

        performanceSummary = PerformanceSummary(
            overallScore = getPerformanceRating(overallProgress),
            overallProgress = formatPercentage(overallProgress),
            totalWorkouts = strengthStats.size + enduranceStats.size,
            trackingPeriod = calculateTrackingPeriod(strengthStats, enduranceStats, measurementHistory),
            keyAchievements = getKeyAchievements(strengthProgress, enduranceProgress, bodyProgress),
            areasForImprovement = getImprovementAreas(strengthProgress, enduranceProgress, bodyProgress)
        ),

        strengthAnalysis = generateStrengthAnalysis(strengthStats, strengthProgress),
        enduranceAnalysis = generateEnduranceAnalysis(enduranceStats, enduranceProgress),
        bodyCompositionAnalysis = generateBodyAnalysis(measurementHistory, bodyProgress),
        progressTimeline = generateProgressTimeline(strengthStats, enduranceStats, measurementHistory),
        recommendations = generateRecommendations(strengthProgress, enduranceProgress, bodyProgress),

        reportFooter = ReportFooter(
            trainerName = trainerName,
            trainerEmail = trainerEmail
        )
    )
}

// Funciones auxiliares
private fun Athlete.calculateTrainingExperience(): String {
    val now = Clock.System.now()
    val duration = now - trainingSince
    val days = duration.inWholeDays
    val years = days / 365
    val months = (days % 365) / 30

    return when {
        years > 0 && months > 0 -> "$years años y $months meses"
        years > 0 -> "$years años"
        months > 0 -> "$months meses"
        else -> "$days días"
    }
}

private fun getPerformanceRating(progress: Double): String = when {
    progress >= 80.0 -> "Excelente"
    progress >= 60.0 -> "Muy Bueno"
    progress >= 40.0 -> "Bueno"
    progress >= 20.0 -> "Regular"
    else -> "Necesita Mejora"
}

private fun formatPercentage(value: Double): String {
    val formatted = BigDecimal(value).setScale(1, RoundingMode.HALF_UP)
    return if (value >= 0) "+$formatted%" else "$formatted%"
}

private fun getTrendIcon(change: Double): String = when {
    change > 5.0 -> "↗️"
    change < -5.0 -> "↘️"
    else -> "➡️"
}

private fun getKeyAchievements(strength: Double, endurance: Double, body: Double): List<String> {
    val achievements = mutableListOf<String>()

    if (strength > 15.0) achievements.add("Mejora significativa en fuerza (+${formatPercentage(strength)})")
    if (endurance > 15.0) achievements.add("Excelente progreso cardiovascular (+${formatPercentage(endurance)})")
    if (body > 10.0) achievements.add("Mejora notable en composición corporal (+${formatPercentage(body)})")

    if (achievements.isEmpty()) {
        achievements.add("Mantiene constancia en el entrenamiento")
        achievements.add("Progreso gradual y sostenible")
    }

    return achievements
}

private fun getImprovementAreas(strength: Double, endurance: Double, body: Double): List<String> {
    val areas = mutableListOf<String>()

    if (strength < 0) areas.add("Enfoque en entrenamiento de fuerza")
    if (endurance < 0) areas.add("Mejorar capacidad cardiovascular")
    if (body < 0) areas.add("Optimizar composición corporal")

    if (areas.isEmpty()) {
        areas.add("Mantener progreso actual")
        areas.add("Establecer nuevos objetivos")
    }

    return areas
}

private fun generateStrengthAnalysis(stats: List<StrengthStatistic>, progress: Double): StrengthAnalysis {
    if (stats.size < 2) {
        return StrengthAnalysis(
            overallStrengthProgress = "Sin datos suficientes",
            strengthRating = "N/A",
            keyMetrics = StrengthMetrics(
                strengthIndex = MetricChange("N/A", "N/A", "N/A", "➡️"),
                muscularEndurance = MetricChange("N/A", "N/A", "N/A", "➡️"),
                powerOutput = null,
                maxLifts = MaxLiftsProgress(null, null, null, null, null)
            ),
            personalRecords = emptyList(),
            strengthTrends = emptyList()
        )
    }

    val sorted = stats.sortedBy { it.recordedAt }
    val first = sorted.first()
    val last = sorted.last()

    // Crear datos para trends de forma segura
    val trendsData = sorted.map { stat ->
        val date = stat.recordedAt.toLocalDateTime(TimeZone.currentSystemDefault())
        "${date.monthNumber}/${date.year}" to stat.strengthIndex
    }

    return StrengthAnalysis(
        overallStrengthProgress = formatPercentage(progress),
        strengthRating = getPerformanceRating(if (progress > 0) progress * 4 else 0.0),
        keyMetrics = StrengthMetrics(
            strengthIndex = createMetricChange(first.strengthIndex, last.strengthIndex),
            muscularEndurance = createMetricChange(first.muscularEndurance, last.muscularEndurance),
            powerOutput = if (first.powerOutput != null && last.powerOutput != null) {
                createMetricChange(first.powerOutput, last.powerOutput)
            } else null,
            maxLifts = MaxLiftsProgress(
                benchPress = createMaxLiftMetric(first.benchPressMax, last.benchPressMax),
                squat = createMaxLiftMetric(first.squatMax, last.squatMax),
                deadlift = createMaxLiftMetric(first.deadliftMax, last.deadliftMax),
                pullUps = createMaxLiftMetricInt(first.pullUpsMax, last.pullUpsMax),
                pushUps = createMaxLiftMetricInt(first.pushUpsMax, last.pushUpsMax)
            )
        ),
        personalRecords = generatePersonalRecords(stats),
        strengthTrends = generateTrends(trendsData)
    )
}

// Nueva función auxiliar para crear MetricChange de forma segura
private fun createMetricChange(firstValue: Double, lastValue: Double): MetricChange {
    val change = if (firstValue != 0.0) {
        ((lastValue - firstValue) / firstValue) * 100
    } else 0.0

    return MetricChange(
        current = BigDecimal(lastValue).setScale(2, RoundingMode.HALF_UP).toString(),
        previous = BigDecimal(firstValue).setScale(2, RoundingMode.HALF_UP).toString(),
        change = formatPercentage(change),
        trend = getTrendIcon(change)
    )
}

private fun createMaxLiftMetric(first: Double?, last: Double?): MetricChange? {
    if (first == null || last == null || first <= 0 || last <= 0) return null

    val change = ((last - first) / first) * 100
    return MetricChange(
        current = "${BigDecimal(last).setScale(1, RoundingMode.HALF_UP)}kg",
        previous = "${BigDecimal(first).setScale(1, RoundingMode.HALF_UP)}kg",
        change = formatPercentage(change),
        trend = getTrendIcon(change)
    )
}

private fun createMaxLiftMetricInt(first: Int?, last: Int?): MetricChange? {
    if (first == null || last == null || first <= 0 || last <= 0) return null

    val change = ((last - first).toDouble() / first) * 100
    return MetricChange(
        current = last.toString(),
        previous = first.toString(),
        change = formatPercentage(change),
        trend = getTrendIcon(change)
    )
}

private fun generatePersonalRecords(stats: List<StrengthStatistic>): List<PersonalRecord> {
    val records = mutableListOf<PersonalRecord>()

    if (stats.isEmpty()) return records

    // Encontrar máximos de cada ejercicio
    stats.maxByOrNull { it.benchPressMax ?: 0.0 }?.let { stat ->
        if (stat.benchPressMax != null && stat.benchPressMax > 0) {
            records.add(
                PersonalRecord(
                    exercise = "Press de Banca",
                    value = "${stat.benchPressMax}kg",
                    date = stat.recordedAt.toLocalDateTime(TimeZone.currentSystemDefault()).date.toString(),
                    improvement = "Récord Personal"
                )
            )
        }
    }

    stats.maxByOrNull { it.squatMax ?: 0.0 }?.let { stat ->
        if (stat.squatMax != null && stat.squatMax > 0) {
            records.add(
                PersonalRecord(
                    exercise = "Sentadilla",
                    value = "${stat.squatMax}kg",
                    date = stat.recordedAt.toLocalDateTime(TimeZone.currentSystemDefault()).date.toString(),
                    improvement = "Récord Personal"
                )
            )
        }
    }

    stats.maxByOrNull { it.deadliftMax ?: 0.0 }?.let { stat ->
        if (stat.deadliftMax != null && stat.deadliftMax > 0) {
            records.add(
                PersonalRecord(
                    exercise = "Peso Muerto",
                    value = "${stat.deadliftMax}kg",
                    date = stat.recordedAt.toLocalDateTime(TimeZone.currentSystemDefault()).date.toString(),
                    improvement = "Récord Personal"
                )
            )
        }
    }

    stats.maxByOrNull { it.pullUpsMax ?: 0 }?.let { stat ->
        if (stat.pullUpsMax != null && stat.pullUpsMax > 0) {
            records.add(
                PersonalRecord(
                    exercise = "Dominadas",
                    value = "${stat.pullUpsMax} reps",
                    date = stat.recordedAt.toLocalDateTime(TimeZone.currentSystemDefault()).date.toString(),
                    improvement = "Récord Personal"
                )
            )
        }
    }

    stats.maxByOrNull { it.pushUpsMax ?: 0 }?.let { stat ->
        if (stat.pushUpsMax != null && stat.pushUpsMax > 0) {
            records.add(
                PersonalRecord(
                    exercise = "Flexiones",
                    value = "${stat.pushUpsMax} reps",
                    date = stat.recordedAt.toLocalDateTime(TimeZone.currentSystemDefault()).date.toString(),
                    improvement = "Récord Personal"
                )
            )
        }
    }

    return records.take(3) // Mostrar solo top 3
}

private fun generateTrends(data: List<Pair<String, Double>>): List<TrendPoint> {
    if (data.isEmpty()) return emptyList()

    val recentData = data.takeLast(6)

    return recentData.mapIndexed { index, (period, value) ->
        val change = if (index > 0 && recentData.size > 1) {
            val prevValue = recentData[index - 1].second
            if (prevValue != 0.0) {
                formatPercentage(((value - prevValue) / prevValue) * 100)
            } else "N/A"
        } else "Base"

        TrendPoint(period, BigDecimal(value).setScale(2, RoundingMode.HALF_UP).toString(), change)
    }
}

private fun generateEnduranceAnalysis(stats: List<EnduranceStatistic>, progress: Double): EnduranceAnalysis {
    // Similar estructura que strength pero para resistencia
    return EnduranceAnalysis(
        overallEnduranceProgress = formatPercentage(progress),
        enduranceRating = getPerformanceRating(if (progress > 0) progress * 4 else 0.0),
        keyMetrics = EnduranceMetrics(
            vo2Max = null, // Implementar según datos disponibles
            cardioEndurance = MetricChange("N/A", "N/A", "N/A", "➡️"),
            distanceCovered = MetricChange("N/A", "N/A", "N/A", "➡️"),
            restingHeartRate = null,
            recoveryTime = null
        ),
        cardioProgress = emptyList(),
        enduranceHighlights = listOf("Progreso constante en resistencia")
    )
}

private fun generateBodyAnalysis(history: List<BodyMeasurementHistoric>, progress: Double): BodyCompositionAnalysis {
    // Similar estructura que strength pero para composición corporal
    return BodyCompositionAnalysis(
        overallBodyProgress = formatPercentage(progress),
        bodyRating = getPerformanceRating(if (progress > 0) progress * 4 else 0.0),
        keyMetrics = BodyMetrics(
            weight = MetricChange("N/A", "N/A", "N/A", "➡️"),
            bodyFat = MetricChange("N/A", "N/A", "N/A", "➡️"),
            muscleMass = MetricChange("N/A", "N/A", "N/A", "➡️"),
            bmi = MetricChange("N/A", "N/A", "N/A", "➡️"),
            bodyComposition = MetricChange("N/A", "N/A", "N/A", "➡️")
        ),
        compositionTrends = emptyList(),
        bodyHighlights = listOf("Mejora en composición corporal")
    )
}

private fun generateProgressTimeline(
    strengthStats: List<StrengthStatistic>,
    enduranceStats: List<EnduranceStatistic>,
    measurementHistory: List<BodyMeasurementHistoric>
): ProgressTimeline {
    return ProgressTimeline(
        milestones = listOf(
            Milestone(
                date = "2025-01-15",
                achievement = "Inicio del programa de entrenamiento",
                category = "General",
                impact = "Alto"
            )
        ),
        monthlyProgress = emptyList()
    )
}

private fun generateRecommendations(strength: Double, endurance: Double, body: Double): RecommendationsSection {
    return RecommendationsSection(
        strengthRecommendations = if (strength < 5.0)
            listOf("Incrementar frecuencia de entrenamiento de fuerza", "Revisar técnica de ejercicios básicos")
        else
            listOf("Mantener intensidad actual", "Incorporar ejercicios de potencia"),

        enduranceRecommendations = if (endurance < 5.0)
            listOf("Aumentar volumen cardiovascular", "Incluir entrenamiento por intervalos")
        else
            listOf("Mantener capacidad aeróbica", "Variar tipos de cardio"),

        nutritionRecommendations = listOf(
            "Mantener hidratación adecuada",
            "Consumir proteína post-entrenamiento"
        ),

        generalRecommendations = listOf(
            "Mantener constancia en entrenamientos",
            "Registrar todos los entrenamientos"
        ),

        nextGoals = listOf(
            "Establecer nuevos récords personales",
            "Mejorar técnica en ejercicios principales"
        )
    )
}

private fun calculateReportPeriod(
    strengthStats: List<StrengthStatistic>,
    enduranceStats: List<EnduranceStatistic>,
    measurementHistory: List<BodyMeasurementHistoric>
): String {
    val allDates = listOf(
        strengthStats.map { it.recordedAt },
        enduranceStats.map { it.recordedAt },
        measurementHistory.map { it.recordedAt }
    ).flatten()

    if (allDates.isEmpty()) return "Sin datos"

    val first = allDates.minOrNull()?.toLocalDateTime(TimeZone.currentSystemDefault())?.date
    val last = allDates.maxOrNull()?.toLocalDateTime(TimeZone.currentSystemDefault())?.date

    return if (first != null && last != null) {
        "$first al $last"
    } else "Período no determinado"
}

private fun calculateTrackingPeriod(
    strengthStats: List<StrengthStatistic>,
    enduranceStats: List<EnduranceStatistic>,
    measurementHistory: List<BodyMeasurementHistoric>
): String {
    val allDates = listOf(
        strengthStats.map { it.recordedAt },
        enduranceStats.map { it.recordedAt },
        measurementHistory.map { it.recordedAt }
    ).flatten()

    if (allDates.size < 2) return "Datos insuficientes"

    val first = allDates.minOrNull()!!
    val last = allDates.maxOrNull()!!
    val duration = last - first
    val days = duration.inWholeDays

    return when {
        days >= 365 -> "${days / 365} años"
        days >= 30 -> "${days / 30} meses"
        else -> "$days días"
    }
}

// Función para generar nombre automático del archivo
fun AthletePerformanceReport.generateReportFilename(format: String = "txt"): String {
    val athleteName = athleteProfile.fullName
        .lowercase()
        .replace(" ", "_")
        .replace(Regex("[^a-z0-9_]"), "") // Remover caracteres especiales

    val currentDate = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .date
        .toString()
        .replace("-", "")

    val reportType = "reporte_rendimiento"

    return "${reportType}_${athleteName}_${currentDate}.${format}"
}

// Función para generar múltiples nombres según el formato
fun AthletePerformanceReport.generateFilenames(): Map<String, String> {
    return mapOf(
        "txt" to generateReportFilename("txt"),
        "pdf" to generateReportFilename("pdf"),
        "html" to generateReportFilename("html"),
        "json" to generateReportFilename("json")
    )
}

// Función para generar nombre personalizado
fun AthletePerformanceReport.generateCustomFilename(
    prefix: String = "reporte_rendimiento",
    includeTrainer: Boolean = false,
    includePeriod: Boolean = false,
    format: String = "txt"
): String {
    val athleteName = athleteProfile.fullName
        .lowercase()
        .replace(" ", "_")
        .replace(Regex("[^a-z0-9_]"), "")

    val currentDate = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .date
        .toString()
        .replace("-", "")

    var filename = "${prefix}_${athleteName}"

    if (includeTrainer && reportFooter.trainerName != null) {
        val trainerName = reportFooter.trainerName
            .lowercase()
            .replace(" ", "_")
            .replace(Regex("[^a-z0-9_]"), "")
        filename += "_entrenador_${trainerName}"
    }

    if (includePeriod) {
        val period = reportHeader.reportPeriod
            .replace(" ", "_")
            .replace(Regex("[^a-z0-9_]"), "")
            .lowercase()
        filename += "_periodo_${period}"
    }

    filename += "_${currentDate}.${format}"

    return filename
}

// Función para exportar el reporte como texto formateado
fun AthletePerformanceReport.toFormattedReport(): String {
    return buildString {
        appendLine("═".repeat(80))
        appendLine(reportHeader.title)
        appendLine(reportHeader.subtitle)
        appendLine("Generado: ${reportHeader.generatedDate}")
        appendLine("Período: ${reportHeader.reportPeriod}")
        appendLine("═".repeat(80))
        appendLine()

        appendLine("👤 PERFIL DEL DEPORTISTA")
        appendLine("─".repeat(40))
        appendLine("Nombre: ${athleteProfile.fullName}")
        appendLine("Edad: ${athleteProfile.age} años")
        appendLine("Sexo: ${athleteProfile.sex}")
        appendLine("Experiencia: ${athleteProfile.trainingExperience}")
        appendLine("Miembro desde: ${athleteProfile.memberSince}")
        appendLine("Programa actual: ${athleteProfile.currentPrograms.workout}")
        appendLine("Dieta actual: ${athleteProfile.currentPrograms.diet}")
        appendLine()

        appendLine("📊 RESUMEN DE RENDIMIENTO")
        appendLine("─".repeat(40))
        appendLine("Calificación General: ${performanceSummary.overallScore}")
        appendLine("Progreso Global: ${performanceSummary.overallProgress}")
        appendLine("Entrenamientos Registrados: ${performanceSummary.totalWorkouts}")
        appendLine("Período de Seguimiento: ${performanceSummary.trackingPeriod}")
        appendLine()

        appendLine("🏆 LOGROS DESTACADOS:")
        performanceSummary.keyAchievements.forEach {
            appendLine("  • $it")
        }
        appendLine()

        appendLine("🎯 ÁREAS DE MEJORA:")
        performanceSummary.areasForImprovement.forEach {
            appendLine("  • $it")
        }
        appendLine()

        appendLine("💪 ANÁLISIS DE FUERZA")
        appendLine("─".repeat(40))
        appendLine("Progreso: ${strengthAnalysis.overallStrengthProgress}")
        appendLine("Calificación: ${strengthAnalysis.strengthRating}")
        appendLine("Índice de Fuerza: ${strengthAnalysis.keyMetrics.strengthIndex.current} ${strengthAnalysis.keyMetrics.strengthIndex.trend}")
        appendLine("Resistencia Muscular: ${strengthAnalysis.keyMetrics.muscularEndurance.current} ${strengthAnalysis.keyMetrics.muscularEndurance.trend}")
        appendLine()

        appendLine("🏃 ANÁLISIS DE RESISTENCIA")
        appendLine("─".repeat(40))
        appendLine("Progreso: ${enduranceAnalysis.overallEnduranceProgress}")
        appendLine("Calificación: ${enduranceAnalysis.enduranceRating}")
        appendLine()

        appendLine("⚖️ ANÁLISIS DE COMPOSICIÓN CORPORAL")
        appendLine("─".repeat(40))
        appendLine("Progreso: ${bodyCompositionAnalysis.overallBodyProgress}")
        appendLine("Calificación: ${bodyCompositionAnalysis.bodyRating}")
        appendLine()

        appendLine("💡 RECOMENDACIONES")
        appendLine("─".repeat(40))
        appendLine("Fuerza:")
        recommendations.strengthRecommendations.forEach { appendLine("  • $it") }
        appendLine("Resistencia:")
        recommendations.enduranceRecommendations.forEach { appendLine("  • $it") }
        appendLine("Nutrición:")
        recommendations.nutritionRecommendations.forEach { appendLine("  • $it") }
        appendLine("General:")
        recommendations.generalRecommendations.forEach { appendLine("  • $it") }
        appendLine()

        appendLine("🎯 PRÓXIMOS OBJETIVOS")
        appendLine("─".repeat(40))
        recommendations.nextGoals.forEach { appendLine("  • $it") }
        appendLine()

        appendLine("═".repeat(80))
        appendLine(reportFooter.generatedBy)
        reportFooter.trainerName?.let { appendLine("Entrenador: $it") }
        reportFooter.trainerEmail?.let { appendLine("Contacto: $it") }
        appendLine()
        appendLine(reportFooter.disclaimer)
        appendLine("═".repeat(80))
    }
}