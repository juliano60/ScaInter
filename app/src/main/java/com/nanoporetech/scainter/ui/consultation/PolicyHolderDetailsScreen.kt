package com.nanoporetech.scainter.ui.consultation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.MedicalInformation
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nanoporetech.scainter.R
import com.nanoporetech.scainter.conf.AppConstants
import com.nanoporetech.scainter.data.DataSource
import com.nanoporetech.scainter.model.PolicyHolder
import com.nanoporetech.scainter.model.imageUrl
import com.nanoporetech.scainter.model.providerDisplayedName
import com.nanoporetech.scainter.ui.components.CardBody
import com.nanoporetech.scainter.ui.components.CardHeader
import com.nanoporetech.scainter.ui.components.CardItem
import com.nanoporetech.scainter.ui.components.PolicyHolderInfo
import com.nanoporetech.scainter.ui.components.PrimaryButton
import com.nanoporetech.scainter.ui.theme.ScaInterAppTheme
import com.nanoporetech.scainter.ui.utils.displayedDate

@Composable
fun PolicyHolderDetailsScreen(
    policyHolder: PolicyHolder,
    modifier: Modifier = Modifier,
    selectedConsultation: String,
    selectedCost: String,
    onConsultationSelected: (String) -> Unit = {},
    onCostSelected: (String) -> Unit = {}
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
    ) {
        // SUBSCRIBER INFO
        PolicyHolderInfo(
            name = policyHolder.fullname,
            internalId = policyHolder.internalId,
            dateOfBirth = policyHolder.dateOfBirth,
            subscriberName = policyHolder.subscriberName,
            contractType = policyHolder.contractType,
            imageUrl = policyHolder.imageUrl,
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_medium)))

        // INSURANCE INFO
        InsuranceInfo(
            policyHolder = policyHolder,
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_medium)))

        // OPTIONS
        OptionsInfo(
            policyHolder = policyHolder,
            selectedConsultation = selectedConsultation,
            selectedCost = selectedCost,
            onConsultationSelected = onConsultationSelected,
            onCostSelected = onCostSelected,
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_medium)))
        Spacer(modifier = Modifier.weight(1.0f))

        PrimaryButton(
            text = stringResource(R.string.confirm_button),
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OptionsInfo(
    policyHolder: PolicyHolder,
    selectedConsultation: String,
    selectedCost: String,
    modifier: Modifier = Modifier,
    onConsultationSelected: (String) -> Unit = {},
    onCostSelected: (String) -> Unit = {}
) {
    val paddingMedium = dimensionResource(R.dimen.padding_medium)
    var consultationExpanded by rememberSaveable() { mutableStateOf(false) }
    var costExpanded by rememberSaveable() { mutableStateOf(false) }

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
                title = stringResource(R.string.options_title),
                iconImg = Icons.Filled.MedicalInformation,
                modifier = Modifier
                    .padding(bottom = paddingMedium)
            )

            // CONSUMPTION LIMIT REACHED
            if (policyHolder.isConsommationLimitReached) {
                Text(
                    text = stringResource(R.string.consumption_limit_reached_message),
                    color = MaterialTheme.colorScheme.error,
                )
            }
            // STATUS DORMANT OR INACTIVE
            else if (policyHolder.isStatusInactive) {
                Text(
                    text = stringResource(R.string.status_inactive_message),
                    color = MaterialTheme.colorScheme.error,
                )
            }
            else {
                // CONSULTATION TYPES
                ExposedDropdownMenuBox(
                    expanded = consultationExpanded,
                    onExpandedChange = { consultationExpanded = !consultationExpanded },
                ) {
                    OutlinedTextField(
                        value = selectedConsultation,
                        readOnly = true,
                        onValueChange = {},
                        label = {
                            Text(
                                text = stringResource(R.string.consultation_label)
                            )
                        },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = consultationExpanded)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = consultationExpanded,
                        onDismissRequest = { consultationExpanded = false }
                    ) {
                        policyHolder.consultationList.forEach { consultation ->
                            DropdownMenuItem(
                                text = { Text(text = consultation) },
                                onClick = {
                                    onConsultationSelected(consultation)
                                    consultationExpanded = false
                                }
                            )
                        }
                    }
                }

                // COSTS
                ExposedDropdownMenuBox(
                    expanded = costExpanded,
                    onExpandedChange = { costExpanded = !costExpanded },
                ) {
                    OutlinedTextField(
                        value = selectedCost,
                        readOnly = true,
                        onValueChange = {},
                        label = {
                            Text(
                                text = stringResource(R.string.cost_label)
                            )
                        },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = costExpanded)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = costExpanded,
                        onDismissRequest = { costExpanded = false }
                    ) {
                        policyHolder.consultationList.forEach { cost ->
                            DropdownMenuItem(
                                text = { Text(text = cost) },
                                onClick = {
                                    onCostSelected(cost)
                                    costExpanded = false
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun getStatusColor(status: String) = when(status) {
    "Actif" -> Color(0xFF19bd04)  // green
    "Non-Actif" -> MaterialTheme.colorScheme.error
    else -> MaterialTheme.colorScheme.onPrimaryContainer
}

@Composable
fun InsuranceInfo(
    policyHolder: PolicyHolder,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        CardItem(
            label = stringResource(R.string.policy_status_label),
            value = policyHolder.insuranceStatus,
            valueColor = getStatusColor(policyHolder.insuranceStatus)
        ),
        CardItem(
            label = stringResource(R.string.subscriber_status_label),
            value = policyHolder.providerStatus,
            valueColor = getStatusColor(policyHolder.providerStatus)
        ),
        CardItem(
            stringResource(R.string.coverage_label),
            policyHolder.coverExternal
        ),
        CardItem(
            stringResource(R.string.last_consultation_label),
            displayedDate(policyHolder.lastConsultationDate)
        ),
        CardItem(
            stringResource(R.string.type_label),
            policyHolder.lastConsultationType
        ),
        CardItem(
            stringResource(R.string.provider_label),
            policyHolder.providerDisplayedName
        )
    )

    val paddingMedium = dimensionResource(R.dimen.padding_medium)

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
                title = stringResource(R.string.policy_status_title),
                iconImg = Icons.Filled.AttachFile,
                modifier = Modifier
                    .padding(bottom = paddingMedium)
            )

            CardBody(
                items = items,
                firstColumnWeight = 0.8f,
                secondColumnWeight = 0.6f
            )
        }
    }
}

@Preview(
    locale = "fr-rCI",
    showBackground = true,
)
@Composable
fun PolicyHolderDetailsContentPreview() {
    ScaInterAppTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
        ) {
            PolicyHolderDetailsScreen(
                policyHolder =
                    DataSource.policyHolders().first(),
                    //DataSource.policyHolders()[1],  // consumption limit reached
                    //DataSource.policyHolders()[2],  // status inactive
                selectedConsultation = "",
                selectedCost = "",
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = AppConstants.lightGreen)
                    .padding(dimensionResource(R.dimen.padding_medium))
            )
        }
    }
}