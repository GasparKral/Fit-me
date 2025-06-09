import es.gaspardev.auxliars.Either
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import java.util.concurrent.TimeoutException

/**
 * CLASE ABSTRACTA PARA API's
 * Contiene toda la lógica común para las operaciones HTTP y manejo de errores
 */
abstract class API<ApiType> where ApiType : Any {

    protected abstract val apiPath: String
    protected abstract val urlPath: String

    protected val httpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
    }

    operator fun invoke() {
        this.httpClient.close()
    }

    // ==================== MÉTODOS PROTEGIDOS COMUNES ====================

    /**
     * Realiza una petición HTTP genérica y maneja la respuesta
     */
    suspend inline fun <reified T : Any> performRequest(
        method: HttpMethod,
        segments: List<String> = listOf(),
        body: Any? = null,
        vararg params: Pair<String, String>
    ): Either<Exception, T> {
        return try {
            val response = when (method) {
                HttpMethod.Get -> `access$httpClient`.get(`access$apiPath`) {
                    `access$configureUrl`(segments, *params)
                }

                HttpMethod.Post -> `access$httpClient`.post(`access$buildUrl`(segments)) {
                    `access$configureJsonBody`(body)
                }

                HttpMethod.Patch -> `access$httpClient`.patch(`access$apiPath`) {
                    `access$configureUrl`(segments, *params)
                    `access$configureJsonBody`(body)
                }

                HttpMethod.Delete -> `access$httpClient`.delete(`access$apiPath`) {
                    `access$configureUrl`(segments, *params)
                }

                else -> throw IllegalArgumentException("Método HTTP no soportado: $method")
            }

            handleResponse(response)
        } catch (e: Exception) {
            Either.Failure(e)
        }
    }

    /**
     * Realiza una petición DELETE y retorna el resultado del manejo de errores
     */
    protected suspend fun performDelete(
        segments: List<String> = listOf(),
        vararg params: Pair<String, String>
    ): Either<Exception, Unit> {
        return try {
            val response = httpClient.delete(apiPath) {
                configureUrl(segments, *params)
            }

            when (response.status) {
                HttpStatusCode.OK -> Either.Success(Unit) // Éxito
                HttpStatusCode.NotFound -> Either.Failure(NoSuchElementException("No se encontró el entrenamiento"))
                HttpStatusCode.RequestTimeout -> Either.Failure(TimeoutException("Fallo en la conexión"))
                HttpStatusCode.ExpectationFailed -> Either.Failure(Exception("Existen usuarios que estan usando este entrenamiento"))
                else -> Either.Failure(Exception("Unexpected status code: ${response.status}"))
            }
        } catch (e: Exception) {
            Either.Failure(e)
        }

    }


    /**
     * Realiza una petición PATCH y retorna el resultado del manejo de errores
     */
    protected suspend fun performPatch(
        segments: List<String> = listOf(),
        body: Any
    ): Either<Exception, Unit> {
        return try {
            httpClient.patch(apiPath) {
                configureUrl(segments)
                configureJsonBody(body)
            }
            Either.Success(Unit)
        } catch (e: Exception) {
            Either.Failure(e)
        }
    }

    // ==================== MÉTODOS DE UTILIDAD PROTEGIDOS ====================

    /**
     * Configura la URL con segmentos y parámetros
     */
    protected fun HttpRequestBuilder.configureUrl(
        segments: List<String> = listOf(),
        vararg params: Pair<String, String>
    ) {
        url {
            pathSegments = urlPath.split("/").filter { it.isNotEmpty() } + segments
            params.forEach { parameters.append(it.first, it.second) }
        }
    }

    /**
     * Configura el cuerpo JSON de la petición
     */
    protected fun HttpRequestBuilder.configureJsonBody(body: Any?) {
        body?.let {
            contentType(ContentType.Application.Json)
            setBody(it)
        }
    }

    /**
     * Construye una URL completa con segmentos
     */
    protected fun buildUrl(segments: List<String> = listOf()): String {
        return if (segments.isNotEmpty()) {
            "$apiPath/${segments.joinToString("/")}"
        } else {
            apiPath
        }
    }

    /**
     * Maneja la respuesta HTTP común para todas las APIs
     */
    suspend inline fun <reified T : Any> handleResponse(
        response: io.ktor.client.statement.HttpResponse
    ): Either<Exception, T> {
        return when (response.status) {
            HttpStatusCode.OK -> Either.Success(response.body())
            HttpStatusCode.RequestTimeout -> Either.Failure(TimeoutException("Fallo en la conexión"))
            else -> Either.Failure(Exception("Unexpected status code: ${response.status}"))
        }
    }

    // ==================== MÉTODOS GENÉRICOS IMPLEMENTADOS ====================

    /**
     * Obtiene una lista genérica de elementos
     */
    suspend inline fun <reified T : Any> getGenericList(
        segments: List<String> = listOf(),
        vararg params: Pair<String, String>
    ): Either<Exception, List<T>> {
        return performRequest<List<T>>(HttpMethod.Get, segments, null, *params)
    }

    /**
     * Obtiene un valor individual genérico
     */
    suspend inline fun <reified T : Any> getSingleValue(
        segments: List<String> = listOf(),
        vararg params: Pair<String, String>
    ): Either<Exception, T> {
        return performRequest<T>(HttpMethod.Get, segments, null, *params)
    }

    /**
     * Crea un elemento genérico
     */
    suspend inline fun <reified T : Any, reified R : Any> postGeneric(
        segments: List<String> = listOf(),
        body: T
    ): Either<Exception, R> {
        return performRequest<R>(HttpMethod.Post, segments, body)
    }

    // ==================== MÉTODOS ABSTRACTOS ESPECÍFICOS ====================

    /**
     * Crea un elemento del tipo específico de la API
     */
    abstract suspend fun <T : Any> post(
        segments: List<String> = listOf(),
        body: T
    ): Either<Exception, ApiType>

    /**
     * Obtiene un elemento específico
     */
    abstract suspend fun get(
        segments: List<String> = listOf(),
        vararg params: Pair<String, String>
    ): Either<Exception, ApiType>

    /**
     * Obtiene una lista de elementos específicos
     */
    abstract suspend fun getList(
        segments: List<String> = listOf(),
        vararg params: Pair<String, String>
    ): Either<Exception, List<ApiType>>

    /**
     * Elimina un elemento
     */
    abstract suspend fun delete(
        segments: List<String> = listOf(),
        vararg params: Pair<String, String>
    ): Either<Exception, Unit>

    /**
     * Actualiza parcialmente un elemento
     */
    abstract suspend fun patch(
        segments: List<String> = listOf(),
        body: Any
    ): Either<Exception, Unit>

    @PublishedApi
    internal val `access$httpClient`: HttpClient
        get() = httpClient

    @PublishedApi
    internal val `access$apiPath`: String
        get() = apiPath

    @PublishedApi
    internal fun HttpRequestBuilder.`access$configureUrl`(segments: List<String>, vararg params: Pair<String, String>) =
        configureUrl(segments, *params)

    @PublishedApi
    internal fun `access$buildUrl`(segments: List<String>) = buildUrl(segments)

    @PublishedApi
    internal fun HttpRequestBuilder.`access$configureJsonBody`(body: Any?) = configureJsonBody(body)


}