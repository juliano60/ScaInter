package com.nanoporetech.scainter.ui.utils

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals

enum class SnackbarType {
    Success,
    Error
}

class AppSnackbarVisuals(
    override val message: String,
    val type: SnackbarType,
    override val actionLabel: String? = null,
    override val withDismissAction: Boolean = false,
    override val duration: SnackbarDuration = SnackbarDuration.Short
): SnackbarVisuals