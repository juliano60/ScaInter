package com.nanoporetech.scainter.data

import com.nanoporetech.scainter.model.FamilyMember
import com.nanoporetech.scainter.model.PolicyHolder

data class NewExaminationUiState(
    /** the policy identifier */
    val familyId: String = "",
    /** all the family members linked to that that policy */
    val familyMembers: List<FamilyMember> = emptyList(),
    /** the family members' details for a given policy */
    val policyHolders: List<PolicyHolder> = emptyList(),
    /** the current policyholder */
    val currentPolicyHolder: PolicyHolder? = null,
    /** we are loading data */
    val isLoading: Boolean = false
)
