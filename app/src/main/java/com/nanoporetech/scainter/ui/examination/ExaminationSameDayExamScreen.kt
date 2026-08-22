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
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nanoporetech.scainter.R
import com.nanoporetech.scainter.conf.AppConstants
import com.nanoporetech.scainter.ui.components.CardHeader
import com.nanoporetech.scainter.ui.components.CostsFragment
import com.nanoporetech.scainter.ui.components.PrimaryButton
import com.nanoporetech.scainter.ui.components.PrimaryOutlinedTextField
import com.nanoporetech.scainter.ui.theme.ScaInterAppTheme
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ExaminationSameDayExamScreen(
    reason: String = "",
    designation: String = "",
    costTotal: String = "",
    costSca: String = "",
    costUser: String = "",
    isSubmitting: Boolean = false,
    onReasonChanged: (String) -> Unit = {},
    onDesignationChanged: (String) -> Unit = {},
    onCostChanged: (String) -> Unit = {},
    onSubmitRequest: () -> Unit = {},
    modifier: Modifier
) {
    val paddingMedium = dimensionResource(R.dimen.padding_medium)
    val isFormValid = reason.isNotBlank() && designation.isNotBlank() && costTotal.isNotBlank()

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
    ) {
        // CARE SECTION
        CareSection(
            reason = reason,
            designation = designation,
            costTotal = costTotal,
            onReasonChanged = onReasonChanged,
            onDesignationChanged = onDesignationChanged,
            onCostChanged = onCostChanged,
            onSubmitRequest = onSubmitRequest,
            isFormValid = isFormValid,
            isSubmitting = isSubmitting,
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(paddingMedium))

        // COSTS SECTION
        CostsFragment(
            costTotal = costTotal,
            costSca = costSca,
            costUser = costUser,
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(paddingMedium))

        // SEND BUTTON
        PrimaryButton(
            iconImg = Icons.AutoMirrored.Filled.Send,
            text = stringResource(R.string.send_button),
            onClick = onSubmitRequest,
            enabled = isFormValid && !isSubmitting,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Composable
fun CareSection(
    reason: String,
    designation: String,
    costTotal: String,
    isFormValid: Boolean,
    isSubmitting: Boolean,
    modifier: Modifier = Modifier,
    onReasonChanged: (String) -> Unit,
    onDesignationChanged: (String) -> Unit,
    onCostChanged: (String) -> Unit,
    onSubmitRequest: () -> Unit,
) {
    val paddingMedium = dimensionResource(R.dimen.padding_medium)
    val paddingSmall = dimensionResource(R.dimen.padding_small)
    val paddingExtraSmall = dimensionResource(R.dimen.padding_xsmall)
    val focusManager = LocalFocusManager.current

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
        elevation = CardDefaults.cardElevation(defaultElevation = dimensionResource(R.dimen.elevation_small)),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .padding(paddingMedium)
        ) {
            CardHeader(
                title = stringResource(R.string.examination_same_day_title),
                iconImg = Icons.AutoMirrored.Filled.Assignment,
                modifier = Modifier
                    .padding(bottom = paddingSmall)
            )

            Spacer(modifier = Modifier.height(paddingSmall))

            Text(
                text = stringResource(R.string.exam_same_day_details),
                color = AppConstants.mainGreen,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(paddingSmall))

            // MOTIF DES SOINS

            PrimaryOutlinedTextField(
                value = reason,
                placeholder = stringResource(R.string.exam_reason_hint),
                onValueChanged = onReasonChanged,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Next)
                    }
                ),
            )

            Spacer(modifier = Modifier.height(paddingExtraSmall))

            // DESIGNATION

            PrimaryOutlinedTextField(
                value = designation,
                placeholder = stringResource(R.string.exam_designatinon_hint),
                onValueChanged = onDesignationChanged,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Next)
                    }
                ),
            )

            Spacer(modifier = Modifier.height(paddingExtraSmall))

            // COST

            PrimaryOutlinedTextField(
                value = costTotal,
                placeholder = stringResource(R.string.exam_cost_hint),
                onValueChanged = { value ->
                    if (value.all { it.isDigit() }) {
                        onCostChanged(value)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (isFormValid && !isSubmitting) {
                            focusManager.clearFocus()
                            onSubmitRequest()
                        }
                    }
                ),
            )
        }
    }
}

@Preview(
    locale = "fr-rCI",
    showBackground = true,
)
@Composable
fun ExaminationSameDayExamScreenPreview() {
    ScaInterAppTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
        ) {
            ExaminationSameDayExamScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = AppConstants.lightGreen)
                    .padding(dimensionResource(R.dimen.padding_medium))
            )
        }
    }
}
