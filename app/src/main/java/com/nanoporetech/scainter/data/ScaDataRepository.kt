package com.nanoporetech.scainter.data

import android.util.Log
import com.nanoporetech.scainter.model.Consultation
import com.nanoporetech.scainter.model.Examination
import com.nanoporetech.scainter.model.Hospitalisation
import com.nanoporetech.scainter.model.Provider
import com.nanoporetech.scainter.network.FetchProviderRequest
import com.nanoporetech.scainter.network.ScaApiService
import okio.IOException

sealed interface FetchProviderResult {
    data class Success(val provider: Provider) : FetchProviderResult
    object AuthenticationFailed : FetchProviderResult
    object NetworkError : FetchProviderResult
}

sealed interface FetchConsultationsResult {
    data class Success(val consultations: List<Consultation>) : FetchConsultationsResult

    object NetworkError : FetchConsultationsResult
    object ServerError: FetchConsultationsResult
    object UnknownError : FetchConsultationsResult
}

sealed interface FetchExaminationsResult {
    data class Success(val examinations: List<Examination>) : FetchExaminationsResult

    object NetworkError : FetchExaminationsResult
    object ServerError: FetchExaminationsResult
    object UnknownError : FetchExaminationsResult
}

sealed interface FetchHospitalisationsResult {
    data class Success(val hospitalisations: List<Hospitalisation>) : FetchHospitalisationsResult

    object NetworkError : FetchHospitalisationsResult
    object ServerError: FetchHospitalisationsResult
    object UnknownError : FetchHospitalisationsResult
}

interface ScaDataRepository {
    suspend fun fetchProvider(username: String, password: String): FetchProviderResult
    suspend fun fetchConsultationsFor(provider: String): FetchConsultationsResult
    suspend fun fetchExaminationsFor(provider: String): FetchExaminationsResult
    suspend fun fetchHospitalisationsFor(provider: String): FetchHospitalisationsResult
}

private const val TAG = "ScaNetworkDataRepository"

class ScaNetworkDataRepository(
    private val scaApiService: ScaApiService
): ScaDataRepository {
    override suspend fun fetchProvider(username: String, password: String): FetchProviderResult {
        return try {
            val request = FetchProviderRequest(
                action = "fetch",
                username = username,
                password = password
            )

            val response = scaApiService.fetchProvider(request)

            if (response.isSuccessful) {
                response.body()?.let {
                    FetchProviderResult.Success(it)
                } ?: FetchProviderResult.NetworkError
            } else {
                when (response.code()) {
                    in 400..409 -> FetchProviderResult.AuthenticationFailed
                    else -> FetchProviderResult.NetworkError
                }
            }
        } catch (e: IOException) {
            Log.d(TAG, e.toString())
            FetchProviderResult.NetworkError
        }
    }

    override suspend fun fetchConsultationsFor(provider: String): FetchConsultationsResult {
        return try {
            val response = scaApiService.fetchConsultations(
                action = "fetch",
                provider = provider,
            )
            when {
                response.isSuccessful -> {
                    response.body()?.let {
                        FetchConsultationsResult.Success(it)
                    } ?: FetchConsultationsResult.UnknownError
                }
                response.code() in 500..599 -> {
                    FetchConsultationsResult.ServerError
                }
                else -> {
                    FetchConsultationsResult.UnknownError
                }
            }
        } catch(e: IOException) {
            Log.d(TAG, e.toString())
            FetchConsultationsResult.NetworkError
        }
    }

    override suspend fun fetchExaminationsFor(provider: String): FetchExaminationsResult {
        return try {
            val response = scaApiService.fetchExaminations(
                action = "fetch",
                provider = provider,
            )
            when {
                response.isSuccessful -> {
                    response.body()?.let {
                        FetchExaminationsResult.Success(it)
                    } ?: FetchExaminationsResult.UnknownError
                }
                response.code() in 500..599 -> {
                    FetchExaminationsResult.ServerError
                }
                else -> {
                    FetchExaminationsResult.UnknownError
                }
            }
        } catch(e: IOException) {
            Log.d(TAG, e.toString())
            FetchExaminationsResult.NetworkError
        }
    }

    override suspend fun fetchHospitalisationsFor(provider: String): FetchHospitalisationsResult {
        return try {
            val response = scaApiService.fetchHospitalisations(
                action = "fetch",
                provider = provider,
            )
            when {
                response.isSuccessful -> {
                    response.body()?.let {
                        FetchHospitalisationsResult.Success(it)
                    } ?: FetchHospitalisationsResult.UnknownError
                }
                response.code() in 500..599 -> {
                    FetchHospitalisationsResult.ServerError
                }
                else -> {
                    FetchHospitalisationsResult.UnknownError
                }
            }
        } catch(e: IOException) {
            Log.d(TAG, e.toString())
            FetchHospitalisationsResult.NetworkError
        }
    }
}