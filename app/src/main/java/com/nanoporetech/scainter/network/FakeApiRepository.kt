package com.nanoporetech.scainter.network

import com.nanoporetech.scainter.model.Provider

class FakeApiRepository: ApiServiceRepository {
    var loginResult: LoginResult = LoginResult.Success(provider = Provider(
        id = 620,
        role = "etablissement",
        name = "TestName"
    ))

    override suspend fun login(
        username: String,
        password: String
    ): LoginResult {
        return loginResult
    }
}