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

    fun setPosology4(value: String) {
        _uiState.update {
            it.copy(posology4 = value)
        }
    }

    fun setQuantity4(index: Int) {
        _uiState.update {
            it.copy(quantityIndex4 = index)
        }
    }

    fun setMedication4(value: String) {
        _uiState.update {
            it.copy(medication4 = value)
        }
    }

    fun setPosology3(value: String) {
        _uiState.update {
            it.copy(posology3 = value)
        }
    }

    fun setQuantity3(index: Int) {
        _uiState.update {
            it.copy(quantityIndex3 = index)
        }
    }

    fun setMedication3(value: String) {
        _uiState.update {
            it.copy(medication3 = value)
        }
    }

    fun setPosology2(value: String) {
        _uiState.update {
            it.copy(posology2 = value)
        }
    }

    fun setQuantity2(index: Int) {
        _uiState.update {
            it.copy(quantityIndex2 = index)
        }
    }

    fun setMedication2(value: String) {
        _uiState.update {
            it.copy(medication2 = value)
        }
    }

    fun setPosology1(value: String) {
        _uiState.update {
            it.copy(posology1 = value)
        }
    }

    fun setQuantity1(index: Int) {
        _uiState.update {
            it.copy(quantityIndex1 = index)
        }
    }

    fun setMedication1(value: String) {
        _uiState.update {
            it.copy(medication1 = value)
        }
    }

    fun addPrescription() {
        val state = _uiState.value

        val newPrescriptions = listOf(
            Prescription(
                name = state.medication1,
                quantityIndex = state.quantityIndex1,
                posology = state.posology1
            ),
            Prescription(
                name = state.medication2,
                quantityIndex = state.quantityIndex2,
                posology = state.posology2
            ),
            Prescription(
                name = state.medication3,
                quantityIndex = state.quantityIndex3,
                posology = state.posology3
            ),
            Prescription(
                name = state.medication4,
                quantityIndex = state.quantityIndex4,
                posology = state.posology4
            )
        )

        val namesToReplace = newPrescriptions
            .mapTo(mutableSetOf()) { it.name }

        val updatedPrescriptions =
            state.prescriptions.filterNot { it.name in namesToReplace } +
                    newPrescriptions

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
            it.copy(
                //medication = "",
                //quantityIndex = 0,
                //posology = "",
                isDialogOpen = true)
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
                //medication = prescription.name,
                //quantityIndex = prescription.quantityIndex,
                //posology = prescription.posology,
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