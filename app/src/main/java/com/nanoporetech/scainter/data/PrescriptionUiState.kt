package com.nanoporetech.scainter.data

import com.nanoporetech.scainter.ui.consultation.Prescription

data class PrescriptionUiState(
    /** Prescribing physician */
    val doctor: String = "",
    /** Affection code */
    val affection: String = "",
    /** Posology for current prescription */
    val posology: String = "",
    /** Quantity index [0-3] for current prescription */
    val quantityIndex: Int = 0,
    /** Medication name for current prescription */
    val medication: String = "",
    /** Whether to open the prescription dialog */
    val isDialogOpen: Boolean = false,
    /** Medical prescriptions */
    val prescriptions: List<Prescription> = listOf()
)