package es.gaspardev.utils

import es.gaspardev.interfaces.Printter
import es.gaspardev.core.debug.BasicPrintter

object Logger {

    private var printter: Printter = BasicPrintter

    fun setPrintter(newPrintter: Printter) {
        this.printter = newPrintter
    }

    fun log(message: String) {
        printter.log(message)
    }

    fun warm(message: String) {
        printter.warm(message)
    }

    fun error(message: String) {
        printter.error(message)
    }

}
