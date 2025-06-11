package es.gaspardev.utils

import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import es.gaspardev.core.domain.dtos.QrData
import kotlinx.serialization.json.Json
import java.awt.Color
import java.awt.image.BufferedImage

object QrGenerator {

    /**
     * Genera un QR code que abre directamente la aplicación FitMe
     * con el token de registro
     */
    fun generateRegistrationQrBitmap(token: String, size: Int): BufferedImage {
        // Crear la URL de deep link que abrirá la aplicación
        val deepLinkUrl = generateRegistrationDeepLink(token)

        val hints = hashMapOf<EncodeHintType, Int>().apply {
            put(EncodeHintType.MARGIN, 1)
        }

        val bits = QRCodeWriter().encode(deepLinkUrl, BarcodeFormat.QR_CODE, size, size, hints)
        return BufferedImage(size, size, BufferedImage.TYPE_INT_RGB).apply {
            for (x in 0 until size) {
                for (y in 0 until size) {
                    setRGB(x, y, if (bits.get(x, y)) Color.BLACK.rgb else Color.WHITE.rgb)
                }
            }
        }
    }

    /**
     * Genera un QR code genérico (método original para compatibilidad)
     */
    fun generateQrBitmap(data: QrData, size: Int): BufferedImage {
        // Para registro, usar el nuevo método
        if (data.action == "register") {
            return generateRegistrationQrBitmap(data.token, size)
        }

        // Para otros casos, usar el método original con JSON
        val json = Json.encodeToString(QrData.serializer(), data)
        val hints = hashMapOf<EncodeHintType, Int>().apply {
            put(EncodeHintType.MARGIN, 1)
        }

        val bits = QRCodeWriter().encode(json, BarcodeFormat.QR_CODE, size, size, hints)
        return BufferedImage(size, size, BufferedImage.TYPE_INT_RGB).apply {
            for (x in 0 until size) {
                for (y in 0 until size) {
                    setRGB(x, y, if (bits.get(x, y)) Color.BLACK.rgb else Color.WHITE.rgb)
                }
            }
        }
    }

    /**
     * Genera la URL de deep link para registro
     * Formato: fitme://connect?token=...&type=register
     */
    private fun generateRegistrationDeepLink(token: String): String {
        return "fitme://connect?token=${token}&type=register"
    }

    /**
     * Genera una URL alternativa HTTPS (para casos donde el deep link no funcione)
     */
    fun generateAlternativeRegistrationUrl(token: String): String {
        return "https://app.fitme.com/register?token=${token}"
    }

    /**
     * Valida si un token es válido antes de generar el QR
     */
    fun isValidRegistrationToken(token: String): Boolean {
        return token.isNotBlank() &&
                token.length >= 10 &&
                token.matches(Regex("[A-Za-z0-9]+")) // Solo letras y números
    }

    /**
     * Genera un QR con información de respaldo
     * Incluye tanto el deep link como información legible para humanos
     */
    fun generateQrWithFallback(token: String, trainerName: String, size: Int): BufferedImage {
        val deepLink = generateRegistrationDeepLink(token)

        // Si quieres incluir información adicional visible al escanear con una app genérica
        val qrContent = buildString {
            appendLine("FitMe - Registro de Atleta")
            appendLine("Entrenador: $trainerName")
            appendLine("Enlace: $deepLink")
            appendLine("Token: $token")
        }

        val hints = hashMapOf<EncodeHintType, Int>().apply {
            put(EncodeHintType.MARGIN, 1)
        }

        val bits = QRCodeWriter().encode(deepLink, BarcodeFormat.QR_CODE, size, size, hints)
        return BufferedImage(size, size, BufferedImage.TYPE_INT_RGB).apply {
            for (x in 0 until size) {
                for (y in 0 until size) {
                    setRGB(x, y, if (bits.get(x, y)) Color.BLACK.rgb else Color.WHITE.rgb)
                }
            }
        }
    }
}