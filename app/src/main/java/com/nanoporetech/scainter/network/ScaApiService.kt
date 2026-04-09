package com.nanoporetech.scainter.network

import com.google.gson.GsonBuilder
import com.nanoporetech.scainter.model.Provider
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

private const val BASE_URL = "http://138.68.160.209/centredesantetout/"

private val loggingInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}

private val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(loggingInterceptor)
    .build()

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .client(okHttpClient)
    .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
    .build()

interface ScaApiService {
    @POST("provider_api.php")
    suspend fun loginProvider(@Body request: FetchProviderRequest): Response<Provider>

    /*@GET("assure_api.php")
    suspend fun fetchFamilyMembers(@Query("action") action: String,
                                   @Query("family_id") familyId: String): Response<List<FamilyMember>>*/
}

object ScaApi {
    val retrofitService: ScaApiService by lazy {
        retrofit.create(ScaApiService::class.java)
    }
}

