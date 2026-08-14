package com.nanoporetech.scainter.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Payments
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
import com.nanoporetech.scainter.ui.theme.ScaInterAppTheme
import com.nanoporetech.scainter.ui.utils.formatCurrency


@Composable
fun CostsFragment(
    costTotal: String,
    costSca: String,
    costUser: String,
    modifier: Modifier = Modifier
) {
    val paddingMedium = dimensionResource(R.dimen.padding_medium)
    val items = listOf(
        CardItem(stringResource(R.string.total_cost_label), formatCurrency(costTotal.toDoubleOrNull() ?: 0.0)),
        CardItem(stringResource(R.string.total_sca_label), formatCurrency(costSca.toDoubleOrNull() ?: 0.0)),
        CardItem(stringResource(R.string.total_holder_label), formatCurrency(costUser.toDoubleOrNull() ?: 0.0))
    )

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
                title = stringResource(R.string.total_cost_title),
                iconImg = Icons.Filled.Payments,
                modifier = Modifier
                    .padding(bottom = paddingMedium)
            )

            CardBody(
                items = items,
                indentRight = true
            )
        }
    }
}

@Preview(
    locale = "fr-rCI",
    showBackground = true,
)
@Composable
fun CostsFragmentPreview() {
    ScaInterAppTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
        ) {
            CostsFragment(
                costTotal = "100000.0",
                costSca = "80000.0",
                costUser = "20000.0",
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(dimensionResource(R.dimen.padding_medium))
            )
        }
    }
}