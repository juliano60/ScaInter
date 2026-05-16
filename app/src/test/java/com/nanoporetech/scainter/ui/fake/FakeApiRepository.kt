package com.nanoporetech.scainter.ui.fake

import com.nanoporetech.scainter.data.FetchProviderResult
import com.nanoporetech.scainter.data.ScaDataRepository
import com.nanoporetech.scainter.model.Provider

class FakeScaNetworkDataRepository(): ScaDataRepository {
    var loginResult: FetchProviderResult = FetchProviderResult.AuthenticationFailed

    override suspend fun fetchProvider(
        username: String,
        password: String
    ): FetchProviderResult {
        return loginResult
    }
}