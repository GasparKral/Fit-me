package es.gaspardev.db

import es.gaspardev.core.domain.entities.User
import es.gaspardev.core.domain.entities.UserStatus
import kotlinx.datetime.Instant
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID


class UserDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserDAO>(UserTable)

    var name by UserTable.name
    var email by UserTable.email
    var creationTime by UserTable.creationTime
    var userImage by UserTable.userImage

    fun toModel(dao: UserDAO = this) = User(
        dao.id.value,
        dao.name,
        dao.email,
        dao.creationTime,
        null,
        UserStatus()
    )
}



