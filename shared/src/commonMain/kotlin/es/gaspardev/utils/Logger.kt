package es.gaspardev.utils

import es.gaspardev.interfaces.Printter
import es.gaspardev.core.debug.BasicPrintter


expect object Logger {

    var printter: Printter

    fun log(message: String)

    fun warn(message: String)

    fun error(message: String)

}
