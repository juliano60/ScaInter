package com.nanoporetech.scainter.ui

import com.nanoporetech.scainter.ui.consultation.MedicalPrescriptionViewModel
import com.nanoporetech.scainter.ui.consultation.Prescription
import com.nanoporetech.scainter.ui.fake.FakeScaNetworkDataRepository
import org.junit.Assert.*
import org.junit.Test


class MedicalPrescriptionViewModelTest {

    private val fakeRepository = FakeScaNetworkDataRepository()

    @Test
    fun medicalViewModel_addPrescription_updatesState() {
        // Arrange
        val viewModel = MedicalPrescriptionViewModel(
            repository = fakeRepository
        )
        val expectedPrescription = Prescription(
            name = "Medication 1",
            quantityIndex = 2,
            posology = "Test posology 1"
        )
        val doctor = "Doctor 1"
        val affection = "360"

        viewModel.setDoctor(doctor)
        viewModel.setAffection (affection)
        viewModel.setMedication (expectedPrescription.name)
        viewModel.setPosology (expectedPrescription.posology)
        viewModel.setQuantity (expectedPrescription.quantityIndex)

        // Act
        viewModel.addPrescription()

        // Assert
        val state = viewModel.uiState.value
        assert(state.prescriptions.size == 1)
        assertEquals(expectedPrescription, state.prescriptions.first())
        assertEquals(doctor, state.doctor)
        assertEquals(affection, state.affection)
    }

    @Test
    fun medicalViewModel_removePrescription_updatesState() {
        // Arrange
        val viewModel = MedicalPrescriptionViewModel(
            repository = fakeRepository
        )
        val prescription = Prescription(
            name = "Medication 1",
            quantityIndex = 2,
            posology = "Test posology 1"
        )
        val doctor = "Doctor 1"
        val affection = "360"

        viewModel.setDoctor(doctor)
        viewModel.setAffection (affection)
        viewModel.setMedication (prescription.name)
        viewModel.setPosology (prescription.posology)
        viewModel.setQuantity (prescription.quantityIndex)

        // Act
        viewModel.addPrescription()
        viewModel.removePrescription(
            item = prescription
        )

        // Assert
        val state = viewModel.uiState.value
        assert(state.prescriptions.isEmpty())
    }

    @Test
    fun medicalViewModel_addDuplicateMedication_lastOneIsRetained() {
        // Arrange
        val viewModel = MedicalPrescriptionViewModel(
            repository = fakeRepository
        )
        val prescription1 = Prescription(
            name = "Medication 1",
            quantityIndex = 2,
            posology = "Test posology 1"
        )
        val prescription2 = Prescription(
            name = "Medication 1",
            quantityIndex = 3,
            posology = "Test posology 2"
        )
        val doctor = "Doctor 1"
        val affection = "360"

        viewModel.setDoctor(doctor)
        viewModel.setAffection (affection)
        viewModel.setMedication (prescription1.name)
        viewModel.setPosology (prescription1.posology)
        viewModel.setQuantity (prescription1.quantityIndex)

        // Act
        viewModel.addPrescription()

        viewModel.setMedication (prescription2.name)
        viewModel.setPosology (prescription2.posology)
        viewModel.setQuantity (prescription2.quantityIndex)
        viewModel.addPrescription()

        // Assert
        val state = viewModel.uiState.value
        assert(state.prescriptions.size == 1)
        assertEquals(prescription2, state.prescriptions.first())
    }
}