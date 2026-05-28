package com.nanoporetech.scainter.model

import kotlinx.serialization.Serializable

@Serializable
data class Status(
    val id: String,
    val status: String
) {
}

fun Status.isOk(): Boolean  {
    return this.status.lowercase() == "ok"
}
