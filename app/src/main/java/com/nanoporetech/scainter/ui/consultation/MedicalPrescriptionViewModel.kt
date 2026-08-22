package com.nanoporetech.scainter.ui.consultation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.nanoporetech.scainter.R
import com.nanoporetech.scainter.ScaInterApplication
import com.nanoporetech.scainter.data.PrescriptionUiState
import com.nanoporetech.scainter.data.ScaDataRepository
import com.nanoporetech.scainter.ui.events.UiMessage
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class Prescription (
    val name: String,
    val quantityIndex: Int,
    val posology: String
)

private const val MAX_PRESCRIPTIONS = 4

class MedicalPrescriptionViewModel(
    private val consultationId: String,
    private val repository: ScaDataRepository
): ViewModel() {

    private var _uiState = MutableStateFlow(PrescriptionUiState(consultationId = consultationId))
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<UiMessage>()
    val events = _events.asSharedFlow()

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

    private fun quantityFromIndex(
        medication: String, index: Int): String =
        if (medication.isBlank()) "0" else (index + 1).toString()

    fun addPrescription() {
        val submittedState = _uiState.value

        val newPrescriptions = listOf(
            Prescription(
                name = submittedState.medication,
                quantityIndex = submittedState.quantityIndex,
                posology = submittedState.posology
            ),
            Prescription(
                name = submittedState.medication1,
                quantityIndex = submittedState.quantityIndex1,
                posology = submittedState.posology1
            ),
            Prescription(
                name = submittedState.medication2,
                quantityIndex = submittedState.quantityIndex2,
                posology = submittedState.posology2
            ),
            Prescription(
                name = submittedState.medication3,
                quantityIndex = submittedState.quantityIndex3,
                posology = submittedState.posology3
            )
        )
            .filter { it.name.isNotBlank() }
            .map {
                it.copy(
                    name = it.name.trim(),
                    posology = it.posology.trim()
                )
            }
            .distinctBy { it.name.trim().lowercase() }

        if (newPrescriptions.isEmpty()) {
            return
        }

        // now send prescription to server
        viewModelScope.launch {
            val successful = runCatching {
                repository.updatePrescription(
                    consultationId = submittedState.consultationId,
                    doctor = submittedState.doctor,
                    affection = submittedState.affection,
                    medicament = submittedState.medication,
                    quantity = quantityFromIndex(submittedState.medication, submittedState.quantityIndex),
                    posologie = submittedState.posology,
                    medicament1 = submittedState.medication1,
                    quantity1 = quantityFromIndex(submittedState.medication1, submittedState.quantityIndex1),
                    posologie1 = submittedState.posology1,
                    medicament2 = submittedState.medication2,
                    quantity2 = quantityFromIndex(submittedState.medication2, submittedState.quantityIndex2),
                    posologie2 = submittedState.posology2,
                    medicament3 = submittedState.medication3,
                    quantity3 = quantityFromIndex(submittedState.medication3, submittedState.quantityIndex3),
                    posologie3 = submittedState.posology3,
                )
            }.getOrDefault(false)

            if (successful) {
                _uiState.update {
                    it.copy(
                        prescriptions = newPrescriptions,
                        isDialogOpen = false
                    )
                }
                _events.emit(UiMessage.Success(R.string.new_prescription_added))
            } else {
                _events.emit(UiMessage.Error(R.string.new_prescription_error))
            }
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
                isDialogOpen = true)
        }
    }

    fun closeDialog() {
        _uiState.update {
            it.copy(isDialogOpen = false)
        }
    }

    companion object {
        fun provideFactory(
            consultationId: String,
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as ScaInterApplication
                val repository = application.container.scaDataRepository

                MedicalPrescriptionViewModel(
                    consultationId = consultationId,
                    repository = repository
                )
            }
        }
    }
}
