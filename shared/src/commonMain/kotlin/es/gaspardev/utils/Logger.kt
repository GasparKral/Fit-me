package es.gaspardev.utils

import es.gaspardev.interfaces.Printter

expect object Logger {

    var printter: Printter

    fun log(message: String)

    fun warn(message: String)

    fun error(message: String)

}
