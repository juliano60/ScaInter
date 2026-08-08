package com.nanoporetech.scainter.data

import com.nanoporetech.scainter.model.Examination

data class ListExaminationsUiState(
    /** the list of examinations */
    val examinations: List<Examination> = emptyList(),
    /** this allows us to distinguish between fetching in flight and empty list */
    val isLoading: Boolean = true
)
