package com.nanoporetech.scainter.ui.consultation

import android.R.attr.name
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.nanoporetech.scainter.ScaInterApplication
import com.nanoporetech.scainter.data.PrescriptionUiState
import com.nanoporetech.scainter.data.ScaDataRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class Prescription (
    val name: String,
    val quantity: Int,
    val posology: String
)

private const val MAX_PRESCRIPTIONS = 4

class MedicalPrescriptionViewModel(
    private val repository: ScaDataRepository
): ViewModel() {

    private var _uiState = MutableStateFlow(PrescriptionUiState())
    val uiState = _uiState.asStateFlow()

    fun setDoctor(name: String) {
        _uiState.update {
            it.copy(doctor = name)
        }
    }

    fun setAffection(code: String) {
        _uiState.update {
            it.copy(affection = code)
        }
    }

    fun setPosology(value: String) {
        _uiState.update {
            it.copy(posology = value)
        }
    }

    fun setQuantity(index: Int) {
        _uiState.update {
            it.copy(quantity = index)
        }
    }

    fun setMedication(value: String) {
        _uiState.update {
            it.copy(medication = value)
        }
    }

    fun addPrescription() {
        if (!canAddPrescription()) {
            return
        }

        val prescription = Prescription(
            name = _uiState.value.medication,
            quantity = _uiState.value.quantity,
            posology = _uiState.value.posology
        )

        _uiState.update { currentState ->
            currentState.copy(
                prescriptions = currentState.prescriptions.plus(prescription),
                isDialogOpen = false
            )
        }
    }

    fun canAddPrescription(): Boolean {
        return _uiState.value.prescriptions.size < MAX_PRESCRIPTIONS
    }

    fun isFormValid(): Boolean {
        return _uiState.value.prescriptions.isNotEmpty()
    }

    fun openDialog() {
        _uiState.update {
            it.copy(isDialogOpen = true)
        }
    }

    fun closeDialog() {
        _uiState.update {
            it.copy(isDialogOpen = false)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as ScaInterApplication)
                val repository = application.container.scaDataRepository
                MedicalPrescriptionViewModel(
                    repository = repository,
                )
            }
        }
    }
}