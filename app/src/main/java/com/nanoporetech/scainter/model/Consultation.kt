package com.nanoporetech.scainter.model

import com.nanoporetech.scainter.conf.appConfig
import kotlinx.serialization.Serializable

@Serializable
data class Consultation(
    val id: Int,
    val policyHolderId: Int,
    val fullname: String,
    val subscriberName: String,
    val contractType: String,
    val act: String,
    val creationDate: String,
    val doctor: String? = null,
    val affliction: String? = null,
    val prescription: String? = null,
    val quantity: Int,
    val posology: String,
    val prescription1: String? = null,
    val quantity1: Int,
    val posology1: String,
    val prescription2: String? = null,
    val quantity2: Int,
    val posology2: String,
    val prescription3: String? = null,
    val quantity3: Int,
    val posology3: String,
    val total: Double,
    val totalSca: Double,
    val totalUser: Double,
    val internalId: String,
    val percentageCoverage: String,
    val dateOfBirth: String
)

// imageUrl returns the image URL (as a String) for the policy holder.
val Consultation.imageUrl: String
    get() {
        return "${appConfig.httpProtocol}://${appConfig.hostname}${appConfig.imagesPath}/$internalId.jpg"
    }

val Consultation.prescriptions: List<String>
    get() {
        val result = mutableListOf<String>()

        fun appendIfNotEmpty(value: String?) {
            if (!value.isNullOrEmpty()) {
                result.add(value)
            }
        }

        appendIfNotEmpty(prescription)
        appendIfNotEmpty(prescription1)
        appendIfNotEmpty(prescription2)
        appendIfNotEmpty(prescription3)
        return result
    }
