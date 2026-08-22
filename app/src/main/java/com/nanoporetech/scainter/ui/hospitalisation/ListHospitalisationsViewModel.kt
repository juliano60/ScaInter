package com.nanoporetech.scainter.ui.hospitalisation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.nanoporetech.scainter.R
import com.nanoporetech.scainter.ScaInterApplication
import com.nanoporetech.scainter.data.FetchHospitalisationsResult
import com.nanoporetech.scainter.data.ListHospitalisationsUiState
import com.nanoporetech.scainter.data.ScaDataRepository
import com.nanoporetech.scainter.ui.events.UiMessage
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class ListHospitalisationsViewModel(
    private val providerName: String,
    private val repository: ScaDataRepository
): ViewModel() {

    private var _uiState = MutableStateFlow(ListHospitalisationsUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<UiMessage>()
    val events = _events.asSharedFlow()

    fun loadHospitalisations() {
        viewModelScope.launch {
            when (val result = repository.fetchHospitalisationsFor(providerName)) {
                is FetchHospitalisationsResult.Success -> {
                    _uiState.update {
                        it.copy(
                            hospitalisations = result.hospitalisations,
                            isLoading = false
                        )
                    }
                    _events.emit(UiMessage.Success(R.string.hospitalistions_loaded_message))
                }
                is FetchHospitalisationsResult.NetworkError -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _events.emit(UiMessage.Error(R.string.err_network_error_message))
                }
                else -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _events.emit(UiMessage.Error(R.string.hospitalisations_loaded_error_message))
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

                ListHospitalisationsViewModel(
                    providerName = providerName,
                    repository = repository
                )
            }
        }
    }
}
