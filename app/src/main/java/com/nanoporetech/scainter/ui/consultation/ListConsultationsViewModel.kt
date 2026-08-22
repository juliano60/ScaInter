package com.nanoporetech.scainter.ui.consultation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.nanoporetech.scainter.ScaInterApplication
import com.nanoporetech.scainter.data.FetchConsultationsResult
import com.nanoporetech.scainter.data.ListConsultationsUiState
import com.nanoporetech.scainter.data.ScaDataRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ListConsultationsViewModel(
    private val providerName: String,
    private val repository: ScaDataRepository
): ViewModel() {

    private var _uiState = MutableStateFlow(ListConsultationsUiState())
    val uiState = _uiState.asStateFlow()

    fun loadConsultations() {
        viewModelScope.launch {
            when (val result = repository.fetchConsultationsFor(providerName)) {
                is FetchConsultationsResult.Success -> {
                    _uiState.update {
                        it.copy(
                            consultations = result.consultations,
                            isLoading = false
                        )
                    }
                }
                is FetchConsultationsResult.NetworkError -> {
                    _uiState.update { it.copy(isLoading = false) }
                }
                else -> {
                    _uiState.update { it.copy(isLoading = false) }
                }
            }
        }
    }

    companion object {
        fun provideFactory(
            providerName: String,
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as ScaInterApplication
                val repository = application.container.scaDataRepository

                ListConsultationsViewModel(
                    providerName = providerName,
                    repository = repository
                )
            }
        }
    }
}
