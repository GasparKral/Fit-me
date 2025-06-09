package es.gaspardev.utils

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

val DESKTOP_DIR = File(System.getProperty("user.home"), "Desktop")

fun saveQrToDesktop(bufferedImage: BufferedImage, fileName: String) {

    // Crear el archivo destino
    val outputFile = File(DESKTOP_DIR, fileName)

    // Guardar la imagen
    ImageIO.write(bufferedImage, "png", outputFile)
}

fun saveTextFile(content: String, fileName: String) {
    File(DESKTOP_DIR, fileName).writeText(content)
}