package com.nanoporetech.scainter.data

import com.nanoporetech.scainter.model.Consultation

data class ListConsultationUiState(
    /** the list of consultations */
    val consultations: List<Consultation> = emptyList(),
    /** this allows us to distinguish between fetching in flight and empty list */
    val isLoading: Boolean = true
)
