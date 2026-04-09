package com.nanoporetech.scainter.network

sealed interface LoginResult {
    data object Success : LoginResult
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
