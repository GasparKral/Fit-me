package es.gaspardev.database.entities


import es.gaspardev.core.domain.entities.comunication.Session
import es.gaspardev.core.domain.entities.users.Athlete
import es.gaspardev.core.domain.entities.users.Trainer
import es.gaspardev.core.domain.entities.users.info.Allergy
import es.gaspardev.core.domain.entities.users.info.Certification
import es.gaspardev.core.domain.entities.users.info.TimeSlot
import es.gaspardev.database.*
import es.gaspardev.enums.WeekDay
import kotlinx.datetime.Clock
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.ResultRow

// ============================================================================
// ENTIDADES DE USUARIOS
// ============================================================================

class UserEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserEntity>(Users)

    var fullname by Users.fullname
    var password by Users.password
    var email by Users.email
    var phone by Users.phone
    var creationDate by Users.creationDate
    var userImageUrl by Users.userImageUrl

    // Relaciones
    val userStatus by UserStatusEntity backReferencedOn UserStatus.userId


    fun toModel(): es.gaspardev.core.domain.entities.users.User {
        return es.gaspardev.core.domain.entities.users.User(
            id = this.id.value,
            fullname = this.fullname,
            password = this.password,
            email = this.email,
            phone = this.phone,
            creationDate = this.creationDate,
            userImageURL = this.userImageUrl,
            status = this.userStatus.toModel()
        )
    }
}

class UserStatusEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserStatusEntity>(UserStatus)

    var state by UserStatus.state
    var lastTimeActive by UserStatus.lastTimeActive
    var user by UserEntity referencedOn UserStatus.userId
    var isOnline by UserStatus.isOnline
    var lastSeenAt by UserStatus.lastSeenAt

    fun setOnline() {
        isOnline = true
        lastTimeActive = Clock.System.now()
        lastSeenAt = null
    }

    fun setOffline() {
        isOnline = false
        lastSeenAt = Clock.System.now()
    }

    fun toModel(): es.gaspardev.core.domain.entities.users.info.UserStatus {
        return es.gaspardev.core.domain.entities.users.info.UserStatus(
            state = this.state,
            lastTimeActive = this.lastTimeActive
        )
    }
}

class TrainerEntity(id: EntityID<Int>) : Entity<Int>(id) {
    companion object : EntityClass<Int, TrainerEntity>(Trainers) {
        override fun createInstance(entityId: EntityID<Int>, row: ResultRow?) = TrainerEntity(entityId)
    }

    var user by UserEntity referencedOn Trainers.userId
    var specialization by Trainers.specialization
    var yearsOfExperience by Trainers.yearsOfExperience

    // Relaciones
    val certifications by CertificationEntity referrersOn Certifications.trainerId
    val socials by TrainerSocialEntity referrersOn TrainerSocials.trainerId
    val availability by TrainerAvailabilityEntity referrersOn TrainerAvailability.trainerId
    val athletes by AthleteEntity optionalReferrersOn Athletes.trainer
    val sessions by SessionEntity referrersOn Sessions.trainerId

    fun toModel(): Trainer {
        return Trainer(
            user = this.user.toModel(),
            specialization = this.specialization,
            yearsOfExperiencie = this.yearsOfExperience,
            raw_socials = this.socials.map { it.toSingleValue() }.associateBy({ it.first }, { it.second }),
            certifications = this.certifications.map { it.toModel() },
            availability = this.availability
                .map { it.toModel() }
                .groupBy({ it.first }, { it.second })
        )
    }
}

class TrainerSocialEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<TrainerSocialEntity>(TrainerSocials)

    var trainer by TrainerEntity referencedOn TrainerSocials.trainerId
    var platform by TrainerSocials.platform
    var url by TrainerSocials.url

    fun toSingleValue(): Pair<String, String> {
        return Pair(platform, url)
    }
}

class AthleteEntity(id: EntityID<Int>) : Entity<Int>(id) {
    companion object : EntityClass<Int, AthleteEntity>(Athletes) {
        override fun createInstance(entityId: EntityID<Int>, row: ResultRow?) = AthleteEntity(entityId)

        fun getByUserId(userId: Int): AthleteEntity {
            return AthleteEntity.all().first { it.userEntity.id.value == userId }
        }
    }

    var userEntity by UserEntity referencedOn Athletes.userId
    var age by Athletes.age
    var sex by Athletes.sex
    var trainingSince by Athletes.trainingSince
    var workout by WorkoutEntity optionalReferencedOn Athletes.workoutId
    var diet by DietEntity optionalReferencedOn Athletes.dietId
    var trainer by TrainerEntity optionalReferencedOn Athletes.trainer
    var needAssistant by Athletes.needAssistant

    // Relaciones
    val allergies by AllergyEntity via AthleteAllergies
    val measurements by MeasurementEntity referrersOn Measurements.athleteId
    /* val workoutCompletions by CompletionWorkoutStatisticEntity referrersOn CompletionWorkoutStatistics.athleteId
     val dietCompletions by CompletionDietStatisticEntity referrersOn CompletionDietStatistics.athleteId
     val conversations by ConversationEntity referrersOn Conversations.athleteId
     val sessions by SessionEntity referrersOn Sessions.athleteId*/

    fun toModel(): Athlete {
        return Athlete(
            user = this.userEntity.toModel(),
            age = this.age,
            sex = this.sex,
            trainingSince = this.trainingSince,
            allergies = this.allergies.map { it.toModel() },
            measurements = measurements.sortedBy { it.measureAt }.first().toModel(),
            workout = this.workout?.toModel(),
            diet = this.diet?.toModel(),
            needAssistant = this.needAssistant
        )
    }
}

// ============================================================================
// ENTIDADES DE INFORMACIÓN DE USUARIOS
// ============================================================================

class AllergyEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<AllergyEntity>(Allergies)

    var name by Allergies.name

    // Relaciones
    val athletes by AthleteEntity via AthleteAllergies

    fun toModel(): Allergy {
        return Allergy(
            id = this.id.value,
            name = this.name
        )
    }
}

class MeasurementEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<MeasurementEntity>(Measurements)

    var athlete by AthleteEntity referencedOn Measurements.athleteId
    var weight by Measurements.weight
    var height by Measurements.height
    var bodyFat by Measurements.bodyFat
    var armSize by Measurements.armSize
    var chestBackSize by Measurements.chestBackSize
    var hipSize by Measurements.hipSize
    var legSize by Measurements.legSize
    var calvesSize by Measurements.calvesSize
    var measureAt by Measurements.measureAt
    var isCurrent by Measurements.isCurrent

    fun toModel(): es.gaspardev.core.domain.entities.users.info.Measurements {
        return es.gaspardev.core.domain.entities.users.info.Measurements(
            weight = this.weight,
            height = this.height,
            bodyFat = this.bodyFat,
            armSize = this.armSize,
            chest_backSize = this.chestBackSize,
            hipSize = this.hipSize,
            legSize = this.legSize,
            calvesSize = this.calvesSize
        )
    }
}

class CertificationEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<CertificationEntity>(Certifications)

    var trainer by TrainerEntity referencedOn Certifications.trainerId
    var name by Certifications.name
    var issuingOrganization by Certifications.issuingOrganization
    var completeAt by Certifications.completeAt

    fun toModel(): Certification {
        return Certification(
            id = this.id.value,
            name = this.name,
            issuingOrganization = this.issuingOrganization,
            completeAt = this.completeAt
        )
    }
}

class TrainerAvailabilityEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<TrainerAvailabilityEntity>(TrainerAvailability)

    var trainer by TrainerEntity referencedOn TrainerAvailability.trainerId
    var weekDay by TrainerAvailability.weekDay
    var startTime by TrainerAvailability.startTime
    var endTime by TrainerAvailability.endTime

    fun toModel(): Pair<WeekDay, TimeSlot> {
        return Pair(this.weekDay, TimeSlot(this.startTime, this.endTime))
    }
}
