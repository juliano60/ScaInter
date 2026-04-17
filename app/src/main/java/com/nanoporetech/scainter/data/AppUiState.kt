package com.nanoporetech.scainter.data

import com.nanoporetech.scainter.model.Provider

data class AppUiState(
    /** whether the user is currently logged in */
    val isLoggedIn: Boolean = false,
    /** whether there was an error logging in */
    val isLoginError: Boolean = false,
    /** data for the Logged in provider */
    val provider: Provider = Provider(),
    /** the user's id */
    val username: String = "",
    /** the user's password */
    val password: String = "",
    /** the rememberMe state */
    val rememberMe: Boolean = false,
)
