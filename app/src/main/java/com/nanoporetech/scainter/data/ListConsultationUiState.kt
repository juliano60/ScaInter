package com.nanoporetech.scainter.data

import com.nanoporetech.scainter.model.Consultation

data class ListConsultationUiState(
    /** the list of consultations */
    val consultations: List<Consultation> = emptyList()
)