package com.nanoporetech.scainter.ui.consultation

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.nanoporetech.scainter.R
import com.nanoporetech.scainter.conf.AppConstants
import com.nanoporetech.scainter.data.Medicine
import com.nanoporetech.scainter.ui.components.PredictiveTextField
import com.nanoporetech.scainter.ui.components.PrimaryButton
import com.nanoporetech.scainter.ui.components.PrimaryOutlinedTextField
import com.nanoporetech.scainter.ui.theme.ScaInterAppTheme

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun PrescriptionDialog(
    act: String,
    doctor: String,
    affection: String,
    medication1: String,
    selectedIndex1: Int,
    posology1: String,
    medication2: String,
    selectedIndex2: Int,
    posology2: String,
    medication3: String,
    selectedIndex3: Int,
    posology3: String,
    medication4: String,
    selectedIndex4: Int,
    posology4: String,
    modifier: Modifier = Modifier,
    onDoctorChanged: (String) -> Unit = {},
    onAffectionChanged: (String) -> Unit = {},
    onMedication1Changed: (String) -> Unit = {},
    onQuantity1Changed: (Int) -> Unit = {},
    onPosology1Changed: (String) -> Unit = {},
    onMedication2Changed: (String) -> Unit = {},
    onQuantity2Changed: (Int) -> Unit = {},
    onPosology2Changed: (String) -> Unit = {},
    onMedication3Changed: (String) -> Unit = {},
    onQuantity3Changed: (Int) -> Unit = {},
    onPosology3Changed: (String) -> Unit = {},
    onMedication4Changed: (String) -> Unit = {},
    onQuantity4Changed: (Int) -> Unit = {},
    onPosology4Changed: (String) -> Unit = {},
    onDismissRequest: () -> Unit = {},
    onAddPrescription: () -> Unit = {}
) {
    val paddingMedium = dimensionResource(R.dimen.padding_medium)
    val paddingSmall = dimensionResource(R.dimen.padding_small)
    val paddingLarge = dimensionResource(R.dimen.padding_large)
    val focusManager = LocalFocusManager.current

    Dialog(
        onDismissRequest = onDismissRequest,
    ) {
        BoxWithConstraints {
            val scrollState = rememberScrollState()
            val maxDialogHeight = maxHeight * 0.9f

            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
                modifier = modifier
                    .fillMaxWidth()
                    .heightIn(max = maxDialogHeight)
            ) {
                Box {
                    Column(
                        modifier = Modifier
                            .padding(paddingMedium)
                            .fillMaxWidth()
                            .verticalScroll(scrollState)
                    ) {
                        Spacer(modifier = Modifier.height(paddingLarge))

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
                                text = act,
                                color = AppConstants.mainGreen,
                            )
                        }

                        // DOCTOR
                        PrimaryOutlinedTextField(
                            value = doctor,
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
                            value = affection,
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
                            value = medication1,
                            placeholder = stringResource(R.string.medication_hint),
                            suggestions = Medicine.load(context = LocalContext.current),
                            onValueChanged = onMedication1Changed,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(paddingSmall))

                        // QUANTITY
                        QuantityDropdown(
                            quantities = listOf(1, 2, 3, 4),
                            selectedIndex = selectedIndex1,
                            onQuantityChanged = onQuantity1Changed,
                            modifier = Modifier.fillMaxWidth()
                        )

                        // POSOLOGY
                        PrimaryOutlinedTextField(
                            value = posology1,
                            placeholder = stringResource(R.string.posology_hint),
                            onValueChanged = onPosology1Changed,
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Done,
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = { onAddPrescription() }
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
                            value = medication2,
                            placeholder = stringResource(R.string.medication_hint),
                            suggestions = Medicine.load(context = LocalContext.current),
                            onValueChanged = onMedication2Changed,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(paddingSmall))

                        // QUANTITY
                        QuantityDropdown(
                            quantities = listOf(1, 2, 3, 4),
                            selectedIndex = selectedIndex2,
                            onQuantityChanged = onQuantity2Changed,
                            modifier = Modifier.fillMaxWidth()
                        )

                        // POSOLOGY
                        PrimaryOutlinedTextField(
                            value = posology2,
                            placeholder = stringResource(R.string.posology_hint),
                            onValueChanged = onPosology2Changed,
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Done,
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = { onAddPrescription() }
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
                            value = medication3,
                            placeholder = stringResource(R.string.medication_hint),
                            suggestions = Medicine.load(context = LocalContext.current),
                            onValueChanged = onMedication3Changed,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(paddingSmall))

                        // QUANTITY
                        QuantityDropdown(
                            quantities = listOf(1, 2, 3, 4),
                            selectedIndex = selectedIndex3,
                            onQuantityChanged = onQuantity3Changed,
                            modifier = Modifier.fillMaxWidth()
                        )

                        // POSOLOGY
                        PrimaryOutlinedTextField(
                            value = posology3,
                            placeholder = stringResource(R.string.posology_hint),
                            onValueChanged = onPosology3Changed,
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Done,
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = { onAddPrescription() }
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
                            value = medication4,
                            placeholder = stringResource(R.string.medication_hint),
                            suggestions = Medicine.load(context = LocalContext.current),
                            onValueChanged = onMedication4Changed,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(paddingSmall))

                        // QUANTITY
                        QuantityDropdown(
                            quantities = listOf(1, 2, 3, 4),
                            selectedIndex = selectedIndex4,
                            onQuantityChanged = onQuantity4Changed,
                            modifier = Modifier.fillMaxWidth()
                        )

                        // POSOLOGY
                        PrimaryOutlinedTextField(
                            value = posology4,
                            placeholder = stringResource(R.string.posology_hint),
                            onValueChanged = onPosology4Changed,
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Done,
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = { onAddPrescription() }
                            ),
                        )

                        Spacer(modifier = Modifier.height(paddingMedium))
                        HorizontalDivider()
                        Spacer(modifier = Modifier.height(paddingMedium))

                        // ADD BUTTON
                        PrimaryButton(
                            iconImg = Icons.Filled.AddCircle,
                            text = stringResource(R.string.add_button),
                            onClick = onAddPrescription,
                            enabled = doctor.isNotBlank() &&
                                    affection.isNotBlank() &&
                                    medication1.isNotBlank(),
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }

                    // DISMISS BUTTON
                    IconButton(
                        onClick = onDismissRequest,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .clip(CircleShape)
                            .background(
                                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f)
                            )
                            .size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = stringResource(R.string.close_button),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuantityPicker(
    selectedIndex: Int,
    onQuantityChanged: (Int) -> Unit = {},
    modifier: Modifier
) {
    val options = listOf("1", "2", "3", "4")

    SingleChoiceSegmentedButtonRow(
        modifier
    ) {
        options.forEachIndexed { index, label ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = options.size
                ),
                onClick = { onQuantityChanged(index) },
                selected = index == selectedIndex,
                label = { Text(label) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuantityDropdown(
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
    showBackground = true)
@Composable
fun PrescriptionDialogPreview() {
    ScaInterAppTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
        ) {
            PrescriptionDialog(
                act = "Neurology",
                doctor = "",
                affection = "",
                medication1 = "",
                posology1 = "",
                selectedIndex1 = 0,
                medication2 = "",
                posology2 = "",
                selectedIndex2 = 0,
                medication3 = "",
                posology3 = "",
                selectedIndex3 = 0,
                medication4 = "",
                posology4 = "",
                selectedIndex4 = 0,
            )
        }
    }
}
