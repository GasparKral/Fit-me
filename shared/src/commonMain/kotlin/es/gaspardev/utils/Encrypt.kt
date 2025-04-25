package es.gaspardev.utils

import java.security.MessageDigest
import java.util.Base64

fun encrypt(text: String): String {
    val bytes = MessageDigest.getInstance("SHA-256").digest(text.toByteArray())
    return bytes.joinToString("") { "%02x".format(it) }
}


@Suppress("NewApi")
fun code64(text: String): String {
    val bytes = text.toByteArray(Charsets.UTF_8)
    return Base64.getEncoder().encodeToString(bytes)
}

@Suppress("NewApi")
fun decode64(text: String): String {
    val bytes = Base64.getDecoder().decode(text)
    return String(bytes, Charsets.UTF_8)
}