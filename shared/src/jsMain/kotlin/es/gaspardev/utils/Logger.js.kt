package es.gaspardev.utils

actual object Logger {
    actual var printter: es.gaspardev.interfaces.Printter
        get() = TODO("Not yet implemented")
        set(value) {}

  
    actual fun log(message: String) {
        console.log(message)
    }

    actual fun warn(message: String) {
        console.warn(message)
    }

    actual fun error(message: String) {
        console.error(message)
    }

}

