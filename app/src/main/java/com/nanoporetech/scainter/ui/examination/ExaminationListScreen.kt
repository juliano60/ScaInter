package com.nanoporetech.scainter.ui.examination

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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.nanoporetech.scainter.R
import com.nanoporetech.scainter.data.DataSource
import com.nanoporetech.scainter.model.Examination
import com.nanoporetech.scainter.ui.utils.displayedDateAndTime

@Composable
fun ExaminationListScreen(
    examinations: List<Examination>,
    modifier: Modifier = Modifier,
    onRowClick: (Examination) -> Unit = {},
) {
    if (examinations.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.no_recent_examination),
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
            )
        }
    } else {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
            //.safeContentPadding()
            //.statusBarsPadding()
        ) {
            items(examinations) { examination  ->
                ExaminationRowItem(
                    examination = examination,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(dimensionResource(R.dimen.padding_small)),
                    onRowClick = onRowClick
                )
                HorizontalDivider()
            }
        }
    }
}

@Composable
fun ExaminationRowItem(
    examination: Examination,
    modifier: Modifier = Modifier,
    onRowClick: (Examination) -> Unit,
) {
    val paddingSmall = dimensionResource(R.dimen.padding_xsmall)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clickable(
                onClick = { onRowClick(examination) }
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
                    text = examination.fullname,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Text(
                text = examination.displayedReason.ifBlank { stringResource(R.string.not_available) },
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Text(
                text = displayedDateAndTime(examination.creationDate),
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

@Preview(
    locale = "fr-rCI",
    showBackground = true)
@Composable
fun ExaminationListScreenPreview() {
    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        ExaminationListScreen(
            examinations = DataSource.examinations()
            //examinations = emptyList()
        )
    }
}