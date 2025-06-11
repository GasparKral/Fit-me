package es.gaspardev.core.infrastructure.apis

import API
import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.entities.settings.UserSettings
import es.gaspardev.utils.SERVER_ADRESS
import io.ktor.http.*

class SettingsAPI(
    override val apiPath: String = "$SERVER_ADRESS/settings",
    override val urlPath: String = "/settings"
) : API<UserSettings>() {

    override suspend fun <T : Any> post(segments: List<String>, body: T): Either<Exception, UserSettings> {
        return performRequest<UserSettings>(HttpMethod.Post, segments, body)
    }

    override suspend fun get(
        segments: List<String>,
        vararg params: Pair<String, String>
    ): Either<Exception, UserSettings> {
        return performRequest<UserSettings>(HttpMethod.Get, segments, null, *params)
    }

    override suspend fun getList(
        segments: List<String>,
        vararg params: Pair<String, String>
    ): Either<Exception, List<UserSettings>> {
        return performRequest<List<UserSettings>>(HttpMethod.Get, segments, null, *params)
    }

    override suspend fun delete(
        segments: List<String>,
        vararg params: Pair<String, String>
    ): Either<Exception, Unit> {
        return performDelete(segments, *params)
    }

    override suspend fun patch(segments: List<String>, body: Any): Either<Exception, Unit> {
        return performPatch(segments, body)
    }

    suspend fun updateSettings(
        userId: Int,
        settingsType: String,
        settingsData: Any
    ): Either<Exception, UserSettings> {
        return performRequest<UserSettings>(
            HttpMethod.Patch,
            listOf(userId.toString(), settingsType),
            settingsData
        )
    }

    suspend fun changePassword(
        userId: Int,
        passwordData: Any
    ): Either<Exception, Unit> {
        return performRequest<Unit>(
            HttpMethod.Patch,
            listOf(userId.toString(), "password"),
            passwordData
        )
    }
}
