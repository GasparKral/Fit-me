package es.gaspardev.database.daos

import es.gaspardev.core.domain.entities.stadistics.BodyMeasurementHistoric
import es.gaspardev.core.domain.entities.stadistics.EnduranceStatistic
import es.gaspardev.core.domain.entities.stadistics.StrengthStatistic
import es.gaspardev.core.domain.entities.users.info.Measurements
import es.gaspardev.database.BodyMeasurementHistory
import es.gaspardev.database.EnduranceStatistics
import es.gaspardev.database.StrengthStatistics
import es.gaspardev.database.entities.BodyMeasurementHistoryEntity
import es.gaspardev.database.entities.EnduranceStatisticEntity
import es.gaspardev.database.entities.StrengthStatisticEntity
import kotlinx.datetime.Clock
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.time.Duration

object StatisticsDao {

   // ============================================================================
   // STRENGTH STATISTICS
   // ============================================================================
   fun getStrengthStatistics(athleteId: Int, timeRange: Duration): List<StrengthStatistic> = transaction {
      val cutoffDate = Clock.System.now().minus(timeRange)

      StrengthStatisticEntity.find {
         (StrengthStatistics.athleteId eq athleteId) and
                 (StrengthStatistics.recordedAt greaterEq cutoffDate)
      }
         .orderBy(StrengthStatistics.recordedAt to SortOrder.DESC)
         .map { it.toModel() }
   }

   // ============================================================================
   // ENDURANCE STATISTICS
   // ============================================================================
   fun getEnduranceStatistics(athleteId: Int, timeRange: Duration): List<EnduranceStatistic> = transaction {
      val cutoffDate = Clock.System.now().minus(timeRange)

      EnduranceStatisticEntity.find {
         (EnduranceStatistics.athleteId eq athleteId) and
                 (EnduranceStatistics.recordedAt greaterEq cutoffDate)
      }
         .orderBy(EnduranceStatistics.recordedAt to SortOrder.DESC)
         .map { it.toModel() }
   }

   // ============================================================================
   // BODY MEASUREMENT HISTORY
   // ============================================================================
   fun getMeasurementHistory(athleteId: Int, timeRange: Duration): List<BodyMeasurementHistoric> = transaction {
      val cutoffDate = Clock.System.now().minus(timeRange)

      BodyMeasurementHistoryEntity.find {
         (BodyMeasurementHistory.athleteId eq athleteId) and
                 (BodyMeasurementHistory.recordedAt greaterEq cutoffDate)
      }
         .orderBy(BodyMeasurementHistory.recordedAt to SortOrder.DESC)
         .map { it.toModel() }
   }

   // ============================================================================
   // COMPLETION STATISTICS
   // ============================================================================


   // ============================================================================
   // HELPER METHODS - ROW MAPPERS
   // ============================================================================


   private fun rowToStrengthStatistics(row: ResultRow): StrengthStatistic {
      return StrengthStatistic(
         id = row[StrengthStatistics.id].value,
         athleteId = row[StrengthStatistics.athleteId].value,
         recordedAt = row[StrengthStatistics.recordedAt],
         benchPressMax = row[StrengthStatistics.benchPressMax],
         squatMax = row[StrengthStatistics.squatMax],
         deadliftMax = row[StrengthStatistics.deadliftMax],
         pullUpsMax = row[StrengthStatistics.pullUpsMax],
         pushUpsMax = row[StrengthStatistics.pushUpsMax],
         strengthIndex = row[StrengthStatistics.strengthIndex],
         muscularEndurance = row[StrengthStatistics.muscularEndurance],
         powerOutput = row[StrengthStatistics.powerOutput]
      )
   }

   private fun rowToEnduranceStatistics(row: ResultRow): EnduranceStatistic {
      return EnduranceStatistic(
         id = row[EnduranceStatistics.id].value,
         athleteId = row[EnduranceStatistics.athleteId].value,
         recordedAt = row[EnduranceStatistics.recordedAt],
         vo2Max = row[EnduranceStatistics.vo2Max],
         restingHeartRate = row[EnduranceStatistics.restingHeartRate],
         maxHeartRate = row[EnduranceStatistics.maxHeartRate],
         runningPace = row[EnduranceStatistics.runningPace],
         cardioEndurance = row[EnduranceStatistics.cardioEndurance],
         aerobicCapacity = row[EnduranceStatistics.aerobicCapacity],
         recoveryTime = row[EnduranceStatistics.recoveryTime],
         distanceCovered = row[EnduranceStatistics.distanceCovered]
      )
   }

   private fun rowToBodyMeasurementHistory(row: ResultRow): BodyMeasurementHistoric {
      return BodyMeasurementHistoric(
         id = row[BodyMeasurementHistory.id].value,
         athleteId = row[BodyMeasurementHistory.athleteId].value,
         measurements = Measurements(),
         recordedAt = row[BodyMeasurementHistory.recordedAt],
         weightChange = row[BodyMeasurementHistory.weightChange],
         bodyFatChange = row[BodyMeasurementHistory.bodyFatChange],
         muscleMassGain = row[BodyMeasurementHistory.muscleMassGain],
         bmi = row[BodyMeasurementHistory.bmi],
         bodyComposition = row[BodyMeasurementHistory.bodyComposition]
      )
   }


   /* private fun rowToCompletionWorkoutStatistics(row: ResultRow): CompletionWorkoutStatistic {
       return CompletionWorkoutStatistic(

       )
    }*/

   /*private fun rowToCompletionDietStatistics(row: ResultRow): CompletionDietStatistic {
      return CompletionDietStatistic(
         id = row[CompletionDietStatistics.id].value,
         dietId = row[CompletionDietStatistics.dietId].value,
         athleteId = row[CompletionDietStatistics.athleteId].value,
         completeAt = row[CompletionDietStatistics.completeAt]
      )
   }*/

   // ============================================================================
   // ANALYTICAL METHODS
   // ============================================================================

   /* fun getAthleteProgressSummary(athleteId: Int, fromDate: Instant? = null): Map<String, Any> = transaction {

       val stats = if (fromDate != null) {
          AthleteStatistics.selectAll()
             .where {
                (AthleteStatistics.athleteId eq athleteId) and (AthleteStatistics.recordedAt greaterEq fromDate)
             }
             .orderBy(AthleteStatistics.recordedAt to SortOrder.ASC)
             .map {
                rowToAthleteStatistics(it)
             }
       } else {
          AthleteStatistics.selectAll()
             .where { (AthleteStatistics.athleteId eq athleteId) }
             .orderBy(AthleteStatistics.recordedAt to SortOrder.ASC)
             .map { rowToAthleteStatistics(it) }
       }

       if (stats.isEmpty()) {
          return@transaction emptyMap<String, Any>()
       }

       val first = stats.first()
       val latest = stats.last()

       mapOf(
          "strengthImprovement" to (latest.strengthScore - first.strengthScore),
          "enduranceImprovement" to (latest.enduranceScore - first.enduranceScore),
          "flexibilityImprovement" to (latest.flexibilityScore - first.flexibilityScore),
          "overallImprovement" to (latest.overallPerformance - first.overallPerformance),
          "totalWorkoutsCompleted" to latest.workoutsCompleted,
          "totalCaloriesBurned" to latest.caloriesBurned,
          "personalRecordsAchieved" to latest.personalRecords,
          "averageWorkoutIntensity" to latest.averageWorkoutIntensity,
          "recordingPeriod" to "${first.recordedAt} to ${latest.recordedAt}"
       )
    }*/

   /*fun getWorkoutCompletionRate(athleteId: Int, fromDate: Instant? = null): Double = transaction {
      val conditions = mutableListOf<Op<Boolean>>()
      conditions.add(CompletionWorkoutStatistics.athleteId eq athleteId)
      fromDate?.let { conditions.add(CompletionWorkoutStatistics.completeAt greaterEq it) }

      val completedWorkouts = CompletionWorkoutStatistics.select { conditions.reduce { acc, op -> acc and op } }
         .count()

      // Aquí podrías calcular el total de workouts asignados vs completados
      // Por simplicidad, retornamos el número de workouts completados
      completedWorkouts.toDouble()
   }*/

   /* fun getDietComplianceRate(athleteId: Int, fromDate: Instant? = null): Double = transaction {
       val conditions = mutableListOf<Op<Boolean>>()
       conditions.add(NutritionStatistics.athleteId eq athleteId)
       fromDate?.let { conditions.add(NutritionStatistics.recordedAt greaterEq it) }

       val nutritionStats = NutritionStatistics.select { conditions.reduce { acc, op -> acc and op } }
          .map { it[NutritionStatistics.dietAdherence] }

       if (nutritionStats.isEmpty()) 0.0 else nutritionStats.average()
    }*/
}