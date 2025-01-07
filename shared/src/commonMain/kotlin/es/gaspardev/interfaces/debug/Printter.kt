package es.gaspardev.interfaces

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

interface Printter {

    private fun getCurrentTime(): Instant {
        return Clock.System.now()
    }

    fun log(message: String) {
        println("\u001b[0mLOG(${getCurrentTime()}): $message\u001b[0m")
    }

    fun warn(message: String) {
        println("\u001b[0;33mWARN(${getCurrentTime()}): $message\u001b[0m")
    }

    fun error(message: String) {
        println("\u001b[31mERROR(${getCurrentTime()}): $message\u001b[0m")
    }


}