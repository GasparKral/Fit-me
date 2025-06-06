package es.gaspardev.database.daos

import es.gaspardev.core.domain.entities.comunication.Conversation
import es.gaspardev.database.*
import es.gaspardev.database.entities.*
import es.gaspardev.enums.MessageStatus
import es.gaspardev.enums.MessageType
import es.gaspardev.enums.StatusState
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.lowerCase
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class CommunicationDao {

    // Conversaciones
    fun getChat(conversationId: Int): ConversationEntity = transaction {
        ConversationEntity[conversationId]
    }

    fun getChatWith(userId: Int, conversationId: Int): Conversation = transaction {
        ConversationEntity[conversationId].toModel(userId)
    }

    fun getConversations(userId: Int): List<ConversationEntity> = transaction {
        ConversationEntity.find {
            (Conversations.trainerId eq userId) or (Conversations.athleteId eq userId)
        }.orderBy(Conversations.lastActivityAt to SortOrder.DESC)
            .toList()
    }

    fun getConversationsWithUserData(userId: Int) = transaction {
        getConversations(userId).map { it.toModel(userId) }
    }

    fun createConversation(trainerId: Int, athleteId: Int): ConversationEntity = transaction {
        val now = Clock.System.now()

        // Verificar si ya existe
        val existing = ConversationEntity.find {
            ((Conversations.trainerId eq trainerId) and (Conversations.athleteId eq athleteId)) or
                    ((Conversations.trainerId eq athleteId) and (Conversations.athleteId eq trainerId))
        }.firstOrNull()

        if (existing != null) {
            return@transaction existing
        }

        // Crear nueva conversación
        val conversation = ConversationEntity.new {
            trainer = UserEntity[trainerId]
            athlete = UserEntity[athleteId]
            createdAt = now
            lastActivityAt = now
        }

        // Crear registros en ConversationUsers para ambos usuarios
        ConversationUserEntity.new {
            this.conversation = conversation
            user = UserEntity[trainerId]
            joinedAt = now
        }

        ConversationUserEntity.new {
            this.conversation = conversation
            user = UserEntity[athleteId]
            joinedAt = now
        }

        conversation
    }

    // Mensajes
    fun createMessage(
        messageId: String = UUID.randomUUID().toString(),
        conversationId: Int,
        senderId: Int,
        receiverId: Int,
        content: String,
        messageType: MessageType = MessageType.TEXT,
        metadata: String? = null
    ): MessageEntity = transaction {
        val now = Clock.System.now()

        val message = MessageEntity.new {
            this.messageId = messageId
            this.conversation = ConversationEntity[conversationId]
            this.sender = UserEntity[senderId]
            this.receiver = UserEntity[receiverId]
            this.senderName = UserEntity[senderId].fullname
            this.content = content
            this.messageType = messageType
            this.sentAt = now
            this.status = MessageStatus.SENT
            this.metadata = metadata
        }

        // Actualizar última actividad de la conversación
        val conversation = ConversationEntity[conversationId]
        conversation.updateLastActivity()

        // Incrementar contador de no leídos para el receptor
        val receiverConversationUser = ConversationUserEntity.find {
            (ConversationUsers.conversationId eq conversationId) and
                    (ConversationUsers.userId eq receiverId)
        }.firstOrNull()

        receiverConversationUser?.incrementUnreadCount()

        message
    }

    fun getMessage(messageId: String): MessageEntity = transaction {
        MessageEntity.find { Messages.messageId eq messageId }.first()
    }

    fun getMessagesByConversation(conversationId: Int, limit: Int = 50, offset: Int = 0): List<MessageEntity> =
        transaction {
            MessageEntity.find { Messages.conversationId eq conversationId }
                .orderBy(Messages.sentAt to SortOrder.DESC)
                .limit(limit).offset(offset.toLong())
                .reversed() // Para obtener orden cronológico
                .toList()
        }

    fun updateMessageStatus(
        messageId: String,
        status: MessageStatus,
        timestamp: Instant = Clock.System.now()
    ): Boolean = transaction {
        try {
            val message = MessageEntity.find { Messages.messageId eq messageId }.firstOrNull()
                ?: return@transaction false

            when (status) {
                MessageStatus.DELIVERED -> {
                    if (message.status == MessageStatus.SENT) {
                        message.status = status
                        message.deliveredAt = timestamp
                    }
                }

                MessageStatus.READ -> {
                    message.markAsRead()

                    // Marcar como leído en ConversationUsers
                    val receiverConversationUser = ConversationUserEntity.find {
                        (ConversationUsers.conversationId eq message.conversation.id) and
                                (ConversationUsers.userId eq message.receiver.id)
                    }.firstOrNull()

                    receiverConversationUser?.markAllAsRead(messageId)
                }

                else -> {
                    message.status = status
                }
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    fun markMessageAsFailed(messageId: String): Boolean = transaction {
        try {
            val message = MessageEntity.find { Messages.messageId eq messageId }.firstOrNull()
                ?: return@transaction false
            message.status = MessageStatus.FAILED
            true
        } catch (e: Exception) {
            false
        }
    }

    // Estados de usuario
    fun updateUserOnlineStatus(userId: Int, isOnline: Boolean): UserStatusEntity = transaction {
        val userStatus = UserStatusEntity.find { UserStatus.userId eq userId }.firstOrNull()
            ?: UserStatusEntity.new {
                user = UserEntity[userId]
                state = StatusState.ACTIVE // Asumiendo que existe este enum
                lastTimeActive = Clock.System.now()
                this.isOnline = false
                lastSeenAt = Clock.System.now()
            }

        if (isOnline) {
            userStatus.setOnline()
        } else {
            userStatus.setOffline()
        }

        userStatus
    }

    fun getUserOnlineStatus(userId: Int): Boolean = transaction {
        UserStatusEntity.find { UserStatus.userId eq userId }
            .firstOrNull()?.isOnline ?: false
    }

    fun getActiveUsers(conversationId: Int): List<UserEntity> = transaction {
        val conversation = ConversationEntity[conversationId]
        val userIds = listOf(conversation.trainer.id.value, conversation.athlete.id.value)

        UserStatusEntity.find {
            (UserStatus.userId inList userIds) and (UserStatus.isOnline eq true)
        }.map { it.user }.toList()
    }

    // Sesiones activas
    fun createActiveSession(
        userId: Int,
        conversationId: Int,
        sessionId: String,
        userAgent: String? = null,
        ipAddress: String? = null
    ): ActiveSessionEntity = transaction {
        ActiveSessionEntity.new {
            user = UserEntity[userId]
            conversation = ConversationEntity[conversationId]
            this.sessionId = sessionId
            connectedAt = Clock.System.now()
            lastPingAt = Clock.System.now()
            this.userAgent = userAgent
            this.ipAddress = ipAddress
        }
    }

    fun removeActiveSession(userId: Int, conversationId: Int, sessionId: String): Boolean = transaction {
        try {
            ActiveSessionEntity.find {
                (ActiveSessions.userId eq userId) and
                        (ActiveSessions.conversationId eq conversationId) and
                        (ActiveSessions.sessionId eq sessionId)
            }.forEach { it.delete() }
            true
        } catch (e: Exception) {
            false
        }
    }

    fun updateSessionPing(userId: Int, conversationId: Int, sessionId: String): Boolean = transaction {
        try {
            val session = ActiveSessionEntity.find {
                (ActiveSessions.userId eq userId) and
                        (ActiveSessions.conversationId eq conversationId) and
                        (ActiveSessions.sessionId eq sessionId)
            }.firstOrNull()

            session?.updatePing()
            session != null
        } catch (e: Exception) {
            false
        }
    }

    fun getActiveSessions(conversationId: Int): List<ActiveSessionEntity> = transaction {
        ActiveSessionEntity.find { ActiveSessions.conversationId eq conversationId }.toList()
    }

    fun cleanupStaleConnections(timeoutMinutes: Int = 5): Int = transaction {
        val cutoffTime = Clock.System.now().minus(kotlin.time.Duration.parse("${timeoutMinutes}m"))

        val staleSessions = ActiveSessionEntity.find {
            ActiveSessions.lastPingAt less cutoffTime
        }

        val count = staleSessions.count().toInt()
        staleSessions.forEach { it.delete() }
        count
    }

    // Utilidades
    fun getUnreadCount(userId: Int, conversationId: Int): Int = transaction {
        ConversationUserEntity.find {
            (ConversationUsers.userId eq userId) and
                    (ConversationUsers.conversationId eq conversationId)
        }.firstOrNull()?.unreadCount ?: 0
    }

    fun getTotalUnreadCount(userId: Int): Int = transaction {
        ConversationUserEntity.find { ConversationUsers.userId eq userId }
            .sumOf { it.unreadCount }
    }

    fun searchMessages(
        conversationId: Int,
        query: String,
        limit: Int = 20
    ): List<MessageEntity> = transaction {
        MessageEntity.find {
            (Messages.conversationId eq conversationId) and
                    (Messages.content.lowerCase() like "%${query.lowercase()}%") and
                    (Messages.isDeleted eq false)
        }.orderBy(Messages.sentAt to SortOrder.DESC)
            .limit(limit)
            .toList()
    }
}