package com.nanoporetech.scainter.ui.hospitalisation

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.nanoporetech.scainter.R
import com.nanoporetech.scainter.conf.AppConstants
import com.nanoporetech.scainter.data.DataSource
import com.nanoporetech.scainter.model.Hospitalisation
import com.nanoporetech.scainter.ui.components.LoadingScreen
import com.nanoporetech.scainter.ui.utils.capitalized
import com.nanoporetech.scainter.ui.utils.displayedDateAndTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HospitalisationListScreen(
    hospitalisations: List<Hospitalisation>,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    onRowClick: (Hospitalisation) -> Unit = {},
    onRefresh: () -> Unit = {},
) {
    PullToRefreshBox(
        isRefreshing = isLoading,
        onRefresh = onRefresh,
        modifier = modifier.fillMaxSize()
    ) {
        when {
            isLoading && hospitalisations.isEmpty() -> {
                LoadingScreen()
            }
            hospitalisations.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.no_recent_hospitalisation),
                        style = MaterialTheme.typography.headlineMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                    )
                }
            }
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    items(hospitalisations) { hospitalisation ->
                        HospitalisationRowItem(
                            hospitalisation = hospitalisation,
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
    }
}

@Composable
fun HospitalisationRowItem(
    hospitalisation: Hospitalisation,
    modifier: Modifier = Modifier,
    onRowClick: (Hospitalisation) -> Unit,
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
                    onClick = { onRowClick(hospitalisation) }
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
                        text = hospitalisation.fullname,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Text(
                    text = "Hospitalisation ${hospitalisation.type.capitalized()}",
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )

                Text(
                    text = displayedDateAndTime(hospitalisation.creationDate),
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
fun HospitalisationListScreenPreview() {
    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        HospitalisationListScreen(
            hospitalisations = DataSource.hospitalisations(),
            isLoading = false,
            //hospitalisations = emptyList(),
            modifier = Modifier
                .fillMaxSize()
                .background(color = AppConstants.lightGreen)
                .padding(dimensionResource(R.dimen.padding_medium))
        )
    }
}