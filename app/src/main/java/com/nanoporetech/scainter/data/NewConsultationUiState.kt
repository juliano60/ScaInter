package com.nanoporetech.scainter.data

import com.nanoporetech.scainter.model.FamilyMember
import com.nanoporetech.scainter.model.PolicyHolder

data class NewConsultationUiState(
    /** the policy identifier */
    val familyId: String = "",
    /** all the family members linked to that that policy */
    val familyMembers: List<FamilyMember> = emptyList(),
    /** the family members' details for a given policy */
    val policyHolders: List<PolicyHolder> = emptyList(),
    /** the desired consultation */
    val selectedConsultation: String = "",
    /** the cost for that consultation */
    val selectedCost: String = ""
)
