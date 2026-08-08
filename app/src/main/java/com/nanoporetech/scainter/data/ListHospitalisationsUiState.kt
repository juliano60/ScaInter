package com.nanoporetech.scainter.data

import com.nanoporetech.scainter.model.Hospitalisation

data class ListHospitalisationsUiState(
    /** the list of hospitalisations */
    val hospitalisations: List<Hospitalisation> = emptyList(),
    /** this allows us to distinguish between fetching in flight and empty list */
    val isLoading: Boolean = true
)
