package com.nanoporetech.scainter.model

import com.nanoporetech.scainter.conf.appConfig
import kotlinx.serialization.Serializable
import java.net.URL

@Serializable
data class PolicyHolder(
    val id: Int,
    val fullname: String,
    val internalId: String?,
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

    // Photos_SCA/<internalId>.jpg
    val imageUrl: URL?
        get() = try {
            URL(
                "${appConfig.httpProtocol}://${appConfig.hostname}" +
                        "${appConfig.imagesPath}/${internalId ?: "dummy"}.jpg"
            )
        } catch (e: Exception) {
            null
        }
}
