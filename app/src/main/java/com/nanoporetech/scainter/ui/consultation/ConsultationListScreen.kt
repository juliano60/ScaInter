package com.nanoporetech.scainter.ui.consultation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.tooling.preview.Preview
import com.nanoporetech.scainter.R
import com.nanoporetech.scainter.data.DataSource
import com.nanoporetech.scainter.model.Consultation
import com.nanoporetech.scainter.ui.utils.displayedDateAndTime

@Composable
fun ConsultationListScreen(
    consultations: List<Consultation>,
    modifier: Modifier = Modifier,
    onRowClick: (Consultation) -> Unit = {},
) {
    Column(modifier = modifier
    ) {
        if (consultations.isEmpty()) {
            Text(text = "noRecentConsultation")
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    //.safeContentPadding()
                    //.statusBarsPadding()
            ) {
                items(consultations) { consultation  ->
                    ConsultationRowItem(
                        consultation = consultation,
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
}

@Composable
fun ConsultationRowItem(
    consultation: Consultation,
    modifier: Modifier = Modifier,
    onRowClick: (Consultation) -> Unit,
) {
    val paddingSmall = dimensionResource(R.dimen.padding_xsmall)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
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
                    //.border(1.dp, color = Color.Yellow)
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
                    maxLines = 1
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

@Preview(
    locale = "fr-rCI",
    showBackground = true)
@Composable
fun ConsultationListPreview() {
    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        ConsultationListScreen(
            consultations = DataSource.consultations()
            //consultations = emptyList()
        )
    }
}