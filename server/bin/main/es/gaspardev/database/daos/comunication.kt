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

object CommunicationDao {

    // Conversaciones
    fun getChat(conversationId: Int): ConversationEntity = transaction {
        ConversationEntity[conversationId]
    }

    fun getChatWith(userId: Int, conversationId: Int): Conversation = transaction {
        ConversationEntity[conversationId].toModel(userId)
    }

    fun getConversations(userId: Int): List<Conversation> = transaction {
        ConversationEntity.find {
            (Conversations.trainerId eq userId) or (Conversations.athleteId eq userId)
        }.orderBy(Conversations.lastActivityAt to SortOrder.DESC).map { it.toModel() }
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

    fun createMessage(
        messageId: String,
        conversationId: Int,
        senderId: Int,
        receiverId: Int,
        content: String,
        messageType: MessageType = MessageType.TEXT,
        metadata: String? = null
    ): MessageEntity {
        return try {
            val now = Clock.System.now()

            println("Creating message: messageId=$messageId, conversationId=$conversationId, senderId=$senderId")

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

            // **FIX: Forzar flush para que se genere el ID**
            message.flush()

            // Actualizar última actividad de la conversación
            val conversation = ConversationEntity[conversationId]
            conversation.updateLastActivity()

            // Incrementar contador de no leídos para el receptor
            val receiverConversationUser = ConversationUserEntity.find {
                (ConversationUsers.conversationId eq conversationId) and
                        (ConversationUsers.userId eq receiverId)
            }.firstOrNull()

            receiverConversationUser?.incrementUnreadCount()

            println("Message created successfully: id=${message.id}, messageId=${message.messageId}")
            message

        } catch (e: Exception) {
            println("Error creating message: ${e.message}")
            e.printStackTrace()
            throw e
        }
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
    ): Boolean {
        return try {
            println("Updating message status: messageId=$messageId, status=$status")

            val message = MessageEntity.find { Messages.messageId eq messageId }.firstOrNull()
            if (message == null) {
                println("Message not found: messageId=$messageId")
                return false
            }

            when (status) {
                MessageStatus.DELIVERED -> {
                    if (message.status == MessageStatus.SENT) {
                        message.status = status
                        message.deliveredAt = timestamp
                        println("Message marked as delivered: messageId=$messageId")
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
                    println("Message marked as read: messageId=$messageId")
                }

                else -> {
                    message.status = status
                    println("Message status updated: messageId=$messageId, status=$status")
                }
            }

            // **FIX: Forzar flush para persistir cambios**
            message.flush()
            true

        } catch (e: Exception) {
            println("Error updating message status: ${e.message}")
            e.printStackTrace()
            false
        }
    }

    fun markMessageAsFailed(messageId: String): Boolean = transaction {
        try {
            val message = MessageEntity.find { Messages.messageId eq messageId }.firstOrNull()
                ?: return@transaction false
            message.status = MessageStatus.FAILED
            message.flush() // **FIX: Forzar persistencia**
            true
        } catch (e: Exception) {
            println("Error marking message as failed: ${e.message}")
            false
        }
    }

    // Estados de usuario - **MÉTODO CORREGIDO**
    fun updateUserOnlineStatus(userId: Int, isOnline: Boolean): UserStatusEntity {
        return try {
            val userStatus = UserStatusEntity.find { UserStatus.userId eq userId }.firstOrNull()
                ?: UserStatusEntity.new {
                    user = UserEntity[userId]
                    state = StatusState.ACTIVE
                    lastTimeActive = Clock.System.now()
                    this.isOnline = false
                    lastSeenAt = Clock.System.now()
                }

            if (isOnline) {
                userStatus.setOnline()
            } else {
                userStatus.setOffline()
            }

            // **FIX: Forzar persistencia**
            userStatus.flush()
            userStatus

        } catch (e: Exception) {
            println("Error updating user online status: ${e.message}")
            throw e
        }
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

    // Sesiones activas - **MÉTODOS CORREGIDOS**
    fun createActiveSession(
        userId: Int,
        conversationId: Int,
        sessionId: String,
        userAgent: String? = null,
        ipAddress: String? = null
    ): ActiveSessionEntity = transaction {
        try {
            val session = ActiveSessionEntity.new {
                user = UserEntity[userId]
                conversation = ConversationEntity[conversationId]
                this.sessionId = sessionId
                connectedAt = Clock.System.now()
                lastPingAt = Clock.System.now()
                this.userAgent = userAgent
                this.ipAddress = ipAddress
            }
            session.flush() // **FIX: Forzar persistencia**
            session
        } catch (e: Exception) {
            println("Error creating active session: ${e.message}")
            throw e
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
            println("Error removing active session: ${e.message}")
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
            session?.flush() // **FIX: Forzar persistencia**
            session != null
        } catch (e: Exception) {
            println("Error updating session ping: ${e.message}")
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