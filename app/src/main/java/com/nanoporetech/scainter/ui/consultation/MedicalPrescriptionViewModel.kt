package com.nanoporetech.scainter.ui.consultation

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
    val quantityIndex: Int,
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
            it.copy(quantityIndex = index)
        }
    }

    fun setMedication(value: String) {
        _uiState.update {
            it.copy(medication = value)
        }
    }

    fun addPrescription() {
        val currentState = _uiState.value

        val prescription = Prescription(
            name = _uiState.value.medication,
            quantityIndex = _uiState.value.quantityIndex,
            posology = _uiState.value.posology
        )

        val updatedPrescriptions = currentState.prescriptions
            // Overwrite old prescription with new one
            .filterNot { it.name == prescription.name }
            .plus(prescription)

        if (updatedPrescriptions.size > MAX_PRESCRIPTIONS) {
            return
        }

        _uiState.update { currentState ->
            currentState.copy(
                prescriptions = updatedPrescriptions,
                isDialogOpen = false
            )
        }
    }

    fun removePrescription(item: Prescription) {
        _uiState.update {
            it.copy(
                prescriptions = it.prescriptions - item
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

    fun editPrescription(prescription: Prescription) {
        _uiState.update {
            it.copy(
                medication = prescription.name,
                quantityIndex = prescription.quantityIndex,
                posology = prescription.posology,
                isDialogOpen = true
            )
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