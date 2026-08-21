package com.nanoporetech.scainter.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.nanoporetech.scainter.R
import com.nanoporetech.scainter.ui.theme.ScaInterAppTheme

data class ExaminationCardItem(
    val label: String = "",
    val value: String = "",
)

@Composable
fun ExaminationCardBody(
    items: List<ExaminationCardItem>,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        for (item in items) {
            Row(modifier = Modifier
                .fillMaxWidth()
            ) {
                Text(
                    text = item.label.uppercase(),
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Row(modifier = Modifier
                .fillMaxWidth()
            ) {
                Text(
                    text = item.value.uppercase(),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontWeight = FontWeight.Normal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Preview(
    locale = "fr-rCI",
    showBackground = true)
@Composable
fun ExaminationCardBodyPreview() {
    ScaInterAppTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(R.dimen.padding_medium))
        ) {
            val items = listOf<ExaminationCardItem>(
                ExaminationCardItem(
                    label = "Label 1",
                    value = "Value 1"
                ),
                ExaminationCardItem(
                    label = "Label 2",
                    value = "Value 2"
                )
            )

            ExaminationCardBody(
                items = items
            )
        }
    }
}