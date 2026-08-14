package com.nanoporetech.scainter.ui.examination

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.nanoporetech.scainter.R
import com.nanoporetech.scainter.ScaInterApplication
import com.nanoporetech.scainter.data.ExaminationUiState
import com.nanoporetech.scainter.data.NewDayCareExaminationResult
import com.nanoporetech.scainter.data.ScaDataRepository
import com.nanoporetech.scainter.ui.events.UiEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ExaminationViewModel(
    private val providerName: String,
    private val careCoverage: String,
    private val userId: String,
    private val repository: ScaDataRepository
): ViewModel() {

    private var _uiState = MutableStateFlow(ExaminationUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<UiEvent>()
    val events = _events.asSharedFlow()

    fun setReason(reason: String) {
        _uiState.update {
            it.copy(reason = reason)
        }
    }

    fun setDesignation(designation: String) {
        _uiState.update {
            it.copy(designation = designation)
        }
    }

    fun updateCost(cost: String) {
        val totalCost = cost.toDoubleOrNull() ?: 0.0
        val percent = (careCoverage.removeSuffix("%").toDoubleOrNull()  ?: 0.0).div(100.0)
        val providerShare = totalCost * percent
        val patientShare = totalCost - providerShare

        _uiState.update {
            it.copy(costTotal = cost,
                costSca = providerShare.toString(),
                costUser = patientShare.toString())
        }
    }

    fun submitRequest() {
        if (_uiState.value.isSubmitting) return

        viewModelScope.launch {
            _uiState.update {
                it.copy(isSubmitting = true)
            }

            try {
                when (repository.newDayCareExamination(
                    provider = providerName,
                    userId = userId,
                    reason = _uiState.value.reason,
                    exam1 = _uiState.value.designation,
                    cost = _uiState.value.costTotal
                )) {
                    NewDayCareExaminationResult.Success -> {
                        _events.emit(UiEvent.Success(R.string.new_same_day_care_success_message))
                    }

                    NewDayCareExaminationResult.NetworkError -> {
                        _events.emit(UiEvent.Error(R.string.err_network_error_message))
                    }

                    NewDayCareExaminationResult.UnknownError -> {
                        _events.emit(UiEvent.Error(R.string.err_unknown_error_message))
                    }
                }
            } finally {
                _uiState.update {
                    it.copy(isSubmitting = false)
                }
            }
        }
    }

    companion object {
        fun provideFactory(
            providerName: String,
            careCoverage: String,
            userId: String,
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as ScaInterApplication
                val repository = application.container.scaDataRepository

                ExaminationViewModel(
                    providerName = providerName,
                    careCoverage = careCoverage,
                    userId = userId,
                    repository = repository
                )
            }
        }
    }
}