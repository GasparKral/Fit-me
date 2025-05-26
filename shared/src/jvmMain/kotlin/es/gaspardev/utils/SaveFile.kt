package es.gaspardev.utils

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

fun saveQrToDesktop(bufferedImage: BufferedImage, fileName: String) {
    // Obtener el directorio del escritorio del usuario
    val desktopDir = File(System.getProperty("user.home"), "Desktop")

    // Crear el directorio si no existe
    if (!desktopDir.exists()) {
        desktopDir.mkdirs()
    }

    // Crear el archivo destino
    val outputFile = File(desktopDir, fileName)

    // Guardar la imagen
    ImageIO.write(bufferedImage, "png", outputFile)
}