package com.nanoporetech.scainter.data

import com.nanoporetech.scainter.ui.consultation.Prescription

data class PrescriptionUiState(
    /** Consultation ID */
    val consultationId: String = "",
    /** Prescribing physician */
    val doctor: String = "",
    /** Affection code */
    val affection: String = "",
    /** Posology for first prescription */
    val posology: String = "",
    /** Quantity index [0-3] for first prescription */
    val quantityIndex: Int = 0,
    /** Medication name first prescription */
    val medication: String = "",
    /** Posology for second prescription */
    val posology1: String = "",
    /** Quantity index [0-3] for second prescription */
    val quantityIndex1: Int = 0,
    /** Medication name second prescription */
    val medication1: String = "",
    /** Posology for third prescription */
    val posology2: String = "",
    /** Quantity index [0-3] for third prescription */
    val quantityIndex2: Int = 0,
    /** Medication name third prescription */
    val medication2: String = "",
    /** Posology for fourth prescription */
    val posology3: String = "",
    /** Quantity index [0-3] for fourth prescription */
    val quantityIndex3: Int = 0,
    /** Medication name fourth prescription */
    val medication3: String = "",
    /** Whether to open the prescription dialog */
    val isDialogOpen: Boolean = false,
    /** Medical prescriptions */
    val prescriptions: List<Prescription> = listOf()
)