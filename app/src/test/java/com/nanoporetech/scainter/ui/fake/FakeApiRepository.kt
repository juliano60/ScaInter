package com.nanoporetech.scainter.ui.fake

import com.nanoporetech.scainter.data.FetchConsultationsResult
import com.nanoporetech.scainter.data.FetchExaminationsResult
import com.nanoporetech.scainter.data.FetchHospitalisationsResult
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

    override suspend fun fetchConsultationsFor(provider: String): FetchConsultationsResult {
        return FetchConsultationsResult.UnknownError
    }

    override suspend fun fetchExaminationsFor(provider: String): FetchExaminationsResult {
        return FetchExaminationsResult.UnknownError
    }

    override suspend fun fetchHospitalisationsFor(provider: String): FetchHospitalisationsResult {
        return FetchHospitalisationsResult.UnknownError
    }
}