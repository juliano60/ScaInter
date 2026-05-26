package com.nanoporetech.scainter.model

import com.nanoporetech.scainter.conf.appConfig
import com.nanoporetech.scainter.ui.utils.capitalized
import kotlinx.serialization.Serializable
import java.net.URL

@Serializable
data class PolicyHolder(
    val id: Int,
    val fullname: String,
    val internalId: String,
    val subscriberName: String,
    val contractType: String,
    val coverExternal: String,
    val dateOfBirth: String,
    val insuranceType: String,
    val plafonnage: String,
    val insuranceStatus: String,
    val picture: String?,
    val providerStatus: String,
    val lastConsultationType: String,
    val lastConsultationDate: String,
    val providerName: String,
    val consommationTotal: Double,
    val plafond: Double,
    val isConsommationLimitReached: Boolean,
    val isStatusInactive: Boolean,
    val isPriceFixed: Boolean,
    val consultationList: List<String>,
    val costs: List<String>
) {
}

// imageUrl returns the image URL (as a String) for the PolicyHolder.
val PolicyHolder.imageUrl: String
    get() {
        return "${appConfig.httpProtocol}://${appConfig.hostname}${appConfig.imagesPath}/$internalId.jpg"
    }

val PolicyHolder.providerDisplayedName: String
    get() {
        val fragments = providerName.split(
            Regex("\\s+|'|’")
        )

        val result = buildString {
            fragments.forEachIndexed { index, fragment ->
                if (fragment.length == 1 && index != fragments.lastIndex) {
                    append(fragment)
                    append("'")
                } else {
                    append(fragment.capitalized())
                    append(" ")
                }
            }
        }

        return result
            .replace("De", "de")
            .trim()
    }