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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.nanoporetech.scainter.R
import com.nanoporetech.scainter.conf.AppConstants
import com.nanoporetech.scainter.ui.components.PrimaryButton
import com.nanoporetech.scainter.ui.components.PrimaryOutlinedTextField
import com.nanoporetech.scainter.ui.theme.ScaInterAppTheme

@Composable
fun PrescriptionDialog(
    act: String,
    doctor: String,
    affection: String,
    medication: String,
    selectedIndex: Int,
    posology: String,
    modifier: Modifier = Modifier,
    onDoctorChanged: (String) -> Unit = {},
    onAffectionChanged: (String) -> Unit = {},
    onMedicationChanged: (String) -> Unit = {},
    onQuantityChanged: (Int) -> Unit = {},
    onPosologyChanged: (String) -> Unit = {},
    onDismissRequest: () -> Unit = {},
    onAddPrescription: () -> Unit = {}
) {
    val paddingMedium = dimensionResource(R.dimen.padding_medium)
    val paddingSmall = dimensionResource(R.dimen.padding_small)

    Dialog(
        onDismissRequest = onDismissRequest,
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
            modifier = modifier
        ) {
            Box() {
                Column(
                    modifier = Modifier
                        .padding(paddingMedium)
                        .fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.height(paddingMedium))

                    // SECTION TITLE
                    Text(
                        text = stringResource(R.string.consultation_details_sub),
                        color = AppConstants.mainGreen,
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 18.sp
                    )

                    Spacer(modifier = Modifier.height(paddingSmall))

                    // ACT
                    Row {
                        Text(
                            text = stringResource(R.string.act_label),
                            color = AppConstants.mainGreen,
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.weight(1.0f))
                        Text(text = act)
                    }

                    // DOCTOR
                    PrimaryOutlinedTextField(
                        text = doctor,
                        placeholder = stringResource(R.string.prescriber_hint),
                        onValueChanged = onDoctorChanged,
                        modifier = Modifier.fillMaxWidth()
                    )

                    // CODE AFFECTION
                    PrimaryOutlinedTextField(
                        text = affection,
                        placeholder = stringResource(R.string.affection_hint),
                        onValueChanged = onAffectionChanged,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(paddingMedium))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(paddingMedium))

                    // PRESCRIPTION
                    Text(
                        text = stringResource(R.string.medical_prescription_title),
                        color = AppConstants.mainGreen,
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Spacer(modifier = Modifier.height(paddingSmall))

                    // MEDICATION
                    PrimaryOutlinedTextField(
                        text = medication,
                        placeholder = stringResource(R.string.medication_hint),
                        onValueChanged = onMedicationChanged,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(paddingSmall))

                    // QUANTITY
                    QuantityPicker(
                        selectedIndex = selectedIndex,
                        onQuantityChanged = onQuantityChanged,
                        modifier = Modifier.fillMaxWidth()
                    )

                    // POSOLOGY
                    PrimaryOutlinedTextField(
                        text = posology,
                        placeholder = stringResource(R.string.posology_hint),
                        onValueChanged = onPosologyChanged,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(paddingMedium))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(paddingMedium))

                    // ADD BUTTON
                    PrimaryButton(
                        iconImg = Icons.Filled.AddCircle,
                        text = stringResource(R.string.add_button),
                        onClick = onAddPrescription,
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

@Preview(
    locale = "fr-rCI",
    showBackground = true)
@Composable
fun PrescriptionDialogPreview() {
    ScaInterAppTheme() {
        Surface(
            modifier = Modifier
                .fillMaxSize()
        ) {
            PrescriptionDialog(
                act = "Neurologie",
                doctor = "",
                affection = "",
                medication = "",
                posology = "",
                selectedIndex = 0
            )
        }
    }
}