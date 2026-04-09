package com.nanoporetech.scainter.network

class FakeApiRepository: ApiServiceRepository {
    var loginResult: LoginResult = LoginResult.Success

    override suspend fun login(
        username: String,
        password: String
    ): LoginResult {
        return loginResult
    }
}