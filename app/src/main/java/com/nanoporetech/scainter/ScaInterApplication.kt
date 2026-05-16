package com.nanoporetech.scainter

import android.app.Application
import com.nanoporetech.scainter.data.AppContainer
import com.nanoporetech.scainter.data.DefaultAppContainer

class ScaInterApplication: Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}