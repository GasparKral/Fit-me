package es.gaspardev.auxliars

sealed class Either<out E, out T> where E : Exception, T : Any {

    // Representa un caso de error
    data class Failure<out E : Exception>(val error: E) : Either<E, Nothing>()

    // Representa un caso de éxito
    data class Success<out T : Any>(val value: T) : Either<Nothing, T>()

    // Ejecuta una función dependiendo de si es un éxito o un error
    inline fun fold(onSuccess: (T) -> Unit, onFailure: (E) -> Unit) {
        when (this) {
            is Failure -> onFailure(error)
            is Success -> onSuccess(value)
        }
    }

    
    // Propiedades para verificar el estado
    val isFailure: Boolean get() = this is Failure<E>
    val isSuccess: Boolean get() = this is Success<T>
}
