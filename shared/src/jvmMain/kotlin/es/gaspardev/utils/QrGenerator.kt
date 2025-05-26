package es.gaspardev.utils

import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import es.gaspardev.core.domain.dtos.QrData
import kotlinx.serialization.json.Json
import java.awt.Color
import java.awt.image.BufferedImage

object QrGenerator {
    fun generateQrBitmap(data: QrData, size: Int): BufferedImage {
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
}