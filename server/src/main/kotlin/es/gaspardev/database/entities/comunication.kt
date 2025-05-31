package es.gaspardev.database.entities

import es.gaspardev.core.domain.entities.comunication.Conversation
import es.gaspardev.core.domain.entities.comunication.Message
import es.gaspardev.core.domain.entities.comunication.Session
import es.gaspardev.database.Conversations
import es.gaspardev.database.Messages
import es.gaspardev.database.SessionNotes
import es.gaspardev.database.Sessions
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class ConversationEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ConversationEntity>(Conversations)

    var trainer by UserEntity referencedOn Conversations.trainerId
    var athlete by UserEntity referencedOn Conversations.athleteId
    var createdAt by Conversations.createdAt

    // Relaciones
    val messages by MessageEntity referrersOn Messages.conversationId

    fun toModel(): Conversation {
        return Conversation(
            id = this.id.value,
            trainer = this.trainer.toModel(),
            athlete = this.athlete.toModel(),
            messages = this.messages.map { it.toModel() },
            lastMessage = this.messages.map { it.toModel() }.minByOrNull { it.sendAt }!!.sendAt,
        )
    }
}

class MessageEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<MessageEntity>(Messages)

    var conversation by ConversationEntity referencedOn Messages.conversationId
    var userName by Messages.userName
    var sentAt by Messages.sentAt
    var content by Messages.content
    var status by Messages.status

    fun toModel(): Message {
        return Message(
            userName = this.userName,
            sendAt = this.sentAt,
            content = this.content,
            messageStatus = this.status,
        )
    }
}

class SessionEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<SessionEntity>(Sessions)

    var title by Sessions.title
    var dateTime by Sessions.dateTime
    var sessionType by Sessions.sessionType
    var trainer by TrainerEntity referencedOn Sessions.trainerId
    var athlete by AthleteEntity referencedOn Sessions.athleteId
    var expectedDuration by Sessions.expectedDuration
    var actualDuration by Sessions.actualDuration
    var completed by Sessions.completed
    var notes by NoteEntity via SessionNotes

    fun toModel(): Session {
        return Session(
            title = this.title,
            dateTime = this.dateTime,
            sessionType = this.sessionType,
            with = this.athlete.userEntity.toModel().fullname,
            expectedDuration = this.expectedDuration,
            notes = this.notes.map { it.toModel() }
        )
    }
}
