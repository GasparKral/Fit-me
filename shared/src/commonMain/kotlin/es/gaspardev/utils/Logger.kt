package es.gaspardev.utils

import es.gaspardev.interfaces.Printter
import es.gaspardev.core.debug.BasicPrinter

object Logger {

    private var printter: Printter = BasicPrinter

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
