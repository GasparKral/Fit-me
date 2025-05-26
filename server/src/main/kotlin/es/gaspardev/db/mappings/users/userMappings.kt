package es.gaspardev.db.mappings.users

import es.gaspardev.core.domain.entities.*
import es.gaspardev.db.*
import es.gaspardev.db.mappings.appointment.SessionEntity
import es.gaspardev.db.mappings.diets.DietDao
import es.gaspardev.db.mappings.diets.DietEntity
import es.gaspardev.db.mappings.diets.DishEntity.Companion.referrersOn
import es.gaspardev.db.mappings.workouts.WorkoutDao
import es.gaspardev.db.mappings.workouts.WorkoutEntity
import es.gaspardev.modules.endpoints.BaseDao
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalTime
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID


// User DAO
class UserDao : BaseDao<UserEntity, Int>(UserEntity) {
    fun toDomain(user: UserEntity): User {
        return User(
            id = user.id.value,
            name = user.name,
            email = user.email,
            phone = user.phone,
            creationTime = user.creationTime,
            userImage = user.userImage?.resource?.let { ResourceDao().toDomain(it) },
            status = UserStatus(
                status = user.status,
                lastActive = user.lastActive,
                needsAttetion = user.needsAttention
            )
        )
    }

    fun fromDomain(user: User, resourceDao: ResourceDao): UserEntity {
        return UserEntity.new(user.id) {
            name = user.name
            email = user.email
            phone = user.phone
            creationTime = user.creationTime
            status = user.status.status
            lastActive = user.status.lastActive
            needsAttention = user.status.needsAttetion
        }
    }
}

class UserEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserEntity>(UsersTable)

    var name by UsersTable.name
    var email by UsersTable.email
    var phone by UsersTable.phone
    var creationTime by UsersTable.creationTime
    var status by UsersTable.status
    var lastActive by UsersTable.lastActive
    var needsAttention by UsersTable.needsAttention
    val userImage by UserImageEntity optionalBackReferencedOn UserImagesTable.userId
    var password by UsersTable.password
}

// Resource DAO
class ResourceDao : BaseDao<ResourceEntity, Int>(ResourceEntity) {
    fun toDomain(resource: ResourceEntity): Resource {
        return Resource(
            resourceType = resource.resourceType,
            src = resource.src
        )
    }

    fun fromDomain(resource: Resource): ResourceEntity {
        return ResourceEntity.new {
            resourceType = resource.resourceType
            src = resource.src
        }
    }
}

class ResourceEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ResourceEntity>(ResourcesTable)

    var resourceType by ResourcesTable.resourceType
    var src by ResourcesTable.src
}

class UserImageEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserImageEntity>(UserImagesTable)

    var user by UserEntity optionalReferencedOn UserImagesTable.userId
    var resource by ResourceEntity referencedOn UserImagesTable.resourceId
}


// Trainer DAO
class TrainerDao : BaseDao<TrainerEntity, Int>(TrainerEntity) {
    fun toDomain(trainer: TrainerEntity, includeSportsmen: Boolean = true): Trainer {
        return Trainer(
            user = UserDao().toDomain(trainer.user),
            specialization = trainer.specialization,
            yearsOfExperience = trainer.yearsOfExperience,
            bio = trainer.bio,
            _rawSocialLinks = trainer.socialLinks.associate { it.platform to it.url },
            availability = WeeklyAvailability(
                trainer.timeSlots.groupBy { it.day }
                    .mapValues { (_, slots) ->
                        DayAvailability(
                            weekDay = slots.first().day,
                            timeSlots = slots.map {
                                TimeSlot(
                                    LocalTime.parse(it.startTime),
                                    LocalTime.parse(it.endTime)
                                )
                            }
                        )
                    }
            ),
            certifications = trainer.certifications.map { CertificationDao().toDomain(it) },
            sportmans = if (includeSportsmen) {
                trainer.sportsmen.map { SportsmanDao().toDomain(it, includeTrainer = false) }
            } else emptyList()
        )
    }
}


class TrainerEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<TrainerEntity>(TrainersTable)

    var user by UserEntity referencedOn TrainersTable.userId
    var specialization by TrainersTable.specialization
    var yearsOfExperience by TrainersTable.yearsOfExperience
    var bio by TrainersTable.bio

    val socialLinks by SocialLinkEntity referrersOn SocialLinksTable.trainerId
    val certifications by CertificationEntity referrersOn CertificationsTable.trainerId
    val timeSlots by TimeSlotEntity referrersOn TimeSlotsTable.trainerId
    val sportsmen by SportsmanEntity optionalReferrersOn SportsmenTable.trainerId
    val sessions by SessionEntity referrersOn SessionsTable.trainerId
}

// SocialLink DAO
class SocialLinkDao : BaseDao<SocialLinkEntity, Int>(SocialLinkEntity) {
    fun toDomain(socialLink: SocialLinkEntity): Pair<Social, String> {
        return Social.valueOf(socialLink.platform) to socialLink.url
    }
}

class SocialLinkEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<SocialLinkEntity>(SocialLinksTable)

    var trainerId by TrainerEntity referencedOn SocialLinksTable.trainerId
    var platform by SocialLinksTable.platform
    var url by SocialLinksTable.url
}

// Certification DAO
class CertificationDao : BaseDao<CertificationEntity, Int>(CertificationEntity) {
    fun toDomain(certification: CertificationEntity): Certification {
        return Certification(
            name = certification.name,
            issuinOrganization = certification.issuingOrganization,
            dateObtained = certification.dateObtained
        )
    }
}

class CertificationEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<CertificationEntity>(CertificationsTable)

    var trainerId by TrainerEntity referencedOn CertificationsTable.trainerId
    var name by CertificationsTable.name
    var issuingOrganization by CertificationsTable.issuingOrganization
    var dateObtained by CertificationsTable.dateObtained
}

// Sportsman DAO
class SportsmanDao : BaseDao<SportsmanEntity, Int>(SportsmanEntity) {
    fun toDomain(sportsman: SportsmanEntity, includeTrainer: Boolean = true): Sportsman {
        return Sportsman(
            user = UserDao().toDomain(sportsman.user),
            trainer = if (includeTrainer) {
                sportsman.trainer?.let { TrainerDao().toDomain(it, includeSportsmen = false) }
            } else null,
            age = sportsman.age,
            sex = sportsman.sex,
            trainingSince = sportsman.trainingSince,
            allergies = sportsman.allergies.map { it.allergy },
            workouts = sportsman.workout?.let { WorkoutDao().toDomain(it) },
            diet = sportsman.diet?.let { DietDao().toDomain(it) },
            mesasurements = sportsman.measurement?.let { MeasurementDao().toDomain(it) } ?: Measurements()
        )
    }
}

class SportsmanEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<SportsmanEntity>(SportsmenTable)

    var user by UserEntity referencedOn SportsmenTable.userId
    var trainer by TrainerEntity optionalReferencedOn SportsmenTable.trainerId
    var age by SportsmenTable.age
    var sex by SportsmenTable.sex
    var trainingSince by SportsmenTable.trainingSince

    val allergies by AllergyEntity referrersOn AllergiesTable.sportsmanId
    val measurement by MeasurementEntity optionalBackReferencedOn MeasurementsTable.sportsmanId
    val workout by WorkoutEntity optionalBackReferencedOn WorkoutsTable.sportsmanId
    val diet by DietEntity optionalBackReferencedOn DietsTable.sportsmanId
}

// Allergy DAO
class AllergyDao : BaseDao<AllergyEntity, Int>(AllergyEntity) {
    fun toDomain(allergy: AllergyEntity): String = allergy.allergy
}

class AllergyEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<AllergyEntity>(AllergiesTable)

    var sportsmanId by SportsmanEntity referencedOn AllergiesTable.sportsmanId
    var allergy by AllergiesTable.allergy
}

// Measurement DAO
class MeasurementDao : BaseDao<MeasurementEntity, Int>(MeasurementEntity) {
    fun toDomain(measurement: MeasurementEntity): es.gaspardev.core.domain.entities.Measurements {
        return Measurements(
            weight = measurement.weight,
            height = measurement.height,
            bodyFat = measurement.bodyFat,
            lastUpdated = measurement.lastUpdated
        )
    }
}

class MeasurementEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<MeasurementEntity>(MeasurementsTable)

    var sportsmanId by SportsmanEntity referencedOn MeasurementsTable.sportsmanId
    var weight by MeasurementsTable.weight
    var height by MeasurementsTable.height
    var bodyFat by MeasurementsTable.bodyFat
    var lastUpdated by MeasurementsTable.lastUpdated
}

// TimeSlot DAO
class TimeSlotDao : BaseDao<TimeSlotEntity, Int>(TimeSlotEntity) {
    fun toDomain(timeSlot: TimeSlotEntity): TimeSlot {
        return TimeSlot(
            start = LocalTime.parse(timeSlot.startTime),
            end = LocalTime.parse(timeSlot.endTime)
        )
    }
}

class TimeSlotEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<TimeSlotEntity>(TimeSlotsTable)

    var trainerId by TrainerEntity referencedOn TimeSlotsTable.trainerId
    var day by TimeSlotsTable.day
    var startTime by TimeSlotsTable.startTime
    var endTime by TimeSlotsTable.endTime
}


class MeasurementHistoryEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<MeasurementHistoryEntity>(MeasurementHistoryTable)

    var sportsman by SportsmanEntity referencedOn MeasurementHistoryTable.sportsmanId
    var weight by MeasurementHistoryTable.weight
    var height by MeasurementHistoryTable.height
    var bodyFat by MeasurementHistoryTable.bodyFat
    var timestamp by MeasurementHistoryTable.timestamp
}

class MeasurementHistoryDao : BaseDao<MeasurementHistoryEntity, Int>(MeasurementHistoryEntity) {
    fun toDomain(entity: MeasurementHistoryEntity): Measurements = Measurements(
        weight = entity.weight,
        height = entity.height,
        bodyFat = entity.bodyFat,
        lastUpdated = entity.timestamp
    )

    fun fromDomain(sportsman: SportsmanEntity, measurements: Measurements): MeasurementHistoryEntity {
        return MeasurementHistoryEntity.new {
            this.sportsman = sportsman
            weight = measurements.weight
            height = measurements.height
            bodyFat = measurements.bodyFat
            timestamp = measurements.lastUpdated ?: Clock.System.now()
        }
    }
}