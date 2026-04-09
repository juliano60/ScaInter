package com.nanoporetech.scainter.network

import com.nanoporetech.scainter.model.Provider

sealed interface LoginResult {
    data class Success(val provider: Provider) : LoginResult
    data object InvalidCredentials : LoginResult
    data object NetworkError : LoginResult
    data object UnknownError : LoginResult
}

interface ApiServiceRepository {
    suspend fun login(
        username: String,
        password: String
    ): LoginResult
}
