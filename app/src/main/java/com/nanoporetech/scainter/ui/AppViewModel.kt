package com.nanoporetech.scainter.ui

import androidx.lifecycle.ViewModel
import com.nanoporetech.scainter.data.AppUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppViewModel(): ViewModel() {
    private val _uiState = MutableStateFlow(AppUiState())
    //val uiState = _uiState.asStateFlow()
}