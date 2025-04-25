package es.gaspardev.core.infrastructure.memo

import es.gaspardev.utils.APLICATION_FOLDER_DIR
import java.io.File

object ContentManager {

    private val contentFolder: File = File(APLICATION_FOLDER_DIR)
    val cacheFolder: File = File(contentFolder, "cache")

    fun initialize() {
        if (!contentFolder.exists()) {
            contentFolder.mkdirs()
        }

        if (!cacheFolder.exists()) {
            cacheFolder.mkdirs()
        }
    }

    fun existCache(): Boolean {
        return cacheFolder.exists() && cacheFolder.listFiles()?.isNotEmpty() == true
    }
}
