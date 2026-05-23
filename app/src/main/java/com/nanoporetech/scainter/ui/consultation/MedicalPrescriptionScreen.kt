package com.nanoporetech.scainter.ui.consultation


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nanoporetech.scainter.R
import com.nanoporetech.scainter.conf.AppConstants
import com.nanoporetech.scainter.data.PrescriptionUiState
import com.nanoporetech.scainter.ui.components.CardHeader
import com.nanoporetech.scainter.ui.components.PrescriptionRow
import com.nanoporetech.scainter.ui.components.PrimaryButton
import com.nanoporetech.scainter.ui.theme.ScaInterAppTheme


@Composable
fun MedicalPrescriptionScreen(
    modifier: Modifier = Modifier,
    viewModel: MedicalPrescriptionViewModel = viewModel(
        factory = MedicalPrescriptionViewModel.Factory
    )
) {
    val state by viewModel.uiState.collectAsState()

    MedicalPrescriptionContent(
        state = state,
        onDoctorChanged = viewModel::setDoctor,
        onAffectionChanged = viewModel::setAffection,
        onPosologyChanged = viewModel::setPosology,
        onQuantityChanged = viewModel::setQuantity,
        onMedicationChanged = viewModel::setMedication,
        onAddPrescription = viewModel::addPrescription,
        onRemovePrescription = viewModel::removePrescription,
        onChangePrescription = { viewModel.editPrescription(it) },
        canAddPrescription = viewModel.canAddPrescription(),
        isFormValid = viewModel.isFormValid(),
        onOpenDialog = viewModel::openDialog,
        onCloseDialog = viewModel::closeDialog,
        modifier = modifier
    )
}

@Composable
fun MedicalPrescriptionContent(
    state: PrescriptionUiState,
    modifier: Modifier = Modifier,
    onDoctorChanged: (String) -> Unit = {},
    onAffectionChanged: (String) -> Unit = {},
    onPosologyChanged: (String) -> Unit = {},
    onQuantityChanged: (Int) -> Unit = {},
    onMedicationChanged: (String) -> Unit = {},
    onAddPrescription: () -> Unit = {},
    onRemovePrescription: (Prescription) -> Unit = {},
    onChangePrescription: (Prescription) -> Unit = {},
    canAddPrescription: Boolean = false,
    isFormValid: Boolean = false,
    onOpenDialog: () -> Unit = {},
    onCloseDialog: () -> Unit = {},
) {
    val paddingMedium = dimensionResource(R.dimen.padding_medium)

    Column(modifier) {
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
            elevation = CardDefaults.cardElevation(defaultElevation = dimensionResource(R.dimen.elevation_small)),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(paddingMedium)
            ) {
                CardHeader(
                    title = stringResource(R.string.medical_prescription_sub),
                    iconImg = Icons.AutoMirrored.Filled.Assignment,
                    modifier = Modifier
                        .padding(bottom = paddingMedium)
                )

                // DISPLAY PRESCRIPTIONS IF AVAILABLE
                if (state.prescriptions.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        items(state.prescriptions) { item ->
                            PrescriptionRow(
                                item = item,
                                onRemovePrescription = onRemovePrescription,
                                onChangePrescription = onChangePrescription
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(paddingMedium))
                }

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth(),
                ) {
                    PrimaryButton(
                        iconImg = Icons.Filled.AddCircle,
                        text = stringResource(R.string.add_medication_button),
                        enabled = canAddPrescription,
                        onClick = onOpenDialog,
                    )
                }
            }
        }

        if (state.isDialogOpen) {
            PrescriptionDialog(
                act = "Neurologie",
                doctor = state.doctor,
                affection = state.affection,
                medication = state.medication,
                posology = state.posology,
                selectedIndex = state.quantityIndex,
                onDoctorChanged = onDoctorChanged,
                onAffectionChanged = onAffectionChanged,
                onPosologyChanged = onPosologyChanged,
                onQuantityChanged = onQuantityChanged,
                onMedicationChanged = onMedicationChanged,
                onAddPrescription = onAddPrescription,
                onDismissRequest = onCloseDialog,
            )
        }

        Spacer(modifier = Modifier.height(paddingMedium))

        PrimaryButton(
            text = stringResource(R.string.confirm_button),
            enabled = isFormValid,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Preview(
    locale = "fr-rCI",
    showBackground = true)
@Composable
fun MedicalPrescriptionContentPreview() {
    ScaInterAppTheme() {
        Surface(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(AppConstants.lightGreen)
                    .padding(dimensionResource(R.dimen.padding_medium))
            ) {
                MedicalPrescriptionContent(
                    state = PrescriptionUiState(
                        prescriptions = listOf<Prescription>(
                            Prescription(
                                name = "Medicament 1",
                                quantityIndex = 1,
                                posology = "",
                            ),
                            Prescription(
                                name = "Medicament 2",
                                quantityIndex = 2,
                                posology = "",
                            )
                        )
                    ),
                    canAddPrescription = true,
                    modifier = Modifier
                        .fillMaxWidth(),
                )
            }
        }
    }
}