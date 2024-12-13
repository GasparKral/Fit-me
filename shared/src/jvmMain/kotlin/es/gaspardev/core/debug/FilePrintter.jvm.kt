package es.gaspardev.core.debug

import es.gaspardev.interfaces.Printter
import es.gaspardev.utils.Logger
import kotlinx.datetime.Clock
import java.io.File
import java.io.FileWriter
import java.io.IOException

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual object FilePrintter : Printter {

    private var outputFile: File = File(System.getProperty("user.home") + "/Desktop/Log.txt")

    fun setSource(path: String) {
        outputFile = File(path)
    }

    private fun writeLog(level: String, message: String) {
        try {
            FileWriter(outputFile, true).use { writer ->
                writer.appendLine("$level(${Clock.System.now()}): $message")
            }
        } catch (io: IOException) {
            Logger.setPrintter(BasicPrintter)
            io.message?.let { Logger.error(it) }
        }
    }

    override fun log(message: String) {
        writeLog("Log", message)
    }

    override fun warm(message: String) {
        writeLog("Warm", message)
    }

    override fun error(message: String) {
        writeLog("Error", message)
    }
}
