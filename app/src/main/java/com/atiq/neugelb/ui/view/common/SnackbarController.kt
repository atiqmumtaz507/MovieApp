package com.atiq.neugelb.ui.view.common

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Stable

@Stable
class SnackbarController {
    private var hostState: SnackbarHostState? = null

    internal fun attach(host: SnackbarHostState) {
        hostState = host
    }

    suspend fun show(
        message: String,
        actionLabel: String? = null,
        duration: SnackbarDuration = SnackbarDuration.Short
    ) {
        hostState?.showSnackbar(
            message = message,
            actionLabel = actionLabel,
            duration = duration
        )
    }
}