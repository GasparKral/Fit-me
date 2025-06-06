package es.gaspardev.enums

import kotlinx.serialization.Serializable

@Serializable
enum class MessageStatus {
    ALL,
    SENDING,    // Mensaje enviándose
    SENT,       // Mensaje enviado al servidor
    DELIVERED,  // Mensaje entregado al destinatario
    READ,       // Mensaje leído por el destinatario
    FAILED      // Error al enviar
}