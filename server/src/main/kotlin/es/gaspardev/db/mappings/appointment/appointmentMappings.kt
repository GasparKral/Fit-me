package es.gaspardev.db.mappings.appointment

import es.gaspardev.core.domain.entities.Chat
import es.gaspardev.core.domain.entities.Message
import es.gaspardev.core.domain.entities.Session
import es.gaspardev.db.ChatsTable
import es.gaspardev.db.MessagesTable
import es.gaspardev.db.SessionsTable
import es.gaspardev.db.mappings.users.*
import es.gaspardev.modules.endpoints.BaseDao
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import kotlin.time.Duration.Companion.milliseconds

// Session DAO
class SessionDao(
    private val trainerDao: TrainerDao,
    private val sportsmanDao: SportsmanDao
) : BaseDao<SessionEntity, Int>(SessionEntity) {
    fun toDomain(session: SessionEntity): Session {
        return Session(
            id = session.id.value,
            dateTime = session.dateTime,
            with = sportsmanDao.toDomain(session.sportsmanId),
            duration = session.durationMillis.milliseconds
        )
    }
}

class SessionEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<SessionEntity>(SessionsTable)

    var trainerId by TrainerEntity referencedOn SessionsTable.trainerId
    var sportsmanId by SportsmanEntity referencedOn SessionsTable.sportsmanId
    var dateTime by SessionsTable.dateTime
    var durationMillis by SessionsTable.duration
}

// Chat DAO
class ChatDao(
    private val trainerDao: TrainerDao,
    private val sportsmanDao: SportsmanDao,
    private val messageDao: MessageDao
) : BaseDao<ChatEntity, Int>(ChatEntity) {
    fun toDomain(chat: ChatEntity): Chat {
        return Chat(
            trainer = trainerDao.toDomain(chat.trainerId),
            sportman = sportsmanDao.toDomain(chat.sportsmanId),
            message = chat.messages.map { messageDao.toDomain(it) }
        )
    }
}

class ChatEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ChatEntity>(ChatsTable)

    var trainerId by TrainerEntity referencedOn ChatsTable.trainerId
    var sportsmanId by SportsmanEntity referencedOn ChatsTable.sportsmanId

    val messages by MessageEntity referrersOn MessagesTable.chatId
}

// Message DAO
class MessageDao(private val userDao: UserDao, private val resourceDao: ResourceDao) :
    BaseDao<MessageEntity, Int>(MessageEntity) {
    fun toDomain(message: MessageEntity): Message {
        return Message(
            author = userDao.toDomain(message.authorId),
            messageType = message.type,
            message = message.message,
            media = message.mediaResourceId?.let { resourceDao.toDomain(it) },
            postTime = message.postTime
        )
    }
}

class MessageEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<MessageEntity>(MessagesTable)

    var chatId by ChatEntity referencedOn MessagesTable.chatId
    var authorId by UserEntity referencedOn MessagesTable.authorId
    var type by MessagesTable.type
    var message by MessagesTable.message
    var mediaResourceId by ResourceEntity optionalReferencedOn MessagesTable.mediaResourceId
    var postTime by MessagesTable.postTime
}