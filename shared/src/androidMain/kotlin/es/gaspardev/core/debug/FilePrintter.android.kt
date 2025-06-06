package es.gaspardev.core.debug

import android.content.Context
import es.gaspardev.interfaces.Printter
import es.gaspardev.utils.Logger
import kotlinx.datetime.Clock
import java.io.File
import java.io.FileWriter
import java.io.IOException

actual object FilePrinter : Printter {

    private lateinit var logFile: File

    fun init(context: Context) {
        logFile = File(context.cacheDir, "Log.txt")
    }

    private fun writeLog(level: String, message: String) {
        try {
            FileWriter(logFile, true).use { writer ->
                writer.appendLine("$level(${Clock.System.now()}): $message")
            }
        } catch (io: IOException) {
            Logger.printter = BasicPrintter
            io.message?.let { Logger.error(it) }
        }
    }

    override fun log(message: String) {
        writeLog("Log", message)
    }

    override fun warn(message: String) {
        writeLog("Warn", message)
    }

    override fun error(message: String) {
        writeLog("Error", message)
    }
}
