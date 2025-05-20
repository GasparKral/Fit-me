package es.gaspardev.db

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class SocialLinkDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<SocialLinkDAO>(SocialLinksTable)

    var trainerId by SocialLinksTable.trainerId
    var socialMedia by SocialLinksTable.socialMedia
    var link by SocialLinksTable.link
}