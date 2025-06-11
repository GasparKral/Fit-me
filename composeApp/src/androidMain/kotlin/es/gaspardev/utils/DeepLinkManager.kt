package es.gaspardev.utils

import android.content.Intent
import android.net.Uri
import android.util.Log

class DeepLinkManager {

    companion object {
        const val SCHEME = "fitme"
        const val HOST_CONNECT = "connect"
        const val PARAM_TOKEN = "token"
        const val PARAM_TYPE = "type"
        const val TYPE_REGISTER = "regist"
        const val TYPE_LOGIN = "login"

        private const val TAG = "DeepLinkManager"
    }

    sealed class DeepLinkAction {
        object REGISTER : DeepLinkAction()
        object LOGIN : DeepLinkAction()
        object UNKNOWN : DeepLinkAction()
    }

    data class DeepLinkData(
        val action: DeepLinkAction,
        val token: String? = null,
        val additionalParams: Map<String, String> = emptyMap()
    )

    /**
     * Procesa un intent que puede contener un deep link
     * Formato esperado: fitme://connect?token=...&type=regist
     */
    fun processDeepLink(intent: Intent): DeepLinkData? {
        Log.d(TAG, "Processing intent: ${intent.action}, data: ${intent.data}")

        val data = intent.data ?: return null

        return parseDeepLinkUri(data)
    }

    /**
     * Parsea una URI de deep link
     */
    fun parseDeepLinkUri(uri: Uri): DeepLinkData? {
        Log.d(TAG, "Parsing URI: $uri")

        // Verificar que sea nuestro esquema
        if (uri.scheme != SCHEME) {
            Log.w(TAG, "Invalid scheme: ${uri.scheme}, expected: $SCHEME")
            return null
        }

        // Verificar el host
        if (uri.host != HOST_CONNECT) {
            Log.w(TAG, "Invalid host: ${uri.host}, expected: $HOST_CONNECT")
            return null
        }

        // Extraer parámetros
        val token = uri.getQueryParameter(PARAM_TOKEN)
        val type = uri.getQueryParameter(PARAM_TYPE)

        Log.d(TAG, "Extracted - token: $token, type: $type")

        if (token.isNullOrBlank()) {
            Log.w(TAG, "Token is required but not found")
            return null
        }

        // Determinar la acción basada en el tipo
        val action = when (type) {
            TYPE_REGISTER -> DeepLinkAction.REGISTER
            TYPE_LOGIN -> DeepLinkAction.LOGIN
            else -> {
                Log.w(TAG, "Unknown type: $type")
                DeepLinkAction.UNKNOWN
            }
        }

        // Extraer parámetros adicionales
        val additionalParams = mutableMapOf<String, String>()
        uri.queryParameterNames.forEach { paramName ->
            if (paramName != PARAM_TOKEN && paramName != PARAM_TYPE) {
                uri.getQueryParameter(paramName)?.let { value ->
                    additionalParams[paramName] = value
                }
            }
        }

        val deepLinkData = DeepLinkData(
            action = action,
            token = token,
            additionalParams = additionalParams
        )

        Log.d(TAG, "Successfully parsed deep link: $deepLinkData")
        return deepLinkData
    }

    /**
     * Valida si un token tiene el formato correcto
     */
    fun isValidToken(token: String?): Boolean {
        if (token.isNullOrBlank()) return false

        // Agregar validaciones específicas del token aquí
        // Por ejemplo, longitud mínima, formato, etc.
        return token.length >= 10 // Ejemplo: mínimo 10 caracteres
    }

    /**
     * Genera una URI de deep link para testing
     */
    fun generateDeepLinkUri(token: String, type: String = TYPE_REGISTER): Uri {
        return Uri.Builder()
            .scheme(SCHEME)
            .authority(HOST_CONNECT)
            .appendQueryParameter(PARAM_TOKEN, token)
            .appendQueryParameter(PARAM_TYPE, type)
            .build()
    }

    /**
     * Valida si una URI es un deep link válido de FitMe
     */
    fun isValidFitMeDeepLink(uri: Uri): Boolean {
        return uri.scheme == SCHEME && uri.host == HOST_CONNECT
    }
}