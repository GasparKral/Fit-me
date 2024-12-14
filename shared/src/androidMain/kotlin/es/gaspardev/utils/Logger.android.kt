package es.gaspardev.utils

import android.util.Log
import es.gaspardev.core.debug.BasicPrintter
import es.gaspardev.core.debug.FilePrintter
import es.gaspardev.interfaces.Printter

actual object Logger {

    actual var printter: Printter = BasicPrintter;
    actual fun log(message: String) {
        if (printter is FilePrintter) {
            printter.log(message)
            return
        }
        Log.i("LogMessage", message)
    }

    actual fun warn(message: String) {
        if (printter is FilePrintter) {
            printter.warn(message)
            return
        }
        Log.w("WarnMessage", message)
    }

    actual fun error(message: String) {
        if (printter is FilePrintter) {
            printter.error(message)
            return
        }
        Log.e("ErrorMessage", message)
    }


}