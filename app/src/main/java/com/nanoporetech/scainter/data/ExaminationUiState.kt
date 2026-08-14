package com.nanoporetech.scainter.data

data class ExaminationUiState(
    /** reason for the examination */
    val reason: String = "",
    /** the requested examination */
    val designation: String = "",
    /** the total cost for that examination */
    val costTotal: String = "",
    /** the patient's share of the cost */
    val costUser: String = "",
    /** the assurer's share of the cost */
    val costSca: String = "",
    /** guard against double submission */
    val isSubmitting: Boolean = false
)
