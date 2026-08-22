package com.nanoporetech.scainter.ui.events

import androidx.annotation.StringRes

sealed interface UiMessage {
    data class Success(@StringRes val messageId: Int): UiMessage
    data class Error(@StringRes val errorId: Int): UiMessage
}
