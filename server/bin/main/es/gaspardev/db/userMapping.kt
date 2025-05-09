package es.gaspardev.db

import es.gaspardev.core.domain.entities.Resource
import es.gaspardev.core.domain.entities.User
import es.gaspardev.enums.MediaType
import kotlinx.datetime.Instant
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID


class UserDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserDAO>(UserTable)

    var name by UserTable.name
    var password by UserTable.password
    var email by UserTable.email
    var creationTime by UserTable.creationTime
    var userImage by UserTable.userImage
}


fun daoToModel(dao: UserDAO) = User(
    dao.id.value,
    dao.name,
    dao.password,
    dao.email,
    Instant.fromEpochSeconds(dao.creationTime.epochSecond),
    Resource(MediaType.IMAGE, dao.userImage)
)
