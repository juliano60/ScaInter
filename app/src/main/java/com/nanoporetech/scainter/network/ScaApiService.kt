package com.nanoporetech.scainter.network

import com.nanoporetech.scainter.model.Consultation
import com.nanoporetech.scainter.model.Provider
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ScaApiService {
    @POST("provider_api.php")
    suspend fun fetchProvider(@Body request: FetchProviderRequest): Response<Provider?>

    @GET("consultation_api.php")
    suspend fun fetchConsultations(
        @Query(value = "action") action: String,
        @Query(value = "provider") provider: String
    ): Response<List<Consultation>>
}
