package com.nanoporetech.scainter.model

import kotlinx.serialization.Serializable

@Serializable
data class FamilyMember(
    val id: Int,          // link with PolicyHolder.id
    val fullname: String,
    val internalId: String?,
    val link: String
)
