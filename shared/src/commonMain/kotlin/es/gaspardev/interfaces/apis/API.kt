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
            json(Json { prettyPrint = true; isLenient = true; ignoreUnknownKeys = true })
        }
    }

    operator fun invoke() {
        this.httpClient.close()
    }

    abstract suspend fun post(
        route: String = apiPath,
        body: ApiType
    ): Either<Exception, ApiType>

    abstract suspend fun get(route: String = apiPath, vararg params: String?): Either<Exception, List<ApiType>>

    abstract suspend fun delete(route: String = apiPath, vararg params: String?): Either.Failure<Exception>?

    abstract suspend fun patch(route: String = apiPath, body: Any): Either.Failure<Exception>?
}
