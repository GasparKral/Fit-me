package es.gaspardev.core.domain.entities.stadistics

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class StrengthStatistic(
    val id: Int,
    val athleteId: Int,
    val recordedAt: Instant,
    val benchPressMax: Double?,
    val squatMax: Double?,
    val deadliftMax: Double?,
    val pullUpsMax: Int?,
    val pushUpsMax: Int?,
    val strengthIndex: Double,
    val muscularEndurance: Double,
    val powerOutput: Double?
) {
    companion object {


        // Calcula el incremento/decremento desde el primer hasta el último record como porcentaje
        fun calculatePerformance(nodes: List<StrengthStatistic>): Double {
            if (nodes.size < 2) return 0.0

            // Ordenar por fecha para obtener el primer y último record
            val sortedNodes = nodes.sortedBy { it.recordedAt }
            val firstRecord = sortedNodes.first()
            val lastRecord = sortedNodes.last()

            // Calcular el score combinado para cada record
            val firstScore = calculateCombinedScore(firstRecord)
            val lastScore = calculateCombinedScore(lastRecord)

            // Evitar división por cero
            if (firstScore == 0.0) return 0.0

            // Calcular el porcentaje de cambio: ((valor_final - valor_inicial) / valor_inicial) * 100
            return ((lastScore - firstScore) / firstScore) * 100
        }

        // Función auxiliar para calcular un score combinado de todos los métricas
        private fun calculateCombinedScore(stat: StrengthStatistic): Double {
            // Combinar todas las métricas en un solo score
            var score = 0.0

            // Métricas principales con pesos
            score += stat.strengthIndex * 0.3
            score += stat.muscularEndurance * 0.3

            // Métricas opcionales
            stat.powerOutput?.let { score += it * 0.2 }
            stat.benchPressMax?.let { score += it * 0.05 }
            stat.squatMax?.let { score += it * 0.05 }
            stat.deadliftMax?.let { score += it * 0.05 }
            stat.pullUpsMax?.let { score += it * 0.025 }
            stat.pushUpsMax?.let { score += it * 0.025 }

            return score
        }
    }
}