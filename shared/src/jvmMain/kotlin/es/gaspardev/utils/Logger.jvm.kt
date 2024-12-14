package es.gaspardev.utils

import es.gaspardev.core.debug.BasicPrintter
import es.gaspardev.interfaces.Printter

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual object Logger {

    actual var printter: Printter = BasicPrintter

    actual fun log(message: String) {
        printter.log(message)
    }

    actual fun warn(message: String) {
        printter.warn(message)
    }

    actual fun error(message: String) {
        printter.error(message)
    }


}