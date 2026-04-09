package com.nanoporetech.scainter.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable
import java.util.Locale

enum class ProviderRole {
    HealthCare,
    Laboratory,
    Pharmacy,
    DentalCare
}

@Serializable
data class Provider(
    @SerializedName("id")
    val id: Int,
    @SerializedName("role")
    val role: String,
    @SerializedName("name")
    val name: String
) {
}

val Provider.roleType: ProviderRole
    get() = when (role.lowercase(Locale.getDefault())) {
        "etablissement" -> ProviderRole.HealthCare
        "laboratoire"   -> ProviderRole.Laboratory
        "pharmacie"     -> ProviderRole.Pharmacy
        "dentaire"      -> ProviderRole.DentalCare
        else -> throw IllegalArgumentException("Unsupported provider role: $role")
    }

// Port of your displayedName logic:
// - split on spaces or ASCII/Unicode apostrophes
// - if fragment is a single char and not last, keep it and add an apostrophe without space
// - otherwise capitalize the fragment (first letter upper, rest lower) and add a trailing space
// - finally, replace standalone "De" with "de"
val Provider.displayedName: String
    get() {
        val regex = Regex("[\\s'’]+") // split on space or ' or ’
        val frags = name.split(regex).filter { it.isNotBlank() }

        val out = buildString {
            frags.forEachIndexed { idx, raw ->
                val isLast = idx == frags.lastIndex
                if (raw.length == 1 && !isLast) {
                    append(raw)
                    append('\'') // keep apostrophe tight (e.g., "O'Connor")
                } else {
                    val capped = raw.lowercase(Locale.getDefault())
                        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
                    append(capped)
                    append(' ')
                }
            }
        }.trim()

        // Replace standalone "De" with "de" (word-boundary safe)
        return out.replace(Regex("\\bDe\\b"), "de")
    }