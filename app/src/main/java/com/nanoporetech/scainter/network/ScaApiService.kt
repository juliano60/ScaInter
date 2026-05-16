package com.nanoporetech.scainter.network

import com.nanoporetech.scainter.model.Provider
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ScaApiService {
    @POST("provider_api.php")
    suspend fun fetchProvider(@Body request: FetchProviderRequest): Response<Provider?>
}
