package es.gaspardev.database.daos

import es.gaspardev.database.entities.ConversationEntity
import es.gaspardev.database.entities.MessageEntity
import es.gaspardev.enums.MessageStatus
import org.jetbrains.exposed.sql.transactions.transaction

class ComunicationDao {
    fun getChat(conversationId: Int): ConversationEntity = transaction {
        ConversationEntity[conversationId]
    }

    fun createMessage(block: MessageEntity.() -> Unit): MessageEntity = transaction {
        MessageEntity.new(block)
    }

    fun updateMessageStatus(messageId: Int, status: MessageStatus) = transaction {
        MessageEntity[messageId].status = status
    }
}