package es.gaspardev.database.entities

import es.gaspardev.core.domain.entities.comunication.Conversation
import es.gaspardev.core.domain.entities.comunication.Message
import es.gaspardev.database.*
import es.gaspardev.enums.MessageStatus
import kotlinx.datetime.Clock
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class ConversationEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ConversationEntity>(Conversations)

    var trainer by UserEntity referencedOn Conversations.trainerId
    var athlete by UserEntity referencedOn Conversations.athleteId
    var createdAt by Conversations.createdAt
    var updatedAt by Conversations.updatedAt
    var lastActivityAt by Conversations.lastActivityAt

    // Relaciones
    val messages by MessageEntity referrersOn Messages.conversationId
    val conversationUsers by ConversationUserEntity referrersOn ConversationUsers.conversationId

    fun toModel(currentUserId: Int? = null): Conversation {
        val sortedMessages = messages.sortedBy { it.sentAt }.map { it.toModel() }
        val lastMessage = sortedMessages.lastOrNull()

        // Si se proporciona currentUserId, obtener datos específicos del usuario
        val userSpecificData = currentUserId?.let { userId ->
            conversationUsers.find { it.user.id.value == userId }
        }

        return Conversation(
            id = this.id.value,
            trainer = this.trainer.toModel(),
            athlete = this.athlete.toModel(),
            messages = sortedMessages,
            lastMessage = lastMessage,
            lastActivity = this.lastActivityAt,
            unreadCount = userSpecificData?.unreadCount ?: 0,
            isOnline = false, // Se calculará desde UserStatus en tiempo real
            createdAt = this.createdAt,
            updatedAt = this.updatedAt
        )
    }

    fun updateLastActivity() {
        this.lastActivityAt = Clock.System.now()
        this.updatedAt = Clock.System.now()
    }
}

class MessageEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<MessageEntity>(Messages)

    var messageId by Messages.messageId
    var conversation by ConversationEntity referencedOn Messages.conversationId
    var sender by UserEntity referencedOn Messages.senderId
    var receiver by UserEntity referencedOn Messages.receiverId
    var senderName by Messages.senderName
    var content by Messages.content
    var messageType by Messages.messageType
    var sentAt by Messages.sentAt
    var deliveredAt by Messages.deliveredAt
    var readAt by Messages.readAt
    var status by Messages.status
    var editedAt by Messages.editedAt
    var isDeleted by Messages.isDeleted
    var metadata by Messages.metadata

    fun toModel(): Message {
        return Message(
            id = this.messageId,
            conversationId = this.conversation.id.value,
            senderId = this.sender.id.value,
            senderName = this.senderName,
            receiverId = this.receiver.id.value,
            content = if (this.isDeleted) "[Message deleted]" else this.content,
            messageType = this.messageType,
            sentAt = this.sentAt,
            deliveredAt = this.deliveredAt,
            readAt = this.readAt,
            messageStatus = this.status,
            isFromCurrentUser = false // Se establecerá en el cliente
        )
    }

    fun markAsDelivered() {
        if (status == MessageStatus.SENT) {
            status = MessageStatus.DELIVERED
            deliveredAt = Clock.System.now()
        }
    }

    fun markAsRead() {
        if (status != MessageStatus.READ) {
            status = MessageStatus.READ
            readAt = Clock.System.now()
            if (deliveredAt == null) {
                deliveredAt = Clock.System.now()
            }
        }
    }
}

class ConversationUserEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ConversationUserEntity>(ConversationUsers)

    var conversation by ConversationEntity referencedOn ConversationUsers.conversationId
    var user by UserEntity referencedOn ConversationUsers.userId
    var lastReadMessageId by ConversationUsers.lastReadMessageId
    var unreadCount by ConversationUsers.unreadCount
    var joinedAt by ConversationUsers.joinedAt
    var leftAt by ConversationUsers.leftAt
    var isMuted by ConversationUsers.isMuted
    var isArchived by ConversationUsers.isArchived

    fun incrementUnreadCount() {
        unreadCount += 1
    }

    fun markAllAsRead(lastMessageId: String) {
        lastReadMessageId = lastMessageId
        unreadCount = 0
    }
}


class ActiveSessionEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ActiveSessionEntity>(ActiveSessions)

    var user by UserEntity referencedOn ActiveSessions.userId
    var conversation by ConversationEntity referencedOn ActiveSessions.conversationId
    var sessionId by ActiveSessions.sessionId
    var connectedAt by ActiveSessions.connectedAt
    var lastPingAt by ActiveSessions.lastPingAt
    var userAgent by ActiveSessions.userAgent
    var ipAddress by ActiveSessions.ipAddress

    fun updatePing() {
        lastPingAt = Clock.System.now()
    }
}
