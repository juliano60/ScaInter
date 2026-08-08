package com.nanoporetech.scainter.ui.consultation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nanoporetech.scainter.R
import com.nanoporetech.scainter.conf.AppConstants
import com.nanoporetech.scainter.data.DataSource
import com.nanoporetech.scainter.model.Consultation
import com.nanoporetech.scainter.ui.theme.ScaInterAppTheme
import com.nanoporetech.scainter.ui.utils.displayedDateAndTime

@Composable
fun ConsultationListScreen(
    providerName: String,
    modifier: Modifier = Modifier,
    onRowClick: (Consultation) -> Unit = {},
    viewModel: ListConsultationsViewModel = viewModel(
        factory = ListConsultationsViewModel.provideFactory(
            providerName = providerName
        )
    )
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ConsultationListContent(
        consultations = uiState.consultations,
        isLoading = uiState.isLoading,
        modifier = modifier,
        onRowClick = onRowClick
    )
}

@Composable
fun ConsultationListContent(
    consultations: List<Consultation>,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    onRowClick: (Consultation) -> Unit = {},
) {
    if (isLoading) {
        Box(
            modifier = modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else if (consultations.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.no_recent_consultation),
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
            )
        }
    } else {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
        ) {
            items(consultations) { consultation  ->
                ConsultationRowItem(
                    consultation = consultation,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(dimensionResource(R.dimen.padding_small)),
                    onRowClick = onRowClick
                )
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.surfaceDim
                )
            }
        }
    }
}

@Composable
fun ConsultationRowItem(
    consultation: Consultation,
    modifier: Modifier = Modifier,
    onRowClick: (Consultation) -> Unit,
) {
    val paddingSmall = dimensionResource(R.dimen.padding_xsmall)
    val paddingMedium = dimensionResource(R.dimen.padding_medium)

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
        elevation = CardDefaults.cardElevation(defaultElevation = dimensionResource(R.dimen.elevation_small)),
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(paddingMedium)
                .clickable(
                    onClick = { onRowClick(consultation) }
                )
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(paddingSmall)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null
                    )
                    Spacer(Modifier.width(paddingSmall))

                    Text(
                        text = consultation.fullname,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Text(
                    text = consultation.act,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )

                Text(
                    text = displayedDateAndTime(consultation.creationDate),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(Modifier.weight(1.0f))

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null
            )
        }
    }
}

@Preview(
    locale = "fr-rCI",
    showBackground = true)
@Composable
fun ConsultationListScreenPreview() {
    ScaInterAppTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
        ) {
            ConsultationListContent(
                consultations = DataSource.consultations(),
                isLoading = false,
                //consultations = emptyList(),
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = AppConstants.lightGreen)
                    .padding(dimensionResource(R.dimen.padding_medium))
            )
        }
    }
}
