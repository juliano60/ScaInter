package com.nanoporetech.scainter.data

data class ExaminationUiState(
    /** reason for the examination */
    val reason: String = "",
    /** the requested examination */
    val designation: String = "",
    /** the total cost for that examination */
    val cost: String = "",
    /** the patient's share of the cost */
    val patientShare: String = "",
    /** the provider's share of the cost */
    val providerShare: String = ""
)
