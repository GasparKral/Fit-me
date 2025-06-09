package es.gaspardev.core.domain.entities.stadistics

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class EnduranceStatistic(
    val id: Int,
    val athleteId: Int,
    val recordedAt: Instant,
    val vo2Max: Double?,
    val restingHeartRate: Int?,
    val maxHeartRate: Int?,
    val runningPace: Double?,
    val cardioEndurance: Double,
    val aerobicCapacity: Double,
    val recoveryTime: Double?,
    val distanceCovered: Double
) {
    companion object {
        // Calcula el incremento/decremento desde el primer hasta el último record como porcentaje
        fun calculatePerformance(nodes: List<EnduranceStatistic>): Double {
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

        // Función auxiliar para calcular un score combinado de todas las métricas
        private fun calculateCombinedScore(stat: EnduranceStatistic): Double {
            var score = 0.0

            // Métricas principales con pesos altos
            score += stat.cardioEndurance * 0.25
            score += stat.aerobicCapacity * 0.25
            score += stat.distanceCovered * 0.2

            // Métricas opcionales beneficiosas (valores más altos = mejor)
            stat.vo2Max?.let { score += it * 0.15 }
            stat.maxHeartRate?.let { score += it * 0.05 }

            // Métricas inversas (valores más bajos = mejor rendimiento)
            stat.restingHeartRate?.let {
                // Convertir a score positivo: menor frecuencia cardíaca en reposo = mejor
                score += (220 - it) * 0.05 // Usar 220 como referencia máxima
            }

            stat.runningPace?.let {
                // Convertir pace a score positivo: menor pace = mejor velocidad
                // Asumiendo pace en min/km, invertir para que menor sea mejor
                if (it > 0) score += (60.0 / it) * 0.03 // Convertir a km/h aproximado
            }

            stat.recoveryTime?.let {
                // Menor tiempo de recuperación = mejor
                // Usar escala inversa
                if (it > 0) score += (300.0 / it) * 0.02 // Asumiendo recovery en minutos
            }

            return score
        }
    }
}