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
        onPosology1Changed = viewModel::setPosology1,
        onQuantity1Changed = viewModel::setQuantity1,
        onMedication1Changed = viewModel::setMedication1,
        onPosology2Changed = viewModel::setPosology2,
        onQuantity2Changed = viewModel::setQuantity2,
        onMedication2Changed = viewModel::setMedication2,
        onPosology3Changed = viewModel::setPosology3,
        onQuantity3Changed = viewModel::setQuantity3,
        onMedication3Changed = viewModel::setMedication3,
        onPosology4Changed = viewModel::setPosology4,
        onQuantity4Changed = viewModel::setQuantity4,
        onMedication4Changed = viewModel::setMedication4,
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
    onPosology1Changed: (String) -> Unit = {},
    onQuantity1Changed: (Int) -> Unit = {},
    onMedication1Changed: (String) -> Unit = {},
    onPosology2Changed: (String) -> Unit = {},
    onQuantity2Changed: (Int) -> Unit = {},
    onMedication2Changed: (String) -> Unit = {},
    onPosology3Changed: (String) -> Unit = {},
    onQuantity3Changed: (Int) -> Unit = {},
    onMedication3Changed: (String) -> Unit = {},
    onPosology4Changed: (String) -> Unit = {},
    onQuantity4Changed: (Int) -> Unit = {},
    onMedication4Changed: (String) -> Unit = {},
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
                        text = stringResource(R.string.add_prescription_button),
                        enabled = canAddPrescription,
                        onClick = onOpenDialog,
                    )
                }
            }
        }

        if (state.isDialogOpen) {
            PrescriptionDialog(
                act = "Neurology",
                doctor = state.doctor,
                affection = state.affection,
                medication1 = state.medication1,
                posology1 = state.posology1,
                selectedIndex1 = state.quantityIndex1,
                medication2 = state.medication2,
                posology2 = state.posology2,
                selectedIndex2 = state.quantityIndex2,
                medication3 = state.medication3,
                posology3 = state.posology3,
                selectedIndex3 = state.quantityIndex3,
                medication4 = state.medication4,
                posology4 = state.posology4,
                selectedIndex4 = state.quantityIndex4,
                onDoctorChanged = onDoctorChanged,
                onAffectionChanged = onAffectionChanged,
                onPosology1Changed = onPosology1Changed,
                onQuantity1Changed = onQuantity1Changed,
                onMedication1Changed = onMedication1Changed,
                onPosology2Changed = onPosology2Changed,
                onQuantity2Changed = onQuantity2Changed,
                onMedication2Changed = onMedication2Changed,
                onPosology3Changed = onPosology3Changed,
                onQuantity3Changed = onQuantity3Changed,
                onMedication3Changed = onMedication3Changed,
                onPosology4Changed = onPosology4Changed,
                onQuantity4Changed = onQuantity4Changed,
                onMedication4Changed = onMedication4Changed,
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
    ScaInterAppTheme {
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
                        prescriptions = listOf(
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