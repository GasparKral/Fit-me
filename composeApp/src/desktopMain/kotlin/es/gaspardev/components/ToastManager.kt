package es.gaspardev.components

import com.dokar.sonner.ToastType
import com.dokar.sonner.ToasterState

object ToastManager {
    private var _toasterState: ToasterState? = null

    fun initialize(toasterState: ToasterState) {
        _toasterState = toasterState
    }

    fun show(message: String) {
        _toasterState?.show(message)
    }

    fun showSuccess(message: String) {
        _toasterState?.show("✅ $message", type = ToastType.Success)
    }

    fun showError(message: String) {
        _toasterState?.show("❌ $message", type = ToastType.Error)
    }

    fun showWarning(message: String) {
        _toasterState?.show("⚠️ $message", type = ToastType.Warning)
    }
}
