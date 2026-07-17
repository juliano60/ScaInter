package com.nanoporetech.scainter.data

import com.google.gson.GsonBuilder
import com.nanoporetech.scainter.network.ScaApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface AppContainer {
    val scaDataRepository: ScaDataRepository
}

class DefaultAppContainer(
    private val okHttpClient: OkHttpClient = createLoggingOkHttpClient()
) : AppContainer {
    private val baseUrl = "http://138.68.160.209/centredesantetout/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val retrofitService: ScaApiService by lazy {
        retrofit.create(ScaApiService::class.java)
    }

    override val scaDataRepository: ScaDataRepository by lazy {
        ScaNetworkDataRepository(retrofitService)
    }

    private companion object {
        fun createLoggingOkHttpClient(): OkHttpClient {
            return OkHttpClient.Builder()
                .addInterceptor(
                    HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    }
                )
                .build()
        }
    }
}
