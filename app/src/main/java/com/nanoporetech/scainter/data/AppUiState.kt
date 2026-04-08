package com.nanoporetech.scainter.data

data class AppUiState(
    /** whether the user is currently logged in */
    val isLoggedIn: Boolean = false,
    /** whether there was an error logging in */
    val isLoginError: Boolean = false,
)
