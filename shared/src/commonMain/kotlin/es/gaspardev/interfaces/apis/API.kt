import es.gaspardev.auxliars.Either
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

/**
 * CLASE ABSTRACTA PARA API's
 * */
abstract class API<ApiType> where ApiType : Any {

    protected abstract val apiPath: String

    protected val httpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json { prettyPrint = true; isLenient = true; ignoreUnknownKeys = true; });
        }
    }

    protected fun buildUrlWithParams(route: String, params: Array<out String?>): String {
        val queryString = params.filterNotNull().joinToString("&") { it }
        return if (queryString.isNotEmpty()) {
            if (route.contains("?")) "$apiPath&$queryString" else "$apiPath?$queryString"
        } else {
            route
        }
    }


    operator fun invoke() {
        this.httpClient.close()
    }

    abstract suspend fun post(
        segments: List<String> = listOf(),
        body: Any
    ): Either<Exception, ApiType>

    abstract suspend fun get(
        segments: List<String> = listOf(),
        vararg params: Pair<String, String>
    ): Either<Exception, ApiType>

    abstract suspend fun getList(
        segments: List<String> = listOf(),
        vararg params: Pair<String, String>
    ): Either<Exception, List<ApiType>>

    abstract suspend fun delete(
        segments: List<String> = listOf(),
        vararg params: Pair<String, String>
    ): Either.Failure<Exception>?

    abstract suspend fun patch(
        segments: List<String> = listOf(),
        body: Any
    ): Either.Failure<Exception>?
}
