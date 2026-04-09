package com.nanoporetech.scainter.data

import com.nanoporetech.scainter.model.Provider

data class AppUiState(
    /** whether the user is currently logged in */
    val isLoggedIn: Boolean = false,
    /** whether there was an error logging in */
    val isLoginError: Boolean = false,
    /** data for the Logged in provider */
    val provider: Provider? = null,
)
