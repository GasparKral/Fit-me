package es.gaspardev.utils

import kotlin.time.Duration

fun Duration.toWeeks(): String {
    val weeks = this.inWholeDays / 7
    return "$weeks %s"
}