package com.nanoporetech.scainter.conf

interface AppConfiguration {
    val httpProtocol: String
    val hostname: String
    val hostPath: String
    val imagesPath: String
}

object ProductionConfig: AppConfiguration {
    override val httpProtocol: String
        get() = "https"
    override val hostname: String
        get() = "www.scaintera-segam.com"
    override val hostPath: String
        get() = "/centredesantetout"
    override val imagesPath: String
        get() = "/Photos_SCA"
}

object TestConfig: AppConfiguration {
    override val httpProtocol: String
        get() = "http"
    override val hostname: String
        get() = "138.68.160.209"
    override val hostPath: String
        get() = "/centredesantetout"
    override val imagesPath: String
        get() = "/Photos_SCA"
}

// global configuration instance
// Replace when switching between test and production environments.
var appConfig: AppConfiguration = TestConfig