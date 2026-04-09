package com.nanoporetech.scainter.network

import com.google.gson.annotations.SerializedName

data class FetchProviderRequest(
    @SerializedName("action")
    val action: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("password")
    val password: String
)
