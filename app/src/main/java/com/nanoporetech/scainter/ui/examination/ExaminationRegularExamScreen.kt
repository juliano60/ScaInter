package com.nanoporetech.scainter.ui.examination

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isShiftPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import com.nanoporetech.scainter.R
import com.nanoporetech.scainter.conf.AppConstants
import com.nanoporetech.scainter.data.RegularExamUiState
import com.nanoporetech.scainter.model.ExamOption
import com.nanoporetech.scainter.ui.components.CardHeader
import com.nanoporetech.scainter.ui.components.PrimaryButton
import com.nanoporetech.scainter.ui.components.PrimaryOutlinedTextField
import com.nanoporetech.scainter.ui.theme.ScaInterAppTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExaminationRegularExamScreen(
    state: RegularExamUiState,
    modifier: Modifier = Modifier,
    onDoctorChanged: (String) -> Unit = {},
    onSpecialtyChanged: (String) -> Unit = {},
    onReasonChanged: (String) -> Unit = {},
    onExaminationSelected: (Int, ExamOption) -> Unit = { index,exam ->},
    onRequestExamination: () -> Unit = {}
) {
    val paddingMedium = dimensionResource(R.dimen.padding_medium)
    val paddingSmall = dimensionResource(R.dimen.padding_small)
    val focusManager = LocalFocusManager.current
    var expandedIndex by rememberSaveable { mutableStateOf<Int?>(null) }
    val examHints = listOf(
        R.string.exam_1_hint,
        R.string.exam_2_hint,
        R.string.exam_3_hint,
        R.string.exam_4_hint,
        R.string.exam_5_hint,
        R.string.exam_6_hint,
        R.string.exam_7_hint,
        R.string.exam_8_hint,
    )

    val isFormValid = state.doctor.isNotBlank() && state.reason.isNotBlank() &&
            state.selectedExaminations.first().name.isNotBlank()

    Column(
        modifier = modifier
            .onPreviewKeyEvent { event ->
                if (event.type == KeyEventType.KeyDown && event.key == Key.Tab) {
                    focusManager.moveFocus(
                        if (event.isShiftPressed) {
                            FocusDirection.Previous
                        } else {
                            FocusDirection.Next
                        }
                    )
                } else {
                    false
                }
            }
            .verticalScroll(
                rememberScrollState()
            )
    ) {
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
                    title = stringResource(R.string.exam_request_title),
                    iconImg = Icons.Filled.EditNote,
                    modifier = Modifier
                        .padding(bottom = paddingMedium)
                )

                // DOCTOR
                PrimaryOutlinedTextField(
                    value = state.doctor,
                    placeholder = stringResource(R.string.exam_doctor_hint),
                    onValueChanged = onDoctorChanged,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                )

                // SPECIALTY
                PrimaryOutlinedTextField(
                    value = state.specialty,
                    placeholder = stringResource(R.string.exam_specialty_hint),
                    onValueChanged = onSpecialtyChanged,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                )

                // REASON FOR CARE
                PrimaryOutlinedTextField(
                    value = state.reason,
                    placeholder = stringResource(R.string.exam_reg_reason_hint),
                    onValueChanged = onReasonChanged,
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

                // Examination list
                state.selectedExaminations.forEachIndexed { index, selectedExamination ->
                    Text(
                        text = stringResource(examHints[index]),
                        color = AppConstants.mainGreen,
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Spacer(modifier = Modifier.height(paddingSmall))

                    ExposedDropdownMenuBox(
                        expanded = expandedIndex == index ,
                        onExpandedChange = {
                            expandedIndex = if (expandedIndex == index) null else index
                        },
                    ) {
                        OutlinedTextField(
                            value = selectedExamination.name,
                            readOnly = true,
                            onValueChange = {},
                            label = {
                                Text(
                                    text = stringResource(R.string.exam_select_exam_hint)
                                )
                            },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(
                                    expanded = expandedIndex == index
                                )
                            },
                            modifier = Modifier
                                .menuAnchor(MenuAnchorType.PrimaryNotEditable, enabled = true)
                                .fillMaxWidth()
                        )

                        ExposedDropdownMenu(
                            expanded = expandedIndex == index,
                            onDismissRequest = { expandedIndex = null }
                        ) {
                            state.examOptions.forEach { examination ->
                                DropdownMenuItem(
                                    text = { Text(text = examination.name) },
                                    onClick = {
                                        onExaminationSelected(index, examination)
                                        expandedIndex = null
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(paddingMedium))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(paddingMedium))
                }
            }
        }

        Spacer(modifier = Modifier.height(paddingMedium))

        // SEND BUTTON
        PrimaryButton(
            iconImg = Icons.AutoMirrored.Filled.Send,
            text = stringResource(R.string.send_button),
            onClick = onRequestExamination,
            enabled = isFormValid,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Preview(
    locale = "fr-rCI",
    showBackground = true,
)
@Composable
fun ExaminationRegularExamScreenPreview() {
    ScaInterAppTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
        ) {
            ExaminationRegularExamScreen(
                state = RegularExamUiState(),
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = AppConstants.lightGreen)
                    .padding(dimensionResource(R.dimen.padding_medium))
            )
        }
    }
}