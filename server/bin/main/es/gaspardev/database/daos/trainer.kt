package es.gaspardev.database.daos

import es.gaspardev.core.domain.entities.users.Athlete
import es.gaspardev.core.domain.entities.users.Trainer
import es.gaspardev.database.entities.*
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

    fun getAthletes(trainerId: Int): List<Athlete> = transaction {
        AthleteEntity.all().filter { it.trainer?.user?.id?.value == trainerId }.map { it.toModel() }
    }

    fun deleteTrainer(trainerId: Int) {
        transaction {
            val trainer = TrainerEntity.all().firstOrNull { it.user.id.value == trainerId }

            if (trainer != null) {
                trainer.delete()
            } else {
                throw NoSuchElementException("Trainer with ID $trainerId not found")
            }
        }
    }


}