package com.nanoporetech.scainter

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import com.nanoporetech.scainter.data.AppContainer
import com.nanoporetech.scainter.data.DefaultAppContainer
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

class ScaInterApplication : Application(), ImageLoaderFactory {
    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
            .build()
    }

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(okHttpClient)
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .okHttpClient(okHttpClient)
            .build()
    }
}
