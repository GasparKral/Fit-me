package es.gaspardev.database.daos

import es.gaspardev.database.Users
import es.gaspardev.database.entities.UserEntity
import kotlinx.datetime.Instant
import org.jetbrains.exposed.sql.transactions.transaction

class UserDao {
    fun createUser(
        fullname: String,
        password: String,
        email: String,
        phone: String,
    ): UserEntity = transaction {
        UserEntity.new {
            this.fullname = fullname
            this.password = password
            this.email = email
            this.phone = phone
            this.creationDate = Instant.fromEpochMilliseconds(System.currentTimeMillis())
        }
    }

    fun findUserByEmail(email: String): UserEntity? = transaction {
        UserEntity.find { Users.email eq email }.singleOrNull()
    }

    fun findUserById(id: Int): UserEntity? = transaction {
        UserEntity.findById(id)
    }

    fun updateUser(userId: Int, updates: UserEntity.() -> Unit): UserEntity? = transaction {
        UserEntity.findById(userId)?.apply(updates)
    }

    fun deleteUser(userId: Int): Boolean = transaction {
        UserEntity.findById(userId)?.delete() != null
    }

    fun getAllUsers(): List<UserEntity> = transaction {
        UserEntity.all().toList()
    }
}