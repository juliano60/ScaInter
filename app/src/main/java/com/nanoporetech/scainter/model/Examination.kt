package com.nanoporetech.scainter.model

import kotlinx.serialization.Serializable

@Serializable
data class Examination(
    val id: Int,
    val fullname: String,
    val internalId: String,
    val coverPercentage: String,
    val subscriberName: String,
    val contractType: String,
    val status: String,
    val controller: String? = null,
    val provenance: String? = null,
    val doctor: String? = null,
    val specialty: String? = null,
    val reason: String,
    val exams: List<String?>,
    val answers: List<String?>,
    val rejectionReason: String? = null,
    val creationDate: String,
    val total: Double,
    val totalSca: Double,
    val totalUser: Double,
    val dateOfBirth: String
) {
    val displayedReason: String
        get() {
            var result = reason

            // Put space around plus signs
            result = result.replace("+", " + ")

            // Capitalize words and normalize spacing
            result = result
                .lowercase()
                .split(Regex("\\s+"))
                .joinToString(" ") { it.replaceFirstChar { c -> c.uppercase() } }

            // French grammatical corrections
            result = result
                .replace("D ", "d'")
                .replace("L ", "l'")
                .replace("De ", "de ")
                .replace("En ", "en ")
                .replace("Sur ", "sur ")
                .replace("Une ", "une ")
                .replace("Un ", "Un ")
                .replace("Avec ", "avec ")
                .replace("Sans ", "sans ")
                .replace("Sauf ", "sauf ")
                .replace("Du ", "du ")
                .replace("Des ", "des ")

            return result
        }
}