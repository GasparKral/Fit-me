package es.gaspardev.database.entities

import es.gaspardev.core.domain.entities.stadistics.*
import es.gaspardev.database.*
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

// ============================================================================
// ENTIDADES DE ESTADÍSTICAS
// ============================================================================


class StrengthStatisticEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<StrengthStatisticEntity>(StrengthStatistics)

    var athlete by AthleteEntity referencedOn StrengthStatistics.athleteId
    var recordedAt by StrengthStatistics.recordedAt
    var benchPressMax by StrengthStatistics.benchPressMax
    var squatMax by StrengthStatistics.squatMax
    var deadliftMax by StrengthStatistics.deadliftMax
    var pullUpsMax by StrengthStatistics.pullUpsMax
    var pushUpsMax by StrengthStatistics.pushUpsMax
    var strengthIndex by StrengthStatistics.strengthIndex
    var muscularEndurance by StrengthStatistics.muscularEndurance
    var powerOutput by StrengthStatistics.powerOutput

    fun toModel(): StrengthStatistic {
        return StrengthStatistic(
            id = this.id.value,
            athleteId = this.athlete.id.value,
            recordedAt = this.recordedAt,
            benchPressMax = this.benchPressMax,
            squatMax = this.squatMax,
            deadliftMax = this.deadliftMax,
            pullUpsMax = this.pullUpsMax,
            pushUpsMax = this.pushUpsMax,
            strengthIndex = this.strengthIndex,
            muscularEndurance = this.muscularEndurance,
            powerOutput = this.powerOutput
        )
    }
}

class EnduranceStatisticEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<EnduranceStatisticEntity>(EnduranceStatistics)

    var athlete by AthleteEntity referencedOn EnduranceStatistics.athleteId
    var recordedAt by EnduranceStatistics.recordedAt
    var vo2Max by EnduranceStatistics.vo2Max
    var restingHeartRate by EnduranceStatistics.restingHeartRate
    var maxHeartRate by EnduranceStatistics.maxHeartRate
    var runningPace by EnduranceStatistics.runningPace
    var cardioEndurance by EnduranceStatistics.cardioEndurance
    var aerobicCapacity by EnduranceStatistics.aerobicCapacity
    var recoveryTime by EnduranceStatistics.recoveryTime
    var distanceCovered by EnduranceStatistics.distanceCovered

    fun toModel(): EnduranceStatistic {
        return EnduranceStatistic(
            id = this.id.value,
            athleteId = this.athlete.id.value,
            recordedAt = this.recordedAt,
            vo2Max = this.vo2Max,
            restingHeartRate = this.restingHeartRate,
            maxHeartRate = this.maxHeartRate,
            runningPace = this.runningPace,
            cardioEndurance = this.cardioEndurance,
            aerobicCapacity = this.aerobicCapacity,
            recoveryTime = this.recoveryTime,
            distanceCovered = this.distanceCovered
        )
    }
}

class BodyMeasurementHistoryEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<BodyMeasurementHistoryEntity>(BodyMeasurementHistory)

    var athlete by AthleteEntity referencedOn BodyMeasurementHistory.athleteId
    var measurement by MeasurementEntity referencedOn BodyMeasurementHistory.measurementId
    var recordedAt by BodyMeasurementHistory.recordedAt
    var weightChange by BodyMeasurementHistory.weightChange
    var bodyFatChange by BodyMeasurementHistory.bodyFatChange
    var muscleMassGain by BodyMeasurementHistory.muscleMassGain
    var bmi by BodyMeasurementHistory.bmi
    var bodyComposition by BodyMeasurementHistory.bodyComposition

    fun toModel(): BodyMeasurementHistoric {
        return BodyMeasurementHistoric(
            id = this.id.value,
            athleteId = this.athlete.id.value,
            recordedAt = this.recordedAt,
            measurements = this.measurement.toModel(),
            weightChange = this.weightChange,
            bodyFatChange = this.bodyFatChange,
            muscleMassGain = this.muscleMassGain,
            bmi = this.bmi,
            bodyComposition = this.bodyComposition
        )
    }
}

