package com.nanoporetech.scainter.data

import com.nanoporetech.scainter.model.FamilyMember

data class NewHospitalisationUiState(
    val familyId: String = "",
    val familyMembers: List<FamilyMember> = emptyList()
)
