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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
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
import com.nanoporetech.scainter.ui.theme.ScaInterAppTheme
import com.nanoporetech.scainter.ui.utils.displayedDate

@Composable
fun PolicyHolderDetailsScreen(
    policyHolder: PolicyHolder,
    modifier: Modifier = Modifier
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

        // OPTIONS

    }
}

@Composable
fun InsuranceInfo(
    policyHolder: PolicyHolder,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        CardItem(stringResource(R.string.policy_status_label),
            policyHolder.insuranceStatus),
        CardItem(stringResource(R.string.subscriber_status_label),
            policyHolder.providerStatus),
        CardItem(stringResource(R.string.coverage_label),
            policyHolder.coverExternal),
        CardItem(stringResource(R.string.last_consultation_label),
            displayedDate(policyHolder.lastConsultationDate)
        ),
        CardItem(stringResource(R.string.type_label),
            policyHolder.lastConsultationType),
        CardItem(stringResource(R.string.provider_label),
            policyHolder.providerDisplayedName)
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
fun PolicyHolderDetailsScreenPreview() {
    ScaInterAppTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
        ) {
            PolicyHolderDetailsScreen(
                policyHolder = DataSource.policyHolders().first(),
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = AppConstants.lightGreen)
                    .padding(dimensionResource(R.dimen.padding_medium))
            )
        }
    }
}