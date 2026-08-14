package com.nanoporetech.scainter.ui.examination

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.nanoporetech.scainter.ScaInterApplication
import com.nanoporetech.scainter.data.ExaminationUiState
import com.nanoporetech.scainter.data.ScaDataRepository
import com.nanoporetech.scainter.ui.consultation.NewConsultationViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ExaminationViewModel(
    private val providerName: String,
    private val careCoverage: String,
    private val repository: ScaDataRepository
): ViewModel() {

    private var _uiState = MutableStateFlow(ExaminationUiState())
    val uiState = _uiState.asStateFlow()

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
        val percent = (careCoverage.toDoubleOrNull()  ?: 0.0).div(100.0)
        val providerShare = totalCost * percent
        val patientShare = totalCost - providerShare

        _uiState.update {
            it.copy(cost = cost,
                providerShare = providerShare.toString(),
                patientShare = patientShare.toString())
        }
    }

    companion object {
        fun provideFactory(
            providerName: String,
            careCoverage: String,
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as ScaInterApplication
                val repository = application.container.scaDataRepository

                ExaminationViewModel(
                    providerName = providerName,
                    careCoverage = careCoverage,
                    repository = repository
                )
            }
        }
    }
}