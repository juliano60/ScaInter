package com.nanoporetech.scainter.network

import java.io.IOException

class NetworkApiRepository(
    private val service: ScaApiService
): ApiServiceRepository {
    override suspend fun login(
        username: String,
        password: String
    ): LoginResult {
        return try {
            val request = FetchProviderRequest(
                action = "fetch",
                username = username,
                password = password
            )

            val response = service.loginProvider(request)

            if (response.isSuccessful) {
                LoginResult.Success
            } else {
                when (response.code()) {
                    in 400..499 -> LoginResult.InvalidCredentials
                    else -> LoginResult.UnknownError
                }
            }
        } catch (e: IOException) {
            LoginResult.NetworkError
        }
    }
}