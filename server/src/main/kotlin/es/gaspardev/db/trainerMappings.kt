package es.gaspardev.db

import es.gaspardev.core.domain.entities.Certification
import es.gaspardev.core.domain.entities.Social
import es.gaspardev.core.domain.entities.Trainer
import es.gaspardev.core.domain.entities.WeeklyAvailability
import kotlinx.datetime.Instant
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.statements.StatementType
import org.jetbrains.exposed.sql.transactions.transaction

class TrainerDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<TrainerDAO>(TrainerTable)

    var userId by TrainerTable.userId
    var specialization by TrainerTable.specialization
    var yearsOfExperience by TrainerTable.years_of_experience
    var bio by TrainerTable.bio
    var certificationIds by TrainerTable.certification

    // Related objects
    val user by UserDAO referencedOn TrainerTable.userId
    val socialLinks by SocialLinkDAO referrersOn SocialLinksTable.trainerId
    val sportsmans by SportsmanDAO optionalReferrersOn SportsmanTable.trainerId

    fun toModel(dao: TrainerDAO = this): Trainer = transaction {
        val userModel = UserDAO.find { UserTable.id eq userId }.map { dao -> dao.toModel() }.first()

        // Convert social links to map
        val socialLinksMap = dao.socialLinks.associate { socialLinkDao ->
            socialLinkDao.socialMedia to socialLinkDao.link
        }

        // Get certifications based on stored IDs
        val certifications = dao.certificationIds.mapNotNull { certId ->
            CertificationDAO.findById(certId)?.let { certDao ->
                Certification(
                    certDao.name,
                    certDao.issuingOrganization,
                    certDao.obtainedDate
                )
            }
        }

        // Map sportsmans - assuming a SportsmanDAO and corresponding toModel function exists
        val sportsmansList = dao.sportsmans.map { sportsmanDao ->
            sportsmanDao.toModel()
        }

        // Create the Trainer object
        // Note: WeeklyAvailability is missing from the database structure, using a default or placeholder
        Trainer(
            user = userModel,
            specialization = dao.specialization,
            yearsOfExperience = dao.yearsOfExperience,
            bio = dao.bio,
            _rawSocialLinks = socialLinksMap,
            availability = WeeklyAvailability(mapOf()),
            certifications = certifications,
            sportmans = sportsmansList
        )
    }


}

