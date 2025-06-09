package es.gaspardev.database.daos

import es.gaspardev.core.domain.entities.users.Athlete
import es.gaspardev.core.domain.entities.users.Trainer
import es.gaspardev.database.entities.*
import es.gaspardev.enums.WeekDay
import kotlinx.datetime.Instant
import org.jetbrains.exposed.sql.transactions.transaction


object TrainerDao {
    fun createTrainer(
        userId: Int,
        specialization: String,
        yearsOfExperience: Int
    ): TrainerEntity = transaction {
        TrainerEntity.new(userId) {
            this.user = UserEntity[userId]
            this.specialization = specialization
            this.yearsOfExperience = yearsOfExperience
        }
    }

    fun findTrainerByCredencials(identification: String, password: String): Trainer? = transaction {
        TrainerEntity.all()
            .firstOrNull { (it.user.fullname == identification || it.user.email == identification && it.user.password == password) }
            ?.toModel()
    }

    fun findTrainerByUserId(userId: Int): TrainerEntity? = transaction {
        TrainerEntity.findById(userId)
    }

    fun getAllTrainers(): List<TrainerEntity> = transaction {
        TrainerEntity.all().toList()
    }

    fun getAthletes(trainerId: Int): List<Athlete> = transaction {
        AthleteEntity.all().filter { it.trainer?.user?.id?.value == trainerId }.map { it.toModel() }
    }

    fun addCertification(
        trainerId: Int,
        name: String,
        issuingOrganization: String,
        completeAt: Instant
    ): CertificationEntity = transaction {
        CertificationEntity.new {
            this.trainer = TrainerEntity[trainerId]
            this.name = name
            this.issuingOrganization = issuingOrganization
            this.completeAt = completeAt
        }
    }

    fun addSocial(
        trainerId: Int,
        platform: String,
        url: String
    ): TrainerSocialEntity = transaction {
        TrainerSocialEntity.new {
            this.trainer = TrainerEntity[trainerId]
            this.platform = platform
            this.url = url
        }
    }

    fun addAvailability(
        trainerId: Int,
        weekDay: WeekDay,
        startTime: Instant,
        endTime: Instant
    ): TrainerAvailabilityEntity = transaction {
        TrainerAvailabilityEntity.new {
            this.trainer = TrainerEntity[trainerId]
            this.weekDay = weekDay
            this.startTime = startTime
            this.endTime = endTime
        }
    }


}