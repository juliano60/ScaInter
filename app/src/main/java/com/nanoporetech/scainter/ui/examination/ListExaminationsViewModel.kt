package com.nanoporetech.scainter.ui.examination

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.nanoporetech.scainter.R
import com.nanoporetech.scainter.ScaInterApplication
import com.nanoporetech.scainter.data.FetchExaminationsResult
import com.nanoporetech.scainter.data.ListExaminationsUiState
import com.nanoporetech.scainter.data.ScaDataRepository
import com.nanoporetech.scainter.ui.events.UiEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class ListExaminationsViewModel(
    private val providerName: String,
    private val repository: ScaDataRepository
): ViewModel() {

    private var _uiState = MutableStateFlow(ListExaminationsUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<UiEvent>()
    val events = _events.asSharedFlow()

    fun loadExaminations() {
        viewModelScope.launch {
            when (val result = repository.fetchExaminationsFor(providerName)) {
                is FetchExaminationsResult.Success -> {
                    _uiState.update {
                        it.copy(
                            examinations = result.examinations,
                            isLoading = false
                        )
                    }
                    _events.emit(UiEvent.Success(R.string.examinations_loaded_message))
                }
                is FetchExaminationsResult.NetworkError -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _events.emit(UiEvent.Error(R.string.err_network_error_message))
                }
                else -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _events.emit(UiEvent.Error(R.string.examinations_loaded_error_message))
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

                ListExaminationsViewModel(
                    providerName = providerName,
                    repository = repository
                )
            }
        }
    }
}