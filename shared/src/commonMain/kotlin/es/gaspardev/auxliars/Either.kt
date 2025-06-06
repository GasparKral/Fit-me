package es.gaspardev.auxliars

sealed class Either<out E, out T> where E : Any, T : Any {
    // Representa un caso de error
    data class Failure<out E : Any>(val error: E) : Either<E, Nothing>()

    // Representa un caso de éxito
    data class Success<out T : Any>(val value: T) : Either<Nothing, T>()

    // Ejecuta una función dependiendo de si es un éxito o un error
    inline fun fold(onSuccess: (T) -> Unit, onFailure: (E) -> Unit = { _ -> }) {
        when (this) {
            is Failure -> onFailure(error)
            is Success -> onSuccess(value)
        }
    }

    // Fold que retorna un valor
    inline fun <R> foldValue(onSuccess: (T) -> R, onFailure: (E) -> R): R {
        return when (this) {
            is Failure -> onFailure(error)
            is Success -> onSuccess(value)
        }
    }

    // Propiedades para verificar el estado
    val isFailure: Boolean get() = this is Failure<E>
    val isSuccess: Boolean get() = this is Success<T>

    // Obtener el valor de éxito o null
    fun getOrNull(): T? {
        return when (this) {
            is Success -> value
            is Failure -> null
        }
    }

    // Obtener el error o null
    fun errorOrNull(): E? {
        return when (this) {
            is Failure -> error
            is Success -> null
        }
    }

    // Obtener el valor o un valor por defecto
    fun getOrElse(defaultValue: @UnsafeVariance T): T {
        return when (this) {
            is Success -> value
            is Failure -> defaultValue
        }
    }

    // Obtener el valor o lanzar la excepción (si E es Exception)
    fun getOrThrow(): T {
        return when (this) {
            is Success -> value
            is Failure -> {
                if (error is Exception) {
                    throw error
                } else {
                    throw RuntimeException("Either.Failure: $error")
                }
            }
        }
    }

    // Map para transformar el valor de éxito
    inline fun <R : Any> map(transform: (T) -> R): Either<E, R> {
        return when (this) {
            is Success -> Success(transform(value))
            is Failure -> this
        }
    }

    // MapLeft para transformar el error
    inline fun <R : Any> mapLeft(transform: (E) -> R): Either<R, T> {
        return when (this) {
            is Success -> this
            is Failure -> Failure(transform(error))
        }
    }

    // FlatMap para encadenar operaciones que retornan Either
    inline fun <R : Any> flatMap(transform: (T) -> Either<@UnsafeVariance E, R>): Either<E, R> {
        return when (this) {
            is Success -> transform(value)
            is Failure -> this
        }
    }
}