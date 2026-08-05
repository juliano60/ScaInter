package com.nanoporetech.scainter.ui.consultation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nanoporetech.scainter.R
import com.nanoporetech.scainter.conf.AppConstants
import com.nanoporetech.scainter.data.Medicine
import com.nanoporetech.scainter.data.PrescriptionUiState
import com.nanoporetech.scainter.ui.components.CardHeader
import com.nanoporetech.scainter.ui.components.ConfirmationDialog
import com.nanoporetech.scainter.ui.components.PredictiveTextField
import com.nanoporetech.scainter.ui.components.PrimaryButton
import com.nanoporetech.scainter.ui.components.PrimaryOutlinedTextField
import com.nanoporetech.scainter.ui.events.UiEvent
import com.nanoporetech.scainter.ui.theme.ScaInterAppTheme
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ConsultationNewPrescriptionScreen(
    consultationId: String,
    modifier: Modifier = Modifier,
    onSubmitSuccess: () -> Unit = {},
    onSubmitError: (Int) -> Unit = {},
    viewModel: MedicalPrescriptionViewModel = viewModel(
        factory = MedicalPrescriptionViewModel.provideFactory(
            consultationId = consultationId
        )
    )
)
{
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // convert from model.events to nav_result
    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is UiEvent.Success -> {
                    onSubmitSuccess()
                }
                is UiEvent.Error -> {
                    onSubmitError(event.errorId)
                }
            }
        }
    }

    ConsultationNewPrescriptionContent(
        state = uiState,
        modifier = modifier,
        onDoctorChanged = viewModel::setDoctor,
        onAffectionChanged = viewModel::setAffection,
        onMedicationChanged = viewModel::setMedication,
        onQuantityChanged = viewModel::setQuantity,
        onPosologyChanged = viewModel::setPosology,
        onMedication1Changed = viewModel::setMedication1,
        onQuantity1Changed = viewModel::setQuantity1,
        onPosology1Changed = viewModel::setPosology1,
        onMedication2Changed = viewModel::setMedication2,
        onQuantity2Changed = viewModel::setQuantity2,
        onPosology2Changed = viewModel::setPosology2,
        onMedication3Changed = viewModel::setMedication3,
        onQuantity3Changed = viewModel::setQuantity3,
        onPosology3Changed = viewModel::setPosology3,
        onSendPrescription = viewModel::addPrescription
    )
}

@Composable
fun ConsultationNewPrescriptionContent(
    state: PrescriptionUiState,
    modifier: Modifier = Modifier,
    onDoctorChanged: (String) -> Unit = {},
    onAffectionChanged: (String) -> Unit = {},
    onMedicationChanged: (String) -> Unit = {},
    onQuantityChanged: (Int) -> Unit = {},
    onPosologyChanged: (String) -> Unit = {},
    onMedication1Changed: (String) -> Unit = {},
    onQuantity1Changed: (Int) -> Unit = {},
    onPosology1Changed: (String) -> Unit = {},
    onMedication2Changed: (String) -> Unit = {},
    onQuantity2Changed: (Int) -> Unit = {},
    onPosology2Changed: (String) -> Unit = {},
    onMedication3Changed: (String) -> Unit = {},
    onQuantity3Changed: (Int) -> Unit = {},
    onPosology3Changed: (String) -> Unit = {},
    onSendPrescription: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .verticalScroll(
                rememberScrollState()
            )
    ) {
        val paddingMedium = dimensionResource(R.dimen.padding_medium)
        val paddingSmall = dimensionResource(R.dimen.padding_small)
        val focusManager = LocalFocusManager.current
        var showConfirmationPrompt by remember { mutableStateOf(false) }
        val isFormValid = state.doctor.isNotBlank() && state.affection.isNotBlank() &&
                state.medication.isNotBlank()

        fun requestConfirmation() {
            if (isFormValid) {
                focusManager.clearFocus()
                showConfirmationPrompt = true
            }
        }

        if (showConfirmationPrompt) {
            ConfirmationDialog(
                title = stringResource(R.string.confirmation_generic_title),
                message = stringResource(R.string.new_prescription_confirmation),
                onConfirm = {
                    showConfirmationPrompt = false
                    onSendPrescription()
                },
                onDismiss = {
                    showConfirmationPrompt = false
                },
            )
        }

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
                    title = stringResource(R.string.medical_prescription_title),
                    iconImg = Icons.AutoMirrored.Filled.Assignment,
                    modifier = Modifier
                        .padding(bottom = paddingMedium)
                )

                Spacer(modifier = Modifier.height(paddingMedium))

                // ACT
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.act_label),
                        color = Color.Black,
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .padding(end = paddingSmall)
                    )
                    Text(
                        text = "Neurologie",
                        color = AppConstants.mainGreen,
                    )
                }

                // DOCTOR
                PrimaryOutlinedTextField(
                    value = state.doctor,
                    placeholder = stringResource(R.string.prescriber_hint),
                    onValueChanged = onDoctorChanged,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                )

                // CODE AFFECTION
                PrimaryOutlinedTextField(
                    value = state.affection,
                    placeholder = stringResource(R.string.affection_hint),
                    onValueChanged = onAffectionChanged,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                )

                Spacer(modifier = Modifier.height(paddingMedium))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(paddingMedium))

                // MEDICATION #1
                Text(
                    text = stringResource(R.string.medication_1_title),
                    color = AppConstants.mainGreen,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(paddingSmall))

                PredictiveTextField(
                    value = state.medication,
                    placeholder = stringResource(R.string.medication_hint),
                    suggestions = Medicine.load(context = LocalContext.current),
                    onValueChanged = onMedicationChanged,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(paddingSmall))

                // QUANTITY
                QuantityDropdown(
                    quantities = listOf(1, 2, 3, 4),
                    selectedIndex = state.quantityIndex,
                    onQuantityChanged = onQuantityChanged,
                    modifier = Modifier.fillMaxWidth()
                )

                // POSOLOGY
                PrimaryOutlinedTextField(
                    value = state.posology,
                    placeholder = stringResource(R.string.posology_hint),
                    onValueChanged = onPosologyChanged,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done,
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { onSendPrescription() }
                    ),
                )

                Spacer(modifier = Modifier.height(paddingMedium))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(paddingMedium))

                // MEDICATION #2
                Text(
                    text = stringResource(R.string.medication_2_title),
                    color = AppConstants.mainGreen,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(paddingSmall))

                PredictiveTextField(
                    value = state.medication1,
                    placeholder = stringResource(R.string.medication_hint),
                    suggestions = Medicine.load(context = LocalContext.current),
                    onValueChanged = onMedication1Changed,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(paddingSmall))

                // QUANTITY
                QuantityDropdown(
                    quantities = listOf(1, 2, 3, 4),
                    selectedIndex = state.quantityIndex1,
                    onQuantityChanged = onQuantity1Changed,
                    modifier = Modifier.fillMaxWidth()
                )

                // POSOLOGY
                PrimaryOutlinedTextField(
                    value = state.posology1,
                    placeholder = stringResource(R.string.posology_hint),
                    onValueChanged = onPosology1Changed,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done,
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { onSendPrescription() }
                    ),
                )

                Spacer(modifier = Modifier.height(paddingMedium))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(paddingMedium))

                // MEDICATION #3
                Text(
                    text = stringResource(R.string.medication_3_title),
                    color = AppConstants.mainGreen,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(paddingSmall))

                PredictiveTextField(
                    value = state.medication2,
                    placeholder = stringResource(R.string.medication_hint),
                    suggestions = Medicine.load(context = LocalContext.current),
                    onValueChanged = onMedication2Changed,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(paddingSmall))

                // QUANTITY
                QuantityDropdown(
                    quantities = listOf(1, 2, 3, 4),
                    selectedIndex = state.quantityIndex2,
                    onQuantityChanged = onQuantity2Changed,
                    modifier = Modifier.fillMaxWidth()
                )

                // POSOLOGY
                PrimaryOutlinedTextField(
                    value = state.posology2,
                    placeholder = stringResource(R.string.posology_hint),
                    onValueChanged = onPosology2Changed,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done,
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { onSendPrescription() }
                    ),
                )

                Spacer(modifier = Modifier.height(paddingMedium))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(paddingMedium))

                // MEDICATION #4
                Text(
                    text = stringResource(R.string.medication_4_title),
                    color = AppConstants.mainGreen,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(paddingSmall))

                PredictiveTextField(
                    value = state.medication3,
                    placeholder = stringResource(R.string.medication_hint),
                    suggestions = Medicine.load(context = LocalContext.current),
                    onValueChanged = onMedication3Changed,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(paddingSmall))

                // QUANTITY
                QuantityDropdown(
                    quantities = listOf(1, 2, 3, 4),
                    selectedIndex = state.quantityIndex3,
                    onQuantityChanged = onQuantity3Changed,
                    modifier = Modifier.fillMaxWidth()
                )

                // POSOLOGY
                PrimaryOutlinedTextField(
                    value = state.posology3,
                    placeholder = stringResource(R.string.posology_hint),
                    onValueChanged = onPosology3Changed,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done,
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            requestConfirmation()
                        }
                    ),
                )
            }
        }

        Spacer(modifier = Modifier.height(paddingMedium))

        // SEND BUTTON
        PrimaryButton(
            iconImg = Icons.AutoMirrored.Filled.Send,
            text = stringResource(R.string.send_button),
            onClick = ::requestConfirmation,
            enabled = isFormValid,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun QuantityDropdown(
    quantities: List<Int>,
    selectedIndex: Int,
    onQuantityChanged: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = quantities[selectedIndex].toString(),
            onValueChange = {},
            readOnly = true,
            label = { Text("Quantity") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded)
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            quantities.forEachIndexed { index, quantity ->
                DropdownMenuItem(
                    text = { Text(quantity.toString()) },
                    onClick = {
                        onQuantityChanged(index)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Preview(
    locale = "fr-rCI",
    showBackground = true
)
@Composable
fun ConsultationNewPrescriptionScreenPreview() {
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
                ConsultationNewPrescriptionContent(
                    state = PrescriptionUiState(),
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
    }
}
