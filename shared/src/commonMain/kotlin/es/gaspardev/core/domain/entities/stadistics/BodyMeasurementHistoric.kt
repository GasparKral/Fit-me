package es.gaspardev.core.domain.entities.stadistics

import es.gaspardev.core.domain.entities.users.info.Measurements
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class BodyMeasurementHistoric(
    val id: Int,
    val athleteId: Int,
    val recordedAt: Instant,
    val measurements: Measurements,
    val weightChange: Double,
    val bodyFatChange: Double,
    val muscleMassGain: Double,
    val bmi: Double,
    val bodyComposition: Double
) {
    companion object {
        // Calcula el incremento/decremento desde el primer hasta el último record como porcentaje
        fun calculatePerformance(nodes: List<BodyMeasurementHistoric>): Double {
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
        private fun calculateCombinedScore(stat: BodyMeasurementHistoric): Double {
            var score = 0.0

            // Métricas principales con pesos altos
            score += stat.bodyComposition * 0.3  // Composición corporal general
            score += stat.muscleMassGain * 0.25  // Ganancia de masa muscular (positivo = mejor)

            // Métricas con interpretación específica
            // BMI óptimo está alrededor de 18.5-24.9, penalizar extremos
            val optimalBMI = 22.0
            val bmiScore = 100.0 - kotlin.math.abs(stat.bmi - optimalBMI) * 5.0
            score += kotlin.math.max(bmiScore, 0.0) * 0.15

            // Cambios corporales (interpretar según contexto fitness)
            // Pérdida de grasa corporal es generalmente positiva
            score += (-stat.bodyFatChange) * 0.15  // Negativo porque menos grasa = mejor

            // Cambio de peso depende del objetivo, usar valor absoluto ponderado
            score += kotlin.math.abs(stat.weightChange) * 0.1

            // Score adicional de las medidas físicas si están disponibles
            score += calculateMeasurementsScore(stat.measurements) * 0.05

            return kotlin.math.max(score, 0.0) // Asegurar que el score no sea negativo
        }

        // Función auxiliar para calcular score de measurements
        private fun calculateMeasurementsScore(measurements: Measurements): Double {
            // Esta función necesitaría ser adaptada según la estructura de Measurements
            // Por ahora retornamos un valor base, pero debería incluir métricas como:
            // - Circunferencias musculares (brazos, pecho, muslos)
            // - Reducción de circunferencia de cintura
            // - Proporciones corporales

            // Placeholder - ajustar según la estructura real de Measurements
            return 50.0
        }
    }
}