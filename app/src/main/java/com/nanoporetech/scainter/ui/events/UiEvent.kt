package com.nanoporetech.scainter.ui.events

import androidx.annotation.StringRes

sealed interface UiEvent {
    data class Success(@StringRes val messageId: Int): UiEvent
    data class Error(@StringRes val errorId: Int): UiEvent
}